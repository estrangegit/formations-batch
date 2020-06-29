package com.zaroumia.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.zaroumia.batch.domaine.Formation;

@Configuration
public class ChargementFormationsStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name = "formationMarshaller")
    public Jaxb2Marshaller formationMarshaller() {
	Jaxb2Marshaller bean = new Jaxb2Marshaller();
	bean.setClassesToBeBound(Formation.class);
	return bean;
    }

    @Bean(name = "formationItemReader")
    @StepScope
    public StaxEventItemReader<Formation> formationItemReader(
	    @Value("#{jobParameters['formationsFile']}") final Resource inputFile,
	    @Qualifier("formationMarshaller") Jaxb2Marshaller formationMarshaller) {
	return new StaxEventItemReaderBuilder<Formation>().name("formationItemReader").resource(inputFile)
		.addFragmentRootElements("formation").unmarshaller(formationMarshaller).build();
    }

    @Bean(name = "formationItemWriter")
    public ItemWriter<Formation> formationItemWriter() {
	return (items) -> items.forEach(System.out::println);
    }

    @Bean(name = "chargementFormationsStep")
    public Step chargementFormationsStep(
	    @Qualifier("formationItemReader") StaxEventItemReader<Formation> formationItemReader,
	    @Qualifier("formationItemWriter") ItemWriter<Formation> formationItemWriter) {
	return stepBuilderFactory.get("chargementFormationsStep").<Formation, Formation>chunk(10)
		.reader(formationItemReader).writer(formationItemWriter).build();
    }
}
