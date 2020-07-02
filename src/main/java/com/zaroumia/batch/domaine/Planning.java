package com.zaroumia.batch.domaine;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Planning {
    private Formateur formateur;
    private List<PlanningItem> seances;
}
