package com.itkun.common.task.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Setter
@Getter
public class CronJobDO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8085532974461780267L;

    /** 主键ID */
    private Long              id;

    /** BeanId */
    private String            beanId;

    /** 运行所在的IP地址 */
    private String            ipaddress;

    /** 运行cron表达式 */
    private String            cron;

    /** 开关状态：0关闭，1开启 */
    private Boolean           isOpen;

    /**任务名称**/
    private String            name;

    /**任务描述**/
    private String            description;

    /**作者**/
    private String            author;

    /**入参**/
    private String            inputParams;

    /**出参**/
    private String            outParams;

    /**上一次运行寄存**/
    private String            previousDatas;

    private String            env;

    private String            checkMsg;


    private Date lastRunBeginTime;
    private Date lastRunEndTime;
    private Long lastRunTime;

    //下一次运行开始时间
    private Date nextRunBeginTime;
}
