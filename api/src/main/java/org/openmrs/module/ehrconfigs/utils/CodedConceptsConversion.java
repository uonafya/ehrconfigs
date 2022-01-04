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
                "1185AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161164AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
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

    private static List<String> addedSetMembersUuidsToUrinalysisOrder() {
        return Arrays.asList(
                "e8710a77-23c0-4a27-bf6b-559ff4d4a0d4",
                "56AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }
    private static List<String> addedSetMembersUuidsToOvaTest() {
        return Arrays.asList(
                "824a7421-210c-4b7a-818f-b82e0b361d95",
                "fc27856d-d8c1-4db3-a7a8-6623dd30a4a7",
                "4d025f49-a660-4f28-b6a1-087101e4f03b",
                "8412007b-201c-49b6-839e-4b7f55102142",
                "925da9ee-f436-445f-ada3-a71b7131222d",
                "6d351243-7792-4498-930a-daf365723573",
                "62eff937-603f-41a0-89ae-098759ce110e",
                "beb0f91d-f2c6-4a04-a480-c04af2cb0945",
                "784f2e5c-068d-4e4c-aa6f-a8026135b600",
                "598673d3-d185-4290-8e09-a68668a36b69",
                "fdef603d-4cef-4eaa-acf3-3d6408110804"
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
    public static void doGeneralSetConversionAndSetup(String conceptSetUuid, List<String> stringList){
        ConceptService conceptService = Context.getConceptService();
        Concept setConcept = conceptService.getConceptByUuid(conceptSetUuid);
        List<Integer> setConceptIdsFromSetMembers = new ArrayList<Integer>();
        List<Integer>  setMembersToBeAdded = new ArrayList<Integer>();
        for(ConceptSet conceptSet1: setConcept.getConceptSets()) {
            setConceptIdsFromSetMembers.add(conceptSet1.getConcept().getConceptId());
        }
        for(String uuids: stringList) {
            Concept partOf = conceptService.getConceptByUuid(uuids);
            if(partOf != null) {
                setMembersToBeAdded.add(partOf.getConceptId());
            }
        }
        //loop through the list to be added vs what is available already
        //if the element is already available, just skip
        for (Integer index : setMembersToBeAdded) {
            if(!setConceptIdsFromSetMembers.contains(index)) {
                setConcept.addSetMember(conceptService.getConcept(index));
                setConcept.setChangedBy(Context.getAuthenticatedUser());
                setConcept.setDateChanged(new Date());
                //save the concept back into the data model
                conceptService.saveConcept(setConcept);
            }
        }

    }
    public static void addSetsToServiceOrderedConcept() {
        doGeneralSetConversionAndSetup("31AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", addedSetMembersUuidsToSrviceOrdered());
    }

    public static void addSetsToDosingUnits() {
        doGeneralSetConversionAndSetup("162384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", addedSetMembersUuidsToDosingUnits());
    }

    public static void addSetsToUrinalysisOrder() {
        doGeneralSetConversionAndSetup("ce5068f1-0ecc-43a8-95c4-cf876dec79bc", addedSetMembersUuidsToUrinalysisOrder());
    }
    public static void addSetsToOvaOrder() {
        doGeneralSetConversionAndSetup("716AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", addedSetMembersUuidsToOvaTest());
    }
}
