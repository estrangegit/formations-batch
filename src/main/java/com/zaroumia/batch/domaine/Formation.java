package com.zaroumia.batch.domaine;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@XmlRootElement
public class Formation {
    private String code;
    private String libelle;
    private String descriptif;
}
