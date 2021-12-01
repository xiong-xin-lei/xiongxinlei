package com.bsg.dbscale.service.task;

import java.io.Serializable;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import com.bsg.dbscale.dao.dao.CrontabCfgDAO;
import com.bsg.dbscale.dao.domain.CrontabCfgDO;

public class InitCronTrigger extends CronTriggerFactoryBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private InitCronTrigger(CrontabCfgDAO crontabCfgDAO, String type) {
        CrontabCfgDO configDO = crontabCfgDAO.get(type);
        if (configDO != null) {
            setCronExpression(configDO.getCrontab());
        }
    }

}
