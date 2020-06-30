package com.zaroumia.batch.deciders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.util.StringUtils;

public class SeancesStepDecider implements JobExecutionDecider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeancesStepDecider.class);

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
