CREATE TABLE IF NOT EXISTS FORMATEURS(
	id INT NOT NULL PRIMARY KEY,
	nom VARCHAR(12) NOT NULL,
	prenom VARCHAR(12) NOT NULL,
	adresse_email VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS FORMATIONS(
	CODE VARCHAR(20) NOT NULL PRIMARY KEY,
	libelle VARCHAR(200) NOT NULL,
	descriptif VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS SEANCES(
	code_formation VARCHAR(20) NOT NULL,
	id_formateur INT NOT NULL,
	date_debut DATE NOT NULL,
	date_fin DATE NOT NULL,
	PRIMARY KEY (code_formation, id_formateur, date_debut, date_fin),
	CONSTRAINT FK_FORMATEURS FOREIGN KEY(id_formateur) REFERENCES FORMATEURS(id),
	CONSTRAINT FK_FORMATIONS FOREIGN KEY(code_formation) REFERENCES FORMATIONS(CODE)
);
