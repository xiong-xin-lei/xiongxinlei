package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmRemoteStoragePoolBody;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.model.CmRemoteStorageBase;
import com.bsg.dbscale.cm.model.CmRemoteStoragePool;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.RemoteStoragePoolCheck;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.RemoteStoragePoolDTO;
import com.bsg.dbscale.service.form.RemoteStoragePoolForm;
import com.bsg.dbscale.util.NumberUnits;

@Service
public class RemoteStoragePoolService extends BaseService {

    @Autowired
    private RemoteStoragePoolCheck remoteStoragePoolCheck;

    public Result list(String remoteStorageId) throws Exception {
        List<RemoteStoragePoolDTO> remoteStoragePoolDTOs = new ArrayList<>();

        List<CmRemoteStoragePool> cmRemoteStoragePools = CmApi.listRemoteStoragePool(remoteStorageId, null);
        if (cmRemoteStoragePools.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmRemoteStoragePool cmRemoteStoragePool : cmRemoteStoragePools) {
                RemoteStoragePoolDTO remoteStoragePoolDTO = getShowDTO(cmRemoteStoragePool, dictTypeDOs);
                remoteStoragePoolDTOs.add(remoteStoragePoolDTO);
            }
        }

        return Result.success(remoteStoragePoolDTOs);
    }

    public Result get(String remoteStorageId, String poolId) throws Exception {
        RemoteStoragePoolDTO remoteStoragePoolDTO = null;
        CmRemoteStoragePool cmRemoteStoragePool = CmApi.getRemoteStoragePool(remoteStorageId, poolId);
        if (cmRemoteStoragePool != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            remoteStoragePoolDTO = getShowDTO(cmRemoteStoragePool, dictTypeDOs);
        }

        return Result.success(remoteStoragePoolDTO);
    }

    public Result save(String remoteStorageId, RemoteStoragePoolForm remoteStoragePoolForm) throws Exception {
        CheckResult checkResult = remoteStoragePoolCheck.checkSave(remoteStorageId, remoteStoragePoolForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmRemoteStoragePoolBody cmRemoteStoragePoolBody = buildCmRemoteStoragePoolBodyForSave(remoteStoragePoolForm);
        CmApi.saveRemoteStoragePool(remoteStorageId, cmRemoteStoragePoolBody);

        return Result.success();
    }

    public Result update(String remoteStorageId, String poolId, RemoteStoragePoolForm remoteStoragePoolForm)
            throws Exception {
        CheckResult checkResult = remoteStoragePoolCheck.checkUpdate(remoteStorageId, poolId, remoteStoragePoolForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmRemoteStoragePoolBody cmRemoteStoragePoolBody = buildCmRemoteStoragePoolBodyForUpdate(remoteStoragePoolForm);
        CmApi.updateRemoteStoragePool(remoteStorageId, poolId, cmRemoteStoragePoolBody);

        return Result.success();
    }

    public Result enabled(String remoteStorageId, String poolId, boolean enabled) throws Exception {
        CheckResult checkResult = remoteStoragePoolCheck.checkEnabled(remoteStorageId, poolId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (enabled) {
            CmApi.enabledRemoteStoragePool(remoteStorageId, poolId);
        } else {
            CmApi.disabledRemoteStoragePool(remoteStorageId, poolId);
        }

        return Result.success();
    }

    public Result remove(String remoteStorageId, String poolId) throws Exception {
        CheckResult checkResult = remoteStoragePoolCheck.checkRemove(remoteStorageId, poolId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmApi.removeRemoteStoragePool(remoteStorageId, poolId);

        return Result.success();
    }

    private RemoteStoragePoolDTO getShowDTO(CmRemoteStoragePool cmRemoteStoragePool, List<DictTypeDO> dictTypeDOs) {
        RemoteStoragePoolDTO poolDTO = new RemoteStoragePoolDTO();
        poolDTO.setId(cmRemoteStoragePool.getId());
        poolDTO.setName(cmRemoteStoragePool.getName());

        long capacity = NumberUnits.ceil(cmRemoteStoragePool.getCapacity() / 1024.0);
        poolDTO.setCapacity(capacity);

        long used = NumberUnits.ceil(cmRemoteStoragePool.getUsed() / 1024.0);
        poolDTO.setUsed(used);

        DisplayDTO performanceDisplayDTO = new DisplayDTO();
        performanceDisplayDTO.setCode(cmRemoteStoragePool.getPerformance());
        DictDO typeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STORAGE_PERFORMANCE,
                cmRemoteStoragePool.getPerformance());
        if (typeDictDO != null) {
            performanceDisplayDTO.setDisplay(typeDictDO.getName());
        }
        poolDTO.setPerformance(performanceDisplayDTO);

        CmRemoteStorageBase cmRemoteStorage = cmRemoteStoragePool.getRemoteStorage();
        if (cmRemoteStorage != null) {
            IdentificationDTO remoteStorageDTO = new IdentificationDTO();
            remoteStorageDTO.setId(cmRemoteStorage.getId());
            remoteStorageDTO.setName(cmRemoteStorage.getName());
            poolDTO.setRemoteStorage(remoteStorageDTO);
        }

        poolDTO.setEnabled(BooleanUtils.negate(cmRemoteStoragePool.getUnschedulable()));
        poolDTO.setDescription(cmRemoteStoragePool.getDesc());

        InfoDTO createdDTO = new InfoDTO();
        poolDTO.setCreated(createdDTO);
        createdDTO.setTimestamp(cmRemoteStoragePool.getCreatedAt());

        return poolDTO;
    }

    private CmRemoteStoragePoolBody buildCmRemoteStoragePoolBodyForSave(RemoteStoragePoolForm remoteStoragePoolForm) {
        CmRemoteStoragePoolBody poolBody = new CmRemoteStoragePoolBody();
        poolBody.setName(remoteStoragePoolForm.getName());
        poolBody.setPerformance(remoteStoragePoolForm.getPerformance());
        poolBody.setUnschedulable(BooleanUtils.negate(remoteStoragePoolForm.getEnabled()));
        poolBody.setDesc(StringUtils.trimToEmpty(remoteStoragePoolForm.getDescription()));
        return poolBody;
    }

    private CmRemoteStoragePoolBody buildCmRemoteStoragePoolBodyForUpdate(RemoteStoragePoolForm remoteStoragePoolForm) {
        CmRemoteStoragePoolBody poolBody = new CmRemoteStoragePoolBody();
        if (remoteStoragePoolForm.getName() != null) {
            poolBody.setName(remoteStoragePoolForm.getName());
        }

        if (remoteStoragePoolForm.getDescription() != null) {
            poolBody.setDesc(remoteStoragePoolForm.getDescription());
        }

        if (remoteStoragePoolForm.getPerformance() != null) {
            poolBody.setPerformance(remoteStoragePoolForm.getName());
        }
        return poolBody;
    }

    public ObjModel getObjModel(String remoteStorageId, String poolId) throws Exception {
        CmRemoteStoragePool cmRemoteStoragePool = CmApi.getRemoteStoragePool(remoteStorageId, poolId);
        if (cmRemoteStoragePool != null) {
            CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(remoteStorageId);
            String siteId = null;
            if (cmRemoteStorage != null) {
                siteId = cmRemoteStorage.getSite().getId();
            }
            return new ObjModel(cmRemoteStoragePool.getName(), siteId);
        }
        return null;
    }
}
