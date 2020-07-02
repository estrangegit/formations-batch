package com.zaroumia.batch.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.zaroumia.batch.domaine.PlanningItem;

public class PlanningItemRowMapper implements RowMapper<PlanningItem> {

    @Override
    public PlanningItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        PlanningItem planningItem = new PlanningItem();
        planningItem.setLibelleFormation(rs.getString(1));
        planningItem.setDateDebutSeance(rs.getDate(2).toLocalDate());
        planningItem.setDateDebutSeance(rs.getDate(3).toLocalDate());
        return planningItem;
    }


}
