package org.openmrs.module.ehrconfigs.utils;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;

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
                "127639AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> addedSetMembersUuids() {
        return Arrays.asList(
                "eb458ded-1fa0-4c1b-92fa-322cada4aff2",
                "0da0f5a5-15f8-4871-ba02-2aa82377223a",
                "160463AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c8cb4aac-be8d-49f1-a98b-2165c3e21ab1",
                "1651AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1651AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c0f775f5-bcc3-4900-a39e-35069b3a08ef"
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
        for(String uuids: addedSetMembersUuids()) {
            serviceOrdered.addSetMember(conceptService.getConceptByUuid(uuids));
            serviceOrdered.setChangedBy(Context.getAuthenticatedUser());
            serviceOrdered.setDateChanged(new Date());
            //save the concept back into the data model
            conceptService.saveConcept(serviceOrdered);
        }

    }
}
