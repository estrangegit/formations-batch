package com.estrange.batch.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.estrange.batch.domaine.Planning;
import com.estrange.batch.domaine.PlanningItem;
import com.estrange.batch.mappers.PlanningItemRowMapper;

public class PlanningProcessor implements ItemProcessor<Planning, Planning> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY =
            "SELECT f.libelle, s.date_debut,s.date_fin FROM formations f, seances s "
                    + "WHERE f.code=s.code_formation AND s.id_formateur=:formateur "
                    + "ORDER BY s.date_debut";

    @Override
    public Planning process(Planning planning) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("formateur", planning.getFormateur().getId());
        List<PlanningItem> items = jdbcTemplate.query(QUERY, params, new PlanningItemRowMapper());
        planning.setSeances(items);
        return planning;
    }

}
