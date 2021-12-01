package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.dao.domain.AppDO;
import com.bsg.dbscale.dao.domain.RoleCfgAppDO;
import com.bsg.dbscale.dao.domain.RoleDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.RoleCfgAppCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.dto.AppDTO;
import com.bsg.dbscale.service.dto.RoleCfgAppDTO;

@Service
public class RoleCfgAppService extends BaseService {

    @Autowired
    private RoleCfgAppCheck cfgAppCheck;

    public Result list(String siteId) throws Exception {
        List<AppDTO> appDTOs = new ArrayList<>();
        try {
            CmSite cmSite = CmApi.getSite(siteId);
            List<AppDO> appDOs = appDAO.listByPid(0L);
            for (AppDO appDO : appDOs) {
                if (cmSite != null) {
                    CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
                    if (kubernetes != null) {
                        if (StringUtils.equals(kubernetes.getStorageMode(), CmConsts.STORAGE_TYPE_VOLUMEPATH)) {
                            if (appDO.getId().equals(108000000L)) {
                                continue;
                            }
                        } else if (StringUtils.equals(kubernetes.getStorageMode(), CmConsts.STORAGE_TYPE_PVC)) {
                            if (appDO.getId().equals(106000000L)) {
                                continue;
                            }
                        } else {
                            if (appDO.getId().equals(108000000L) || appDO.getId().equals(106000000L)) {
                                continue;
                            }
                        }
                    } else {
                        if (appDO.getId().equals(108000000L) || appDO.getId().equals(106000000L)) {
                            continue;
                        }
                    }
                }
                AppDTO appDTO = new AppDTO();
                appDTOs.add(appDTO);
                BeanUtils.copyProperties(appDO, appDTO);
            }

            return Result.success(appDTOs);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void setVisiableAppDOs(List<AppDO> visiableAppDOs, List<AppDO> childrens, List<Long> appIds) {
        if (childrens != null && childrens.size() > 0) {
            for (AppDO appDO : childrens) {
                if (appIds.contains(appDO.getId())) {
                    AppDO newAppDO = new AppDO();
                    visiableAppDOs.add(newAppDO);
                    newAppDO.setId(appDO.getId());
                    newAppDO.setName(appDO.getName());
                    newAppDO.setType(appDO.getType());
                    newAppDO.setCode(appDO.getCode());
                    newAppDO.setIcon(appDO.getIcon());
                    newAppDO.setPos(appDO.getPos());
                    newAppDO.setSequence(appDO.getSequence());
                    newAppDO.setTabletopSeq(appDO.getTabletopSeq());
                    newAppDO.setRowSeq(appDO.getRowSeq());
                    newAppDO.setPid(appDO.getPid());
                    List<AppDO> retChildrens = new ArrayList<>();
                    newAppDO.setChildrens(retChildrens);
                    setVisiableAppDOs(retChildrens, appDO.getChildrens(), appIds);
                }
            }
        }
    }

    public Result get(String roleId, String siteId) throws Exception {
        RoleCfgAppDTO cfgAppDTO = null;
        try {
            RoleDO roleDO = roleDAO.get(roleId);
            if (roleDO != null) {
                cfgAppDTO = new RoleCfgAppDTO();
                cfgAppDTO.setRoleId(roleDO.getId());
                cfgAppDTO.setRoleName(roleDO.getName());
                List<AppDTO> appDTOs = new ArrayList<>();
                cfgAppDTO.setApps(appDTOs);

                List<AppDO> visiableAppDOs = new ArrayList<>();
                if (roleDO != null) {
                    List<AppDO> appDOs = appDAO.listByPid(0L);
                    if (!roleDO.getId().equals(Consts.ROLE_SYSTEM_SUPER_MANAGER)) {
                        List<Long> appIds = roleCfgAppDAO.listAppIdByRoleId(roleDO.getId());
                        setVisiableAppDOs(visiableAppDOs, appDOs, appIds);
                    } else {
                        visiableAppDOs = appDOs;
                    }
                }

                CmSite cmSite = CmApi.getSite(siteId);
                for (AppDO appDO : visiableAppDOs) {
                    CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
                    if (kubernetes != null) {
                        if (StringUtils.equals(kubernetes.getStorageMode(), CmConsts.STORAGE_TYPE_VOLUMEPATH)) {
                            if (appDO.getId().equals(108000000L)) {
                                continue;
                            }
                        } else if (StringUtils.equals(kubernetes.getStorageMode(), CmConsts.STORAGE_TYPE_PVC)) {
                            if (appDO.getId().equals(106000000L)) {
                                continue;
                            }
                        } else {
                            if (appDO.getId().equals(108000000L) || appDO.getId().equals(106000000L)) {
                                continue;
                            }
                        }
                    } else {
                        if (appDO.getId().equals(108000000L) || appDO.getId().equals(106000000L)) {
                            continue;
                        }
                    }
                    AppDTO appDTO = new AppDTO();
                    appDTOs.add(appDTO);
                    BeanUtils.copyProperties(appDO, appDTO);
                }
            }

            return Result.success(cfgAppDTO);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(String roleId, List<Long> appIds) throws Exception {
        try {
            CheckResult checkResult = cfgAppCheck.checkSave(roleId, appIds);
            if (checkResult.getCode() != CheckResult.SUCCESS) {
                logger.error(checkResult.getMsg());
                return Result.failure(checkResult);
            }
            roleCfgAppDAO.removeByRoleId(roleId);
            for (Long appId : appIds) {
                RoleCfgAppDO cfgAppDO = new RoleCfgAppDO();
                cfgAppDO.setRoleId(roleId);
                cfgAppDO.setAppId(appId);

                roleCfgAppDAO.save(cfgAppDO);
            }
            return Result.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
