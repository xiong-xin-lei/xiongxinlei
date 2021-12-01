package com.bsg.dbscale.cm.constant;

public class CmConsts {

    public static final String PERFORMANCE_HIGH = "high";

    public static final String PERFORMANCE_MEDIUM = "medium";

    public static final String PORT_NAME = "server";

    public static final String STORAGE_TYPE_VOLUMEPATH = "volumepath";

    public static final String STORAGE_TYPE_PVC = "pvc";

    public static final String STORAGE_LOCAL = "local";

    public static final String STORAGE_REMOTE = "remote";

    public static final String VOLUME_DATA = "data";

    public static final String VOLUME_LOG = "log";

    public static final String STATE_PASSING = "passing";

    public static final String STATE_CRITICAL = "critical";

    public static final String STATE_ONLINE = "online";

    public static final String STATE_OFFLINE = "offline";

    public static final String HOST_PHASE_READY = "ready";

    public static final String HOST_PHASE_FAILED = "failed";

    public static final String SERV_PHASE_READY = "ready";

    public static final String SERV_PHASE_CREATE_FAILED = "create_failed";

    public static final String SERV_PHASE_COMPOSE_FAILED = "compose_failed";

    public static final String BACKUP_FILE_RUNNING = "Running";

    public static final String BACKUP_FILE_COMPLETE = "Complete";

    public static final String BACKUP_FILE_FAILED = "Failed";

    public static final String BACKUP_FILE_DELETING = "Deleting";

    public static final String BACKUP_FILE_DELETEFAILED = "DeleteFailed";

    public static final String RESTORE_COMPLETE = "Complete";

    public static final String RESTORE_FAILED = "Failed";

    public static final String ROLE_MASTER = "master";

    public static final String ROLE_SLAVE = "slave";

    public static final String REPL_MODE_SINGLE = "single";

    public static final String REPL_MODE_SEMI_SYNC = "semi_sync";

    public static final String REPL_MODE_ASYNC = "async";

    public static final String REPLICATION_STATE_YES = "Yes";

    public static final String REPLICATION_STATE_NO = "No";

    public static final String BACKUP_STORAGE_TYPE_NFS = "nfs";

    public static final String PWD_TYPE_NATIVE = "native";

    public static final String POD_STATE_RUNNING = "Running";
    
    public static final String LOADBALANCER_NONE = "none";
}
