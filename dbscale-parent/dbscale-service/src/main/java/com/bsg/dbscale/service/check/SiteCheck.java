package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.query.CmSiteQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.query.BusinessAreaQuery;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.SiteForm;

@Service
public class SiteCheck extends BaseCheck {

    public CheckResult checkSave(SiteForm siteForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(siteForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(siteForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String siteId, SiteForm siteForm) throws Exception {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(siteForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(siteId, siteForm);

        return checkResult;
    }

    public CheckResult checkRemove(String siteId) throws Exception {
        CmSite cmSite = CmApi.getSite(siteId);
        if (cmSite == null) {
            String msg = "该站点不存在。";
            return CheckResult.failure(msg);
        }

        BusinessAreaQuery businessAreaQuery = new BusinessAreaQuery();
        businessAreaQuery.setSiteId(siteId);
        List<BusinessAreaDO> businessAreaDOs = businessAreaDAO.list(businessAreaQuery);
        if (businessAreaDOs != null && businessAreaDOs.size() > 0) {
            String msg = "该站点下已存在关联业务区，无法删除此站点。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(SiteForm siteForm) {
        if (StringUtils.isBlank(siteForm.getName())) {
            String msg = "站点名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(siteForm.getRegion())) {
            String msg = "地域不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(siteForm.getKubeconfig())) {
            String msg = "证书不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(SiteForm siteForm) throws Exception {
        DictDO regionDictDO = dictDAO.get(DictTypeConsts.REGION, siteForm.getRegion());
        if (regionDictDO == null) {
            String msg = "地域不存在。";
            return CheckResult.failure(msg);
        }

        CmSiteQuery cmSiteQuery = new CmSiteQuery();
        cmSiteQuery.setName(siteForm.getName());
        List<CmSite> cmSites = CmApi.listSite(cmSiteQuery);
        if (cmSites != null && cmSites.size() > 0) {
            String msg = "该站点已存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(SiteForm siteForm) {
        if (StringUtils.equals(StringUtils.EMPTY, siteForm.getName())) {
            String msg = "站点名不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String siteId, SiteForm siteForm) throws Exception {
        CmSite cmSite = CmApi.getSite(siteId);
        if (cmSite == null) {
            String msg = "该站点不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }
}
