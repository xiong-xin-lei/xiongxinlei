package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmCfgBody;
import com.bsg.dbscale.cm.body.CmServiceBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmImage;
import com.bsg.dbscale.cm.model.CmImageBase;
import com.bsg.dbscale.cm.model.CmNetwork;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceCfg;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.cm.query.CmImageQuery;
import com.bsg.dbscale.cm.query.CmNetworkQuery;
import com.bsg.dbscale.cm.response.CmSaveServiceResp;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.OrderDO;
import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskCfgDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.BusinessSubsystemBaseDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.ServCfgDTO;
import com.bsg.dbscale.service.dto.ServGroupBaseDTO;
import com.bsg.dbscale.service.dto.ServGroupDetailDTO;
import com.bsg.dbscale.service.dto.ServGroupStateBaseDTO;
import com.bsg.dbscale.service.dto.TaskBaseDTO;
import com.bsg.dbscale.service.dto.UnitEventDTO;
import com.bsg.dbscale.service.dto.UserBaseDTO;
import com.bsg.dbscale.service.form.ExamineForm;
import com.bsg.dbscale.service.form.ParamCfgForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;
import com.bsg.dbscale.service.query.ServGroupQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.service.util.TaskResult;
import com.bsg.dbscale.util.DateUtils;
import com.bsg.dbscale.util.NumberUnits;
import com.bsg.dbscale.util.RandomUtil;

@Service
public class ServGroupService extends BaseService {

    @Autowired
    protected OrderGroupService orderGroupService;

    @Autowired
    protected UnitService unitService;

    public Result getEvent(String servGroupId) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            int unitCnt = 0;
            List<ServDO> servDOs = servGroupDO.getServs();
            for (ServDO servDO : servDOs) {
                List<UnitDO> unitDOs = servDO.getUnits();
                for (UnitDO unitDO : unitDOs) {
                    if (StringUtils.isNotBlank(unitDO.getRelateId())) {
                        unitCnt++;
                    }
                }
            }
            Map<String, UnitEventDTO> map = new HashMap<String, UnitEventDTO>(unitCnt);

            CountDownLatch countDownLatch = new CountDownLatch(unitCnt);

            for (ServDO servDO : servDOs) {
                List<UnitDO> unitDOs = servDO.getUnits();
                for (UnitDO unitDO : unitDOs) {
                    if (StringUtils.isNotBlank(unitDO.getRelateId())) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Result result = unitService.getEvent(unitDO.getId());
                                    UnitEventDTO unitEventDTO = null;
                                    if (result.getData() != null) {
                                        unitEventDTO = (UnitEventDTO) result.getData();
                                    }
                                    map.put(unitDO.getRelateId(), unitEventDTO);
                                } catch (Exception e) {
                                    logger.error("查询网段信息异常：", e);
                                } finally {
                                    countDownLatch.countDown();
                                }
                            }
                        });
                    }
                }
            }

            countDownLatch.await();

            return Result.success(map);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(OrderGroupDO orderGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        ServGroupDO servGroupDO = servGroupDAO.getByOrderGroupId(orderGroupDO.getId());
        if (servGroupDO == null) {
            servGroupDO = buildServGroupDO(orderGroupDO, nowDate);
            List<ServDO> servDOs = servGroupDO.getServs();
            for (ServDO servDO : servDOs) {
                List<UnitDO> unitDOs = servDO.getUnits();
                for (UnitDO unitDO : unitDOs) {
                    unitDAO.save(unitDO);
                }
                servDAO.save(servDO);
            }
            servGroupDAO.save(servGroupDO);
        } else {
            updateServGroupDO(servGroupDO, orderGroupDO, nowDate);
        }
        servGroupDO.setOrderGroup(orderGroupDO);

        TaskDO taskDO = buildCreateTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result start(ServGroupDO servGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildStartTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result stop(ServGroupDO servGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildStopTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result imageUpdate(ServGroupDO servGroupDO, ServImageForm imageForm, String activeUsername)
            throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        OrderGroupDO orderGroupDO = orderGroupService.buildImageUpdateOrderGroupDO(servGroupDO, imageForm,
                activeUsername, nowDate);
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            orderDAO.save(orderDO);
        }
        orderGroupDAO.save(orderGroupDO);

        boolean autoExamine = orderGroupService.isAutoExamine(activeUsername);
        if (autoExamine) {
            ExamineForm examineForm = new ExamineForm();
            examineForm.setState(DictConsts.ORDER_STATE_APPROVED);
            orderGroupService.examine(orderGroupDO, examineForm, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void imageUpdate(OrderGroupDO orderGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        ServGroupDO servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());
        servGroupDO.setOrderGroupId(orderGroupDO.getId());
        servGroupDAO.update(servGroupDO);

        List<ServDO> servDOs = servGroupDO.getServs();
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getMajorVersion() != null) {
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        servDO.setMajorVersion(orderDO.getMajorVersion());
                        servDO.setMinorVersion(orderDO.getMinorVersion());
                        servDO.setPatchVersion(orderDO.getPatchVersion());
                        servDO.setBuildVersion(orderDO.getBuildVersion());
                        servDAO.update(servDO);
                    }
                }
                break;
            }
        }

        TaskDO taskDO = buildImageUpdateTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result scaleUpCpuMem(ServGroupDO servGroupDO, ServScaleCpuMemForm scaleCpuMemForm, String activeUsername)
            throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        OrderGroupDO orderGroupDO = orderGroupService.buildScaleUpCpuMemOrderGroupDO(servGroupDO, scaleCpuMemForm,
                activeUsername, nowDate);
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            orderDAO.save(orderDO);
        }
        orderGroupDAO.save(orderGroupDO);

        boolean autoExamine = orderGroupService.isAutoExamine(activeUsername);
        if (autoExamine) {
            ExamineForm examineForm = new ExamineForm();
            examineForm.setState(DictConsts.ORDER_STATE_APPROVED);
            orderGroupService.examine(orderGroupDO, examineForm, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void scaleUpCpuMem(OrderGroupDO orderGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        ServGroupDO servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());
        servGroupDO.setOrderGroupId(orderGroupDO.getId());
        servGroupDAO.update(servGroupDO);

        List<ServDO> servDOs = servGroupDO.getServs();
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getCpuCnt() != null) {
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        servDO.setCpuCnt(orderDO.getCpuCnt());
                        servDO.setMemSize(orderDO.getMemSize());
                        servDAO.update(servDO);
                    }
                }
                break;
            }
        }

        TaskDO taskDO = buildScaleUpCpuMemTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result scaleUpStorage(ServGroupDO servGroupDO, ServScaleStorageForm scaleStorageForm, String activeUsername)
            throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        OrderGroupDO orderGroupDO = orderGroupService.buildScaleUpStorageOrderGroupDO(servGroupDO, scaleStorageForm,
                activeUsername, nowDate);
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            orderDAO.save(orderDO);
        }
        orderGroupDAO.save(orderGroupDO);

        boolean autoExamine = orderGroupService.isAutoExamine(activeUsername);
        if (autoExamine) {
            ExamineForm examineForm = new ExamineForm();
            examineForm.setState(DictConsts.ORDER_STATE_APPROVED);
            orderGroupService.examine(orderGroupDO, examineForm, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void scaleUpStorage(OrderGroupDO orderGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        ServGroupDO servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());
        servGroupDO.setOrderGroupId(orderGroupDO.getId());
        servGroupDAO.update(servGroupDO);

        List<ServDO> servDOs = servGroupDO.getServs();
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getDataSize() != null) {
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        servDO.setDataSize(orderDO.getDataSize());
                        servDO.setLogSize(orderDO.getLogSize());
                        servDAO.update(servDO);
                    }
                }
                break;
            }
        }

        TaskDO taskDO = buildScaleUpStorageTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result archUp(ServGroupDO servGroupDO, ServArchUpForm archUpForm, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        OrderGroupDO orderGroupDO = orderGroupService.buildArchUpOrderGroupDO(servGroupDO, archUpForm, activeUsername,
                nowDate);
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            orderDAO.save(orderDO);
        }
        orderGroupDAO.save(orderGroupDO);

        boolean autoExamine = orderGroupService.isAutoExamine(activeUsername);
        if (autoExamine) {
            ExamineForm examineForm = new ExamineForm();
            examineForm.setState(DictConsts.ORDER_STATE_APPROVED);
            orderGroupService.examine(orderGroupDO, examineForm, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void archUp(OrderGroupDO orderGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        ServGroupDO servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());
        servGroupDO.setOrderGroupId(orderGroupDO.getId());
        servGroupDAO.update(servGroupDO);

        List<ServDO> servDOs = servGroupDO.getServs();
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getArchMode() != null) {
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        int originalUnitCnt = servDO.getUnitCnt();

                        servDO.setArchMode(orderDO.getArchMode());
                        servDO.setUnitCnt(orderDO.getUnitCnt());
                        servDAO.update(servDO);

                        int addUnitCnt = orderDO.getUnitCnt() - originalUnitCnt;
                        if (addUnitCnt > 0) {
                            for (int i = 0; i < addUnitCnt; i++) {
                                UnitDO unitDO = new UnitDO();
                                unitDO.setId(PrimaryKeyUtil.get());
                                unitDO.setServId(servDO.getId());
                                unitDO.setType(orderDO.getType());
                                unitDAO.save(unitDO);
                            }
                        }
                    }
                }
                break;
            }
        }

        servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());

        TaskDO taskDO = buildArchUpTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(ServGroupDO servGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        if (BooleanUtils.isNotTrue(servGroupDO.getFlag())) {
            TaskDO taskDO = buildRemoveTask(servGroupDO, activeUsername, nowDate);
            List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
            for (SubtaskDO subtaskDO : subtaskDOs) {
                subtaskDAO.save(subtaskDO);
            }
            taskDAO.save(taskDO);

            executor.execute(new Task(taskDO));
            return Result.success();
        }

        OrderGroupDO orderGroupDO = orderGroupService.buildRemoveOrderGroupDO(servGroupDO, activeUsername, nowDate);
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            orderDAO.save(orderDO);
        }
        orderGroupDAO.save(orderGroupDO);

        boolean autoExamine = orderGroupService.isAutoExamine(activeUsername);
        if (autoExamine) {
            ExamineForm examineForm = new ExamineForm();
            examineForm.setState(DictConsts.ORDER_STATE_APPROVED);
            orderGroupService.examine(orderGroupDO, examineForm, activeUsername);
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(OrderGroupDO orderGroupDO, String activeUsername) throws Exception {
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        ServGroupDO servGroupDO = servGroupDAO.getByName(orderGroupDO.getName());
        servGroupDO.setOrderGroupId(orderGroupDO.getId());
        servGroupDAO.update(servGroupDO);

        TaskDO taskDO = buildRemoveTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
    }

    public Result listCfg(String servGroupId, String type) throws Exception {
        List<ServCfgDTO> servCfgDTOs = new ArrayList<>();
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            List<ServDO> servDOs = servGroupDO.getServs();
            for (ServDO servDO : servDOs) {
                if (servDO.getType().equals(type)) {
                    List<CmServiceCfg> cmServiceCfgs = CmApi.listServiceCfg(servDO.getRelateId());
                    if (cmServiceCfgs != null) {
                        for (CmServiceCfg cmServiceCfg : cmServiceCfgs) {
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
                    break;
                }
            }
        }

        return Result.success(servCfgDTOs);
    }

    public Result updateCfg(String servGroupId, String type, ParamCfgForm paramCfgForm) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            List<ServDO> servDOs = servGroupDO.getServs();
            for (ServDO servDO : servDOs) {
                if (servDO.getType().equals(type)) {
                    CmCfgBody cmCfgBody = new CmCfgBody();
                    cmCfgBody.setKey(paramCfgForm.getKey());
                    cmCfgBody.setValue(paramCfgForm.getValue());
                    CmApi.updateServiceCfg(servDO.getRelateId(), cmCfgBody);
                    break;
                }
            }
        }

        return Result.success();
    }

    public List<ServGroupDO> listQualifiedData(ServGroupQuery servGroupQuery, String activeUsername) {
        List<ServGroupDO> results = new ArrayList<>();
        com.bsg.dbscale.dao.query.ServGroupQuery daoQuery = convertToDAOQuery(servGroupQuery);
        List<ServGroupDO> servGroupDOs = servGroupDAO.list(daoQuery);
        Set<String> usernames = listVisiableUserData(activeUsername);
        for (ServGroupDO servGroupDO : servGroupDOs) {
            if (usernames.contains(servGroupDO.getOwner())) {
                results.add(servGroupDO);
            }
        }
        return results;
    }

    private com.bsg.dbscale.dao.query.ServGroupQuery convertToDAOQuery(ServGroupQuery servGroupQuery) {
        com.bsg.dbscale.dao.query.ServGroupQuery daoQuery = new com.bsg.dbscale.dao.query.ServGroupQuery();
        daoQuery.setSiteId(servGroupQuery.getSiteId());
        daoQuery.setCategory(servGroupQuery.getCategory());
        daoQuery.setFlag(servGroupQuery.getCreateSuccess());
        return daoQuery;
    }

    private ServGroupDO buildServGroupDO(OrderGroupDO orderGroupDO, Date nowDate) {
        ServGroupDO servGroupDO = new ServGroupDO();
        servGroupDO.setId(PrimaryKeyUtil.get());
        servGroupDO.setCategory(orderGroupDO.getCategory());
        servGroupDO.setBusinessSubsystemId(orderGroupDO.getBusinessSubsystemId());
        servGroupDO.setBusinessAreaId(orderGroupDO.getBusinessAreaId());
        servGroupDO.setSysArchitecture(orderGroupDO.getSysArchitecture());
        servGroupDO.setName(orderGroupDO.getName());
        servGroupDO.setOwner(orderGroupDO.getOwner());
        servGroupDO.setOrderGroupId(orderGroupDO.getId());
        servGroupDO.setGmtCreate(nowDate);

        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        List<ServDO> servDOs = new ArrayList<>(orderDOs.size());
        servGroupDO.setServs(servDOs);
        for (OrderDO orderDO : orderDOs) {
            for (int i = 0; i < orderDO.getCnt(); i++) {
                ServDO servDO = new ServDO();
                servDOs.add(servDO);
                servDO.setId(PrimaryKeyUtil.get());
                servDO.setServGroupId(servGroupDO.getId());
                servDO.setType(orderDO.getType());
                servDO.setMajorVersion(orderDO.getMajorVersion());
                servDO.setMinorVersion(orderDO.getMinorVersion());
                servDO.setPatchVersion(orderDO.getPatchVersion());
                servDO.setBuildVersion(orderDO.getBuildVersion());
                servDO.setArchMode(orderDO.getArchMode());
                servDO.setUnitCnt(orderDO.getUnitCnt());
                servDO.setCpuCnt(orderDO.getCpuCnt());
                servDO.setMemSize(orderDO.getMemSize());
                servDO.setDiskType(orderDO.getDiskType());
                servDO.setDataSize(orderDO.getDataSize());
                servDO.setLogSize(orderDO.getLogSize());
                servDO.setPort(orderDO.getPort());

                List<UnitDO> unitDOs = new ArrayList<>();
                servDO.setUnits(unitDOs);
                for (int j = 0; j < orderDO.getUnitCnt(); j++) {
                    UnitDO unitDO = new UnitDO();
                    unitDOs.add(unitDO);

                    unitDO.setId(PrimaryKeyUtil.get());
                    unitDO.setServId(servDO.getId());
                    unitDO.setType(orderDO.getType());
                }
            }
        }
        return servGroupDO;
    }

    private void updateServGroupDO(ServGroupDO servGroupDO, OrderGroupDO orderGroupDO, Date nowDate) {
        servGroupDO.setBusinessSubsystemId(orderGroupDO.getBusinessSubsystemId());
        servGroupDO.setBusinessAreaId(orderGroupDO.getBusinessAreaId());
        servGroupDO.setSysArchitecture(orderGroupDO.getSysArchitecture());
        servGroupDO.setGmtCreate(nowDate);

        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        List<ServDO> servDOs = servGroupDO.getServs();
        for (OrderDO orderDO : orderDOs) {
            for (ServDO servDO : servDOs) {
                if (servDO.getType().equals(orderDO.getType())) {
                    servDO.setMajorVersion(orderDO.getMajorVersion());
                    servDO.setMinorVersion(orderDO.getMinorVersion());
                    servDO.setPatchVersion(orderDO.getPatchVersion());
                    servDO.setBuildVersion(orderDO.getBuildVersion());
                    servDO.setCpuCnt(orderDO.getCpuCnt());
                    servDO.setMemSize(orderDO.getMemSize());
                    servDO.setDiskType(orderDO.getDiskType());
                    servDO.setDataSize(orderDO.getDataSize());
                    servDO.setLogSize(orderDO.getLogSize());
                    servDO.setPort(orderDO.getPort());
                }
            }
        }
    }

    public TaskDO buildCreateTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_CREATE);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        if (BooleanUtils.isFalse(servGroupDO.getFlag())) {
            List<ServDO> servDOs = servGroupDO.getServs();
            for (ServDO servDO : servDOs) {
                SubtaskDO subtask_remove = new SubtaskDO();
                subtaskDOs.add(subtask_remove);

                subtask_remove.setId(PrimaryKeyUtil.get());
                subtask_remove.setTaskId(taskDO.getId());
                subtask_remove.setObjType(DictConsts.OBJ_TYPE_SERV);
                subtask_remove.setObjId(servDO.getId());
                subtask_remove.setObjName(servDO.getType());
                subtask_remove.setActionType(DictConsts.ACTION_TYPE_REMOVE);
                subtask_remove.setPriority(priority);
                subtask_remove.setState(DictConsts.TASK_STATE_READY);
                SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_REMOVE);
                if (subtaskCfgDO != null) {
                    subtask_remove.setTimeout(subtaskCfgDO.getTimeout());
                }
                subtask_remove.setDataSource(servDO);
            }
            priority++;
        }

        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            SubtaskDO subtask_create = new SubtaskDO();
            subtaskDOs.add(subtask_create);

            subtask_create.setId(PrimaryKeyUtil.get());
            subtask_create.setTaskId(taskDO.getId());
            subtask_create.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_create.setObjId(servDO.getId());
            subtask_create.setObjName(servDO.getType());
            subtask_create.setActionType(DictConsts.ACTION_TYPE_CREATE);
            subtask_create.setPriority(priority);
            subtask_create.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_CREATE);
            if (subtaskCfgDO != null) {
                subtask_create.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask_create.setDataSource(servDO);
        }

        return taskDO;
    }

    public TaskDO buildStartTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_START);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            SubtaskDO subtask = new SubtaskDO();
            subtaskDOs.add(subtask);

            subtask.setId(PrimaryKeyUtil.get());
            subtask.setTaskId(taskDO.getId());
            subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask.setObjId(servDO.getId());
            subtask.setObjName(servDO.getType());
            subtask.setActionType(DictConsts.ACTION_TYPE_START);
            subtask.setPriority(priority);
            subtask.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_START);
            if (subtaskCfgDO != null) {
                subtask.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask.setDataSource(servDO);
        }
        return taskDO;
    }

    public TaskDO buildStopTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_STOP);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            SubtaskDO subtask = new SubtaskDO();
            subtaskDOs.add(subtask);

            subtask.setId(PrimaryKeyUtil.get());
            subtask.setTaskId(taskDO.getId());
            subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask.setObjId(servDO.getId());
            subtask.setObjName(servDO.getType());
            subtask.setActionType(DictConsts.ACTION_TYPE_STOP);
            subtask.setPriority(priority);
            subtask.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_STOP);
            if (subtaskCfgDO != null) {
                subtask.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask.setDataSource(servDO);
        }
        return taskDO;
    }

    public TaskDO buildImageUpdateTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_IMAGE_UPDATE);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getMajorVersion() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                List<ServDO> servDOs = servGroupDO.getServs();
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        SubtaskDO subtask = new SubtaskDO();
                        subtaskDOs.add(subtask);

                        subtask.setId(PrimaryKeyUtil.get());
                        subtask.setTaskId(taskDO.getId());
                        subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                        subtask.setObjId(servDO.getId());
                        subtask.setObjName(servDO.getType());
                        subtask.setActionType(DictConsts.ACTION_TYPE_IMAGE_UPDATE);
                        subtask.setPriority(priority);
                        subtask.setState(DictConsts.TASK_STATE_READY);
                        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                                DictConsts.ACTION_TYPE_IMAGE_UPDATE);
                        if (subtaskCfgDO != null) {
                            subtask.setTimeout(subtaskCfgDO.getTimeout());
                        }
                        subtask.setDataSource(servDO);
                    }
                }
                break;
            }
        }

        return taskDO;
    }

    public TaskDO buildScaleUpCpuMemTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getCpuCnt() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                List<ServDO> servDOs = servGroupDO.getServs();
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        SubtaskDO subtask = new SubtaskDO();
                        subtaskDOs.add(subtask);

                        subtask.setId(PrimaryKeyUtil.get());
                        subtask.setTaskId(taskDO.getId());
                        subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                        subtask.setObjId(servDO.getId());
                        subtask.setObjName(servDO.getType());
                        subtask.setActionType(DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM);
                        subtask.setPriority(priority);
                        subtask.setState(DictConsts.TASK_STATE_READY);
                        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                                DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM);
                        if (subtaskCfgDO != null) {
                            subtask.setTimeout(subtaskCfgDO.getTimeout());
                        }
                        subtask.setDataSource(servDO);
                    }
                }
                break;
            }
        }

        return taskDO;
    }

    public TaskDO buildScaleUpStorageTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_SCALE_UP_STORAGE);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getDataSize() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                List<ServDO> servDOs = servGroupDO.getServs();
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        SubtaskDO subtask = new SubtaskDO();
                        subtaskDOs.add(subtask);

                        subtask.setId(PrimaryKeyUtil.get());
                        subtask.setTaskId(taskDO.getId());
                        subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                        subtask.setObjId(servDO.getId());
                        subtask.setObjName(servDO.getType());
                        subtask.setActionType(DictConsts.ACTION_TYPE_SCALE_UP_STORAGE);
                        subtask.setPriority(priority);
                        subtask.setState(DictConsts.TASK_STATE_READY);
                        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                                DictConsts.ACTION_TYPE_SCALE_UP_STORAGE);
                        if (subtaskCfgDO != null) {
                            subtask.setTimeout(subtaskCfgDO.getTimeout());
                        }
                        subtask.setDataSource(servDO);
                    }
                }
                break;
            }
        }

        return taskDO;
    }

    public TaskDO buildArchUpTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_ARCH_UP);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getArchMode() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                List<ServDO> servDOs = servGroupDO.getServs();
                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        SubtaskDO subtask = new SubtaskDO();
                        subtaskDOs.add(subtask);

                        subtask.setId(PrimaryKeyUtil.get());
                        subtask.setTaskId(taskDO.getId());
                        subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                        subtask.setObjId(servDO.getId());
                        subtask.setObjName(servDO.getType());
                        subtask.setActionType(DictConsts.ACTION_TYPE_ARCH_UP);
                        subtask.setPriority(priority);
                        subtask.setState(DictConsts.TASK_STATE_READY);
                        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                                DictConsts.ACTION_TYPE_ARCH_UP);
                        if (subtaskCfgDO != null) {
                            subtask.setTimeout(subtaskCfgDO.getTimeout());
                        }
                        subtask.setDataSource(servDO);
                    }
                }
                break;
            }
        }

        return taskDO;
    }

    public void setServGroupStateBaseDTO(ServGroupStateBaseDTO servGroupStateBaseDTO, ServGroupDO servGroupDO,
            CmSite cmSite, List<CmService> cmServices, List<DictTypeDO> dictTypeDOs, UserDO owner,
            TaskDO latestTaskDO) {
        setServGroupBaseDTO(servGroupStateBaseDTO, servGroupDO, cmSite, dictTypeDOs, owner);
        if (latestTaskDO != null) {
            TaskBaseDTO taskDTO = new TaskBaseDTO();
            servGroupStateBaseDTO.setTask(taskDTO);

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

        DisplayDTO stateDisplayDTO = new DisplayDTO();
        servGroupStateBaseDTO.setState(stateDisplayDTO);
        String state = getState(servGroupDO, cmServices);
        stateDisplayDTO.setCode(state);
        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, state);
        if (stateDictDO != null) {
            stateDisplayDTO.setDisplay(stateDictDO.getName());
        }

        Boolean meetExpectation = isMeetExpectation(servGroupDO, cmServices, cmSite);
        servGroupStateBaseDTO.setMeetExpectation(meetExpectation);

    }

    private Boolean isMeetExpectation(ServGroupDO servGroupDO, List<CmService> cmServices, CmSite cmSite) {
        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            CmService cmService = CmApi.findService(cmServices, servDO.getRelateId());
            if (cmService == null) {
                return false;
            }

            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus == null) {
                return false;
            }

            List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
            if (cmUnits == null || cmUnits.size() != servDO.getUnitCnt()) {
                return false;
            }

            for (CmService.Status.Unit cmUnit : cmUnits) {
                CmImageBase cmImage = cmUnit.getImage();
                if (cmImage == null) {
                    return false;
                }

                if (!servDO.getMajorVersion().equals(cmImage.getMajor())
                        || !servDO.getMinorVersion().equals(cmImage.getMinor())
                        || !servDO.getPatchVersion().equals(cmImage.getPatch())
                        || !servDO.getBuildVersion().equals(cmImage.getBuild())) {
                    return false;
                }

                CmService.Resources cmResources = cmUnit.getResources();
                if (cmResources == null) {
                    return false;
                }

                double cpuCnt = NumberUnits.retainDigits(cmResources.getMilicpu() / 1000.0);
                double memSize = NumberUnits.retainDigits(cmResources.getMemory() / 1024.0);
                if (!servDO.getCpuCnt().equals(cpuCnt) || !servDO.getMemSize().equals(memSize)) {
                    return false;
                }

                CmService.Resources.Storage cmStorage = cmResources.getStorage();
                if (cmStorage == null) {
                    return false;
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
                if (!servDO.getDataSize().equals(NumberUnits.ceil(dataSize / 1024.0))
                        || !servDO.getLogSize().equals(NumberUnits.ceil(logSize / 1024.0))) {
                    return false;
                }

            }
        }
        return true;
    }

    public void setServGroupBaseDTO(ServGroupBaseDTO servGroupBaseDTO, ServGroupDO servGroupDO, CmSiteBase cmSite,
            List<DictTypeDO> dictTypeDOs, UserDO owner) {
        servGroupBaseDTO.setId(servGroupDO.getId());
        servGroupBaseDTO.setCategory(servGroupDO.getCategory());

        DisplayDTO archDisplayDTO = new DisplayDTO();
        servGroupBaseDTO.setSysArchitecture(archDisplayDTO);

        archDisplayDTO.setCode(servGroupDO.getSysArchitecture());
        DictDO archDictDO = findDictDO(dictTypeDOs, DictTypeConsts.SYS_ARCHITECTURE, servGroupDO.getSysArchitecture());
        if (archDictDO != null) {
            archDisplayDTO.setDisplay(archDictDO.getName());
        }

        BusinessSubsystemDO businessSubsystemDO = servGroupDO.getBusinessSubsystem();
        if (businessSubsystemDO != null) {
            BusinessSubsystemBaseDTO businessSubsystemDTO = new BusinessSubsystemBaseDTO();
            servGroupBaseDTO.setBusinessSubsystem(businessSubsystemDTO);

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

        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        if (businessAreaDO != null) {
            IdentificationDTO businessAreaDTO = new IdentificationDTO();
            servGroupBaseDTO.setBusinessArea(businessAreaDTO);
            businessAreaDTO.setId(businessAreaDO.getId());
            businessAreaDTO.setName(businessAreaDO.getName());
        }

        if (cmSite != null) {
            IdentificationDTO siteDTO = new IdentificationDTO();
            servGroupBaseDTO.setSite(siteDTO);
            siteDTO.setId(cmSite.getId());
            siteDTO.setName(cmSite.getName());
        }

        servGroupBaseDTO.setName(servGroupDO.getName());
        servGroupBaseDTO.setFlag(servGroupDO.getFlag());

        List<ServDO> servDOs = servGroupDO.getServs();
        Set<String> types = new HashSet<>();
        for (ServDO servDO : servDOs) {
            types.add(servDO.getType());
        }
        if (types.size() > 1) {
            servGroupBaseDTO.setHighAvailable(true);
        } else {
            servGroupBaseDTO.setHighAvailable(false);
        }

        UserBaseDTO userDTO = new UserBaseDTO();
        servGroupBaseDTO.setOwner(userDTO);
        userDTO.setUsername(servGroupDO.getOwner());
        if (owner != null) {
            userDTO.setName(owner.getName());
            userDTO.setCompany(owner.getCompany());
            userDTO.setTelephone(owner.getTelephone());
        }

        servGroupBaseDTO.setGmtCreate(DateUtils.dateTimeToString(servGroupDO.getGmtCreate()));

    }

    public String getState(ServGroupDO servGroupDO, List<CmService> cmServices) {
        List<ServDO> servDOs = servGroupDO.getServs();
        int passingCnt = 0;
        int criticalCnt = 0;
        for (ServDO servDO : servDOs) {
            CmService cmService = CmApi.findService(cmServices, servDO.getRelateId());
            if (cmService != null) {
                CmService.Status cmStatus = cmService.getStatus();
                if (cmStatus != null) {
                    switch (cmStatus.getState()) {
                    case CmConsts.STATE_PASSING:
                        passingCnt++;
                        break;
                    case CmConsts.STATE_CRITICAL:
                        criticalCnt++;
                        break;
                    }
                }
            }
        }

        if (criticalCnt > 0) {
            return DictConsts.STATE_CRITICAL;
        } else if (passingCnt == servDOs.size()) {
            return DictConsts.STATE_PASSING;
        } else {
            return DictConsts.STATE_WARNNING;
        }
    }

    public void setServGroupDetailDTO(ServGroupDetailDTO servGroupDetailDTO, ServGroupDO servGroupDO, CmSite cmSite,
            List<CmService> cmServices, List<DictTypeDO> dictTypeDOs, UserDO owner, TaskDO latestTaskDO) {
        setServGroupStateBaseDTO(servGroupDetailDTO, servGroupDO, cmSite, cmServices, dictTypeDOs, owner, latestTaskDO);
    }

    public TaskDO buildRemoveTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_REMOVE);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            SubtaskDO subtask_remove = new SubtaskDO();
            subtaskDOs.add(subtask_remove);

            subtask_remove.setId(PrimaryKeyUtil.get());
            subtask_remove.setTaskId(taskDO.getId());
            subtask_remove.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_remove.setObjId(servDO.getId());
            subtask_remove.setObjName(servDO.getType());
            subtask_remove.setActionType(DictConsts.ACTION_TYPE_REMOVE);
            subtask_remove.setPriority(priority);
            subtask_remove.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_REMOVE);
            if (subtaskCfgDO != null) {
                subtask_remove.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask_remove.setDataSource(servDO);
        }
        return taskDO;
    }

    @Override
    public void executeTaskStart(TaskDO taskDO) {
        ServGroupDO servGroupDO = servGroupDAO.get(taskDO.getObjId());
        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        switch (taskDO.getActionType()) {
        case DictConsts.ACTION_TYPE_CREATE:
        case DictConsts.ACTION_TYPE_IMAGE_UPDATE:
        case DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM:
        case DictConsts.ACTION_TYPE_SCALE_UP_STORAGE:
        case DictConsts.ACTION_TYPE_ARCH_UP:
            orderGroupDO.setState(DictConsts.ORDER_STATE_EXECUTING);
            orderGroupDAO.updateStateAndMsg(orderGroupDO);
            break;
        case DictConsts.ACTION_TYPE_REMOVE:
            if (BooleanUtils.isTrue(servGroupDO.getFlag())) {
                orderGroupDO.setState(DictConsts.ORDER_STATE_EXECUTING);
                orderGroupDAO.updateStateAndMsg(orderGroupDO);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public TaskResult executeSubtask(TaskDO taskDO, SubtaskDO subtaskDO) {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        switch (subtaskDO.getActionType()) {
        case DictConsts.ACTION_TYPE_CREATE:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                CmServiceBody serviceBody = buildCmServiceCreateRequestBody(servDO);
                CmSaveServiceResp cmSaveServiceResp = CmApi.saveService(serviceBody);
                if (cmSaveServiceResp == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("接口返回错误。");
                    return taskResult;
                }

                servDO.setRelateId(cmSaveServiceResp.getId());
                servDAO.updateRelateId(servDO);

                taskResult = pollingCreateTask(subtaskDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("创建异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_START:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                CmApi.startService(servDO.getRelateId());

                taskResult = pollingState(subtaskDO, CmConsts.STATE_PASSING);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("启动异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_STOP:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                CmApi.stopService(servDO.getRelateId());

                taskResult = pollingState(subtaskDO, CmConsts.STATE_CRITICAL);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("停止异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_IMAGE_UPDATE:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                CmServiceBody serviceBody = buildCmServiceImageUpdateRequestBody(servDO);
                CmApi.updateServiceImage(servDO.getRelateId(), serviceBody);

                taskResult = pollingImageUpdateTask(subtaskDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("升级异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                CmServiceBody serviceBody = buildCmServiceScaleUpCpuMemRequestBody(servDO);
                CmApi.scaleUpCpuMemService(servDO.getRelateId(), serviceBody);

                taskResult = pollingScaleUpCpuMemTask(subtaskDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("计算扩容异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_SCALE_UP_STORAGE:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                CmServiceBody serviceBody = buildCmServiceScaleUpStorageRequestBody(servDO);
                CmApi.scaleUpStorageService(servDO.getRelateId(), serviceBody);

                taskResult = pollingScaleUpStorageTask(subtaskDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("存储扩容异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_ARCH_UP:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                CmServiceBody serviceBody = buildCmServiceArchUpRequestBody(servDO);
                CmApi.archUpService(servDO.getRelateId(), serviceBody);

                taskResult = pollingArchUpTask(subtaskDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("节点扩展异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_REMOVE:
            try {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                if (StringUtils.isNotBlank(servDO.getRelateId())) {
                    if (BooleanUtils.isTrue(servDO.getMonitorFlag())) {
                        CmApi.unregisterMonitor(servDO.getRelateId());
                    }
                    CmApi.removeService(servDO.getRelateId(), subtaskDO.getTimeout().intValue());
                }

                List<UnitDO> unitDOs = servDO.getUnits();
                for (UnitDO unitDO : unitDOs) {
                    unitDO.setRelateId(null);
                    unitDAO.update(unitDO);

                    forceRebuildLogDAO.removeByUnitRelateId(unitDO.getRelateId());
                }

                servDO.setRelateId(null);
                servDAO.updateRelateId(servDO);

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("删除异常：", e);
            }
            break;
        }
        return taskResult;
    }

    private TaskResult pollingCreateTask(SubtaskDO subtaskDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        boolean updateUnitRelateId = true;
        ServDO servDO = (ServDO) subtaskDO.getDataSource();
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                CmService.Status cmStatus = cmService.getStatus();
                if (cmStatus != null) {
                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                    if (cmUnits != null && cmUnits.size() > 0 && updateUnitRelateId) {
                        List<UnitDO> unitDOs = servDO.getUnits();
                        for (int i = 0; i < cmUnits.size(); i++) {
                            UnitDO unitDO = unitDOs.get(i);
                            unitDO.setRelateId(cmUnits.get(i).getId());
                            unitDAO.update(unitDO);
                        }
                        updateUnitRelateId = false;
                    }
                }

                if (cmStatus != null && cmStatus.getPhase().equals(CmConsts.SERV_PHASE_READY)) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    CmImageBase image = cmService.getSpec().getImage();
                    if (image != null) {
                        CmImage cmImage = CmApi.getImage(image.getId());
                        if (cmImage != null && cmImage.getExporterPort() != null) {
                            boolean monitorFlag = true;
                            try {
                                CmApi.registerMonitor(servDO.getRelateId());
                            } catch (Exception e) {
                                monitorFlag = false;
                                logger.error("监控注册失败：", e);
                            }
                            servDO.setMonitorFlag(monitorFlag);
                            servDAO.updateMonitorFlag(servDO);
                        }
                    }
                    return taskResult;
                } else if (cmStatus != null && cmStatus.getPhase().equals(CmConsts.SERV_PHASE_CREATE_FAILED)
                        || cmStatus.getPhase().equals(CmConsts.SERV_PHASE_COMPOSE_FAILED)) {
                    taskResult.setMsg(cmStatus.getErrMsg());
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                        taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                        taskResult.setMsg("timeout");
                        return taskResult;
                    } else {
                        Thread.sleep(10000);
                        subtaskDO = subtaskDAO.get(subtaskDO.getId());
                        if (subtaskDO == null) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            taskResult.setMsg("none object");
                            return taskResult;
                        }
                    }
                }
            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    private TaskResult pollingState(SubtaskDO subtaskDO, String specialState) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        ServDO servDO = (ServDO) subtaskDO.getDataSource();
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                String state = "";
                CmService.Status cmStatus = cmService.getStatus();
                if (cmStatus != null) {
                    state = cmStatus.getState();
                }

                if (state.equals(specialState)) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                        taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                        taskResult.setMsg("timeout");
                        return taskResult;
                    } else {
                        Thread.sleep(10000);
                        subtaskDO = subtaskDAO.get(subtaskDO.getId());
                        if (subtaskDO == null) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            taskResult.setMsg("none object");
                            return taskResult;
                        }
                    }
                }
            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    private TaskResult pollingImageUpdateTask(SubtaskDO subtaskDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        ServDO servDO = (ServDO) subtaskDO.getDataSource();
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                boolean complete = false;
                CmService.Spec cmSpec = cmService.getSpec();
                if (cmSpec != null) {
                    CmImageBase cmSpecImage = cmSpec.getImage();
                    if (cmSpecImage != null) {
                        CmService.Status cmStatus = cmService.getStatus();
                        if (cmStatus != null) {
                            List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                            int equalCnt = 0;
                            for (CmService.Status.Unit cmUnit : cmUnits) {
                                CmImageBase cmStatusImage = cmUnit.getImage();
                                if (cmStatusImage != null && cmSpecImage.getId().equals(cmStatusImage.getId())
                                        && CmConsts.STATE_PASSING.equals(cmUnit.getState())) {
                                    equalCnt++;
                                }
                            }
                            if (cmUnits.size() != 0 && equalCnt == cmUnits.size()) {
                                complete = true;
                            }
                        }
                    }
                }

                if (complete) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                        taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                        taskResult.setMsg("timeout");
                        return taskResult;
                    } else {
                        Thread.sleep(10000);
                        subtaskDO = subtaskDAO.get(subtaskDO.getId());
                        if (subtaskDO == null) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            taskResult.setMsg("none object");
                            return taskResult;
                        }
                    }
                }

            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    private TaskResult pollingScaleUpCpuMemTask(SubtaskDO subtaskDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        ServDO servDO = (ServDO) subtaskDO.getDataSource();
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                boolean complete = false;
                CmService.Spec cmSpec = cmService.getSpec();
                if (cmSpec != null) {
                    CmService.Spec.Unit cmSpecUnit = cmSpec.getUnit();
                    if (cmSpecUnit != null) {
                        CmService.Spec.Unit.Resources cmSpecResources = cmSpecUnit.getResources();
                        if (cmSpecResources != null) {
                            CmService.Spec.Unit.Resources.Requests cmSpecRequests = cmSpecResources.getRequests();
                            if (cmSpecRequests != null) {
                                CmService.Status cmStatus = cmService.getStatus();
                                if (cmStatus != null) {
                                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                                    int equalCnt = 0;
                                    for (CmService.Status.Unit cmUnit : cmUnits) {
                                        CmService.Resources cmStatusResources = cmUnit.getResources();
                                        if (cmStatusResources != null) {
                                            if (cmSpecRequests.getMilicpu().equals(cmStatusResources.getMilicpu())
                                                    && cmSpecRequests.getMemory().equals(cmStatusResources.getMemory())
                                                    && CmConsts.STATE_PASSING.equals(cmUnit.getState())) {
                                                equalCnt++;
                                            }
                                        }
                                    }
                                    if (cmUnits.size() != 0 && equalCnt == cmUnits.size()) {
                                        complete = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (complete) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                        taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                        taskResult.setMsg("timeout");
                        return taskResult;
                    } else {
                        Thread.sleep(10000);
                        subtaskDO = subtaskDAO.get(subtaskDO.getId());
                        if (subtaskDO == null) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            taskResult.setMsg("none object");
                            return taskResult;
                        }
                    }
                }

            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    private TaskResult pollingScaleUpStorageTask(SubtaskDO subtaskDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        ServDO servDO = (ServDO) subtaskDO.getDataSource();
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                boolean complete = false;
                CmService.Spec cmSpec = cmService.getSpec();
                if (cmSpec != null) {
                    CmService.Spec.Unit cmSpecUnit = cmSpec.getUnit();
                    if (cmSpecUnit != null) {
                        CmService.Spec.Unit.Resources cmSpecResources = cmSpecUnit.getResources();
                        if (cmSpecResources != null) {
                            CmService.Spec.Unit.Resources.Requests cmSpecRequests = cmSpecResources.getRequests();
                            if (cmSpecRequests != null) {
                                CmService.Resources.Storage cmSpecStorage = cmSpecRequests.getStorage();
                                CmService.Status cmStatus = cmService.getStatus();
                                if (cmStatus != null) {
                                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                                    int equalCnt = 0;
                                    for (CmService.Status.Unit cmUnit : cmUnits) {
                                        CmService.Resources cmStatusResources = cmUnit.getResources();
                                        if (cmStatusResources != null) {
                                            CmService.Resources.Storage cmStatusStorage = cmStatusResources
                                                    .getStorage();
                                            boolean equalsStorage = equalsStorage(cmSpecStorage, cmStatusStorage);
                                            if (equalsStorage) {
                                                equalCnt++;
                                            }
                                        }
                                    }
                                    if (cmUnits.size() != 0 && equalCnt == cmUnits.size()) {
                                        complete = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (complete) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                        taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                        taskResult.setMsg("timeout");
                        return taskResult;
                    } else {
                        Thread.sleep(10000);
                        subtaskDO = subtaskDAO.get(subtaskDO.getId());
                        if (subtaskDO == null) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            taskResult.setMsg("none object");
                            return taskResult;
                        }
                    }
                }

            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    private TaskResult pollingArchUpTask(SubtaskDO subtaskDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        ServDO servDO = (ServDO) subtaskDO.getDataSource();
        boolean updateUnit = true;
        boolean removeUnit = false;
        List<UnitDO> unitDOs = servDO.getUnits();
        if (servDO.getUnitCnt().intValue() != unitDOs.size()) {
            removeUnit = true;
        }
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                CmService.Status cmStatus = cmService.getStatus();
                if (cmStatus != null) {
                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                    if (cmUnits != null) {
                        if (removeUnit) {
                            List<UnitDO> removeUnitDOs = new ArrayList<>();
                            for (UnitDO unitDO : unitDOs) {
                                boolean exist = false;
                                for (CmService.Status.Unit cmUnit : cmUnits) {
                                    if (cmUnit.getId().equals(unitDO.getRelateId())) {
                                        exist = true;
                                        break;
                                    }
                                }
                                if (!exist) {
                                    removeUnitDOs.add(unitDO);
                                }
                            }
                            for (UnitDO unitDO : removeUnitDOs) {
                                unitDAO.remove(unitDO.getId());
                            }
                        } else {
                            if (updateUnit && cmUnits.size() == servDO.getUnitCnt().intValue()) {

                                List<UnitDO> addUnitDOs = new ArrayList<>();
                                for (UnitDO unitDO : unitDOs) {
                                    if (StringUtils.isBlank(unitDO.getRelateId())) {
                                        addUnitDOs.add(unitDO);
                                    }
                                }
                                if (addUnitDOs.size() > 0) {
                                    List<CmService.Status.Unit> addCmUnits = new ArrayList<>();
                                    for (CmService.Status.Unit cmUnit : cmUnits) {
                                        boolean exist = false;
                                        for (UnitDO unitDO : unitDOs) {
                                            if (cmUnit.getId().equals(unitDO.getRelateId())) {
                                                exist = true;
                                                break;
                                            }
                                        }
                                        if (!exist) {
                                            addCmUnits.add(cmUnit);
                                        }
                                    }

                                    for (int i = 0; i < addCmUnits.size(); i++) {
                                        UnitDO unitDO = addUnitDOs.get(i);
                                        unitDO.setRelateId(addCmUnits.get(i).getId());
                                        unitDAO.update(unitDO);
                                    }
                                    updateUnit = false;
                                }

                            }
                        }
                    }
                }

                if (cmStatus != null && cmStatus.getPhase().equals(CmConsts.SERV_PHASE_READY)) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                        taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                        taskResult.setMsg("timeout");
                        return taskResult;
                    } else {
                        Thread.sleep(10000);
                        subtaskDO = subtaskDAO.get(subtaskDO.getId());
                        if (subtaskDO == null) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            taskResult.setMsg("none object");
                            return taskResult;
                        }
                    }
                }
            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    private boolean equalsStorage(CmService.Resources.Storage cmSpecStorage,
            CmService.Resources.Storage cmStatusStorage) {
        if (cmSpecStorage == null || cmStatusStorage == null) {
            return false;
        }
        List<CmService.Resources.Storage.Volume> cmSpecVolumes = cmSpecStorage.getVolumes();
        List<CmService.Resources.Storage.Volume> cmStatusVolumes = cmStatusStorage.getVolumes();

        if (cmSpecVolumes == null || cmSpecVolumes.size() == 0 || cmStatusVolumes == null || cmStatusVolumes.size() == 0
                || cmSpecVolumes.size() != cmStatusVolumes.size()) {
            return false;
        }
        int equalCnt = 0;
        for (CmService.Resources.Storage.Volume cmSpecVolume : cmSpecVolumes) {
            for (CmService.Resources.Storage.Volume cmStatusVolume : cmStatusVolumes) {
                if (cmSpecVolume.getType().equals(cmStatusVolume.getType())
                        && cmSpecVolume.getCapacity().equals(cmStatusVolume.getCapacity())) {
                    equalCnt++;
                    break;
                }
            }
        }
        if (equalCnt == cmSpecVolumes.size()) {
            return true;
        }
        return false;
    }

    @Override
    public void executeTaskDone(TaskDO taskDO) {
        ServGroupDO servGroupDO = servGroupDAO.get(taskDO.getObjId());
        if (servGroupDO == null) {
            return;
        }
        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        switch (taskDO.getActionType()) {
        case DictConsts.ACTION_TYPE_CREATE:
            if (taskDO.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                servGroupDO.setFlag(true);
                orderGroupDO.setState(DictConsts.ORDER_STATE_SUCCESS);
            } else {
                servGroupDO.setFlag(false);
                orderGroupDO.setState(DictConsts.ORDER_STATE_FAILED);
            }
            servGroupDAO.update(servGroupDO);
            orderGroupDAO.updateStateAndMsg(orderGroupDO);
            break;
        case DictConsts.ACTION_TYPE_IMAGE_UPDATE:
        case DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM:
        case DictConsts.ACTION_TYPE_SCALE_UP_STORAGE:
        case DictConsts.ACTION_TYPE_ARCH_UP:
            if (taskDO.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                orderGroupDO.setState(DictConsts.ORDER_STATE_SUCCESS);
            } else {
                orderGroupDO.setState(DictConsts.ORDER_STATE_FAILED);
            }
            orderGroupDAO.updateStateAndMsg(orderGroupDO);
            break;
        case DictConsts.ACTION_TYPE_REMOVE:
            if (taskDO.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                List<ServDO> servDOs = servGroupDO.getServs();
                for (ServDO servDO : servDOs) {
                    List<UnitDO> unitDOs = servDO.getUnits();
                    for (UnitDO unitDO : unitDOs) {
                        unitDAO.remove(unitDO.getId());
                    }
                    servDAO.remove(servDO.getId());
                }
                servGroupDAO.remove(servGroupDO.getId());
                orderGroupDAO.removeCascadeByName(servGroupDO.getName());
                for (ServDO servDO : servDOs) {
                    List<UnitDO> unitDOs = servDO.getUnits();
                    for (UnitDO unitDO : unitDOs) {
                        taskDAO.removeCascadeByObjTypeAndObjId(DictConsts.OBJ_TYPE_UNIT, unitDO.getId());
                    }
                    taskDAO.removeCascadeByObjTypeAndObjId(DictConsts.OBJ_TYPE_SERV, servDO.getId());
                }
                taskDAO.removeCascadeByObjTypeAndObjId(DictConsts.OBJ_TYPE_SERV_GROUP, servGroupDO.getId());
            } else {
                orderGroupDO.setState(DictConsts.ORDER_STATE_FAILED);
                orderGroupDAO.updateStateAndMsg(orderGroupDO);
            }
            break;
        default:
            break;
        }
    }

    private CmServiceBody buildCmServiceCreateRequestBody(ServDO servDO) throws Exception {
        CmServiceBody cmServiceBody = new CmServiceBody();
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        CmSite cmSite = CmApi.getSite(servGroupDO.getBusinessArea().getSiteId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getType().equals(servDO.getType())) {
                String name = orderGroupDO.getName() + "-" + RandomUtil.getRandomCharAndNum(3);
                cmServiceBody.setName(name);
                cmServiceBody.setGroup(orderGroupDO.getName());
                cmServiceBody.setGroupType(orderGroupDO.getCategory());
                cmServiceBody.setArch(orderGroupDO.getSysArchitecture());
                cmServiceBody.setDesc("");

                CmServiceBody.Spec spec = cmServiceBody.new Spec();
                cmServiceBody.setSpec(spec);

                CmServiceBody.Spec.Image image = spec.new Image();
                spec.setImage(image);

                CmImageQuery imageQuery = new CmImageQuery();
                imageQuery.setSiteId(orderGroupDO.getBusinessArea().getSiteId());
                imageQuery.setType(orderDO.getType());
                imageQuery.setMajor(orderDO.getMajorVersion());
                imageQuery.setMinor(orderDO.getMinorVersion());
                imageQuery.setPatch(orderDO.getPatchVersion());
                imageQuery.setBuild(orderDO.getBuildVersion());
                imageQuery.setArch(orderGroupDO.getSysArchitecture());
                List<CmImage> cmImages = CmApi.listImage(imageQuery);
                if (cmImages != null && cmImages.size() == 1) {
                    image.setId(cmImages.get(0).getId());
                }

                CmServiceBody.Spec.Arch arch = spec.new Arch();
                spec.setArch(arch);

                arch.setMode(orderDO.getArchMode());
                arch.setReplicas(orderDO.getUnitCnt());

                List<CmServiceBody.Spec.Port> ports = new ArrayList<>();
                spec.setPorts(ports);
                CmServiceBody.Spec.Port port = spec.new Port();
                ports.add(port);
                port.setName(CmConsts.PORT_NAME);
                port.setPort(orderDO.getPort());

                if (StringUtils.isNotBlank(orderDO.getCfg())) {
                    JSONObject cfgJson = JSONObject.parseObject(orderDO.getCfg());
                    JSONObject paramJson = cfgJson.getJSONObject("param");
                    if (paramJson != null && !paramJson.isEmpty()) {
                        spec.setOptions((Map<String, Object>) paramJson);
                    }
                }

                CmServiceBody.Spec.Conditions conditions = spec.new Conditions();
                spec.setConditions(conditions);

                CmServiceBody.Spec.Conditions.Info cluster = conditions.new Info();
                conditions.setCluster(cluster);
                CmClusterQuery cmClusterQuery = new CmClusterQuery();
                cmClusterQuery.setZone(orderGroupDO.getBusinessAreaId());
                List<CmCluster> cmClusters = CmApi.listCluster(cmClusterQuery);
                if (cmClusters != null && cmClusters.size() > 0) {
                    List<String> cmClusterIds = new ArrayList<>();
                    for (CmCluster cmCluster : cmClusters) {
                        cmClusterIds.add(cmCluster.getId());
                    }
                    cluster.setId(cmClusterIds);
                }
                cluster.setHighAvailable(orderDO.getClusterHA());

                CmServiceBody.Spec.Conditions.Info host = conditions.new Info();
                conditions.setHost(host);
                host.setHighAvailable(orderDO.getHostHA());

                CmServiceBody.Spec.Conditions.Info remoteStorage = conditions.new Info();
                conditions.setRemoteStorage(remoteStorage);
                remoteStorage.setHighAvailable(orderDO.getClusterHA());

                CmServiceBody.Spec.Conditions.Info network = conditions.new Info();
                conditions.setNetwork(network);
                network.setHighAvailable(orderDO.getClusterHA());
                List<String> cmNetworkIds = new ArrayList<>();
                network.setId(cmNetworkIds);
                boolean designatedNetwok = false;
                if (StringUtils.isNotBlank(orderDO.getCfg())) {
                    JSONObject cfgJson = JSONObject.parseObject(orderDO.getCfg());
                    if (cfgJson.containsKey("networks")) {
                        designatedNetwok = true;
                        cmNetworkIds = JSONObject.parseArray(cfgJson.getJSONArray("networks").toJSONString(),
                                String.class);
                    }
                }
                if (!designatedNetwok) {
                    CmNetworkQuery cmNetworkQuery = new CmNetworkQuery();
                    cmNetworkQuery.setZone(orderGroupDO.getBusinessAreaId());
                    List<CmNetwork> cmNetworks = CmApi.listNetwork(cmNetworkQuery);
                    if (cmNetworks != null && cmNetworks.size() > 0) {
                        for (CmNetwork cmNetwork : cmNetworks) {
                            cmNetworkIds.add(cmNetwork.getId());
                        }
                    }
                }

                CmServiceBody.Spec.Unit unit = spec.new Unit();
                spec.setUnit(unit);

                CmServiceBody.Spec.Unit.Resources resources = unit.new Resources();
                unit.setResources(resources);

                CmServiceBody.Spec.Unit.Resources.Request request = resources.new Request();
                resources.setRequest(request);

                request.setMiliCpu(new Double(orderDO.getCpuCnt() * 1000).intValue());
                request.setMemory(new Double(orderDO.getMemSize() * 1024).longValue());

                CmServiceBody.Spec.Unit.Resources.Request.Storage storage = request.new Storage();
                request.setStorage(storage);

                List<CmServiceBody.Spec.Unit.Resources.Request.Storage.Volume> volumes = new ArrayList<>();
                storage.setVolumes(volumes);

                CmServiceBody.Spec.Unit.Resources.Request.Storage.Volume dataVolume = storage.new Volume();
                volumes.add(dataVolume);
                dataVolume.setType(CmConsts.VOLUME_DATA);
                dataVolume.setCapacity(orderDO.getDataSize() * 1024L);

                CmServiceBody.Spec.Unit.Resources.Request.Storage.Volume logVolume = storage.new Volume();
                volumes.add(logVolume);
                logVolume.setType(CmConsts.VOLUME_LOG);
                logVolume.setCapacity(orderDO.getLogSize() * 1024L);

                CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
                if (kubernetes != null) {
                    if (CmConsts.STORAGE_TYPE_VOLUMEPATH.equals(kubernetes.getStorageMode())) {
                        storage.setMode(CmConsts.STORAGE_TYPE_VOLUMEPATH);
                        CmServiceBody.Spec.Unit.Resources.Request.Storage.VolumePath volumePath = storage.new VolumePath();
                        storage.setVolumePath(volumePath);

                        if (orderDO.getDiskType().equals(DictConsts.DISK_TYPE_LOCAL_HDD)) {
                            volumePath.setType(CmConsts.STORAGE_LOCAL);
                            volumePath.setPerformance(CmConsts.PERFORMANCE_MEDIUM);
                        } else if (orderDO.getDiskType().equals(DictConsts.DISK_TYPE_LOCAL_SSD)) {
                            volumePath.setType(CmConsts.STORAGE_LOCAL);
                            volumePath.setPerformance(CmConsts.PERFORMANCE_HIGH);
                        } else if (orderDO.getDiskType().equals(DictConsts.DISK_TYPE_REMOTE_STORAGE_HIGH)) {
                            volumePath.setType(CmConsts.STORAGE_REMOTE);
                            volumePath.setPerformance(CmConsts.PERFORMANCE_HIGH);
                        } else if (orderDO.getDiskType().equals(DictConsts.DISK_TYPE_REMOTE_STORAGE_MEDIUM)) {
                            volumePath.setType(CmConsts.STORAGE_REMOTE);
                            volumePath.setPerformance(CmConsts.PERFORMANCE_MEDIUM);
                        }
                    }
                }
                break;
            }
        }

        return cmServiceBody;
    }

    private CmServiceBody buildCmServiceImageUpdateRequestBody(ServDO servDO) throws Exception {
        CmServiceBody cmServiceBody = new CmServiceBody();
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());

        CmServiceBody.Spec spec = cmServiceBody.new Spec();
        cmServiceBody.setSpec(spec);

        CmServiceBody.Spec.Image image = spec.new Image();
        spec.setImage(image);

        CmImageQuery imageQuery = new CmImageQuery();
        imageQuery.setSiteId(servGroupDO.getBusinessArea().getSiteId());
        imageQuery.setType(servDO.getType());
        imageQuery.setMajor(servDO.getMajorVersion());
        imageQuery.setMinor(servDO.getMinorVersion());
        imageQuery.setPatch(servDO.getPatchVersion());
        imageQuery.setBuild(servDO.getBuildVersion());
        imageQuery.setArch(servGroupDO.getSysArchitecture());
        List<CmImage> cmImages = CmApi.listImage(imageQuery);
        if (cmImages != null && cmImages.size() == 1) {
            image.setId(cmImages.get(0).getId());
        }
        return cmServiceBody;
    }

    private CmServiceBody buildCmServiceScaleUpCpuMemRequestBody(ServDO servDO) {
        CmServiceBody cmServiceBody = new CmServiceBody();

        CmServiceBody.Spec spec = cmServiceBody.new Spec();
        cmServiceBody.setSpec(spec);

        CmServiceBody.Spec.Unit unit = spec.new Unit();
        spec.setUnit(unit);

        CmServiceBody.Spec.Unit.Resources resources = unit.new Resources();
        unit.setResources(resources);

        CmServiceBody.Spec.Unit.Resources.Request request = resources.new Request();
        resources.setRequest(request);

        request.setMiliCpu(new Double(servDO.getCpuCnt() * 1000).intValue());
        request.setMemory(new Double(servDO.getMemSize() * 1024).longValue());

        return cmServiceBody;
    }

    private CmServiceBody buildCmServiceScaleUpStorageRequestBody(ServDO servDO) throws Exception {
        CmServiceBody cmServiceBody = new CmServiceBody();
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        CmSite cmSite = CmApi.getSite(servGroupDO.getBusinessArea().getSiteId());

        CmServiceBody.Spec spec = cmServiceBody.new Spec();
        cmServiceBody.setSpec(spec);

        CmServiceBody.Spec.Unit unit = spec.new Unit();
        spec.setUnit(unit);

        CmServiceBody.Spec.Unit.Resources resources = unit.new Resources();
        unit.setResources(resources);

        CmServiceBody.Spec.Unit.Resources.Request request = resources.new Request();
        resources.setRequest(request);

        CmServiceBody.Spec.Unit.Resources.Request.Storage storage = request.new Storage();
        request.setStorage(storage);

        CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
        if (kubernetes != null) {
            List<CmServiceBody.Spec.Unit.Resources.Request.Storage.Volume> volumes = new ArrayList<>();

            CmServiceBody.Spec.Unit.Resources.Request.Storage.Volume dataVolume = storage.new Volume();
            volumes.add(dataVolume);
            dataVolume.setType(CmConsts.VOLUME_DATA);
            dataVolume.setCapacity(servDO.getDataSize() * 1024L);

            CmServiceBody.Spec.Unit.Resources.Request.Storage.Volume logVolume = storage.new Volume();
            volumes.add(logVolume);
            logVolume.setType(CmConsts.VOLUME_LOG);
            logVolume.setCapacity(servDO.getLogSize() * 1024L);
            storage.setVolumes(volumes);
        }

        return cmServiceBody;
    }

    private CmServiceBody buildCmServiceArchUpRequestBody(ServDO servDO) {
        CmServiceBody cmServiceBody = new CmServiceBody();

        CmServiceBody.Spec spec = cmServiceBody.new Spec();
        cmServiceBody.setSpec(spec);

        CmServiceBody.Spec.Arch arch = spec.new Arch();
        spec.setArch(arch);

        arch.setMode(servDO.getArchMode());
        arch.setReplicas(servDO.getUnitCnt());

        return cmServiceBody;
    }

    public ObjModel getObjModel(String servGroupId) {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            return new ObjModel(servGroupDO.getName(), servGroupDO.getCategory(),
                    servGroupDO.getBusinessArea().getSiteId());
        }
        return null;
    }
}