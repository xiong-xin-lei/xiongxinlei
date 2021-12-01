package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.dao.domain.BackupStrategyDO;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.BackupStrategyCheck;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.BackupStrategyDTO;
import com.bsg.dbscale.service.dto.BusinessSubsystemBaseDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.UserBaseDTO;
import com.bsg.dbscale.service.form.BackupStrategyForm;
import com.bsg.dbscale.service.query.BackupStrategyQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.util.DateUtils;

@Service
public class BackupStrategyService extends BaseService {

    @Autowired
    private BackupStrategyCheck backupStrategyCheck;

    public Result list(BackupStrategyQuery backupStrategyQuery) throws Exception {
        List<BackupStrategyDTO> backupStrategyDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.BackupStrategyQuery daoQuery = convertToDAOQuery(backupStrategyQuery);
        List<BackupStrategyDO> backupStrategyDOs = backupStrategyDAO.list(daoQuery);
        if (backupStrategyDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);
            for (BackupStrategyDO backupStrategyDO : backupStrategyDOs) {
                ServGroupDO servGroupDO = backupStrategyDO.getServGroup();
                CmSite cmSite = null;
                if (servGroupDO != null) {
                    BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
                    if (businessAreaDO != null) {
                        cmSite = CmApi.findSite(cmSites, businessAreaDO.getSiteId());
                    }
                }
                BackupStrategyDTO backupStrategyDTO = getShowDTO(backupStrategyDO, cmSite, dictTypeDOs, userDOs);
                backupStrategyDTOs.add(backupStrategyDTO);
            }
        }
        return Result.success(backupStrategyDTOs);
    }

    public Result get(String backupStrategyId) throws Exception {
        BackupStrategyDTO backupStrategyDTO = null;
        BackupStrategyDO backupStrategyDO = backupStrategyDAO.get(backupStrategyId);
        if (backupStrategyDO != null) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);
            ServGroupDO servGroupDO = backupStrategyDO.getServGroup();
            CmSite cmSite = null;
            if (servGroupDO != null) {
                BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
                if (businessAreaDO != null) {
                    cmSite = CmApi.findSite(cmSites, businessAreaDO.getSiteId());
                }
            }
            backupStrategyDTO = getShowDTO(backupStrategyDO, cmSite, dictTypeDOs, userDOs);
        }

        return Result.success(backupStrategyDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(BackupStrategyForm backupStrategyForm, String activeUsername) throws Exception {
        CheckResult checkResult = backupStrategyCheck.checkSave(backupStrategyForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date date = systemDAO.getCurrentSqlDateTime();
        BackupStrategyDO backupStrategyDO = buildBackupStrategyDOForSave(backupStrategyForm, activeUsername, date);

        backupStrategyDAO.save(backupStrategyDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String backupStrategyId, BackupStrategyForm backupStrategyForm) {
        CheckResult checkResult = backupStrategyCheck.checkUpdate(backupStrategyId, backupStrategyForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        BackupStrategyDO backupStrategyDO = buildBackupStrategyDOForUpdate(backupStrategyId, backupStrategyForm);

        backupStrategyDAO.update(backupStrategyDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result enabled(String backupStrategyId, Boolean enabled) {
        CheckResult checkResult = backupStrategyCheck.checkEnabled(backupStrategyId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        BackupStrategyDO backupStrategyDO = backupStrategyDAO.get(backupStrategyId);
        backupStrategyDO.setEnabled(enabled);

        backupStrategyDAO.updateEnabled(backupStrategyDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String backupStrategyId) {
        CheckResult checkResult = backupStrategyCheck.checkRemove(backupStrategyId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        backupStrategyDAO.remove(backupStrategyId);

        return Result.success();
    }

    private com.bsg.dbscale.dao.query.BackupStrategyQuery convertToDAOQuery(BackupStrategyQuery backupStrategyQuery) {
        com.bsg.dbscale.dao.query.BackupStrategyQuery daoQuery = new com.bsg.dbscale.dao.query.BackupStrategyQuery();
        daoQuery.setServGroupId(backupStrategyQuery.getServGroupId());
        daoQuery.setSiteId(backupStrategyQuery.getSiteId());
        return daoQuery;
    }

    private BackupStrategyDTO getShowDTO(BackupStrategyDO backupStrategyDO, CmSite cmSite, List<DictTypeDO> dictTypeDOs,
            List<UserDO> userDOs) {
        BackupStrategyDTO backupStrategyDTO = new BackupStrategyDTO();
        backupStrategyDTO.setId(backupStrategyDO.getId());

        DisplayDTO backupStorageTypeDisplayDTO = new DisplayDTO();
        backupStorageTypeDisplayDTO.setCode(backupStrategyDO.getBackupStorageType());
        DictDO backupStorageTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.BACKUP_STORAGE_TYPE,
                backupStrategyDO.getBackupStorageType());
        if (backupStorageTypeDictDO != null) {
            backupStorageTypeDisplayDTO.setDisplay(backupStorageTypeDictDO.getName());
        }
        backupStrategyDTO.setBackupStorageType(backupStorageTypeDisplayDTO);

        DisplayDTO typeDisplayDTO = new DisplayDTO();
        typeDisplayDTO.setCode(backupStrategyDO.getType());
        DictDO typeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.BACKUP_TYPE, backupStrategyDO.getType());
        if (typeDictDO != null) {
            typeDisplayDTO.setDisplay(typeDictDO.getName());
        }
        backupStrategyDTO.setType(typeDisplayDTO);

        if (StringUtils.isNotBlank(backupStrategyDO.getTables())) {
            backupStrategyDTO
                    .setTables(new ArrayList<>(Arrays.asList(StringUtils.split(backupStrategyDO.getTables(), ","))));
        }
        backupStrategyDTO.setCronExpression(backupStrategyDO.getCronExpression());
        backupStrategyDTO.setFileRetentionNum(backupStrategyDO.getFileRetentionNum());
        backupStrategyDTO.setEnabled(backupStrategyDO.getEnabled());
        backupStrategyDTO.setDescription(backupStrategyDO.getDescription());

        InfoDTO createdDTO = new InfoDTO();
        backupStrategyDTO.setCreated(createdDTO);
        createdDTO.setUsername(backupStrategyDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, backupStrategyDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(backupStrategyDO.getGmtCreate()));

        ServGroupDO servGroupDO = backupStrategyDO.getServGroup();
        if (servGroupDO != null) {
            BackupStrategyDTO.ServGroupDTO servGroupDTO = backupStrategyDTO.new ServGroupDTO();
            backupStrategyDTO.setServGroup(servGroupDTO);
            servGroupDTO.setId(servGroupDO.getId());
            servGroupDTO.setName(servGroupDO.getName());
            servGroupDTO.setCategory(servGroupDO.getCategory());

            BusinessSubsystemDO businessSubsystemDO = servGroupDO.getBusinessSubsystem();
            if (businessSubsystemDO != null) {
                BusinessSubsystemBaseDTO businessSubsystemDTO = new BusinessSubsystemBaseDTO();
                servGroupDTO.setBusinessSubsystem(businessSubsystemDTO);

                businessSubsystemDTO.setId(businessSubsystemDO.getId());
                businessSubsystemDTO.setName(businessSubsystemDO.getName());

                BusinessSystemDO businessSystemDO = businessSubsystemDO.getBusinessSystem();
                if (businessSystemDO != null) {
                    IdentificationDTO businessSystemDTO = new IdentificationDTO();
                    businessSubsystemDTO.setBusinessSystem(businessSystemDTO);

                    businessSystemDTO.setId(businessSystemDO.getId());
                    businessSystemDTO.setName(businessSystemDO.getName());
                }
            }

            if (cmSite != null) {
                IdentificationDTO siteDTO = new IdentificationDTO();
                servGroupDTO.setSite(siteDTO);
                siteDTO.setId(cmSite.getId());
                siteDTO.setName(cmSite.getName());
            }

            UserBaseDTO userDTO = new UserBaseDTO();
            servGroupDTO.setOwner(userDTO);
            userDTO.setUsername(servGroupDO.getOwner());
            UserDO owner = findUserDO(userDOs, servGroupDO.getOwner());
            if (owner != null) {
                userDTO.setName(owner.getName());
                userDTO.setCompany(owner.getCompany());
                userDTO.setTelephone(owner.getTelephone());
            }
            backupStrategyDTO.setServGroup(servGroupDTO);
        }

        return backupStrategyDTO;
    }

    public BackupStrategyDO buildBackupStrategyDOForSave(BackupStrategyForm backupStrategyForm, String activeUsername,
            Date date) {
        BackupStrategyDO backupStrategyDO = new BackupStrategyDO();
        backupStrategyDO.setId(PrimaryKeyUtil.get());
        backupStrategyDO.setServGroupId(backupStrategyForm.getServGroupId());
        backupStrategyDO.setBackupStorageType(backupStrategyForm.getBackupStorageType());
        backupStrategyDO.setType(backupStrategyForm.getType());
        backupStrategyDO.setTables(StringUtils.join(backupStrategyForm.getTables(), ","));
        backupStrategyDO.setCronExpression(backupStrategyForm.getCronExpression());
        backupStrategyDO.setFileRetentionNum(backupStrategyForm.getFileRetentionNum());
        backupStrategyDO.setEnabled(BooleanUtils.isTrue(backupStrategyForm.getEnabled()));
        backupStrategyDO.setDescription(StringUtils.trimToEmpty(backupStrategyForm.getDescription()));
        backupStrategyDO.setCreator(activeUsername);
        backupStrategyDO.setGmtCreate(date);
        return backupStrategyDO;
    }

    private BackupStrategyDO buildBackupStrategyDOForUpdate(String backupStrategyId,
            BackupStrategyForm backupStrategyForm) {
        BackupStrategyDO backupStrategyDO = backupStrategyDAO.get(backupStrategyId);

        if (backupStrategyForm.getBackupStorageType() != null) {
            backupStrategyDO.setBackupStorageType(backupStrategyForm.getBackupStorageType());
        }

        if (backupStrategyForm.getType() != null) {
            backupStrategyDO.setType(backupStrategyForm.getType());
        }

        if (backupStrategyForm.getTables() != null) {
            backupStrategyDO.setTables(StringUtils.join(backupStrategyForm.getTables(), ","));
        }

        if (backupStrategyForm.getCronExpression() != null) {
            backupStrategyDO.setCronExpression(backupStrategyForm.getCronExpression());
        }

        if (backupStrategyForm.getFileRetentionNum() != null) {
            backupStrategyDO.setFileRetentionNum(backupStrategyForm.getFileRetentionNum());
        }

        if (backupStrategyForm.getDescription() != null) {
            backupStrategyDO.setDescription(backupStrategyForm.getDescription());
        }
        return backupStrategyDO;
    }

    public ObjModel getObjModel(String backupStrategyId) {
        BackupStrategyDO backupStrategyDO = backupStrategyDAO.get(backupStrategyId);
        if (backupStrategyDO != null) {
            return new ObjModel(backupStrategyDO.getCronExpression(), backupStrategyDO.getServGroupId(), null);
        }
        return null;
    }
}
