package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.dao.domain.GroupDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.GroupCheck;
import com.bsg.dbscale.service.dto.GroupDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.form.GroupForm;
import com.bsg.dbscale.service.query.GroupQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.util.DateUtils;

@Service
public class GroupService extends BaseService {

    @Autowired
    private GroupCheck groupCheck;

    @Transactional
    public Result list(GroupQuery groupQuery) throws Exception {
        List<GroupDTO> groupDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.GroupQuery daoQuery = convertToDAOQuery(groupQuery);
        List<GroupDO> groupDOs = groupDAO.list(daoQuery);

        if (groupDOs.size() > 0) {
            List<UserDO> userDOs = userDAO.list(null);
            for (GroupDO groupDO : groupDOs) {
                GroupDTO groupDTO = getShowDTO(groupDO, userDOs);
                groupDTOs.add(groupDTO);
            }
        }
        return Result.success(groupDTOs);
    }

    public Result get(String groupId) {
        GroupDTO groupDTO = null;
        GroupDO groupDO = groupDAO.get(groupId);
        if (groupDO != null) {
            List<UserDO> userDOs = userDAO.list(null);
            groupDTO = getShowDTO(groupDO, userDOs);
        }

        return Result.success(groupDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(GroupForm groupForm, String activeUsername) {
        CheckResult checkResult = groupCheck.checkSave(groupForm, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        GroupDO groupDO = buildGroupDOForSave(groupForm, nowDate, activeUsername);

        groupDAO.save(groupDO);
        groupUserDAO.save(groupDO.getId(), activeUsername);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String groupId, GroupForm groupForm, String activeUsername) {
        CheckResult checkResult = groupCheck.checkUpdate(groupId, groupForm, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        GroupDO groupDO = buildGroupDOForUpdate(groupId, groupForm);

        groupDAO.update(groupDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String groupId, String activeUsername) {
        CheckResult checkResult = groupCheck.checkRemove(groupId, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        GroupDO groupDO = groupDAO.get(groupId);
        if (groupDO.getCreator().equals(activeUsername)) {
            groupDAO.remove(groupId);
            groupUserDAO.removeByGroupId(groupId);
        } else {
            groupUserDAO.remove(groupId, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveUsers(String groupId, List<String> usernames, String activeUsername) {
        CheckResult checkResult = groupCheck.checkSaveUser(groupId, activeUsername);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        for (String username : usernames) {
            int cnt = groupUserDAO.countByGroupIdAndUsername(groupId, username);
            if (cnt == 0) {
                groupUserDAO.save(groupId, username);
            }
        }
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result removeUser(String groupId, String username) {
        CheckResult checkResult = groupCheck.checkRemoveUser(groupId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        GroupDO groupDO = groupDAO.get(groupId);
        if (!groupDO.getCreator().equals(username)) {
            groupUserDAO.remove(groupId, username);
        }
        return Result.success();
    }

    private com.bsg.dbscale.dao.query.GroupQuery convertToDAOQuery(GroupQuery groupQuery) {
        com.bsg.dbscale.dao.query.GroupQuery daoQuery = new com.bsg.dbscale.dao.query.GroupQuery();
        daoQuery.setUsername(groupQuery.getOwner());
        return daoQuery;
    }

    private GroupDTO getShowDTO(GroupDO groupDO, List<UserDO> userDOs) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(groupDO.getId());
        groupDTO.setName(groupDO.getName());
        groupDTO.setDescription(groupDO.getDescription());
        groupDTO.setSys(groupDO.getSys());
        InfoDTO createdDTO = new InfoDTO();
        groupDTO.setCreated(createdDTO);
        createdDTO.setUsername(groupDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, groupDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(groupDO.getGmtCreate()));
        return groupDTO;
    }

    public GroupDO buildGroupDOForSave(GroupForm groupForm, Date nowDate, String activeUsername) {
        GroupDO groupDO = new GroupDO();
        groupDO.setId(PrimaryKeyUtil.get());
        groupDO.setName(groupForm.getName());
        groupDO.setSys(false);
        groupDO.setDescription(StringUtils.trimToEmpty(groupForm.getDescription()));
        groupDO.setCreator(activeUsername);
        groupDO.setGmtCreate(nowDate);
        return groupDO;
    }

    private GroupDO buildGroupDOForUpdate(String groupId, GroupForm groupForm) {
        GroupDO groupDO = groupDAO.get(groupId);

        if (groupForm.getName() != null) {
            groupDO.setName(groupForm.getName());
        }
        if (groupForm.getDescription() != null) {
            groupDO.setDescription(groupForm.getDescription());
        }
        return groupDO;
    }

    public ObjModel getObjModel(String groupId) {
        GroupDO groupDO = groupDAO.get(groupId);
        if (groupDO != null) {
            return new ObjModel(groupDO.getName(), null);
        }
        return null;
    }
}
