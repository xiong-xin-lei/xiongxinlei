package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmImage;
import com.bsg.dbscale.cm.query.CmImageQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.OrderForm;
import com.bsg.dbscale.service.form.OrderGroupForm;
import com.bsg.dbscale.service.form.VersionForm;

@Service
public class OrderGroupCheck extends BaseCheck {

    public CheckResult checkSave(OrderGroupForm orderGroupForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(orderGroupForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(orderGroupForm);

        return checkResult;
    }

    private CheckResult checkSaveNonLogic(OrderGroupForm orderGroupForm) {
        String category = orderGroupForm.getCategory();
        if (!StringUtils.equals(category, Consts.SERV_GROUP_TYPE_MYSQL)
                && !StringUtils.equals(category, Consts.SERV_GROUP_TYPE_REDIS)
                && !StringUtils.equals(category, Consts.SERV_GROUP_TYPE_CMHA)) {
            String msg = "工单组类型不存在。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(orderGroupForm.getName())) {
            String msg = "服务名称不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(orderGroupForm.getBusinessAreaId())) {
            String msg = "所属业务区不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(orderGroupForm.getBusinessSubsystemId())) {
            String msg = "业务子系统不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(orderGroupForm.getSysArchitecture())) {
            String msg = "硬件架构不能为空。";
            return CheckResult.failure(msg);
        }

        List<OrderForm> orderForms = orderGroupForm.getOrders();
        for (OrderForm orderForm : orderForms) {
            if (StringUtils.equals(category, Consts.SERV_GROUP_TYPE_REDIS)
                    && !StringUtils.equals(orderForm.getType(), Consts.SERV_TYPE_REDIS)
                    && !StringUtils.equals(orderForm.getType(), Consts.SERV_TYPE_SENTINEL)) {
                String msg = "工单类型不正确。";
                return CheckResult.failure(msg);
            }

            if (StringUtils.equals(category, Consts.SERV_GROUP_TYPE_REDIS)
                    && StringUtils.equals(orderForm.getType(), Consts.SERV_TYPE_REDIS)) {
                if (orderForm.getPort() == null) {
                    String msg = "端口不能为空。";
                    return CheckResult.failure(msg);
                }

                VersionForm versionForm = orderForm.getVersion();
                if (versionForm == null) {
                    String msg = "版本不能为空。";
                    return CheckResult.failure(msg);
                }

                if (StringUtils.isBlank(orderForm.getArchMode()) || orderForm.getUnitCnt() == null) {
                    String msg = "架构不能为空。";
                    return CheckResult.failure(msg);
                }

                if (orderForm.getCpuCnt() == null || orderForm.getMemSize() == null) {
                    String msg = "规模不能为空。";
                    return CheckResult.failure(msg);
                }

                if (orderForm.getDataSize() == null) {
                    String msg = "表空间不能为空。";
                    return CheckResult.failure(msg);
                }
            }

        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(OrderGroupForm orderGroupForm) throws Exception {
        int nameCnt = orderGroupDAO.countByName(orderGroupForm.getName());
        if (nameCnt > 0) {
            String msg = "该名称已存在。";
            return CheckResult.failure(msg);
        }

        BusinessAreaDO businessAreaDO = businessAreaDAO.get(orderGroupForm.getBusinessAreaId());
        if (businessAreaDO == null) {
            String msg = "所属业务区不存在。";
            return CheckResult.failure(msg);
        }
        if (BooleanUtils.isFalse(businessAreaDO.getEnabled())) {
            String msg = "该业务区已停用。";
            return CheckResult.failure(msg);
        }

        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(orderGroupForm.getBusinessSubsystemId());
        if (businessSubsystemDO == null) {
            String msg = "该业务子系统不存在。";
            return CheckResult.failure(msg);
        }
        if (BooleanUtils.isFalse(businessSubsystemDO.getEnabled())) {
            String msg = "该业务子系统已停用。";
            return CheckResult.failure(msg);
        }

        DictDO sysArchitectureDictDO = dictDAO.get(DictTypeConsts.SYS_ARCHITECTURE,
                orderGroupForm.getSysArchitecture());
        if (sysArchitectureDictDO == null) {
            String msg = "硬件架构不存在。";
            return CheckResult.failure(msg);
        }

        List<OrderForm> orderForms = orderGroupForm.getOrders();
        for (OrderForm orderForm : orderForms) {
            CmImageQuery imageQuery = new CmImageQuery();
            imageQuery.setSiteId(businessAreaDO.getSiteId());
            imageQuery.setType(orderForm.getType());
            imageQuery.setUnschedulable(false);
            List<CmImage> cmImages = CmApi.listImage(imageQuery);

            VersionForm versionForm = orderForm.getVersion();
            Integer major = null;
            Integer minor = null;
            Integer patch = null;
            Integer build = null;
            if (versionForm != null) {
                major = versionForm.getMajor();
                minor = versionForm.getMinor();
                patch = versionForm.getPatch();
                build = versionForm.getBuild();
            }
            CmImage cmImage = CmApi.findLatestImage(cmImages, orderForm.getType(), major, minor, patch, build);

            String type = orderForm.getType();
            if (StringUtils.equals(orderGroupForm.getCategory(), Consts.SERV_GROUP_TYPE_REDIS)
                    && StringUtils.equals(orderForm.getType(), Consts.SERV_TYPE_REDIS)) {
                type = "";
            }
            if (cmImage == null) {
                String msg = type + "版本不存在。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }

    public CheckResult checkExamine(String orderGroupId) {
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        if (orderGroupDO == null) {
            String msg = "工单不存在。";
            return CheckResult.failure(msg);
        }

        if (!orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVED)) {
            String msg = "只能审批状态为未审批的工单。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkExecute(String orderGroupId) {
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        if (orderGroupDO == null) {
            String msg = "工单不存在。";
            return CheckResult.failure(msg);
        }

        if (!(orderGroupDO.getState().equals(DictConsts.ORDER_STATE_APPROVED)
                || orderGroupDO.getState().equals(DictConsts.ORDER_STATE_FAILED))) {
            String msg = "只能执行状态为审批通过或执行失败的工单。";
            return CheckResult.failure(msg);
        }

        ServGroupDO servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());
        if (servGroupDO != null) {
            TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_SERV_GROUP, servGroupDO.getId(), null);
            if (taskDO != null) {
                if (taskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                    String msg = "任务执行中，禁止其他操作。";
                    return CheckResult.failure(msg);
                }
            }
        }

        return CheckResult.success();
    }

    public CheckResult checkRemove(String orderGroupId) {
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        if (orderGroupDO == null) {
            String msg = "工单不存在。";
            return CheckResult.failure(msg);
        }

        if (!(orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVED)
                || orderGroupDO.getState().equals(DictConsts.ORDER_STATE_APPROVED)
                || orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVE))) {
            String msg = "只能删除状态为未审批或审批通过或审批拒绝的工单。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

}
