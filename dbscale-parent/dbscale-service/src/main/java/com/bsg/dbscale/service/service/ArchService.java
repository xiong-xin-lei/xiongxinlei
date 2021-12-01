package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.query.CmServiceArchQuery;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.ArchDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.query.ArchQuery;

@Service
public class ArchService extends BaseService {

    public Result list(ArchQuery archQuery) throws Exception {
        List<ArchDTO> archDTOs = new ArrayList<>();
        CmServiceArchQuery cmServiceArchQuery = new CmServiceArchQuery();
        cmServiceArchQuery.setType(archQuery.getType());
        cmServiceArchQuery.setVersion(archQuery.getVersion());
        List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(cmServiceArchQuery);
        if (cmServiceArchs != null && cmServiceArchs.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<DefServDO> defServDOs = defServDAO.list(null);
            for (CmServiceArch cmServiceArch : cmServiceArchs) {
                DefServDO defServDO = findDefServDO(defServDOs, cmServiceArch.getServiceType());
                ArchDTO archDTO = getShowDTO(cmServiceArch, defServDO, dictTypeDOs);
                archDTOs.add(archDTO);
            }
        }
        return Result.success(archDTOs);
    }

    public ArchDTO getShowDTO(CmServiceArch cmServiceArch, DefServDO defServDO, List<DictTypeDO> dictTypeDOs) {
        ArchDTO archDTO = new ArchDTO();

        DisplayDTO typeDisplayDTO = new DisplayDTO();
        archDTO.setType(typeDisplayDTO);
        typeDisplayDTO.setCode(cmServiceArch.getServiceType());
        if (defServDO != null) {
            typeDisplayDTO.setDisplay(defServDO.getName());
        }

        archDTO.setName(cmServiceArch.getDesc());
        if (StringUtils.isNotBlank(cmServiceArch.getMode())) {
            DisplayDTO modeDisplayDTO = new DisplayDTO();
            archDTO.setMode(modeDisplayDTO);
            
            modeDisplayDTO.setCode(cmServiceArch.getMode());
            DictDO modeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ARCH_MODE, cmServiceArch.getMode());
            if (modeDictDO != null) {
                modeDisplayDTO.setDisplay(modeDictDO.getName());
            } else {
                modeDisplayDTO.setDisplay(cmServiceArch.getMode());
            }
        }
        
        archDTO.setUnitCnt(cmServiceArch.getReplicas());
        return archDTO;
    }

}
