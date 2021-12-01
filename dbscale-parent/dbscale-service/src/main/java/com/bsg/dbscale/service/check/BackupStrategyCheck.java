package com.bsg.dbscale.service.check;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.service.form.BackupStrategyForm;

@Service
public class BackupStrategyCheck extends BaseCheck {

    public CheckResult checkSave(BackupStrategyForm backupStrategyForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(backupStrategyForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(backupStrategyForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String backupStrategyId, BackupStrategyForm backupStrategyForm) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(backupStrategyForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(backupStrategyId, backupStrategyForm);

        return checkResult;
    }

    public CheckResult checkRemove(String backupStrategyId) {
        return CheckResult.success();
    }

    public CheckResult checkEnabled(String backupStrategyId, boolean enabled) {
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(BackupStrategyForm backupStrategyForm) {
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(BackupStrategyForm backupStrategyForm) throws Exception {
        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(BackupStrategyForm backupStrategyForm) {
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String backupStrategyId, BackupStrategyForm backupStrategyForm) {
        return CheckResult.success();
    }
}
