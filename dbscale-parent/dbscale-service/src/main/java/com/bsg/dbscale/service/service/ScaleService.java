package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.ScaleDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.ScaleCheck;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.ScaleDTO;
import com.bsg.dbscale.service.form.ScaleForm;
import com.bsg.dbscale.service.query.ScaleQuery;

@Service
public class ScaleService extends BaseService {

    @Autowired
    private ScaleCheck scaleCheck;

    public Result list(ScaleQuery scaleQuery) throws Exception {
        List<ScaleDTO> scaleDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.ScaleQuery daoQuery = convertToDAOQuery(scaleQuery);
        List<ScaleDO> scaleDOs = scaleDAO.list(daoQuery);
        if (scaleDOs.size() > 0) {
            List<DefServDO> defServDOs = defServDAO.list(null);
            for (ScaleDO scaleDO : scaleDOs) {
                DefServDO defServDO = findDefServDO(defServDOs, scaleDO.getType());
                ScaleDTO scaleDTO = getShowDTO(scaleDO, defServDO);
                scaleDTOs.add(scaleDTO);
            }
        }
        return Result.success(scaleDTOs);
    }

    public Result get(String type, Double cpuCnt, Double memSize) throws Exception {
        ScaleDTO scaleDTO = null;
        ScaleDO scaleDO = scaleDAO.get(type, cpuCnt, memSize);
        if (scaleDO != null) {
            DefServDO defServDO = defServDAO.get(scaleDO.getType());
            scaleDTO = getShowDTO(scaleDO, defServDO);
        }

        return Result.success(scaleDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(ScaleForm scaleForm) throws Exception {
        CheckResult checkResult = scaleCheck.checkSave(scaleForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ScaleDO scaleDO = buildScaleDOForSave(scaleForm);

        scaleDAO.save(scaleDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result enabled(String type, Double cpuCnt, Double memSize, Boolean enabled) {
        CheckResult checkResult = scaleCheck.checkEnabled(type, cpuCnt, memSize, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ScaleDO scaleDO = scaleDAO.get(type, cpuCnt, memSize);
        scaleDO.setEnabled(enabled);

        scaleDAO.updateEnabled(scaleDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String type, Double cpuCnt, Double memSize) {
        CheckResult checkResult = scaleCheck.checkRemove(type, cpuCnt, memSize);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        scaleDAO.remove(type, cpuCnt, memSize);

        return Result.success();
    }

    private com.bsg.dbscale.dao.query.ScaleQuery convertToDAOQuery(ScaleQuery scaleQuery) {
        com.bsg.dbscale.dao.query.ScaleQuery daoQuery = new com.bsg.dbscale.dao.query.ScaleQuery();
        daoQuery.setType(scaleQuery.getType());
        daoQuery.setEnabled(scaleQuery.getEnabled());
        return daoQuery;
    }

    public ScaleDTO getShowDTO(ScaleDO scaleDO, DefServDO defServDO) {
        ScaleDTO scaleDTO = new ScaleDTO();
        DisplayDTO typeDisplayDTO = new DisplayDTO();
        typeDisplayDTO.setCode(scaleDO.getType());
        if (defServDO != null) {
            typeDisplayDTO.setDisplay(defServDO.getName());
        }
        scaleDTO.setType(typeDisplayDTO);
        scaleDTO.setName(scaleDO.getName());
        scaleDTO.setCpuCnt(scaleDO.getCpuCnt());
        scaleDTO.setMemSize(scaleDO.getMemSize());
        scaleDTO.setEnabled(scaleDO.getEnabled());
        return scaleDTO;
    }

    private ScaleDO buildScaleDOForSave(ScaleForm scaleForm) {
        ScaleDO scaleDO = new ScaleDO();
        scaleDO.setType(scaleForm.getType());
        String scaleName = getScaleName(scaleForm.getCpuCnt(), scaleForm.getMemSize());
        scaleDO.setName(scaleName);
        scaleDO.setCpuCnt(scaleForm.getCpuCnt());
        scaleDO.setMemSize(scaleForm.getMemSize());
        Long sequence = new Double(scaleForm.getCpuCnt() * 1000000 + scaleForm.getMemSize() * 1000).longValue();
        scaleDO.setSequence(sequence);
        boolean enabled = false;
        if (scaleForm.getEnabled() != null) {
            enabled = scaleForm.getEnabled();
        }
        scaleDO.setEnabled(enabled);
        return scaleDO;
    }

    public ObjModel getObjModel(String type, Double cpuCnt, Double memSize) {
        ScaleDO scaleDO = scaleDAO.get(type, cpuCnt, memSize);
        if (scaleDO != null) {
            return new ObjModel(scaleDO.getName(), null);
        }
        return null;
    }

}
