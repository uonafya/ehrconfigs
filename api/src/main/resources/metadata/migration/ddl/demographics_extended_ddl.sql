-- create table demographics_extended
DROP table IF EXISTS migration_tr.tr_demographics;
create table migration_tr.tr_demographics (
patient_id INT(11) DEFAULT NULL,
Person_Id INT(11) not null primary key,
uuid char(38),
given_name VARCHAR(255),
middle_name VARCHAR(255),
family_name VARCHAR(255),
Gender VARCHAR(10),
DOB DATE,
national_id VARCHAR(50),
huduma_number VARCHAR(50),
passport_no VARCHAR(50),
birth_cert_number VARCHAR(50),
patient_clinic_number VARCHAR(15) DEFAULT NULL,
openmrs_id VARCHAR(50),
driving_license_no VARCHAR(50),
patient_opd_number VARCHAR(50),
national_unique_patient_identifier VARCHAR(50),
phone_number VARCHAR(50) DEFAULT NULL,
birth_place VARCHAR(50) DEFAULT NULL,
citizenship VARCHAR(50) DEFAULT NULL,
email_address VARCHAR(100) DEFAULT NULL,
occupation VARCHAR(100) DEFAULT NULL,
next_of_kin VARCHAR(255) DEFAULT NULL,
next_of_kin_phone VARCHAR(100) DEFAULT NULL,
next_of_kin_relationship VARCHAR(100) DEFAULT NULL,
marital_status VARCHAR(50) DEFAULT NULL,
education_level VARCHAR(50) DEFAULT NULL,
paying_category_type VARCHAR(50) DEFAULT NULL,
non_paying_category_type VARCHAR(50) DEFAULT NULL,
special_scheme_category_type VARCHAR(50) DEFAULT NULL,
payment_category VARCHAR(50) DEFAULT NULL,
file_number VARCHAR(50) DEFAULT NULL,
waiver_number VARCHAR(50) DEFAULT NULL,
exemption_number VARCHAR(50) DEFAULT NULL,
nhif_card_number VARCHAR(50) DEFAULT NULL,
student_id VARCHAR(50) DEFAULT NULL,
payment_sub_category VARCHAR(50) DEFAULT NULL,
dead INT(11),
death_date DATE DEFAULT NULL,
county VARCHAR(100) DEFAULT NULL,
sub_county VARCHAR(100) DEFAULT NULL,
location VARCHAR(100) DEFAULT NULL,
ward VARCHAR(100) DEFAULT NULL,
sub_location VARCHAR(100) DEFAULT NULL,
village VARCHAR(100) DEFAULT NULL,
postal_address VARCHAR(100) DEFAULT NULL,
land_mark VARCHAR(100) DEFAULT NULL,
Exact_DOB int(11) DEFAULT NULL,
Next_of_kin_address varchar(225) DEFAULT NULL,
UPN varchar(225) DEFAULT NULL,
Nearest_Health_Centre varchar(225) DEFAULT NULL,
voided INT(11),
index(patient_id),
index(Person_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
