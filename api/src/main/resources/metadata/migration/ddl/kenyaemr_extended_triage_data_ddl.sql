CREATE TABLE `kenyaemr_extended_triage_patient_data` (
  `person_uuid` CHAR(38) NOT NULL,
  `triage_log_id` int DEFAULT NULL,
  `weight` decimal(5,2) DEFAULT NULL,
  `height` decimal(5,2) DEFAULT NULL,
  `temperature` decimal(5,2) DEFAULT NULL,
  `systolic` int DEFAULT NULL,
  `daistolic` int DEFAULT NULL,
  `respiratory_rate` int DEFAULT NULL,
  `pulse_rate` int DEFAULT NULL,
  `blood_group` varchar(10) DEFAULT NULL,
  `last_menstrual` datetime DEFAULT NULL,
  `rhesus_factor` varchar(12) DEFAULT NULL,
  `pitct` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `mua` decimal(5,2) DEFAULT NULL,
  `chest` decimal(5,2) DEFAULT NULL,
  `abdominal` decimal(5,2) DEFAULT NULL,
  `encounter_opd` int DEFAULT NULL,
  `BMI` decimal(5,2) DEFAULT NULL,
  `oxygen_saturation` double DEFAULT NULL,
  `patient_id` int DEFAULT '0',
  CONSTRAINT FOREIGN KEY (patient_id) REFERENCES kenyaemr_extended_patient_demographics(patient_id),
  INDEX(patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;