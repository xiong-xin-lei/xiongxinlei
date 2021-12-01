package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmImageBase;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmServiceCfg;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.ArchBaseDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.ScaleBaseDTO;
import com.bsg.dbscale.service.dto.ServCfgDTO;
import com.bsg.dbscale.service.dto.ServDTO;
import com.bsg.dbscale.service.dto.TaskBaseDTO;
import com.bsg.dbscale.service.dto.UnitBaseDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.util.NumberUnits;

@Service
public class ServService extends BaseService {

    public Result listCfg(String servId) throws Exception {
        List<ServCfgDTO> servCfgDTOs = new ArrayList<>();
        ServDO servDO = servDAO.get(servId);
        if (servDO != null && StringUtils.isNotBlank(servDO.getRelateId())) {
            List<CmServiceCfg> cmServiceCfgs = CmApi.listServiceCfg(servDO.getRelateId());
            if (cmServiceCfgs != null) {
                for (CmServiceCfg cmServiceCfg : cmServiceCfgs) {
                    if (BooleanUtils.isTrue(cmServiceCfg.getCanSet())) {
                        ServCfgDTO servCfgDTO = new ServCfgDTO();
                        servCfgDTOs.add(servCfgDTO);

                        servCfgDTO.setKey(cmServiceCfg.getKey());
                        servCfgDTO.setValue(cmServiceCfg.getValue());
                        servCfgDTO.setRange(cmServiceCfg.getRange());
                        servCfgDTO.setDefaultValue(cmServiceCfg.getDefaultValue());
                        servCfgDTO.setCanSet(cmServiceCfg.getCanSet());
                        servCfgDTO.setMustRestart(cmServiceCfg.getMustRestart());
                        servCfgDTO.setDesc(cmServiceCfg.getDesc());
                    }
                }
            }
        }

        return Result.success(servCfgDTOs);
    }

    public void setServDTO(ServDTO servDTO, ServDO servDO, CmService cmService, CmServiceArch cmServiceArch,
            DefServDO defServDO, List<DictTypeDO> dictTypeDOs) {
        servDTO.setId(servDO.getId());

        DisplayDTO typeDisplayDTO = new DisplayDTO();
        servDTO.setType(typeDisplayDTO);
        typeDisplayDTO.setCode(servDO.getType());
        if (defServDO != null) {
            typeDisplayDTO.setDisplay(defServDO.getName());
        }

        servDTO.setRelateId(servDO.getRelateId());

        String state = DictConsts.STATE_WARNNING;
        if (cmService != null) {
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                state = cmStatus.getState();
            }
        }
        DisplayDTO stateDisplayDTO = new DisplayDTO();
        servDTO.setState(stateDisplayDTO);
        stateDisplayDTO.setCode(state);
        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, state);
        if (stateDictDO != null) {
            stateDisplayDTO.setDisplay(stateDictDO.getName());
        }

        ArchBaseDTO archDTO = new ArchBaseDTO();
        DisplayDTO modeDisplayDTO = new DisplayDTO();
        archDTO.setMode(modeDisplayDTO);

        modeDisplayDTO.setCode(servDO.getArchMode());
        DictDO modeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ARCH_MODE, servDO.getArchMode());
        if (modeDictDO != null) {
            modeDisplayDTO.setDisplay(modeDictDO.getName());
        } else {
            modeDisplayDTO.setDisplay(servDO.getArchMode());
        }
        archDTO.setUnitCnt(servDO.getUnitCnt());

        if (cmServiceArch != null) {
            archDTO.setName(cmServiceArch.getDesc());
        }
        servDTO.setArch(archDTO);

        ScaleBaseDTO scaleDTO = new ScaleBaseDTO();
        String scaleName = getScaleName(servDO.getCpuCnt(), servDO.getMemSize());
        scaleDTO.setName(scaleName);
        scaleDTO.setCpuCnt(servDO.getCpuCnt());
        scaleDTO.setMemSize(servDO.getMemSize());
        servDTO.setScale(scaleDTO);

        VersionDTO versionDTO = new VersionDTO();
        servDTO.setVersion(versionDTO);
        versionDTO.setMajor(servDO.getMajorVersion());
        versionDTO.setMinor(servDO.getMinorVersion());
        versionDTO.setPatch(servDO.getPatchVersion());
        versionDTO.setBuild(servDO.getBuildVersion());

        DisplayDTO diskTypeDisplayDTO = new DisplayDTO();
        servDTO.setDiskType(diskTypeDisplayDTO);
        diskTypeDisplayDTO.setCode(servDO.getDiskType());
        DictDO diskTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.DISK_TYPE, servDO.getDiskType());
        if (diskTypeDictDO != null) {
            diskTypeDisplayDTO.setDisplay(diskTypeDictDO.getName());
        }

        servDTO.setDataSize(servDO.getDataSize());
        servDTO.setLogSize(servDO.getLogSize());
    }

    public void setUnitDTO(UnitBaseDTO unitDTO, UnitDO unitDO, CmService cmService, DefServDO defServDO,
            List<DictTypeDO> dictTypeDOs, TaskDO latestTaskDO) {
        unitDTO.setId(unitDO.getId());

        DisplayDTO typeDisplayDTO = new DisplayDTO();
        unitDTO.setType(typeDisplayDTO);
        typeDisplayDTO.setCode(unitDO.getType());
        if (defServDO != null) {
            typeDisplayDTO.setDisplay(defServDO.getName());
        }

        unitDTO.setRelateId(unitDO.getRelateId());

        if (latestTaskDO != null) {
            TaskBaseDTO taskDTO = new TaskBaseDTO();
            unitDTO.setTask(taskDTO);

            taskDTO.setId(latestTaskDO.getId());
            DisplayDTO actionDisplayDTO = new DisplayDTO();
            taskDTO.setAction(actionDisplayDTO);
            actionDisplayDTO.setCode(latestTaskDO.getActionType());
            DictDO actionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE, latestTaskDO.getActionType());
            if (actionDictDO != null) {
                actionDisplayDTO.setDisplay(actionDictDO.getName());
            }

            DisplayDTO taskStateDisplayDTO = new DisplayDTO();
            taskDTO.setState(taskStateDisplayDTO);
            taskStateDisplayDTO.setCode(latestTaskDO.getState());
            DictDO taskStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.TASK_STATE, latestTaskDO.getState());
            if (taskStateDictDO != null) {
                taskStateDisplayDTO.setDisplay(taskStateDictDO.getName());

            }
        }

        if (cmService != null) {
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                if (cmUnits != null && cmUnits.size() > 0) {
                    for (CmService.Status.Unit cmUnit : cmUnits) {
                        if (cmUnit.getId().equals(unitDO.getRelateId())) {
                            unitDTO.setIp(cmUnit.getIp());

                            DisplayDTO stateDisplayDTO = new DisplayDTO();
                            unitDTO.setState(stateDisplayDTO);
                            stateDisplayDTO.setCode(cmUnit.getState());
                            DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, cmUnit.getState());
                            if (stateDictDO != null) {
                                stateDisplayDTO.setDisplay(stateDictDO.getName());
                            }

                            DisplayDTO podStateDisplayDTO = new DisplayDTO();
                            unitDTO.setPodState(podStateDisplayDTO);
                            podStateDisplayDTO.setCode(cmUnit.getPodState());
                            DictDO podStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.POD_STATE,
                                    cmUnit.getPodState());
                            if (podStateDictDO != null) {
                                podStateDisplayDTO.setDisplay(podStateDictDO.getName());
                            }

                            List<CmService.Port> ports = cmStatus.getPorts();
                            for (CmService.Port port : ports) {
                                if (port.getName().equals(CmConsts.PORT_NAME)) {
                                    unitDTO.setPort(port.getPort());
                                    break;
                                }
                            }

                            CmService.Status.Unit.Node cmNode = cmUnit.getNode();
                            if (cmNode != null) {
                                UnitBaseDTO.HostState hostDTO = unitDTO.new HostState();
                                hostDTO.setIp(cmNode.getHostIp());
                                hostDTO.setName(cmNode.getName());
                                unitDTO.setHost(hostDTO);

                                CmService.Status.Unit.Node.Cluster cmCluster = cmNode.getCluster();
                                if (cmCluster != null) {
                                    IdentificationDTO cluterDTO = new IdentificationDTO();
                                    hostDTO.setCluster(cluterDTO);
                                    cluterDTO.setId(cmCluster.getId());
                                    cluterDTO.setName(cmCluster.getName());
                                }

                                String nodeCondition = cmUnit.getNodeCondition();
                                String hostState = null;
                                if (nodeCondition != null) {
                                    if (nodeCondition.toLowerCase().equals("true")) {
                                        hostState = DictConsts.STATE_PASSING;
                                    } else if (nodeCondition.toLowerCase().equals("false")) {
                                        hostState = DictConsts.STATE_CRITICAL;
                                    } else {
                                        hostState = DictConsts.STATE_WARNNING;
                                    }
                                }
                                DisplayDTO hostStateDisplayDTO = new DisplayDTO();
                                hostDTO.setState(hostStateDisplayDTO);
                                hostStateDisplayDTO.setCode(hostState);
                                DictDO hostStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, hostState);
                                if (hostStateDictDO != null) {
                                    hostStateDisplayDTO.setDisplay(hostStateDictDO.getName());
                                }
                            }

                            CmImageBase cmImage = cmUnit.getImage();
                            if (cmImage != null) {
                                VersionDTO versionDTO = new VersionDTO();
                                versionDTO.setMajor(cmImage.getMajor());
                                versionDTO.setMinor(cmImage.getMinor());
                                versionDTO.setPatch(cmImage.getPatch());
                                versionDTO.setBuild(cmImage.getBuild());
                                unitDTO.setVersion(versionDTO);
                            }

                            CmService.Resources cmResources = cmUnit.getResources();
                            if (cmResources != null) {
                                if (cmResources.getMilicpu() != null) {
                                    double cpuCnt = NumberUnits.retainDigits(cmResources.getMilicpu() / 1000.0);
                                    unitDTO.setCpuCnt(cpuCnt);
                                }

                                if (cmResources.getMemory() != null) {
                                    double memSize = NumberUnits.retainDigits(cmResources.getMemory() / 1024.0);
                                    unitDTO.setMemSize(memSize);
                                }

                                CmService.Resources.Storage cmStorage = cmResources.getStorage();
                                if (cmStorage != null) {
                                    String diskType = null;
                                    if (CmConsts.STORAGE_TYPE_VOLUMEPATH.equals(cmStorage.getMode())) {
                                        CmService.Resources.Storage.VolumePath volumePath = cmStorage.getVolumePath();
                                        if (volumePath.getType().equals(CmConsts.STORAGE_LOCAL)
                                                && volumePath.getPerformance().equals(CmConsts.PERFORMANCE_MEDIUM)) {
                                            diskType = DictConsts.DISK_TYPE_LOCAL_HDD;
                                        } else if (volumePath.getType().equals(CmConsts.STORAGE_LOCAL)
                                                && volumePath.getPerformance().equals(CmConsts.PERFORMANCE_HIGH)) {
                                            diskType = DictConsts.DISK_TYPE_LOCAL_SSD;
                                        }
                                    }
                                    if (diskType != null) {
                                        DisplayDTO diskTypeDisplayDTO = new DisplayDTO();
                                        unitDTO.setDiskType(diskTypeDisplayDTO);
                                        diskTypeDisplayDTO.setCode(diskType);
                                        DictDO diskTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.DISK_TYPE,
                                                diskType);
                                        if (diskTypeDictDO != null) {
                                            diskTypeDisplayDTO.setDisplay(diskTypeDictDO.getName());
                                        }
                                    }

                                    List<CmService.Resources.Storage.Volume> cmVolumes = cmStorage.getVolumes();

                                    Long dataSize = 0L;
                                    Long logSize = 0L;
                                    for (CmService.Resources.Storage.Volume cmVolume : cmVolumes) {
                                        if (cmVolume.getCapacity() != null) {
                                            if (cmVolume.getType().equals(CmConsts.VOLUME_DATA)) {
                                                dataSize += cmVolume.getCapacity();
                                            } else if (cmVolume.getType().equals(CmConsts.VOLUME_LOG)) {
                                                logSize += cmVolume.getCapacity();
                                            }
                                        }
                                    }

                                    unitDTO.setDataSize(NumberUnits.ceil(dataSize / 1024.0));
                                    unitDTO.setLogSize(NumberUnits.ceil(logSize / 1024.0));
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}