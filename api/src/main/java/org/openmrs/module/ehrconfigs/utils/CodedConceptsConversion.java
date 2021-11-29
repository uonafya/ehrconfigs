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

    public static List<String> convertIntoCodedValues() {
        return Arrays.asList("162810AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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
