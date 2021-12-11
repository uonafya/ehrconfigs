package org.openmrs.module.ehrconfigs.utils;

import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CodedConceptsConversion {

    public static List<String> convertIntoCodedValues() {
        return Arrays.asList(
                "162810AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161166AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "164051AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161199AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "116688AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "148346AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159364AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
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
}
