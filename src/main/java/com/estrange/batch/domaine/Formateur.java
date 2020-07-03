package com.estrange.batch.domaine;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Formateur {
    private Integer id;
    private String nom;
    private String prenom;
    private String adresseEmail;
}
