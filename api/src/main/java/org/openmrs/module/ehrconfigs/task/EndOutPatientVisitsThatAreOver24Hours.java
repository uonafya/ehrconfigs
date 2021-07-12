package org.openmrs.module.ehrconfigs.task;

import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

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
                Context.getVisitService().stopVisits(new Date());
            }
            catch (Exception e) {
                log.error("Error while auto closing visits that are over 24 hours:", e);
            }
            finally {
                stopExecuting();
            }
        }
    }
}
