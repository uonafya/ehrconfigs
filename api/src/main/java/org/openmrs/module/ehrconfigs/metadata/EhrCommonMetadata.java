/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ehrconfigs.metadata;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.encounterType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.form;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.personAttributeType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.program;

@Component
public class EhrCommonMetadata extends AbstractMetadataBundle {
	
	//visit types
	public static final class _VistTypes {
		
		public static final String FACILITY_VISIT = "66a4ef36-fac4-11ea-bcbf-375d20d55603";
		
		public static final String INITIAL_MCH_CLINIC_VISIT = "76583758-fac4-11ea-b5f3-d340ddbbb0d2";
		
		public static final String RETURN_ANC_CLINIC_VISIT = "8462eab4-fac4-11ea-a965-338aef2ca794";
		
		public static final String RETURN_PNC_CLINIC_VISIT = "94e337ea-fac4-11ea-be33-0736112d65ec";
		
		public static final String RETURN_CWC_CLINIC_VISIT = "a6329004-fac4-11ea-babb-7b8345fde680";
	}
	
	//person atttribute types
	public static final class _EhrPersonAttributeType {
		
		public static final String PAYING_CATEGORY_TYPE = "e191b0b8-f069-11ea-b498-2bfd800847e8";
		
		public static final String NON_PAYING_CATEGORY_TYPE = "0a8ae818-f06a-11ea-ab82-2f183f30d954";
		
		public static final String SPECIAL_CLINIC_CATEGORY_TYPE = "341ee8fa-f06a-11ea-aca0-03d040bd88c8";
		
		public static final String PAYMENT_CATEGORY = "09cd268a-f0f5-11ea-99a8-b3467ddbf779"; // Id 14 in old afyaehms
		
		public static final String FILE_NUMBER = "858781dc-282f-11eb-8741-8ff5ddd45b7c"; //id 43 in old afyaehms
		
		public static final String WAIVER_NUMBER = "a804c03e-f1bc-11ea-ae43-dfa0f52ad887"; //id in old afyaehms is 32
		
		public static final String EXEMPTION_NUMBER = "a22892ce-f1d2-11ea-a512-138a123d324a"; //id in old afyaehms is 36
		
		public static final String PPO_NUMBER = "bd2e75c6-0261-11eb-8942-afdb78609e0e"; //id in old afyaehms is 17
		
		public static final String FREE_CATEGORY = "e41c0400-0261-11eb-a88c-9f66e7b2fac4"; //id in old afyaehms is 18
		
		public static final String FREE_CATEGORY_REASON = "0d3b9134-0262-11eb-979f-57cecced53d8"; //id in old afyaehms is 19
		
		public static final String WEIGHT = "9105adee-0265-11eb-8ffe-cfd4b66eebbe"; //id in old afyaehms is 21
		
		public static final String BLOOD_PRESSURE = "b6a0bbca-0265-11eb-9df5-536d14a99f2f"; //id in old afyaehms is 22
		
		public static final String PATIENT_HISTORY = "ee3b64c2-0265-11eb-8e51-bb9aaa2cc007"; //id in old afyaehms is 23
		
		public static final String HEALTH_ID = "0df9ad6e-0266-11eb-ae59-87b5342637a3"; //id in old afyaehms is 24
		
		public static final String OTHER_NAME = "357620c0-0266-11eb-b45b-f3f89d85a8d7"; //id in old afyaehms is 25
		
		public static final String MARITAL_STATUS = "71631020-0266-11eb-a662-ef6a6b91583c"; //id in old afyaehms is 26
		
		public static final String NATIONALITY = "a2b040bc-0266-11eb-b808-332aa122dc1b"; //id in old afyaehms is 27
		
		public static final String PHYSICAL_RESIDENCE_RELATIVE = "aac38e38-026d-11eb-a502-0fd7f9c94160"; //id in old afyaehms is 28
		
		public static final String TELEPHONE_NUMBER_RELATIVE = "41eb505c-026e-11eb-8f64-a76d93dc6b55"; //id in old afyaehms is 29
		
		public static final String EMAIL_ADDRESS_RELATIVE = "706dbf50-026e-11eb-b716-fbb3bb196995"; //id in old afyaehms is 30
		
		public static final String CHILD_LESS_THAN_5_YEAR_EXCEMPTION_NUMBER = "39f40dca-026f-11eb-a1f5-c31fabaf988d"; //id in old afyaehms is 31
		
		public static final String NHIF_CARD_ID = "9c945480-026f-11eb-9ff1-dbeee1a64b43"; //id in old afyaehms is 33
		
		public static final String NHIF_CARD_NUMBER = "bde64e04-026f-11eb-bb17-73c196be052b"; //id in old afyaehms is 34
		
		public static final String COMPREHENSIVE_CARE_CLINIC_PATIENT_EXEMPTION_NUMBER = "644d27c2-0270-11eb-8141-7336ef783a2e"; //id in old afyaehms is 35
		
		public static final String PATIENT_EMAIL_ADDRESS = "8f504f4e-0270-11eb-8ec9-6bb265d687b4"; //id in old afyaehms is 37
		
		public static final String PASSPORT_NUMBER = "ceb91c92-0270-11eb-ae44-1f4df7069a2e"; //id in old afyaehms is 38
		
		public static final String OTHER_NATIONALITY = "ef5bb162-0270-11eb-aef7-5bf3d4c5bc8b"; //id in old afyaehms is 39
		
		public static final String RELIGION = "1d357ec4-0271-11eb-bd97-47682e436948"; //id in old afyaehms is 40
		
		public static final String CHIEFDOM = "49a10820-0271-11eb-ab9f-f771acccf116"; //id in old afyaehms is 41
		
		public static final String STUDENT_ID = "88546440-0271-11eb-b43f-c392cfe8f5df"; //id in old afyaehms is 42
		
		public static final String UNIVERSITY = "5158d876-0272-11eb-a732-4f63eec879b9"; //id in old afyaehms is 47
		
		public static final String UNKOWN_PATIENT = "65c1b1f4-0275-11eb-b010-afbfa32a1d87"; //id in old afyaehms is 49
		
		public static final String TEST_PATIENT = "8e4006f8-0275-11eb-9ab8-f7ec6b1f4cec"; //id in old afyaehms is 50
		
		public static final String PATIENT_NUMBER_NOK = "cfca6154-0275-11eb-a9ed-43bf454ed3f5"; //id in old afyaehms is 51
		
		public static final String CHILD_FULLY_IMMUNIZED = "0c355d56-0276-11eb-80f6-cb51ab9e11cb"; //id in old afyaehms is 52
		
		//Already existing in kenyaEMR, just use the constant directly
		public static final String NEXT_OF_KIN_INFORMANT_NAME_TYPE = "830bef6d-b01f-449d-9f8d-ac0fede8dbd3"; //id in old afyaehms is 15
		
		public static final String PHONE_NUMBER = "b2c38640-2603-4629-aebd-3b54f33f1e3a"; //id in old afyaehms is 16
		
		public static final String NATIONAL_ID_NUMBER = "73d34479-2f9e-4de3-a5e6-1f79a17459bb"; //id in old afyaehms is 20
		
		//
	}
	
	//encounter types
	public static final class _EhrEncounterTypes {
		
		public static final String RADIOLOGYENCOUNTER = "012bb9f4-f282-11ea-a6d6-3b4fa4aefb5a"; // old value is 8
		
		public static final String LABENCOUNTER = "11d3f37a-f282-11ea-a825-1b5b1ff1b854";// old value is 7
		
		public static final String ADMISION = "6e1105ba-f282-11ea-ad42-e7971c094de0"; // old id was 15
		
		public static final String ADULTINITIAL = "63721d9e-f28f-11ea-acf1-1ba6050a20d6"; // old id 1
		
		public static final String ADULTRETURN = "6cc4098e-f28f-11ea-ac2c-335ca43b58da"; // old id 2
		
		public static final String PEDSINITIAL = "7c14fbbe-f28f-11ea-ba41-df4a13e6c5d6"; // old id 3
		
		public static final String PEDSRETURN = "856c6f8a-f28f-11ea-ab67-3f6de890685e"; //old id  4
		
		public static final String REGINITIAL = "8efa1534-f28f-11ea-b25f-af56118cf21b"; // old id 5
		
		public static final String REGREVISIT = "98d42234-f28f-11ea-b609-bbd062a0383b"; // old id 6
		
		public static final String OPDENCOUNTER = "ba45c278-f290-11ea-9666-1b3e6e848887"; // old id 9
		
		public static final String IPDENCOUNTER = "f2fdbb02-f290-11ea-9a81-3729eb94d52d"; // old id 10
		
		public static final String TRIAGEENCOUNTER = "2af60550-f291-11ea-b725-9753b5f685ae"; // old id 11
		
		public static final String CHECKIN = "ce33a16e-f291-11ea-90fb-2fc2d783a570"; // old id 12
		
		public static final String VITALS = "f15399a6-f291-11ea-b2ad-3341295ede42"; // old id 13
		
		public static final String DISCHARGE = "14dc10b0-f292-11ea-a573-cf69ab0e0e17"; // old id 14
		
		public static final String VISITNOTE = "502281ae-f292-11ea-8273-ebd1e54b661c"; // old id 16
		
		public static final String CHECKOUT = "6f248e9e-f292-11ea-b084-0fbd52cd2188"; // old id 17
		
		public static final String TRANSFER = "8d9451e8-f292-11ea-bb2f-8fdebce3194d"; // old id 18
		
		public static final String ANCENCOUNTER = "ae378f6e-f292-11ea-8ef8-0bb1ae4b7f42"; // old id 19 //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String PNCENCOUNTER = "cbe0212a-f292-11ea-affa-2b398f67bbb4"; // old id 20 //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		//NCD encounter types added
		public static final String DM_HTN_INITIAL_ENCOUNTER_TYPE = "cb5f27f0-18f8-11eb-88d7-fb1a7178f8ea";
		
		public static final String DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE = "f1573d1c-18f8-11eb-a453-63d51e56f5cb";
		
		public static final String NCD_DISCONTINUE_ENCOUNTER_TYPE = "1f7ec412-18f9-11eb-8122-8f25a543787a";
		
		public static final String NCD_ADMISSION_ENCOUNTER_TYPE = "4664a5b0-18f9-11eb-8a5a-2bee5937c71b";
		
		public static final String NCD_FOOT_CLINIC_ENCOUNTER_TYPE = "af5dbd36-18f9-11eb-ae6b-7f4c0920f004";
		
		////////
		public static final String PNC_TRIAGE_ENCOUNTER_TYPE = "fcaec384-fac5-11ea-8aeb-fffc453fcc77";
		
		public static final String CWC_ENCOUNTER_TYPE = "09073c56-fac6-11ea-87aa-bfe01fd142fd"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String CWC_TRIAGE_ENCOUNTER_TYPE = "150932f2-fac6-11ea-b158-4b1b80bfe51c";
		
		public static final String ANC_TRIAGE_ENCOUNTER_TYPE = "25dbb7d0-fac6-11ea-94e8-0bd56324f4b5";
		
		public static final String ONCOLOGY_INITIAL = "586660a8-08ca-11eb-9dac-6f55ef0a3a7d";
		
		public static final String ONCOLOGY_DISCONTINUE = "7ae70e0c-08ca-11eb-9325-676401a68809";
		
		public static final String ONCOLOGY_FOLLOWUP = "984405fe-08ca-11eb-bb7a-cb6ccfe9c91e";
		
		public static final String ONCOLOGY_SCREENING = "e24209cc-0a1d-11eb-8f2a-bb245320c623";
		
		public static final String FAMILY_CANCER_HISTORY = "b9c51a7e-0e24-11eb-9559-13c33ac53eec";
	}
	
	public static final class _EhrPrograms {
		
		public static final String ANC_PROGRAM = "e8751e5c-fbda-11ea-9bba-ff7e8cea17d3"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String ANC_PROGRAM_CONCEPT = "160446AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String PNC_PROGRAM = "23edfbca-fbdb-11ea-a675-17377ca3079e"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String CWC_PROGRAM = "645d7e4c-fbdb-11ea-911a-5fe00fc87a47"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String CWC_PROGRAM_CONCEPT = "163110AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String PNC_PROGRAM_CONCEPT = "1623AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String ONCOLOGY_PROGRAM_CONCEPT = "116030AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		
		public static final String ONCOLOGY_PROGRAM = "64d6b82c-08a9-11eb-b236-077a7c74158b";
		
	}
	
	public static final class _EhrForms {
		
		public static final String ONCOLOGY_ENROLLMENT_FORM = "4b19bf6a-08cc-11eb-ad40-63f9910aca7b";
		
		public static final String ONCOLOGY_DISCONTINUATION_FORM = "67b0c4de-08cc-11eb-ae4d-c756d1210d5c";
		
		public static final String ONCOLOGY_FOLLOWUP_FORM = "884df5d6-08cc-11eb-a31e-27b10c64a0a7";
		
		public static final String ONCOLOGY_SCREENING_FORM = "be5c5602-0a1d-11eb-9e20-37d2e56925ee";
		
		public static final String FAMILY_CANCER_HISTORY = "dd0e4f3c-0e24-11eb-aa70-bb2cf351b19b";
		
		//NCD forms
		public static final String NCD_ADMISSION = "9836224a-18fb-11eb-b83c-232364035228";
		
		public static final String NCD_FOLLOW_UP = "b3e07c84-18fb-11eb-bab2-6f7808a09aa6";
		
		public static final String NCD_DISCONTINUE = "d2c4ae9a-18fb-11eb-ab7d-1b8027b414d7";
		
		public static final String NCD_INITIAL = "edd8c072-18fb-11eb-9c05-839296c291c4";
		
		public static final String NCD_FOOT_CLINIC = "099d5e12-18fc-11eb-86f3-231df7469c4e";
		
	}
	
	@Override
	public void install() throws Exception {
		//Installing person attribute types
		install(personAttributeType("Paying Category Type", "Paying Category Type person attribute", String.class, null,
		    false, 1.0, _EhrPersonAttributeType.PAYING_CATEGORY_TYPE));
		install(personAttributeType("Non-Paying Category Type", "Paying Category Type person attribute", String.class, null,
		    false, 1.0, _EhrPersonAttributeType.NON_PAYING_CATEGORY_TYPE));
		install(personAttributeType("Special Scheme Category Type", "Paying Category Type person attribute", String.class,
		    null, false, 1.0, _EhrPersonAttributeType.SPECIAL_CLINIC_CATEGORY_TYPE));
		install(personAttributeType("Payment Category", "The category to which the patient belongs to for hospital admin",
		    String.class, null, false, 1.0, _EhrPersonAttributeType.PAYMENT_CATEGORY));
		install(personAttributeType("File Number", "File number used for the patients enrolled in the special schemes",
		    String.class, null, false, 1.0, _EhrPersonAttributeType.FILE_NUMBER));
		install(personAttributeType("Waiver Number", "Waiver Number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.WAIVER_NUMBER));
		install(personAttributeType("Exemption Number", "Exemption Number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.EXEMPTION_NUMBER));
		install(personAttributeType("PPO Number", "Attribute to store pensions number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.PPO_NUMBER));
		install(personAttributeType("Free Category", "Free Category", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.FREE_CATEGORY));
		install(personAttributeType("Free Category Reason", "Free Category Reason", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.FREE_CATEGORY_REASON));
		install(personAttributeType("Weight", "Weight of a patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.WEIGHT));
		install(personAttributeType("Blood Pressure", "Blood pressure of a Patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.BLOOD_PRESSURE));
		install(personAttributeType("Patient History", "Medical history of a Patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.PATIENT_HISTORY));
		install(personAttributeType("Health ID", "Health ID of a patient is used for registering a patient", String.class,
		    null, false, 1.0, _EhrPersonAttributeType.HEALTH_ID));
		install(personAttributeType("Other Name", "Other Name of the Patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.OTHER_NAME));
		install(personAttributeType("Marital Status", "Marital Status of the Patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.MARITAL_STATUS));
		install(personAttributeType("Nationality", "Nationality of the Patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.NATIONALITY));
		install(personAttributeType("Physical Residence Relative",
		    "Physical Residence of the relative (NOK) of the patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.PHYSICAL_RESIDENCE_RELATIVE));
		install(personAttributeType("Telephone Number Relative", "Telephone Number of the relative (NOK) of the patient",
		    String.class, null, false, 1.0, _EhrPersonAttributeType.TELEPHONE_NUMBER_RELATIVE));
		install(personAttributeType("Email Address Relative", "E-mail address of the relative (NOK) of the patient",
		    String.class, null, false, 1.0, _EhrPersonAttributeType.EMAIL_ADDRESS_RELATIVE));
		install(personAttributeType("Child less than 5 yr Exemption Number", "Exemption Number for child less than 5 years",
		    String.class, null, false, 1.0, _EhrPersonAttributeType.CHILD_LESS_THAN_5_YEAR_EXCEMPTION_NUMBER));
		install(personAttributeType("NHIF Card ID", "NHIF Card ID", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.NHIF_CARD_ID));
		install(personAttributeType("NHIF Card Number", "NHIF Exemption Number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.NHIF_CARD_NUMBER));
		install(personAttributeType("Comprehensive Care Clinic Patient Exemption Number",
		    "Comprehensive Care Clinic Patient Exemption Number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.COMPREHENSIVE_CARE_CLINIC_PATIENT_EXEMPTION_NUMBER));
		install(personAttributeType("Exemption Number", "Exemption Number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.EXEMPTION_NUMBER));
		install(personAttributeType("Patient E-mail Address", "E-mail address of patient", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.PATIENT_EMAIL_ADDRESS));
		install(personAttributeType("Passport Number", "Passport Number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.PASSPORT_NUMBER));
		install(personAttributeType("Other Nationality", "Other Nationality", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.OTHER_NATIONALITY));
		install(personAttributeType("Religion", "Religion", String.class, null, false, 1.0, _EhrPersonAttributeType.RELIGION));
		install(personAttributeType("Chiefdom", "Chiefdom of the village", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.CHIEFDOM));
		install(personAttributeType("Student ID", "Student ID Number", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.STUDENT_ID));
		install(personAttributeType("University", "University to which the student belongs to", String.class, null, false,
		    1.0, _EhrPersonAttributeType.UNIVERSITY));
		install(personAttributeType("Unknown patient",
		    "Used to flag patients that cannot be identified during the check-in process", String.class, null, false, 1.0,
		    _EhrPersonAttributeType.UNKOWN_PATIENT));
		install(personAttributeType("Test Patient", "Flag to describe if the patient was created to a test or not",
		    String.class, null, false, 1.0, _EhrPersonAttributeType.TEST_PATIENT));
		install(personAttributeType("Phone Number (NOK)", "Contacts of Next of kin/ Informant", String.class, null, false,
		    1.0, _EhrPersonAttributeType.PATIENT_NUMBER_NOK));
		install(personAttributeType("Child Fully Immunized", "Stored property whether a child has completed immunization",
		    String.class, null, false, 1.0, _EhrPersonAttributeType.CHILD_FULLY_IMMUNIZED));
		//Installing encounter types
		install(encounterType("RADIOLOGYENCOUNTER", "This define encounters with radiology/ultrasounds",
		    _EhrEncounterTypes.RADIOLOGYENCOUNTER));
		install(encounterType("LABENCOUNTER", "Encounters in General Lab", _EhrEncounterTypes.LABENCOUNTER));
		install(encounterType(
		    "Admission",
		    "Indicates that the patient has been admitted for inpatient care, and is not expected to leave the hospital unless discharged.",
		    _EhrEncounterTypes.ADMISION));
		install(encounterType("ADULTINITIAL", "Outpatient Adult Initial Visit", _EhrEncounterTypes.ADULTINITIAL));
		install(encounterType("ADULTRETURN", "Outpatient Adult Return Visit", _EhrEncounterTypes.ADULTRETURN));
		install(encounterType("PEDSINITIAL", "Outpatient Pediatric Initial Visit", _EhrEncounterTypes.PEDSINITIAL));
		install(encounterType("PEDSRETURN", "Outpatient Pediatric Return Visit", _EhrEncounterTypes.PEDSRETURN));
		install(encounterType("REGINITIAL", "Outpatient Adult Initial Visit", _EhrEncounterTypes.REGINITIAL));
		install(encounterType("REGREVISIT", "Outpatient Adult Return Visit", _EhrEncounterTypes.REGREVISIT));
		install(encounterType("OPDENCOUNTER", "Encounter for OPD", _EhrEncounterTypes.OPDENCOUNTER));
		install(encounterType("IPDENCOUNTER", "Encounter for IPD", _EhrEncounterTypes.IPDENCOUNTER));
		install(encounterType("TRIAGEENCOUNTER", "Encounter for Triage", _EhrEncounterTypes.TRIAGEENCOUNTER));
		install(encounterType("Check In",
		    "Indicates the patient has done the required paperwork and check-in to begin a visit to the clinic/hospital.",
		    _EhrEncounterTypes.CHECKIN));
		install(encounterType("Vitals", "For capturing vital signs", _EhrEncounterTypes.VITALS));
		install(encounterType("Discharge",
		    "Indicates that a patient's inpatient care at the hospital is ending, and they are expected to leave soon.",
		    _EhrEncounterTypes.DISCHARGE));
		install(encounterType(
		    "Visit Note",
		    "Encounter where a full or abbreviated examination is done, usually leading to a presumptive or confirmed diagnosis, recorded by the examining clinician.",
		    _EhrEncounterTypes.VISITNOTE));
		install(encounterType("Check Out",
		    "The patient is explicitly leaving the hospital/clinic. (Usually no formal encounter is captured for this.)",
		    _EhrEncounterTypes.CHECKOUT));
		install(encounterType(
		    "Transfer",
		    "Indicates that a patient is being transferred into a different department within the hospital. (Transfers out of the hospital should not use this encounter type.)",
		    _EhrEncounterTypes.TRANSFER));
		install(encounterType("ANCENCOUNTER", "ANC encounter type", _EhrEncounterTypes.ANCENCOUNTER));
		install(encounterType("PNCENCOUNTER", "PNC encounter type", _EhrEncounterTypes.PNCENCOUNTER));
		
		//visit types to be applied
		install(encounterType("FACILITYVISIT",
		    "Patient visits the clinic/hospital (as opposed to a home visit, or telephone contact)",
		    _VistTypes.FACILITY_VISIT));
		install(encounterType("INITIALMCHCLINICVISIT", "Initial Visit to the MCH Clinic",
		    _VistTypes.INITIAL_MCH_CLINIC_VISIT));
		install(encounterType("RETURNANCCLINICVISIT", "Return ANC Clinic Visit", _VistTypes.RETURN_ANC_CLINIC_VISIT));
		install(encounterType("RETURNPNCCLINICVISIT", "Return PNC Clinic Visit", _VistTypes.RETURN_PNC_CLINIC_VISIT));
		install(encounterType("RETURNCWCCLINICVISIT", "Return CWC Clinic Visit", _VistTypes.RETURN_CWC_CLINIC_VISIT));
		install(encounterType("ANCTRIAGEENCOUNTER", "ANC triage encounter type",
		    _EhrEncounterTypes.ANC_TRIAGE_ENCOUNTER_TYPE));
		install(encounterType("PNCTRIAGEENCOUNTER", "PNC triage encounter type",
		    _EhrEncounterTypes.PNC_TRIAGE_ENCOUNTER_TYPE));
		install(encounterType("CWCENCOUNTER", "CWC encounter type", _EhrEncounterTypes.CWC_ENCOUNTER_TYPE));
		install(encounterType("CWCTRIAGEENCOUNTER", "CWC triage encounter type",
		    _EhrEncounterTypes.CWC_TRIAGE_ENCOUNTER_TYPE));
		
		//programs
		install(program("Antenatal Care Program", "ANC Program", _EhrPrograms.ANC_PROGRAM_CONCEPT, _EhrPrograms.ANC_PROGRAM));
		install(program("Postnatal Care Program", "PNC Program", _EhrPrograms.PNC_PROGRAM_CONCEPT, _EhrPrograms.PNC_PROGRAM));
		install(program("Child Welfare Program", "CW Program", _EhrPrograms.CWC_PROGRAM_CONCEPT, _EhrPrograms.CWC_PROGRAM));
		//oncology
		install(program("Oncology Program", "Oncology program", _EhrPrograms.ONCOLOGY_PROGRAM_CONCEPT,
		    _EhrPrograms.ONCOLOGY_PROGRAM));
		//oncology encounter types
		install(encounterType("Oncology Initial", "Initial oncology encounter type", _EhrEncounterTypes.ONCOLOGY_INITIAL));
		install(encounterType("Oncology Discontinue", "Discontinue oncology encounter type",
		    _EhrEncounterTypes.ONCOLOGY_DISCONTINUE));
		install(encounterType("Oncology Followup", "Follow up oncology encounter type", _EhrEncounterTypes.ONCOLOGY_FOLLOWUP));
		install(encounterType("Oncology Screening", "Oncology screening  encounter type",
		    _EhrEncounterTypes.ONCOLOGY_SCREENING));
		install(encounterType("Family cancer history", "Family cancer history encounter type",
		    _EhrEncounterTypes.FAMILY_CANCER_HISTORY));
		//oncology forms
		install(form("Oncology Initial enrollment form", "Initial form for oncology enrollment",
		    _EhrEncounterTypes.ONCOLOGY_INITIAL, "1.0", _EhrForms.ONCOLOGY_ENROLLMENT_FORM));
		install(form("Oncology  Discontinutaion form", "Discontinuation  form for oncology",
		    _EhrEncounterTypes.ONCOLOGY_DISCONTINUE, "1.0", _EhrForms.ONCOLOGY_DISCONTINUATION_FORM));
		install(form("Oncology Followup form", "Followup form for oncology", _EhrEncounterTypes.ONCOLOGY_FOLLOWUP, "1.0",
		    _EhrForms.ONCOLOGY_FOLLOWUP_FORM));
		install(form("Oncology Screening form", "Screening form for oncology", _EhrEncounterTypes.ONCOLOGY_SCREENING, "1.0",
		    _EhrForms.ONCOLOGY_SCREENING_FORM));
		install(form("Family cancer history form", "Family cancer history form for oncology",
		    _EhrEncounterTypes.FAMILY_CANCER_HISTORY, "1.0", _EhrForms.FAMILY_CANCER_HISTORY));
		
		//NCD metadata starts here
		//Encounter types
		install(encounterType("DM HTN Initial", "Used for collecting Initial encounter information",
		    _EhrEncounterTypes.DM_HTN_INITIAL_ENCOUNTER_TYPE));
		install(encounterType("DM HTN Clinical Follow up", "DM HTN Follow up information",
		    _EhrEncounterTypes.DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE));
		install(encounterType("DM HTN discontinue patient", "DM HTN discontinue patient from care from this facility",
		    _EhrEncounterTypes.NCD_DISCONTINUE_ENCOUNTER_TYPE));
		install(encounterType("DM HTN admit patient", "DM HTN admit patient",
		    _EhrEncounterTypes.NCD_ADMISSION_ENCOUNTER_TYPE));
		install(encounterType("DM HTN foot clinic", "DM HTN foot clinic", _EhrEncounterTypes.NCD_FOOT_CLINIC_ENCOUNTER_TYPE));
		//forms
		install(form("NCD admit Patient", "Admit patient into this facility with NCD related complications",
		    _EhrEncounterTypes.NCD_ADMISSION_ENCOUNTER_TYPE, "1.0", _EhrForms.NCD_ADMISSION));
		install(form("NCD DM HTN FOLLOW UP", "NCD Clinical Follow Up Form",
		    _EhrEncounterTypes.DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE, "1.0", _EhrForms.NCD_FOLLOW_UP));
		install(form("NCD Discontinue Patient", "NCD discontinue patient from care from this facility",
		    _EhrEncounterTypes.DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE, "1.0", _EhrForms.NCD_DISCONTINUE));
		install(form("NCD DM HTN INITIAL", "NCD DM HTN Initial Encounter Form",
		    _EhrEncounterTypes.DM_HTN_INITIAL_ENCOUNTER_TYPE, "1.0", _EhrForms.NCD_INITIAL));
		install(form("NCD Foot clinic", "NCD Foot examination and treatment",
		    _EhrEncounterTypes.NCD_FOOT_CLINIC_ENCOUNTER_TYPE, "1.0", _EhrForms.NCD_FOOT_CLINIC));
		
	}
}
