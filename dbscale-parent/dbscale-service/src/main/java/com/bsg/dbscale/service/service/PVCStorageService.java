package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmStorageclassBody;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.model.CmStorageclass;
import com.bsg.dbscale.cm.query.CmStorageclassQuery;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.PVCStorageCheck;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.PVCStorageDTO;
import com.bsg.dbscale.service.form.PVCStorageForm;
import com.bsg.dbscale.service.query.PVCStorageQuery;

@Service
public class PVCStorageService extends BaseService {

    @Autowired
    private PVCStorageCheck storageCheck;

    public Result list(PVCStorageQuery storageQuery) throws Exception {
        List<PVCStorageDTO> storageDTOs = new ArrayList<>();
        CmStorageclassQuery cmStorageclassQuery = new CmStorageclassQuery();
        cmStorageclassQuery.setSiteId(storageQuery.getSiteId());
        cmStorageclassQuery.setUnschedulable(BooleanUtils.negate(storageQuery.getEnabled()));

        List<CmStorageclass> cmStorageclasses = CmApi.listStorageclass(cmStorageclassQuery);
        if (cmStorageclasses.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmStorageclass cmStorageclass : cmStorageclasses) {
                PVCStorageDTO storageDTO = getShowDTO(cmStorageclass, dictTypeDOs);
                storageDTOs.add(storageDTO);
            }
        }

        return Result.success(storageDTOs);
    }

    public Result get(String storageId) throws Exception {
        PVCStorageDTO storageDTO = null;
        CmStorageclass cmStorageclass = CmApi.getStorageclass(storageId);
        if (cmStorageclass != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            storageDTO = getShowDTO(cmStorageclass, dictTypeDOs);
        }

        return Result.success(storageDTO);
    }

    public Result save(PVCStorageForm storageForm) throws Exception {
        CheckResult checkResult = storageCheck.checkSave(storageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmStorageclassBody storageclassBody = new CmStorageclassBody();
        storageclassBody.setSiteId(storageForm.getSiteId());
        storageclassBody.setName(storageForm.getName());
        storageclassBody.setProvisioner(storageForm.getProvisioner());
        storageclassBody.setUnschedulable(BooleanUtils.negate(storageForm.getEnabled()));
        storageclassBody.setDesc(StringUtils.trimToEmpty(storageForm.getDescription()));
        CmApi.saveStorageclass(storageclassBody);

        return Result.success();
    }

    public Result update(String storageId, PVCStorageForm storageForm) throws Exception {
        CheckResult checkResult = storageCheck.checkUpdate(storageId, storageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmStorageclassBody storageclassBody = new CmStorageclassBody();
        if (storageForm.getName() != null) {
            storageclassBody.setName(storageForm.getName());
        }

        if (storageForm.getProvisioner() != null) {
            storageclassBody.setProvisioner(storageForm.getProvisioner());
        }

        if (storageForm.getDescription() != null) {
            storageclassBody.setDesc(storageForm.getDescription());
        }

        CmApi.updateStorageclass(storageId, storageclassBody);

        return Result.success();
    }

    public Result enabled(String storageId, boolean enabled) throws Exception {
        CheckResult checkResult = storageCheck.checkEnabled(storageId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (enabled) {
            CmApi.enabledStorageclass(storageId);
        } else {
            CmApi.disabledStorageclass(storageId);
        }

        return Result.success();
    }

    public Result remove(String storageId) throws Exception {
        CheckResult checkResult = storageCheck.checkRemove(storageId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmApi.removeStorageclass(storageId);
        ;

        return Result.success();
    }

    private PVCStorageDTO getShowDTO(CmStorageclass cmStorageclass, List<DictTypeDO> dictTypeDOs) {
        PVCStorageDTO storageDTO = new PVCStorageDTO();
        storageDTO.setId(cmStorageclass.getId());
        storageDTO.setName(cmStorageclass.getName());
        storageDTO.setProvisioner(cmStorageclass.getProvisioner());

        DisplayDTO reclaimPolicyDisplayDTO = new DisplayDTO();
        reclaimPolicyDisplayDTO.setCode(cmStorageclass.getReclaimPolicy());
        DictDO reclaimPolicyDictDO = findDictDO(dictTypeDOs, DictTypeConsts.RECLAIM_POLICY,
                cmStorageclass.getReclaimPolicy());
        if (reclaimPolicyDictDO != null) {
            reclaimPolicyDisplayDTO.setDisplay(reclaimPolicyDictDO.getName());
        }
        storageDTO.setReclaimPolicy(reclaimPolicyDisplayDTO);

        storageDTO.setEnabled(BooleanUtils.negate(cmStorageclass.getUnschedulable()));
        storageDTO.setDescription(cmStorageclass.getDesc());
        storageDTO.setAllowVolumeExpansion(cmStorageclass.getAllowVolumeExpansion());

        if (StringUtils.isNotBlank(cmStorageclass.getVolumeBingdingMode())) {
            DisplayDTO volumeBingdingTypeDTO = new DisplayDTO();
            volumeBingdingTypeDTO.setCode(cmStorageclass.getVolumeBingdingMode());
            DictDO volumeBingdingDictDO = findDictDO(dictTypeDOs, DictTypeConsts.VOLUME_BINGDING_MODE,
                    cmStorageclass.getVolumeBingdingMode());
            if (volumeBingdingDictDO != null) {
                volumeBingdingTypeDTO.setDisplay(volumeBingdingDictDO.getName());
            }
            storageDTO.setVolumeBingdingType(volumeBingdingTypeDTO);
        }

        CmSiteBase cmSite = cmStorageclass.getSite();
        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            storageDTO.setSite(siteDTO);

            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }

        InfoDTO createdDTO = new InfoDTO();
        storageDTO.setCreated(createdDTO);
        createdDTO.setTimestamp(cmStorageclass.getCreatedAt());

        return storageDTO;
    }

    public ObjModel getObjModel(String remoteStorageId) throws Exception {
        CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(remoteStorageId);
        if (cmRemoteStorage != null) {
            return new ObjModel(cmRemoteStorage.getName(), cmRemoteStorage.getSite().getId());
        }
        return null;
    }
}
