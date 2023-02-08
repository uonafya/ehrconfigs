package org.openmrs.module.ehrconfigs.utils;

import org.openmrs.*;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EhrConfigsUtils {
   public static Provider getProvider(Person person) {
        Provider provider = null;
        ProviderService providerService = Context.getProviderService();
        List<Provider> providerList = new ArrayList<Provider>(providerService.getProvidersByPerson(person));
        if(providerList.size() > 0){
            provider = providerList.get(0);
        }
        return provider;
    }

    public static EncounterRole getDefaultEncounterRole()  {
        return Context.getEncounterService().getEncounterRoleByUuid("a0b03050-c99b-11e0-9572-0800200c9a66");
    }

    public static String getPreferredPatientIdentifier(Patient patient) {
       PatientService service = Context.getPatientService();
        String identifier = "";
        Set<PatientIdentifier> identifierSet = new HashSet<PatientIdentifier>(patient.getIdentifiers());
        for(PatientIdentifier patientIdentifier :identifierSet){
            //check if there is a patient clinic number, use that and exit
            if(patientIdentifier.getIdentifierType().equals(service.getPatientIdentifierTypeByUuid("b4d66522-11fc-45c7-83e3-39a1af21ae0d"))) {
                identifier = patientIdentifier.getIdentifier();
                break;
            }
            //check if there is a UPN
            else if(patientIdentifier.getIdentifierType().equals(service.getPatientIdentifierTypeByUuid("f85081e2-b4be-4e48-b3a4-7994b69bb101"))){
                identifier = patientIdentifier.getIdentifier();
            }
            //use the OpenMRS ID
            else if(patientIdentifier.getIdentifierType().equals(service.getPatientIdentifierTypeByUuid("dfacd928-0370-4315-99d7-6ec1c9f7ae76"))){
                identifier = patientIdentifier.getIdentifier();
            }
            else {
                identifier = patientIdentifier.getIdentifier();
            }
        }

        return identifier;

    }
}
