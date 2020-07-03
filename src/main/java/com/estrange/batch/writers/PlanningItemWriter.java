package com.estrange.batch.writers;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import com.estrange.batch.domaine.Planning;
import com.estrange.batch.services.MailContentGenerator;
import com.estrange.batch.services.PlanningMailSenderService;

public class PlanningItemWriter implements ItemWriter<Planning> {

    private MailContentGenerator mailContentGenerator;

    private PlanningMailSenderService planningMailSenderService;

    public PlanningItemWriter(MailContentGenerator mailContentGenerator,
            PlanningMailSenderService planningMailSenderService) {
        this.mailContentGenerator = mailContentGenerator;
        this.planningMailSenderService = planningMailSenderService;
    }

    @Override
    public void write(List<? extends Planning> plannings) throws Exception {

        for (Planning planning : plannings) {
            String content = mailContentGenerator.generate(planning);
            planningMailSenderService.send(planning.getFormateur().getAdresseEmail(), content);
        }
    }
}
