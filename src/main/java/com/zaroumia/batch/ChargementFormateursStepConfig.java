package com.zaroumia.batch;

import static com.zaroumia.batch.mappers.FormateurItemPreparedStatementSetter.FORMATEURS_INSERT_QUERY;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.zaroumia.batch.domaine.Formateur;
import com.zaroumia.batch.listeners.ChargementFormateursStepListener;
import com.zaroumia.batch.mappers.FormateurItemPreparedStatementSetter;

@Configuration
public class ChargementFormateursStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource datasource;

    @Bean(name = "formateurItemPreparedStatementSetter")
    public ItemPreparedStatementSetter<Formateur> formateurItemPreparedStatementSetter() {
	return new FormateurItemPreparedStatementSetter();
    }

    @Bean(name = "chargementFormateursStepListener")
    public StepExecutionListener chargementFormateursStepListener() {
	return new ChargementFormateursStepListener();
    }

    @Bean(name = "formateurItemReader")
    @StepScope
    public FlatFileItemReader<Formateur> formateurItemReader(
	    @Value("#{jobParameters['formateursFile']}") final Resource inputFile) {
	return new FlatFileItemReaderBuilder<Formateur>().name("FormateurItemReader").resource(inputFile).delimited()
		.delimiter(";").names(new String[] { "id", "nom", "prenom", "adresseEmail" })
		.targetType(Formateur.class).build();
    }

    @Bean(name = "formateurItemWriter")
    public JdbcBatchItemWriter<Formateur> formateurItemWriter(
	    @Qualifier("formateurItemPreparedStatementSetter") ItemPreparedStatementSetter<Formateur> formateurItemPreparedStatementSetter) {
	return new JdbcBatchItemWriterBuilder<Formateur>().dataSource(datasource).sql(FORMATEURS_INSERT_QUERY)
		.itemPreparedStatementSetter(formateurItemPreparedStatementSetter).build();
    }

    @Bean(name = "chargementFormateursStep")
    public Step chargementFormateursStep(
	    @Qualifier("formateurItemReader") FlatFileItemReader<Formateur> formateurItemReader,
	    @Qualifier("formateurItemWriter") ItemWriter<Formateur> formateurItemWriter,
	    @Qualifier("chargementFormateursStepListener") StepExecutionListener chargementFormateursStepListener) {
	return stepBuilderFactory.get("chargementFormateursStep").<Formateur, Formateur>chunk(10)
		.reader(formateurItemReader).writer(formateurItemWriter).listener(chargementFormateursStepListener)
		.build();
    }
}
