package com.estrange.batch;

import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import com.estrange.batch.domaine.Planning;
import com.estrange.batch.mappers.PlanningRowMapper;
import com.estrange.batch.processors.PlanningProcessor;
import com.estrange.batch.services.MailContentGenerator;
import com.estrange.batch.services.MailContentGeneratorImpl;
import com.estrange.batch.services.PlanningMailSenderService;
import com.estrange.batch.services.PlanningMailSenderServiceImpl;
import com.estrange.batch.writers.PlanningItemWriter;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateNotFoundException;

@Configuration
public class PlanningStepConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private freemarker.template.Configuration conf;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailContentGenerator mailContentGenerator;

    @Autowired
    private PlanningMailSenderService planningMailSenderService;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

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

    @Bean
    PlanningMailSenderService planningMailSenderService() {
        return new PlanningMailSenderServiceImpl(javaMailSender);
    }

    @Bean
    public PlanningItemWriter planningWriter() {
        return new PlanningItemWriter(mailContentGenerator, planningMailSenderService);
    }

    @Bean
    public Step planningStep() {
        return stepBuilderFactory.get("planningStep").<Planning, Planning>chunk(10)
                .reader(planningItemReader()).processor(planningProcessor())
                .writer(planningWriter()).build();
    }
}
