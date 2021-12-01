package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmBackupExternalBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmBackupFile;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmBackupFileQuery;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.cm.response.CmBackupExternalResp;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.service.check.BackupFileCheck;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.BackupFileDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.form.BackupExternalForm;
import com.bsg.dbscale.service.query.BackupFileQuery;

@Service
public class BackupFileService extends BaseService {

    @Autowired
    private BackupFileCheck backupFileCheck;

    public Result list(BackupFileQuery backupFileQuery) throws Exception {
        List<BackupFileDTO> backupFileDTOs = new ArrayList<>();
        CmBackupFileQuery cmBackupFileQuery = new CmBackupFileQuery();
        cmBackupFileQuery.setSiteId(backupFileQuery.getSiteId());
        cmBackupFileQuery.setUnitId(backupFileQuery.getUnitId());
        cmBackupFileQuery.setType(backupFileQuery.getType());
        cmBackupFileQuery.setExternal(backupFileQuery.getExternal());
        if (BooleanUtils.isTrue(backupFileQuery.getSuccess())) {
            cmBackupFileQuery.setStatus(CmConsts.BACKUP_FILE_COMPLETE);
        }

        if (StringUtils.isNotBlank(backupFileQuery.getServGroupId())) {
            ServGroupDO servGroupDO = servGroupDAO.get(backupFileQuery.getServGroupId());
            if (servGroupDO == null) {
                return Result.success(backupFileDTOs);
            }

            ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
            cmBackupFileQuery.setServiceId(mysqlServDO.getRelateId());
        }

        List<CmBackupFile> cmBackupFiles = CmApi.listBackupFile(cmBackupFileQuery);
        if (cmBackupFiles != null && cmBackupFiles.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmBackupFile cmBackupFile : cmBackupFiles) {
                BackupFileDTO backupFileDTO = getShowDTO(cmBackupFile, dictTypeDOs);
                backupFileDTOs.add(backupFileDTO);
            }
        }

        return Result.success(backupFileDTOs);
    }

    private BackupFileDTO getShowDTO(CmBackupFile cmBackupFile, List<DictTypeDO> dictTypeDOs) {
        BackupFileDTO backupFileDTO = new BackupFileDTO();
        backupFileDTO.setId(cmBackupFile.getId());
        backupFileDTO.setName(cmBackupFile.getName());
        backupFileDTO.setSize(cmBackupFile.getSize());
        backupFileDTO.setExternal(cmBackupFile.getExternal());
        backupFileDTO.setDirectory(cmBackupFile.getDirectory());

        DisplayDTO backupStorageDispalyDTO = new DisplayDTO();
        backupFileDTO.setBackupStorageType(backupStorageDispalyDTO);
        backupStorageDispalyDTO.setCode(cmBackupFile.getBackupStorageType());
        DictDO backupStorageDictDO = findDictDO(dictTypeDOs, DictTypeConsts.BACKUP_STORAGE_TYPE,
                cmBackupFile.getBackupStorageType());
        if (backupStorageDictDO != null) {
            backupStorageDispalyDTO.setDisplay(backupStorageDictDO.getName());
        }

        DisplayDTO typeDispalyDTO = new DisplayDTO();
        backupFileDTO.setType(typeDispalyDTO);
        typeDispalyDTO.setCode(cmBackupFile.getType());
        DictDO typeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.BACKUP_TYPE, cmBackupFile.getType());
        if (typeDictDO != null) {
            typeDispalyDTO.setDisplay(typeDictDO.getName());
        }

        DisplayDTO statusDispalyDTO = new DisplayDTO();
        backupFileDTO.setStatus(statusDispalyDTO);
        statusDispalyDTO.setCode(cmBackupFile.getStatus());
        switch (cmBackupFile.getStatus()) {
        case CmConsts.BACKUP_FILE_RUNNING:
            statusDispalyDTO.setDisplay("备份中");
            break;
        case CmConsts.BACKUP_FILE_COMPLETE:
            statusDispalyDTO.setDisplay("备份成功");
            break;
        case CmConsts.BACKUP_FILE_FAILED:
            statusDispalyDTO.setDisplay("备份失败");
            break;
        case CmConsts.BACKUP_FILE_DELETING:
            statusDispalyDTO.setDisplay("删除中");
            break;
        case CmConsts.BACKUP_FILE_DELETEFAILED:
            statusDispalyDTO.setDisplay("删除失败");
            break;

        default:
            break;
        }

        backupFileDTO.setMsg(cmBackupFile.getMsg());
        backupFileDTO.setCreatedAt(cmBackupFile.getCreatedAt());
        backupFileDTO.setFinishAt(cmBackupFile.getFinishAt());
        backupFileDTO.setExpiredAt(cmBackupFile.getExpiredAt());
        backupFileDTO.setUnitName(cmBackupFile.getUnit());
        backupFileDTO.setServiceName(cmBackupFile.getService());

        CmSiteBase cmSite = cmBackupFile.getSite();
        if (cmSite != null) {
            IdentificationDTO site = new IdentificationDTO();
            backupFileDTO.setSite(site);
            site.setId(cmSite.getId());
            site.setName(cmSite.getName());
        }

        return backupFileDTO;
    }

    public Result remove(String backupFileId) throws Exception {
        CmApi.removeBackupFile(backupFileId);

        return Result.success();
    }

    public Result saveExternalFile(BackupExternalForm backupExternalForm) throws Exception {
        CheckResult checkResult = backupFileCheck.saveExternalFile(backupExternalForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServGroupDO servGroupDO = servGroupDAO.get(backupExternalForm.getServGroupId());
        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        CmBackupExternalBody cmBackupExternalBody = new CmBackupExternalBody();
        cmBackupExternalBody.setServiceId(mysqlServDO.getId());
        cmBackupExternalBody.setBackupStorageType(CmConsts.BACKUP_STORAGE_TYPE_NFS);

        CmNfsQuery nfsQuery = new CmNfsQuery();
        nfsQuery.setZone(servGroupDO.getBusinessAreaId());
        nfsQuery.setUnschedulable(false);
        List<CmNfs> cmNfs = CmApi.listNfs(nfsQuery);

        cmBackupExternalBody.setBackupStorageId(cmNfs.get(0).getId());
        CmBackupExternalResp cmBackupExternalResp = CmApi.backupExternal(cmBackupExternalBody);
        return Result.success(cmBackupExternalResp);
    }

    public ObjModel getObjModel(String backupFileId) throws Exception {
        CmBackupFile cmBackupFile = CmApi.getBackupFile(backupFileId);
        if (cmBackupFile != null) {
            String siteId = null;
            CmSiteBase cmSite = cmBackupFile.getSite();
            if (cmSite != null) {
                siteId = cmSite.getId();
            }
            return new ObjModel(cmBackupFile.getName(), siteId);
        }
        return null;
    }
}
