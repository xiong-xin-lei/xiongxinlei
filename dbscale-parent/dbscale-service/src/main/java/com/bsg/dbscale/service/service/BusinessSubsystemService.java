package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.service.check.BusinessSubsystemCheck;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.dto.BusinessSubsystemDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.form.BusinessSubsystemForm;
import com.bsg.dbscale.service.query.BusinessSubsystemQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.util.DateUtils;

@Service
public class BusinessSubsystemService extends BaseService {

    @Autowired
    private BusinessSubsystemCheck businessSubsystemCheck;

    public Result list(BusinessSubsystemQuery businessSubsystemQuery) throws Exception {
        List<BusinessSubsystemDTO> businessSubsystemDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.BusinessSubsystemQuery daoQuery = convertToDAOQuery(businessSubsystemQuery);
        List<BusinessSubsystemDO> businessSubsystemDOs = businessSubsystemDAO.list(daoQuery);
        if (businessSubsystemDOs.size() > 0) {
            for (BusinessSubsystemDO businessSubsystemDO : businessSubsystemDOs) {
                BusinessSubsystemDTO businessSubsystemDTO = getShowDTO(businessSubsystemDO);
                businessSubsystemDTOs.add(businessSubsystemDTO);
            }
        }
        return Result.success(businessSubsystemDTOs);
    }

    public Result get(String businessSubsystemId) throws Exception {
        BusinessSubsystemDTO businessSubsystemDTO = null;
        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(businessSubsystemId);
        if (businessSubsystemDO != null) {
            businessSubsystemDTO = getShowDTO(businessSubsystemDO);
        }

        return Result.success(businessSubsystemDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(BusinessSubsystemForm businessSubsystemForm, String activeUsername) throws Exception {
        CheckResult checkResult = businessSubsystemCheck.checkSave(businessSubsystemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date date = systemDAO.getCurrentSqlDateTime();
        BusinessSubsystemDO businessSubsystemDO = buildBusinessSubsystemDOForSave(businessSubsystemForm, date);

        businessSubsystemDAO.save(businessSubsystemDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String businessSubsystemId, BusinessSubsystemForm businessSubsystemForm) {
        CheckResult checkResult = businessSubsystemCheck.checkUpdate(businessSubsystemId, businessSubsystemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        BusinessSubsystemDO businessSubsystemDO = buildBusinessSubsystemDOForUpdate(businessSubsystemId,
                businessSubsystemForm);

        businessSubsystemDAO.update(businessSubsystemDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result enabled(String businessSubsystemId, Boolean enabled) {
        CheckResult checkResult = businessSubsystemCheck.checkEnabled(businessSubsystemId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(businessSubsystemId);
        businessSubsystemDO.setEnabled(enabled);

        businessSubsystemDAO.updateEnabled(businessSubsystemDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String businessSubsystemId) {
        CheckResult checkResult = businessSubsystemCheck.checkRemove(businessSubsystemId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        businessSubsystemDAO.remove(businessSubsystemId);

        return Result.success();
    }

    private com.bsg.dbscale.dao.query.BusinessSubsystemQuery convertToDAOQuery(
            BusinessSubsystemQuery businessSubsystemQuery) {
        com.bsg.dbscale.dao.query.BusinessSubsystemQuery daoQuery = new com.bsg.dbscale.dao.query.BusinessSubsystemQuery();
        daoQuery.setBusinessSystemId(businessSubsystemQuery.getBusinessSystemId());
        daoQuery.setOwner(businessSubsystemQuery.getOwner());
        daoQuery.setEnabled(businessSubsystemQuery.getEnabled());
        return daoQuery;
    }

    public BusinessSubsystemDTO getShowDTO(BusinessSubsystemDO businessSubsystemDO) {
        BusinessSubsystemDTO businessSubsystemDTO = new BusinessSubsystemDTO();
        businessSubsystemDTO.setId(businessSubsystemDO.getId());
        businessSubsystemDTO.setName(businessSubsystemDO.getName());
        businessSubsystemDTO.setEnabled(BooleanUtils.isTrue(businessSubsystemDO.getEnabled()));
        businessSubsystemDTO.setDescription(businessSubsystemDO.getDescription());
        businessSubsystemDTO.setGmtCreate(DateUtils.dateTimeToString(businessSubsystemDO.getGmtCreate()));

        BusinessSystemDO businessSystemDO = businessSubsystemDO.getBusinessSystem();
        if (businessSubsystemDO != null) {
            IdentificationDTO businessSystemDTO = new IdentificationDTO();
            businessSubsystemDTO.setBusinessSystem(businessSystemDTO);

            businessSystemDTO.setId(businessSystemDO.getId());
            businessSystemDTO.setName(businessSystemDO.getName());
        }

        return businessSubsystemDTO;
    }

    private BusinessSubsystemDO buildBusinessSubsystemDOForSave(BusinessSubsystemForm businessSubsystemForm,
            Date date) {
        BusinessSubsystemDO businessSubsystemDO = new BusinessSubsystemDO();
        businessSubsystemDO.setId(PrimaryKeyUtil.get());
        businessSubsystemDO.setName(businessSubsystemForm.getName());
        businessSubsystemDO.setEnabled(BooleanUtils.isTrue(businessSubsystemForm.getEnabled()));
        businessSubsystemDO.setDescription(StringUtils.trimToEmpty(businessSubsystemForm.getDescription()));
        businessSubsystemDO.setBusinessSystemId(businessSubsystemForm.getBusinessSystemId());
        businessSubsystemDO.setGmtCreate(date);
        return businessSubsystemDO;
    }

    private BusinessSubsystemDO buildBusinessSubsystemDOForUpdate(String businessSubsystemId,
            BusinessSubsystemForm businessSubsystemForm) {
        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(businessSubsystemId);

        if (businessSubsystemForm.getName() != null) {
            businessSubsystemDO.setName(businessSubsystemForm.getName());
        }
        if (businessSubsystemForm.getDescription() != null) {
            businessSubsystemDO.setDescription(businessSubsystemForm.getDescription());
        }
        return businessSubsystemDO;
    }

    public ObjModel getObjModel(String businessSubsystemId) {
        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(businessSubsystemId);
        if (businessSubsystemDO != null) {
            return new ObjModel(businessSubsystemDO.getName(), null);
        }
        return null;
    }
}
