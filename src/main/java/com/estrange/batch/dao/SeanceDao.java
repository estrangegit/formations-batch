package com.estrange.batch.dao;

import java.util.List;
import com.estrange.batch.domaine.PlanningItem;

public interface SeanceDao {
    int count();

    List<PlanningItem> getByFormateurId(Integer formateurId);
}
