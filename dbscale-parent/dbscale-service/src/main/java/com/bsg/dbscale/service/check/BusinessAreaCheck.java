package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmNetwork;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.cm.query.CmNetworkQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.service.form.BusinessAreaForm;

@Service
public class BusinessAreaCheck extends BaseCheck {

    public CheckResult checkSave(BusinessAreaForm businessAreaForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(businessAreaForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(businessAreaForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String businessAreaId, BusinessAreaForm businessAreaForm) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(businessAreaForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(businessAreaId, businessAreaForm);

        return checkResult;
    }

    public CheckResult checkRemove(String businessAreaId) throws Exception {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(businessAreaId);
        if (businessAreaDO == null) {
            String msg = "该业务区不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(businessAreaDO.getEnabled())) {
            String msg = "该业务区已启用，不能删除。";
            return CheckResult.failure(msg);
        }

        CmClusterQuery cmClusterQuery = new CmClusterQuery();
        cmClusterQuery.setZone(businessAreaId);
        List<CmCluster> cmClusters = CmApi.listCluster(cmClusterQuery);
        if (cmClusters != null && cmClusters.size() > 0) {
            String msg = "该业务区已关联集群，不能删除。";
            return CheckResult.failure(msg);
        }

        CmNetworkQuery cmNetworkQuery = new CmNetworkQuery();
        cmNetworkQuery.setZone(businessAreaId);
        List<CmNetwork> cmNetworks = CmApi.listNetwork(cmNetworkQuery);
        if (cmNetworks != null && cmNetworks.size() > 0) {
            String msg = "该业务区已关联网段，不能删除。";
            return CheckResult.failure(msg);
        }

        int orderGroupCnt = orderGroupDAO.countByBusinessAreaId(businessAreaId);
        if (orderGroupCnt > 0) {
            String msg = "该业务区已关联工单，不能删除。";
            return CheckResult.failure(msg);
        }

        int servGroupCnt = servGroupDAO.countByBusinessAreaId(businessAreaId);
        if (servGroupCnt > 0) {
            String msg = "该业务区已关联服务，不能删除。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkEnabled(String businessAreaId, boolean enabled) {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(businessAreaId);
        if (businessAreaDO == null) {
            String msg = "该业务区不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(BusinessAreaForm businessAreaForm) {
        if (StringUtils.isBlank(businessAreaForm.getName())) {
            String msg = "业务区名不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(BusinessAreaForm businessAreaForm) throws Exception {
        CmSite cmSite = CmApi.getSite(businessAreaForm.getSiteId());
        if (cmSite == null) {
            String msg = "站点不存在。";
            return CheckResult.failure(msg);
        }

        BusinessAreaDO businessAreaDO = businessAreaDAO.getByNameAndSiteId(businessAreaForm.getName(),
                businessAreaForm.getSiteId());
        if (businessAreaDO != null) {
            String msg = "该站点下业务区名已存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(BusinessAreaForm businessAreaForm) {
        if (StringUtils.equals(StringUtils.EMPTY, businessAreaForm.getName())) {
            String msg = "业务区名不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String businessAreaId, BusinessAreaForm businessAreaForm) {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(businessAreaId);
        if (businessAreaDO == null) {
            String msg = "该业务区不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(businessAreaDO.getEnabled())) {
            String msg = "该业务区已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(businessAreaForm.getName())
                && !businessAreaForm.getName().equals(businessAreaDO.getName())) {
            businessAreaDO = businessAreaDAO.getByNameAndSiteId(businessAreaForm.getName(), businessAreaDO.getSiteId());
            if (businessAreaDO != null) {
                String msg = "该站点下业务区名已存在。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }
}
