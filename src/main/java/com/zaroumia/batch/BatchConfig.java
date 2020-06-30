package com.zaroumia.batch;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaroumia.batch.validators.MyJobParametersValidator;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean(name = "defaultJobParametersValidator")
    public JobParametersValidator defaultJobParametersValidator() {
	DefaultJobParametersValidator bean = new DefaultJobParametersValidator();
	bean.setRequiredKeys(new String[] { "formateursFile", "formationsFile", "seancesFile" });
	bean.setOptionalKeys(new String[] { "run.id" });
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
    public Job job(@Qualifier("chargementFormateursStep") Step chargementFormateursStep,
	    @Qualifier("chargementFormationsStep") Step chargementFormationsStep,
	    @Qualifier("chargementSeancesCsvStep") Step chargementSeancesCsvStep,
	    @Qualifier("chargementSeancesTxtStep") Step chargementSeancesTxtStep,
	    @Qualifier("compositeJobParametersValidator") JobParametersValidator compositeJobParametersValidator) {
	return jobBuilderFactory.get("formations-batch").start(chargementFormateursStep).next(chargementFormationsStep)
		.next(chargementSeancesTxtStep).validator(compositeJobParametersValidator)
		.incrementer(new RunIdIncrementer()).build();
    }
}
