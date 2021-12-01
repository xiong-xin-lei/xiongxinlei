package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.dto.DictTypeDTO;

@Service
public class DictTypeService extends BaseService {

    public Result list() {
        List<DictTypeDTO> dictTypeDTOs = new ArrayList<>();

        List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
        for (DictTypeDO dictTypeDO : dictTypeDOs) {
            DictTypeDTO dictTypeDTO = new DictTypeDTO();
            dictTypeDTOs.add(dictTypeDTO);

            dictTypeDTO.setCode(dictTypeDO.getCode());
            dictTypeDTO.setName(dictTypeDO.getName());
        }

        return Result.success(dictTypeDTOs);
    }

}
