package org.openmrs.module.ehrconfigs.utils;

import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptSet;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CodedConceptsConversion {

    private static List<String> convertIntoCodedValues() {
        return Arrays.asList(
                "162810AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161166AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "164051AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161199AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "116688AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "148346AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159364AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160463AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160463AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "112930AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1482AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "142452AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "127639AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "e799649c-6508-4660-9d9d-4df3449df20e",
                "1185AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> addedSetMembersUuidsToSrviceOrdered() {
        return Arrays.asList(
                "eb458ded-1fa0-4c1b-92fa-322cada4aff2",
                "0da0f5a5-15f8-4871-ba02-2aa82377223a",
                "160463AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c8cb4aac-be8d-49f1-a98b-2165c3e21ab1",
                "1651AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c0f775f5-bcc3-4900-a39e-35069b3a08ef"
        );
    }

    private static List<String> addedSetMembersUuidsToDosingUnits() {
        return Arrays.asList(
                "162335AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162376AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162351AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162354AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162355AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162356AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162357AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162358AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162359AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161554AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162360AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162362AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162363AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162364AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162365AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162366AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161553AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162263AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162371AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162372AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162374AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162375AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1513AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162378AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162379AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162380AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    public static void doActualConversion() {
        ConceptService conceptService = Context.getConceptService();
        for(String string: convertIntoCodedValues()) {
            Concept concept = conceptService.getConceptByUuid(string);
            concept.setDatatype(conceptService.getConceptDatatypeByUuid(ConceptDatatype.CODED_UUID));
            concept.setChangedBy(Context.getAuthenticatedUser());
            concept.setDateChanged(new Date());
            //save the concept back into the data model
            conceptService.saveConcept(concept);
        }
    }
    public static void addSetsToServiceOrderedConcept() {
        ConceptService conceptService = Context.getConceptService();
        Concept serviceOrdered = conceptService.getConceptByUuid("31AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        List<Integer> setConceptIdsFromSetMembers = new ArrayList<Integer>();
        List<Integer>  setMembersToBeAdded = new ArrayList<Integer>();
        for(ConceptSet conceptSet1: serviceOrdered.getConceptSets()) {
            setConceptIdsFromSetMembers.add(conceptSet1.getConcept().getConceptId());
        }
        for(String uuids: addedSetMembersUuidsToSrviceOrdered()) {
            Concept partOf = conceptService.getConceptByUuid(uuids);
            if(partOf != null) {
                setMembersToBeAdded.add(partOf.getConceptId());
            }
        }
        //loop through the list to be added vs what is available already
        //if the element is already available, just skip
        for (Integer index : setMembersToBeAdded) {
            if(!setConceptIdsFromSetMembers.contains(index)) {
                serviceOrdered.addSetMember(conceptService.getConcept(index));
                serviceOrdered.setChangedBy(Context.getAuthenticatedUser());
                serviceOrdered.setDateChanged(new Date());
                //save the concept back into the data model
                conceptService.saveConcept(serviceOrdered);
            }
        }

    }

    public static void addSetsToDosingUnits() {
        ConceptService conceptService = Context.getConceptService();
        Concept dosingUnits = conceptService.getConceptByUuid("162384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        List<Integer> setConceptIdsFromSetMembers = new ArrayList<Integer>();
        List<Integer>  setMembersToBeAdded = new ArrayList<Integer>();
        for(ConceptSet conceptSet1: dosingUnits.getConceptSets()) {
            setConceptIdsFromSetMembers.add(conceptSet1.getConcept().getConceptId());
        }
        for(String uuids: addedSetMembersUuidsToDosingUnits()) {
            Concept partOf = conceptService.getConceptByUuid(uuids);
            if(partOf != null) {
                setMembersToBeAdded.add(partOf.getConceptId());
            }
        }
        //loop through the list to be added vs what is available already
        //if the element is already available, just skip
        for (Integer index : setMembersToBeAdded) {
            if(!setConceptIdsFromSetMembers.contains(index)) {
                dosingUnits.addSetMember(conceptService.getConcept(index));
                dosingUnits.setChangedBy(Context.getAuthenticatedUser());
                dosingUnits.setDateChanged(new Date());
                //save the concept back into the data model
                conceptService.saveConcept(dosingUnits);
            }
        }
    }
}
