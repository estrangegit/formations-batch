package com.estrange.batch;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

class ChargementFormateursStepConfigTest extends BaseTest {

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
