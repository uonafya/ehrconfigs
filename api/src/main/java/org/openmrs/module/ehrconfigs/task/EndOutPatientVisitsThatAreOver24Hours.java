package org.openmrs.module.ehrconfigs.task;

import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EndOutPatientVisitsThatAreOver24Hours extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(EndOutPatientVisitsThatAreOver24Hours.class);

    /**
     * @see org.openmrs.scheduler.tasks.AbstractTask#execute()
     */
    @Override
    public void execute() {
        if (!isExecuting) {
            log.debug("Starting Auto Close Visits Task for all outpatients visits that are over 24 hours...");

            startExecuting();
            try {
                closeAllVisits();
            } catch (Exception e) {
                log.error("Error while auto closing visits that are over 24 hours:", e);
            } finally {
                stopExecuting();
            }
        }


    }
    private void closeAllVisits() {
        VisitService visitService = Context.getVisitService();
        VisitType outPatient = visitService.getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c");
        List<Visit> getVisits = visitService.getVisits(Arrays.asList(outPatient), null, null, null, null, null, null, null, null, false, false);
        for(Visit visit: getVisits) {

        }
    }
}