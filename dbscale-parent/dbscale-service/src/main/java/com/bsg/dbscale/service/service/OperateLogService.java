package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.dao.domain.OperateLogDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.OperateLogDTO;
import com.bsg.dbscale.service.query.OperateLogQuery;
import com.bsg.dbscale.util.DateUtils;

@Service
public class OperateLogService extends BaseService {

    public Result list(OperateLogQuery operateLogQuery, String activeUsername) throws Exception {
        List<OperateLogDTO> operateLogDTOs = new ArrayList<>();
        List<OperateLogDO> operateLogDOs = listQualifiedData(operateLogQuery, activeUsername);
        if (operateLogDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<UserDO> userDOs = userDAO.list(null);
            for (OperateLogDO operateLogDO : operateLogDOs) {
                CmSite cmSite = CmApi.findSite(cmSites, operateLogDO.getSiteId());
                OperateLogDTO operateLogDTO = getShowDTO(operateLogDO, cmSite, userDOs);
                operateLogDTOs.add(operateLogDTO);
            }
        }
        return Result.success(operateLogDTOs);
    }

    private List<OperateLogDO> listQualifiedData(OperateLogQuery operateLogQuery, String activeUsername) {
        List<OperateLogDO> results = new ArrayList<>();
        com.bsg.dbscale.dao.query.OperateLogQuery daoQuery = convertToDAOQuery(operateLogQuery);
        List<OperateLogDO> operateLogDOs = operateLogDAO.list(daoQuery);
        Set<String> usernames = listVisiableUserData(activeUsername);
        for (OperateLogDO operateLogDO : operateLogDOs) {
            if (usernames.contains(operateLogDO.getCreator())) {
                results.add(operateLogDO);
            }
        }
        return results;
    }

    private com.bsg.dbscale.dao.query.OperateLogQuery convertToDAOQuery(OperateLogQuery operateLogQuery) {
        com.bsg.dbscale.dao.query.OperateLogQuery daoQuery = new com.bsg.dbscale.dao.query.OperateLogQuery();
        daoQuery.setSiteId(operateLogQuery.getSiteId());
        if (operateLogQuery.getStart() != null) {
            daoQuery.setStart(new Date(operateLogQuery.getStart() * 1000));
        }
        if (operateLogQuery.getEnd() != null) {
            daoQuery.setEnd(new Date(operateLogQuery.getEnd() * 1000));
        }
        return daoQuery;
    }

    private OperateLogDTO getShowDTO(OperateLogDO operateLogDO, CmSite cmSite, List<UserDO> userDOs) {
        OperateLogDTO operateLogDTO = new OperateLogDTO();
        operateLogDTO.setId(operateLogDO.getId());
        operateLogDTO.setObjType(operateLogDO.getObjType());
        operateLogDTO.setObjName(operateLogDO.getObjName());
        operateLogDTO.setDescription(operateLogDO.getDescription());
        InfoDTO createdDTO = new InfoDTO();
        operateLogDTO.setCreated(createdDTO);
        createdDTO.setUsername(operateLogDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, operateLogDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(operateLogDO.getGmtCreate()));

        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            operateLogDTO.setSite(siteDTO);

            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }
        return operateLogDTO;
    }

}
