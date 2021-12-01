package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmClusterBody;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.ClusterCheck;
import com.bsg.dbscale.service.dto.ClusterDTO;
import com.bsg.dbscale.service.dto.DefServDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.IdentificationStatusDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.form.ClusterForm;
import com.bsg.dbscale.service.query.ClusterQuery;

@Service
public class ClusterService extends BaseService {

    @Autowired
    private ClusterCheck clusterCheck;

    public Result list(ClusterQuery clusterQuery) throws Exception {
        List<ClusterDTO> clusterDTOs = new ArrayList<>();
        CmClusterQuery cmClusterQuery = new CmClusterQuery();
        cmClusterQuery.setSiteId(clusterQuery.getSiteId());
        cmClusterQuery.setZone(clusterQuery.getBusinessAreaId());
        cmClusterQuery.setUnschedulable(BooleanUtils.negate(clusterQuery.getEnabled()));

        List<CmCluster> cmClusters = CmApi.listCluster(cmClusterQuery);
        if (cmClusters != null && cmClusters.size() > 0) {
            List<BusinessAreaDO> businessAreaDOs = businessAreaDAO.list(null);
            List<DefServDO> defServDOs = defServDAO.list(null);
            for (CmCluster cmCluster : cmClusters) {
                BusinessAreaDO businessAreaDO = findBusinessAreaDO(businessAreaDOs, cmCluster.getZone());
                ClusterDTO clusterDTO = getShowDTO(cmCluster, businessAreaDO, defServDOs);
                clusterDTOs.add(clusterDTO);
            }
        }

        return Result.success(clusterDTOs);
    }

    public Result get(String clusterId) throws Exception {
        ClusterDTO clusterDTO = null;
        CmCluster cmCluster = CmApi.getCluster(clusterId);
        if (cmCluster != null) {
            BusinessAreaDO businessAreaDO = businessAreaDAO.get(cmCluster.getZone());
            List<DefServDO> defServDOs = defServDAO.list(null);
            clusterDTO = getShowDTO(cmCluster, businessAreaDO, defServDOs);
        }

        return Result.success(clusterDTO);
    }

    public Result save(ClusterForm clusterForm) throws Exception {
        CheckResult checkResult = clusterCheck.checkSave(clusterForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmClusterBody cmClusterBody = buildCmClusterBodyForSave(clusterForm);
        CmApi.saveCluster(cmClusterBody);

        return Result.success();
    }

    public Result update(String clusterId, ClusterForm clusterForm) throws Exception {
        CheckResult checkResult = clusterCheck.checkUpdate(clusterId, clusterForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmClusterBody cmClusterBody = buildCmClusterBodyForUpdate(clusterForm);
        CmApi.updateCluster(clusterId, cmClusterBody);

        return Result.success();
    }

    public Result enabled(String clusterId, boolean enabled) throws Exception {
        CheckResult checkResult = clusterCheck.checkEnabled(clusterId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (enabled) {
            CmApi.enabledCluster(clusterId);
        } else {
            CmApi.disabledCluster(clusterId);
        }

        return Result.success();
    }

    public Result remove(String clusterId) throws Exception {
        CheckResult checkResult = clusterCheck.checkRemove(clusterId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmApi.removeCluster(clusterId);

        return Result.success();
    }

    private ClusterDTO getShowDTO(CmCluster cmCluster, BusinessAreaDO businessAreaDO, List<DefServDO> defServDOs) {
        ClusterDTO clusterDTO = new ClusterDTO();

        clusterDTO.setId(cmCluster.getId());
        clusterDTO.setName(cmCluster.getName());
        List<String> imageTypes = cmCluster.getImageTypes();
        List<DefServDTO> defServDTOs = new ArrayList<>();
        clusterDTO.setImageTypes(defServDTOs);
        for (String imageType : imageTypes) {
            DefServDTO defServDTO = new DefServDTO();
            defServDTOs.add(defServDTO);

            defServDTO.setCode(imageType);
            DefServDO defServDO = findDefServDO(defServDOs, imageType);
            if (defServDO != null) {
                defServDTO.setName(defServDO.getName());
                defServDTO.setStateful(defServDO.getStateful());
                defServDTO.setEnabled(defServDO.getEnabled());
                defServDTO.setDescription(defServDO.getDescription());
            }
        }
        clusterDTO.setHaTag(cmCluster.getHaTag());
        clusterDTO.setEnabled(BooleanUtils.negate(cmCluster.getUnschedulable()));
        clusterDTO.setDescription(cmCluster.getDesc());

        CmSiteBase cmSite = cmCluster.getSite();
        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            clusterDTO.setSite(siteDTO);

            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }

        IdentificationStatusDTO businessAreaDTO = new IdentificationStatusDTO();
        clusterDTO.setBusinessArea(businessAreaDTO);

        businessAreaDTO.setId(cmCluster.getZone());
        if (businessAreaDO != null) {
            businessAreaDTO.setName(businessAreaDO.getName());
            businessAreaDTO.setEnabled(businessAreaDO.getEnabled());
        }

        InfoDTO createdDTO = new InfoDTO();
        clusterDTO.setCreated(createdDTO);
        createdDTO.setTimestamp(cmCluster.getCreatedAt());

        return clusterDTO;
    }

    private CmClusterBody buildCmClusterBodyForSave(ClusterForm clusterForm) {
        CmClusterBody clusterBody = new CmClusterBody();
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(clusterForm.getBusinessAreaId());
        clusterBody.setSiteId(businessAreaDO.getSiteId());
        clusterBody.setZone(clusterForm.getBusinessAreaId());
        clusterBody.setName(clusterForm.getName());
        clusterBody.setUnschedulable(BooleanUtils.negate(clusterForm.getEnabled()));
        clusterBody.setHaTag(clusterForm.getHaTag());
        clusterBody.setImageTypes(clusterForm.getImageTypes());
        clusterBody.setDesc(clusterForm.getDescription());
        return clusterBody;
    }

    private CmClusterBody buildCmClusterBodyForUpdate(ClusterForm clusterForm) {
        CmClusterBody cmClusterBody = new CmClusterBody();
        if (clusterForm.getBusinessAreaId() != null) {
            cmClusterBody.setZone(clusterForm.getBusinessAreaId());
        }

        if (clusterForm.getName() != null) {
            cmClusterBody.setName(clusterForm.getName());
        }

        if (clusterForm.getHaTag() != null) {
            cmClusterBody.setHaTag(clusterForm.getHaTag());
        }

        if (clusterForm.getDescription() != null) {
            cmClusterBody.setDesc(clusterForm.getDescription());
        }

        if (clusterForm.getImageTypes() != null) {
            cmClusterBody.setImageTypes(clusterForm.getImageTypes());
        }

        return cmClusterBody;
    }

    public ObjModel getObjModel(String clusterId) throws Exception {
        CmCluster cmCluster = CmApi.getCluster(clusterId);
        if (cmCluster != null) {
            String siteId = null;
            CmSiteBase cmSite = cmCluster.getSite();
            if (cmSite != null) {
                siteId = cmSite.getId();
            }
            return new ObjModel(cmCluster.getName(), siteId);
        }
        return null;
    }
}
