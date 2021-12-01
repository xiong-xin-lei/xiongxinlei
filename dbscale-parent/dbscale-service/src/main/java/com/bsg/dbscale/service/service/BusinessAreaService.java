package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.BusinessAreaCheck;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.BusinessAreaDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.NfsBaseDTO;
import com.bsg.dbscale.service.form.BusinessAreaForm;
import com.bsg.dbscale.service.query.BusinessAreaQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.util.DateUtils;

@Service
public class BusinessAreaService extends BaseService {

    @Autowired
    private BusinessAreaCheck businessAreaCheck;

    public Result list(BusinessAreaQuery businessAreaQuery) throws Exception {
        List<BusinessAreaDTO> businessAreaDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.BusinessAreaQuery daoQuery = convertToDAOQuery(businessAreaQuery);
        List<BusinessAreaDO> businessAreaDOs = businessAreaDAO.list(daoQuery);
        if (businessAreaDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<UserDO> userDOs = userDAO.list(null);
            List<CmNfs> cmNfsBackups = CmApi.listNfs(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (BusinessAreaDO businessAreaDO : businessAreaDOs) {
                CmSite cmSite = CmApi.findSite(cmSites, businessAreaDO.getSiteId());
                CmNfs cmNfs = CmApi.findNfsByZone(cmNfsBackups, businessAreaDO.getId());
                BusinessAreaDTO businessAreaDTO = getShowDTO(businessAreaDO, cmSite, userDOs, cmNfs, dictTypeDOs);
                businessAreaDTOs.add(businessAreaDTO);
            }
        }
        return Result.success(businessAreaDTOs);
    }

    public Result get(String businessAreaId) throws Exception {
        BusinessAreaDTO businessAreaDTO = null;
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(businessAreaId);
        if (businessAreaDO != null) {
            CmSite cmSite = CmApi.getSite(businessAreaDO.getSiteId());
            List<UserDO> userDOs = userDAO.list(null);
            CmNfs cmNfs = null;
            List<DictTypeDO> dictTypeDOs = null;
            CmNfsQuery cmNfsQuery = new CmNfsQuery();
            cmNfsQuery.setZone(businessAreaId);
            List<CmNfs> cmNfsBackups = CmApi.listNfs(cmNfsQuery);
            if (cmNfsBackups != null && cmNfsBackups.size() > 0) {
                cmNfs = cmNfsBackups.get(0);
                dictTypeDOs = dictTypeDAO.list();
            }
            businessAreaDTO = getShowDTO(businessAreaDO, cmSite, userDOs, cmNfs, dictTypeDOs);
        }

        return Result.success(businessAreaDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(BusinessAreaForm businessAreaForm, String activeUsername) throws Exception {
        CheckResult checkResult = businessAreaCheck.checkSave(businessAreaForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date date = systemDAO.getCurrentSqlDateTime();
        BusinessAreaDO businessAreaDO = buildBusinessAreaDOForSave(businessAreaForm, activeUsername, date);

        businessAreaDAO.save(businessAreaDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String businessAreaId, BusinessAreaForm businessAreaForm) {
        CheckResult checkResult = businessAreaCheck.checkUpdate(businessAreaId, businessAreaForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        BusinessAreaDO businessAreaDO = buildBusinessAreaDOForUpdate(businessAreaId, businessAreaForm);

        businessAreaDAO.update(businessAreaDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result enabled(String businessAreaId, Boolean enabled) {
        CheckResult checkResult = businessAreaCheck.checkEnabled(businessAreaId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        BusinessAreaDO businessAreaDO = businessAreaDAO.get(businessAreaId);
        businessAreaDO.setEnabled(enabled);

        businessAreaDAO.updateEnabled(businessAreaDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String businessAreaId) throws Exception {
        CheckResult checkResult = businessAreaCheck.checkRemove(businessAreaId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        businessAreaDAO.remove(businessAreaId);

        return Result.success();
    }

    private com.bsg.dbscale.dao.query.BusinessAreaQuery convertToDAOQuery(BusinessAreaQuery businessAreaQuery) {
        com.bsg.dbscale.dao.query.BusinessAreaQuery daoQuery = new com.bsg.dbscale.dao.query.BusinessAreaQuery();
        daoQuery.setSiteId(businessAreaQuery.getSiteId());
        daoQuery.setEnabled(businessAreaQuery.getEnabled());
        return daoQuery;
    }

    private BusinessAreaDTO getShowDTO(BusinessAreaDO businessAreaDO, CmSite cmSite, List<UserDO> userDOs, CmNfs cmNfs,
            List<DictTypeDO> dictTypeDOs) {
        BusinessAreaDTO businessAreaDTO = new BusinessAreaDTO();
        businessAreaDTO.setId(businessAreaDO.getId());
        businessAreaDTO.setName(businessAreaDO.getName());
        businessAreaDTO.setDescription(businessAreaDO.getDescription());
        businessAreaDTO.setEnabled(businessAreaDO.getEnabled());
        InfoDTO createdDTO = new InfoDTO();
        businessAreaDTO.setCreated(createdDTO);
        createdDTO.setUsername(businessAreaDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, businessAreaDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(businessAreaDO.getGmtCreate()));

        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
            businessAreaDTO.setSite(siteDTO);
        }

        if (cmNfs != null) {
            NfsBaseDTO nfsBaseDTO = new NfsBaseDTO();
            businessAreaDTO.setNfs(nfsBaseDTO);
            nfsBaseDTO.setId(cmNfs.getId());
            nfsBaseDTO.setName(cmNfs.getName());
            nfsBaseDTO.setNfsIp(cmNfs.getNfsIp());
            nfsBaseDTO.setNfsSource(cmNfs.getNfsSource());
            nfsBaseDTO.setNfsOpts(cmNfs.getNfsOpts());
            nfsBaseDTO.setEnabled(BooleanUtils.negate(cmNfs.getUnschedulable()));
            nfsBaseDTO.setDescription(cmNfs.getDesc());

            DisplayDTO stateDisplayDTO = new DisplayDTO();
            nfsBaseDTO.setState(stateDisplayDTO);
            String state = cmNfs.getStatus();
            stateDisplayDTO.setCode(state);
            DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, state);
            if (stateDictDO != null) {
                stateDisplayDTO.setDisplay(stateDictDO.getName());
            }
        }

        return businessAreaDTO;
    }

    private BusinessAreaDO buildBusinessAreaDOForSave(BusinessAreaForm businessAreaForm, String activeUsername,
            Date date) {
        BusinessAreaDO businessAreaDO = new BusinessAreaDO();
        businessAreaDO.setId(PrimaryKeyUtil.get());
        businessAreaDO.setName(businessAreaForm.getName());
        businessAreaDO.setEnabled(BooleanUtils.isTrue(businessAreaForm.getEnabled()));
        businessAreaDO.setDescription(StringUtils.trimToEmpty(businessAreaForm.getDescription()));
        businessAreaDO.setSiteId(businessAreaForm.getSiteId());
        businessAreaDO.setCreator(activeUsername);
        businessAreaDO.setGmtCreate(date);
        return businessAreaDO;
    }

    private BusinessAreaDO buildBusinessAreaDOForUpdate(String businessAreaId, BusinessAreaForm businessAreaForm) {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(businessAreaId);

        if (businessAreaForm.getName() != null) {
            businessAreaDO.setName(businessAreaForm.getName());
        }
        if (businessAreaForm.getDescription() != null) {
            businessAreaDO.setDescription(businessAreaForm.getDescription());
        }
        return businessAreaDO;
    }

    public ObjModel getObjModel(String businessAreaId) {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(businessAreaId);
        if (businessAreaDO != null) {
            return new ObjModel(businessAreaDO.getName(), businessAreaDO.getSiteId());
        }
        return null;
    }
}
