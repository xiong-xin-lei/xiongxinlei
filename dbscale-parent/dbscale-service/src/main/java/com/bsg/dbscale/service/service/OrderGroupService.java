package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmImage;
import com.bsg.dbscale.cm.model.CmNetwork;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.query.CmImageQuery;
import com.bsg.dbscale.cm.query.CmServiceArchQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.OrderCfgDO;
import com.bsg.dbscale.dao.domain.OrderDO;
import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dao.query.ServGroupQuery;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.OrderGroupCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.ArchBaseDTO;
import com.bsg.dbscale.service.dto.BusinessSubsystemBaseDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.OrderDTO;
import com.bsg.dbscale.service.dto.OrderGroupDTO;
import com.bsg.dbscale.service.dto.ScaleBaseDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.service.form.BackupStrategyForm;
import com.bsg.dbscale.service.form.ExamineForm;
import com.bsg.dbscale.service.form.OrderForm;
import com.bsg.dbscale.service.form.OrderGroupForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;
import com.bsg.dbscale.service.form.VersionForm;
import com.bsg.dbscale.service.query.OrderGroupQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.util.DateUtils;

@Service
public class OrderGroupService extends BaseService {

    @Autowired
    private OrderGroupCheck orderGroupCheck;

    @Autowired
    private MysqlServGroupService mysqlServGroupService;

    @Autowired
    private CmhaServGroupService cmhaServGroupService;

    @Autowired
    private RedisServGroupService redisServGroupService;

    public Result list(OrderGroupQuery orderGroupQuery, String activeUsername) throws Exception {
        List<OrderGroupDTO> orderGroupDTOs = new ArrayList<>();
        List<OrderGroupDO> orderGroupDOs = listQualifiedData(orderGroupQuery, activeUsername);
        if (orderGroupDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<UserDO> userDOs = userDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(null);
            List<CmNetwork> cmNetworks = CmApi.listNetwork(null);
            List<DefServDO> defServDOs = defServDAO.list(null);

            ServGroupQuery servGroupQuery = new ServGroupQuery();
            servGroupQuery.setCategory(orderGroupQuery.getCategory());
            servGroupQuery.setSiteId(orderGroupQuery.getSiteId());
            List<ServGroupDO> servGroupDOs = servGroupDAO.list(servGroupQuery);
            for (OrderGroupDO orderGroupDO : orderGroupDOs) {
                String cmSiteId = orderGroupDO.getBusinessArea().getSiteId();
                CmSite cmSite = CmApi.findSite(cmSites, cmSiteId);
                OrderGroupDTO orderGroupDTO = getShowDTO(orderGroupDO, defServDOs, cmSite, dictTypeDOs, cmServiceArchs,
                        cmNetworks, userDOs);
                ServGroupDO servGroupDO = findServGroupDOByName(servGroupDOs, orderGroupDO.getName());
                if (servGroupDO != null) {
                    orderGroupDTO.setServGroupId(servGroupDO.getId());
                    String actionType = "";
                    switch (orderGroupDO.getCreateType()) {
                    case DictConsts.ORDER_CREATE_TYPE_NEW:
                        actionType = DictConsts.ACTION_TYPE_CREATE;
                        break;
                    case DictConsts.ORDER_CREATE_TYPE_SCALE_UP_CPUMEM:
                        actionType = DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM;
                        break;
                    case DictConsts.ORDER_CREATE_TYPE_SCALE_UP_STORAGE:
                        actionType = DictConsts.ACTION_TYPE_SCALE_UP_STORAGE;
                        break;
                    case DictConsts.ORDER_CREATE_TYPE_IMAGE_UPDATE:
                        actionType = DictConsts.ACTION_TYPE_IMAGE_UPDATE;
                        break;
                    case DictConsts.ORDER_CREATE_TYPE_ARCH_UP:
                        actionType = DictConsts.ACTION_TYPE_ARCH_UP;
                        break;
                    case DictConsts.ORDER_CREATE_TYPE_DELETE:
                        actionType = DictConsts.ACTION_TYPE_REMOVE;
                        break;

                    default:
                        break;
                    }

                    if (StringUtils.equals(orderGroupDO.getState(), DictConsts.ORDER_STATE_EXECUTING)
                            || StringUtils.equals(orderGroupDO.getState(), DictConsts.ORDER_STATE_SUCCESS)
                            || StringUtils.equals(orderGroupDO.getState(), DictConsts.ORDER_STATE_FAILED)) {
                        TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_SERV_GROUP, servGroupDO.getId(),
                                actionType);
                        if (taskDO != null) {
                            orderGroupDTO.setTaskId(taskDO.getId());
                        }
                    }
                }
                orderGroupDTOs.add(orderGroupDTO);
            }

        }
        return Result.success(orderGroupDTOs);
    }

    private List<OrderGroupDO> listQualifiedData(OrderGroupQuery orderGroupQuery, String activeUsername) {
        List<OrderGroupDO> results = new ArrayList<>();
        com.bsg.dbscale.dao.query.OrderGroupQuery daoQuery = convertToDAOQuery(orderGroupQuery);
        List<OrderGroupDO> orderGroupDOs = orderGroupDAO.list(daoQuery);
        Set<String> usernames = listVisiableUserData(activeUsername);
        for (OrderGroupDO orderGroupDO : orderGroupDOs) {
            if (usernames.contains(orderGroupDO.getOwner())) {
                results.add(orderGroupDO);
            }
        }
        return results;
    }

    public Result get(String orderGroupId) throws Exception {
        OrderGroupDTO orderGroupDTO = null;
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        if (orderGroupDO != null) {
            String cmSiteId = orderGroupDO.getBusinessArea().getSiteId();
            CmSite cmSite = CmApi.getSite(cmSiteId);

            List<UserDO> userDOs = userDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(null);
            List<CmNetwork> cmNetworks = CmApi.listNetwork(null);
            List<DefServDO> defServDOs = defServDAO.list(null);
            orderGroupDTO = getShowDTO(orderGroupDO, defServDOs, cmSite, dictTypeDOs, cmServiceArchs, cmNetworks,
                    userDOs);

            ServGroupDO servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());
            if (servGroupDO != null) {
                orderGroupDTO.setServGroupId(servGroupDO.getId());
                String actionType = "";
                switch (orderGroupDO.getCreateType()) {
                case DictConsts.ORDER_CREATE_TYPE_NEW:
                    actionType = DictConsts.ACTION_TYPE_CREATE;
                    break;
                case DictConsts.ORDER_CREATE_TYPE_SCALE_UP_CPUMEM:
                    actionType = DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM;
                    break;
                case DictConsts.ORDER_CREATE_TYPE_SCALE_UP_STORAGE:
                    actionType = DictConsts.ACTION_TYPE_SCALE_UP_STORAGE;
                    break;
                case DictConsts.ORDER_CREATE_TYPE_IMAGE_UPDATE:
                    actionType = DictConsts.ACTION_TYPE_IMAGE_UPDATE;
                    break;
                case DictConsts.ORDER_CREATE_TYPE_ARCH_UP:
                    actionType = DictConsts.ACTION_TYPE_ARCH_UP;
                    break;
                case DictConsts.ORDER_CREATE_TYPE_DELETE:
                    actionType = DictConsts.ACTION_TYPE_REMOVE;
                    break;

                default:
                    break;
                }
                if (StringUtils.equals(orderGroupDO.getState(), DictConsts.ORDER_STATE_EXECUTING)
                        || StringUtils.equals(orderGroupDO.getState(), DictConsts.ORDER_STATE_SUCCESS)
                        || StringUtils.equals(orderGroupDO.getState(), DictConsts.ORDER_STATE_FAILED)) {
                    TaskDO taskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_SERV_GROUP, servGroupDO.getId(), actionType);
                    if (taskDO != null) {
                        orderGroupDTO.setTaskId(taskDO.getId());
                    }
                }
            }
        }

        return Result.success(orderGroupDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(OrderGroupForm orderGroupForm, String activeUsername) throws Exception {
        CheckResult checkResult = orderGroupCheck.checkSave(orderGroupForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        OrderGroupDO orderGroupDO = buildOrderGroupDOForSave(orderGroupForm, activeUsername, nowDate);
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            orderDAO.save(orderDO);
        }
        orderGroupDAO.save(orderGroupDO);

        boolean autoExamine = isAutoExamine(activeUsername);
        if (autoExamine) {
            ExamineForm examineForm = new ExamineForm();
            examineForm.setState(DictConsts.ORDER_STATE_APPROVED);
            examine(orderGroupDO, examineForm, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String orderGroupId, OrderGroupForm orderGroupForm, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        OrderGroupDO orderGroupDO = buildOrderGroupDOForUpdate(orderGroupId, orderGroupForm, activeUsername, nowDate);
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            orderDAO.update(orderDO);
        }
        orderGroupDAO.update(orderGroupDO);

        boolean autoExamine = isAutoExamine(activeUsername);
        if (autoExamine) {
            ExamineForm examineForm = new ExamineForm();
            examineForm.setState(DictConsts.ORDER_STATE_APPROVED);
            examine(orderGroupDO, examineForm, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result examine(String orderGroupId, ExamineForm examineForm, String activeUsername) throws Exception {
        CheckResult checkResult = orderGroupCheck.checkExamine(orderGroupId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        examine(orderGroupDO, examineForm, activeUsername);
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void examine(OrderGroupDO orderGroupDO, ExamineForm examineForm, String activeUsername) throws Exception {
        orderGroupDO.setState(examineForm.getState());
        orderGroupDO.setMsg(StringUtils.trimToEmpty(examineForm.getMsg()));
        orderGroupDAO.updateStateAndMsg(orderGroupDO);

        boolean autoExecute = isAutoExecute(activeUsername);
        if (DictConsts.ORDER_STATE_APPROVED.equals(examineForm.getState()) && autoExecute) {
            execute(orderGroupDO, activeUsername);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result execute(String orderGroupId, String activeUsername) throws Exception {
        CheckResult checkResult = orderGroupCheck.checkExecute(orderGroupId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        execute(orderGroupDO, activeUsername);
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void execute(OrderGroupDO orderGroupDO, String activeUsername) throws Exception {
        switch (orderGroupDO.getCreateType()) {
        case DictConsts.ORDER_CREATE_TYPE_NEW:
            switch (orderGroupDO.getCategory()) {
            case Consts.SERV_GROUP_TYPE_MYSQL:
                mysqlServGroupService.create(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_CMHA:
                cmhaServGroupService.create(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_REDIS:
                redisServGroupService.create(orderGroupDO, activeUsername);
                break;
            default:
                break;
            }
            break;
        case DictConsts.ORDER_CREATE_TYPE_IMAGE_UPDATE:
            switch (orderGroupDO.getCategory()) {
            case Consts.SERV_GROUP_TYPE_MYSQL:
                mysqlServGroupService.imageUpdate(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_CMHA:
                cmhaServGroupService.imageUpdate(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_REDIS:
                redisServGroupService.imageUpdate(orderGroupDO, activeUsername);
                break;
            default:
                break;
            }
            break;
        case DictConsts.ORDER_CREATE_TYPE_SCALE_UP_CPUMEM:
            switch (orderGroupDO.getCategory()) {
            case Consts.SERV_GROUP_TYPE_MYSQL:
                mysqlServGroupService.scaleUpCpuMem(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_CMHA:
                cmhaServGroupService.scaleUpCpuMem(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_REDIS:
                redisServGroupService.scaleUpCpuMem(orderGroupDO, activeUsername);
                break;
            default:
                break;
            }
            break;
        case DictConsts.ORDER_CREATE_TYPE_SCALE_UP_STORAGE:
            switch (orderGroupDO.getCategory()) {
            case Consts.SERV_GROUP_TYPE_MYSQL:
                mysqlServGroupService.scaleUpStorage(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_CMHA:
                cmhaServGroupService.scaleUpStorage(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_REDIS:
                redisServGroupService.scaleUpStorage(orderGroupDO, activeUsername);
                break;
            default:
                break;
            }
            break;
        case DictConsts.ORDER_CREATE_TYPE_ARCH_UP:
            switch (orderGroupDO.getCategory()) {
            case Consts.SERV_GROUP_TYPE_MYSQL:
                mysqlServGroupService.archUp(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_CMHA:
                cmhaServGroupService.archUp(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_REDIS:
                redisServGroupService.archUp(orderGroupDO, activeUsername);
                break;
            default:
                break;
            }
            break;
        case DictConsts.ORDER_CREATE_TYPE_DELETE:
            switch (orderGroupDO.getCategory()) {
            case Consts.SERV_GROUP_TYPE_MYSQL:
                mysqlServGroupService.remove(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_CMHA:
                cmhaServGroupService.remove(orderGroupDO, activeUsername);
                break;
            case Consts.SERV_GROUP_TYPE_REDIS:
                redisServGroupService.remove(orderGroupDO, activeUsername);
                break;
            default:
                break;
            }
            break;
        default:
            break;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String orderGroupId) {
        CheckResult checkResult = orderGroupCheck.checkRemove(orderGroupId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        orderGroupDAO.remove(orderGroupId);
        orderDAO.removeByOrderGroupId(orderGroupId);
        return Result.success();
    }

    private com.bsg.dbscale.dao.query.OrderGroupQuery convertToDAOQuery(OrderGroupQuery orderGroupQuery) {
        com.bsg.dbscale.dao.query.OrderGroupQuery daoQuery = new com.bsg.dbscale.dao.query.OrderGroupQuery();
        daoQuery.setCategory(orderGroupQuery.getCategory());
        daoQuery.setCreateType(orderGroupQuery.getCreateType());
        daoQuery.setSiteId(orderGroupQuery.getSiteId());
        daoQuery.setState(orderGroupQuery.getState());
        daoQuery.setOwner(orderGroupQuery.getOwner());
        return daoQuery;
    }

    private OrderGroupDTO getShowDTO(OrderGroupDO orderGroupDO, List<DefServDO> defServDOs, CmSite cmSite,
            List<DictTypeDO> dictTypeDOs, List<CmServiceArch> cmServiceArchs, List<CmNetwork> cmNetworks,
            List<UserDO> userDOs) {
        OrderGroupDTO orderGroupDTO = new OrderGroupDTO();
        orderGroupDTO.setId(orderGroupDO.getId());
        orderGroupDTO.setCategory(orderGroupDO.getCategory());
        orderGroupDTO.setName(orderGroupDO.getName());

        DisplayDTO archDisplayDTO = new DisplayDTO();
        orderGroupDTO.setSysArchitecture(archDisplayDTO);

        archDisplayDTO.setCode(orderGroupDO.getSysArchitecture());
        DictDO archDictDO = findDictDO(dictTypeDOs, DictTypeConsts.SYS_ARCHITECTURE, orderGroupDO.getSysArchitecture());
        if (archDictDO != null) {
            archDisplayDTO.setDisplay(archDictDO.getName());
        }

        BusinessSubsystemDO businessSubsystemDO = orderGroupDO.getBusinessSubsystem();
        if (businessSubsystemDO != null) {
            BusinessSubsystemBaseDTO businessSubsystemDTO = new BusinessSubsystemBaseDTO();
            orderGroupDTO.setBusinessSubsystem(businessSubsystemDTO);

            businessSubsystemDTO.setId(businessSubsystemDO.getId());
            businessSubsystemDTO.setName(businessSubsystemDO.getName());

            BusinessSystemDO businessSystemDO = businessSubsystemDO.getBusinessSystem();
            if (businessSystemDO != null) {
                IdentificationDTO businessSystemDTO = new IdentificationDTO();
                businessSubsystemDTO.setBusinessSystem(businessSystemDTO);

                businessSystemDTO.setId(businessSystemDO.getId());
                businessSystemDTO.setName(businessSystemDO.getName());
            }
        }

        BusinessAreaDO businessAreaDO = orderGroupDO.getBusinessArea();
        if (businessAreaDO != null) {
            IdentificationDTO businessAreaDTO = new IdentificationDTO();
            orderGroupDTO.setBusinessArea(businessAreaDTO);

            businessAreaDTO.setId(businessAreaDO.getId());
            businessAreaDTO.setName(businessAreaDO.getName());

            if (cmSite != null) {
                IdentificationDTO siteDTO = new IdentificationDTO();
                orderGroupDTO.setSite(siteDTO);

                siteDTO.setId(cmSite.getId());
                siteDTO.setName(cmSite.getName());
            }
        }

        DisplayDTO createTypeDisplayDTO = new DisplayDTO();
        orderGroupDTO.setCreateType(createTypeDisplayDTO);

        createTypeDisplayDTO.setCode(orderGroupDO.getCreateType());
        DictDO createTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ORDER_CREATE_TYPE,
                orderGroupDO.getCreateType());
        if (createTypeDictDO != null) {
            createTypeDisplayDTO.setDisplay(createTypeDictDO.getName());
        }

        DisplayDTO stateDisplayDTO = new DisplayDTO();
        orderGroupDTO.setState(stateDisplayDTO);

        stateDisplayDTO.setCode(orderGroupDO.getState());
        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ORDER_STATE, orderGroupDO.getState());
        if (stateDictDO != null) {
            stateDisplayDTO.setDisplay(stateDictDO.getName());
        }

        orderGroupDTO.setMsg(orderGroupDO.getMsg());

        OrderGroupDTO.UserDTO owner = orderGroupDTO.new UserDTO();
        orderGroupDTO.setOwner(owner);
        owner.setUsername(orderGroupDO.getOwner());
        UserDO ownerUserDO = findUserDO(userDOs, orderGroupDO.getOwner());
        if (ownerUserDO != null) {
            owner.setName(ownerUserDO.getName());
            owner.setTelephone(ownerUserDO.getTelephone());
            owner.setCompany(ownerUserDO.getCompany());
        }

        InfoDTO createdDTO = new InfoDTO();
        orderGroupDTO.setCreated(createdDTO);
        createdDTO.setUsername(orderGroupDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, orderGroupDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(orderGroupDO.getGmtCreate()));

        if (StringUtils.isNotBlank(orderGroupDO.getEditor())) {
            InfoDTO modifiedDTO = new InfoDTO();
            orderGroupDTO.setModified(modifiedDTO);
            modifiedDTO.setUsername(orderGroupDO.getCreator());
            UserDO modifiedUserDO = findUserDO(userDOs, orderGroupDO.getEditor());
            if (modifiedUserDO != null) {
                modifiedDTO.setName(modifiedUserDO.getName());
            }
            modifiedDTO.setTimestamp(DateUtils.dateTimeToString(orderGroupDO.getGmtModified()));
        }

        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        if (orderDOs.size() > 1) {
            orderGroupDTO.setHighAvailable(true);
        } else {
            orderGroupDTO.setHighAvailable(false);
        }
        List<OrderDTO> orderDTOs = new ArrayList<>(orderDOs.size());
        orderGroupDTO.setOrders(orderDTOs);

        for (OrderDO orderDO : orderDOs) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTOs.add(orderDTO);

            orderDTO.setId(orderDO.getId());

            DisplayDTO typeDisplayDTO = new DisplayDTO();
            orderDTO.setType(typeDisplayDTO);
            typeDisplayDTO.setCode(orderDO.getType());
            DefServDO defServDO = findDefServDO(defServDOs, orderDO.getType());
            if (defServDO != null) {
                typeDisplayDTO.setDisplay(defServDO.getName());
            }

            if (orderDO.getMajorVersion() != null || orderDO.getMinorVersion() != null
                    || orderDO.getPatchVersion() != null || orderDO.getBuildVersion() != null) {
                VersionDTO version = new VersionDTO();
                version.setMajor(orderDO.getMajorVersion());
                version.setMinor(orderDO.getMinorVersion());
                version.setPatch(orderDO.getPatchVersion());
                version.setBuild(orderDO.getBuildVersion());
                orderDTO.setVersion(version);

            }

            if (StringUtils.isNotBlank(orderDO.getArchMode())) {
                ArchBaseDTO archDTO = new ArchBaseDTO();
                if (StringUtils.isNotBlank(orderDO.getArchMode())) {
                    DisplayDTO modeDisplayDTO = new DisplayDTO();
                    archDTO.setMode(modeDisplayDTO);

                    modeDisplayDTO.setCode(orderDO.getArchMode());
                    DictDO modeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ARCH_MODE, orderDO.getArchMode());
                    if (modeDictDO != null) {
                        modeDisplayDTO.setDisplay(modeDictDO.getName());
                    } else {
                        modeDisplayDTO.setDisplay(orderDO.getArchMode());
                    }
                }

                archDTO.setUnitCnt(orderDO.getUnitCnt());
                orderDTO.setArch(archDTO);

                String versionStr = null;
                if (orderDO.getMajorVersion() != null) {
                    versionStr = orderDO.getMajorVersion() + "." + orderDO.getMinorVersion();
                }
                CmServiceArch cmServiceArch = CmApi.findServiceArch(cmServiceArchs, orderDO.getType(), versionStr,
                        orderDO.getArchMode(), orderDO.getUnitCnt());
                if (cmServiceArch != null) {
                    archDTO.setName(cmServiceArch.getDesc());
                }
            }

            if (orderDO.getCpuCnt() != null && orderDO.getMemSize() != null) {
                ScaleBaseDTO scaleDTO = new ScaleBaseDTO();
                String scaleName = getScaleName(orderDO.getCpuCnt(), orderDO.getMemSize());
                scaleDTO.setName(scaleName);
                scaleDTO.setCpuCnt(orderDO.getCpuCnt());
                scaleDTO.setMemSize(orderDO.getMemSize());
                orderDTO.setScale(scaleDTO);
            }

            if (StringUtils.isNotBlank(orderDO.getDiskType())) {
                DisplayDTO diskTypeDisplayDTO = new DisplayDTO();
                orderDTO.setDiskType(diskTypeDisplayDTO);

                diskTypeDisplayDTO.setCode(orderDO.getDiskType());
                DictDO diskTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.DISK_TYPE, orderDO.getDiskType());
                if (diskTypeDictDO != null) {
                    diskTypeDisplayDTO.setDisplay(diskTypeDictDO.getName());
                }
            }

            orderDTO.setDataSize(orderDO.getDataSize());
            orderDTO.setLogSize(orderDO.getLogSize());
            orderDTO.setPort(orderDO.getPort());

            if (StringUtils.isNoneBlank(orderDO.getCfg())) {
                JSONObject cfgJson = JSONObject.parseObject(orderDO.getCfg());
                OrderDTO.Cfg cfgDTO = orderDTO.new Cfg();
                orderDTO.setCfg(cfgDTO);

                JSONObject paramJson = cfgJson.getJSONObject("param");
                if (paramJson != null && !paramJson.isEmpty()) {
                    cfgDTO.setParam((Map<String, Object>) paramJson);
                }

                if (cfgJson.containsKey("backupStrategy")) {
                    BackupStrategyForm backupStrategy = cfgJson.getObject("backupStrategy", BackupStrategyForm.class);
                    if (backupStrategy != null) {
                        OrderDTO.Cfg.BackupStrategyDTO backupStrategyDTO = cfgDTO.new BackupStrategyDTO();
                        cfgDTO.setBackupStrategy(backupStrategyDTO);

                        DisplayDTO backupStorageTypeDisplayDTO = new DisplayDTO();
                        backupStorageTypeDisplayDTO.setCode(backupStrategy.getBackupStorageType());
                        DictDO backupStorageTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.BACKUP_STORAGE_TYPE,
                                backupStrategy.getBackupStorageType());
                        if (backupStorageTypeDictDO != null) {
                            backupStorageTypeDisplayDTO.setDisplay(backupStorageTypeDictDO.getName());
                        }
                        backupStrategyDTO.setBackupStorageType(backupStorageTypeDisplayDTO);

                        DisplayDTO backupStrategyTypeDisplayDTO = new DisplayDTO();
                        backupStrategyTypeDisplayDTO.setCode(backupStrategy.getType());
                        DictDO typeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.BACKUP_TYPE,
                                backupStrategy.getType());
                        if (typeDictDO != null) {
                            backupStrategyTypeDisplayDTO.setDisplay(typeDictDO.getName());
                        }
                        backupStrategyDTO.setType(backupStrategyTypeDisplayDTO);

                        backupStrategyDTO.setCronExpression(backupStrategy.getCronExpression());
                        backupStrategyDTO.setFileRetentionNum(backupStrategy.getFileRetentionNum());
                        backupStrategyDTO.setDescription(backupStrategy.getDescription());
                    }
                }

                if (cfgJson.containsKey("networkIds")) {
                    List<String> networkIds = JSONObject.parseArray(cfgJson.getJSONArray("networkIds").toJSONString(),
                            String.class);
                    List<IdentificationDTO> networkDTOs = new ArrayList<>();
                    cfgDTO.setNetworks(networkDTOs);
                    for (String networkId : networkIds) {
                        CmNetwork cmNetwork = CmApi.findNetwork(cmNetworks, networkId);
                        if (cmNetwork != null) {
                            IdentificationDTO networkDTO = new IdentificationDTO();
                            networkDTOs.add(networkDTO);
                            networkDTO.setId(cmNetwork.getId());
                            networkDTO.setName(cmNetwork.getName());
                        }
                    }
                }
            }

            orderDTO.setCnt(orderDO.getCnt());
            orderDTO.setClusterHA(orderDO.getClusterHA());
            orderDTO.setHostHA(orderDO.getHostHA());
        }
        return orderGroupDTO;
    }

    private OrderGroupDO buildOrderGroupDOForSave(OrderGroupForm orderGroupForm, String activeUsername, Date nowDate)
            throws Exception {
        OrderGroupDO orderGroupDO = new OrderGroupDO();
        orderGroupDO.setId(PrimaryKeyUtil.get());
        orderGroupDO.setCategory(orderGroupForm.getCategory());
        orderGroupDO.setBusinessSubsystemId(orderGroupForm.getBusinessSubsystemId());
        orderGroupDO.setBusinessAreaId(orderGroupForm.getBusinessAreaId());
        orderGroupDO.setSysArchitecture(orderGroupForm.getSysArchitecture());
        orderGroupDO.setName(orderGroupForm.getName());
        orderGroupDO.setCreateType(DictConsts.ORDER_CREATE_TYPE_NEW);
        orderGroupDO.setState(DictConsts.ORDER_STATE_UNAPPROVED);
        orderGroupDO.setMsg("");
        orderGroupDO.setOwner(activeUsername);
        orderGroupDO.setCreator(activeUsername);
        orderGroupDO.setGmtCreate(nowDate);

        List<OrderForm> orderForms = orderGroupForm.getOrders();
        List<OrderCfgDO> orderCfgDOs = orderCfgDAO.list(orderGroupForm.getCategory());
        List<OrderDO> orders = new ArrayList<>(orderCfgDOs.size());
        orderGroupDO.setOrders(orders);

        BusinessAreaDO businessAreaDO = businessAreaDAO.get(orderGroupForm.getBusinessAreaId());

        for (OrderCfgDO orderCfgDO : orderCfgDOs) {
            OrderForm orderForm = null;
            for (OrderForm form : orderForms) {
                if (orderCfgDO.getType().equals(form.getType())) {
                    orderForm = form;
                    break;
                }
            }

            if (orderForm == null) {
                if (BooleanUtils.isNotTrue(orderGroupForm.getHighAvailable())) {
                    continue;
                }
                orderForm = new OrderForm();
            }

            OrderDO orderDO = new OrderDO();
            orders.add(orderDO);

            orderDO.setId(PrimaryKeyUtil.get());
            orderDO.setOrderGroupId(orderGroupDO.getId());
            orderDO.setType(orderCfgDO.getType());

            CmImageQuery imageQuery = new CmImageQuery();
            imageQuery.setSiteId(businessAreaDO.getSiteId());
            imageQuery.setType(orderCfgDO.getType());
            imageQuery.setUnschedulable(false);
            List<CmImage> cmImages = CmApi.listImage(imageQuery);

            VersionForm versionForm = orderForm.getVersion();
            Integer major = null;
            Integer minor = null;
            Integer patch = null;
            Integer build = null;
            if (versionForm != null) {
                major = versionForm.getMajor();
                minor = versionForm.getMinor();
                patch = versionForm.getPatch();
                build = versionForm.getBuild();
            }
            CmImage cmImage = CmApi.findLatestImage(cmImages, orderCfgDO.getType(), major, minor, patch, build);
            orderDO.setMajorVersion(cmImage.getMajor());
            orderDO.setMinorVersion(cmImage.getMinor());
            orderDO.setPatchVersion(cmImage.getPatch());
            orderDO.setBuildVersion(cmImage.getBuild());

            if (StringUtils.isNotBlank(orderForm.getArchMode()) && orderForm.getUnitCnt() != null) {
                orderDO.setArchMode(orderForm.getArchMode());
                orderDO.setUnitCnt(orderForm.getUnitCnt());
            } else {
                CmServiceArchQuery cmServiceArchQuery = new CmServiceArchQuery();
                cmServiceArchQuery.setType(orderCfgDO.getType());
                String version = cmImage.getMajor() + "." + cmImage.getMinor();
                cmServiceArchQuery.setVersion(version);
                List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(cmServiceArchQuery);
                if (cmServiceArchs != null && cmServiceArchs.size() > 0) {
                    CmServiceArch cmServiceArch_0 = cmServiceArchs.get(0);
                    orderDO.setArchMode(cmServiceArch_0.getMode());
                    orderDO.setUnitCnt(cmServiceArch_0.getReplicas());
                }
            }

            if (orderForm.getCpuCnt() != null && orderForm.getMemSize() != null) {
                orderDO.setCpuCnt(orderForm.getCpuCnt());
                orderDO.setMemSize(orderForm.getMemSize());
            } else {
                orderDO.setCpuCnt(orderCfgDO.getCpuCnt());
                orderDO.setMemSize(orderCfgDO.getMemSize());
            }

            CmSite cmSite = CmApi.getSite(businessAreaDO.getSiteId());
            CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
            if (kubernetes != null) {
                if (CmConsts.STORAGE_TYPE_VOLUMEPATH.equals(kubernetes.getStorageMode())) {
                    if (StringUtils.isNotBlank(orderForm.getDiskType())) {
                        orderDO.setDiskType(orderForm.getDiskType());
                    } else {
                        orderDO.setDiskType(orderCfgDO.getDiskType());
                    }
                } else if (CmConsts.STORAGE_TYPE_PVC.equals(kubernetes.getStorageMode())) {
                    orderDO.setDiskType(null);
                }
            }

            if (orderForm.getDataSize() != null) {
                orderDO.setDataSize(orderForm.getDataSize());
            } else {
                orderDO.setDataSize(orderCfgDO.getDataSize());
            }

            if (orderForm.getLogSize() != null) {
                orderDO.setLogSize(orderForm.getLogSize());
            } else {
                orderDO.setLogSize(orderCfgDO.getLogSize());
            }

            if (orderForm.getPort() != null) {
                orderDO.setPort(orderForm.getPort());
            } else {
                orderDO.setPort(orderCfgDO.getPort());
            }

            if (orderForm.getCfg() != null) {
                String cfgJsonStr = JSONObject.toJSONString(orderForm.getCfg());
                orderDO.setCfg(cfgJsonStr);
            }

            if (orderForm.getCnt() != null) {
                orderDO.setCnt(orderForm.getCnt());
            } else {
                orderDO.setCnt(1);
            }

            orderDO.setClusterHA(orderCfgDO.getClusterHA());
            orderDO.setHostHA(orderCfgDO.getHostHA());

        }
        return orderGroupDO;
    }

    public boolean isAutoExamine(String username) {
        UserDO userDO = userDAO.get(username);
        return BooleanUtils.isTrue(userDO.getOgAutoExamine());
    }

    public boolean isAutoExecute(String username) {
        UserDO userDO = userDAO.get(username);
        return BooleanUtils.isTrue(userDO.getOgAutoExecute());
    }

    private OrderGroupDO buildOrderGroupDOForUpdate(String orderGroupId, OrderGroupForm orderGroupForm,
            String activeUsername, Date nowDate) throws Exception {
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        if (orderGroupForm.getBusinessSubsystemId() != null) {
            orderGroupDO.setBusinessSubsystemId(orderGroupForm.getBusinessSubsystemId());
        }
        if (orderGroupForm.getBusinessAreaId() != null) {
            orderGroupDO.setBusinessAreaId(orderGroupForm.getBusinessAreaId());
        }
        if (orderGroupForm.getSysArchitecture() != null) {
            orderGroupDO.setSysArchitecture(orderGroupForm.getSysArchitecture());
        }
        if (orderGroupForm.getName() != null) {
            orderGroupDO.setName(orderGroupForm.getName());
        }
        orderGroupDO.setState(DictConsts.ORDER_STATE_UNAPPROVED);
        orderGroupDO.setMsg("");
        orderGroupDO.setEditor(activeUsername);
        orderGroupDO.setGmtModified(nowDate);

        CmSite cmSite = CmApi.getSite(orderGroupDO.getBusinessArea().getSiteId());

        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        List<OrderForm> orderForms = orderGroupForm.getOrders();
        if (orderForms != null) {
            for (OrderForm orderForm : orderForms) {
                for (OrderDO orderDO : orderDOs) {
                    if (orderForm.getType().equals(orderDO.getType())) {
                        VersionForm versionForm = orderForm.getVersion();
                        if (versionForm != null && versionForm.getMajor() != null && versionForm.getMinor() != null
                                && versionForm.getPatch() != null && versionForm.getBuild() != null) {
                            orderDO.setMajorVersion(versionForm.getMajor());
                            orderDO.setMinorVersion(versionForm.getMinor());
                            orderDO.setPatchVersion(versionForm.getPatch());
                            orderDO.setBuildVersion(versionForm.getBuild());
                        }
                        if (StringUtils.isNotBlank(orderForm.getArchMode())) {
                            orderDO.setArchMode(orderForm.getArchMode());
                        }
                        if (orderForm.getUnitCnt() != null) {
                            orderDO.setUnitCnt(orderForm.getUnitCnt());
                        }
                        if (orderForm.getCpuCnt() != null) {
                            orderDO.setCpuCnt(orderForm.getCpuCnt());
                        }
                        if (orderForm.getMemSize() != null) {
                            orderDO.setMemSize(orderForm.getMemSize());
                        }

                        CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
                        if (kubernetes != null) {
                            if (CmConsts.STORAGE_TYPE_VOLUMEPATH.equals(kubernetes.getStorageMode())) {
                                if (StringUtils.isNotBlank(orderForm.getDiskType())) {
                                    orderDO.setDiskType(orderForm.getDiskType());
                                }
                            } else if (CmConsts.STORAGE_TYPE_PVC.equals(kubernetes.getStorageMode())) {
                                orderDO.setDiskType(null);
                            }
                        }

                        if (orderForm.getDataSize() != null) {
                            orderDO.setDataSize(orderForm.getDataSize());
                        }
                        if (orderForm.getLogSize() != null) {
                            orderDO.setLogSize(orderForm.getLogSize());
                        }
                        if (orderForm.getPort() != null) {
                            orderDO.setPort(orderForm.getPort());
                        }
                        OrderForm.Cfg cfgForm = orderForm.getCfg();
                        if (cfgForm != null) {
                            JSONObject cfgJson = new JSONObject();
                            if (StringUtils.isNotBlank(orderDO.getCfg())) {
                                cfgJson = JSONObject.parseObject(orderDO.getCfg());
                            }

                            Map<String, Object> param = cfgForm.getParam();
                            if (param != null && !param.isEmpty()) {
                                cfgJson.put("param", param);
                            } else {
                                cfgJson.remove("param");
                            }

                            BackupStrategyForm backupStrategyForm = cfgForm.getBackupStrategy();
                            if (backupStrategyForm != null) {
                                cfgJson.put("backupStrategy", backupStrategyForm);
                            } else {
                                cfgJson.remove("backupStrategy");
                            }

                            List<String> networkIds = cfgForm.getNetworkIds();
                            if (networkIds != null && networkIds.size() > 0) {
                                cfgJson.put("networkIds", networkIds);
                            } else {
                                cfgJson.remove("networkIds");
                            }

                            orderDO.setCfg(cfgJson.toJSONString());
                        }
                    }
                }
            }
        }
        return orderGroupDO;
    }

    public OrderGroupDO buildImageUpdateOrderGroupDO(ServGroupDO servGroupDO, ServImageForm imageForm,
            String activeUsername, Date nowDate) throws Exception {
        OrderGroupDO orderGroupDO = new OrderGroupDO();
        orderGroupDO.setId(PrimaryKeyUtil.get());
        orderGroupDO.setCategory(servGroupDO.getCategory());
        orderGroupDO.setBusinessSubsystemId(servGroupDO.getBusinessSubsystemId());
        orderGroupDO.setBusinessAreaId(servGroupDO.getBusinessAreaId());
        orderGroupDO.setSysArchitecture(servGroupDO.getSysArchitecture());
        orderGroupDO.setName(servGroupDO.getName());
        orderGroupDO.setCreateType(DictConsts.ORDER_CREATE_TYPE_IMAGE_UPDATE);
        orderGroupDO.setState(DictConsts.ORDER_STATE_UNAPPROVED);
        orderGroupDO.setMsg("");
        orderGroupDO.setOwner(servGroupDO.getOwner());
        orderGroupDO.setCreator(activeUsername);
        orderGroupDO.setGmtCreate(nowDate);

        List<ServDO> servDOs = servGroupDO.getServs();
        Set<String> types = new HashSet<>();
        for (ServDO servDO : servDOs) {
            types.add(servDO.getType());
        }

        List<OrderDO> orders = new ArrayList<>();
        orderGroupDO.setOrders(orders);
        for (String type : types) {
            OrderDO orderDO = new OrderDO();
            orders.add(orderDO);

            orderDO.setId(PrimaryKeyUtil.get());
            orderDO.setOrderGroupId(orderGroupDO.getId());
            orderDO.setType(type);
            if (type.equals(imageForm.getType())) {
                VersionForm versionForm = imageForm.getVersion();
                orderDO.setMajorVersion(versionForm.getMajor());
                orderDO.setMinorVersion(versionForm.getMinor());
                orderDO.setPatchVersion(versionForm.getPatch());
                orderDO.setBuildVersion(versionForm.getBuild());
            }
        }

        return orderGroupDO;
    }

    public OrderGroupDO buildScaleUpCpuMemOrderGroupDO(ServGroupDO servGroupDO, ServScaleCpuMemForm scaleCpuMemForm,
            String activeUsername, Date nowDate) throws Exception {
        OrderGroupDO orderGroupDO = new OrderGroupDO();
        orderGroupDO.setId(PrimaryKeyUtil.get());
        orderGroupDO.setCategory(servGroupDO.getCategory());
        orderGroupDO.setBusinessSubsystemId(servGroupDO.getBusinessSubsystemId());
        orderGroupDO.setBusinessAreaId(servGroupDO.getBusinessAreaId());
        orderGroupDO.setSysArchitecture(servGroupDO.getSysArchitecture());
        orderGroupDO.setName(servGroupDO.getName());
        orderGroupDO.setCreateType(DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM);
        orderGroupDO.setState(DictConsts.ORDER_STATE_UNAPPROVED);
        orderGroupDO.setMsg("");
        orderGroupDO.setOwner(servGroupDO.getOwner());
        orderGroupDO.setCreator(activeUsername);
        orderGroupDO.setGmtCreate(nowDate);

        List<ServDO> servDOs = servGroupDO.getServs();
        Set<String> types = new HashSet<>();
        for (ServDO servDO : servDOs) {
            types.add(servDO.getType());
        }

        List<OrderDO> orders = new ArrayList<>();
        orderGroupDO.setOrders(orders);
        for (String type : types) {
            OrderDO orderDO = new OrderDO();
            orders.add(orderDO);

            orderDO.setId(PrimaryKeyUtil.get());
            orderDO.setOrderGroupId(orderGroupDO.getId());
            orderDO.setType(type);
            for (ServDO servDO : servDOs) {
                if (type.equals(servDO.getType())) {
                    orderDO.setDiskType(servDO.getDiskType());
                    break;
                }
            }
            if (type.equals(scaleCpuMemForm.getType())) {
                if (scaleCpuMemForm.getCpuCnt() != null) {
                    orderDO.setCpuCnt(scaleCpuMemForm.getCpuCnt());
                }
                if (scaleCpuMemForm.getMemSize() != null) {
                    orderDO.setMemSize(scaleCpuMemForm.getMemSize());
                }
            }
        }

        return orderGroupDO;
    }

    public OrderGroupDO buildScaleUpStorageOrderGroupDO(ServGroupDO servGroupDO, ServScaleStorageForm scaleStorageForm,
            String activeUsername, Date nowDate) throws Exception {
        OrderGroupDO orderGroupDO = new OrderGroupDO();
        orderGroupDO.setId(PrimaryKeyUtil.get());
        orderGroupDO.setCategory(servGroupDO.getCategory());
        orderGroupDO.setBusinessSubsystemId(servGroupDO.getBusinessSubsystemId());
        orderGroupDO.setBusinessAreaId(servGroupDO.getBusinessAreaId());
        orderGroupDO.setSysArchitecture(servGroupDO.getSysArchitecture());
        orderGroupDO.setName(servGroupDO.getName());
        orderGroupDO.setCreateType(DictConsts.ACTION_TYPE_SCALE_UP_STORAGE);
        orderGroupDO.setState(DictConsts.ORDER_STATE_UNAPPROVED);
        orderGroupDO.setMsg("");
        orderGroupDO.setOwner(servGroupDO.getOwner());
        orderGroupDO.setCreator(activeUsername);
        orderGroupDO.setGmtCreate(nowDate);

        List<ServDO> servDOs = servGroupDO.getServs();
        Set<String> types = new HashSet<>();
        for (ServDO servDO : servDOs) {
            types.add(servDO.getType());
        }

        List<OrderDO> orders = new ArrayList<>();
        orderGroupDO.setOrders(orders);
        for (String type : types) {
            OrderDO orderDO = new OrderDO();
            orders.add(orderDO);

            orderDO.setId(PrimaryKeyUtil.get());
            orderDO.setOrderGroupId(orderGroupDO.getId());
            orderDO.setType(type);
            for (ServDO servDO : servDOs) {
                if (type.equals(servDO.getType())) {
                    orderDO.setDiskType(servDO.getDiskType());
                    break;
                }
            }
            if (type.equals(scaleStorageForm.getType())) {
                if (scaleStorageForm.getDataSize() != null) {
                    orderDO.setDataSize(scaleStorageForm.getDataSize());
                }
                if (scaleStorageForm.getLogSize() != null) {
                    orderDO.setLogSize(scaleStorageForm.getLogSize());
                }
            }
        }

        return orderGroupDO;
    }

    public OrderGroupDO buildArchUpOrderGroupDO(ServGroupDO servGroupDO, ServArchUpForm archUpForm,
            String activeUsername, Date nowDate) throws Exception {
        OrderGroupDO orderGroupDO = new OrderGroupDO();
        orderGroupDO.setId(PrimaryKeyUtil.get());
        orderGroupDO.setCategory(servGroupDO.getCategory());
        orderGroupDO.setBusinessSubsystemId(servGroupDO.getBusinessSubsystemId());
        orderGroupDO.setBusinessAreaId(servGroupDO.getBusinessAreaId());
        orderGroupDO.setSysArchitecture(servGroupDO.getSysArchitecture());
        orderGroupDO.setName(servGroupDO.getName());
        orderGroupDO.setCreateType(DictConsts.ACTION_TYPE_ARCH_UP);
        orderGroupDO.setState(DictConsts.ORDER_STATE_UNAPPROVED);
        orderGroupDO.setMsg("");
        orderGroupDO.setOwner(servGroupDO.getOwner());
        orderGroupDO.setCreator(activeUsername);
        orderGroupDO.setGmtCreate(nowDate);

        List<ServDO> servDOs = servGroupDO.getServs();
        Set<String> types = new HashSet<>();
        for (ServDO servDO : servDOs) {
            types.add(servDO.getType());
        }

        List<OrderDO> orders = new ArrayList<>();
        orderGroupDO.setOrders(orders);
        for (String type : types) {
            OrderDO orderDO = new OrderDO();
            orders.add(orderDO);

            orderDO.setId(PrimaryKeyUtil.get());
            orderDO.setOrderGroupId(orderGroupDO.getId());
            orderDO.setType(type);
            if (type.equals(archUpForm.getType())) {
                orderDO.setUnitCnt(archUpForm.getUnitCnt());
                orderDO.setArchMode(archUpForm.getArchMode());
            }
        }

        return orderGroupDO;
    }

    public OrderGroupDO buildRemoveOrderGroupDO(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        OrderGroupDO orderGroupDO = new OrderGroupDO();
        orderGroupDO.setId(PrimaryKeyUtil.get());
        orderGroupDO.setCategory(servGroupDO.getCategory());
        orderGroupDO.setBusinessSubsystemId(servGroupDO.getBusinessSubsystemId());
        orderGroupDO.setBusinessAreaId(servGroupDO.getBusinessAreaId());
        orderGroupDO.setSysArchitecture(servGroupDO.getSysArchitecture());
        orderGroupDO.setName(servGroupDO.getName());
        orderGroupDO.setCreateType(DictConsts.ORDER_CREATE_TYPE_DELETE);
        orderGroupDO.setState(DictConsts.ORDER_STATE_UNAPPROVED);
        orderGroupDO.setMsg("");
        orderGroupDO.setOwner(servGroupDO.getOwner());
        orderGroupDO.setCreator(activeUsername);
        orderGroupDO.setGmtCreate(nowDate);

        List<ServDO> servDOs = servGroupDO.getServs();
        Set<String> types = new HashSet<>();
        for (ServDO servDO : servDOs) {
            types.add(servDO.getType());
        }

        List<OrderDO> orders = new ArrayList<>();
        orderGroupDO.setOrders(orders);
        for (String type : types) {
            OrderDO orderDO = new OrderDO();
            orders.add(orderDO);

            orderDO.setId(PrimaryKeyUtil.get());
            orderDO.setOrderGroupId(orderGroupDO.getId());
            orderDO.setType(type);
        }

        return orderGroupDO;
    }

    public ObjModel getObjModel(String orderGroupId) {
        OrderGroupDO orderGroupDO = orderGroupDAO.get(orderGroupId);
        if (orderGroupDO != null) {
            return new ObjModel(orderGroupDO.getName(), orderGroupDO.getCategory(),
                    orderGroupDO.getBusinessArea().getSiteId());
        }
        return null;
    }

}
