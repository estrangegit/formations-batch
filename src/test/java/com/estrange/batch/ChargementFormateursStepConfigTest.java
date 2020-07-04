package com.estrange.batch;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@JdbcTest
@SpringBatchTest
@ContextConfiguration(classes = ConfigurationForTest.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ChargementFormateursStepConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private freemarker.template.Configuration conf;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void shouldLoadFormateursWithSuccess() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:inputs/formateursFile.csv")
                .toJobParameters();

        JobExecution result =
                jobLauncherTestUtils.launchStep("chargementFormateursStep", jobParameters);
        assertThat(result.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        Integer count =
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM formateurs;", Integer.class);
        assertThat(count).isEqualTo(16);

    }

}
