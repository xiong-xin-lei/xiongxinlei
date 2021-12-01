package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmNfsBody;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.NfsCheck;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.IdentificationStatusDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.NfsDTO;
import com.bsg.dbscale.service.form.NfsForm;
import com.bsg.dbscale.service.query.NfsQuery;

@Service
public class NfsService extends BaseService {

    @Autowired
    private NfsCheck nfsCheck;

    public Result list(NfsQuery nfsQuery) throws Exception {
        List<NfsDTO> nfsDTOs = new ArrayList<>();
        CmNfsQuery cmNfsQuery = new CmNfsQuery();
        cmNfsQuery.setSiteId(nfsQuery.getSiteId());
        cmNfsQuery.setZone(nfsQuery.getBusinessAreaId());
        cmNfsQuery.setUnschedulable(BooleanUtils.negate(nfsQuery.getEnabled()));

        List<CmNfs> cmNfsBackups = CmApi.listNfs(cmNfsQuery);
        if (cmNfsBackups != null && cmNfsBackups.size() > 0) {
            List<BusinessAreaDO> businessAreaDOs = businessAreaDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmNfs cmNfs : cmNfsBackups) {
                BusinessAreaDO businessAreaDO = findBusinessAreaDO(businessAreaDOs, cmNfs.getZone());
                NfsDTO nfsDTO = getShowDTO(cmNfs, businessAreaDO, dictTypeDOs);
                nfsDTOs.add(nfsDTO);
            }
        }

        return Result.success(nfsDTOs);
    }

    public Result get(String nfsId) throws Exception {
        NfsDTO nfsDTO = null;
        CmNfs cmNfs = CmApi.getNfs(nfsId);
        if (cmNfs != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            BusinessAreaDO businessAreaDO = businessAreaDAO.get(cmNfs.getZone());
            nfsDTO = getShowDTO(cmNfs, businessAreaDO, dictTypeDOs);
        }

        return Result.success(nfsDTO);
    }

    public Result save(NfsForm nfsForm) throws Exception {
        CheckResult checkResult = nfsCheck.checkSave(nfsForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmNfsBody cmNfsBody = buildCmNfsBodyForSave(nfsForm);
        CmApi.saveNFS(cmNfsBody);

        return Result.success();
    }

    public Result enabled(String nfsId, boolean enabled) throws Exception {
        CheckResult checkResult = nfsCheck.checkEnabled(nfsId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (enabled) {
            CmApi.enabledNFS(nfsId);
        } else {
            CmApi.disabledNFS(nfsId);
        }

        return Result.success();
    }

    public Result remove(String nfsId) throws Exception {
        CheckResult checkResult = nfsCheck.checkRemove(nfsId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmApi.removeNfs(nfsId);

        return Result.success();
    }

    private NfsDTO getShowDTO(CmNfs cmNfs, BusinessAreaDO businessAreaDO, List<DictTypeDO> dictTypeDOs) {
        NfsDTO nfsDTO = new NfsDTO();

        nfsDTO.setId(cmNfs.getId());
        nfsDTO.setName(cmNfs.getName());
        nfsDTO.setNfsIp(cmNfs.getNfsIp());
        nfsDTO.setNfsSource(cmNfs.getNfsSource());
        nfsDTO.setNfsOpts(cmNfs.getNfsOpts());
        nfsDTO.setEnabled(BooleanUtils.negate(cmNfs.getUnschedulable()));
        nfsDTO.setDescription(cmNfs.getDesc());

        DisplayDTO stateDisplayDTO = new DisplayDTO();
        nfsDTO.setState(stateDisplayDTO);
        String state = cmNfs.getStatus();
        stateDisplayDTO.setCode(state);
        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, state);
        if (stateDictDO != null) {
            stateDisplayDTO.setDisplay(stateDictDO.getName());
        }

        CmSiteBase cmSite = cmNfs.getSite();
        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            nfsDTO.setSite(siteDTO);

            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }

        IdentificationStatusDTO businessAreaDTO = new IdentificationStatusDTO();
        nfsDTO.setBusinessArea(businessAreaDTO);

        businessAreaDTO.setId(cmNfs.getZone());
        if (businessAreaDO != null) {
            businessAreaDTO.setName(businessAreaDO.getName());
            businessAreaDTO.setEnabled(businessAreaDO.getEnabled());
        }

        InfoDTO createdDTO = new InfoDTO();
        nfsDTO.setCreated(createdDTO);
        createdDTO.setTimestamp(cmNfs.getCreatedAt());

        return nfsDTO;
    }

    private CmNfsBody buildCmNfsBodyForSave(NfsForm nfsForm) {
        CmNfsBody nfsBody = new CmNfsBody();
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(nfsForm.getBusinessAreaId());
        nfsBody.setSiteId(businessAreaDO.getSiteId());
        nfsBody.setZone(nfsForm.getBusinessAreaId());
        nfsBody.setNfsIp(nfsForm.getNfsIp());
        nfsBody.setNfsSource(nfsForm.getNfsSource());
        nfsBody.setNfsOpts(nfsForm.getNfsOpts());
        nfsBody.setName(nfsForm.getName());
        nfsBody.setUnschedulable(BooleanUtils.negate(nfsForm.getEnabled()));
        nfsBody.setDesc(nfsForm.getDescription());
        return nfsBody;
    }

    public ObjModel getObjModel(String nfsId) throws Exception {
        CmNfs cmNfs = CmApi.getNfs(nfsId);
        if (cmNfs != null) {
            String siteId = null;
            CmSiteBase cmSite = cmNfs.getSite();
            if (cmSite != null) {
                siteId = cmSite.getId();
            }
            return new ObjModel(cmNfs.getName(), siteId);
        }
        return null;
    }
}
