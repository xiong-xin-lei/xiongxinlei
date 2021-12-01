package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.service.dto.DefServDTO;

@Service
public class DefServService extends BaseService {

    @Transactional
    public Result list(Boolean enabled) throws Exception {
        List<DefServDTO> defServDTOs = new ArrayList<>();
        List<DefServDO> defServDOs = defServDAO.list(enabled);
        for (DefServDO defServDO : defServDOs) {
            DefServDTO defServDTO = new DefServDTO();
            defServDTOs.add(defServDTO);

            defServDTO.setCode(defServDO.getCode());
            defServDTO.setName(defServDO.getName());
            defServDTO.setStateful(defServDO.getStateful());
            defServDTO.setEnabled(defServDO.getEnabled());
            defServDTO.setDescription(defServDO.getDescription());
        }
        return Result.success(defServDTOs);
    }

}
