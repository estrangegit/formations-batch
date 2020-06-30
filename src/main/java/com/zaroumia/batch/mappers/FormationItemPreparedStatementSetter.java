package com.zaroumia.batch.mappers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import com.zaroumia.batch.domaine.Formation;

public class FormationItemPreparedStatementSetter
        implements ItemPreparedStatementSetter<Formation> {

    public static final String FORMATIONS_INSERT_QUERY =
            "INSERT INTO formations (code, libelle, descriptif) VALUES (?,?,?);";

    @Override
    public void setValues(Formation formation, PreparedStatement ps) throws SQLException {
        ps.setString(1, formation.getCode());
        ps.setString(2, formation.getLibelle());
        ps.setString(3, formation.getDescriptif());
    }
}
