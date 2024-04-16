package org.openmrs.module.ehrconfigs.task;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.MigrationTracking;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.openmrs.util.OpenmrsClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.ehrconfigs.utils.PatientMigrationTracking.stripOffTimePartOnString;

public class AmendDobTask extends AbstractTask {
    private static final Logger log = LoggerFactory.getLogger(AmendDobTask.class);
    @Override
    public void execute() {
        if (!isExecuting) {
            if (log.isDebugEnabled()) {
                log.debug("Start amending the DOB");
            }

            startExecuting();
            try {
                updateBirthDateForPatient();
            }
            catch (Exception e) {
                log.error("Error while amending the DOB ", e);
            }
            finally {
                stopExecuting();
            }
        }

    }

    public void updateBirthDateForPatient() {
        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_search_langalanga_clean.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

        //patient
        Integer oldPatientId = null;
        String dob = "";
        int track = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                oldPatientId = Integer.valueOf(records[0]);
                dob = records[7];
                Patient patient = getPatient(oldPatientId);
                if(patient != null && StringUtils.isNotBlank(dob)) {
                    patient.setBirthdate((DateUtils.getDateFromString(stripOffTimePartOnString(dob), "yyyy-MM-dd")));
                    patient.setChangedBy(Context.getAuthenticatedUser());
                    patient.setDateChanged(new Date());

                    //update the patient
                    Context.getPatientService().savePatient(patient);
                    track++;
                }
                System.out.println("So far >>"+track+">>Patient corrected");
                if(track == 200) {
                    Context.clearSession();
                    Context.flushSession();
                }

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    Patient getPatient(int patientId) {
        HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
        List<MigrationTracking> migrationTrackingList = hospitalCoreService.getMigrationPatientTrackingDetails();
        Map<Integer, Integer> getPatientIds = new HashMap<Integer, Integer>();
        if(!migrationTrackingList.isEmpty()) {
            for(MigrationTracking migrationTracking: migrationTrackingList) {
                getPatientIds.put(migrationTracking.getOldPatientId(), migrationTracking.getNewPatientId());
            }
        }
        Patient patient = null;
        if (!getPatientIds.isEmpty() && getPatientIds.containsKey(patientId)) {
            patient = Context.getPatientService().getPatient(getPatientIds.get(patientId));
        }
        //check if the patientID passed is found in the map, then pick the value to be used instead
        return patient;
    }
}
