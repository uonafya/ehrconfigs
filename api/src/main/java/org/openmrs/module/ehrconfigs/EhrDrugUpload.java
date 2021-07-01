package org.openmrs.module.ehrconfigs;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsClassLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class EhrDrugUpload {

    public static void uploadDrugs() {
        ConceptService conceptService = Context.getConceptService();
        InputStream drugsPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/drugs.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";
        String name = "";
        String concept = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(drugsPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                name = records[0];
                concept = records[1];
                if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(concept) && conceptService.getDrug(name) == null) {
                    Drug drug = new Drug();
                    drug.setName(name);
                    drug.setConcept(conceptService.getConcept(concept));
                    drug.setCreator(Context.getAuthenticatedUser());
                    drug.setDateCreated(new Date());

                    //save the drug in the data model
                    conceptService.saveDrug(drug);

                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
