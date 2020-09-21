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
		
		public static final String FILE_NUMBER = "09cd268a-f0f5-11ea-99a8-b3467ddbf779"; //id 43 in old afyaehms
		
		public static final String WAIVER_NUMBER = "a804c03e-f1bc-11ea-ae43-dfa0f52ad887"; //id in old afyaehms is 32
		
		public static final String EXEMPTION_NUMBER = "a22892ce-f1d2-11ea-a512-138a123d324a"; //id in old afyaehms is 36
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
		
		////////
		public static final String PNC_TRIAGE_ENCOUNTER_TYPE = "fcaec384-fac5-11ea-8aeb-fffc453fcc77";
		
		public static final String CWC_ENCOUNTER_TYPE = "09073c56-fac6-11ea-87aa-bfe01fd142fd"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String CWC_TRIAGE_ENCOUNTER_TYPE = "150932f2-fac6-11ea-b158-4b1b80bfe51c";
		
		public static final String ANC_TRIAGE_ENCOUNTER_TYPE = "25dbb7d0-fac6-11ea-94e8-0bd56324f4b5";
	}
	
	public static final class _EhrPrograms {
		
		public static final String ANC_PROGRAM = "e8751e5c-fbda-11ea-9bba-ff7e8cea17d3"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String ANC_PROGRAM_CONCEPT = "160446AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String PNC_PROGRAM = "23edfbca-fbdb-11ea-a675-17377ca3079e"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String CWC_PROGRAM = "645d7e4c-fbdb-11ea-911a-5fe00fc87a47"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String CWC_PROGRAM_CONCEPT = "163110AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
		
		public static final String PNC_PROGRAM_CONCEPT = "1623AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"; //check this in the kenyaEMR server if the uuid exists, do not install but reuse it
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
	}
}
