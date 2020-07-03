package com.estrange.batch;

import static com.estrange.batch.mappers.FormationItemPreparedStatementSetter.FORMATIONS_INSERT_QUERY;
import javax.sql.DataSource;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import com.estrange.batch.domaine.Formation;
import com.estrange.batch.listeners.ChargementFormationsStepListener;
import com.estrange.batch.mappers.FormationItemPreparedStatementSetter;

@Configuration
public class ChargementFormationsStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource datasource;

    @Bean
    public ItemPreparedStatementSetter<Formation> formationItemPreparedStatementSetter() {
        return new FormationItemPreparedStatementSetter();
    }

    @Bean
    public StepExecutionListener chargementFormationsStepListener() {
        return new ChargementFormationsStepListener();
    }

    @Bean
    public Jaxb2Marshaller formationMarshaller() {
        Jaxb2Marshaller bean = new Jaxb2Marshaller();
        bean.setClassesToBeBound(Formation.class);
        return bean;
    }

    @Bean
    @StepScope
    public StaxEventItemReader<Formation> formationItemReader(
            @Value("#{jobParameters['formationsFile']}") final Resource inputFile) {
        return new StaxEventItemReaderBuilder<Formation>().name("formationItemReader")
                .resource(inputFile).addFragmentRootElements("formation")
                .unmarshaller(formationMarshaller()).build();
    }

    @Bean
    public JdbcBatchItemWriter<Formation> formationItemWriter() {
        return new JdbcBatchItemWriterBuilder<Formation>().dataSource(datasource)
                .sql(FORMATIONS_INSERT_QUERY)
                .itemPreparedStatementSetter(formationItemPreparedStatementSetter()).build();
    }

    @Bean
    public Step chargementFormationsStep() {
        return stepBuilderFactory.get("chargementFormationsStep").<Formation, Formation>chunk(10)
                .reader(formationItemReader(null)).writer(formationItemWriter())
                .listener(chargementFormationsStepListener()).build();
    }
}
