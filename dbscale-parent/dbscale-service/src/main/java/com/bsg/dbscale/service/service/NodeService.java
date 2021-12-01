package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmNode;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.cm.query.CmNodeQuery;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.HostDO;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.NodeDTO;
import com.bsg.dbscale.service.dto.StatisticsDTO;
import com.bsg.dbscale.service.query.NodeQuery;
import com.bsg.dbscale.util.DateUtils;
import com.bsg.dbscale.util.NumberUnits;

@Service
public class NodeService extends BaseService {

    public Result list(NodeQuery nodeQuery) throws Exception {
        List<NodeDTO> nodeDTOs = new ArrayList<>();
        CmNodeQuery cmNodeQuery = new CmNodeQuery();
        cmNodeQuery.setOs(nodeQuery.getOs());
        cmNodeQuery.setArch(nodeQuery.getArch());
        cmNodeQuery.setRole(nodeQuery.getRole());

        List<CmNode> cmNodes = CmApi.listNode(nodeQuery.getSiteId(), cmNodeQuery);
        if (cmNodes != null && cmNodes.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<HostDO> hostDOs = listHostDO(nodeQuery.getSiteId());
            for (CmNode cmNode : cmNodes) {
                NodeDTO nodeDTO = getShowDTO(cmNode, dictTypeDOs);
                boolean registered = false;
                for (HostDO hostDO : hostDOs) {
                    if (nodeDTO.getIp().equals(hostDO.getIp())) {
                        registered = true;
                        break;
                    }
                }
                if (!registered) {
                    nodeDTOs.add(nodeDTO);
                }
            }
        }

        return Result.success(nodeDTOs);
    }

    private List<HostDO> listHostDO(String siteId) throws Exception {
        List<HostDO> results = new ArrayList<>();
        List<HostDO> hostDOs = hostDAO.list(null);
        CmClusterQuery cmClusterQuery = new CmClusterQuery();
        cmClusterQuery.setSiteId(siteId);
        List<CmCluster> cmClusters = CmApi.listCluster(cmClusterQuery);
        for (HostDO hostDO : hostDOs) {
            CmCluster cmCluster = CmApi.findCluster(cmClusters, hostDO.getClusterId());
            if (cmCluster != null) {
                results.add(hostDO);
            }
        }
        return hostDOs;
    }

    private NodeDTO getShowDTO(CmNode cmNode, List<DictTypeDO> dictTypeDOs) {
        NodeDTO nodeDTO = new NodeDTO();
        CmNode.Metadata cmMetadata = cmNode.getMetadata();
        nodeDTO.setId(cmMetadata.getName());
        nodeDTO.setGmtCreated(DateUtils.dateTimeToString(DateUtils.parseDate(cmMetadata.getCreationTimestamp())));

        CmNode.Status cmStatus = cmNode.getStatus();
        if (cmStatus != null) {
            StatisticsDTO<Double> cpu = new StatisticsDTO<>();
            nodeDTO.setCpu(cpu);

            StatisticsDTO<Double> mem = new StatisticsDTO<>();
            nodeDTO.setMem(mem);

            StatisticsDTO<Double> storage = new StatisticsDTO<>();
            nodeDTO.setStorage(storage);

            CmNode.Status.Resource cmCapacity = cmStatus.getCapacity();
            if (cmCapacity != null) {
                Double cpuCapacity = 0.0;
                if (cmCapacity.getMilicpu() != null) {
                    cpuCapacity = NumberUnits.retainDigits(cmCapacity.getMilicpu() / 1000.0);
                }
                cpu.setCapacity(cpuCapacity);

                Double memCapacity = 0.0;
                if (cmCapacity.getMemory() != null) {
                    memCapacity = NumberUnits.retainDigits(cmCapacity.getMemory() / 1024.0);
                }
                mem.setCapacity(memCapacity);

                Double storageCapacity = 0.0;
                if (cmCapacity.getStorage() != null) {
                    storageCapacity = NumberUnits.retainDigits(cmCapacity.getStorage() / 1024.0);
                }
                storage.setCapacity(storageCapacity);

                CmNode.Status.Resource cmAllocatable = cmStatus.getAllocatable();
                if (cmAllocatable != null) {
                    Double cpuAllocatable = 0.0;
                    if (cmAllocatable.getMilicpu() != null) {
                        cpuAllocatable = NumberUnits.retainDigits(cmAllocatable.getMilicpu() / 1000.0);
                    }
                    cpu.setUsed(NumberUnits.retainDigits(cpuCapacity - cpuAllocatable));

                    Double memAllocatable = 0.0;
                    if (cmAllocatable.getMemory() != null) {
                        memAllocatable = NumberUnits.retainDigits(cmAllocatable.getMemory() / 1024.0);
                    }
                    mem.setUsed(NumberUnits.retainDigits(memCapacity - memAllocatable));

                    Double storageAllocatable = 0.0;
                    if (cmAllocatable.getStorage() != null) {
                        storageAllocatable = NumberUnits.retainDigits(cmAllocatable.getStorage() / 1024.0);
                    }
                    storage.setUsed(NumberUnits.retainDigits(storageCapacity - storageAllocatable));
                }

            }

            List<CmNode.Status.Address> addresses = cmStatus.getAddresses();
            if (addresses != null) {
                for (CmNode.Status.Address address : addresses) {
                    if (StringUtils.equalsIgnoreCase(address.getType(), "hostname")) {
                        nodeDTO.setName(address.getAddress());
                    } else if (StringUtils.equalsIgnoreCase(address.getType(), "internalip")) {
                        nodeDTO.setIp(address.getAddress());
                    }
                }
            }

            CmNode.Status.NodeInfo cmNodeInfo = cmStatus.getNodeInfo();
            if (cmNodeInfo != null) {
                nodeDTO.setKernelVersion(cmNodeInfo.getKernelVersion());
                nodeDTO.setOsImage(cmNodeInfo.getOsImage());
                nodeDTO.setContainerRuntimeVersion(cmNodeInfo.getContainerRuntimeVersion());
                nodeDTO.setKubeletVersion(cmNodeInfo.getKubeletVersion());
                nodeDTO.setKubeProxyVersion(cmNodeInfo.getKubeProxyVersion());
                nodeDTO.setOperatingSystem(cmNodeInfo.getOperatingSystem());

                DisplayDTO archDisplayDTO = new DisplayDTO();
                archDisplayDTO.setCode(cmNodeInfo.getArchitecture());
                DictDO archDictDO = findDictDO(dictTypeDOs, DictTypeConsts.SYS_ARCHITECTURE,
                        cmNodeInfo.getArchitecture());
                if (archDictDO != null) {
                    archDisplayDTO.setDisplay(archDictDO.getName());
                }
                nodeDTO.setArchitecture(archDisplayDTO);
            }

            List<CmNode.Status.Condition> cmConditions = cmStatus.getConditions();
            if (cmConditions != null) {
                for (CmNode.Status.Condition cmCondition : cmConditions) {
                    if (StringUtils.equalsIgnoreCase(cmCondition.getType(), "ready")) {
                        if (StringUtils.equalsIgnoreCase(cmCondition.getStatus(), "true")) {
                            nodeDTO.setEnabled(true);
                        } else {
                            nodeDTO.setEnabled(false);
                        }
                        nodeDTO.setMessage(cmCondition.getMessage());
                    }
                }
            }
        }
        return nodeDTO;
    }

}
