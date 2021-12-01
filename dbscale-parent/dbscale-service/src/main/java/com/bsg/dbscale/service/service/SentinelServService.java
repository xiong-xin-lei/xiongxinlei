package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.model.CmLoadbalancer;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.dto.SentinelServDTO;
import com.bsg.dbscale.service.dto.SentinelUnitDTO;
import com.bsg.dbscale.service.dto.UnitBaseDTO;

@Service
public class SentinelServService extends ServService {

    public SentinelServDTO getServDTO(ServDO servDO, CmService cmService, CmServiceArch cmServiceArch,
            CmLoadbalancer cmLoadbalancer, DefServDO defServDO, List<DictTypeDO> dictTypeDOs) {
        SentinelServDTO sentinelServDTO = new SentinelServDTO();
        setServDTO(sentinelServDTO, servDO, cmService, cmServiceArch, defServDO, dictTypeDOs);
        if (cmLoadbalancer != null) {
            sentinelServDTO.setLoadbalanceIps(cmLoadbalancer.getIps());
        }

        List<UnitDO> unitDOs = servDO.getUnits();
        List<UnitBaseDTO> unitDTOs = new ArrayList<>(unitDOs.size());
        sentinelServDTO.setUnits(unitDTOs);
        for (UnitDO unitDO : unitDOs) {
            TaskDO latestTaskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_UNIT, unitDO.getId(), null);
            SentinelUnitDTO sentinelUnitDTO = getUnitDTO(unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
            unitDTOs.add(sentinelUnitDTO);
        }
        return sentinelServDTO;
    }

    private SentinelUnitDTO getUnitDTO(UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        SentinelUnitDTO sentinelUnitDTO = new SentinelUnitDTO();
        setUnitDTO(sentinelUnitDTO, unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
        return sentinelUnitDTO;
    }
}