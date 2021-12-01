package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.DBSchemaForm;
import com.bsg.dbscale.service.form.MysqlServGroupUserForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;

@Service
public class MysqlServGroupCheck extends ServGroupCheck {

    public CheckResult checkStart(ServGroupDO servGroupDO) {
        return checkStart(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkStop(ServGroupDO servGroupDO) {
        return checkStop(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkImageUpdate(ServGroupDO servGroupDO, ServImageForm imageForm) {
        return checkImageUpdate(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL, imageForm);
    }

    public CheckResult checkScaleUpCpuMem(ServGroupDO servGroupDO, ServScaleCpuMemForm scaleCpuMemForm) {
        return checkScaleUpCpuMem(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL, scaleCpuMemForm);
    }

    public CheckResult checkScaleUpStorage(ServGroupDO servGroupDO, ServScaleStorageForm scaleStorageForm) {
        return checkScaleUpStorage(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL, scaleStorageForm);
    }

    public CheckResult checkArchUp(ServGroupDO servGroupDO, ServArchUpForm archUpForm) {
        return checkArchUp(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL, archUpForm);
    }

    public CheckResult checkRemove(ServGroupDO servGroupDO) {
        return checkRemove(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkBackup(ServGroupDO servGroupDO) {
        return checkServGroupCommon(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkSaveDBSchema(ServGroupDO servGroupDO, DBSchemaForm DBSchemaForm) {
        // Non-logical check for save
        CheckResult checkResult = checkSaveDBSchemaNonLogic(DBSchemaForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveDBSchemaLogic(servGroupDO, DBSchemaForm);

        return checkResult;
    }

    private CheckResult checkSaveDBSchemaNonLogic(DBSchemaForm DBSchemaForm) {
        if (StringUtils.isBlank(DBSchemaForm.getName())) {
            String msg = "库名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(DBSchemaForm.getCharacterSet())) {
            String msg = "字符集不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveDBSchemaLogic(ServGroupDO servGroupDO, DBSchemaForm dbSchemaForm) {
        CheckResult checkResult = checkCommon(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        DictDO dictDO = dictDAO.get(DictTypeConsts.CHARACTERSET, dbSchemaForm.getCharacterSet());
        if (dictDO == null) {
            String msg = "该字符集不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkRemoveDBSchema(ServGroupDO servGroupDO, String dbSchemaName) throws Exception {
        return checkCommon(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkSaveUser(ServGroupDO servGroupDO, MysqlServGroupUserForm mysqlServGroupUserForm)
            throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveUserNonLogic(mysqlServGroupUserForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveUserLogic(servGroupDO, mysqlServGroupUserForm);

        return checkResult;
    }

    private CheckResult checkSaveUserNonLogic(MysqlServGroupUserForm mysqlServGroupUserForm) {
        return CheckResult.success();
    }

    private CheckResult checkSaveUserLogic(ServGroupDO servGroupDO, MysqlServGroupUserForm mysqlServGroupUserForm)
            throws Exception {
        return checkCommon(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkUpdateUser(ServGroupDO servGroupDO, String username, String whiteIp,
            MysqlServGroupUserForm DBUserForm) throws Exception {
        return checkCommon(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkResetUserPwd(ServGroupDO servGroupDO, String username, String whiteIp) throws Exception {
        return checkCommon(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

    public CheckResult checkRemoveUser(ServGroupDO servGroupDO, String username, String whiteIp) throws Exception {
        return checkCommon(servGroupDO, Consts.SERV_GROUP_TYPE_MYSQL);
    }

}
