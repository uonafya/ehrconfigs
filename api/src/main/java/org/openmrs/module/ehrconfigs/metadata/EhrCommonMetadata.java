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

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.personAttributeType;

@Component
public class EhrCommonMetadata extends AbstractMetadataBundle {
	
	public static final class _EhrPersonAttributeType {
		
		public static final String PAYING_CATEGORY_TYPE = "e191b0b8-f069-11ea-b498-2bfd800847e8";
		
		public static final String NON_PAYING_CATEGORY_TYPE = "0a8ae818-f06a-11ea-ab82-2f183f30d954";
		
		public static final String SPECIAL_CLINIC_CATEGORY_TYPE = "341ee8fa-f06a-11ea-aca0-03d040bd88c8";
		
		public static final String PAYMENT_CATEGORY = "09cd268a-f0f5-11ea-99a8-b3467ddbf779"; // Id 14 in old afyaehms
		
		public static final String FILE_NUMBER = "09cd268a-f0f5-11ea-99a8-b3467ddbf779"; //id 43 in old afyaehms
	}
	
	@Override
	public void install() throws Exception {
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
	}
}
