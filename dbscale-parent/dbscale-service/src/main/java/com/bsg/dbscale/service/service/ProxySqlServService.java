package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmProxyUserBody;
import com.bsg.dbscale.cm.model.CmLoadbalancer;
import com.bsg.dbscale.cm.model.CmProxyUser;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.ProxyUserDTO;
import com.bsg.dbscale.service.dto.ProxysqlServDTO;
import com.bsg.dbscale.service.dto.ProxysqlUnitDTO;
import com.bsg.dbscale.service.dto.UnitBaseDTO;
import com.bsg.dbscale.service.form.CmhaServGroupUserForm;

@Service
public class ProxySqlServService extends ServService {

    public ProxysqlServDTO getServDTO(ServDO proxySqlServDO, CmService cmService, CmServiceArch cmServiceArch,
            CmLoadbalancer cmLoadbalancer, DefServDO defServDO, List<DictTypeDO> dictTypeDOs) {
        ProxysqlServDTO proxysqlServDTO = new ProxysqlServDTO();
        setServDTO(proxysqlServDTO, proxySqlServDO, cmService, cmServiceArch, defServDO, dictTypeDOs);
        if (cmLoadbalancer != null) {
            proxysqlServDTO.setLoadbalanceIps(cmLoadbalancer.getIps());
        }

        List<UnitDO> unitDOs = proxySqlServDO.getUnits();
        List<UnitBaseDTO> unitDTOs = new ArrayList<>(unitDOs.size());
        proxysqlServDTO.setUnits(unitDTOs);
        for (UnitDO unitDO : unitDOs) {
            TaskDO latestTaskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_UNIT, unitDO.getId(), null);
            ProxysqlUnitDTO proxysqllUnitDTO = getUnitDTO(unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
            unitDTOs.add(proxysqllUnitDTO);
        }
        return proxysqlServDTO;
    }

    private ProxysqlUnitDTO getUnitDTO(UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        ProxysqlUnitDTO proxysqlUnitDTO = new ProxysqlUnitDTO();
        setUnitDTO(proxysqlUnitDTO, unitDO, cmService, defServDO, dictTypeDOs, latestTaskDO);
        return proxysqlUnitDTO;
    }

    public List<ProxyUserDTO> listProxyUser(ServDO proxySqlServDO) throws Exception {
        List<ProxyUserDTO> proxyUserDTOs = new ArrayList<>();
        if (StringUtils.isNotBlank(proxySqlServDO.getRelateId())) {
            List<CmProxyUser> cmProxyUsers = CmApi.listProxyUser(proxySqlServDO.getRelateId());
            if (cmProxyUsers != null) {
                List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
                for (CmProxyUser cmProxyUser : cmProxyUsers) {
                    ProxyUserDTO proxyUserDTO = getProxyUserDTO(cmProxyUser, dictTypeDOs);
                    proxyUserDTOs.add(proxyUserDTO);
                }
            }
        }

        return proxyUserDTOs;
    }

    private ProxyUserDTO getProxyUserDTO(CmProxyUser cmProxyUser, List<DictTypeDO> dictTypeDOs) {
        if (cmProxyUser == null) {
            return null;
        }
        ProxyUserDTO proxyUserDTO = new ProxyUserDTO();
        proxyUserDTO.setUsername(cmProxyUser.getUsername());
        proxyUserDTO.setMaxConnection(cmProxyUser.getMaxConnection());

        DisplayDTO propertiesDTO = new DisplayDTO();
        proxyUserDTO.setProperties(propertiesDTO);
        propertiesDTO.setCode(String.valueOf(cmProxyUser.getHostgroup()));
        DictDO taskStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.MYSQL_USER_PROPERTIES, propertiesDTO.getCode());
        if (taskStateDictDO != null) {
            propertiesDTO.setDisplay(taskStateDictDO.getName());

        }
        return proxyUserDTO;
    }

    public ProxyUserDTO getProxyUserDTO(ServDO proxySqlServDO, String username) throws Exception {
        ProxyUserDTO proxyUserDTO = null;
        if (StringUtils.isNotBlank(proxySqlServDO.getRelateId())) {
            CmProxyUser cmProxyUser = CmApi.getProxyUser(proxySqlServDO.getRelateId(), username);
            if (cmProxyUser != null) {
                List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
                proxyUserDTO = getProxyUserDTO(cmProxyUser, dictTypeDOs);
            }
        }

        return proxyUserDTO;
    }

    public void saveUser(ServDO proxyServDO, CmhaServGroupUserForm userForm) throws Exception {
        CmProxyUserBody proxyUserBody = new CmProxyUserBody();
        proxyUserBody.setUsername(userForm.getUsername());
        proxyUserBody.setPwd(userForm.getPassword());
        proxyUserBody.setMaxConnection(userForm.getMaxConnection());
        proxyUserBody.setHostGroup(Integer.parseInt(userForm.getProperties()));
        CmApi.saveProxyUser(proxyServDO.getRelateId(), proxyUserBody);
    }

    public void updateUser(ServDO proxyServDO, String username, CmhaServGroupUserForm userForm) throws Exception {
        CmProxyUserBody proxyUserBody = new CmProxyUserBody();
        if (userForm.getMaxConnection() != null) {
            proxyUserBody.setMaxConnection(userForm.getMaxConnection());
        }
        if (StringUtils.isNotBlank(userForm.getProperties())) {
            proxyUserBody.setHostGroup(Integer.parseInt(userForm.getProperties()));
        }
        CmApi.updateProxyUser(proxyServDO.getRelateId(), username, proxyUserBody);
    }

    public void resetUserPwd(ServDO proxyServDO, String username, String pwd) throws Exception {
        CmProxyUserBody proxyUserBody = new CmProxyUserBody();
        proxyUserBody.setPwd(pwd);
        CmApi.resetProxyUserPwd(proxyServDO.getRelateId(), username, proxyUserBody);
    }

    public void removeUser(ServDO proxyServDO, String username) throws Exception {
        CmApi.removeProxyUser(proxyServDO.getRelateId(), username);
    }
}