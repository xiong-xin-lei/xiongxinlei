package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmServiceArchBase;
import com.bsg.dbscale.cm.model.CmTopology;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.CmhaServDTO;
import com.bsg.dbscale.service.dto.CmhaUnitDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.UnitBaseDTO;

@Service
public class CmhaServService extends ServService {

    public CmhaServDTO getServDTO(ServDO servDO, CmService cmService, CmTopology cmTopology,
            CmServiceArch cmServiceArch, DefServDO defServDO, List<DictTypeDO> dictTypeDOs) {
        CmhaServDTO cmhaServDTO = new CmhaServDTO();
        setServDTO(cmhaServDTO, servDO, cmService, cmServiceArch, defServDO, dictTypeDOs);

        if (cmTopology != null) {
            List<CmTopology.Node> cmNodes = cmTopology.getNodes();
            if (cmNodes != null && cmNodes.size() > 0) {
                Map<String, CmhaServDTO.Topology> topologyMap = new HashMap<>(cmNodes.size());
                cmhaServDTO.setTopology(topologyMap);
                for (CmTopology.Node cmNode : cmNodes) {
                    CmhaServDTO.Topology topology = cmhaServDTO.new Topology();

                    if (StringUtils.isNotBlank(cmNode.getStatus())) {
                        DisplayDTO stateDisplayDTO = new DisplayDTO();
                        topology.setState(stateDisplayDTO);
                        stateDisplayDTO.setCode(cmNode.getStatus());

                        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.AVAILABLE_STATE,
                                cmNode.getStatus());
                        if (stateDictDO != null) {
                            stateDisplayDTO.setDisplay(stateDictDO.getName());
                        }
                    }

                    if (StringUtils.isNotBlank(cmNode.getRole())) {
                        DisplayDTO roleDisplayDTO = new DisplayDTO();
                        topology.setRole(roleDisplayDTO);
                        roleDisplayDTO.setCode(cmNode.getRole());

                        DictDO roleDictDO = findDictDO(dictTypeDOs, DictTypeConsts.REPLICATION_ROLE, cmNode.getRole());
                        if (roleDictDO != null) {
                            roleDisplayDTO.setDisplay(roleDictDO.getName());
                        }
                    }

                    topology.setMasterIp(cmNode.getMasterHost());

                    CmTopology.Node.Replication cmReplication = cmNode.getReplication();
                    String ioThread = null;
                    String sqlThread = null;
                    String replMode = null;
                    if (cmReplication != null) {
                        ioThread = cmReplication.getIoThread();

                        if (StringUtils.isNotBlank(ioThread)) {
                            DisplayDTO ioThreadDisplayDTO = new DisplayDTO();
                            topology.setIoThread(ioThreadDisplayDTO);
                            ioThreadDisplayDTO.setCode(ioThread);

                            DictDO ioThreadDictDO = findDictDO(dictTypeDOs, DictTypeConsts.REPLICATION_STATE, ioThread);
                            if (ioThreadDictDO != null) {
                                ioThreadDisplayDTO.setDisplay(ioThreadDictDO.getName());
                            }
                        }

                        sqlThread = cmReplication.getIoThread();
                        if (StringUtils.isNotBlank(sqlThread)) {
                            DisplayDTO sqlThreadDisplayDTO = new DisplayDTO();
                            topology.setSqlThread(sqlThreadDisplayDTO);
                            sqlThreadDisplayDTO.setCode(sqlThread);

                            DictDO sqlThreadDictDO = findDictDO(dictTypeDOs, DictTypeConsts.REPLICATION_STATE,
                                    sqlThread);
                            if (sqlThreadDictDO != null) {
                                sqlThreadDisplayDTO.setDisplay(sqlThreadDictDO.getName());
                            }
                        }

                        replMode = cmReplication.getReplMode();
                        if (StringUtils.isNotBlank(replMode)) {
                            DisplayDTO replModeDisplayDTO = new DisplayDTO();
                            topology.setReplMode(replModeDisplayDTO);
                            replModeDisplayDTO.setCode(replMode);

                            DictDO replModeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.REPLICATION_MODE, replMode);
                            if (replModeDictDO != null) {
                                replModeDisplayDTO.setDisplay(replModeDictDO.getName());
                            }
                        }
                        topology.setReplErrCounter(cmReplication.getReplErrCounter());
                    }
                    topology.setCandidate(cmNode.getCandidate());
                    topology.setMaintain(cmNode.getMaintain());
                    topology.setIsolate(cmNode.getIsolate());

                    String runningState = DictConsts.STATE_UNKNOWN;
                    if (cmNode.getRole().equals(CmConsts.ROLE_MASTER)) {
                        if (StringUtils.equalsIgnoreCase(cmNode.getStatus(), CmConsts.STATE_ONLINE)
                                && BooleanUtils.isFalse(cmNode.getIsolate())
                                && StringUtils.equalsIgnoreCase(replMode, CmConsts.REPL_MODE_SEMI_SYNC)
                                && BooleanUtils.isFalse(cmNode.getMaintain())) {
                            runningState = DictConsts.STATE_PASSING;
                        } else if (StringUtils.equalsIgnoreCase(cmNode.getStatus(), CmConsts.STATE_OFFLINE)) {
                            runningState = DictConsts.STATE_CRITICAL;
                        } else if (StringUtils.equalsIgnoreCase(cmNode.getStatus(), CmConsts.STATE_ONLINE)
                                && BooleanUtils.isFalse(cmNode.getIsolate())
                                && StringUtils.equalsIgnoreCase(replMode, CmConsts.REPL_MODE_ASYNC)) {
                            runningState = DictConsts.STATE_WARNNING;
                        }
                    } else if (cmNode.getRole().equals(CmConsts.ROLE_SLAVE)) {
                        if (StringUtils.equalsIgnoreCase(cmNode.getStatus(), CmConsts.STATE_ONLINE)
                                && BooleanUtils.isFalse(cmNode.getIsolate())
                                && StringUtils.equalsIgnoreCase(ioThread, CmConsts.REPLICATION_STATE_YES)
                                && StringUtils.equalsIgnoreCase(sqlThread, CmConsts.REPLICATION_STATE_YES)
                                && BooleanUtils.isFalse(cmNode.getMaintain())) {
                            runningState = DictConsts.STATE_PASSING;
                        } else if (StringUtils.equalsIgnoreCase(cmNode.getStatus(), CmConsts.STATE_OFFLINE)) {
                            runningState = DictConsts.STATE_CRITICAL;
                        } else if (StringUtils.equalsIgnoreCase(cmNode.getStatus(), CmConsts.STATE_ONLINE)
                                && (!StringUtils.equalsIgnoreCase(ioThread, CmConsts.REPLICATION_STATE_YES)
                                        || !StringUtils.equalsIgnoreCase(sqlThread, CmConsts.REPLICATION_STATE_YES))) {
                            runningState = DictConsts.STATE_WARNNING;
                        }
                    }

                    DisplayDTO runningStateDisplayDTO = new DisplayDTO();
                    topology.setRunningState(runningStateDisplayDTO);
                    runningStateDisplayDTO.setCode(runningState);

                    DictDO runningStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, runningState);
                    if (runningStateDictDO != null) {
                        runningStateDisplayDTO.setDisplay(runningStateDictDO.getName());
                    }

                    topologyMap.put(cmNode.getNode(), topology);
                }
            }
        }

        List<UnitDO> unitDOs = servDO.getUnits();
        List<UnitBaseDTO> unitDTOs = new ArrayList<>(unitDOs.size());
        cmhaServDTO.setUnits(unitDTOs);
        for (UnitDO unitDO : unitDOs) {
            TaskDO latestTaskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_UNIT, unitDO.getId(), null);
            CmhaUnitDTO cmhaUnitDTO = getCmhaUnitDTO(unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
            unitDTOs.add(cmhaUnitDTO);
        }
        return cmhaServDTO;
    }

    private CmhaUnitDTO getCmhaUnitDTO(UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        CmhaUnitDTO cmhaUnitDTO = new CmhaUnitDTO();
        setUnitDTO(cmhaUnitDTO, unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);

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
                                    CmService.Status.Unit.Replication cmReplication = cmReplicationMap.get(mode);
                                    if (cmReplication != null && StringUtils.isNotBlank(cmReplication.getRole())) {
                                        DisplayDTO roleDisplayDTO = new DisplayDTO();
                                        cmhaUnitDTO.setRole(roleDisplayDTO);
                                        roleDisplayDTO.setCode(cmReplication.getRole());

                                        DictDO roleDictDO = findDictDO(dictTypeDOs, DictTypeConsts.REPLICATION_ROLE,
                                                cmReplication.getRole());
                                        if (roleDictDO != null) {
                                            roleDisplayDTO.setDisplay(roleDictDO.getName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return cmhaUnitDTO;
    }
}