package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.SubtaskCfgDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.SubtaskCfgCheck;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.SubtaskCfgDTO;
import com.bsg.dbscale.service.form.SubtaskCfgForm;

@Service
public class SubtaskCfgService extends BaseService {

    @Autowired
    private SubtaskCfgCheck subtaskCfgCheck;

    public Result list() {
        List<SubtaskCfgDTO> subtaskCfgDTOs = new ArrayList<>();

        List<SubtaskCfgDO> subtaskCfgDOs = subtaskCfgDAO.list();
        if (subtaskCfgDOs.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (SubtaskCfgDO subtaskCfgDO : subtaskCfgDOs) {
                SubtaskCfgDTO subtaskCfgDTO = getShowDTO(subtaskCfgDO, dictTypeDOs);
                subtaskCfgDTOs.add(subtaskCfgDTO);
            }
        }

        return Result.success(subtaskCfgDTOs);
    }

    public Result get(String objType, String actionType) {
        SubtaskCfgDTO subtaskCfgDTO = null;

        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(objType, actionType);
        if (subtaskCfgDO != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            subtaskCfgDTO = getShowDTO(subtaskCfgDO, dictTypeDOs);
        }

        return Result.success(subtaskCfgDTO);
    }

    @Transactional
    public Result update(String objType, String actionType, SubtaskCfgForm subtaskCfgForm) {
        CheckResult checkResult = subtaskCfgCheck.checkUpdate(objType, actionType, subtaskCfgForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(objType, actionType);
        if (subtaskCfgForm.getTimeout() != null) {
            subtaskCfgDO.setTimeout(subtaskCfgForm.getTimeout());
        }
        if (subtaskCfgForm.getDescription() != null) {
            subtaskCfgDO.setDescription(subtaskCfgForm.getDescription());
        }
        subtaskCfgDAO.update(subtaskCfgDO);

        return Result.success();
    }

    private SubtaskCfgDTO getShowDTO(SubtaskCfgDO subtaskCfgDO, List<DictTypeDO> dictTypeDOs) {
        SubtaskCfgDTO subtaskCfgDTO = new SubtaskCfgDTO();
        DisplayDTO objTypeDisplayDTO = new DisplayDTO();
        objTypeDisplayDTO.setCode(subtaskCfgDO.getObjType());
        DictDO objTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.OBJ_TYPE, subtaskCfgDO.getObjType());
        if (objTypeDictDO != null) {
            objTypeDisplayDTO.setDisplay(objTypeDictDO.getName());
        }
        subtaskCfgDTO.setObjType(objTypeDisplayDTO);

        DisplayDTO actionDisplayDTO = new DisplayDTO();
        actionDisplayDTO.setCode(subtaskCfgDO.getActionType());
        DictDO actionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE, subtaskCfgDO.getActionType());
        if (actionDictDO != null) {
            actionDisplayDTO.setDisplay(actionDictDO.getName());
        }
        subtaskCfgDTO.setAction(actionDisplayDTO);

        subtaskCfgDTO.setTimeout(subtaskCfgDO.getTimeout());
        subtaskCfgDTO.setDescription(subtaskCfgDO.getDescription());

        return subtaskCfgDTO;
    }

    public ObjModel getObjModel(String objType, String actionType) {
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(objType, actionType);
        if (subtaskCfgDO != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            DictDO objTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.OBJ_TYPE, subtaskCfgDO.getObjType());
            DictDO actionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE, subtaskCfgDO.getActionType());
            return new ObjModel(objTypeDictDO.getName() + "-" + actionDictDO.getName(), null);
        }
        return null;
    }
}
