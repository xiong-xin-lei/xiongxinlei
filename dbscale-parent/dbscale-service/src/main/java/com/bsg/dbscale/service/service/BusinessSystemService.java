package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.BusinessSystemCheck;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.dto.BusinessSystemDTO;
import com.bsg.dbscale.service.form.BusinessSystemForm;
import com.bsg.dbscale.service.query.BusinessSystemQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.util.DateUtils;

@Service
public class BusinessSystemService extends BaseService {

    @Autowired
    private BusinessSystemCheck businessSystemCheck;

    public Result list(BusinessSystemQuery businessSystemQuery) throws Exception {
        List<BusinessSystemDTO> businessSystemDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.BusinessSystemQuery daoQuery = convertToDAOQuery(businessSystemQuery);
        List<BusinessSystemDO> businessSystemDOs = businessSystemDAO.list(daoQuery);
        if (businessSystemDOs.size() > 0) {
            List<UserDO> userDOs = userDAO.list(null);
            for (BusinessSystemDO businessSystemDO : businessSystemDOs) {
                BusinessSystemDTO businessSystemDTO = getShowDTO(businessSystemDO, userDOs);
                businessSystemDTOs.add(businessSystemDTO);
            }
        }
        return Result.success(businessSystemDTOs);
    }

    public Result get(String businessSystemId) throws Exception {
        BusinessSystemDTO businessSystemDTO = null;
        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSystemId);
        if (businessSystemDO != null) {
            List<UserDO> userDOs = userDAO.list(null);
            businessSystemDTO = getShowDTO(businessSystemDO, userDOs);
        }

        return Result.success(businessSystemDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(BusinessSystemForm businessSystemForm, String activeUsername) throws Exception {
        CheckResult checkResult = businessSystemCheck.checkSave(businessSystemForm, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date date = systemDAO.getCurrentSqlDateTime();
        BusinessSystemDO businessSystemDO = buildBusinessSystemDOForSave(businessSystemForm, activeUsername, date);

        businessSystemDAO.save(businessSystemDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String businessSystemId, BusinessSystemForm businessSystemForm) {
        CheckResult checkResult = businessSystemCheck.checkUpdate(businessSystemId, businessSystemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        BusinessSystemDO businessSystemDO = buildBusinessSystemDOForUpdate(businessSystemId, businessSystemForm);

        businessSystemDAO.update(businessSystemDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result enabled(String businessSystemId, Boolean enabled) {
        CheckResult checkResult = businessSystemCheck.checkEnabled(businessSystemId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSystemId);
        businessSystemDO.setEnabled(enabled);

        businessSystemDAO.updateEnabled(businessSystemDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String businessSystemId) {
        CheckResult checkResult = businessSystemCheck.checkRemove(businessSystemId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        businessSystemDAO.remove(businessSystemId);

        return Result.success();
    }

    private com.bsg.dbscale.dao.query.BusinessSystemQuery convertToDAOQuery(BusinessSystemQuery businessSystemQuery) {
        com.bsg.dbscale.dao.query.BusinessSystemQuery daoQuery = new com.bsg.dbscale.dao.query.BusinessSystemQuery();
        daoQuery.setOwner(businessSystemQuery.getOwner());
        daoQuery.setEnabled(businessSystemQuery.getEnabled());
        return daoQuery;
    }

    private BusinessSystemDTO getShowDTO(BusinessSystemDO businessSystemDO, List<UserDO> userDOs) {
        BusinessSystemDTO businessSystemDTO = new BusinessSystemDTO();
        businessSystemDTO.setId(businessSystemDO.getId());
        businessSystemDTO.setName(businessSystemDO.getName());
        businessSystemDTO.setEnabled(businessSystemDO.getEnabled());
        businessSystemDTO.setDescription(businessSystemDO.getDescription());
        businessSystemDTO.setOwner(businessSystemDO.getOwner());
        UserDO createdUserDO = findUserDO(userDOs, businessSystemDO.getOwner());
        if (createdUserDO != null) {
            businessSystemDTO.setOwnerName(createdUserDO.getName());
        }
        businessSystemDTO.setGmtCreate(DateUtils.dateTimeToString(businessSystemDO.getGmtCreate()));

        return businessSystemDTO;
    }

    private BusinessSystemDO buildBusinessSystemDOForSave(BusinessSystemForm businessSystemForm, String activeUsername,
            Date date) {
        BusinessSystemDO businessSystemDO = new BusinessSystemDO();
        businessSystemDO.setId(PrimaryKeyUtil.get());
        businessSystemDO.setName(businessSystemForm.getName());
        businessSystemDO.setEnabled(BooleanUtils.isTrue(businessSystemForm.getEnabled()));
        businessSystemDO.setDescription(StringUtils.trimToEmpty(businessSystemForm.getDescription()));
        businessSystemDO.setOwner(activeUsername);
        businessSystemDO.setGmtCreate(date);
        return businessSystemDO;
    }

    private BusinessSystemDO buildBusinessSystemDOForUpdate(String businessSystemId,
            BusinessSystemForm businessSystemForm) {
        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSystemId);

        if (businessSystemForm.getName() != null) {
            businessSystemDO.setName(businessSystemForm.getName());
        }
        if (businessSystemForm.getDescription() != null) {
            businessSystemDO.setDescription(businessSystemForm.getDescription());
        }
        return businessSystemDO;
    }

    public ObjModel getObjModel(String businessSystemId) {
        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSystemId);
        if (businessSystemDO != null) {
            return new ObjModel(businessSystemDO.getName(), null);
        }
        return null;
    }
}
