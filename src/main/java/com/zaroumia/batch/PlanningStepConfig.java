package com.zaroumia.batch;

import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaroumia.batch.domaine.Planning;
import com.zaroumia.batch.mappers.PlanningRowMapper;
import com.zaroumia.batch.processors.PlanningProcessor;
import com.zaroumia.batch.services.MailContentGenerator;
import com.zaroumia.batch.services.MailContentGeneratorImpl;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateNotFoundException;

@Configuration
public class PlanningStepConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    freemarker.template.Configuration conf;

    public JdbcCursorItemReader<Planning> planningItemReader() {
        return new JdbcCursorItemReaderBuilder<Planning>().name("planningItemReader")
                .dataSource(dataSource)
                .sql("SELECT * FROM FORMATEURS formateurs1 WHERE formateurs1.id IN ("
                        + "SELECT DISTINCT(formateurs2.id) "
                        + "FROM FORMATEURS formateurs2, SEANCES seances "
                        + "WHERE formateurs2.id = seances.id_formateur);")
                .rowMapper(new PlanningRowMapper()).build();
    }

    @Bean
    public ItemProcessor<Planning, Planning> planningProcessor() {
        return new PlanningProcessor();
    }

    @Bean
    public MailContentGenerator mailContentGenerator() throws TemplateNotFoundException,
            MalformedTemplateNameException, ParseException, IOException {
        return new MailContentGeneratorImpl(conf);
    }
}
