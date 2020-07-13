package com.itkun.common.task.Scheduler;

import com.itkun.common.task.domain.CronJobDO;
import it.sauronsoftware.cron4j.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class CronJobInstance implements ApplicationListener<ContextRefreshedEvent> {
    private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private static Map<Class<?>, Scheduler> all_jobs = new ConcurrentHashMap<Class<?>, Scheduler>();

    private static Map<Class<?>, String> tid_job = new ConcurrentHashMap<>();
    @Autowired
    private Map<String, AbstractJob> beanMap;

    CronJobDO cronJobDO = new CronJobDO();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Thread daemon = new Thread() {
            public void run() {
                while (true) {
                    exec.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // TODO 查询数据库,优先使用NOsql
                                cronJobDO.setCron("* * * * *");
                                AbstractJob job = beanMap.get("newMockJob");

                                Scheduler scheduler = all_jobs.get(job.getClass());
                                if (scheduler == null) {
                                    scheduler = new Scheduler();

                                    String tid = scheduler.schedule(cronJobDO.getCron(), job);
                                    scheduler.start();
                                    all_jobs.put(job.getClass(), scheduler);
                                    tid_job.put(job.getClass(), tid);
                                } else {
                                    String tid = tid_job.get(job.getClass());
                                    String oldCron = scheduler.getSchedulingPattern(tid).toString();
                                    if (!oldCron.equalsIgnoreCase(cronJobDO.getCron())) {
                                        scheduler.reschedule(tid, cronJobDO.getCron());
                                    }

                                }

                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        daemon.setDaemon(true);
        daemon.start();
    }
}
