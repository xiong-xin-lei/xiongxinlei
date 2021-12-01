package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.HostDO;
import com.bsg.dbscale.dao.query.HostQuery;
import com.bsg.dbscale.service.form.ClusterForm;

@Service
public class ClusterCheck extends BaseCheck {

    public CheckResult checkSave(ClusterForm clusterForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(clusterForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(clusterForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String clusterId, ClusterForm clusterForm) throws Exception {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(clusterForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(clusterId, clusterForm);

        return checkResult;
    }

    public CheckResult checkRemove(String clusterId) throws Exception {
        CmCluster cmCluster = CmApi.getCluster(clusterId);
        if (cmCluster == null) {
            String msg = "该集群不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmCluster.getUnschedulable())) {
            String msg = "该集群已启用，不能删除。";
            return CheckResult.failure(msg);
        }

        HostQuery hostQuery = new HostQuery();
        hostQuery.setClusterId(clusterId);
        List<HostDO> hostDOs = hostDAO.list(hostQuery);
        if (hostDOs.size() > 0) {
            String msg = "该集群已关联主机，不能删除。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkEnabled(String clusterId, boolean enabled) throws Exception {
        CmCluster cmCluster = CmApi.getCluster(clusterId);
        if (cmCluster == null) {
            String msg = "该集群不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(ClusterForm clusterForm) {
        if (StringUtils.isBlank(clusterForm.getBusinessAreaId())) {
            String msg = "业务区不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(clusterForm.getName())) {
            String msg = "集群名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(clusterForm.getHaTag())) {
            String msg = "高可用标签不能为空。";
            return CheckResult.failure(msg);
        }

        if (clusterForm.getImageTypes() == null || clusterForm.getImageTypes().size() == 0) {
            String msg = "软件类型不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(ClusterForm clusterForm) throws Exception {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(clusterForm.getBusinessAreaId());
        if (businessAreaDO == null) {
            String msg = "业务区不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(businessAreaDO.getEnabled())) {
            String msg = "该业务区已停用。";
            return CheckResult.failure(msg);
        }

        CmClusterQuery clusterQuery = new CmClusterQuery();
        clusterQuery.setSiteId(businessAreaDO.getSiteId());
        clusterQuery.setName(clusterForm.getName());
        List<CmCluster> cmClusters = CmApi.listCluster(clusterQuery);
        if (cmClusters != null && cmClusters.size() > 0) {
            String msg = "该站点下集群名已存在。";
            return CheckResult.failure(msg);
        }

        List<DefServDO> defServDOs = defServDAO.list(null);
        List<String> imageTypes = clusterForm.getImageTypes();
        for (String imageType : imageTypes) {
            DefServDO defServDO = findDefServDO(defServDOs, imageType);
            if (defServDO == null) {
                String msg = imageType + "软件类型不存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(ClusterForm clusterForm) {
        if (StringUtils.equals(StringUtils.EMPTY, clusterForm.getBusinessAreaId())) {
            String msg = "业务区不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, clusterForm.getName())) {
            String msg = "集群名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, clusterForm.getHaTag())) {
            String msg = "高可用标签不能为空。";
            return CheckResult.failure(msg);
        }

        if (clusterForm.getImageTypes() != null && clusterForm.getImageTypes().size() == 0) {
            String msg = "软件类型不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String clusterId, ClusterForm clusterForm) throws Exception {
        CmCluster cmCluster = CmApi.getCluster(clusterId);
        if (cmCluster == null) {
            String msg = "该集群不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmCluster.getUnschedulable())) {
            String msg = "该集群已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(clusterForm.getBusinessAreaId())) {
            if (!cmCluster.getZone().equals(clusterForm.getBusinessAreaId())) {
                BusinessAreaDO newBusinessAreaDO = businessAreaDAO.get(clusterForm.getBusinessAreaId());
                if (newBusinessAreaDO == null) {
                    String msg = "该业务区不存在。";
                    return CheckResult.failure(msg);
                }

                if (BooleanUtils.isFalse(newBusinessAreaDO.getEnabled())) {
                    String msg = "该业务区已停用。";
                    return CheckResult.failure(msg);
                }
            }
        }

        if (StringUtils.isNotBlank(clusterForm.getName())) {
            if (!cmCluster.getName().equals(clusterForm.getName())) {
                CmClusterQuery clusterQuery = new CmClusterQuery();
                clusterQuery.setSiteId(cmCluster.getSite().getId());
                clusterQuery.setName(clusterForm.getName());
                List<CmCluster> cmClusters = CmApi.listCluster(clusterQuery);
                if (cmClusters != null && cmClusters.size() > 0) {
                    String msg = "该站点下集群名已存在。";
                    return CheckResult.failure(msg);
                }
            }
        }

        List<DefServDO> defServDOs = defServDAO.list(null);
        List<String> imageTypes = clusterForm.getImageTypes();
        if (imageTypes != null) {
            for (String imageType : imageTypes) {
                DefServDO defServDO = findDefServDO(defServDOs, imageType);
                if (defServDO == null) {
                    String msg = imageType + "软件类型不存在。";
                    return CheckResult.failure(msg);
                }
            }
        }

        return CheckResult.success();
    }
}
