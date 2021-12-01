package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.DictCheck;
import com.bsg.dbscale.service.dto.DictDTO;
import com.bsg.dbscale.service.form.DictForm;

@Service
public class DictService extends BaseService {

    @Autowired
    private DictCheck dictCheck;

    public Result list(String dictTypeCode) {
        List<DictDTO> dictDTOs = new ArrayList<>();

        DictTypeDO dictTypeDO = dictTypeDAO.get(dictTypeCode);
        if (dictTypeDO != null) {
            List<DictDO> dictDOs = dictTypeDO.getDicts();
            for (DictDO dictDO : dictDOs) {
                DictDTO dictDTO = getShowDTO(dictDO);
                dictDTOs.add(dictDTO);
            }
        }

        return Result.success(dictDTOs);
    }

    public Result get(String dictTypeCode, String dictCode) {
        DictDTO dictDTO = null;

        DictDO dictDO = dictDAO.get(dictTypeCode, dictCode);
        if (dictDO != null) {
            dictDTO = getShowDTO(dictDO);
        }

        return Result.success(dictDTO);
    }

    @Transactional
    public Result update(String dictTypeCode, String dictCode, DictForm dictForm, String activeUsername) {
        CheckResult checkResult = dictCheck.checkUpdate(dictTypeCode, dictCode, dictForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        DictDO dictDO = dictDAO.get(dictTypeCode, dictCode);
        if (StringUtils.isNotBlank(dictForm.getName())) {
            dictDO.setName(dictForm.getName());
        }
        if (dictForm.getSequence() != null) {
            dictDO.setSequence(dictForm.getSequence());
        }
        dictDAO.update(dictDO);

        return Result.success();
    }

    private DictDTO getShowDTO(DictDO dictDO) {
        DictDTO dictDTO = new DictDTO();
        dictDTO.setCode(dictDO.getCode());
        dictDTO.setName(dictDO.getName());

        return dictDTO;
    }
}
