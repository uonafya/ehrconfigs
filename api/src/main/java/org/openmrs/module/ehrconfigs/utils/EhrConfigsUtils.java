package org.openmrs.module.ehrconfigs.utils;

import org.openmrs.*;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    public static Map<Integer, List<String>> listMapDiagnosisAndProcedures(List<Obs> obsList){
        HashMap<Integer, List<String>> hashMap = new HashMap<Integer, List<String>>();
        List<String> newList = null;
        Set<Integer> uniqueCodes = new HashSet<Integer>();
        Integer conceptId = null;
        for(Obs obs:obsList) {
            if (obs.getValueCoded() != null) {
                uniqueCodes.add(obs.getValueCoded().getConceptId());
            }
        }
        if (!uniqueCodes.isEmpty()) {
            for (Integer valueId : uniqueCodes) {
                if (!hashMap.containsKey((valueId))) {
                    newList = new ArrayList<String>();
                    conceptId = valueId;
                    for (Obs obs : obsList) {
                        if (obs.getValueCoded() != null && obs.getValueCoded().getConceptId().equals(conceptId)) {
                            newList.add(obs.getValueCoded().getDisplayString());
                        }
                    }
                }
                hashMap.put(conceptId, newList);
            }
        }

        return hashMap;
    }

    public static Map<Integer, List<String>> listTestsAndQuestionsConcepts(List<Obs> obsList) {
        HashMap<Integer, List<String>> hashMap = new HashMap<Integer, List<String>>();
        List<String> newList = null;
        Set<Integer> uniqueCodes = new HashSet<Integer>();
        Integer conceptId = null;
        for(Obs obs:obsList) {
            if (obs.getConcept() != null) {
                uniqueCodes.add(obs.getConcept().getConceptId());
            }
        }
        if (!uniqueCodes.isEmpty()) {
            for (Integer valueId : uniqueCodes) {
                if (!hashMap.containsKey((valueId))) {
                    newList = new ArrayList<String>();
                    conceptId = valueId;
                    for (Obs obs : obsList) {
                        if (obs.getConcept() != null && obs.getConcept().getConceptId().equals(conceptId)) {
                            newList.add(obs.getConcept().getDisplayString());
                        }
                    }
                }
                hashMap.put(conceptId, newList);
            }
        }
        return hashMap;
    }

    public static Map<Integer, List<String>> listMapOfDrugPrescription(List<OpdDrugOrder> opdDrugOrderList){

        HashMap<Integer, List<String>> hashMapOfDrugs = new HashMap<Integer, List<String>>();
        List<String> drugStrings = null;
        Set<Integer> uniqueDrugIds = new HashSet<Integer>();
        Integer key = null;
        for(OpdDrugOrder order : opdDrugOrderList) {
            uniqueDrugIds.add(order.getInventoryDrug().getId());
        }
        if(!uniqueDrugIds.isEmpty()) {
            for (OpdDrugOrder order : opdDrugOrderList) {
                    key = order.getInventoryDrug().getId();
                if (!hashMapOfDrugs.containsKey(key)) {
                    drugStrings = new ArrayList<String>();
                    for (OpdDrugOrder order1 : opdDrugOrderList) {
                        if (order1.getInventoryDrug().getId().equals(key)) {
                            drugStrings.add(order1.getInventoryDrug().getName());
                        }
                    }
                    hashMapOfDrugs.put(key, drugStrings);
                }
            }
        }
        return hashMapOfDrugs;

    }
}
