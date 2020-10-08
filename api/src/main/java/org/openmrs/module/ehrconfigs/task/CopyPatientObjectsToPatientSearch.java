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
package org.openmrs.module.ehrconfigs.task;

import org.apache.commons.lang.StringEscapeUtils;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Timestamp;

import java.util.List;

public class CopyPatientObjectsToPatientSearch extends AbstractTask {
	
	private static final Logger log = LoggerFactory.getLogger(CopyPatientObjectsToPatientSearch.class);
	
	@Override
	public void execute() {
		List<Patient> patientList = Context.getPatientService().getAllPatients();
		if (!isExecuting) {
			if (log.isDebugEnabled()) {
				log.debug("Copying patient object to patient search object to fit the hospital core searching");
			}
			
			startExecuting();
			try {
				//do all the work here
				for (Patient patient : patientList) {
					copyPatientObjectsToPatientSearchObjects(patient);
				}
			}
			catch (Exception e) {
				log.error("Error while copying patients to the respective destination ", e);
			}
			finally {
				stopExecuting();
			}
		}
		
	}
	
	private void copyPatientObjectsToPatientSearchObjects(Patient patient) {
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		PatientSearch patientSearch = new PatientSearch();
		String givenName = "";
		String fullname = "";
		String middleName = "";
		String familyName = "";
		Timestamp birtDate = null;
		PatientIdentifier patientIdentifier;
		if (patient != null && hospitalCoreService.getPatientByPatientId(patient.getPatientId()) == null) {
			log.error("Starting with patient>>" + patient.getPatientId());
			givenName = patient.getGivenName();
			familyName = patient.getFamilyName();
			middleName = patient.getMiddleName();
			fullname = givenName + " " + middleName + " " + familyName;
			birtDate = new Timestamp(patient.getBirthdate().getTime());
			patientIdentifier = patient.getPatientIdentifier();
			
			if (patientIdentifier != null) {
				patientSearch.setPatientId(patient.getPatientId());
				patientSearch.setIdentifier(patientIdentifier.getIdentifier());
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
				//commit the patient object in the patient_search table
				hospitalCoreService.savePatientSearch(patientSearch);
				log.info("Saved patient object successfully>> " + patient);
			}
			
		}
	}
}
