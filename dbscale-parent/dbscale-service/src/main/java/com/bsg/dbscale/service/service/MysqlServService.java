package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmBackupBody;
import com.bsg.dbscale.cm.body.CmDBSchemaBody;
import com.bsg.dbscale.cm.body.CmDBUserBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmBackupFile;
import com.bsg.dbscale.cm.model.CmDBSchema;
import com.bsg.dbscale.cm.model.CmDBUser;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmServiceArchBase;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DBPrivilegeDTO;
import com.bsg.dbscale.service.dto.DBSchemaDTO;
import com.bsg.dbscale.service.dto.DBSchemaDetailDTO;
import com.bsg.dbscale.service.dto.DBUserDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.MysqlServDTO;
import com.bsg.dbscale.service.dto.MysqlUnitDTO;
import com.bsg.dbscale.service.dto.UnitBaseDTO;
import com.bsg.dbscale.service.form.DBPrivilegeForm;
import com.bsg.dbscale.service.form.DBSchemaForm;
import com.bsg.dbscale.service.form.MysqlServUserForm;
import com.bsg.dbscale.service.task.datasource.BackupDataSource;
import com.bsg.dbscale.service.util.TaskResult;

@Service
public class MysqlServService extends ServService {

    public MysqlServDTO getServDTO(ServDO mysqlServDO, CmService cmService, CmServiceArch cmServiceArch,
            DefServDO defServDO, List<DictTypeDO> dictTypeDOs) {
        MysqlServDTO mysqlServDTO = new MysqlServDTO();
        setServDTO(mysqlServDTO, mysqlServDO, cmService, cmServiceArch, defServDO, dictTypeDOs);

        List<UnitDO> unitDOs = mysqlServDO.getUnits();
        List<UnitBaseDTO> unitDTOs = new ArrayList<>(unitDOs.size());
        mysqlServDTO.setUnits(unitDTOs);
        for (UnitDO unitDO : unitDOs) {
            TaskDO latestTaskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_UNIT, unitDO.getId(), null);
            MysqlUnitDTO mysqlUnitDTO = geUnitDTO(unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
            unitDTOs.add(mysqlUnitDTO);
        }
        return mysqlServDTO;
    }

    private MysqlUnitDTO geUnitDTO(UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        MysqlUnitDTO mysqlUnitDTO = new MysqlUnitDTO();
        setUnitDTO(mysqlUnitDTO, unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
        return mysqlUnitDTO;
    }

    public void setUnitDTO(MysqlUnitDTO mysqlUnitDTO, UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        super.setUnitDTO(mysqlUnitDTO, unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);

        if (cmService != null) {
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                CmServiceArchBase cmArch = cmStatus.getArch();
                if (cmArch != null) {
                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                    if (cmUnits != null && cmUnits.size() > 0) {
                        for (CmService.Status.Unit cmUnit : cmUnits) {
                            if (cmUnit.getId().equals(unitDO.getRelateId())) {
                                Map<String, CmService.Status.Unit.Replication> cmReplicationMap = cmUnit
                                        .getReplication();
                                if (cmReplicationMap != null) {
                                    String mode = cmArch.getMode();
                                    if (CmConsts.REPL_MODE_SINGLE.equals(mode)) {
                                        CmService.Status.Unit.Replication cmReplication = cmReplicationMap.get(mode);
                                        if (cmReplication != null) {
                                            MysqlUnitDTO.SingelReplication replicationDTO = mysqlUnitDTO.new SingelReplication();
                                            mysqlUnitDTO.setReplication(replicationDTO);
                                            replicationDTO.setSelfIp(cmReplication.getSelfIp());
                                            replicationDTO.setSelfPort(cmReplication.getSelfPort());
                                        }
                                    } else {
                                        CmService.Status.Unit.Replication cmReplication = cmReplicationMap.get(mode);
                                        if (cmReplication != null) {
                                            MysqlUnitDTO.UnSingelReplication replicationDTO = mysqlUnitDTO.new UnSingelReplication();
                                            mysqlUnitDTO.setReplication(replicationDTO);
                                            replicationDTO.setSelfIp(cmReplication.getSelfIp());
                                            replicationDTO.setSelfPort(cmReplication.getSelfPort());
                                            replicationDTO.setMasterIp(cmReplication.getMasterIp());
                                            replicationDTO.setMasterPort(cmReplication.getMasterPort());
                                            if (StringUtils.isNotBlank(cmReplication.getRole())) {
                                                DisplayDTO roleDisplayDTO = new DisplayDTO();
                                                replicationDTO.setRole(roleDisplayDTO);
                                                roleDisplayDTO.setCode(cmReplication.getRole());

                                                DictDO roleDictDO = findDictDO(dictTypeDOs,
                                                        DictTypeConsts.REPLICATION_ROLE, cmReplication.getRole());
                                                if (roleDictDO != null) {
                                                    roleDisplayDTO.setDisplay(roleDictDO.getName());
                                                }
                                            }

                                            String slaveRunning = getUnitReplicationState(cmReplication);
                                            DisplayDTO slaveRunningDisplayDTO = new DisplayDTO();
                                            replicationDTO.setSlaveRunning(slaveRunningDisplayDTO);
                                            slaveRunningDisplayDTO.setCode(slaveRunning);

                                            DictDO slaveRunningDictDO = findDictDO(dictTypeDOs,
                                                    DictTypeConsts.REPLICATION_STATE, slaveRunning);
                                            if (slaveRunningDictDO != null) {
                                                slaveRunningDisplayDTO.setDisplay(slaveRunningDictDO.getName());
                                            }

                                            replicationDTO.setSlaveIoState(cmReplication.getSlaveIoState());
                                            if (StringUtils.isNotBlank(cmReplication.getSlaveIoRunning())) {
                                                DisplayDTO slaveIoRunningDisplayDTO = new DisplayDTO();
                                                replicationDTO.setSlaveIoRunning(slaveIoRunningDisplayDTO);
                                                slaveIoRunningDisplayDTO.setCode(cmReplication.getSlaveIoRunning());

                                                DictDO slaveIoRunningDictDO = findDictDO(dictTypeDOs,
                                                        DictTypeConsts.REPLICATION_STATE,
                                                        cmReplication.getSlaveIoRunning());
                                                if (slaveIoRunningDictDO != null) {
                                                    slaveIoRunningDisplayDTO.setDisplay(slaveIoRunningDictDO.getName());
                                                }
                                            }

                                            replicationDTO.setSlaveSqlState(cmReplication.getSlaveSqlRunningState());
                                            if (StringUtils.isNotBlank(cmReplication.getSlaveSqlRunning())) {
                                                DisplayDTO slaveSqlRunningDisplayDTO = new DisplayDTO();
                                                replicationDTO.setSlaveSqlRunning(slaveSqlRunningDisplayDTO);
                                                slaveSqlRunningDisplayDTO.setCode(cmReplication.getSlaveSqlRunning());

                                                DictDO slaveSqlRunningDictDO = findDictDO(dictTypeDOs,
                                                        DictTypeConsts.REPLICATION_STATE,
                                                        cmReplication.getSlaveSqlRunning());
                                                if (slaveSqlRunningDictDO != null) {
                                                    slaveSqlRunningDisplayDTO
                                                            .setDisplay(slaveSqlRunningDictDO.getName());
                                                }
                                            }

                                            replicationDTO
                                                    .setSecondsBehindMaster(cmReplication.getSecondsBehindMaster());
                                            replicationDTO.setMasterLogFile(cmReplication.getMasterLogFile());
                                            replicationDTO.setRelayMasterLogFile(cmReplication.getRelayMasterLogFile());
                                            replicationDTO.setReadMasterLogPos(cmReplication.getReadMasterLogPos());
                                            replicationDTO.setExecMasterLogPos(cmReplication.getExecMasterLogPos());
                                            replicationDTO.setRelayLogFile(cmReplication.getRelayLogFile());
                                            replicationDTO.setRelayLogPos(cmReplication.getRelayLogPos());
                                            replicationDTO.setLastIoError(cmReplication.getLastIoError());
                                            replicationDTO.setLastSqlError(cmReplication.getLastSqlError());

                                            if (StringUtils.isNotBlank(cmReplication.getArchMode())) {
                                                DisplayDTO archModeDisplayDTO = new DisplayDTO();
                                                replicationDTO.setArchMode(archModeDisplayDTO);
                                                archModeDisplayDTO.setCode(cmReplication.getArchMode());

                                                DictDO archModeDictDO = findDictDO(dictTypeDOs,
                                                        DictTypeConsts.ARCH_MODE, cmReplication.getArchMode());
                                                if (archModeDictDO != null) {
                                                    archModeDisplayDTO.setDisplay(archModeDictDO.getName());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String getUnitReplicationState(CmService.Status.Unit.Replication cmReplication) {
        String state = DictConsts.REPLICATION_STATE_UNKNOWN;
        if (cmReplication != null) {
            if (StringUtils.isNotBlank(cmReplication.getSlaveIoRunning())
                    && StringUtils.isNotBlank(cmReplication.getSlaveSqlRunning())) {
                if (StringUtils.equalsIgnoreCase(cmReplication.getSlaveIoRunning(), CmConsts.REPLICATION_STATE_YES)
                        && StringUtils.equalsIgnoreCase(cmReplication.getSlaveSqlRunning(),
                                CmConsts.REPLICATION_STATE_YES)) {
                    state = CmConsts.REPLICATION_STATE_YES;
                } else {
                    state = CmConsts.REPLICATION_STATE_NO;
                }
            }
        }
        return state;
    }

    public Result listDBSchema(ServDO mysqlServDO) throws Exception {
        List<DBSchemaDTO> dbSchemaDTOs = new ArrayList<>();
        if (StringUtils.isNotBlank(mysqlServDO.getRelateId())) {
            List<CmDBSchema> cmDBSchemas = CmApi.listDBSchema(mysqlServDO.getRelateId());
            if (cmDBSchemas != null) {
                for (CmDBSchema cmDBSchema : cmDBSchemas) {
                    DBSchemaDTO dbSchemaDTO = new DBSchemaDTO();
                    dbSchemaDTO.setName(cmDBSchema.getName());
                    dbSchemaDTO.setCharacterSet(cmDBSchema.getCharacterSet());
                    dbSchemaDTO.setSize(cmDBSchema.getSize());
                    dbSchemaDTOs.add(dbSchemaDTO);
                }
            }
        }

        return Result.success(dbSchemaDTOs);
    }

    public Result getDBSchema(ServDO mysqlServDO, String dbSchemaName) throws Exception {
        DBSchemaDTO dbSchemaDTO = null;
        if (StringUtils.isNotBlank(mysqlServDO.getRelateId())) {
            CmDBSchema cmDBSchema = CmApi.getDBSchema(mysqlServDO.getRelateId(), dbSchemaName);
            if (cmDBSchema != null) {
                dbSchemaDTO = geteDBSchemaDTO(cmDBSchema);
            }
        }

        return Result.success(dbSchemaDTO);
    }

    private DBSchemaDetailDTO geteDBSchemaDTO(CmDBSchema cmDBSchema) {
        if (cmDBSchema == null) {
            return null;
        }
        DBSchemaDetailDTO dbSchemaDTO = new DBSchemaDetailDTO();
        dbSchemaDTO.setName(cmDBSchema.getName());
        dbSchemaDTO.setCharacterSet(cmDBSchema.getCharacterSet());
        dbSchemaDTO.setSize(cmDBSchema.getSize());

        List<CmDBSchema.Table> cmTables = cmDBSchema.getTables();
        if (cmTables != null && cmTables.size() > 0) {
            List<DBSchemaDetailDTO.TableDTO> tableDTOs = new ArrayList<>();
            dbSchemaDTO.setTables(tableDTOs);

            for (CmDBSchema.Table cmTable : cmTables) {
                DBSchemaDetailDTO.TableDTO tableDTO = dbSchemaDTO.new TableDTO();
                tableDTOs.add(tableDTO);
                tableDTO.setName(cmTable.getName());
                tableDTO.setSize(cmTable.getSize());
            }
        }
        return dbSchemaDTO;
    }

    public Result saveDBSchema(ServDO mysqlServDO, DBSchemaForm dbSchemaForm) throws Exception {
        CmDBSchemaBody cmDBSchemaBody = buildCmDBSchemaBodyForSave(dbSchemaForm);
        CmApi.saveDBSchema(mysqlServDO.getRelateId(), cmDBSchemaBody);

        return Result.success();
    }

    private CmDBSchemaBody buildCmDBSchemaBodyForSave(DBSchemaForm dbSchemaForm) {
        CmDBSchemaBody cmDBSchemaBody = new CmDBSchemaBody();
        cmDBSchemaBody.setName(dbSchemaForm.getName());
        cmDBSchemaBody.setCharacterSet(dbSchemaForm.getCharacterSet());
        return cmDBSchemaBody;
    }

    public Result removeDBSchema(ServDO mysqlServDO, String dbSchemaName) throws Exception {
        CmApi.removeDBSchema(mysqlServDO.getRelateId(), dbSchemaName);

        return Result.success();
    }

    public List<DBUserDTO> listDBUser(ServDO mysqlServDO) throws Exception {
        List<DBUserDTO> dbDBUserDTOs = new ArrayList<>();
        if (StringUtils.isNotBlank(mysqlServDO.getRelateId())) {
            List<CmDBUser> cmDBUsers = CmApi.listDBUser(mysqlServDO.getRelateId());
            if (cmDBUsers != null) {
                for (CmDBUser cmDBUser : cmDBUsers) {
                    DBUserDTO dbUserDTO = geteDBUserDTO(cmDBUser);
                    dbDBUserDTOs.add(dbUserDTO);
                }
            }
        }

        return dbDBUserDTOs;
    }

    private DBUserDTO geteDBUserDTO(CmDBUser cmDBUser) {
        if (cmDBUser == null) {
            return null;
        }
        DBUserDTO dbUserDTO = new DBUserDTO();
        dbUserDTO.setUsername(cmDBUser.getName());
        dbUserDTO.setWhiteIp(cmDBUser.getIp());
        List<CmDBUser.DbPrivilege> cmDbPrivileges = cmDBUser.getDbPrivileges();
        if (cmDbPrivileges != null) {
            List<DBPrivilegeDTO> dbPrivilegeDTOs = new ArrayList<>();
            dbUserDTO.setDbPrivileges(dbPrivilegeDTOs);

            for (CmDBUser.DbPrivilege cmDbPrivilege : cmDbPrivileges) {
                DBPrivilegeDTO dbPrivilegeDTO = new DBPrivilegeDTO();
                dbPrivilegeDTOs.add(dbPrivilegeDTO);

                dbPrivilegeDTO.setDbName(cmDbPrivilege.getDbName());
                dbPrivilegeDTO.setPrivileges(cmDbPrivilege.getPrivileges());
            }
        }
        return dbUserDTO;
    }

    public DBUserDTO getDBUser(ServDO mysqlServDO, String dbUsername, String ip) throws Exception {
        DBUserDTO dbUserDTO = null;
        if (StringUtils.isNotBlank(mysqlServDO.getRelateId())) {
            CmDBUser cmDBUser = CmApi.getDBUser(mysqlServDO.getRelateId(), dbUsername, ip);
            if (cmDBUser != null) {
                dbUserDTO = geteDBUserDTO(cmDBUser);
            }
        }

        return dbUserDTO;
    }

    public void saveUser(ServDO mysqlServDO, MysqlServUserForm userForm) throws Exception {
        List<Future<TaskResult>> taskResults = new ArrayList<Future<TaskResult>>();
        List<String> whiteIps = userForm.getWhiteIps();
        for (String whiteIp : whiteIps) {
            CmDBUserBody cmDBUserBody = new CmDBUserBody();
            cmDBUserBody.setAuthType(userForm.getAuthType());
            cmDBUserBody.setName(userForm.getUsername());
            cmDBUserBody.setPwd(userForm.getPassword());
            cmDBUserBody.setIp(whiteIp);

            List<DBPrivilegeForm> dbPrivilegeForms = userForm.getDbPrivileges();
            List<CmDBUserBody.DBPrivilege> cmDBPrivileges = new ArrayList<>(dbPrivilegeForms.size());
            cmDBUserBody.setDbPrivileges(cmDBPrivileges);
            for (DBPrivilegeForm dbPrivilegeForm : dbPrivilegeForms) {
                CmDBUserBody.DBPrivilege cmDBPrivilege = cmDBUserBody.new DBPrivilege();
                cmDBPrivileges.add(cmDBPrivilege);

                cmDBPrivilege.setDbName(dbPrivilegeForm.getDbName());
                cmDBPrivilege.setPrivileges(dbPrivilegeForm.getPrivileges());
            }
            Future<TaskResult> future = executor.submit(new Callable<TaskResult>() {
                @Override
                public TaskResult call() throws Exception {
                    TaskResult result = new TaskResult();
                    result.setState(DictConsts.TASK_STATE_SUCCESS);
                    try {
                        CmApi.saveDBUser(mysqlServDO.getRelateId(), cmDBUserBody);
                    } catch (Exception e) {
                        result.setState(DictConsts.TASK_STATE_FAILED);
                        result.setMsg(userForm.getUsername() + "@" + whiteIp + "添加失败：" + e.getMessage());
                    }
                    return result;
                }
            });
            taskResults.add(future);
        }

        String errMsg = "";
        for (Future<TaskResult> future : taskResults) {
            TaskResult taskResult = future.get();
            if (!taskResult.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                errMsg = errMsg + taskResult.getMsg() + "\r\n";
            }
        }

        if (StringUtils.isNotBlank(errMsg)) {
            throw new Exception(errMsg);
        }
    }

    public void updateUser(ServDO mysqlServDO, String dbUsername, List<String> whiteIps, MysqlServUserForm userForm)
            throws Exception {
        List<Future<TaskResult>> taskResults = new ArrayList<Future<TaskResult>>();
        CmDBUserBody cmDBUserBody = buildCmDBUserBodyForUpdate(userForm);
        for (String whiteIp : whiteIps) {
            Future<TaskResult> future = executor.submit(new Callable<TaskResult>() {
                @Override
                public TaskResult call() throws Exception {
                    TaskResult result = new TaskResult();
                    result.setState(DictConsts.TASK_STATE_SUCCESS);
                    try {
                        CmApi.updateDBUserPrivileges(mysqlServDO.getRelateId(), dbUsername, whiteIp, cmDBUserBody);
                    } catch (Exception e) {
                        result.setState(DictConsts.TASK_STATE_FAILED);
                        result.setMsg(userForm.getUsername() + "@" + whiteIp + "编辑失败：" + e.getMessage());
                    }
                    return result;
                }
            });
            taskResults.add(future);
        }

        String errMsg = "";
        for (Future<TaskResult> future : taskResults) {
            TaskResult taskResult = future.get();
            if (!taskResult.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                errMsg = errMsg + taskResult.getMsg() + "\r\n";
            }
        }

        if (StringUtils.isNotBlank(errMsg)) {
            throw new Exception(errMsg);
        }
    }

    private CmDBUserBody buildCmDBUserBodyForUpdate(MysqlServUserForm userForm) {
        CmDBUserBody cmDBUserBody = new CmDBUserBody();
        List<DBPrivilegeForm> dbPrivilegeForms = userForm.getDbPrivileges();
        List<CmDBUserBody.DBPrivilege> cmDBPrivileges = new ArrayList<>(dbPrivilegeForms.size());
        cmDBUserBody.setDbPrivileges(cmDBPrivileges);
        for (DBPrivilegeForm dbPrivilegeForm : dbPrivilegeForms) {
            CmDBUserBody.DBPrivilege cmDBPrivilege = cmDBUserBody.new DBPrivilege();
            cmDBPrivileges.add(cmDBPrivilege);

            cmDBPrivilege.setDbName(dbPrivilegeForm.getDbName());
            cmDBPrivilege.setPrivileges(dbPrivilegeForm.getPrivileges());
        }

        return cmDBUserBody;
    }

    public void resetUserPwd(ServDO mysqlServDO, String dbUsername, List<String> whiteIps, String pwd)
            throws Exception {
        List<Future<TaskResult>> taskResults = new ArrayList<Future<TaskResult>>();
        CmDBUserBody cmDBUserBody = new CmDBUserBody();
        cmDBUserBody.setPwd(pwd);
        for (String whiteIp : whiteIps) {
            Future<TaskResult> future = executor.submit(new Callable<TaskResult>() {
                @Override
                public TaskResult call() throws Exception {
                    TaskResult result = new TaskResult();
                    result.setState(DictConsts.TASK_STATE_SUCCESS);
                    try {
                        CmApi.resetDBUserPwd(mysqlServDO.getRelateId(), dbUsername, whiteIp, cmDBUserBody);
                    } catch (Exception e) {
                        result.setState(DictConsts.TASK_STATE_FAILED);
                        result.setMsg(dbUsername + "@" + whiteIp + "重置密码失败：" + e.getMessage());
                    }
                    return result;
                }
            });
            taskResults.add(future);
        }

        String errMsg = "";
        for (Future<TaskResult> future : taskResults) {
            TaskResult taskResult = future.get();
            if (!taskResult.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                errMsg = errMsg + taskResult.getMsg() + "\r\n";
            }
        }

        if (StringUtils.isNotBlank(errMsg)) {
            throw new Exception(errMsg);
        }

    }

    public void removeUser(ServDO mysqlServDO, String dbUsername, List<String> whiteIps) throws Exception {
        List<Future<TaskResult>> taskResults = new ArrayList<Future<TaskResult>>();
        for (String whiteIp : whiteIps) {
            Future<TaskResult> future = executor.submit(new Callable<TaskResult>() {
                @Override
                public TaskResult call() throws Exception {
                    TaskResult result = new TaskResult();
                    result.setState(DictConsts.TASK_STATE_SUCCESS);
                    try {
                        CmApi.removeDBUser(mysqlServDO.getRelateId(), dbUsername, whiteIp);
                    } catch (Exception e) {
                        result.setState(DictConsts.TASK_STATE_FAILED);
                        result.setMsg(dbUsername + "@" + whiteIp + "删除失败：" + e.getMessage());
                    }
                    return result;
                }
            });
            taskResults.add(future);
        }

        String errMsg = "";
        for (Future<TaskResult> future : taskResults) {
            TaskResult taskResult = future.get();
            if (!taskResult.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                errMsg = errMsg + taskResult.getMsg() + "\r\n";
            }
        }

        if (StringUtils.isNotBlank(errMsg)) {
            throw new Exception(errMsg);
        }
    }

    public CmBackupBody buildCmServiceBackupRequestBody(BackupDataSource backupDataSource, ServDO servDO)
            throws Exception {
        CmBackupBody cmBackupBody = new CmBackupBody();
        cmBackupBody.setBackupStorageType(backupDataSource.getBackupStorageType());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        String businessAreaId = servGroupDO.getBusinessAreaId();
        if (CmConsts.BACKUP_STORAGE_TYPE_NFS.equals(backupDataSource.getBackupStorageType())) {
            CmNfsQuery nfsQuery = new CmNfsQuery();
            nfsQuery.setZone(businessAreaId);
            nfsQuery.setUnschedulable(false);
            List<CmNfs> cmNfs = CmApi.listNfs(nfsQuery);
            if (cmNfs.size() < 1) {
                throw new Exception("无符合条件的NFS。");
            }
            cmBackupBody.setBackupStorageId(cmNfs.get(0).getId());
        }
        cmBackupBody.setType(backupDataSource.getType());
        cmBackupBody.setExpiredAt(backupDataSource.getExpired());
        return cmBackupBody;
    }

    public TaskResult pollingBackup(SubtaskDO subtaskDO, String backupFileId) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmBackupFile cmBackupFile = CmApi.getBackupFile(backupFileId);
                if (cmBackupFile == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                String state = cmBackupFile.getStatus();
                if (StringUtils.equalsIgnoreCase(state, CmConsts.BACKUP_FILE_COMPLETE)) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else if (StringUtils.equalsIgnoreCase(state, CmConsts.BACKUP_FILE_FAILED)) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg(cmBackupFile.getMsg());
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                        taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                        taskResult.setMsg("timeout");
                        return taskResult;
                    } else {
                        Thread.sleep(10000);
                        subtaskDO = subtaskDAO.get(subtaskDO.getId());
                    }
                }
            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }
}