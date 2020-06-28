package com.zaroumia.batch;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaroumia.batch.validators.MyJobParametersValidator;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Bean(name = "defaultJobParametersValidator")
    public JobParametersValidator defaultJobParametersValidator() {
	DefaultJobParametersValidator bean = new DefaultJobParametersValidator();
	bean.setRequiredKeys(new String[] { "formateursFile", "formationsFile", "seancesFile" });
	return bean;
    }

    @Bean(name = "myJobParametersValidator")
    public JobParametersValidator myJobParametersValidator() {
	return new MyJobParametersValidator();
    }

    @Bean(name = "compositeJobParametersValidator")
    public JobParametersValidator compositeJobParametersValidator(
	    @Qualifier("defaultJobParametersValidator") JobParametersValidator defaultJobParametersValidator,
	    @Qualifier("myJobParametersValidator") JobParametersValidator myJobParametersValidator) {
	CompositeJobParametersValidator bean = new CompositeJobParametersValidator();
	bean.setValidators(Arrays.asList(defaultJobParametersValidator, myJobParametersValidator));
	return bean;
    }

    @Bean
    public Step step1() {
	return stepBuilderFactory.get("step1").tasklet(new Tasklet() {

	    @Override
	    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("Hello world");
		return RepeatStatus.FINISHED;
	    }
	}).build();
    }

    @Bean
    public Job job(Step step1,
	    @Qualifier("compositeJobParametersValidator") JobParametersValidator compositeJobParametersValidator) {
	return jobBuilderFactory.get("formations-batch").start(step1).validator(compositeJobParametersValidator)
		.build();
    }
}
