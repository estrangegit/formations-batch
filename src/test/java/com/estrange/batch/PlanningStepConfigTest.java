package com.estrange.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.context.jdbc.Sql;

class PlanningStepConfigTest extends BaseTest {

    @Test
    @Sql(scripts = {"classpath:init-all-tables.sql"})
    void shouldSendPlanningWithSuccess() throws MessagingException {
        JobExecution result = jobLauncherTestUtils.launchStep("planningStep");
        assertThat(result.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        verify(planningMailSenderService, times(4)).send(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
