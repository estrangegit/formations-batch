package com.zaroumia.batch;

import static com.zaroumia.batch.mappers.SeanceItemPreparedStatementSetter.SEANCES_INSERT_QUERY;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;

import com.zaroumia.batch.domaine.Seance;
import com.zaroumia.batch.listeners.ChargementSeancesStepListener;
import com.zaroumia.batch.mappers.SeanceItemPreparedStatementSetter;

@Configuration
public class ChargementSeancesStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource datasource;

    @Bean(name = "seanceItemPreparedStatementSetter")
    public ItemPreparedStatementSetter<Seance> seanceItemPreparedStatementSetter() {
	return new SeanceItemPreparedStatementSetter();
    }

    @Bean(name = "chargementSeancesStepListener")
    public StepExecutionListener chargementSeancesStepListener() {
	return new ChargementSeancesStepListener();
    }

    @Bean(name = "stringToLocalDateConversionService")
    public ConversionService stringToLocalDateConversionService() {
	DefaultConversionService dcs = new DefaultConversionService();
	DefaultConversionService.addDefaultConverters(dcs);
	dcs.addConverter(new Converter<String, LocalDate>() {
	    @Override
	    public LocalDate convert(String input) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
		return LocalDate.parse(input, dtf);
	    }
	});
	return dcs;
    }

    @Bean(name = "seanceFieldSetMapper")
    public FieldSetMapper<Seance> seanceFieldSetMapper(
	    @Qualifier("stringToLocalDateConversionService") ConversionService stringToLocalDateConversionService) {
	BeanWrapperFieldSetMapper<Seance> bean = new BeanWrapperFieldSetMapper<>();
	bean.setTargetType(Seance.class);
	bean.setConversionService(stringToLocalDateConversionService);
	return bean;
    }

    @Bean(name = "seanceCsvItemReader")
    @StepScope
    public FlatFileItemReader<Seance> seanceCsvItemReader(
	    @Value("#{jobParameters['seancesFile']}") final Resource inputFile,
	    @Qualifier("seanceFieldSetMapper") FieldSetMapper<Seance> seanceFieldSetMapper) {
	return new FlatFileItemReaderBuilder<Seance>().name("seanceCsvItemReader").resource(inputFile).delimited()
		.delimiter(";").names(new String[] { "codeFormation", "idFormateur", "dateDebut", "dateFin" })
		.fieldSetMapper(seanceFieldSetMapper).build();
    }

    @Bean(name = "seanceTxtItemReader")
    @StepScope
    public FlatFileItemReader<Seance> seanceTxtItemReader(
	    @Value("#{jobParameters['seancesFile']}") final Resource inputFile,
	    @Qualifier("seanceFieldSetMapper") FieldSetMapper<Seance> seanceFieldSetMapper) {
	return new FlatFileItemReaderBuilder<Seance>().name("seanceTxtItemReader").resource(inputFile).fixedLength()
		.columns(new Range[] { new Range(1, 16), new Range(17, 20), new Range(25, 32), new Range(37, 44) })
		.names(new String[] { "codeFormation", "idFormateur", "dateDebut", "dateFin" })
		.fieldSetMapper(seanceFieldSetMapper).build();
    }

    @Bean(name = "seanceItemWriter")
    public JdbcBatchItemWriter<Seance> seanceItemWriter(
	    @Qualifier("seanceItemPreparedStatementSetter") ItemPreparedStatementSetter<Seance> seanceItemPreparedStatementSetter) {
	return new JdbcBatchItemWriterBuilder<Seance>().dataSource(datasource).sql(SEANCES_INSERT_QUERY)
		.itemPreparedStatementSetter(seanceItemPreparedStatementSetter).build();
    }

    @Bean(name = "chargementSeancesCsvStep")
    public Step chargementSeancesCsvStep(
	    @Qualifier("seanceCsvItemReader") FlatFileItemReader<Seance> seanceCsvItemReader,
	    @Qualifier("seanceItemWriter") JdbcBatchItemWriter<Seance> seanceItemWriter,
	    @Qualifier("chargementSeancesStepListener") StepExecutionListener chargementSeancesStepListener) {
	return stepBuilderFactory.get("chargementFormationsStep").<Seance, Seance>chunk(10).reader(seanceCsvItemReader)
		.writer(seanceItemWriter).listener(chargementSeancesStepListener).build();
    }

    @Bean(name = "chargementSeancesTxtStep")
    public Step chargementSeancesTxtStep(
	    @Qualifier("seanceTxtItemReader") FlatFileItemReader<Seance> seanceTxtItemReader,
	    @Qualifier("seanceItemWriter") JdbcBatchItemWriter<Seance> seanceItemWriter,
	    @Qualifier("chargementSeancesStepListener") StepExecutionListener chargementSeancesStepListener) {
	return stepBuilderFactory.get("chargementFormationsStep").<Seance, Seance>chunk(10).reader(seanceTxtItemReader)
		.writer(seanceItemWriter).listener(chargementSeancesStepListener).build();
    }
}
