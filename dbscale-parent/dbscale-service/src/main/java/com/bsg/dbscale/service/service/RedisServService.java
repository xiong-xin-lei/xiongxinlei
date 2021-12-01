package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmServiceArchBase;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.RedisServDTO;
import com.bsg.dbscale.service.dto.RedisUnitDTO;
import com.bsg.dbscale.service.dto.UnitBaseDTO;

@Service
public class RedisServService extends ServService {

    public RedisServDTO getServDTO(ServDO servDO, CmService cmService, CmServiceArch cmServiceArch, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs) {
        RedisServDTO redisServDTO = new RedisServDTO();
        setServDTO(redisServDTO, servDO, cmService, cmServiceArch, defServDO, dictTypeDOs);

        List<UnitDO> unitDOs = servDO.getUnits();
        List<UnitBaseDTO> unitDTOs = new ArrayList<>(unitDOs.size());
        redisServDTO.setUnits(unitDTOs);
        for (UnitDO unitDO : unitDOs) {
            TaskDO latestTaskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_UNIT, unitDO.getId(), null);
            RedisUnitDTO redisUnitDTO = geUnitDTO(unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
            unitDTOs.add(redisUnitDTO);
        }
        return redisServDTO;
    }

    private RedisUnitDTO geUnitDTO(UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        RedisUnitDTO redisUnitDTO = new RedisUnitDTO();
        setUnitDTO(redisUnitDTO, unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
        return redisUnitDTO;
    }

    public void setUnitDTO(RedisUnitDTO redisUnitDTO, UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        super.setUnitDTO(redisUnitDTO, unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);

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
                                            RedisUnitDTO.SingelReplication replicationDTO = redisUnitDTO.new SingelReplication();
                                            redisUnitDTO.setReplication(replicationDTO);
                                            replicationDTO.setSelfIp(cmReplication.getSelfIp());
                                            replicationDTO.setSelfPort(cmReplication.getSelfPort());
                                        }
                                    } else {
                                        CmService.Status.Unit.Replication cmReplication = cmReplicationMap.get(mode);
                                        if (cmReplication != null) {
                                            RedisUnitDTO.UnSingelReplication replicationDTO = redisUnitDTO.new UnSingelReplication();
                                            redisUnitDTO.setReplication(replicationDTO);
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

                                            DisplayDTO stateDisplayDTO = new DisplayDTO();
                                            replicationDTO.setState(stateDisplayDTO);
                                            stateDisplayDTO.setCode(cmReplication.getReplicaLinkStatus());

                                            DictDO stateDictDO = findDictDO(dictTypeDOs,
                                                    DictTypeConsts.REPLICATION_STATE,
                                                    cmReplication.getReplicaLinkStatus());
                                            if (stateDictDO != null) {
                                                stateDisplayDTO.setDisplay(stateDictDO.getName());
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