package com.estrange.batch.domaine;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Seance {
    private String codeFormation;
    private Integer idFormateur;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
