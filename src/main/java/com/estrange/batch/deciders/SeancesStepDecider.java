package com.estrange.batch.deciders;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.util.StringUtils;

public class SeancesStepDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution,
            org.springframework.batch.core.StepExecution stepExecution) {
        if (StringUtils.endsWithIgnoreCase(jobExecution.getJobParameters().getString("seancesFile"),
                "txt")) {
            return new FlowExecutionStatus("txt");
        } else {
            return new FlowExecutionStatus("csv");
        }
    }
}
