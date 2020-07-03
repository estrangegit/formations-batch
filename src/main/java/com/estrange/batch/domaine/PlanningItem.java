package com.estrange.batch.domaine;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanningItem {
    private String libelleFormation;
    private String descriptifFormation;
    private LocalDate dateDebutSeance;
    private LocalDate dateFinSeance;
}
