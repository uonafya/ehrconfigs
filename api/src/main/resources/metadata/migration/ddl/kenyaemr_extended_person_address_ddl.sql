CREATE TABLE kenyaemr_extended_person_address (
    uuid CHAR(38) NOT NULL PRIMARY KEY,
    patient_id INT(11) NOT NULL ,
    county VARCHAR(100) DEFAULT NULL,
    sub_county VARCHAR(100) DEFAULT NULL,
    location VARCHAR(100) DEFAULT NULL,
    ward VARCHAR(100) DEFAULT NULL,
    sub_location VARCHAR(100) DEFAULT NULL,
    village VARCHAR(100) DEFAULT NULL,
    postal_address VARCHAR(100) DEFAULT NULL,
    land_mark VARCHAR(100) DEFAULT NULL,
    voided INT(11),
    CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_extended_patient_demographics(patient_id),
    CONSTRAINT unique_uuid UNIQUE(uuid),
    INDEX(patient_id)
  );