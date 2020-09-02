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

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CopyPatientObjectsToPatientSearch extends AbstractTask {
	
	private static final Logger log = LoggerFactory.getLogger(CopyPatientObjectsToPatientSearch.class);
	
	@Override
	public void execute() {
		if (!isExecuting) {
			if (log.isDebugEnabled()) {
				log.debug("Copying patient object to patient search object to fit the hospital core searching");
			}
			
			startExecuting();
			try {
				//do all the work here
				copyPatientObjectsToPatientSearchObjects();
			}
			catch (Exception e) {
				log.error("Error while copying patients to the respective destination ", e);
			}
			finally {
				stopExecuting();
			}
		}
		
	}
	
	private void copyPatientObjectsToPatientSearchObjects() {
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		PatientSearch patientSearch = new PatientSearch();
		PatientService patientService = Context.getPatientService();
		List<Patient> patientList = patientService.getAllPatients();
		for (Patient patient : patientList) {
			if (hospitalCoreService.getPatientByPatientId(patient.getPatientId()) == null) {
				patientSearch.setPatientId(patient.getPatientId());
				patientSearch.setIdentifier(patient.getPatientIdentifier().getIdentifier());
				patientSearch.setFullname(patient.getGivenName()+" "+patient.getFamilyName());
				patientSearch.setGivenName(patient.getGivenName());
				patientSearch.setMiddleName(patient.getMiddleName());
				patientSearch.setFamilyName(patient.getFamilyName());
				patientSearch.setGender(patient.getGender());
				patientSearch.setBirthdate(patient.getBirthdate());
				patientSearch.setAge(patient.getAge());
				patientSearch.setPersonNameId(patient.getPersonName().getPersonNameId());
				patientSearch.setDead(false);
				patientSearch.setAdmitted(false);
				//commit the patient object in the patient_search table
				hospitalCoreService.savePatientSearch(patientSearch);
			}
		}
	}
}
