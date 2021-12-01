package com.bsg.dbscale.service.constant;

/**
 * 字典项常量类
 * 
 * @author HCK
 *
 */
public class DictConsts {

    /******************************* 数据范围 STA ********************************/
    /**
     * 数据范围:仅本人数据
     */
    public static final String DATA_SCOPE_ONESELF = "oneself";

    /**
     * 数据范围:本组所有数据
     */
    public static final String DATA_SCOPE_GROUP = "group";

    /**
     * 数据范围:所有数据
     */
    public static final String DATA_SCOPE_ALL = "all";

    /******************************* 数据范围 END ********************************/

    /******************************* 认证方式 STA ********************************/
    /**
     * 认证方式:本地认证
     */
    public static final String AUTH_TYPE_NATIVE = "native";

    /**
     * 认证方式:SSO认证
     */
    public static final String AUTH_TYPE_SSO = "sso";
    /******************************* 认证方式 END ********************************/

    public static final String DISK_TYPE_LOCAL_HDD = "local_hdd";

    public static final String DISK_TYPE_LOCAL_SSD = "local_ssd";
    
    public static final String DISK_TYPE_REMOTE_STORAGE_HIGH = "remote_storage_high";
    
    public static final String DISK_TYPE_REMOTE_STORAGE_MEDIUM = "remote_storage_medium";

    public static final String OBJ_TYPE_HOST = "host";

    public static final String OBJ_TYPE_SERV_GROUP = "servGroup";

    public static final String OBJ_TYPE_SERV = "serv";

    public static final String OBJ_TYPE_UNIT = "unit";

    public static final String ACTION_TYPE_IN = "in";

    public static final String ACTION_TYPE_OUT = "out";

    public static final String ACTION_TYPE_CREATE = "create";

    public static final String ACTION_TYPE_LINK = "link";
    
    public static final String ACTION_TYPE_SAVE_LOADBALANCER = "save_loadbalancer";
    
    public static final String ACTION_TYPE_REMOVE_LOADBALANCER = "remove_loadbalancer";

    public static final String ACTION_TYPE_ADD_BACKUP_STRATEGY = "add_backup_strategy";

    public static final String ACTION_TYPE_START = "start";

    public static final String ACTION_TYPE_STOP = "stop";

    public static final String ACTION_TYPE_IMAGE_UPDATE = "image_update";

    public static final String ACTION_TYPE_SCALE_UP_CPUMEM = "scale_up_cpumem";

    public static final String ACTION_TYPE_SCALE_UP_STORAGE = "scale_up_storage";
    
    public static final String ACTION_TYPE_ARCH_UP = "arch_up";

    public static final String ACTION_TYPE_REBUILD = "rebuild";
    
    public static final String ACTION_TYPE_FORCE_REBUILD = "force_rebuild";
    
    public static final String ACTION_TYPE_REBUILD_INIT = "rebuild_init";
    
    public static final String ACTION_TYPE_UPDATE_ROLE = "update_role";

    public static final String ACTION_TYPE_BACKUP = "backup";
    
    public static final String ACTION_TYPE_BACKUP_MASTER = "backup_master";

    public static final String ACTION_TYPE_RESTORE = "restore";

    public static final String ACTION_TYPE_REMOVE = "remove";
    
    public static final String ACTION_TYPE_SET_MAINTENANCE = "set_maintenance";
    
    public static final String ACTION_TYPE_CANCEL_MAINTENANCE = "cancel_maintenance";
    
    public static final String TASK_STATE_READY = "ready";

    public static final String TASK_STATE_RUNNING = "running";

    public static final String TASK_STATE_SUCCESS = "success";

    public static final String TASK_STATE_FAILED = "failed";

    public static final String TASK_STATE_TIMEOUT = "timeout";

    public static final String TASK_STATE_UNKNOWN = "unknown";

    /**
     * 工单类型：新增
     */
    public static final String ORDER_CREATE_TYPE_NEW = "new";

    /**
     * 工单类型：计算扩容
     */
    public static final String ORDER_CREATE_TYPE_SCALE_UP_CPUMEM = "scale_up_cpumem";

    /**
     * 工单类型：存储扩容
     */
    public static final String ORDER_CREATE_TYPE_SCALE_UP_STORAGE = "scale_up_storage";
    
    /**
     * 工单类型：节点扩展
     */
    public static final String ORDER_CREATE_TYPE_ARCH_UP = "arch_up";

    /**
     * 工单类型：升级
     */
    public static final String ORDER_CREATE_TYPE_IMAGE_UPDATE = "image_update";

    /**
     * 工单类型：删除
     */
    public static final String ORDER_CREATE_TYPE_DELETE = "delete";

    /******************************** 工单状态 STA ********************************/
    /**
     * 工单状态：未审批
     */
    public static final String ORDER_STATE_UNAPPROVED = "unapproved";

    /**
     * 工单状态：审批通过
     */
    public static final String ORDER_STATE_APPROVED = "approved";

    /**
     * 工单状态：审批拒绝
     */
    public static final String ORDER_STATE_UNAPPROVE = "unapprove";

    /**
     * 工单状态：执行中
     */
    public static final String ORDER_STATE_EXECUTING = "executing";

    /**
     * 工单状态：执行成功
     */
    public static final String ORDER_STATE_SUCCESS = "success";

    /**
     * 工单状态：执行失败
     */
    public static final String ORDER_STATE_FAILED = "failed";

    /**
     * 状态：passing
     */
    public static final String STATE_PASSING = "passing";

    /**
     * 状态：warning
     */
    public static final String STATE_WARNNING = "warning";

    /**
     * 状态：critical
     */
    public static final String STATE_CRITICAL = "critical";

    /**
     * 状态：unknown
     */
    public static final String STATE_UNKNOWN = "unknown";

    public static final String REPLICATION_STATE_UNKNOWN = "unknown";
    
    public static final String REPLICATION_ROLE_MASTER = "master";
    
    public static final String REPLICATION_ROLE_SLAVE = "slave";
    
    public static final String BACKUP_TYPE_FULL = "full"; 

}
