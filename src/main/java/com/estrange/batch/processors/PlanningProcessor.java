package com.estrange.batch.processors;

import java.util.List;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import com.estrange.batch.dao.SeanceDao;
import com.estrange.batch.domaine.Planning;
import com.estrange.batch.domaine.PlanningItem;

public class PlanningProcessor implements ItemProcessor<Planning, Planning> {

    @Autowired
    private SeanceDao seanceDao;

    @Override
    public Planning process(Planning planning) throws Exception {
        List<PlanningItem> items = seanceDao.getByFormateurId(planning.getFormateur().getId());
        planning.setSeances(items);
        return planning;
    }

}
