/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ehrconfigs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ehrconfigs.utils.CodedConceptsConversion;
import org.openmrs.module.ehrconfigs.utils.PatientMigrationTracking;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class EHRConfigurtaionsActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see #started()
	 */
	public void started() {
		//make sure all the concepts have been set properly to the required data types
		runConfigurations();
		log.info("Started EHR Configurtaions");
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutdown EHR Configurtaions");
	}

	private void runConfigurations() {
		//CodedConceptsConversion.doActualConversion();
		//CodedConceptsConversion.addSetsToServiceOrderedConcept();
		//CodedConceptsConversion.addSetsToDosingUnits();
		//CodedConceptsConversion.addSetsToUrinalysisOrder();
		//CodedConceptsConversion.addSetsToOvaOrder();
		//CodedConceptsConversion.changeTestsUnits();
		//CodedConceptsConversion.addSetsToAntenatalCareProfileOrder();
		//CodedConceptsConversion.addSetsToUrinePhysicalExamination();
		//CodedConceptsConversion.addSetsToStoolForOvaandCyst();
		//CodedConceptsConversion.addSetsToUrineMicroscopyDeposits();
		//CodedConceptsConversion.addAnswersToRadiologyDepartment();
		//CodedConceptsConversion.addAnswersToProcedurePerformed();
		//CodedConceptsConversion.addAnswersToTyphoidRDT();
		//CodedConceptsConversion.conceptsToRetireFromDb();
		//CodedConceptsConversion.addAnswersToSalmonellaTyphiRapidDiagnosisTest();
		//CodedConceptsConversion.conceptsToConvertToNaDataType();
		CodedConceptsConversion.unRetireConcepts();
		//CodedConceptsConversion.allowDecimalsInConcepts();
		//       CodedConceptsConversion.addAnswersToHivTest();
		PatientMigrationTracking.updatePatientIds();
		PatientMigrationTracking.updatePatientTriageData();
	}
	
}
