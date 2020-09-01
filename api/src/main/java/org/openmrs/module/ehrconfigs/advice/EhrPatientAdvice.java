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
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class EhrPatientAdvice implements MethodBeforeAdvice {
	
	@Override
	public void before(Method method, Object[] args, Object o) throws Throwable {
		if (method.getName().equals("savePatient")) {
			
		}
	}
	
	/**
	 * Invoked before any call to save encounter
	 * 
	 * @param patient the patient
	 */
	protected void beforeSavePatient(Patient patient) {
		if (patient != null) {
			// comapre the patient object and do the required
		}
	}
}
