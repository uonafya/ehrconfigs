package org.openmrs.module.ehrconfigs.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openmrs.CareSetting;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.TestOrder;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.OrderContext;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.PatientQueueService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.module.hospitalcore.model.IdentifierNumbersGenerator;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.MigrationEncounterTracking;
import org.openmrs.module.hospitalcore.model.MigrationObsTracking;
import org.openmrs.module.hospitalcore.model.MigrationOrders;
import org.openmrs.module.hospitalcore.model.MigrationTracking;
import org.openmrs.module.hospitalcore.model.MigrationVisitsTracking;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.TriagePatientData;
import org.openmrs.module.hospitalcore.util.DateUtils;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
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
                        personName.setFamilyName(StringUtils.deleteWhitespace(family_name));
                    }
                    if(StringUtils.isNotBlank(middle_name)) {
                        personName.setMiddleName(StringUtils.deleteWhitespace(middle_name));
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

                    if(Context.getService(HospitalCoreService.class).getMigrationPatientTrackingDetailsByOldPatientId(oldPatientId) == null) {
                        //create the patient
                        Patient patient = new Patient();
                        patient.addName(personName);
                        patient.setCreator(Context.getAuthenticatedUser());
                        patient.setDateCreated(new Date());
                        patient.addIdentifier(patientIdentifier);
                        patient.setGender(gender);
                        patient.setBirthdate(DateUtils.getDateFromString(stripOffTimePartOnString(dob), "MM/dd/yyyy"));
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
                    }
                    if(track == 100) {
                        Context.clearSession();
                        Context.flushSession();
                    }

                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void updatePatientVisits() {
        int start = 0;
        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_visit_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

        String old_visit_id = "";
        String patient = "";
        VisitType visittype = null;
        String date_started = "";
        String date_stopped = "";
        //indication_concept_id,
        Location location_id = null;
        User creator = null;
        String  date_created = "";
        //changed_by,
        //date_changed,
        //voided;
        //voided_by,
        //date_voided,
        //void_reason,
        //uuid
        Date date_started_1 = null;
        Date date_stopped_1 = null;
        Date date_created_1 = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                old_visit_id = records[0];
                patient = records[1];
                Patient patient_1 = getPatient(Integer.parseInt(patient));
                visittype = Context.getVisitService().getVisitTypeByUuid("3371a4d4-f66f-4454-a86d-92c7b3da990c");
                date_started = getSafeString(records[3]);
                date_started_1 =  DateUtils.getDateFromString(date_started, "yyyy-MM-dd HH:mm:ss");
                date_stopped = getSafeString(records[4]);
                date_stopped_1 = DateUtils.getDateFromString(date_stopped, "yyyy-MM-dd HH:mm:ss");

                date_created = getSafeString(records[8]);
                date_created_1 =  DateUtils.getDateFromString(date_created, "yyyy-MM-dd HH:mm:ss");
                location_id = Context.getService(KenyaEmrService.class).getDefaultLocation();
                creator = Context.getAuthenticatedUser();
                if(Context.getService(HospitalCoreService.class).getMigrationVisitsTrackingDetailsByOldVisitId(Integer.valueOf(old_visit_id)) == null && patient_1 !=null) {
                    //create the visit
                    Visit visit = new Visit();
                    visit.setVisitType(visittype);
                    visit.setPatient(patient_1);
                    visit.setStartDatetime(date_started_1);
                    if (date_stopped_1 != null) {
                        visit.setStopDatetime(date_stopped_1);
                    }
                    visit.setCreator(creator);
                    visit.setDateCreated(date_created_1);
                    visit.setLocation(location_id);
                    //save visit
                    Visit savedVisit = Context.getVisitService().saveVisit(visit);
                        start++;

                    //populate the model to help track the visits
                    MigrationVisitsTracking migrationVisitsTracking = new MigrationVisitsTracking();
                    migrationVisitsTracking.setOldVisitId(Integer.valueOf(old_visit_id));
                    migrationVisitsTracking.setNewVisitId(savedVisit.getVisitId());
                    migrationVisitsTracking.setCreatedBy(creator.getId());
                    migrationVisitsTracking.setCreatedOn(new Date());
                    //save the tracking model
                    Context.getService(HospitalCoreService.class).createMigrationVisitsTrackingDetails(migrationVisitsTracking);
                }
                System.out.println("Total of >>"+start+" visits done");
                if(start == 100) {
                    Context.clearSession();
                    Context.flushSession();
                }
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
        int old_encounter_id;
        String  encounter_type = "";
        Patient patient = null;
        Location location = Context.getService(KenyaEmrService.class).getDefaultLocation();
        //form_id;
        Date encounter_datetime = null;
        User creator = Context.getAuthenticatedUser();
        Date date_created = null;
        int count = 0;
        //voided;
        //voided_by;
        //date_voided;
        //void_reason;
        //changed_by;
        //date_changed;
        Visit visit_id = null;
        //uuid;
        String encounter_datetime_1 = "";
        String date_created_1 = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                old_encounter_id = Integer.parseInt(records[0]);
                encounter_type = records[1];
                EncounterType encounter_type_1 = getEncounterType(Integer.parseInt(encounter_type));
                patient = getPatient(Integer.parseInt(records[2]));
                encounter_datetime_1 = getSafeString(records[5]);
                date_created_1 = getSafeString(records[7]);
                encounter_datetime = DateUtils.getDateFromString(encounter_datetime_1, "yyyy-MM-dd HH:mm:ss");
                date_created = DateUtils.getDateFromString(date_created_1, "yyyy-MM-dd HH:mm:ss");
                String visit_id_str = records[14];
                if(visit_id_str.matches("^-?\\d+$")) {
                    visit_id = getVisit(Integer.parseInt(visit_id_str));
                }
                else {
                    visit_id = null;
                }
                if(Context.getService(HospitalCoreService.class).getMigrationEncounterTrackingDetailsByOldEncounterID(old_encounter_id) == null && patient !=null && encounter_type_1 != null) {
                    Encounter encounter = new Encounter();
                    if(visit_id != null && visit_id.getPatient().equals(patient)) {
                        encounter.setEncounterDatetime(visit_id.getStartDatetime());
                        encounter.setVisit(visit_id);
                    }
                    else {
                        encounter.setEncounterDatetime(encounter_datetime);
                    }
                    encounter.setPatient(patient);
                    encounter.setDateCreated(date_created);
                    encounter.setEncounterType(encounter_type_1);
                    encounter.setCreator(creator);
                    encounter.setLocation(location);
                    encounter.setProvider(EhrConfigsUtils.getDefaultEncounterRole(),
                            EhrConfigsUtils.getProvider(Context.getAuthenticatedUser().getPerson()));
                    //save the encounter in the database
                    Encounter savedEncounter = Context.getEncounterService().saveEncounter(encounter);
                    //save an object that tracks encounters
                    MigrationEncounterTracking migrationEncounterTracking = new MigrationEncounterTracking();
                    migrationEncounterTracking.setOldEncounterId(old_encounter_id);
                    migrationEncounterTracking.setNewEncounterId(savedEncounter.getEncounterId());
                    migrationEncounterTracking.setCreatedOn(new Date());
                    //Save the object
                    Context.getService(HospitalCoreService.class).createMigrationEncounterTrackingDetails(migrationEncounterTracking);
                    count++;
                }
                System.out.println("The encounter are >>"+count);
                if(count == 100) {
                    Context.clearSession();
                    Context.flushSession();
                }

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void updateOpdTestOrders() {
        PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);
        List<OpdTestOrder> opdTestOrderList = patientDashboardService.getAllOpdTestOrder();
        int count = 0;
        Encounter encounterToUse = null;
        Patient patientToUse = null;
        for (OpdTestOrder opdTestOrder : opdTestOrderList) {
            Patient patientFromCurrent = opdTestOrder.getPatient();
            Encounter encounterFromCurrent = opdTestOrder.getEncounter();

            if (patientFromCurrent != null) {
                patientToUse = getPatient(patientFromCurrent.getPatientId());
            }
            if (encounterFromCurrent != null) {
                encounterToUse = getEncounter(encounterFromCurrent.getEncounterId());
            }
            opdTestOrder.setPatient(patientToUse);
            opdTestOrder.setEncounter(encounterToUse);
            opdTestOrder.setCreator(Context.getAuthenticatedUser());

            //update the record
            patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);
            System.out.println("Updated TEST ORDER>>" + count++);

        }
    }
    public static void updateOpdDrugOrder() {
        PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);
        List<OpdDrugOrder> opdDrugOrderList = patientDashboardService.getAllOpdDrugOrder();
        Encounter encounterToUse = null;
        Patient patientToUse = null;
        int count = 0;
        for(OpdDrugOrder opdDrugOrder : opdDrugOrderList) {
            Patient patientFromCurrent = opdDrugOrder.getPatient();
            Encounter encounterFromCurrent = opdDrugOrder.getEncounter();

            if(patientFromCurrent != null){
                patientToUse = getPatient(patientFromCurrent.getPatientId());
            }
            if(encounterFromCurrent != null) {
                encounterToUse = getEncounter(encounterFromCurrent.getEncounterId());
            }
            opdDrugOrder.setPatient(patientToUse);
            opdDrugOrder.setEncounter(encounterToUse);
            opdDrugOrder.setCreator(Context.getAuthenticatedUser());

            //update the record
            patientDashboardService.saveOrUpdateOpdDrugOrder(opdDrugOrder);
            System.out.println("Updated DRUG ORDER>>"+count++);

        }
    }
    public static void updatePatientBillingServices() {
        BillingService billingService = Context.getService(BillingService.class);
        List<PatientServiceBill> patientServiceBillList = billingService.getAllPatientServiceBill();
        Encounter encounterToUse = null;
        Patient patientToUse = null;
        int count = 0;
        for(PatientServiceBill patientServiceBill : patientServiceBillList) {
            Patient patientFromCurrent = patientServiceBill.getPatient();
            Encounter encounterFromCurrent = patientServiceBill.getEncounter();

            if(patientFromCurrent != null){
                patientToUse = getPatient(patientFromCurrent.getPatientId());
            }
            if(encounterFromCurrent != null) {
                encounterToUse = getEncounter(encounterFromCurrent.getEncounterId());
            }
            patientServiceBill.setPatient(patientToUse);
            patientServiceBill.setEncounter(encounterToUse);
            patientServiceBill.setCreator(Context.getAuthenticatedUser());

            //update the record
            billingService.savePatientServiceBill(patientServiceBill);
            System.out.println("Updated Service BILL>>"+count++);

        }

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
        String  createdOn = "";
        Date convertedCreatedOn = null;
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

                weight = stripNullFromString(records[2]);
                height = stripNullFromString(records[3]);
                temperature = stripNullFromString(records[4]);
                systolic = stripNullFromString(records[5]);
                daistolic = stripNullFromString(records[6]);
                respiratoryRate = stripNullFromString(records[7]);
                pulsRate = stripNullFromString(records[8]);
                createdOn = getSafeString(records[13]);
                convertedCreatedOn = DateUtils.getDateFromString(createdOn, "yyyy-MM-dd HH:mm:ss");
                mua = stripNullFromString(records[14]);
                chest = stripNullFromString(records[15]);
                abdominal = stripNullFromString(records[16]);
                oxygenSaturation = stripNullFromString(records[19]);
                patient = stripNullFromString(records[20]);

                //
                TriagePatientData triagePatientData = new TriagePatientData();
                if(createdOn != null) {
                    triagePatientData.setCreatedOn(convertedCreatedOn);
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

        InputStream obsPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/obs_new_1.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

        Integer old_obs_id = null;//0
        Patient  person_id = null;//1
        Concept concept_id = null;//2
        String encounter_id;//3
        String order_id;//4
        String obs_datetime = "";//5
        Location location_id = Context.getService(KenyaEmrService.class).getDefaultLocation();//6
        //obs_group_id,//7
        //String accession_number = "";//8
        //value_group_id, //9
        String value_coded; //10
        //Integer value_coded_name_id = null; //11
        //String value_drug = null; //12
        String value_datetime = null; //13
        String value_numeric;//14
        //String value_modifier = "";//15
        String value_text = ""; //16
        //String value_complex = "";//17
        String comments = "";//18
        //creator,//19
        String date_created = "";//20
        //voided,//21
        //voided_by,//22
        //date_voided,//23
        //void_reason,//24
        //uuid,//25
        //previous_version,//26
        //form_namespace_and_path,//27
        String status = "";//28
        String interpretation = "";//29
        int obsCount = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(obsPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                    old_obs_id = Integer.valueOf(records[0]);
                    //get into the observations populating
                    person_id = getPatient(Integer.parseInt(records[1]));
                    concept_id = Context.getConceptService().getConcept(records[2]);
                    encounter_id = getSafeString(records[3]);
                    Encounter encounter = null;
                    if(encounter_id.matches("^-?\\d+$")) {
                        encounter = getEncounter(Integer.parseInt(encounter_id));
                    }
                    order_id = getSafeString(records[4]);
                    Order order = null;
                    if(order_id.matches("^-?\\d+$")) {
                        order = getOrders(Integer.parseInt(order_id));
                    }
                    obs_datetime = getSafeString(records[5]);
                    Date obs_datetime_converted =  null;
                    if(!obs_datetime.equals("NULL")){
                        obs_datetime_converted = DateUtils.getDateFromString(obs_datetime, "yyyy-MM-dd HH:mm:ss");
                    }
                    value_coded = getSafeString(records[10]);
                    Concept valueCoded = null;
                    if(value_coded.matches("^-?\\d+$")) {
                        valueCoded = Context.getConceptService().getConcept(value_coded);
                    }
                    value_datetime = getSafeString(records[13]);
                    Date value_datetime_converted = null;
                    if(!value_datetime.equals("NULL")) {
                        value_datetime_converted = DateUtils.getDateFromString(value_datetime, "yyyy-MM-dd HH:mm:ss");
                    }
                    value_numeric = getSafeString(records[14]);
                    Double valueNumeric = null;
                    if(value_numeric.matches("^-?\\d+(\\.\\d+)?$")) {
                        valueNumeric = Double.valueOf(value_numeric);
                    }
                    System.out.println("The value Numeric is outside >>"+valueNumeric);
                    value_text = records[16];
                    comments = records[18];
                    date_created = getSafeString(records[20]);
                    Date date_created_converted = DateUtils.getDateFromString(date_created, "yyyy-MM-dd HH:mm:ss");
                    status = records[28];

                    //construct an observation row
                if(person_id != null) {
                            Obs obs = new Obs();
                            obs.setPerson(person_id);
                            obs.setConcept(concept_id);
                            if (encounter != null) {
                                obs.setEncounter(encounter);
                                obs.setObsDatetime(encounter.getEncounterDatetime());
                            } else {
                                obs.setEncounter(encounter);
                                obs.setObsDatetime(obs_datetime_converted);
                            }
                            if (order != null) {
                                obs.setOrder(order);
                            } else {
                                obs.setOrder(order);
                            }
                            if (valueCoded != null) {
                                obs.setValueCoded(valueCoded);
                            }
                            if (value_datetime_converted != null) {
                                obs.setValueDatetime(value_datetime_converted);
                            }
                            if (valueNumeric != null) {
                                //System.out.println("The value Numeric is INSIDE >>"+valueNumeric);
                                obs.setValueNumeric(valueNumeric);
                            }
                            else {
                                obs.setValueNumeric(null);
                            }
                            if (StringUtils.isNotBlank(comments)) {
                                obs.setComment(comments);
                            }
                            if (date_created_converted != null) {
                                obs.setDateCreated(date_created_converted);
                            }
                            if (StringUtils.isNotBlank(value_text)) {
                                obs.setValueText(value_text);
                            }
                            //Save the obs
                            Obs savedObs = Context.getObsService().saveObs(obs, "Data migration");
                            //save the Migation Obs tracker
                            MigrationObsTracking migrationObsTracking = new MigrationObsTracking();
                            migrationObsTracking.setOldObsId(old_obs_id);
                            migrationObsTracking.setNewObsId(savedObs.getObsId());
                            Context.getService(HospitalCoreService.class).createMigrationObsTrackingDetails(migrationObsTracking);
                            obsCount++;
                    }
                    System.out.println("The number of OBS registered so far is >>"+obsCount);
                    if(obsCount == 100) {
                        Context.clearSession();
                        Context.flushSession();
                    }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void updateOrderData() {

        InputStream ordersPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/orders_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";
        Integer order_id = null; //0
        OrderType order_type_id = null;//1
        Concept concept_id = null;//2
        Provider orderer = HospitalCoreUtils.getProvider(Context.getAuthenticatedUser().getPerson());
        Encounter encounter_id = null;//4
        String instructions = "";//5
        String date_activated = "";//6
        String auto_expire_date = "";//7
        String date_stopped = "";//8
        Concept order_reason = null;//9
        String order_reason_non_coded = "";//10
        User creator = Context.getAuthenticatedUser();
        String date_created = "";//12
        //voided,//13
        //voided_by,//14
        //date_voided,//15
        //void_reason,//16
        Patient patient_id = null;//17
        String accession_number = "";//18
        //uuid,//19
        Order.Urgency urgency = Order.Urgency.ROUTINE;//20
        String order_number = "";//21
        //Integer previous_order_id = null;//22
        Order.Action action = Order.Action.NEW;;//23
        String comment_to_fulfiller = "";//24
        CareSetting careSetting = Context.getOrderService().getCareSettingByUuid("6f0c9a92-6f24-11e3-af88-005056821db0"); //25
        String scheduled_date = "";//26
        //order_group_id,//27
        //sort_weight,//28
        String fulfiller_comment = "";//29
        Order.FulfillerStatus fulfillerStatus = Order.FulfillerStatus.COMPLETED;//30
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ordersPath, "UTF-8"));
            headLine = br.readLine();
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                order_id = Integer.valueOf(records[0]);
                order_type_id = getOrderType(Integer.parseInt(records[1]));
                concept_id = Context.getConceptService().getConcept(records[2]);
                encounter_id = getEncounter(Integer.parseInt(records[4]));
                instructions = records[5];
                date_activated = getSafeString(records[6]);
                Date date_activated_1 = DateUtils.getDateFromString(date_activated, "yyyy-MM-dd HH:mm:ss");
                auto_expire_date = getSafeString(records[7]);
                Date auto_expire_date_1 = null;
                date_stopped = getSafeString(records[8]);
                Date date_stopped_1 = DateUtils.getDateFromString(date_stopped, "yyyy-MM-dd HH:mm:ss");
                order_reason = Context.getConceptService().getConcept(records[9]);
                order_reason_non_coded = records[10];
                date_created = getSafeString(records[12]);
                Date date_created_1 = DateUtils.getDateFromString(date_created, "yyyy-MM-dd HH:mm:ss");
                patient_id = getPatient(Integer.parseInt(records[17]));
                accession_number = records[18];
                order_number = records[21];
                comment_to_fulfiller = records[24];
                scheduled_date = records[26];
                Date scheduled_date_1 = DateUtils.getDateFromString(scheduled_date, "yyyy-MM-dd HH:mm:ss");
                fulfiller_comment = records[29];
                if(StringUtils.isNotBlank(auto_expire_date) || !auto_expire_date.equals("NULL")) {
                    auto_expire_date_1 =  DateUtils.getDateFromString(auto_expire_date, "yyyy-MM-dd HH:mm:ss");
                }
                if(StringUtils.isNotBlank(date_stopped) || !date_stopped.equals("NULL")) {
                    date_stopped_1 =  DateUtils.getDateFromString(date_stopped, "yyyy-MM-dd HH:mm:ss");
                }
                //create order
                if(patient_id != null) {
                    if(order_type_id.getName().equals("Drug Order")) {
                        DrugOrder order = new DrugOrder();
                        order.setOrderType(order_type_id);
                        order.setOrderer(orderer);
                        order.setPatient(patient_id);
                        order.setOrderReason(order_reason);
                        order.setAccessionNumber(accession_number);
                        order.setAction(action);
                        if (auto_expire_date_1 != null) {
                            order.setAutoExpireDate(auto_expire_date_1);
                        }
                        order.setConcept(concept_id);
                        order.setCareSetting(careSetting);
                        order.setCommentToFulfiller(comment_to_fulfiller);
                        if(encounter_id != null) {
                            order.setEncounter(encounter_id);
                            order.setDateActivated(encounter_id.getEncounterDatetime());
                        }
                        else {
                            order.setDateActivated(date_activated_1);
                        }
                        order.setCreator(creator);
                        order.setInstructions(instructions);
                        order.setOrderReasonNonCoded(order_reason_non_coded);
                        order.setDateCreated(date_created_1);
                        order.setFulfillerComment(fulfiller_comment);
                        order.setScheduledDate(scheduled_date_1);
                        order.setUrgency(urgency);

                        //save the order to backend
                        Order saveOrder = Context.getOrderService().saveOrder(order, null);
                        //populate the migration order tracking map
                        MigrationOrders migrationOrders = new MigrationOrders();
                        migrationOrders.setOldOrderId(order_id);
                        migrationOrders.setNewOrderId(saveOrder.getOrderId());
                        //save the migration order
                        Context.getService(HospitalCoreService.class).createMigrationOrdersTrackingDetails(migrationOrders);
                        count++;
                    }

                    else{
                        TestOrder order = new TestOrder();
                        order.setOrderType(order_type_id);
                        order.setOrderer(orderer);
                        order.setPatient(patient_id);
                        order.setOrderReason(order_reason);
                        order.setAccessionNumber(accession_number);
                        order.setAction(action);
                        if (auto_expire_date_1 != null) {
                            order.setAutoExpireDate(auto_expire_date_1);
                        }
                        order.setConcept(concept_id);
                        order.setCareSetting(careSetting);
                        order.setCommentToFulfiller(comment_to_fulfiller);
                        if(encounter_id != null) {
                            order.setEncounter(encounter_id);
                            order.setDateActivated(encounter_id.getEncounterDatetime());
                        }
                        else {
                            order.setDateActivated(date_activated_1);
                        }
                        order.setCreator(creator);
                        order.setInstructions(instructions);
                        order.setOrderReasonNonCoded(order_reason_non_coded);
                        order.setDateCreated(date_created_1);
                        order.setFulfillerComment(fulfiller_comment);
                        order.setScheduledDate(scheduled_date_1);
                        order.setUrgency(urgency);

                        //save the order to backend
                        Order saveOrder = Context.getOrderService().saveOrder(order, null);
                        //populate the migration order tracking map
                        MigrationOrders migrationOrders = new MigrationOrders();
                        migrationOrders.setOldOrderId(order_id);
                        migrationOrders.setNewOrderId(saveOrder.getOrderId());
                        //save the migration order
                        Context.getService(HospitalCoreService.class).createMigrationOrdersTrackingDetails(migrationOrders);
                        count++;
                    }
                    System.out.println("The number of orders registered so far is >>"+count);
                    if(count == 100) {
                        Context.clearSession();
                        Context.flushSession();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePersonAddress() {
        InputStream addressPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/person_address_migration.csv");
        String line = "";
        String headLine = "";
        String cvsSplitBy = ",";
        String person_id = ""; //1
        String address1 = ""; //3
        String address2 = ""; //4
        String city_village = ""; //5
        String state_province = ""; //6
        String postal_code = ""; //7
        String country = "" ; //8
        String latitude = ""; //9
        String longitude = ""; //10
        String start_date = ""; //11
        String end_date = ""; //12
        String date_created = "";//14
        String county_district = ""; //19
        String address3 = ""; //20
        String address4 = ""; //21
        String address5 = ""; //22
        String address6 = ""; //23
        //String date_changed = ""; //24
        String address7 = ""; //27
        String address8 = ""; //28
        String address9 = ""; //29
        String address10 = ""; //30
        String address11 = ""; //31
        String address12 = ""; //32
        String address13 = ""; //33
        String address14 = ""; //34
        String address15 = ""; //35
        Person person = null;
        int count=0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(addressPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                person_id = records[1];
                Patient patient = getPatient(Integer.parseInt(person_id));
                if(patient != null) {
                    person = patient.getPerson();
                }
                address1 = getSafeString(records[3]);
                address2 = getSafeString(records[4]);
                city_village = getSafeString(records[5]);
                state_province = getSafeString(records[6]);
                postal_code = getSafeString(records[7]);
                country = getSafeString(records[8]);
                latitude = getSafeString(records[9]);
                longitude = getSafeString(records[10]);
                start_date = getSafeString(records[11]);
                Date start_date_1 = DateUtils.getDateFromString(start_date, "yyyy-MM-dd HH:mm:ss");
                end_date = getSafeString(records[12]);
                Date end_date_1 = DateUtils.getDateFromString(end_date, "yyyy-MM-dd HH:mm:ss");
                date_created = getSafeString(records[14]);
                Date date_created_1 = DateUtils.getDateFromString(date_created, "yyyy-MM-dd HH:mm:ss");
                county_district = records[19];
                address3 = records[20];
                address4 = records[21];
                address5 = records[22];
                address6 = records[23];
                address7 = records[27];
                address8 = records[28];
                address9 = records[29];
                address10 = records[30];
                address11 = records[31];
                address12 = records[32];
                address13 = records[33];
                address14 = records[34];
                address15 = records[35];

                //construct a person address
                if(person != null) {
                    PersonAddress personAddress = new PersonAddress();
                    personAddress.setCreator(Context.getAuthenticatedUser());
                    personAddress.setPerson(person);
                    if (StringUtils.isNotBlank(address1)) {
                        personAddress.setAddress1(address1);
                    }
                    if (StringUtils.isNotBlank(address2)) {
                        personAddress.setAddress2(address2);
                    }
                    if (StringUtils.isNotBlank(city_village)) {
                        personAddress.setCityVillage(city_village);
                    }
                    if (StringUtils.isNotBlank(state_province)) {
                        personAddress.setStateProvince(state_province);
                    }
                    if (StringUtils.isNotBlank(postal_code)) {
                        personAddress.setPostalCode(postal_code);
                    }
                    if (StringUtils.isNotBlank(country)) {
                        personAddress.setCountry(country);
                    }
                    if (StringUtils.isNotBlank(latitude)) {
                        personAddress.setLatitude(latitude);
                    }
                    if (StringUtils.isNotBlank(longitude)) {
                        personAddress.setLongitude(longitude);
                    }
                    if (start_date_1 != null) {
                        personAddress.setStartDate(start_date_1);
                    }
                    if (end_date_1 != null) {
                        personAddress.setEndDate(end_date_1);
                    }
                    if (date_created_1 != null) {
                        personAddress.setDateCreated(date_created_1);
                    } else {
                        personAddress.setDateCreated(new Date());
                    }
                    if (StringUtils.isNotBlank(county_district)) {
                        personAddress.setCountyDistrict(county_district);
                    }
                    if (StringUtils.isNotBlank(address3)) {
                        personAddress.setAddress3(address3);
                    }
                    if (StringUtils.isNotBlank(address4)) {
                        personAddress.setAddress4(address4);
                    }
                    if (StringUtils.isNotBlank(address5)) {
                        personAddress.setAddress5(address5);
                    }
                    if (StringUtils.isNotBlank(address6)) {
                        personAddress.setAddress6(address6);
                    }
                    if (StringUtils.isNotBlank(address7)) {
                        personAddress.setAddress7(address7);
                    }
                    if (StringUtils.isNotBlank(address8)) {
                        personAddress.setAddress8(address8);
                    }
                    if (StringUtils.isNotBlank(address9)) {
                        personAddress.setAddress9(address9);
                    }
                    if (StringUtils.isNotBlank(address10)) {
                        personAddress.setAddress10(address10);
                    }
                    if (StringUtils.isNotBlank(address11)) {
                        personAddress.setAddress11(address11);
                    }
                    if (StringUtils.isNotBlank(address12)) {
                        personAddress.setAddress12(address12);
                    }
                    if (StringUtils.isNotBlank(address13)) {
                        personAddress.setAddress13(address13);
                    }
                    if (StringUtils.isNotBlank(address14)) {
                        personAddress.setAddress14(address14);
                    }
                    if (StringUtils.isNotBlank(address15)) {
                        personAddress.setAddress15(address15);
                    }

                    Context.getPersonService().savePersonAddress(personAddress);
                    count++;
                }
                System.out.println("The address done are >>"+count);
                if(count == 100) {
                    Context.clearSession();
                    Context.flushSession();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

    static Visit getVisit(int visitId) {
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
        int count = 0;
        String patient = "";
        String opd_number = "";
        User createdBy = Context.getAuthenticatedUser();
        Date dateCreated = null;
        PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByUuid("61A354CB-4F7F-489A-8BE8-09D0ACEDDC63");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(opdNumbersPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                patient = records[1];
                Patient patient_1 = getPatient(Integer.parseInt(patient));
                opd_number = records[2];
                dateCreated = DateUtils.getDateFromString(records[4], "yyyy-MM-dd HH:mm:ss");

                //Save patient identifier
                //Check if such patient identifier exist for this patient
                if (patient_1 != null) {
                    List<PatientIdentifier> patientIdentifierList = Context.getPatientService().getPatientIdentifiers(
                            null,
                            Arrays.asList(identifierType), null, Arrays.asList(patient_1), false);
                    if (patientIdentifierList.isEmpty() && patient != null) {
                        //populate the patient identifier
                        PatientService patientService = Context.getPatientService();
                        PatientIdentifier opdPatientIdentifier = new PatientIdentifier();
                        opdPatientIdentifier.setIdentifierType(identifierType);
                        opdPatientIdentifier.setIdentifier(opd_number);
                        opdPatientIdentifier.setPatient(patient_1);
                        opdPatientIdentifier.setPreferred(true);
                        opdPatientIdentifier.setDateCreated(dateCreated);
                        opdPatientIdentifier.setCreator(createdBy);

                        //save the patient identifier
                        patientService.savePatientIdentifier(opdPatientIdentifier);

                        //save the object
                        IdentifierNumbersGenerator opdNumbersGenerator = new IdentifierNumbersGenerator();
                        opdNumbersGenerator.setPatientId(patient_1.getPatientId());
                        opdNumbersGenerator.setIdentifier(opd_number);
                        opdNumbersGenerator.setDateCreated(dateCreated);
                        opdNumbersGenerator.setCreatedBy(createdBy.getId());
                        opdNumbersGenerator.setIdentifierType(identifierType.getPatientIdentifierTypeId());
                        Context.getService(HospitalCoreService.class).saveOpdNumbersGenerator(opdNumbersGenerator);

                        count++;
                    }
                }
            }
            System.out.println("The numbers transferred is >>"+count);
            if(count == 100) {
                Context.clearSession();
                Context.flushSession();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Map<Integer, String> getEncounterTypeMappings() {
        Map<Integer, String> encounterMap = new HashMap<Integer, String>();
        encounterMap.put(1,"ed6dacc9-0827-4c82-86be-53c0d8c449be");
        encounterMap.put(2,"2bdada65-4c72-4a48-8730-859890e25cee");
        encounterMap.put(3,"465a92f2-baf8-42e9-9612-53064be868e8");
        encounterMap.put(4,"17a381d1-7e29-406a-b782-aa903b963c28");
        encounterMap.put(5,"de1f9d67-b73e-4e1b-90d0-036166fc6995");
        encounterMap.put(6,"d1059fb9-a079-4feb-a749-eedd709ae542");
        encounterMap.put(7,"de78a6be-bfc5-4634-adc3-5f1a280455cc");
        encounterMap.put(8,"a0034eee-1940-4e35-847f-97537a35d05e");
        encounterMap.put(9,"415f5136-ca4a-49a8-8db3-f994187c3af6");
        encounterMap.put(10,"bcc6da85-72f2-4291-b206-789b8186a021");
        encounterMap.put(11,"01894f88-dc73-42d4-97a3-0929118403fb");
        encounterMap.put(12,"82169b8d-c945-4c41-be62-433dfd9d6c86");
        encounterMap.put(13,"5feee3f1-aa16-4513-8bd0-5d9b27ef1208");
        encounterMap.put(14,"3ee036d8-7c13-4393-b5d6-036f2fe45126");
        encounterMap.put(15,"c6d09e05-1f25-4164-8860-9f32c5a02df0");
        encounterMap.put(16,"7c426cfc-3b47-4481-b55f-89860c21c7de");
        encounterMap.put(17,"9d8498a4-372d-4dc4-a809-513a2434621e");
        encounterMap.put(18,"d3e3d723-7458-4b4e-8998-408e8a551a84");
        encounterMap.put(19,"fbf0bfce-e9f4-45bb-935a-59195d8a0e35");
        encounterMap.put(20,"9c0a7a57-62ff-4f75-babe-5835b0e921b7");
        encounterMap.put(21,"e87aa2ad-6886-422e-9dfd-064e3bfe3aad");
        encounterMap.put(22,"975ae894-7660-4224-b777-468c2e710a2a");
        encounterMap.put(23,"0c61819d-4f82-434e-b24d-aa8c82d49297");
        encounterMap.put(24,"de5cacd4-7d15-4ad0-a1be-d81c77b6c37d");
        encounterMap.put(25,"bb77c683-2144-48a5-a011-66d904d776c9");
        encounterMap.put(26,"aadeafbe-a3b1-4c57-bc76-8461b778ebd6");
        encounterMap.put(27,"9bc15e94-2794-11e8-b467-0ed5f89f718b");
        encounterMap.put(28,"7df67b83-1b84-4fe2-b1b7-794b4e9bfcc3");
        encounterMap.put(29,"7dffc392-13e7-11e9-ab14-d663bd873d93");
        encounterMap.put(30,"e1406e88-e9a9-11e8-9f32-f2801f1b9fd1");
        encounterMap.put(31,"1495edf8-2df2-11e9-b210-d663bd873d93");
        encounterMap.put(32,"35468fe8-a889-4cd4-9b35-27ac98bdd750");
        encounterMap.put(33,"c4a2be28-6673-4c36-b886-ea89b0a42116");
        encounterMap.put(34,"6e5ec039-8d2a-4172-b3fb-ee9d0ba647b7");
        encounterMap.put(35,"3c37b7a0-9f83-4fdc-994f-17308c22b423");
        encounterMap.put(36,"83610d13-d4fc-42c3-8c1d-a403cd6dd073");
        encounterMap.put(37,"402c10a3-d419-4040-b5d8-bde0af646405");
        encounterMap.put(38,"c4657c33-f252-4ba9-8a4f-b09ed0deda75");
        encounterMap.put(39,"119362fb-6af6-4462-9fb2-7a09c43c9874");
        encounterMap.put(40,"26bb869b-b569-4acd-b455-02c853e9f1e6");
        encounterMap.put(41,"d7cfa460-2944-11e9-b210-d663bd873d93");
        encounterMap.put(42,"47c73adb-f9db-4c79-b582-e16064f9cee0");
        encounterMap.put(43,"5feffc6c-3194-43df-9b80-290054216c35");
        encounterMap.put(44,"66609dee-3438-11e9-b210-d663bd873d93");
        encounterMap.put(45,"291c0828-a216-11e9-a2a3-2a2ae2dbcce4");
        encounterMap.put(46,"706a8b12-c4ce-40e4-aec3-258b989bf6d3");
        encounterMap.put(47,"5cf0124e-09da-11ea-8d71-362b9e155667");
        encounterMap.put(48,"5cf00d9e-09da-11ea-8d71-362b9e155667");
        encounterMap.put(49,"16238574-0464-11ea-9a9f-362b9e155667");
        encounterMap.put(50,"162382b8-0464-11ea-9a9f-362b9e155667");
        encounterMap.put(51,"162386c8-0464-11ea-9a9f-362b9e155667");
        encounterMap.put(76,"c7f47a56-207b-11e9-ab14-d663bd873d93");
        encounterMap.put(77,"d7142400-2495-11e9-ab14-d663bd873d93");
        encounterMap.put(78,"ce841b19-0acd-46fd-b223-2ca9b5356237");
        encounterMap.put(79,"999792ec-8854-11e9-bc42-526af7764f64");
        encounterMap.put(80,"a3ce2705-d72d-458a-a76c-dae0f93398e7");
        encounterMap.put(81,"928ea6b2-3425-4ee9-854d-daa5ceaade03");
        encounterMap.put(82,"c3fb7831-f8fc-4b71-bd54-f23cdd77e305");
        encounterMap.put(83,"383974fe-58ef-488f-bdff-8962f4dd7518");
        encounterMap.put(84,"2cc8c535-bbfa-4668-98c7-b12e3550ee7b");
        encounterMap.put(85,"7b69daf5-b567-4384-9d29-f020c408d613");
        encounterMap.put(86,"84220f19-9071-4745-9045-3b2f8d3dc128");
        encounterMap.put(87,"bd64b3b0-7bc9-4541-a813-8a917f623e2e");
        encounterMap.put(88,"ea68aad6-4655-4dc5-80f2-780e33055a9e");
        encounterMap.put(89,"596f878f-5adf-4f8e-8829-6a87aaeda9a3");
        encounterMap.put(90,"92e03f22-9686-11e9-bc42-526af7764f64");
        encounterMap.put(91,"c4f9db39-2c18-49a6-bf9b-b243d673c64d");
        encounterMap.put(92,"119217a4-06d6-11ea-8d71-362b9e155667");
        encounterMap.put(93,"a70a1056-75b3-11ea-bc55-0242ac130003");
        encounterMap.put(94,"5c64e368-7fdc-11ea-bc55-0242ac130003");
        encounterMap.put(95,"94eebf1a-83a1-11ea-bc55-0242ac130003");
        encounterMap.put(96,"b046eb36-7bd0-40cf-bdcb-c662bc0f00c3");
        encounterMap.put(97,"3fefa230-ea10-45c7-b62b-b3b8eb7274bb");
        encounterMap.put(98,"356d447a-b494-11ea-8337-f7bcaf3e8fec");
        encounterMap.put(99,"012bb9f4-f282-11ea-a6d6-3b4fa4aefb5a");
        encounterMap.put(100,"11d3f37a-f282-11ea-a825-1b5b1ff1b854");
        encounterMap.put(101,"6e1105ba-f282-11ea-ad42-e7971c094de0");
        encounterMap.put(102,"63721d9e-f28f-11ea-acf1-1ba6050a20d6");
        encounterMap.put(103,"6cc4098e-f28f-11ea-ac2c-335ca43b58da");
        encounterMap.put(104,"7c14fbbe-f28f-11ea-ba41-df4a13e6c5d6");
        encounterMap.put(105,"856c6f8a-f28f-11ea-ab67-3f6de890685e");
        encounterMap.put(106,"8efa1534-f28f-11ea-b25f-af56118cf21b");
        encounterMap.put(107,"98d42234-f28f-11ea-b609-bbd062a0383b");
        encounterMap.put(108,"ba45c278-f290-11ea-9666-1b3e6e848887");
        encounterMap.put(109,"f2fdbb02-f290-11ea-9a81-3729eb94d52d");
        encounterMap.put(110,"2af60550-f291-11ea-b725-9753b5f685ae");
        encounterMap.put(111,"ce33a16e-f291-11ea-90fb-2fc2d783a570");
        encounterMap.put(112,"f15399a6-f291-11ea-b2ad-3341295ede42");
        encounterMap.put(113,"14dc10b0-f292-11ea-a573-cf69ab0e0e17");
        encounterMap.put(114,"502281ae-f292-11ea-8273-ebd1e54b661c");
        encounterMap.put(115,"6f248e9e-f292-11ea-b084-0fbd52cd2188");
        encounterMap.put(116,"8d9451e8-f292-11ea-bb2f-8fdebce3194d");
        encounterMap.put(117,"ae378f6e-f292-11ea-8ef8-0bb1ae4b7f42");
        encounterMap.put(118,"cbe0212a-f292-11ea-affa-2b398f67bbb4");
        encounterMap.put(119,"d029ec12-6bae-11eb-b775-0f590ebc4940");
        encounterMap.put(120,"25dbb7d0-fac6-11ea-94e8-0bd56324f4b5");
        encounterMap.put(121,"fcaec384-fac5-11ea-8aeb-fffc453fcc77");
        encounterMap.put(122,"09073c56-fac6-11ea-87aa-bfe01fd142fd");
        encounterMap.put(123,"150932f2-fac6-11ea-b158-4b1b80bfe51c");
        encounterMap.put(124,"586660a8-08ca-11eb-9dac-6f55ef0a3a7d");
        encounterMap.put(125,"7ae70e0c-08ca-11eb-9325-676401a68809");
        encounterMap.put(126,"984405fe-08ca-11eb-bb7a-cb6ccfe9c91e");
        encounterMap.put(127,"e24209cc-0a1d-11eb-8f2a-bb245320c623");
        encounterMap.put(128,"b9c51a7e-0e24-11eb-9559-13c33ac53eec");
        encounterMap.put(129,"cb5f27f0-18f8-11eb-88d7-fb1a7178f8ea");
        encounterMap.put(130,"f1573d1c-18f8-11eb-a453-63d51e56f5cb");
        encounterMap.put(131,"1f7ec412-18f9-11eb-8122-8f25a543787a");
        encounterMap.put(132,"4664a5b0-18f9-11eb-8a5a-2bee5937c71b");
        encounterMap.put(133,"af5dbd36-18f9-11eb-ae6b-7f4c0920f004");
        encounterMap.put(134,"d7aaaf20-31ca-4d22-9dd9-0796eb47a341");
        encounterMap.put(135,"cf805d0a-a470-4194-b375-7e04f56d4dee");
        encounterMap.put(136,"90e54c41-da23-4ace-b472-0c8521c97594");
        encounterMap.put(137,"e360f35f-e496-4f01-843b-e2894e278b5b");
        encounterMap.put(138,"86709cfc-1490-11ec-82a8-0242ac130003");
        encounterMap.put(139,"8b706d42-b4ae-4b3b-bd83-b14f15294362");
        encounterMap.put(140,"bec91024-5433-11ec-8ddd-bf8f24d733fa");
        encounterMap.put(141,"04b0ac34-6f4f-11e6-8b77-86f30ca893d3");
        encounterMap.put(142,"85019fbe-9339-49f7-8341-e9a04311bb99");
        encounterMap.put(143,"4f02dfed-a2ec-40c2-b546-85dab5831871");
        encounterMap.put(144,"35c6fcc2-960b-11ec-b909-0242ac120002");
        encounterMap.put(145,"a2010bf5-2db0-4bf4-819f-8a3cffbcb21b");
        encounterMap.put(146,"2504e865-638e-4a63-bf08-7e8f03a376f3");
        encounterMap.put(147,"6632e66c-9ae5-11ec-b909-0242ac120002");
        encounterMap.put(148,"4224f8bf-11b2-4e47-a958-1dbdfd7fa41d");
        encounterMap.put(149,"ec2a91e5-444a-4ca0-87f1-f71ddfaf57eb");
        encounterMap.put(150,"54df6991-13de-4efc-a1a9-2d5ac1b72ff8");
        encounterMap.put(151,"f091b067-bea5-4657-8445-cfec05dc46a2");
        encounterMap.put(152,"899d64ad-be13-4071-a879-2153847206b7");
        encounterMap.put(153,"bf484793-1734-4f57-a6f1-b866545ca8df");
        encounterMap.put(154,"ee366157-d40b-4204-8de8-c24262c65b5a");

        return encounterMap;
    }

    static EncounterType getEncounterType(Integer oldEncounterTypeId) {
        EncounterType encounterType = null;
        if(!getEncounterTypeMappings().isEmpty() && getEncounterTypeMappings().containsKey(oldEncounterTypeId)) {
            encounterType = Context.getEncounterService().getEncounterTypeByUuid(getEncounterTypeMappings().get(oldEncounterTypeId));
        }
        return encounterType;
    }

    static Encounter getEncounter(int encounterId) {
        HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
        List<MigrationEncounterTracking> migrationEncounterTracking = hospitalCoreService.getMigrationEncounterTrackingDetails();
        Map<Integer, Integer> getEncounterIds = new HashMap<Integer, Integer>();
        if(!migrationEncounterTracking.isEmpty()) {
            for(MigrationEncounterTracking migrationVisitTrackingItem: migrationEncounterTracking) {
                getEncounterIds.put(migrationVisitTrackingItem.getOldEncounterId(), migrationVisitTrackingItem.getNewEncounterId());
            }
        }
        Encounter encounter = null;
        if (!getEncounterIds.isEmpty() && getEncounterIds.containsKey(encounterId)) {
            encounter = Context.getEncounterService().getEncounter(getEncounterIds.get(encounterId));
        }
        return encounter;
    }

    static boolean getBooleanStatus(int value){
        boolean status = false;
        if(value == 1) {
            status = true;
        }
        return status;
    }
    static String getSafeString(String str){
        return str.replaceAll("^\"|\"$", "");
    }

    static Order getOrders(int orderId) {
        HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
        List<MigrationOrders> migrationOrders = hospitalCoreService.getMigrationOrdersDetails();
        Map<Integer, Integer> getOrdersIds = new HashMap<Integer, Integer>();
        if(!migrationOrders.isEmpty()) {
            for(MigrationOrders migrationOrdersItem: migrationOrders) {
                getOrdersIds.put(migrationOrdersItem.getOldOrderId(), migrationOrdersItem.getNewOrderId());
            }
        }
        Order order = null;
        if (!getOrdersIds.isEmpty() && getOrdersIds.containsKey(orderId)) {
            order = Context.getOrderService().getOrder(getOrdersIds.get(orderId));
        }
        return order;
    }

    static MigrationObsTracking getLastMigrationObsTracking() {
        return Context.getService(HospitalCoreService.class).getLastMigrationObsTracking();

    }

    static Map<Integer, String> getOrderTypeMappings() {
        Map<Integer, String> orderTypeMap = new HashMap<Integer, String>();
        InputStream orderTypePath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/orders_type_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(orderTypePath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                orderTypeMap.put(Integer.valueOf(records[0]), records[9]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return orderTypeMap;
    }
    static OrderType getOrderType(Integer oldOrderTypeId) {
        OrderType orderType = null;
        if(!getOrderTypeMappings().isEmpty() && getOrderTypeMappings().containsKey(oldOrderTypeId)) {
            orderType = Context.getOrderService().getOrderTypeByUuid(getOrderTypeMappings().get(oldOrderTypeId));
        }
        return orderType;
    }

}
