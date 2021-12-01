package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmHost;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.HostDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.HostForm;
import com.bsg.dbscale.service.form.VolumePathForm;

@Service
public class HostCheck extends BaseCheck {

    public CheckResult checkSave(HostForm hostForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(hostForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(hostForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String hostId, HostForm hostForm) throws Exception {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(hostForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(hostId, hostForm);

        return checkResult;
    }

    public CheckResult checkIn(String hostId) throws Exception {
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO == null) {
            String msg = "该主机不存在。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(hostDO.getRelateId())) {
            String msg = "请先出库，再入库。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkOut(String hostId) throws Exception {
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO == null) {
            String msg = "该主机不存在。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(hostDO.getRelateId())) {
            String msg = "该主机未入库，不能出库。";
            return CheckResult.failure(msg);
        }

        TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_HOST, hostId, null);
        if (taskDO != null) {
            if (taskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                String msg = "任务执行中，禁止其他操作。";
                return CheckResult.failure(msg);
            }
        }

        CmHost cmHost = CmApi.getHost(hostDO.getRelateId());
        if (cmHost != null) {
            CmHost.Status cmStatus = cmHost.getStatus();
            if (cmStatus != null) {
                if (cmStatus.getPhase().equals(CmConsts.HOST_PHASE_READY)
                        && BooleanUtils.negate(cmStatus.getUnschedulable())) {
                    String msg = "请先停用主机，然后再出库。";
                    return CheckResult.failure(msg);
                }

                Integer allocatable = cmStatus.getAllocatable().getUnit();
                Integer capacity = cmStatus.getCapacity().getUnit();
                if (capacity != null && allocatable != null && capacity.intValue() - allocatable.intValue() > 0) {
                    String msg = "该主机上存在单元信息，无法出库。";
                    return CheckResult.failure(msg);
                }
            }
        }

        return CheckResult.success();
    }

    public CheckResult checkRemove(String hostId) throws Exception {
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO == null) {
            String msg = "该主机不存在。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(hostDO.getRelateId())) {
            String msg = "请先执行出库操作，然后在注销。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkEnabled(String hostId, boolean enabled) throws Exception {
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO == null) {
            String msg = "该主机不存在。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(hostDO.getRelateId())) {
            String msg = "该主机未入库，不能启用或停用。";
            return CheckResult.failure(msg);
        }

        TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_HOST, hostId, DictConsts.ACTION_TYPE_IN);
        if (taskDO == null || !taskDO.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
            String msg = "该主机未入库成功，不能启用或停用。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(HostForm hostForm) {
        if (StringUtils.isBlank(hostForm.getClusterId())) {
            String msg = "集群不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(hostForm.getIp())) {
            String msg = "主机IP不能为空。";
            return CheckResult.failure(msg);
        }

        if (hostForm.getMaxUsage() == null) {
            String msg = "最大使用率不能为空。";
            return CheckResult.failure(msg);
        }
        if (hostForm.getMaxUsage() <= 0 || hostForm.getMaxUsage() > 100) {
            String msg = "资源最大分配率只能在0~100之间。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(hostForm.getRole())) {
            String msg = "角色不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(HostForm hostForm) throws Exception {
        CmCluster cmCluster = CmApi.getCluster(hostForm.getClusterId());
        if (cmCluster == null) {
            String msg = "该集群不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(cmCluster.getUnschedulable())) {
            String msg = "该集群已停用。";
            return CheckResult.failure(msg);
        }

        int cnt = hostDAO.countByIp(hostForm.getIp());
        if (cnt > 0) {
            String msg = "该IP地址已存在。";
            return CheckResult.failure(msg);
        }

        CmSiteBase cmSiteBase = cmCluster.getSite();
        if (cmSiteBase == null) {
            String msg = "集群信息错误。";
            return CheckResult.failure(msg);
        }
        CmSite cmSite = CmApi.getSite(cmSiteBase.getId());
        CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
        if (kubernetes == null) {
            String msg = "站点信息错误。";
            return CheckResult.failure(msg);
        }
        if (kubernetes.getStorageMode().equals(CmConsts.STORAGE_TYPE_VOLUMEPATH)) {
            VolumePathForm volumePathForm = hostForm.getVolumePath();
            if (volumePathForm == null
                    || (volumePathForm.getHddPaths() == null || volumePathForm.getHddPaths().size() == 0)
                            && (volumePathForm.getSsdPaths() == null || volumePathForm.getSsdPaths().size() == 0)
                            && StringUtils.isBlank(volumePathForm.getRemoteStorageId())) {
                String msg = "机械盘设备、固态盘设备和外置存储不能全部为空。";
                return CheckResult.failure(msg);
            }
            if (StringUtils.isNotBlank(volumePathForm.getRemoteStorageId())) {
                CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(volumePathForm.getRemoteStorageId());
                if (cmRemoteStorage == null) {
                    String msg = "该外置存储不存在。";
                    return CheckResult.failure(msg);
                }

                if (BooleanUtils.isTrue(cmRemoteStorage.getUnschedulable())) {
                    String msg = "该外置存储已停用。";
                    return CheckResult.failure(msg);
                }
            }

        }

        List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
        DictDO hostRoleDictDO = findDictDO(dictTypeDOs, DictTypeConsts.HOST_ROLE, hostForm.getRole());
        if (hostRoleDictDO == null) {
            String msg = "该角色不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(HostForm hostForm) {
        if (hostForm.getClusterId() != null && StringUtils.isEmpty(hostForm.getClusterId())) {
            String msg = "集群不能为空。";
            return CheckResult.failure(msg);
        }

        if (hostForm.getIp() != null && StringUtils.isEmpty(hostForm.getIp())) {
            String msg = "IP地址不能为空。";
            return CheckResult.failure(msg);
        }

        if (hostForm.getRole() != null && StringUtils.isEmpty(hostForm.getRole())) {
            String msg = "角色不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String hostId, HostForm hostForm) throws Exception {
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO == null) {
            String msg = "该主机不存在。";
            return CheckResult.failure(msg);
        }

        if (hostForm.getIp() != null && !hostDO.getIp().equals(hostForm.getIp())) {
            int cnt = hostDAO.countByIp(hostForm.getIp());
            if (cnt > 0) {
                String msg = "该IP地址已存在。";
                return CheckResult.failure(msg);
            }
        }

        if (hostForm.getClusterId() != null && !hostDO.getClusterId().equals(hostForm.getClusterId())) {
            CmCluster cmCluster = CmApi.getCluster(hostForm.getClusterId());
            if (cmCluster == null) {
                String msg = "该集群不存在。";
                return CheckResult.failure(msg);
            }

            if (BooleanUtils.isTrue(cmCluster.getUnschedulable())) {
                String msg = "该集群已停用。";
                return CheckResult.failure(msg);
            }
        }
        if (hostForm.getRole() != null && !hostDO.getRole().equals(hostForm.getRole())) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            DictDO hostRoleDictDO = findDictDO(dictTypeDOs, DictTypeConsts.HOST_ROLE, hostForm.getRole());
            if (hostRoleDictDO == null) {
                String msg = "该角色不存在。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }
}
