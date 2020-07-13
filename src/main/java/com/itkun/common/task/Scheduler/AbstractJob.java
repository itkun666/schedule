package com.itkun.common.task.Scheduler;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public abstract class AbstractJob extends Task implements Job {

    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
       this.run();

    }
}
