package org.openmrs.module.ehrconfigs.utils;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.IdentifierTypes;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.IdentifierNumbersGenerator;
import org.openmrs.module.hospitalcore.model.MigrationTracking;
import org.openmrs.module.hospitalcore.model.MigrationVisitsTracking;
import org.openmrs.module.hospitalcore.model.TriagePatientData;
import org.openmrs.module.hospitalcore.model.TriagePatientQueueLog;
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
import java.math.BigDecimal;
import java.util.Arrays;
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
                    person.setBirthdate(DateUtils.getDateFromString(stripOffTimePartOnString(dob), "MM/dd/yyyy"));
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

        String old_visit_id = "";
        Patient patient = null;
        VisitType visittype = null;
        Date date_started = null;
        Date date_stopped = null;
        //indication_concept_id,
        Location location_id = null;
        User creator = null;
        Date date_created = null;
        //changed_by,
        //date_changed,
        //voided;
        //voided_by,
        //date_voided,
        //void_reason,
        //uuid
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                old_visit_id = records[0];
                patient = getPatient(Integer.parseInt(records[1]));
                visittype = Context.getVisitService().getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c");
                date_started = DateUtils.getDateFromString(records[3], "dd/MM/yyyy hh:mm:ss");
                date_stopped = DateUtils.getDateFromString(records[4], "dd/MM/yyyy hh:mm:ss");
                date_created = DateUtils.getDateFromString(records[8], "dd/MM/yyyy hh:mm:ss");
                location_id = Context.getService(KenyaEmrService.class).getDefaultLocation();
                creator = Context.getAuthenticatedUser();

                //create the visit
                Visit visit = new Visit();
                visit.setVisitType(visittype);
                visit.setPatient(patient);
                visit.setStartDatetime(date_started);
                if(date_stopped != null) {
                    visit.setStartDatetime(date_stopped);
                }
                visit.setCreator(creator);
                visit.setDateCreated(date_created);
                visit.setLocation(location_id);
                //save visit
                Visit savedVisit = Context.getVisitService().saveVisit(visit);

                //populate the model to help track the visits
                MigrationVisitsTracking migrationVisitsTracking = new MigrationVisitsTracking();
                migrationVisitsTracking.setOldVisitId(Integer.valueOf(old_visit_id));
                migrationVisitsTracking.setNewVisitId(savedVisit.getVisitId());
                migrationVisitsTracking.setCreatedBy(creator.getId());
                migrationVisitsTracking.setCreatedOn(new Date());
                //save the tracking model
                Context.getService(HospitalCoreService.class).createMigrationVisitsTrackingDetails(migrationVisitsTracking);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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
        //members to be saved
        String temperature;
        String systolic;
        String daistolic;
        String respiratoryRate;
        String pulsRate;
        Date createdOn;
        String oxygenSaturation;
        String patient;
        String weight;
        String height;
        String mua;
        String chest;
        String abdominal;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);

                weight = stripNullFromString(records[3]);
                height = stripNullFromString(records[4]);
                temperature = stripNullFromString(records[5]);
                systolic = stripNullFromString(records[6]);
                daistolic = stripNullFromString(records[7]);
                respiratoryRate = stripNullFromString(records[8]);
                pulsRate = stripNullFromString(records[9]);
                createdOn = DateUtils.getDateFromString(records[14], "dd/MM/yyyy hh:mm:ss");
                mua = stripNullFromString(records[15]);
                chest = stripNullFromString(records[16]);
                abdominal = stripNullFromString(records[17]);
                oxygenSaturation = stripNullFromString(records[20]);
                patient = stripNullFromString(records[21]);

                //
                TriagePatientData triagePatientData = new TriagePatientData();
                if(createdOn != null) {
                    triagePatientData.setCreatedOn(createdOn);
                }
                if(StringUtils.isNotBlank(weight)) {
                    triagePatientData.setWeight(new BigDecimal(weight));
                }
                if(StringUtils.isNotBlank(height)) {
                    triagePatientData.setHeight(new BigDecimal(height));
                }
                if(StringUtils.isNotBlank(temperature)) {
                    triagePatientData.setTemperature(new BigDecimal(temperature));
                }
                if(StringUtils.isNotBlank(systolic)) {
                    triagePatientData.setSystolic(Integer.valueOf(systolic));
                }
                if(StringUtils.isNotBlank(daistolic)) {
                    triagePatientData.setDaistolic(Integer.valueOf(daistolic));
                }
                if(StringUtils.isNotBlank(respiratoryRate)) {
                    triagePatientData.setRespiratoryRate(Integer.valueOf(respiratoryRate));
                }
                if(StringUtils.isNotBlank(pulsRate)) {
                    triagePatientData.setPulsRate(Integer.valueOf(pulsRate));
                }
                if(StringUtils.isNotBlank(mua)) {
                    triagePatientData.setMua(new BigDecimal(mua));
                }
                if(StringUtils.isNotBlank(chest)) {
                    triagePatientData.setChest(new BigDecimal(chest));
                }
                if(StringUtils.isNotBlank(abdominal)) {
                    triagePatientData.setAbdominal(new BigDecimal(abdominal));
                }
                if(StringUtils.isNotBlank(oxygenSaturation)) {
                    triagePatientData.setOxygenSaturation(Double.valueOf(oxygenSaturation));
                }
                if(StringUtils.isNotBlank(patient)) {
                    triagePatientData.setPatient(getPatient(Integer.parseInt(patient)));
                }

                //save the triage data
                if(triagePatientData.getPatient() != null && triagePatientData.getCreatedOn() != null) {
                    Context.getService(PatientQueueService.class).saveTriagePatientData(triagePatientData);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
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

    static String stripNullFromString(String value){
        String val = "";
        if(!value.equals("NULL")) {
            val = value;
        }
        return val;
    }

    static Patient getPatient(int patientId) {
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

    static Visit
    getVisit(int visitId) {
        HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
        List<MigrationVisitsTracking> migrationVisitTrackingList = hospitalCoreService.getMigrationVisitsTrackingDetails();
        Map<Integer, Integer> getVisitIds = new HashMap<Integer, Integer>();
        if(!migrationVisitTrackingList.isEmpty()) {
            for(MigrationVisitsTracking migrationVisitsTracking: migrationVisitTrackingList) {
                getVisitIds.put(migrationVisitsTracking.getOldVisitId(), migrationVisitsTracking.getNewVisitId());
            }
        }
        Visit visit = null;
        if (!getVisitIds.isEmpty() && getVisitIds.containsKey(visitId)) {
            visit = Context.getVisitService().getVisit(getVisitIds.get(visitId));
        }
        //check if the patientID passed is found in the map, then pick the value to be used instead
        return visit;
    }

    public static void updateAvailableOpdNumbers() {
        InputStream opdNumbersPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_opd_numbers_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

        Patient patient = null;
        String opd_number = "";
        User createdBy = Context.getAuthenticatedUser();
        Date dateCreated = null;
        PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByUuid("61A354CB-4F7F-489A-8BE8-09D0ACEDDC63");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(opdNumbersPath, "UTF-8"));
            String[] records = line.split(cvsSplitBy);
            headLine = br.readLine();
            patient = getPatient(Integer.parseInt(stripNullFromString(records[1])));
            opd_number = records[1];
            dateCreated = DateUtils.getDateFromString(records[3], "dd/MM/yyyy hh:mm:ss");

            //Save patient identifier
            //Check if such patient identifier exist for this patient
            List<PatientIdentifier> patientIdentifierList = Context.getPatientService().getPatientIdentifiers(
                    null,
                    Arrays.asList(identifierType), null, Arrays.asList(patient), false);
            if (patientIdentifierList.isEmpty()) {
                //populate the patient identifier
                PatientService patientService = Context.getPatientService();
                PatientIdentifier opdPatientIdentifier = new PatientIdentifier();
                opdPatientIdentifier.setIdentifierType(identifierType);
                opdPatientIdentifier.setIdentifier(opd_number);
                opdPatientIdentifier.setPatient(patient);
                opdPatientIdentifier.setPreferred(true);
                opdPatientIdentifier.setDateCreated(dateCreated);
                opdPatientIdentifier.setCreator(createdBy);

                //save the patient identifier
                patientService.savePatientIdentifier(opdPatientIdentifier);

                //save the object
                IdentifierNumbersGenerator opdNumbersGenerator = new IdentifierNumbersGenerator();
                opdNumbersGenerator.setPatientId(patient.getPatientId());
                opdNumbersGenerator.setIdentifier(opd_number);
                opdNumbersGenerator.setDateCreated(dateCreated);
                opdNumbersGenerator.setCreatedBy(createdBy.getId());
                opdNumbersGenerator.setIdentifierType(identifierType.getPatientIdentifierTypeId());
                Context.getService(HospitalCoreService.class).saveOpdNumbersGenerator(opdNumbersGenerator);


            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
