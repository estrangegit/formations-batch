package com.estrange.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

class BatchConfigTest extends BaseTest {

    @Test
    void shouldExecuteJobWithSuccess() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:inputs/formateursFile.csv")
                .addString("formationsFile", "classpath:inputs/formationsFile.xml")
                .addString("seancesFile", "classpath:inputs/seancesFile.txt").toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(result.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM formateurs;", Integer.class))
                .isEqualTo(16);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM formations;", Integer.class))
                .isEqualTo(4);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM seances;", Integer.class))
                .isEqualTo(20);

        verify(planningMailSenderService, times(4)).send(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
