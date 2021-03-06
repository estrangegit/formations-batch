package com.estrange.batch.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;
import com.estrange.batch.domaine.Seance;

public class ChargementSeancesStepListener extends StepListenerSupport<Seance, Seance> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ChargementSeancesStepListener.class);

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("Chargement des seances :{} seances(s) enregistrée(s) ",
                stepExecution.getWriteCount());
        return stepExecution.getExitStatus();
    }
}
