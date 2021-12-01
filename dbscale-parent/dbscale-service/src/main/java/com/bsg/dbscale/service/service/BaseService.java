package com.bsg.dbscale.service.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.dao.AppDAO;
import com.bsg.dbscale.dao.dao.BackupStrategyDAO;
import com.bsg.dbscale.dao.dao.BusinessAreaDAO;
import com.bsg.dbscale.dao.dao.BusinessSubsystemDAO;
import com.bsg.dbscale.dao.dao.BusinessSystemDAO;
import com.bsg.dbscale.dao.dao.CrontabCfgDAO;
import com.bsg.dbscale.dao.dao.DefServDAO;
import com.bsg.dbscale.dao.dao.DictDAO;
import com.bsg.dbscale.dao.dao.DictTypeDAO;
import com.bsg.dbscale.dao.dao.ForceRebuildLogDAO;
import com.bsg.dbscale.dao.dao.GroupDAO;
import com.bsg.dbscale.dao.dao.GroupUserDAO;
import com.bsg.dbscale.dao.dao.HostDAO;
import com.bsg.dbscale.dao.dao.OperateLogDAO;
import com.bsg.dbscale.dao.dao.OrderCfgDAO;
import com.bsg.dbscale.dao.dao.OrderDAO;
import com.bsg.dbscale.dao.dao.OrderGroupDAO;
import com.bsg.dbscale.dao.dao.PrivilegeDAO;
import com.bsg.dbscale.dao.dao.RoleCfgAppDAO;
import com.bsg.dbscale.dao.dao.RoleDAO;
import com.bsg.dbscale.dao.dao.ScaleDAO;
import com.bsg.dbscale.dao.dao.ServDAO;
import com.bsg.dbscale.dao.dao.ServGroupDAO;
import com.bsg.dbscale.dao.dao.SubtaskCfgDAO;
import com.bsg.dbscale.dao.dao.SubtaskDAO;
import com.bsg.dbscale.dao.dao.SystemDAO;
import com.bsg.dbscale.dao.dao.TaskDAO;
import com.bsg.dbscale.dao.dao.UnitDAO;
import com.bsg.dbscale.dao.dao.UserDAO;
import com.bsg.dbscale.dao.domain.AppDO;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.GroupDO;
import com.bsg.dbscale.dao.domain.HostDO;
import com.bsg.dbscale.dao.domain.OperateLogDO;
import com.bsg.dbscale.dao.domain.ScaleDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.util.TaskResult;
import com.bsg.dbscale.util.NumberUnits;

@Service
public class BaseService {

    @Autowired
    protected SystemDAO systemDAO;

    @Autowired
    protected OperateLogDAO operateLogDAO;

    @Autowired
    protected GroupDAO groupDAO;

    @Autowired
    protected RoleDAO roleDAO;

    @Autowired
    protected RoleCfgAppDAO roleCfgAppDAO;

    @Autowired
    protected UserDAO userDAO;

    @Autowired
    protected GroupUserDAO groupUserDAO;

    @Autowired
    protected DictTypeDAO dictTypeDAO;

    @Autowired
    protected DictDAO dictDAO;

    @Autowired
    protected AppDAO appDAO;

    @Autowired
    protected DefServDAO defServDAO;

    @Autowired
    protected TaskDAO taskDAO;

    @Autowired
    protected SubtaskDAO subtaskDAO;

    @Autowired
    protected SubtaskCfgDAO subtaskCfgDAO;

    @Autowired
    protected BusinessAreaDAO businessAreaDAO;

    @Autowired
    protected HostDAO hostDAO;

    @Autowired
    protected BusinessSystemDAO businessSystemDAO;

    @Autowired
    protected BusinessSubsystemDAO businessSubsystemDAO;

    @Autowired
    protected ScaleDAO scaleDAO;

    @Autowired
    protected OrderCfgDAO orderCfgDAO;

    @Autowired
    protected OrderDAO orderDAO;

    @Autowired
    protected OrderGroupDAO orderGroupDAO;

    @Autowired
    protected ServGroupDAO servGroupDAO;

    @Autowired
    protected ServDAO servDAO;

    @Autowired
    protected UnitDAO unitDAO;

    @Autowired
    protected PrivilegeDAO privilegeDAO;

    @Autowired
    protected BackupStrategyDAO backupStrategyDAO;

    @Autowired
    protected CrontabCfgDAO crontabCfgDAO;

    @Autowired
    protected ForceRebuildLogDAO forceRebuildLogDAO;

    @Autowired
    protected ThreadPoolTaskExecutor executor;

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static String FILEPATH = File.separator + "etc" + File.separator + "dbscale" + File.separator
            + "sys.properties";

    public Properties getProperties() throws Exception {
        FileSystemResource file = new FileSystemResource(FILEPATH);
        if (!file.exists()) {
            throw new FileNotFoundException(FILEPATH + " not found。");
        }
        Properties prop = PropertiesLoaderUtils.loadProperties(file);
        return prop;
    }

    private Map<Integer, List<SubtaskDO>> convertToTreeMap(List<SubtaskDO> subtaskDOs) {
        Map<Integer, List<SubtaskDO>> treeMap = new TreeMap<>();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            int priority = subtaskDO.getPriority();
            if (treeMap.containsKey(priority)) {
                treeMap.get(priority).add(subtaskDO);
            } else {
                List<SubtaskDO> subs = new ArrayList<>();
                subs.add(subtaskDO);
                treeMap.put(priority, subs);
            }
        }
        return treeMap;
    }

    public Set<String> listVisiableUserData(String username) {
        Set<String> usernames = new HashSet<>();
        if (StringUtils.isBlank(username)) {
            return usernames;
        }
        List<UserDO> userDOs = userDAO.list(null);
        UserDO curUserDO = findUserDO(userDOs, username);
        if (curUserDO == null) {
            return usernames;
        }
        usernames.add(username);

        String dataScope = curUserDO.getRole().getDataScope();
        if (DictConsts.DATA_SCOPE_ALL.equals(dataScope)) {
            for (UserDO userDO : userDOs) {
                if (!userDO.getUsername().equals(username)) {
                    usernames.add(userDO.getUsername());
                }
            }
        } else if (DictConsts.DATA_SCOPE_GROUP.equals(dataScope)) {
            for (UserDO userDO : userDOs) {
                if (!userDO.getUsername().equals(username)) {
                    boolean sameGroup = isSameGroup(curUserDO, userDO);
                    if (sameGroup) {
                        usernames.add(userDO.getUsername());
                    }
                }
            }
        }
        return usernames;
    }

    private boolean isSameGroup(UserDO userDO1, UserDO userDO2) {
        List<GroupDO> groupDOs1 = userDO1.getGroups();
        List<GroupDO> groupDOs2 = userDO2.getGroups();
        for (GroupDO groupDO1 : groupDOs1) {
            for (GroupDO groupDO2 : groupDOs2) {
                if (groupDO1.getId().equals(groupDO2.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public class Task implements Runnable {
        private TaskDO taskDO;

        public Task(TaskDO taskDO) {
            this.taskDO = taskDO;
        }

        @Override
        public void run() {
            List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
            int subtaskCnt = subtaskDOs.size();

            String logPrefix = taskDO.getObjType() + " " + taskDO.getActionType() + " task";
            logger.info("{}: start build concurrent subtask queues。", logPrefix);
            String state = null;
            try {
                // 构建并发异步任务
                Map<Integer, List<SubtaskDO>> subtaskDOsMap = convertToTreeMap(subtaskDOs);

                int curSubtaskIndex = 0;

                logger.info("{}: start task.", logPrefix);
                for (Map.Entry<Integer, List<SubtaskDO>> entry : subtaskDOsMap.entrySet()) {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if (curSubtaskIndex == 0) {
                        logger.info("{}: start update task start time and state.", logPrefix);
                        taskDO.setStartDateTime(nowDate);
                        taskDO.setState(DictConsts.TASK_STATE_RUNNING);
                        taskDAO.updateToStart(taskDO);

                        executeTaskStart(taskDO);
                    }

                    List<SubtaskDO> concurrencySubtaskDOs = entry.getValue();

                    List<Future<TaskResult>> resultList = new ArrayList<Future<TaskResult>>();
                    for (SubtaskDO subtaskDO : concurrencySubtaskDOs) {
                        String format = "{}({}/{}:{})";
                        FormattingTuple ft = MessageFormatter.arrayFormat(format,
                                new Object[] { logPrefix, curSubtaskIndex + 1, subtaskCnt,
                                        subtaskDO.getActionType() + " " + subtaskDO.getObjName() });
                        Future<TaskResult> future = executor
                                .submit(new Subtask(taskDO, subtaskDO, nowDate, ft.getMessage()));
                        resultList.add(future);
                        curSubtaskIndex++;
                    }

                    while (true) {
                        boolean isAllDone = true;
                        for (Future<TaskResult> fs : resultList) {
                            isAllDone &= (fs.isDone() || fs.isCancelled());
                        }
                        if (isAllDone) {
                            break;
                        }
                        Thread.sleep(1000);
                    }

                    for (Future<TaskResult> fs : resultList) {
                        state = fs.get().getState();
                        if (state.equals(DictConsts.TASK_STATE_FAILED) || state.equals(DictConsts.TASK_STATE_TIMEOUT)) {
                            return;
                        }
                    }
                }

            } catch (Exception e) {
                logger.error("{}: exception.", e);
                state = DictConsts.TASK_STATE_FAILED;
            } finally {
                if (taskDO.getEndDateTime() == null) {
                    taskDO.setState(state);

                    try {
                        executeTaskDone(taskDO);
                    } catch (Exception e) {
                        logger.error("{}: exception.", e);
                    }

                    logger.info("{}: start update task end time and state.", logPrefix);
                    Date endDate = getLastEndDate(subtaskDOs);

                    taskDO.setEndDateTime(endDate);
                    taskDAO.updateToEnd(taskDO);
                }
                logger.info("{}: task completed({}).", logPrefix, state);
            }
        }
    }

    private Date getLastEndDate(List<SubtaskDO> subtaskDOs) {
        long time = 0L;
        for (SubtaskDO subtaskDO : subtaskDOs) {
            if (subtaskDO.getEndDateTime() != null) {
                if (subtaskDO.getEndDateTime().getTime() > time) {
                    time = subtaskDO.getEndDateTime().getTime();
                }
            }
        }
        if (time == 0) {
            return systemDAO.getCurrentSqlDateTime();
        }
        return new Date(time);
    }

    private class Subtask implements Callable<TaskResult> {
        private TaskDO taskDO;
        private SubtaskDO subtaskDO;
        private String logPrefix;
        private Date nowDate;

        private Subtask(TaskDO taskDO, SubtaskDO subtaskDO, Date nowDate, String logPrefix) {
            this.taskDO = taskDO;
            this.subtaskDO = subtaskDO;
            this.logPrefix = logPrefix;
            this.nowDate = nowDate;
        }

        @Override
        public TaskResult call() throws Exception {
            TaskResult result = new TaskResult();
            result.setState(DictConsts.TASK_STATE_RUNNING);
            try {
                logger.info("{}: start subtask.", logPrefix);
                logger.info("{}: start update subtask start time and state.", logPrefix);
                subtaskDO.setStartDateTime(nowDate);
                subtaskDO.setState(DictConsts.TASK_STATE_RUNNING);
                subtaskDAO.updateToStart(subtaskDO);

                result = executeSubtask(taskDO, subtaskDO);
            } catch (Exception e) {
                logger.error("exception:", e);
                result.setState(DictConsts.TASK_STATE_FAILED);
                result.setMsg(e.toString());
            } finally {
                if (subtaskDO.getEndDateTime() == null) {
                    logger.info("{}: start update subtask end time and state and msg.", logPrefix);
                    Date endDate = systemDAO.getCurrentSqlDateTime();
                    subtaskDO.setEndDateTime(endDate);
                    subtaskDO.setState(result.getState());
                    subtaskDO.setMsg(result.getMsg());
                    subtaskDAO.updateToEnd(subtaskDO);
                }
                logger.info("{}: subtask completed.", logPrefix);
            }
            return result;
        }

    }

    // public TaskResult pollingCmTask(String cmTaskId) throws Exception {
    // TaskResult taskResult = new TaskResult();
    // SubtaskDO subtaskDO = subtaskDAO.getByRelateId(cmTaskId);
    // taskResult.setState(subtaskDO.getState());
    // while (true) {
    // if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
    // CmTask cmTask = CmApi.getTask(cmTaskId);
    // if (cmTask == null) {
    // taskResult.setState(DictConsts.TASK_STATE_FAILED);
    // taskResult.setMsg("none task");
    // return taskResult;
    // }
    // if (cmTask.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
    // Date nowDate = systemDAO.getCurrentSqlDateTime();
    // if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 >
    // subtaskDO.getTimeout()) {
    // taskResult.setState(DictConsts.TASK_STATE_FAILED);
    // taskResult.setMsg("timeout");
    // return taskResult;
    // } else {
    // Thread.sleep(10000);
    // subtaskDO = subtaskDAO.getByRelateId(cmTaskId);
    // }
    // } else {
    // taskResult.setState(cmTask.getState());
    // taskResult.setMsg(cmTask.getError());
    // return taskResult;
    // }
    // } else {
    // taskResult.setState(subtaskDO.getState());
    // taskResult.setMsg(subtaskDO.getMsg());
    // return taskResult;
    // }
    // }
    // }

    public void executeTaskStart(TaskDO taskDO) {
    }

    public TaskResult executeSubtask(TaskDO taskDO, SubtaskDO subtaskDO) {
        return null;
    }

    public void executeTaskDone(TaskDO taskDO) {
    }

    public UserDO findUserDO(List<UserDO> userDOs, String username) {
        if (userDOs == null || userDOs.size() == 0 || StringUtils.isBlank(username)) {
            return null;
        }

        for (UserDO userDO : userDOs) {
            if (userDO.getUsername().equals(username)) {
                return userDO;
            }
        }
        return null;
    }

    public GroupDO findGroupDO(List<GroupDO> groupDOs, String groupId) {
        if (groupDOs == null || groupDOs.size() == 0 || StringUtils.isBlank(groupId)) {
            return null;
        }

        for (GroupDO groupDO : groupDOs) {
            if (groupDO.getId().equals(groupId)) {
                return groupDO;
            }
        }
        return null;
    }

    public DictDO findDictDO(List<DictTypeDO> dictTypeDOs, String dictTypeCode, String dictCode) {
        if (dictTypeDOs == null || dictTypeDOs.size() == 0 || StringUtils.isBlank(dictTypeCode)
                || StringUtils.isBlank(dictCode)) {
            return null;
        }

        for (DictTypeDO dictTypeDO : dictTypeDOs) {
            if (dictTypeDO.getCode().equals(dictTypeCode)) {
                List<DictDO> dictDOs = dictTypeDO.getDicts();
                for (DictDO dictDO : dictDOs) {
                    if (dictDO.getCode().equals(dictCode)) {
                        return dictDO;
                    }
                }
                break;
            }
        }
        return null;
    }

    public DefServDO findDefServDO(List<DefServDO> defServDOs, String defServCode) {
        if (defServDOs == null || defServDOs.size() == 0 || StringUtils.isBlank(defServCode)) {
            return null;
        }

        for (DefServDO defServDO : defServDOs) {
            if (defServDO.getCode().equals(defServCode)) {
                return defServDO;
            }
        }
        return null;
    }

    public BusinessAreaDO findBusinessAreaDO(List<BusinessAreaDO> businessAreaDOs, String id) {
        if (businessAreaDOs == null || businessAreaDOs.size() == 0 || StringUtils.isBlank(id)) {
            return null;
        }

        for (BusinessAreaDO businessAreaDO : businessAreaDOs) {
            if (businessAreaDO.getId().equals(id)) {
                return businessAreaDO;
            }
        }
        return null;
    }

    public HostDO findHostDOByRelateId(List<HostDO> hostDOs, String hostRelateId) {
        if (hostDOs == null || hostDOs.size() == 0 || StringUtils.isBlank(hostRelateId)) {
            return null;
        }

        for (HostDO hostDO : hostDOs) {
            if (hostDO.getRelateId().equals(hostRelateId)) {
                return hostDO;
            }
        }
        return null;
    }

    public TaskDO findTaskDO(List<TaskDO> taskDOs, String taskId) {
        if (taskDOs == null || taskDOs.size() == 0 || StringUtils.isBlank(taskId)) {
            return null;
        }

        for (TaskDO taskDO : taskDOs) {
            if (taskDO.getId().equals(taskId)) {
                return taskDO;
            }
        }
        return null;
    }

    public TaskDO findLasterTaskDO(List<TaskDO> latestTaskDOs, String objType, String objId) {
        if (latestTaskDOs == null || latestTaskDOs.size() == 0 || StringUtils.isBlank(objType)
                || StringUtils.isBlank(objId)) {
            return null;
        }

        for (TaskDO taskDO : latestTaskDOs) {
            if (taskDO.getObjType().equals(objType) && taskDO.getObjId().equals(objId)) {
                return taskDO;
            }
        }
        return null;
    }

    public ScaleDO findScaleDO(List<ScaleDO> scaleDOs, String type, Double cpuCnt, Double memSize) {
        if (scaleDOs == null || scaleDOs.size() == 0 || StringUtils.isBlank(type) || cpuCnt == null
                || memSize == null) {
            return null;
        }

        for (ScaleDO scaleDO : scaleDOs) {
            if (scaleDO.getType().equals(type) && scaleDO.getCpuCnt().equals(cpuCnt)
                    && scaleDO.getMemSize().equals(memSize)) {
                return scaleDO;
            }
        }
        return null;
    }

    public ServGroupDO findServGroupDOByName(List<ServGroupDO> servGroupDOs, String name) {
        if (servGroupDOs == null || servGroupDOs.size() == 0 || StringUtils.isBlank(name)) {
            return null;
        }

        for (ServGroupDO servGroupDO : servGroupDOs) {
            if (servGroupDO.getName().equals(name)) {
                return servGroupDO;
            }
        }
        return null;
    }

    public ServGroupDO findServGroupDOByRelateId(List<ServGroupDO> servGroupDOs, String relateId) {
        if (servGroupDOs == null || servGroupDOs.size() == 0 || StringUtils.isBlank(relateId)) {
            return null;
        }

        for (ServGroupDO servGroupDO : servGroupDOs) {
            List<ServDO> servDOs = servGroupDO.getServs();
            for (ServDO servDO : servDOs) {
                if (servDO.getRelateId() != null && servDO.getRelateId().equals(relateId)) {
                    return servGroupDO;
                }
            }
        }
        return null;
    }

    public ServDO findServDOByRelateId(ServGroupDO servGroupDO, String relateId) {
        if (servGroupDO == null || StringUtils.isBlank(relateId)) {
            return null;
        }

        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            if (servDO.getRelateId() != null && servDO.getRelateId().equals(relateId)) {
                return servDO;
            }
        }
        return null;
    }

    public ServDO findAnyServDOType(ServGroupDO servGroupDO, String type) {
        if (servGroupDO == null) {
            return null;
        }
        List<ServDO> servDOs = servGroupDO.getServs();
        return findAnyServDOType(servDOs, type);
    }

    public ServDO findAnyServDOType(List<ServDO> servDOs, String type) {
        if (servDOs == null || servDOs.size() == 0 || StringUtils.isBlank(type)) {
            return null;
        }

        for (ServDO servDO : servDOs) {
            if (type.equals(servDO.getType())) {
                return servDO;
            }
        }
        return null;
    }

    public UnitDO findUnitDOByRelateId(ServDO servDO, String relateId) {
        if (servDO == null || StringUtils.isBlank(relateId)) {
            return null;
        }

        List<UnitDO> unitDOs = servDO.getUnits();
        for (UnitDO unitDO : unitDOs) {
            if (unitDO.getRelateId() != null && unitDO.getRelateId().equals(relateId)) {
                return unitDO;
            }
        }
        return null;
    }

    public AppDO findAppDO(List<AppDO> appDOs, Long appId) {
        if (appDOs != null) {
            for (AppDO appDO : appDOs) {
                if (appDO.getId().equals(appId)) {
                    return appDO;
                }
            }
        }
        return null;
    }

    // public List<String> listVisiableUserData(String username, String type) {
    // List<String> usernames = new ArrayList<>();
    // if (StringUtils.isBlank(username) || StringUtils.isBlank(type)) {
    // return usernames;
    // }
    // List<UserDO> userDOs = userDAO.list(null);
    // UserDO userDO = findUserDO(userDOs, username);
    // if (userDO == null) {
    // return usernames;
    // }
    // List<GroupDO> groupDOs = userDO.getGroups();
    // List<String> groupIds = new ArrayList<>();
    // for (GroupDO groupDO : groupDOs) {
    // groupIds.add(groupDO.getId());
    // }
    // RoleCfgDataScopeDO roleCfgDataScopeDO =
    // roleCfgDataScopeDAO.getByRoleId(userDO.getRoleId());
    // if (roleCfgDataScopeDO == null) {
    // return usernames;
    // }
    // String dataScope = null;
    // if (DataCategoryConsts.SERV_GROUP.equals(type)) {
    // dataScope = roleCfgDataScopeDO.getServGroup();
    // } else if (DataCategoryConsts.ORDER_GROUP.equals(type)) {
    // dataScope = roleCfgDataScopeDO.getOrderGroup();
    // } else if (DataCategoryConsts.ALARM.equals(type)) {
    // dataScope = roleCfgDataScopeDO.getAlarm();
    // } else if (DataCategoryConsts.OPERATE_LOG.equals(type)) {
    // dataScope = roleCfgDataScopeDO.getOperateLog();
    // } else if (DataCategoryConsts.USER.equals(type)) {
    // dataScope = roleCfgDataScopeDO.getUser();
    // }
    //
    // if (DictConsts.DATA_SCOPE_ONESELF.equals(dataScope)) {
    // usernames.add(username);
    // } else if (DictConsts.DATA_SCOPE_GROUP.equals(dataScope)) {
    // if (groupIds.size() == 0) {
    // usernames.add(username);
    // } else {
    // for (UserDO u : userDOs) {
    // List<GroupDO> gs = u.getGroups();
    // for (GroupDO g : gs) {
    // if (groupIds.contains(g.getId())) {
    // usernames.add(u.getUsername());
    // break;
    // }
    // }
    // }
    // }
    // } else if (DictConsts.DATA_SCOPE_ALL.equals(dataScope)) {
    // for (UserDO u : userDOs) {
    // usernames.add(u.getUsername());
    // }
    // }
    // return usernames;
    // }

    public String getScaleName(Double cpuCnt, Double memSize) {
        if (cpuCnt == null || memSize == null) {
            return "";
        } else {
            StringBuilder sBuilder = new StringBuilder();
            if (NumberUnits.isInt(cpuCnt)) {
                sBuilder.append(cpuCnt.intValue());
            } else {
                sBuilder.append(cpuCnt);
            }
            sBuilder.append("核");
            if (NumberUnits.isInt(memSize)) {
                sBuilder.append(memSize.intValue());
            } else {
                sBuilder.append(memSize);
            }
            sBuilder.append("G");
            return sBuilder.toString();
        }
    }

    public UserDO getActiveUser(HttpSession session) {
        return (UserDO) session.getAttribute("user");
    }

    public void saveOperateLog(String objType, String objName, String description, String operator) {
        saveOperateLog(objType, objName, description, operator, null);
    }

    public void saveOperateLog(String objType, String objName, String description, String operator, String siteId) {
        OperateLogDO operateLogDO = new OperateLogDO();
        operateLogDO.setObjType(objType);
        operateLogDO.setObjName(objName);
        operateLogDO.setDescription(description);
        operateLogDO.setSiteId(siteId);
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        operateLogDO.setGmtCreate(nowDate);
        operateLogDO.setCreator(operator);
        operateLogDAO.save(operateLogDO);
    }

    public class ObjModel {
        private String name;
        private String type;
        private String siteId;

        public ObjModel(String name, String siteId) {
            super();
            this.name = name;
            this.siteId = siteId;
        }

        public ObjModel(String name, String type, String siteId) {
            super();
            this.name = name;
            this.type = type;
            this.siteId = siteId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        @Override
        public String toString() {
            return "ObjModel [name=" + name + ", type=" + type + ", siteId=" + siteId + "]";
        }

    }

}
