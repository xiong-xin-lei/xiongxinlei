package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmImage;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.ImageForm;

@Service
public class ImageCheck extends BaseCheck {

    public CheckResult checkSave(ImageForm imageForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(imageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(imageForm);

        return checkResult;
    }

    public CheckResult checkRemove(String imageId) throws Exception {
        CmImage cmImage = CmApi.getImage(imageId);
        if (cmImage == null) {
            String msg = "该镜像不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmImage.getUnschedulable())) {
            String msg = "该镜像已启用，不能删除。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkEnabled(String imageId, boolean enabled) throws Exception {
        CmImage cmImage = CmApi.getImage(imageId);
        if (cmImage == null) {
            String msg = "该镜像不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(ImageForm imageForm) {
        if (StringUtils.isBlank(imageForm.getSiteId())) {
            String msg = "所属站点不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(imageForm.getArch())) {
            String msg = "架构不能为空。";
            return CheckResult.failure(msg);
        }

        if (imageForm.getMajor() == null || imageForm.getMinor() == null || imageForm.getPatch() == null
                || imageForm.getBuild() == null) {
            String msg = "版本不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(ImageForm imageForm) throws Exception {
        CmSite cmSite = CmApi.getSite(imageForm.getSiteId());
        if (cmSite == null) {
            String msg = "站点不存在。";
            return CheckResult.failure(msg);
        }

        DictDO dictDO = dictDAO.get(DictTypeConsts.SYS_ARCHITECTURE, imageForm.getArch());
        if (dictDO == null) {
            String msg = "架构不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

}
