package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmTopology;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.form.BackupForm;
import com.bsg.dbscale.service.form.UnitRebuildForm;
import com.bsg.dbscale.service.form.UnitRebuildInitForm;

@Service
public class UnitCheck extends BaseCheck {

    public CheckResult checkCommon(UnitDO unitDO) {
        if (unitDO == null) {
            String msg = "该单元不存在。";
            return CheckResult.failure(msg);
        }

        TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_UNIT, unitDO.getId(), null);
        if (taskDO != null) {
            if (taskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                String msg = "任务执行中，禁止其他操作。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

    public CheckResult checkStart(UnitDO unitDO) {
        return checkCommon(unitDO);
    }

    public CheckResult checkStop(UnitDO unitDO) {
        return checkCommon(unitDO);
    }

    public CheckResult checkRebuild(UnitDO unitDO, UnitRebuildForm unitRebuildForm) {
        CheckResult checkResult = checkCommon(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        ServDO servDO = servDAO.get(unitDO.getServId());
        if (servDO == null) {
            String msg = "未知单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        if (servGroupDO == null) {
            String msg = "未知单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        if (unitDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
            if (BooleanUtils.isNotTrue(unitRebuildForm.getForce())
                    && StringUtils.isBlank(unitRebuildForm.getBackupFileId())) {
                String msg = "备份文件不能为空。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

    public CheckResult checkRebuildInit(UnitDO unitDO, UnitRebuildInitForm unitRebuildInitForm) throws Exception {
        CheckResult checkResult = checkCommon(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }
        if (unitDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
            if (StringUtils.isBlank(unitRebuildInitForm.getBackupFileId())) {
                ServDO servDO = servDAO.get(unitDO.getServId());
                ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
                ServDO cmhaServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_CMHA);
                if (cmhaServDO == null) {
                    CmService cmService = CmApi.getServiceDetail(servDO.getRelateId());
                    CmService.Status.Unit cmMasterUnit = CmApi.findMasterUnit(cmService);
                    if (cmMasterUnit == null) {
                        String msg = "找不到主节点。";
                        return CheckResult.failure(msg);
                    }

                    if (cmMasterUnit.getId().equals(unitDO.getRelateId())) {
                        String msg = "不能在主节点上进行重建初始化操作。";
                        return CheckResult.failure(msg);
                    }

                    if (!cmMasterUnit.getPodState().equals(CmConsts.POD_STATE_RUNNING)) {
                        String msg = "主节点状态异常。";
                        return CheckResult.failure(msg);
                    }
                } else {
                    CmTopology cmTopology = CmApi.getTopology(cmhaServDO.getRelateId());
                    CmTopology.Node cmMasterNode = CmApi.findMasterUnit(cmTopology);
                    if (cmMasterNode == null) {
                        String msg = "找不到主节点。";
                        return CheckResult.failure(msg);
                    }
                    
                    if (cmMasterNode.getNode().equals(unitDO.getRelateId())) {
                        String msg = "不能在主节点上进行重建初始化操作。";
                        return CheckResult.failure(msg);
                    }
                    
                    if (!StringUtils.equalsIgnoreCase(cmMasterNode.getStatus(), CmConsts.STATE_ONLINE)) {
                        String msg = "主节点状态不是online，不能进行重建初始化操作。";
                        return CheckResult.failure(msg);
                    }
                }
            }
        }
        return CheckResult.success();
    }

    public CheckResult checkBackup(BackupForm backupForm, UnitDO unitDO) throws Exception {
        CheckResult checkResult = checkCommon(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        if (!unitDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
            String msg = "该类型单元不支持备份操作。";
            return CheckResult.failure(msg);
        }

        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        String businessAreaId = servGroupDO.getBusinessAreaId();

        if (CmConsts.BACKUP_STORAGE_TYPE_NFS.equals(backupForm.getBackupStorageType())) {
            CmNfsQuery nfsQuery = new CmNfsQuery();
            nfsQuery.setZone(businessAreaId);
            nfsQuery.setUnschedulable(false);
            List<CmNfs> cmNfs = CmApi.listNfs(nfsQuery);
            if (cmNfs == null || cmNfs.size() < 1) {
                String msg = "无符合条件的NFS。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

    public CheckResult checkRestore(UnitDO unitDO) {
        CheckResult checkResult = checkCommon(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        if (!unitDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
            String msg = "该类型单元不支持还原操作。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkResetMaster(UnitDO unitDO) {
        CheckResult checkResult = checkCommon(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        if (!unitDO.getType().equals(Consts.SERV_TYPE_MYSQL) && !unitDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
            String msg = "该类型单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        ServDO servDO = servDAO.get(unitDO.getServId());
        if (servDO == null) {
            String msg = "未知单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        if (servGroupDO == null) {
            String msg = "未知单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        List<UnitDO> unitDOs = servDO.getUnits();
        for (UnitDO unitDO2 : unitDOs) {
            if (!unitDO2.getId().equals(unitDO.getId())) {
                TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_UNIT, unitDO.getId(), null);
                if (taskDO != null) {
                    if (taskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                        String msg = "有其他单元在执行任务，禁止执行此操作。";
                        return CheckResult.failure(msg);
                    }
                }
            }
        }

        return CheckResult.success();
    }

    public CheckResult checkMaintenance(UnitDO unitDO) {
        CheckResult checkResult = checkCommon(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        if (!unitDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
            String msg = "该类型单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        ServDO servDO = servDAO.get(unitDO.getServId());
        if (servDO == null) {
            String msg = "未知单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        if (servGroupDO == null || servGroupDO.getServs().size() == 1) {
            String msg = "未知单元不支持此操作。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }
}
