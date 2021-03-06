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
 * ???????????????
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
                case "??????":
                    switch (action) {
                    case "??????":
                        LoginForm loginForm = (LoginForm) args[0];
                        objName = loginForm.getUsername();
                        description = "????????????" + "[" + objName + "]";
                        operator = loginForm.getUsername();
                        break;
                    case "??????":
                        UserForm form = (UserForm) args[0];
                        objName = form.getUsername();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "????????????":
                        String username = (String) args[0];
                        objModel = userService.getObjModel(username);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        GroupForm form = (GroupForm) args[0];
                        objName = form.getName();
                        break;
                    case "??????":
                    case "??????":
                        String groupId = (String) args[0];
                        objModel = groupService.getObjModel(groupId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "????????????":
                    switch (action) {
                    case "??????":
                        String groupId = (String) args[0];
                        objModel = groupService.getObjModel(groupId);
                        @SuppressWarnings("unchecked")
                        List<String> usernames = (List<String>) args[1];
                        objName = objModel.getName() + "(" + usernames + ")";
                        break;
                    case "??????":
                        groupId = (String) args[0];
                        objModel = groupService.getObjModel(groupId);
                        String username = (String) args[1];
                        objName = objModel.getName() + "(" + username + ")";
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        RoleForm form = (RoleForm) args[0];
                        objName = form.getName();
                        break;
                    case "??????":
                    case "??????":
                    case "????????????":
                        String roleId = (String) args[0];
                        objModel = roleService.getObjModel(roleId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        SiteForm form = (SiteForm) args[0];
                        objName = form.getName();
                        break;
                    case "??????":
                    case "??????":
                        siteId = (String) args[0];
                        objModel = siteService.getObjModel(siteId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "?????????":
                    switch (action) {
                    case "??????":
                        BusinessAreaForm form = (BusinessAreaForm) args[0];
                        objName = form.getName();
                        siteId = form.getSiteId();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String businessAreaId = (String) args[0];
                        objModel = businessAreaService.getObjModel(businessAreaId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "NFS":
                    switch (action) {
                    case "??????":
                        NfsForm form = (NfsForm) args[0];
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                        String nfsId = (String) args[0];
                        objModel = nfsService.getObjModel(nfsId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        ClusterForm form = (ClusterForm) args[0];
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String clusterId = (String) args[0];
                        objModel = clusterService.getObjModel(clusterId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        NetworkForm form = (NetworkForm) args[0];
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String networkId = (String) args[0];
                        objModel = networkService.getObjModel(networkId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        ImageForm form = (ImageForm) args[0];
                        objName = form.getType() + form.getMajor() + "." + form.getMinor() + "." + form.getPatch() + "."
                                + form.getBuild();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                        String imageId = (String) args[0];
                        objModel = imageService.getObjModel(imageId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "????????????":
                    switch (action) {
                    case "??????":
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
                case "????????????":
                    switch (action) {
                    case "??????":
                        RemoteStorageForm form = (RemoteStorageForm) args[0];
                        objName = form.getName();
                        siteId = form.getSiteId();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String remoteStorageId = (String) args[0];
                        objModel = remoteStorageService.getObjModel(remoteStorageId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "???????????????":
                    switch (action) {
                    case "??????":
                        String remoteStorageId = (String) args[0];
                        RemoteStoragePoolForm form = (RemoteStoragePoolForm) args[1];
                        objName = form.getName();
                        objModel = remoteStorageService.getObjModel(remoteStorageId);
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        remoteStorageId = (String) args[0];
                        String poolId = (String) args[1];
                        objModel = remoteStoragePoolService.getObjModel(remoteStorageId, poolId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "PVC??????":
                    switch (action) {
                    case "??????":
                        PVCStorageForm form = (PVCStorageForm) args[0];
                        objName = form.getName();
                        siteId = form.getSiteId();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String storageId = (String) args[0];
                        objModel = pvcStorageService.getObjModel(storageId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        HostForm form = (HostForm) args[0];
                        objName = form.getIp();
                        objModel = clusterService.getObjModel(form.getClusterId());
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String hostId = (String) args[0];
                        objModel = hostService.getObjModel(hostId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "????????????":
                    switch (action) {
                    case "??????":
                        BusinessSystemForm form = (BusinessSystemForm) args[0];
                        objName = form.getName();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String businessSystemId = (String) args[0];
                        objModel = businessSystemService.getObjModel(businessSystemId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "???????????????":
                    switch (action) {
                    case "??????":
                        BusinessSubsystemForm form = (BusinessSubsystemForm) args[0];
                        objName = form.getName();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String businessSubsystemId = (String) args[0];
                        objModel = businessSubsystemService.getObjModel(businessSubsystemId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        ScaleForm form = (ScaleForm) args[0];
                        objName = scaleService.getScaleName(form.getCpuCnt(), form.getMemSize());
                        description = action + form.getType() + objType + "[" + objName + "]";
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                        String type = (String) args[0];
                        Double cpuCnt = (Double) args[1];
                        Double memSize = (Double) args[2];
                        objModel = scaleService.getObjModel(type, cpuCnt, memSize);
                        break;
                    default:
                        break;
                    }
                    break;
                case "????????????":
                    switch (action) {
                    case "??????":
                    case "??????":
                    case "??????":
                        String category = (String) args[0];
                        if (category.equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                            objType = "MySQL????????????";
                        } else if (category.equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                            objType = "REDIS????????????";
                        }
                        objName = objType;
                        description = action + objName;
                        break;
                    default:
                        break;
                    }
                    break;
                case "????????????":
                    switch (action) {
                    case "??????":
                        String taskObjType = (String) args[0];
                        String actionType = (String) args[1];
                        objModel = subtaskCfgService.getObjModel(taskObjType, actionType);
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    switch (action) {
                    case "??????":
                        OrderGroupForm form = (OrderGroupForm) args[0];
                        if (form.getCategory().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                            objType = "MySQL??????";
                        } else if (form.getCategory().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                            objType = "Redis??????";
                        }
                        objName = form.getName();
                        objModel = businessAreaService.getObjModel(form.getBusinessAreaId());
                        siteId = objModel.getSiteId();
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                        String orderGroupId = (String) args[0];
                        objModel = orderGroupService.getObjModel(orderGroupId);
                        if (objModel != null) {
                            if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                                objType = "MySQL??????";
                            } else if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                                objType = "Redis??????";
                            }
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "??????":
                    String servGroupId = (String) args[0];
                    objModel = servGroupService.getObjModel(servGroupId);
                    if (objModel != null) {
                        if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                            objType = "MySQL??????";
                        } else if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                            objType = "CMHA??????";
                        } else if (objModel.getType().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                            objType = "Redis??????";
                        }
                    }
                    switch (action) {
                    case "??????":
                    case "??????":
                    case "??????":
                        break;
                    case "????????????":
                        ServScaleCpuMemForm scaleCpuMemForm = (ServScaleCpuMemForm) args[1];
                        if (objModel != null) {
                            description = action + "[" + objModel.getName()
                                    + getServTypeDisplay(scaleCpuMemForm.getType()) + "]";
                        }
                        break;
                    case "????????????":
                        ServScaleStorageForm scaleStorageForm = (ServScaleStorageForm) args[1];
                        if (objModel != null) {
                            description = action + "[" + objModel.getName()
                                    + getServTypeDisplay(scaleStorageForm.getType()) + "]";
                        }
                        break;
                    case "????????????":
                        ServArchUpForm archUpForm = (ServArchUpForm) args[1];
                        if (objModel != null) {
                            description = action + "[" + objModel.getName() + getServTypeDisplay(archUpForm.getType())
                                    + "]";
                        }
                        break;
                    case "??????":
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
                case "MySQL?????????":
                    switch (action) {
                    case "??????":
                        servGroupId = (String) args[0];
                        objModel = servGroupService.getObjModel(servGroupId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "Redis?????????":
                    switch (action) {
                    case "????????????":
                        servGroupId = (String) args[0];
                        objModel = servGroupService.getObjModel(servGroupId);
                        break;
                    default:
                        break;
                    }
                    break;
                case "????????????":
                    switch (action) {
                    case "??????":
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
                case "??????":
                    switch (action) {
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????????????????":
                    case "?????????":
                    case "?????????????????????":
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
                case "MySQL?????????-???":
                case "CMHA?????????-???":
                    servGroupId = (String) args[0];
                    objModel = servGroupService.getObjModel(servGroupId);
                    switch (action) {
                    case "??????":
                        DBSchemaForm dbSchemaForm = (DBSchemaForm) args[1];
                        if (objModel != null) {
                            objName = dbSchemaForm.getName() + "(" + objModel.getName() + ")";
                        }
                        break;
                    case "??????":
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
                case "MySQL?????????-??????":
                case "CMHA?????????-??????":
                    servGroupId = (String) args[0];
                    objModel = servGroupService.getObjModel(servGroupId);
                    switch (action) {
                    case "??????":
                        if (objType.equals("MySQL?????????-??????")) {
                            MysqlServGroupUserForm userForm = (MysqlServGroupUserForm) args[1];
                            if (objModel != null) {
                                objName = userForm.getUsername() + "[" + StringUtils.join(userForm.getWhiteIps(), ",")
                                        + "]" + "(" + objModel.getName() + ")";
                            }
                        } else if (objType.equals("CMHA?????????-??????")) {
                            CmhaServGroupUserForm userForm = (CmhaServGroupUserForm) args[1];
                            if (objModel != null) {
                                objName = userForm.getUsername() + "[" + StringUtils.join(userForm.getWhiteIps(), ",")
                                        + "]" + "(" + objModel.getName() + ")";
                            }
                        }
                        break;
                    case "??????":
                    case "????????????":
                    case "??????":
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
                case "????????????":
                    switch (action) {
                    case "??????":
                        BackupStrategyForm form = (BackupStrategyForm) args[0];
                        objModel = servGroupService.getObjModel(form.getServGroupId());
                        if (objModel != null) {
                            siteId = objModel.getSiteId();
                            objName = objModel.getName() + "(" + form.getCronExpression() + ")";
                            String category = objModel.getType();
                            if (category.equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                                objType = "MySQL??????????????????";
                            } else if (category.equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                                objType = "CMHA?????????????????????";
                            }
                        }
                        break;
                    case "??????":
                    case "??????":
                    case "??????":
                    case "??????":
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
                                    objType = "MySQL?????????????????????";
                                } else if (category.equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                                    objType = "CMHA?????????????????????";
                                }
                            }
                        }
                        break;
                    default:
                        break;
                    }
                    break;
                case "????????????":
                    switch (action) {
                    case "??????":
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
            logger.error("???????????????????????????", e);
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
            return "?????????";
        } else if (Consts.SERV_TYPE_PROXYSQL.equals(type)) {
            return "??????";
        } else if (Consts.SERV_TYPE_CMHA.equals(type) || Consts.SERV_TYPE_SENTINEL.equals(type)) {
            return "?????????";
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
