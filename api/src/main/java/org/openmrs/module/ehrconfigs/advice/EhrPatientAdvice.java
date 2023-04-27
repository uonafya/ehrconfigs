/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.ehrconfigs.advice;

import org.openmrs.*;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PersonService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrconfigs.metadata.EhrCommonMetadata;
import org.openmrs.module.ehrconfigs.utils.EhrConfigsUtils;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.springframework.aop.AfterReturningAdvice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class EhrPatientAdvice implements AfterReturningAdvice {

	/**
	 * Invoked before any call to save encounter
	 *
	 * @param patient the patient
	 */
	protected void afterSavePatient(Patient patient) {
		//add a person attribute to be used for searching patients directly from the walk in queues
		PersonService personService = Context.getPersonService();
		PersonAttributeType patientRegistered = personService.getPersonAttributeTypeByUuid("d93b0954-b8d6-11ed-bc05-57dd7af60c15");

		PersonAttribute personAttribute = new PersonAttribute();


		// compare the patient object and do the required
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		String givenName = "";
		String fullname = "";
		String middleName = "";
		String familyName = "";
		Timestamp birtDate = null;
		//get if this patient is already registered or NOT
		if (patient != null && hospitalCoreService.getPatientByPatientId(patient.getPatientId()) == null) {

			personAttribute.setAttributeType(patientRegistered);
			personAttribute.setPerson(patient);
			personAttribute.setValue("Registration");
			personAttribute.setCreator(Context.getAuthenticatedUser());
			personAttribute.setDateCreated(new Date());
			patient.addAttribute(personAttribute);
			personService.savePerson(patient);

			givenName = patient.getGivenName();
			familyName = patient.getFamilyName();
			if (patient.getMiddleName() != null) {
				middleName = patient.getMiddleName();
			}
			fullname = givenName + " " + middleName + " " + familyName;
			birtDate = new Timestamp(patient.getBirthdate().getTime());

			PatientSearch patientSearch = new PatientSearch();
			patientSearch.setPatientId(patient.getPatientId());
			patientSearch.setIdentifier(EhrConfigsUtils.getPreferredPatientIdentifier(patient));
			patientSearch.setFullname(fullname);
			patientSearch.setGivenName(givenName);
			patientSearch.setMiddleName(middleName);
			patientSearch.setFamilyName(familyName);
			patientSearch.setGender(patient.getGender());
			patientSearch.setBirthdate(birtDate);
			patientSearch.setAge(patient.getAge());
			patientSearch.setPersonNameId(patient.getPersonName().getPersonNameId());
			patientSearch.setDead(false);
			patientSearch.setAdmitted(false);
			//save the search in the database
			hospitalCoreService.savePatientSearch(patientSearch);
			//create a visit for this patient as an outpatient

			EncounterService encounterService = Context.getEncounterService();
			Encounter encounter = new Encounter();
			encounter.setEncounterType(encounterService.getEncounterTypeByUuid("de1f9d67-b73e-4e1b-90d0-036166fc6995"));
			encounter.addProvider(EhrConfigsUtils.getDefaultEncounterRole(), EhrConfigsUtils.getProvider(Context.getAuthenticatedUser().getPerson()));
			encounter.setEncounterDatetime(new Date());
			encounter.setCreator(Context.getAuthenticatedUser());
			encounter.setDateCreated(new Date());
			encounter.setPatient(patient);
			encounter.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());

			VisitService visitService = Context.getVisitService();
			Visit visit = new Visit();
			visit.setPatient(patient);
			visit.setStartDatetime(new Date());
			visit.setVisitType(visitService.getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c"));
			visit.setDateCreated(new Date());
			visit.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
			visit.setCreator(Context.getAuthenticatedUser());

			//save the visit in the database to be used on encounters
			Visit savedVisit = visitService.saveVisit(visit);
			//attach the visit to the encounter now
			encounter.setVisit(savedVisit);
			//save the encounter so that this patient can have a visit
			encounterService.saveEncounter(encounter);
		}
	}

	protected void afterSaveVisit(Visit visit) {
		if(visit != null) {
			Patient patient = visit.getPatient();
			Set<Encounter> encounterSet = visit.getEncounters();
			HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
			String initialEncounterType = EhrCommonMetadata._EhrEncounterTypes.REGINITIAL;
			String revisitEncounterType = EhrCommonMetadata._EhrEncounterTypes.REGREVISIT;
			List<Encounter> visitEncounters = new ArrayList<Encounter>();

			if (patient != null && hospitalCoreService.getPatientByPatientId(patient.getPatientId()) == null && encounterSet != null) {
				//pick the encounter to make sure it is NOT being supplied by the initial patient queue application
				for(Encounter encounter : encounterSet) {
					if(encounter.getEncounterType().getUuid().equals(initialEncounterType) || encounter.getEncounterType().getUuid().equals(revisitEncounterType)) {
						visitEncounters.add(encounter);
					}
				}
				//check if we have such encounters already saved
				if(visitEncounters.isEmpty()) {
					PersonService personService = Context.getPersonService();
					PersonAttributeType patientRegistered = personService.getPersonAttributeTypeByUuid("d93b0954-b8d6-11ed-bc05-57dd7af60c15");

					PersonAttribute personAttribute = new PersonAttribute();


					// compare the patient object and do the required
					String givenName = "";
					String fullname = "";
					String middleName = "";
					String familyName = "";
					Timestamp birtDate = null;
					personAttribute.setAttributeType(patientRegistered);
					personAttribute.setPerson(patient);
					personAttribute.setValue("Registration");
					personAttribute.setCreator(Context.getAuthenticatedUser());
					personAttribute.setDateCreated(new Date());
					patient.addAttribute(personAttribute);
					personService.savePerson(patient);

					givenName = patient.getGivenName();
					familyName = patient.getFamilyName();
					if (patient.getMiddleName() != null) {
						middleName = patient.getMiddleName();
					}
					fullname = givenName + " " + middleName + " " + familyName;
					birtDate = new Timestamp(patient.getBirthdate().getTime());

					PatientSearch patientSearch = new PatientSearch();
					patientSearch.setPatientId(patient.getPatientId());
					patientSearch.setIdentifier(EhrConfigsUtils.getPreferredPatientIdentifier(patient));
					patientSearch.setFullname(fullname);
					patientSearch.setGivenName(givenName);
					patientSearch.setMiddleName(middleName);
					patientSearch.setFamilyName(familyName);
					patientSearch.setGender(patient.getGender());
					patientSearch.setBirthdate(birtDate);
					patientSearch.setAge(patient.getAge());
					patientSearch.setPersonNameId(patient.getPersonName().getPersonNameId());
					patientSearch.setDead(false);
					patientSearch.setAdmitted(false);
					//save the search in the database
					hospitalCoreService.savePatientSearch(patientSearch);
					//create a visit for this patient as an outpatient

					EncounterService encounterService = Context.getEncounterService();
					Encounter encounter = new Encounter();
					encounter.setEncounterType(encounterService.getEncounterTypeByUuid("de1f9d67-b73e-4e1b-90d0-036166fc6995"));
					encounter.addProvider(EhrConfigsUtils.getDefaultEncounterRole(), EhrConfigsUtils.getProvider(Context.getAuthenticatedUser().getPerson()));
					encounter.setEncounterDatetime(new Date());
					encounter.setCreator(Context.getAuthenticatedUser());
					encounter.setDateCreated(new Date());
					encounter.setPatient(patient);
					encounter.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
					encounter.setVisit(visit);
					//save the encounter so that this patient can have a visit
					encounterService.saveEncounter(encounter);
				}

			}
		}
	}

	@java.lang.Override
	public void afterReturning(java.lang.Object o, java.lang.reflect.Method method, java.lang.Object[] args, java.lang.Object o1) throws Throwable {

		String globalProperty = Context.getAdministrationService().getGlobalProperty("ehrconfigs.autoCheckIn");

		if (method.getName().equals("savePatient") && globalProperty.equals("true")) { // handles both create and edit patient
			Patient patient = (Patient) args[0];
			afterSavePatient(patient);
		}
		//check for encounter type
		//check if the patient is NOT in patient search
		if(method.getName().equals("saveVisit")) {
			Visit visit = (Visit) args[0];
			afterSaveVisit(visit);
		}

	}
}
