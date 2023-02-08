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

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrconfigs.utils.EhrConfigsUtils;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.springframework.aop.AfterReturningAdvice;
import java.sql.Timestamp;


public class EhrPatientAdvice implements AfterReturningAdvice {
	
	/**
	 * Invoked before any call to save encounter
	 * 
	 * @param patient the patient
	 */
	protected void afterSavePatient(Patient patient) {
			// comapre the patient object and do the required
			HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
			String givenName = "";
			String fullname = "";
			String middleName = "";
			String familyName = "";
			Timestamp birtDate = null;
			//get if this patient is already registered or NOT
			if (patient != null && hospitalCoreService.getPatientByPatientId(patient.getPatientId()) == null) {
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
			}
	}

	@java.lang.Override
	public void afterReturning(java.lang.Object o, java.lang.reflect.Method method, java.lang.Object[] args, java.lang.Object o1) throws Throwable {

		if (method.getName().equals("savePatient")) { // handles both create and edit patient
			Patient patient = (Patient) args[0];
			afterSavePatient(patient);
		}

	}
}
