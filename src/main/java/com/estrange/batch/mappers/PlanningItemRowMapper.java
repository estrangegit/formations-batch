package com.estrange.batch.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.jdbc.core.RowMapper;
import com.estrange.batch.domaine.PlanningItem;

public class PlanningItemRowMapper implements RowMapper<PlanningItem> {

    @Override
    public PlanningItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        PlanningItem planningItem = new PlanningItem();
        planningItem.setLibelleFormation(StringEscapeUtils.escapeHtml4(rs.getString(1)));
        planningItem.setDateDebutSeance(rs.getDate(2).toLocalDate());
        planningItem.setDateFinSeance(rs.getDate(3).toLocalDate());
        return planningItem;
    }


}
