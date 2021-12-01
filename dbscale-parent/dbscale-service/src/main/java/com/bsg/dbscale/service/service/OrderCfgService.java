package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.OrderCfgDO;
import com.bsg.dbscale.dao.domain.ScaleDO;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.OrderCfgDTO;
import com.bsg.dbscale.service.dto.ScaleBaseDTO;
import com.bsg.dbscale.service.form.OrderCfgForm;

@Service
public class OrderCfgService extends BaseService {

    public Result list(String category) throws Exception {
        List<OrderCfgDTO> orderCfgDTOs = new ArrayList<>();
        List<OrderCfgDO> orderCfgDOs = orderCfgDAO.list(category);
        if (orderCfgDOs.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<ScaleDO> scaleDOs = scaleDAO.list(null);
            List<DefServDO> defServDOs = defServDAO.list(null);
            for (OrderCfgDO orderCfgDO : orderCfgDOs) {
                ScaleDO scaleDO = findScaleDO(scaleDOs, orderCfgDO.getType(), orderCfgDO.getCpuCnt(),
                        orderCfgDO.getMemSize());
                DefServDO defServDO = findDefServDO(defServDOs, orderCfgDO.getType());
                OrderCfgDTO orderCfgDTO = getShowDTO(orderCfgDO, defServDO, scaleDO, dictTypeDOs);
                orderCfgDTOs.add(orderCfgDTO);
            }
        }
        return Result.success(orderCfgDTOs);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(String category, List<OrderCfgForm> orderCfgForms) throws Exception {
        for (OrderCfgForm orderCfgForm : orderCfgForms) {
            OrderCfgDO orderCfgDO = buildOrderCfgDOForSave(category, orderCfgForm);
            orderCfgDAO.save(orderCfgDO);
        }
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String category, List<OrderCfgForm> orderCfgForms) {
        for (OrderCfgForm orderCfgForm : orderCfgForms) {
            OrderCfgDO orderCfgDO = buildOrderCfgDOForUpdate(category, orderCfgForm);
            orderCfgDAO.update(orderCfgDO);
        }
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String category) {
        orderCfgDAO.remove(category);

        return Result.success();
    }

    private OrderCfgDTO getShowDTO(OrderCfgDO orderCfgDO, DefServDO defServDO, ScaleDO scaleDO,
            List<DictTypeDO> dictTypeDOs) {
        OrderCfgDTO orderCfgDTO = new OrderCfgDTO();
        orderCfgDTO.setCategory(orderCfgDO.getCategory());

        DisplayDTO typeDisplayDTO = new DisplayDTO();
        orderCfgDTO.setType(typeDisplayDTO);
        typeDisplayDTO.setCode(orderCfgDO.getType());
        if (defServDO != null) {
            typeDisplayDTO.setDisplay(defServDO.getName());
        }

        if (scaleDO != null) {
            ScaleBaseDTO scaleDTO = new ScaleBaseDTO();
            scaleDTO.setName(scaleDO.getName());
            scaleDTO.setCpuCnt(scaleDO.getCpuCnt());
            scaleDTO.setMemSize(scaleDO.getMemSize());
            orderCfgDTO.setScale(scaleDTO);
        }

        DisplayDTO diskDisplayDTO = new DisplayDTO();
        orderCfgDTO.setDiskType(diskDisplayDTO);
        diskDisplayDTO.setCode(orderCfgDO.getDiskType());
        DictDO diskDictDO = findDictDO(dictTypeDOs, DictTypeConsts.DISK_TYPE, orderCfgDO.getDiskType());
        if (diskDictDO != null) {
            diskDisplayDTO.setDisplay(diskDictDO.getName());
        }

        orderCfgDTO.setDataSize(orderCfgDO.getDataSize());
        orderCfgDTO.setLogSize(orderCfgDO.getLogSize());
        orderCfgDTO.setPort(orderCfgDO.getPort());
        orderCfgDTO.setClusterHA(orderCfgDO.getClusterHA());
        orderCfgDTO.setHostHA(orderCfgDO.getHostHA());

        return orderCfgDTO;
    }

    private OrderCfgDO buildOrderCfgDOForSave(String category, OrderCfgForm orderCfgForm) {
        OrderCfgDO orderCfgDO = new OrderCfgDO();
        orderCfgDO.setCategory(category);
        orderCfgDO.setType(orderCfgForm.getType());
        orderCfgDO.setCpuCnt(orderCfgForm.getCpuCnt());
        orderCfgDO.setMemSize(orderCfgForm.getMemSize());
        orderCfgDO.setDiskType(orderCfgForm.getDiskType());
        orderCfgDO.setDataSize(orderCfgForm.getDataSize());
        orderCfgDO.setLogSize(orderCfgForm.getLogSize());
        orderCfgDO.setPort(orderCfgForm.getPort());
        orderCfgDO.setClusterHA(orderCfgForm.getClusterHA());
        if (BooleanUtils.isTrue(orderCfgForm.getClusterHA())) {
            orderCfgDO.setHostHA(true);
        } else {
            orderCfgDO.setHostHA(orderCfgForm.getHostHA());
        }
        return orderCfgDO;
    }

    private OrderCfgDO buildOrderCfgDOForUpdate(String category, OrderCfgForm orderCfgForm) {
        OrderCfgDO orderCfgDO = orderCfgDAO.get(category, orderCfgForm.getType());
        if (orderCfgForm.getCpuCnt() != null) {
            orderCfgDO.setCpuCnt(orderCfgForm.getCpuCnt());
        }
        if (orderCfgForm.getMemSize() != null) {
            orderCfgDO.setMemSize(orderCfgForm.getMemSize());
        }
        if (StringUtils.isNotBlank(orderCfgForm.getDiskType())) {
            orderCfgDO.setDiskType(orderCfgForm.getDiskType());
        }
        if (orderCfgForm.getDataSize() != null) {
            orderCfgDO.setDataSize(orderCfgForm.getDataSize());
        }
        if (orderCfgForm.getLogSize() != null) {
            orderCfgDO.setLogSize(orderCfgForm.getLogSize());
        }
        if (orderCfgForm.getPort() != null) {
            orderCfgDO.setPort(orderCfgForm.getPort());
        }
        boolean clusterHA = orderCfgDO.getClusterHA();
        if (orderCfgForm.getClusterHA() != null) {
            clusterHA = orderCfgForm.getClusterHA();
            orderCfgDO.setClusterHA(orderCfgForm.getClusterHA());
        }
        if (clusterHA) {
            orderCfgDO.setHostHA(true);
        } else {
            if (orderCfgForm.getHostHA() != null) {
                orderCfgDO.setHostHA(orderCfgForm.getHostHA());
            }
        }
        return orderCfgDO;
    }
}
