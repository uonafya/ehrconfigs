package org.openmrs.module.ehrconfigs.task;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.openmrs.util.OpenmrsClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class CopyDrugsTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(CopyDrugsTask.class);

    @Override
    public void execute() {
        if (!isExecuting) {
            if (log.isDebugEnabled()) {
                log.debug("Copying the drugs to the openmrs data model");
            }

            startExecuting();
            try {
                uploadDrugs();
            }
            catch (Exception e) {
                log.error("Error while copying drugs ", e);
            }
            finally {
                stopExecuting();
            }
        }

    }

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
                if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(concept.trim()) && conceptService.getDrug(name) == null) {
                    Drug drug = new Drug();
                    drug.setName(name);
                    drug.setConcept(conceptService.getConcept(concept.trim()));
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
