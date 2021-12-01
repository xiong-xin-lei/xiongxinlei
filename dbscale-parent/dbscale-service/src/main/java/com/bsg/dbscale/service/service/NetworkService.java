package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmNetworkBody;
import com.bsg.dbscale.cm.model.CmNetwork;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmNetworkQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.NetworkCheck;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.IdentificationStatusDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.NetworkDTO;
import com.bsg.dbscale.service.form.NetworkForm;
import com.bsg.dbscale.service.query.NetworkQuery;

@Service
public class NetworkService extends BaseService {

    @Autowired
    private NetworkCheck networkCheck;

    public Result list(NetworkQuery networkQuery) throws Exception {
        List<NetworkDTO> networkDTOs = new ArrayList<>();
        CmNetworkQuery cmNetworkQuery = new CmNetworkQuery();
        cmNetworkQuery.setSiteId(networkQuery.getSiteId());
        cmNetworkQuery.setZone(networkQuery.getBusinessAreaId());
        cmNetworkQuery.setTopology(networkQuery.getTopology());
        cmNetworkQuery.setUnschedulable(BooleanUtils.negate(networkQuery.getEnabled()));

        List<CmNetwork> cmNetworks = CmApi.listNetwork(cmNetworkQuery);
        if (cmNetworks.size() > 0) {
            List<BusinessAreaDO> businessAreaDOs = businessAreaDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmNetwork cmNetwork : cmNetworks) {
                BusinessAreaDO businessAreaDO = findBusinessAreaDO(businessAreaDOs, cmNetwork.getZone());
                NetworkDTO networkDTO = getShowDTO(cmNetwork, businessAreaDO, dictTypeDOs);
                networkDTOs.add(networkDTO);
            }
        }

        return Result.success(networkDTOs);
    }

    public Result get(String networkId) throws Exception {
        NetworkDTO networkDTO = null;
        CmNetwork cmNetwork = CmApi.getNetwork(networkId);
        if (cmNetwork != null) {
            BusinessAreaDO businessAreaDO = businessAreaDAO.get(cmNetwork.getZone());
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            networkDTO = getShowDTO(cmNetwork, businessAreaDO, dictTypeDOs);
        }

        return Result.success(networkDTO);
    }

    public Result save(NetworkForm networkForm) throws Exception {
        CheckResult checkResult = networkCheck.checkSave(networkForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmNetworkBody cmNetworkBody = buildCmNetworkBodyForSave(networkForm);
        CmApi.saveNetwork(cmNetworkBody);

        return Result.success();
    }

    public Result update(String networkId, NetworkForm networkForm) throws Exception {
        CheckResult checkResult = networkCheck.checkUpdate(networkId, networkForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmNetworkBody cmNetworkBody = buildCmNetworkBodyForUpdate(networkForm);
        CmApi.updateNetwork(networkId, cmNetworkBody);

        return Result.success();
    }

    public Result enabled(String networkId, boolean enabled) throws Exception {
        CheckResult checkResult = networkCheck.checkEnabled(networkId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (enabled) {
            CmApi.enabledNetwork(networkId);
        } else {
            CmApi.disabledNetwork(networkId);
        }

        return Result.success();
    }

    public Result remove(String networkId) throws Exception {
        CheckResult checkResult = networkCheck.checkRemove(networkId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmApi.removeNetwork(networkId);

        return Result.success();
    }

    private NetworkDTO getShowDTO(CmNetwork cmNetwork, BusinessAreaDO businessAreaDO, List<DictTypeDO> dictTypeDOs) {
        NetworkDTO networkDTO = new NetworkDTO();
        networkDTO.setId(cmNetwork.getId());
        networkDTO.setName(cmNetwork.getName());
        CmNetwork.IpSummary cmIpSummary = cmNetwork.getIpSummary();
        if (cmIpSummary != null) {
            networkDTO.setStartIp(cmIpSummary.getStartIp());
            networkDTO.setEndIp(cmIpSummary.getEndIp());
            networkDTO.setGateway(cmIpSummary.getGateway());
            networkDTO.setNetmask(cmIpSummary.getPrefix());
            networkDTO.setVlan(cmIpSummary.getVlan());
            networkDTO.setIpTotal(cmIpSummary.getTotal());
            networkDTO.setIpUsed(cmIpSummary.getUsed());
        }

        List<DisplayDTO> topologyDTOs = new ArrayList<>();
        networkDTO.setTopologys(topologyDTOs);
        List<String> topologys = cmNetwork.getTopology();
        if (topologys != null) {
            for (String topology : topologys) {
                DisplayDTO displayDTO = new DisplayDTO();
                topologyDTOs.add(displayDTO);

                displayDTO.setCode(topology);
                DictDO dictDO = findDictDO(dictTypeDOs, DictTypeConsts.NETWORK_TOPOLOGY, topology);
                if (dictDO != null) {
                    displayDTO.setDisplay(dictDO.getName());
                }
            }
        }
        networkDTO.setEnabled(BooleanUtils.negate(cmNetwork.getUnschedulable()));
        networkDTO.setDescription(cmNetwork.getDesc());
        CmSiteBase cmSite = cmNetwork.getSite();
        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            networkDTO.setSite(siteDTO);

            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }

        IdentificationStatusDTO businessAreaDTO = new IdentificationStatusDTO();
        networkDTO.setBusinessArea(businessAreaDTO);

        businessAreaDTO.setId(cmNetwork.getZone());
        if (businessAreaDO != null) {
            businessAreaDTO.setName(businessAreaDO.getName());
            businessAreaDTO.setEnabled(businessAreaDO.getEnabled());
        }

        InfoDTO createdDTO = new InfoDTO();
        networkDTO.setCreated(createdDTO);
        createdDTO.setTimestamp(cmNetwork.getCreatedAt());

        return networkDTO;
    }

    private CmNetworkBody buildCmNetworkBodyForSave(NetworkForm networkForm) throws Exception {
        CmNetworkBody networkBody = new CmNetworkBody();
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(networkForm.getBusinessAreaId());
        networkBody.setSiteId(businessAreaDO.getSiteId());
        networkBody.setZone(networkForm.getBusinessAreaId());

        networkBody.setName(networkForm.getName());
        networkBody.setUnschedulable(BooleanUtils.negate(networkForm.getEnabled()));
        networkBody.setDesc(networkForm.getDescription());
        networkBody.setTopologys(networkForm.getTopologys());

        CmNetworkBody.IpSummary ipSummary = networkBody.new IpSummary();
        networkBody.setIpSummary(ipSummary);

        ipSummary.setStartIp(networkForm.getStartIp());
        ipSummary.setEndIp(networkForm.getEndIp());
        ipSummary.setGateway(networkForm.getGateway());
        ipSummary.setPrefix(networkForm.getNetmask());
        ipSummary.setVlan(networkForm.getVlan());

        return networkBody;
    }

    private CmNetworkBody buildCmNetworkBodyForUpdate(NetworkForm networkForm) {
        CmNetworkBody cmNetworkBody = new CmNetworkBody();
        if (networkForm.getBusinessAreaId() != null) {
            cmNetworkBody.setZone(networkForm.getBusinessAreaId());
        }

        if (networkForm.getName() != null) {
            cmNetworkBody.setName(networkForm.getName());
        }

        if (networkForm.getDescription() != null) {
            cmNetworkBody.setDesc(networkForm.getDescription());
        }

        if (networkForm.getTopologys() != null && networkForm.getTopologys().size() != 0) {
            cmNetworkBody.setTopologys(networkForm.getTopologys());
        }

        return cmNetworkBody;
    }

    public ObjModel getObjModel(String networkId) throws Exception {
        CmNetwork cmNetwork = CmApi.getNetwork(networkId);
        if (cmNetwork != null) {
            return new ObjModel(cmNetwork.getName(), cmNetwork.getSite().getId());
        }
        return null;
    }
}
