package com.estrange.batch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.estrange.batch.domaine.PlanningItem;
import com.estrange.batch.mappers.PlanningItemRowMapper;

@Repository
public class SeanceDaoImpl implements SeanceDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterjdbcTemplate;

    private static final String QUERY =
            "SELECT f.libelle, s.date_debut,s.date_fin FROM formations f, seances s "
                    + "WHERE f.code=s.code_formation AND s.id_formateur=:formateur "
                    + "ORDER BY s.date_debut";

    @Override
    public int count() {
        return namedParameterjdbcTemplate.queryForObject("SELECT COUNT(*) FROM seances;",
                new HashMap<>(), Integer.class);
    }

    @Override
    public List<PlanningItem> getByFormateurId(Integer formateurId) {
        Map<String, Object> params = new HashMap<>();
        params.put("formateur", formateurId);
        return namedParameterjdbcTemplate.query(QUERY, params, new PlanningItemRowMapper());
    }
}
