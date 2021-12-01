package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmImageBody;
import com.bsg.dbscale.cm.body.CmImageTemplateBody;
import com.bsg.dbscale.cm.model.CmImage;
import com.bsg.dbscale.cm.model.CmImageTemplate;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmImageQuery;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.ImageCheck;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.ImageDTO;
import com.bsg.dbscale.service.dto.ImageTemplateDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.service.form.ImageForm;
import com.bsg.dbscale.service.form.ImageTemplateForm;
import com.bsg.dbscale.service.query.ImageQuery;

@Service
public class ImageService extends BaseService {

    @Autowired
    private ImageCheck imageCheck;

    public Result list(ImageQuery imageQuery) throws Exception {
        List<ImageDTO> imageDTOs = new ArrayList<>();
        CmImageQuery cmImageQuery = new CmImageQuery();
        cmImageQuery.setSiteId(imageQuery.getSiteId());
        cmImageQuery.setType(imageQuery.getType());
        cmImageQuery.setArch(imageQuery.getArchitecture());
        cmImageQuery.setMajor(imageQuery.getMajor());
        cmImageQuery.setMinor(imageQuery.getMinor());
        cmImageQuery.setUnschedulable(BooleanUtils.negate(imageQuery.getEnabled()));

        List<CmImage> cmImages = CmApi.listImage(cmImageQuery);
        if (cmImages != null && cmImages.size() > 0) {
            List<DefServDO> defServDOs = defServDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmImage cmImage : cmImages) {
                DefServDO defServDO = findDefServDO(defServDOs, cmImage.getType());
                ImageDTO imageDTO = getShowDTO(cmImage, defServDO, dictTypeDOs);
                imageDTOs.add(imageDTO);
            }
        }

        return Result.success(imageDTOs);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(ImageForm imageForm) throws Exception {
        CheckResult checkResult = imageCheck.checkSave(imageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmImageBody cmImageBody = new CmImageBody();
        cmImageBody.setSiteId(imageForm.getSiteId());
        cmImageBody.setArch(imageForm.getArch());
        cmImageBody.setType(imageForm.getType());
        cmImageBody.setMajor(imageForm.getMajor());
        cmImageBody.setMinor(imageForm.getMinor());
        cmImageBody.setPatch(imageForm.getPatch());
        cmImageBody.setBuild(imageForm.getBuild());
        cmImageBody.setExporterPort(imageForm.getExporterPort());
        cmImageBody.setUnschedulable(BooleanUtils.negate(imageForm.getEnabled()));
        cmImageBody.setDesc(imageForm.getDescription());
        CmApi.saveImage(cmImageBody);

        return Result.success();
    }

    public Result get(String imageId) throws Exception {
        ImageDTO imageDTO = null;
        CmImage cmImage = CmApi.getImage(imageId);
        if (cmImage != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            DefServDO defServDO = defServDAO.get(cmImage.getType());
            imageDTO = getShowDTO(cmImage, defServDO, dictTypeDOs);
        }
        return Result.success(imageDTO);
    }

    public Result enabled(String imageId, boolean enabled) throws Exception {
        CheckResult checkResult = imageCheck.checkEnabled(imageId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (enabled) {
            CmApi.enabledImage(imageId);
        } else {
            CmApi.disabledImage(imageId);
        }

        return Result.success();
    }

    public Result remove(String imageId) throws Exception {
        CheckResult checkResult = imageCheck.checkRemove(imageId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        CmApi.removeImage(imageId);

        return Result.success();
    }

    public Result listTempate(String siteId, String type, Integer major, Integer minor, Integer patch, Integer build,
            String architecture) throws Exception {
        List<ImageTemplateDTO> templateDTOs = new ArrayList<>();
        CmImageQuery cmImageQuery = new CmImageQuery();
        cmImageQuery.setSiteId(siteId);
        cmImageQuery.setType(type);
        cmImageQuery.setMajor(major);
        cmImageQuery.setMinor(minor);
        cmImageQuery.setPatch(patch);
        cmImageQuery.setBuild(build);
        cmImageQuery.setArch(architecture);
        List<CmImage> cmImages = CmApi.listImage(cmImageQuery);
        if (cmImages != null && cmImages.size() == 1) {
            CmImage cmImage = cmImages.get(0);
            CmImageTemplate cmImageTemplate = CmApi.getImageTemplate(cmImage.getId());
            if (cmImageTemplate != null) {
                CmImageTemplate.Template cmTemplate = cmImageTemplate.getTemplate();
                if (cmTemplate != null) {
                    List<CmImageTemplate.Template.Keyset> keysets = cmTemplate.getKeysets();
                    if (keysets != null) {
                        for (CmImageTemplate.Template.Keyset keyset : keysets) {
                            ImageTemplateDTO templateDTO = new ImageTemplateDTO();
                            templateDTOs.add(templateDTO);

                            templateDTO.setKey(keyset.getKey());
                            templateDTO.setValue(keyset.getValue());
                            templateDTO.setDefaultValue(keyset.getDefaultValue());
                            templateDTO.setRange(keyset.getRange());
                            templateDTO.setCanSet(keyset.getCanSet());
                            templateDTO.setMustRestart(keyset.getMustRestart());
                            templateDTO.setDescription(keyset.getDesc());
                        }
                    }
                }
            }
        }
        return Result.success(templateDTOs);
    }

    public Result updateTempate(String siteId, String type, Integer major, Integer minor, Integer patch, Integer build,
            String architecture, ImageTemplateForm imageTemplateForm) throws Exception {
        CmImageQuery cmImageQuery = new CmImageQuery();
        cmImageQuery.setSiteId(siteId);
        cmImageQuery.setType(type);
        cmImageQuery.setMajor(major);
        cmImageQuery.setMinor(minor);
        cmImageQuery.setPatch(patch);
        cmImageQuery.setBuild(build);
        cmImageQuery.setArch(architecture);
        List<CmImage> cmImages = CmApi.listImage(cmImageQuery);
        if (cmImages != null && cmImages.size() == 1) {
            CmImage cmImage = cmImages.get(0);

            CmImageTemplateBody cmImageTemplateBody = new CmImageTemplateBody();
            cmImageTemplateBody.setKey(imageTemplateForm.getKey());
            cmImageTemplateBody.setValue(imageTemplateForm.getValue());
            cmImageTemplateBody.setDefaultValue(imageTemplateForm.getDefaultValue());
            cmImageTemplateBody.setRange(imageTemplateForm.getRange());
            cmImageTemplateBody.setCanSet(imageTemplateForm.getCanSet());
            cmImageTemplateBody.setMustRestart(imageTemplateForm.getMustRestart());
            cmImageTemplateBody.setDesc(imageTemplateForm.getDescription());
            CmApi.updateImageTemplate(cmImage.getId(), cmImageTemplateBody);
        }

        return Result.success();
    }

    private ImageDTO getShowDTO(CmImage cmImage, DefServDO defServDO, List<DictTypeDO> dictTypeDOs) {
        if (cmImage == null) {
            return null;
        }
        ImageDTO imageDTO = new ImageDTO();

        imageDTO.setId(cmImage.getId());

        DisplayDTO typeDisplayDTO = new DisplayDTO();
        typeDisplayDTO.setCode(cmImage.getType());
        if (defServDO != null) {
            typeDisplayDTO.setDisplay(defServDO.getName());
            imageDTO.setStateful(defServDO.getStateful());
        }
        imageDTO.setType(typeDisplayDTO);

        VersionDTO version = new VersionDTO();
        version.setMajor(cmImage.getMajor());
        version.setMinor(cmImage.getMinor());
        version.setPatch(cmImage.getPatch());
        version.setBuild(cmImage.getBuild());
        imageDTO.setVersion(version);

        DisplayDTO archDisplayDTO = new DisplayDTO();
        archDisplayDTO.setCode(cmImage.getArch());
        DictDO archDictDO = findDictDO(dictTypeDOs, DictTypeConsts.SYS_ARCHITECTURE, cmImage.getArch());
        if (archDictDO != null) {
            archDisplayDTO.setDisplay(archDictDO.getName());
        }
        imageDTO.setArchitecture(archDisplayDTO);
        imageDTO.setEnabled(BooleanUtils.negate(cmImage.getUnschedulable()));
        imageDTO.setDescription(cmImage.getDesc());

        CmSiteBase cmSite = cmImage.getSite();
        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            imageDTO.setSite(siteDTO);

            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }

        imageDTO.setGmtCreated(cmImage.getCreatedAt());

        return imageDTO;
    }

    public ObjModel getObjModel(String imageId) throws Exception {
        CmImage cmImage = CmApi.getImage(imageId);
        if (cmImage != null) {
            String name = cmImage.getType() + cmImage.getMajor() + "." + cmImage.getMinor() + "." + cmImage.getPatch()
                    + "." + cmImage.getBuild();
            return new ObjModel(name, cmImage.getType(), cmImage.getSite().getId());
        }
        return null;
    }
}
