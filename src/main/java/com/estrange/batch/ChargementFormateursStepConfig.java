package com.estrange.batch;

import static com.estrange.batch.mappers.FormateurItemPreparedStatementSetter.FORMATEURS_INSERT_QUERY;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import com.estrange.batch.domaine.Formateur;
import com.estrange.batch.listeners.ChargementFormateursStepListener;
import com.estrange.batch.mappers.FormateurItemPreparedStatementSetter;

@Configuration
public class ChargementFormateursStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource datasource;

    @Bean
    public ItemPreparedStatementSetter<Formateur> formateurItemPreparedStatementSetter() {
        return new FormateurItemPreparedStatementSetter();
    }

    @Bean
    public StepExecutionListener chargementFormateursStepListener() {
        return new ChargementFormateursStepListener();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Formateur> formateurItemReader(
            @Value("#{jobParameters['formateursFile']}") final Resource inputFile) {
        return new FlatFileItemReaderBuilder<Formateur>().name("formateurItemReader")
                .resource(inputFile).delimited().delimiter(";")
                .names(new String[] {"id", "nom", "prenom", "adresseEmail"})
                .targetType(Formateur.class).build();
    }

    @Bean
    public JdbcBatchItemWriter<Formateur> formateurItemWriter() {
        return new JdbcBatchItemWriterBuilder<Formateur>().dataSource(datasource)
                .sql(FORMATEURS_INSERT_QUERY)
                .itemPreparedStatementSetter(formateurItemPreparedStatementSetter()).build();
    }

    @Bean
    public Step chargementFormateursStep() {
        return stepBuilderFactory.get("chargementFormateursStep").<Formateur, Formateur>chunk(10)
                .reader(formateurItemReader(null)).writer(formateurItemWriter())
                .listener(chargementFormateursStepListener()).build();
    }
}
