package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.OrderDO;
import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.query.OrderGroupQuery;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;

@Service
public class ServGroupCheck extends BaseCheck {

    public CheckResult checkCommon(ServGroupDO servGroupDO, String category) {
        if (servGroupDO == null) {
            String msg = "该服务不存在。";
            return CheckResult.failure(msg);
        }

        if (!StringUtils.equals(servGroupDO.getCategory(), category)) {
            String msg = "该服务类型不正确。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isNotTrue(servGroupDO.getFlag())) {
            String msg = "服务未创建成功，不能执行此操作。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkServGroupCommon(ServGroupDO servGroupDO, String category) {
        CheckResult checkResult = checkCommon(servGroupDO, category);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_SERV_GROUP, servGroupDO.getId(), null);
        if (taskDO != null) {
            if (taskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                String msg = "任务执行中，禁止其他操作。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

    public CheckResult checkStart(ServGroupDO servGroupDO, String category) {
        return checkServGroupCommon(servGroupDO, category);
    }

    public CheckResult checkStop(ServGroupDO servGroupDO, String category) {
        return checkServGroupCommon(servGroupDO, category);
    }

    public CheckResult checkImageUpdate(ServGroupDO servGroupDO, String category, ServImageForm imageForm) {
        CheckResult checkResult = checkServGroupCommon(servGroupDO, category);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        OrderGroupQuery orderGroupQuery = new OrderGroupQuery();
        orderGroupQuery.setName(servGroupDO.getName());
        orderGroupQuery.setCreateType(DictConsts.ORDER_CREATE_TYPE_IMAGE_UPDATE);
        List<OrderGroupDO> orderGroupDOs = orderGroupDAO.list(orderGroupQuery);
        for (OrderGroupDO orderGroupDO : orderGroupDOs) {
            if (!orderGroupDO.getState().equals(DictConsts.ORDER_STATE_SUCCESS)
                    && !orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVE)
                    && !orderGroupDO.getState().equals(DictConsts.ORDER_STATE_FAILED)) {
                List<OrderDO> orderDOs = orderGroupDO.getOrders();
                for (OrderDO orderDO : orderDOs) {
                    if (orderDO.getMajorVersion() != null && orderDO.getType().equals(imageForm.getType())) {
                        String msg = "该服务存在升级单未处理完成，禁止再次升级。";
                        return CheckResult.failure(msg);
                    }
                }
            }
        }
        return checkResult;
    }

    public CheckResult checkScaleUpCpuMem(ServGroupDO servGroupDO, String category,
            ServScaleCpuMemForm scaleCpuMemForm) {
        CheckResult checkResult = checkServGroupCommon(servGroupDO, category);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        OrderGroupQuery orderGroupQuery = new OrderGroupQuery();
        orderGroupQuery.setName(servGroupDO.getName());
        orderGroupQuery.setCreateType(DictConsts.ORDER_CREATE_TYPE_SCALE_UP_CPUMEM);
        List<OrderGroupDO> orderGroupDOs = orderGroupDAO.list(orderGroupQuery);
        for (OrderGroupDO orderGroupDO : orderGroupDOs) {
            if (!orderGroupDO.getState().equals(DictConsts.ORDER_STATE_SUCCESS)
                    && !orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVE)
                    && !orderGroupDO.getState().equals(DictConsts.ORDER_STATE_FAILED)) {
                List<OrderDO> orderDOs = orderGroupDO.getOrders();
                for (OrderDO orderDO : orderDOs) {
                    if (orderDO.getCpuCnt() != null && orderDO.getType().equals(scaleCpuMemForm.getType())) {
                        String msg = "该服务存在计算扩容单未处理完成，禁止再次扩容。";
                        return CheckResult.failure(msg);
                    }
                }
            }
        }
        return checkResult;
    }

    public CheckResult checkScaleUpStorage(ServGroupDO servGroupDO, String category,
            ServScaleStorageForm scaleStorageForm) {
        CheckResult checkResult = checkServGroupCommon(servGroupDO, category);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        OrderGroupQuery orderGroupQuery = new OrderGroupQuery();
        orderGroupQuery.setName(servGroupDO.getName());
        orderGroupQuery.setCreateType(DictConsts.ORDER_CREATE_TYPE_SCALE_UP_STORAGE);
        List<OrderGroupDO> orderGroupDOs = orderGroupDAO.list(orderGroupQuery);
        for (OrderGroupDO orderGroupDO : orderGroupDOs) {
            if (!orderGroupDO.getState().equals(DictConsts.ORDER_STATE_SUCCESS)
                    && !orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVE)) {
                List<OrderDO> orderDOs = orderGroupDO.getOrders();
                for (OrderDO orderDO : orderDOs) {
                    if (orderDO.getDataSize() != null && orderDO.getType().equals(scaleStorageForm.getType())) {
                        String msg = "该服务存在存储扩容单未处理完成或扩容失败，禁止再次扩容。";
                        return CheckResult.failure(msg);
                    }
                }
            }
        }
        return checkResult;
    }

    public CheckResult checkArchUp(ServGroupDO servGroupDO, String category, ServArchUpForm archUpForm) {
        CheckResult checkResult = checkServGroupCommon(servGroupDO, category);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        OrderGroupQuery orderGroupQuery = new OrderGroupQuery();
        orderGroupQuery.setName(servGroupDO.getName());
        orderGroupQuery.setCreateType(DictConsts.ORDER_CREATE_TYPE_ARCH_UP);
        List<OrderGroupDO> orderGroupDOs = orderGroupDAO.list(orderGroupQuery);
        for (OrderGroupDO orderGroupDO : orderGroupDOs) {
            if (!orderGroupDO.getState().equals(DictConsts.ORDER_STATE_SUCCESS)
                    && !orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVE)) {
                String msg = "该服务存在节点扩展单未处理完成，禁止再次进行节点扩展。";
                return CheckResult.failure(msg);
            }
        }
        return checkResult;
    }

    public CheckResult checkRemove(ServGroupDO servGroupDO, String category) {
        if (servGroupDO == null) {
            String msg = "该服务不存在。";
            return CheckResult.failure(msg);
        }

        if (!StringUtils.equals(servGroupDO.getCategory(), category)) {
            String msg = "该服务类型不正确。";
            return CheckResult.failure(msg);
        }

        // TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_SERV_GROUP,
        // servGroupDO.getId(), null);
        // if (taskDO != null) {
        // if (taskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
        // String msg = "任务执行中，禁止其他操作。";
        // return CheckResult.failure(msg);
        // }
        // }

        if (BooleanUtils.isTrue(servGroupDO.getFlag())) {
            OrderGroupQuery orderGroupQuery = new OrderGroupQuery();
            orderGroupQuery.setName(servGroupDO.getName());
            orderGroupQuery.setCreateType(DictConsts.ORDER_CREATE_TYPE_DELETE);
            List<OrderGroupDO> orderGroupDOs = orderGroupDAO.list(orderGroupQuery);
            for (OrderGroupDO orderGroupDO : orderGroupDOs) {
                if (orderGroupDO.getCreateType().equals(DictConsts.ORDER_CREATE_TYPE_DELETE)) {
                    if (!orderGroupDO.getState().equals(DictConsts.ORDER_STATE_SUCCESS)
                            && !orderGroupDO.getState().equals(DictConsts.ORDER_STATE_UNAPPROVE)) {
                        String msg = "该服务存在删除单未处理完成，禁止再次删除。";
                        return CheckResult.failure(msg);
                    }
                }
            }
        }

        return CheckResult.success();
    }

}
