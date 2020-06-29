package com.zaroumia.batch;

import java.util.List;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.zaroumia.batch.domaine.Formateur;

@Configuration
public class ChargementFormateursStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name = "formateurItemReader")
    @StepScope
    public FlatFileItemReader<Formateur> formateurItemReader(
	    @Value("#{jobParameters['formateursFile']}") final Resource inputFile) {
	return new FlatFileItemReaderBuilder<Formateur>().name("FormateurItemReader").resource(inputFile).delimited()
		.delimiter(";").names(new String[] { "id", "nom", "prenom", "adresseEmail" })
		.targetType(Formateur.class).build();
    }

    @Bean(name = "formateurItemWriter")
    public ItemWriter<Formateur> formateurItemWriter() {
	return new ItemWriter<Formateur>() {
	    @Override
	    public void write(List<? extends Formateur> items) throws Exception {
		items.forEach(System.out::println);
	    }
	};
    }

    @Bean(name = "chargementFormateursStep")
    public Step chargementFormateursStep(@Qualifier("formateurItemReader") FlatFileItemReader formateurItemReader,
	    @Qualifier("formateurItemWriter") ItemWriter formateurItemWriter) {
	return stepBuilderFactory.get("chargementFormateursStep").<Formateur, Formateur>chunk(10)
		.reader(formateurItemReader).writer(formateurItemWriter).build();
    }
}
