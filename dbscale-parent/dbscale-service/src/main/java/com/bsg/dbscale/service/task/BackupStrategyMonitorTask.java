package com.bsg.dbscale.service.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.BackupStrategyDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.query.BackupStrategyQuery;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.form.BackupForm;
import com.bsg.dbscale.service.service.BaseService;
import com.bsg.dbscale.service.service.CmhaServGroupService;
import com.bsg.dbscale.service.service.MysqlServGroupService;

@Service
public class BackupStrategyMonitorTask extends BaseService {

    @Autowired
    private MysqlServGroupService mysqlServGroupService;
    
    @Autowired
    private CmhaServGroupService cmhaServGroupService;

    public void doTask() {
        try {
            logger.info("定时任务：备份策略监控");
            // 备份策略监控频率（1分钟）
            int repeatInterval = 60 * 1000;

            Date date = systemDAO.getCurrentSqlDateTime();

            BackupStrategyQuery daoQuery = new BackupStrategyQuery();
            daoQuery.setEnabled(true);

            List<BackupStrategyDO> backupStrategyDOs = backupStrategyDAO.list(daoQuery);
            for (BackupStrategyDO backupStrategyDO : backupStrategyDOs) {
                try {
                    String quartzCronExpression = convertQuartzCronExpression(backupStrategyDO.getCronExpression());
                    CronExpression cronExpression = new CronExpression(quartzCronExpression);
                    // 下次备份时间
                    Date nextBackupDatetime = cronExpression.getTimeAfter(new Date(date.getTime() - repeatInterval));

                    long diff = date.getTime() - nextBackupDatetime.getTime();
                    if (diff >= 0 && diff <= repeatInterval) {
                        ServGroupDO servGroupDO = backupStrategyDO.getServGroup();
                        BackupForm backupForm = new BackupForm();
                        backupForm.setBackupStorageType(backupStrategyDO.getBackupStorageType());
                        backupForm.setType(backupStrategyDO.getType());
                        if (StringUtils.isNotBlank(backupStrategyDO.getTables())) {
                            backupForm.setTables(new ArrayList<>(
                                    Arrays.asList(StringUtils.split(backupStrategyDO.getTables(), ","))));
                        }

                        for (int i = 0; i < backupStrategyDO.getFileRetentionNum(); i++) {
                            nextBackupDatetime = cronExpression
                                    .getTimeAfter(new Date(nextBackupDatetime.getTime() + 1));
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(nextBackupDatetime);
                        calendar.set(Calendar.HOUR_OF_DAY, 23);
                        calendar.set(Calendar.MINUTE, 59);
                        calendar.set(Calendar.SECOND, 59);
                        Long expired = calendar.getTimeInMillis() / 1000;
                        backupForm.setExpired(expired);

                        if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                            mysqlServGroupService.backup(servGroupDO.getId(), backupForm, servGroupDO.getOwner());
                        } else if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                            cmhaServGroupService.backup(servGroupDO.getId(), backupForm, servGroupDO.getOwner());
                        }
                    }
                } catch (Exception e) {
                    logger.error("定时任务：备份策略监控异常：", e);
                }
            }
        } catch (Exception e) {
            logger.error("定时任务：备份策略监控异常：", e);
        }
    }

    public static void main(String[] args) throws ParseException {
        CronExpression cronExpression = new CronExpression("0 0 10 * * ?");
        Date nextBackupDatetime = cronExpression.getTimeAfter(new Date(new Date().getTime()));
        for (int i = 0; i < 1; i++) {
            nextBackupDatetime = cronExpression.getTimeAfter(new Date(nextBackupDatetime.getTime() + 1));
        }
        System.out.println(nextBackupDatetime);
    }

    private String convertQuartzCronExpression(String linuxCronExpression) {
        String cronExpression = "";
        String[] arr = linuxCronExpression.split(" ");
        if (arr.length != 5) {
            return null;
        }
        if (arr[4].equals("*")) {
            arr[4] = "?";
        } else {
            switch (arr[4]) {
            case "0":
                arr[4] = "1";
                break;
            case "1":
                arr[4] = "2";
                break;
            case "2":
                arr[4] = "3";
                break;
            case "3":
                arr[4] = "4";
                break;
            case "4":
                arr[4] = "5";
                break;
            case "5":
                arr[4] = "6";
                break;
            case "6":
                arr[4] = "7";
                break;
            case "7":
                arr[4] = "1";
                break;
            default:
                break;
            }
            if (arr[2].equals("*")) {
                arr[2] = "?";
            }
        }
        for (String str : arr) {
            cronExpression += " " + str;
        }
        cronExpression = 0 + cronExpression;
        return cronExpression;
    }

}
