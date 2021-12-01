package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmRemoteStorageBody;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmRemoteStorageQuery;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.RemoteStorageCheck;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.RemoteStorageDTO;
import com.bsg.dbscale.service.form.RemoteStorageForm;
import com.bsg.dbscale.service.query.RemoteStorageQuery;
import com.bsg.dbscale.util.NumberUnits;

@Service
public class RemoteStorageService extends BaseService {

    @Autowired
    private RemoteStorageCheck remoteStorageCheck;

    public Result list(RemoteStorageQuery remoteStorageQuery) throws Exception {
        List<RemoteStorageDTO> remoteStorageDTOs = new ArrayList<>();
        CmRemoteStorageQuery cmRemoteStorageQuery = new CmRemoteStorageQuery();
        cmRemoteStorageQuery.setSiteId(remoteStorageQuery.getSiteId());
        cmRemoteStorageQuery.setUnschedulable(BooleanUtils.negate(remoteStorageQuery.getEnabled()));

        List<CmRemoteStorage> cmRemoteStorages = CmApi.listRemoteStorage(cmRemoteStorageQuery);
        if (cmRemoteStorages.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmRemoteStorage cmRemoteStorage : cmRemoteStorages) {
                RemoteStorageDTO remoteStorageDTO = getShowDTO(cmRemoteStorage, dictTypeDOs);
                remoteStorageDTOs.add(remoteStorageDTO);
            }
        }

        return Result.success(remoteStorageDTOs);
    }

    public Result get(String remoteStorageId) throws Exception {
        RemoteStorageDTO remoteStorageDTO = null;
        CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(remoteStorageId);
        if (cmRemoteStorage != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            remoteStorageDTO = getShowDTO(cmRemoteStorage, dictTypeDOs);
        }

        return Result.success(remoteStorageDTO);
    }

    public Result save(RemoteStorageForm remoteStorageForm) throws Exception {
        CheckResult checkResult = remoteStorageCheck.checkSave(remoteStorageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmRemoteStorageBody cmRemoteStorageBody = buildCmRemoteStorageBodyForSave(remoteStorageForm);
        CmApi.saveRemoteStorage(cmRemoteStorageBody);

        return Result.success();
    }

    public Result update(String remoteStorageId, RemoteStorageForm remoteStorageForm) throws Exception {
        CheckResult checkResult = remoteStorageCheck.checkUpdate(remoteStorageId, remoteStorageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmRemoteStorageBody cmRemoteStorageBody = buildCmRemoteStorageBodyForUpdate(remoteStorageForm);
        CmApi.updateRemoteStorage(remoteStorageId, cmRemoteStorageBody);

        return Result.success();
    }

    public Result enabled(String remoteStorageId, boolean enabled) throws Exception {
        CheckResult checkResult = remoteStorageCheck.checkEnabled(remoteStorageId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (enabled) {
            CmApi.enabledRemoteStorage(remoteStorageId);
        } else {
            CmApi.disabledRemoteStorage(remoteStorageId);
        }

        return Result.success();
    }

    public Result remove(String remoteStorageId) throws Exception {
        CheckResult checkResult = remoteStorageCheck.checkRemove(remoteStorageId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmApi.removeRemoteStorage(remoteStorageId);

        return Result.success();
    }

    private RemoteStorageDTO getShowDTO(CmRemoteStorage cmRemoteStorage, List<DictTypeDO> dictTypeDOs) {
        RemoteStorageDTO remoteStorageDTO = new RemoteStorageDTO();
        remoteStorageDTO.setId(cmRemoteStorage.getId());
        remoteStorageDTO.setName(cmRemoteStorage.getName());
        remoteStorageDTO.setVendor(cmRemoteStorage.getVendor());
        remoteStorageDTO.setVersion(cmRemoteStorage.getModel());

        CmRemoteStorage.Auth cmAuth = cmRemoteStorage.getAuth();
        if (cmAuth != null) {
            remoteStorageDTO.setIp(cmAuth.getIp());
            remoteStorageDTO.setPort(cmAuth.getPort());
        }

        long capacity = NumberUnits.ceil(cmRemoteStorage.getCapacity() / 1024.0);
        remoteStorageDTO.setCapacity(capacity);

        long used = NumberUnits.ceil(cmRemoteStorage.getUsed() / 1024.0);
        remoteStorageDTO.setUsed(used);

        DisplayDTO typeDisplayDTO = new DisplayDTO();
        typeDisplayDTO.setCode(cmRemoteStorage.getType());
        DictDO typeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.REMOTE_STORAGE_TYPE, cmRemoteStorage.getType());
        if (typeDictDO != null) {
            typeDisplayDTO.setDisplay(typeDictDO.getName());
        }
        remoteStorageDTO.setType(typeDisplayDTO);

        remoteStorageDTO.setEnabled(BooleanUtils.negate(cmRemoteStorage.getUnschedulable()));
        remoteStorageDTO.setDescription(cmRemoteStorage.getDesc());

        DisplayDTO stateDTO = new DisplayDTO();
        stateDTO.setCode(cmRemoteStorage.getState());
        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, cmRemoteStorage.getState());
        if (stateDictDO != null) {
            stateDTO.setDisplay(stateDictDO.getName());
        }
        remoteStorageDTO.setState(stateDTO);

        CmSiteBase cmSite = cmRemoteStorage.getSite();
        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            remoteStorageDTO.setSite(siteDTO);

            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }

        InfoDTO createdDTO = new InfoDTO();
        remoteStorageDTO.setCreated(createdDTO);
        createdDTO.setTimestamp(cmRemoteStorage.getCreatedAt());

        return remoteStorageDTO;
    }

    private CmRemoteStorageBody buildCmRemoteStorageBodyForSave(RemoteStorageForm remoteStorageForm) throws Exception {
        CmRemoteStorageBody remoteStorageBody = new CmRemoteStorageBody();
        remoteStorageBody.setSiteId(remoteStorageForm.getSiteId());
        remoteStorageBody.setName(remoteStorageForm.getName());
        remoteStorageBody.setVendor(remoteStorageForm.getVendor());
        remoteStorageBody.setModel(remoteStorageForm.getVersion());
        remoteStorageBody.setType(remoteStorageForm.getType());
        remoteStorageBody.setUnschedulable(BooleanUtils.negate(remoteStorageForm.getEnabled()));
        remoteStorageBody.setDesc(StringUtils.trimToEmpty(remoteStorageForm.getDescription()));
        CmRemoteStorageBody.Auth cmAuth = remoteStorageBody.new Auth();
        remoteStorageBody.setAuth(cmAuth);
        cmAuth.setIp(remoteStorageForm.getIp());
        cmAuth.setPort(remoteStorageForm.getPort());
        cmAuth.setUsername(remoteStorageForm.getUsername());
        cmAuth.setPassword(remoteStorageForm.getPassword());
        return remoteStorageBody;
    }

    private CmRemoteStorageBody buildCmRemoteStorageBodyForUpdate(RemoteStorageForm remoteStorageForm) {
        CmRemoteStorageBody remoteStorageBody = new CmRemoteStorageBody();
        if (remoteStorageForm.getName() != null) {
            remoteStorageBody.setName(remoteStorageForm.getName());
        }

        if (remoteStorageForm.getDescription() != null) {
            remoteStorageBody.setDesc(remoteStorageForm.getDescription());
        }
        return remoteStorageBody;
    }

    public ObjModel getObjModel(String remoteStorageId) throws Exception {
        CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(remoteStorageId);
        if (cmRemoteStorage != null) {
            return new ObjModel(cmRemoteStorage.getName(), cmRemoteStorage.getSite().getId());
        }
        return null;
    }
}
