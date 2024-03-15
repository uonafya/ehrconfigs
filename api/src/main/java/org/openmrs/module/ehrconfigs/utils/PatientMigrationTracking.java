package org.openmrs.module.ehrconfigs.utils;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.MigrationTracking;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.util.OpenmrsClassLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientMigrationTracking {

    public static void updatePatientIds() {
        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_search_new.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

        //person
        String gender = "";
        String dob = "";
        //personName
        String given_name = "";
        String middle_name = "";
        String family_name = "";
        //patient
        Integer oldPatientId = null;
        String mohId = "";
        int track = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                oldPatientId = Integer.valueOf(records[0]);
                mohId = records[1];
                gender = records[6];
                dob = records[7];
                //
                given_name = records[3];
                middle_name = records[4];
                family_name = records[5];
                //start processing the records
                if(!checkIfSimilarPatientExistsBasedOnMohId(mohId)) {
                    //create the person
                    Person person = new Person();
                    person.setGender(gender);
                    person.setBirthdate(DateUtils.getDateFromString(stripOffTimePartOnString(dob), "dd/MM/yyyy"));
                    person.setCreator(Context.getAuthenticatedUser());
                    person.setDateCreated(new Date());
                    person.setDead(false);

                    //create the person name
                    PersonName personName = new PersonName();

                    if(StringUtils.isNotBlank(given_name)) {
                        personName.setGivenName(StringUtils.deleteWhitespace(given_name));
                    }

                    if(StringUtils.isNotBlank(family_name)) {
                        personName.setGivenName(StringUtils.deleteWhitespace(family_name));
                    }
                    if(StringUtils.isNotBlank(middle_name)) {
                        personName.setGivenName(StringUtils.deleteWhitespace(middle_name));
                    }
                    personName.setCreator(Context.getAuthenticatedUser());
                    personName.setDateCreated(new Date());
                    personName.setPerson(person);

                    //Link it to patient identifier type
                    PatientIdentifierType openmrsIdType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.OPENMRS_ID);
                    String generated = Context.getService(IdentifierSourceService.class).generateIdentifier(openmrsIdType, "Registration");
                    //Generate the identifier
                    PatientIdentifier patientIdentifier = new PatientIdentifier();
                    patientIdentifier.setIdentifierType(openmrsIdType);
                    patientIdentifier.setIdentifier(generated);
                    patientIdentifier.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
                    patientIdentifier.setCreator(Context.getAuthenticatedUser());
                    patientIdentifier.setDateCreated(new Date());
                    patientIdentifier.setPreferred(true);


                    //create the patient
                    Patient patient = new Patient();
                    patient.addName(personName);
                    patient.setCreator(Context.getAuthenticatedUser());
                    patient.setDateCreated(new Date());
                    patient.addIdentifier(patientIdentifier);
                    patient.setGender(gender);
                    //save the patient
                    Patient newPatient = Context.getPatientService().savePatient(patient);
                    //record the tracking history
                    MigrationTracking migrationTracking = new MigrationTracking();
                    migrationTracking.setCreatedBy(Context.getAuthenticatedUser().getUserId());
                    migrationTracking.setOldPatientId(oldPatientId);
                    migrationTracking.setNewPatientId(newPatient.getPatientId());
                    migrationTracking.setOpenmrsId(generated);
                    migrationTracking.setCreatedOn(new Date());
                    //save the migration tracking
                    Context.getService(HospitalCoreService.class).createMigrationPatientTrackingDetails(migrationTracking);
                    track++;
                    System.out.println("Saved patient number "+track);

                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void updatePatientVisits() {

        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_visit_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

    }

    public static void updatePatientEncounters() {

        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_encounter_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

    }
    public static void updatePatientTriageData() {

        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_triage_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";
        String triage_log_id;
        /*weight,
        height,
        temperature,
        systolic,
        daistolic,
        respiratory_rate,
        pulse_rate,
        blood_group,
        last_menstrual,
        rhesus_factor,
        pitct,
        created_on,
        mua,chest,
        abdominal,
        encounter_opd,
        BMI,
        oxygen_saturation,
        patient_id*/

        HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
        List<MigrationTracking> migrationTrackingList = hospitalCoreService.getMigrationPatientTrackingDetails();
        Map<Integer, Integer> getPatientIds = new HashMap<Integer, Integer>();
        if(!migrationTrackingList.isEmpty()) {
            for(MigrationTracking migrationTracking: migrationTrackingList) {
                getPatientIds.put(migrationTracking.getOldPatientId(), migrationTracking.getNewPatientId());
            }
        }

    }

    public static void updatePatientObsData() {

        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_obs_1_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

    }
    static boolean checkIfSimilarPatientExistsBasedOnMohId(String mohID) {
        PatientService patientService = Context.getPatientService();
        boolean exist = false;
        if(StringUtils.isNotBlank(mohID)) {
            if(patientService.getPatientIdentifiers(mohID, null, null, null, false).isEmpty() ){

            }
            else {
                System.out.println("The patient exists already");
            }
        }
        return exist;
    }

    static String stripOffTimePartOnString(String str) {
        String output = "";
        if(StringUtils.isNotBlank(str)) {
            String[] splited = str.split("\\s+");
            output = splited[0];
        }
        return output;
    }
}
