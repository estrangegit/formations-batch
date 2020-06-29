package com.zaroumia.batch.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;

import com.zaroumia.batch.domaine.Formateur;

public class ChargementFormateursStepListener extends StepListenerSupport<Formateur, Formateur> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargementFormateursStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
	// TODO Auto-generated method stub

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
	LOGGER.info("Chargement des formateurs :{} formateur(s) enregistré(s) ", stepExecution.getWriteCount());
	return stepExecution.getExitStatus();
    }

}
