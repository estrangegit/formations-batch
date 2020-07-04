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
import org.springframework.beans.factory.annotation.Autowired;
import com.estrange.batch.dao.FormateurDao;
import com.estrange.batch.dao.FormationDao;
import com.estrange.batch.dao.SeanceDao;

class BatchConfigTest extends BaseTest {

    @Autowired
    private FormateurDao formateurDao;

    @Autowired
    private FormationDao formationDao;

    @Autowired
    private SeanceDao seanceDao;

    @Test
    void shouldExecuteJobWithSuccess() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:inputs/formateursFile.csv")
                .addString("formationsFile", "classpath:inputs/formationsFile.xml")
                .addString("seancesFile", "classpath:inputs/seancesFile.txt").toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(result.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        assertThat(formateurDao.count()).isEqualTo(16);
        assertThat(formationDao.count()).isEqualTo(4);
        assertThat(seanceDao.count()).isEqualTo(20);

        verify(planningMailSenderService, times(4)).send(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
