package com.bsg.dbscale.dbscale.aspect;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.dbscale.annotation.OperateLog;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.form.BackupStrategyForm;
import com.bsg.dbscale.service.form.BusinessAreaForm;
import com.bsg.dbscale.service.form.BusinessSubsystemForm;
import com.bsg.dbscale.service.form.BusinessSystemForm;
import com.bsg.dbscale.service.form.ClusterForm;
import com.bsg.dbscale.service.form.CmhaServGroupUserForm;
import com.bsg.dbscale.service.form.DBSchemaForm;
import com.bsg.dbscale.service.form.GroupForm;
import com.bsg.dbscale.service.form.HostForm;
import com.bsg.dbscale.service.form.ImageForm;
import com.bsg.dbscale.service.form.ImageTemplateForm;
import com.bsg.dbscale.service.form.LoginForm;
import com.bsg.dbscale.service.form.MysqlServGroupUserForm;
import com.bsg.dbscale.service.form.NetworkForm;
import com.bsg.dbscale.service.form.NfsForm;
import com.bsg.dbscale.service.form.OrderGroupForm;
import com.bsg.dbscale.service.form.PVCStorageForm;
import com.bsg.dbscale.service.form.ParamCfgForm;
import com.bsg.dbscale.service.form.RemoteStorageForm;
import com.bsg.dbscale.service.form.RemoteStoragePoolForm;
import com.bsg.dbscale.service.form.RoleForm;
import com.bsg.dbscale.service.form.ScaleForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;
import com.bsg.dbscale.service.form.SiteForm;
import com.bsg.dbscale.service.form.UserForm;
import com.bsg.dbscale.service.service.BackupFileService;
import com.bsg.dbscale.service.service.BackupStrategyService;
import com.bsg.dbscale.service.service.BaseService;
import com.bsg.dbscale.service.service.BaseService.ObjModel;
import com.bsg.dbscale.service.service.BusinessAreaService;
import com.bsg.dbscale.service.service.BusinessSubsystemService;
import com.bsg.dbscale.service.service.BusinessSystemService;
import com.bsg.dbscale.service.service.ClusterService;
import com.bsg.dbscale.service.service.GroupService;
import com.bsg.dbscale.service.service.HostService;
import com.bsg.dbscale.service.service.ImageService;
import com.bsg.dbscale.service.service.NetworkService;
import com.bsg.dbscale.service.service.NfsService;
import com.bsg.dbscale.service.service.OrderGroupService;
import com.bsg.dbscale.service.service.PVCStorageService;
import com.bsg.dbscale.service.service.RemoteStoragePoolService;
import com.bsg.dbscale.service.service.RemoteStorageService;
import com.bsg.dbscale.service.service.RoleService;
import com.bsg.dbscale.service.service.ScaleService;
import com.bsg.dbscale.service.service.ServGroupService;
import com.bsg.dbscale.service.service.SiteService;
import com.bsg.dbscale.service.service.SubtaskCfgService;
import com.bsg.dbscale.service.service.UnitService;
import com.bsg.dbscale.service.service.UserService;

/**
 * 日志切面类
 * 
 * @author HCK
 *
 */
@Aspect
@Component
public class OperateLogAspect {

    protected Logger logger = Logger.getLogger(getClass());

    @Resource(name = "baseService")
    private BaseService service;

    @Autowired
    private SiteService siteService;

    @Autowired
    private BusinessAreaService businessAreaService;

    @Autowired
    private NfsService nfsService;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private RemoteStorageService remoteStorageService;

    @Autowired
    private RemoteStoragePoolService remoteStoragePoolService;

    @Autowired
    private PVCStorageService pvcStorageService;

    @Autowired
    private HostService hostService;

    @Autowired
    private BusinessSystemService businessSystemService;

    @Autowired
    private BusinessSubsystemService businessSubsystemService;

    @Autowired
    private ScaleService scaleService;

    @Autowired
    private SubtaskCfgService subtaskCfgService;

    @Autowired
    private OrderGroupService orderGroupService;

    @Resource(name = "servGroupService")
    private ServGroupService servGroupService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private BackupStrategyService backupStrategyService;

    @Autowired
    private BackupFileService backupFileService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private HttpSession session;

    @Pointcut("execution(public * com.bsg.dbscale.dbscale.controller.*.*(..)) ")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) {
        Object object = null;
        try {
            String objType = null;
            String action = null;
            String objName = null;
            String description = null;
            String siteId = null;
            String operator = getOperator();
            Class<? extends Object> c = pjp.getTarget().getClass();
            OperateLog operateLog = c.getAnnotation(OperateLog.class);
            if (operateLog != null) {
                objType = operateLog.objType();
            }

            Object[] args = pjp.getArgs();
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();

            operateLog = method.getAnnotation(OperateLog.class);
            if (operateLog != null) {
                if (!operateLog.objType().equals("")) {
                    objType = operateLog.objType();
                }
                action = operateLog.action();
            }

            if (StringUtils.isNotBlank(objType) && StringUtils.isNotBlank(action)) {
                ObjModel objModel = null;
                switch (objType) {
                case "用户":
                    switch (action) {
                    case "登录":
                        LoginForm loginForm = (LoginForm) args[0];
                        objName = loginForm.getUsername();
                        description = "用户登录" + "[" + objName + "]";
                        operator = loginForm.getUsername();
                        break;
                    case "新增":
                        UserForm form = (UserForm) args[0];
                        objName = form.getUsername();
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "修改密码":
                        String username = (String) args[0];
                        objModel = userService.getObjModel(username);
                        break;
                    default:
                        break;
                    }
                    break;
                case "组别":
                    switch (action) {
                    case "新增":
                        GroupForm form = (GroupForm) args[0];
                        objName = form.getName();
                        break;
                    case "编辑":
                    case "删除":
                        String groupId = (String) args[0];
                        objModel = groupService.getObjModel(groupId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "组别用户":
                    switch (action) {
                    case "新增":
                        String groupId = (String) args[0];
                        objModel = groupService.getObjModel(groupId);
                        @SuppressWarnings("unchecked")
                        List<String> usernames = (List<String>) args[1];
                        objName = objModel.getName() + "(" + usernames + ")";
                        break;
                    case "删除":
                        groupId = (String) args[0];
                        objModel = groupService.getObjModel(groupId);
                        String username = (String) args[1];
                        objName = objModel.getName() + "(" + username + ")";
                        break;
                    default:
                        break;
                    }
                    break;
                case "角色":
                    switch (action) {
                    case "新增":
                        RoleForm form = (RoleForm) args[0];
                        objName = form.getName();
                        break;
                    case "编辑":
                    case "删除":
                    case "功能配置":
                        String roleId = (String) args[0];
                        objModel = roleService.getObjModel(roleId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "站点":
                    switch (action) {
                    case "新增":
                        SiteForm form = (SiteForm) args[0];
                        objName = form.getName();
                        break;
                    case "编辑":
                    case "删除":
                        siteId = (String) args[0];
                        objModel = siteService.getObjModel(siteId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "业务区":
                    switch (action) {
                    case "新增":
                        BusinessAreaForm form = (BusinessAreaForm) args[0];
                        objName = form.getName();
                        siteId = form.getSiteId();
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String businessAreaId = (String) args[0];
                        objModel = businessAreaService.getObjModel(businessAreaId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "NFS":
                    switch (action) {
                    case "新增":
                        NfsForm form = (NfsForm) args[0];
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        break;
                    case "启用":
                    case "停用":
                    case "删除":
                        String nfsId = (String) args[0];
                        objModel = nfsService.getObjModel(nfsId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "集群":
                    switch (action) {
                    case "新增":
                        ClusterForm form = (ClusterForm) args[0];
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String clusterId = (String) args[0];
                        objModel = clusterService.getObjModel(clusterId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "网段":
                    switch (action) {
                    case "新增":
                        NetworkForm form = (NetworkForm) args[0];
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String networkId = (String) args[0];
                        objModel = networkService.getObjModel(networkId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "镜像":
                    switch (action) {
                    case "注册":
                        ImageForm form = (ImageForm) args[0];
                        objName = form.getType() + form.getMajor() + "." + form.getMinor() + "." + form.getPatch() + "."
                                + form.getBuild();
                        break;
                    case "启用":
                    case "停用":
                    case "注销":
                        String imageId = (String) args[0];
                        objModel = imageService.getObjModel(imageId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "镜像模板":
                    switch (action) {
                    case "编辑":
                        String type = (String) args[0];
                        siteId = (String) args[1];
                        Integer major = (Integer) args[2];
                        Integer minor = (Integer) args[3];
                        Integer patch = (Integer) args[4];
                        Integer build = (Integer) args[5];
                        ImageTemplateForm imageTemplateForm = (ImageTemplateForm) args[7];
                        String imageName = type + major + "." + minor + "." + patch + "." + build;
                        objName = imageTemplateForm.getKey();
                        description = action + imageName + objType + "[" + objName + "]";
                        break;
                    default:
                        break;
                    }
                    break;
                case "外置存储":
                    switch (action) {
                    case "新增":
                        RemoteStorageForm form = (RemoteStorageForm) args[0];
                        objName = form.getName();
                        siteId = form.getSiteId();
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String remoteStorageId = (String) args[0];
                        objModel = remoteStorageService.getObjModel(remoteStorageId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "外置存储池":
                    switch (action) {
                    case "新增":
                        String remoteStorageId = (String) args[0];
                        RemoteStoragePoolForm form = (RemoteStoragePoolForm) args[1];
                        objName = form.getName();
                        objModel = remoteStorageService.getObjModel(remoteStorageId);
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        remoteStorageId = (String) args[0];
                        String poolId = (String) args[1];
                        objModel = remoteStoragePoolService.getObjModel(remoteStorageId, poolId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "PVC存储":
                    switch (action) {
                    case "新增":
                        PVCStorageForm form = (PVCStorageForm) args[0];
                        objName = form.getName();
                        siteId = form.getSiteId();
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String storageId = (String) args[0];
                        objModel = pvcStorageService.getObjModel(storageId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "主机":
                    switch (action) {
                    case "注册":
                        HostForm form = (HostForm) args[0];
                        objName = form.getIp();
                        objModel = clusterService.getObjModel(form.getClusterId());
                        break;
                    case "编辑":
                    case "入库":
                    case "出库":
                    case "启用":
                    case "停用":
                    case "注销":
                        String hostId = (String) args[0];
                        objModel = hostService.getObjModel(hostId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "业务系统":
                    switch (action) {
                    case "新增":
                        BusinessSystemForm form = (BusinessSystemForm) args[0];
                        objName = form.getName();
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String businessSystemId = (String) args[0];
                        objModel = businessSystemService.getObjModel(businessSystemId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "业务子系统":
                    switch (action) {
                    case "新增":
                        BusinessSubsystemForm form = (BusinessSubsystemForm) args[0];
                        objName = form.getName();
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String businessSubsystemId = (String) args[0];
                        objModel = businessSubsystemService.getObjModel(businessSubsystemId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "规模":
                    switch (action) {
                    case "新增":
                        ScaleForm form = (ScaleForm) args[0];
                        objName = scaleService.getScaleName(form.getCpuCnt(), form.getMemSize());
                        description = action + form.getType() + objType + "[" + objName + "]";
                        break;
                    case "启用":
                    case "停用":
                    case "删除":
                        String type = (String) args[0];
                        Double cpuCnt = (Double) args[1];
                        Double memSize = (Double) args[2];
                        objModel = scaleService.getObjModel(type, cpuCnt, memSize);
                        break;
                    default:
                        break;
                    }
                    break;
                case "工单配置":
                    switch (action) {
                    case "新增":
                    case "编辑":
                    case "删除":
                        String category = (String) args[0];
                        if (category.equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                            objType = "MySQL工单配置";
                        } else if (category.equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                            objType = "REDIS工单配置";
                        }
                        objName = objType;
                        description = action + objName;
                        break;
                    default:
                        break;
                    }
                    break;
                case "任务配置":
                    switch (action) {
                    case "编辑":
                        String taskObjType = (String) args[0];
                        String actionType = (String) args[1];
                        objModel = subtaskCfgService.getObjModel(taskObjType, actionType);
                        break;
                    default:
                        break;
                    }
                    break;
                case "工单":
                    switch (action) {
                    case "申请":
                        OrderGroupForm form = (OrderGroupForm) args[0];
                        if (form.getCategory().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                            objType = "MySQL工单";
                        } else if (form.getCategory().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                            objType = "Redis工单";
                        }
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        siteId = objModel.getSiteId();
                        break;
                    case "编辑":
                    case "审批":
                    case "执行":
                    case "删除":
                        String orderGroupId = (String) args[0];
                        objModel = orderGroupService.getObjModel(orderGroupId);
                        if (objModel != null) {
                            if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                                objType = "MySQL工单";
                            } else if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                                objType = "Redis工单";
                            }
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "服务":
                    String servGroupId = (String) args[0];
                    objModel = servGroupService.getObjModel(servGroupId);
                    if (objModel != null) {
                        if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                            objType = "MySQL服务";
                        } else if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                            objType = "CMHA服务";
                        } else if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                            objType = "Redis服务";
                        }
                    }
                    switch (action) {
                    case "启动":
                    case "停止":
                    case "删除":
                        break;
                    case "计算扩容":
                        ServScaleCpuMemForm scaleCpuMemForm = (ServScaleCpuMemForm) args[1];
                        if (objModel != null) {
                            description = action + "[" + objModel.getName()
                                    + getServTypeDisplay(scaleCpuMemForm.getType()) + "]";
                        }
                        break;
                    case "存储扩容":
                        ServScaleStorageForm scaleStorageForm = (ServScaleStorageForm) args[1];
                        if (objModel != null) {
                            description = action + "[" + objModel.getName()
                                    + getServTypeDisplay(scaleStorageForm.getType()) + "]";
                        }
                        break;
                    case "节点扩展":
                        ServArchUpForm archUpForm = (ServArchUpForm) args[1];
                        if (objModel != null) {
                            description = action + "[" + objModel.getName() + getServTypeDisplay(archUpForm.getType())
                                    + "]";
                        }
                        break;
                    case "升级":
                        ServImageForm imageForm = (ServImageForm) args[1];
                        if (objModel != null) {
                            description = action + "[" + objModel.getName() + getServTypeDisplay(imageForm.getType())
                                    + "]";
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "MySQL服务组":
                    switch (action) {
                    case "备份":
                        servGroupId = (String) args[0];
                        objModel = servGroupService.getObjModel(servGroupId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "Redis服务组":
                    switch (action) {
                    case "重置密码":
                        servGroupId = (String) args[0];
                        objModel = servGroupService.getObjModel(servGroupId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "参数配置":
                    switch (action) {
                    case "编辑":
                        servGroupId = (String) args[0];
                        objModel = servGroupService.getObjModel(servGroupId);
                        String type = (String) args[1];
                        ParamCfgForm paramCfgForm = (ParamCfgForm) args[2];
                        objName = paramCfgForm.getKey();
                        description = action + type + objType + "[" + objName + "]";
                        break;
                    default:
                        break;
                    }
                    break;
                case "单元":
                    switch (action) {
                    case "启动":
                    case "停止":
                    case "重建":
                    case "备份":
                    case "还原":
                    case "设置维护模式":
                    case "设置主":
                    case "强制重建初始化":
                        String unitId = (String) args[0];
                        objModel = unitService.getObjModel(unitId);
                        if (objModel != null) {
                            objType = objModel.getType() + objType;
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "MySQL服务组-库":
                case "CMHA服务组-库":
                    servGroupId = (String) args[0];
                    objModel = servGroupService.getObjModel(servGroupId);
                    switch (action) {
                    case "新增":
                        DBSchemaForm dbSchemaForm = (DBSchemaForm) args[1];
                        if (objModel != null) {
                            objName = dbSchemaForm.getName() + "(" + objModel.getName() + ")";
                        }
                        break;
                    case "删除":
                        servGroupId = (String) args[0];
                        String dbSchemaName = (String) args[1];
                        if (objModel != null) {
                            objName = dbSchemaName + "(" + objModel.getName() + ")";
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "MySQL服务组-用户":
                case "CMHA服务组-用户":
                    servGroupId = (String) args[0];
                    objModel = servGroupService.getObjModel(servGroupId);
                    switch (action) {
                    case "新增":
                        if (objType.equals("MySQL服务组-用户")) {
                            MysqlServGroupUserForm userForm = (MysqlServGroupUserForm) args[1];
                            if (objModel != null) {
                                objName = userForm.getUsername() + "[" + StringUtils.join(userForm.getWhiteIps(), ",")
                                        + "]" + "(" + objModel.getName() + ")";
                            }
                        } else if (objType.equals("CMHA服务组-用户")) {
                            CmhaServGroupUserForm userForm = (CmhaServGroupUserForm) args[1];
                            if (objModel != null) {
                                objName = userForm.getUsername() + "[" + StringUtils.join(userForm.getWhiteIps(), ",")
                                        + "]" + "(" + objModel.getName() + ")";
                            }
                        }
                        break;
                    case "编辑":
                    case "重置密码":
                    case "删除":
                        servGroupId = (String) args[0];
                        String dbUsername = (String) args[1];
                        String whiteIp = (String) args[2];
                        if (objModel != null) {
                            objName = dbUsername + "[" + whiteIp + "]" + "(" + objModel.getName() + ")";
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "备份策略":
                    switch (action) {
                    case "新增":
                        BackupStrategyForm form = (BackupStrategyForm) args[0];
                        objModel = servGroupService.getObjModel(form.getServGroupId());
                        if (objModel != null) {
                            siteId = objModel.getSiteId();
                            objName = objModel.getName() + "(" + form.getCronExpression() + ")";
                            String category = objModel.getType();
                            if (category.equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                                objType = "MySQL服务备份策略";
                            } else if (category.equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                                objType = "CMHA服务组备份策略";
                            }
                        }
                        break;
                    case "编辑":
                    case "启用":
                    case "停用":
                    case "删除":
                        String backupStrategyId = (String) args[0];
                        objModel = backupStrategyService.getObjModel(backupStrategyId);
                        if (objModel != null) {
                            String cronExpression = objModel.getName();
                            objModel = servGroupService.getObjModel(objModel.getType());
                            if (objModel != null) {
                                siteId = objModel.getSiteId();
                                objName = objModel.getName() + "(" + cronExpression + ")";
                                String category = objModel.getType();
                                if (category.equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                                    objType = "MySQL服务组备份策略";
                                } else if (category.equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                                    objType = "CMHA服务组备份策略";
                                }
                            }
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "备份文件":
                    switch (action) {
                    case "删除":
                        String backupFileId = (String) args[0];
                        objModel = backupFileService.getObjModel(backupFileId);
                        break;
                    default:
                        break;
                    }
                    break;
                }

                if (objModel != null) {
                    if (StringUtils.isBlank(objName)) {
                        objName = objModel.getName();
                    }
                    if (StringUtils.isBlank(siteId)) {
                        siteId = objModel.getSiteId();
                    }
                }

                if (StringUtils.isBlank(description)) {
                    description = action + objType + "[" + objName + "]";
                }

                if (StringUtils.isNotBlank(objName)) {
                    service.saveOperateLog(objType, objName, description, operator, siteId);
                }
            }

        } catch (Throwable e) {
            logger.error("保存操作记录异常：", e);
        } finally {
            try {
                object = pjp.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private String getServTypeDisplay(String type) {
        if (Consts.SERV_TYPE_MYSQL.equals(type) || Consts.SERV_TYPE_REDIS.equals(type)) {
            return "数据库";
        } else if (Consts.SERV_TYPE_PROXYSQL.equals(type)) {
            return "代理";
        } else if (Consts.SERV_TYPE_CMHA.equals(type) || Consts.SERV_TYPE_SENTINEL.equals(type)) {
            return "高可用";
        }
        return null;
    }

    private String getOperator() {
        if (session.getAttribute("user") != null) {
            UserDO userDO = (UserDO) session.getAttribute("user");
            return userDO.getUsername();
        }
        return null;
    }
}
