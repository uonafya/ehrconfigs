package org.openmrs.module.ehrconfigs.utils;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.CareSetting;
import org.openmrs.Concept;
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
                        System.out.println("Saved patient number " + track);
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
            //headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                old_visit_id = records[0];
                patient = records[1];
               Patient patient_1 = getPatient(Integer.parseInt(patient));
                System.out.println("The patient is  >>||"+patient_1 +"||>> with OLD ID as "+records[1]);
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
        int old_encounter_id;
        EncounterType encounter_type = null;
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
            //headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);

                old_encounter_id = Integer.parseInt(records[0]);
                encounter_type = getEncounterType(Integer.parseInt(records[1]));
                patient = getPatient(Integer.parseInt(records[2]));
                encounter_datetime_1 = getSafeString(records[5]);
                date_created_1 = getSafeString(records[7]);
                encounter_datetime = DateUtils.getDateFromString(encounter_datetime_1, "yyyy-MM-dd HH:mm:ss");
                date_created = DateUtils.getDateFromString(date_created_1, "yyyy-MM-dd HH:mm:ss");
                visit_id = getVisit(Integer.parseInt(records[14]));
                if(Context.getService(HospitalCoreService.class).getMigrationEncounterTrackingDetailsByOldEncounterID(old_encounter_id) == null && patient !=null) {
                    Encounter encounter = new Encounter();
                    encounter.setEncounterDatetime(encounter_datetime);
                    encounter.setPatient(patient);
                    encounter.setDateCreated(date_created);
                    encounter.setEncounterType(encounter_type);
                    encounter.setVisit(visit_id);
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

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void updateOpdTestOrders(){
        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/opd_test_order_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";
        Integer opd_order_id = null;
        Patient patient_id = null;
        Encounter encounter_id = null;
        Concept concept_id = null;
        Integer type_concept = null;
        Concept value_coded = null;
        User created_by = Context.getAuthenticatedUser();
        Date created_on = null;
        Integer billing_status = null;
        Integer cancel_status = null;
        BillableService billable_service_id = null;
        Date schedule_date = null;
        Integer indoor_status = null;
        String referral_ward_name = null;
        Integer service_type = null;
        int count=0;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                //start populating the object
                opd_order_id = Integer.valueOf(records[0]);
                patient_id = getPatient(Integer.parseInt(records[1]));
                encounter_id = getEncounter(Integer.parseInt(records[2]));
                concept_id = Context.getConceptService().getConcept(records[3]);
                type_concept = Integer.valueOf(records[4]);
                value_coded = Context.getConceptService().getConcept(records[5]);
                created_on = DateUtils.getDateFromString(getSafeString(records[7]), "yyyy-MM-dd hh:mm:ss");
                billing_status = Integer.valueOf(records[8]);
                cancel_status = Integer.valueOf(records[9]);
                billable_service_id = Context.getService(BillingService.class).getServiceById(Integer.valueOf(records[10]));
                schedule_date = DateUtils.getDateFromString(getSafeString(records[11]), "yyyy-MM-dd hh:mm:ss");
                indoor_status = Integer.valueOf(records[12]);
                referral_ward_name = records[13];
                service_type = Integer.valueOf(records[14]);
                //construct the object here
                if(patient_id != null) {
                    OpdTestOrder opdTestOrder = new OpdTestOrder();
                    opdTestOrder.setOpdOrderId(opd_order_id);
                    opdTestOrder.setPatient(patient_id);
                    opdTestOrder.setEncounter(encounter_id);
                    opdTestOrder.setConcept(concept_id);
                    opdTestOrder.setTypeConcept(type_concept);
                    opdTestOrder.setValueCoded(value_coded);
                    opdTestOrder.setCreator(created_by);
                    opdTestOrder.setCreatedOn(created_on);
                    opdTestOrder.setBillingStatus(billing_status);
                    opdTestOrder.setCancelStatus(cancel_status);
                    opdTestOrder.setBillableService(billable_service_id);
                    opdTestOrder.setScheduleDate(schedule_date);
                    opdTestOrder.setIndoorStatus(indoor_status);
                    opdTestOrder.setFromDept(referral_ward_name);
                    opdTestOrder.setServiceType(service_type);
                    //Save the object
                    Context.getService(PatientDashboardService.class).saveOrUpdateOpdOrder(opdTestOrder);
                    count++;
                }
                System.out.println("The number of test orders recorded is >>"+count);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateOpdDrugOrder() {
        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_opd_drug_order_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        Integer opd_drug_order_id = null;
        Patient patient_id = null;
        Encounter encounter_id = null;
        Integer inventory_drug_id = null;
        Integer formulation_id = null;
        Integer frequency_concept_id = null;
        Integer no_of_days = null;
        String comments = "";
        User created_by = Context.getAuthenticatedUser();
        Date created_on = null;
        Integer order_status = null;
        Integer cancel_status = null;
        String referral_ward_name = "";
        String dosage = "";
        Integer dosage_unit = null;
        int count=0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                opd_drug_order_id = Integer.valueOf(records[0]);
                patient_id = getPatient(Integer.parseInt(records[1]));
                encounter_id = getEncounter(Integer.parseInt(records[2]));
                inventory_drug_id = Integer.valueOf(records[3]);
                formulation_id = Integer.valueOf(records[4]);
                frequency_concept_id = Integer.valueOf(records[5]);
                no_of_days = Integer.valueOf(records[6]);
                comments = records[7];
                created_on = DateUtils.getDateFromString(getSafeString(records[9]), "yyyy-MM-dd HH:mm:ss");
                order_status = Integer.valueOf(records[10]);
                cancel_status = Integer.valueOf(records[11]);
                referral_ward_name = records[12];
                dosage = records[13];
                dosage_unit = Integer.valueOf(records[14]);

                //Construct an OPD drug order object
                if(patient_id != null) {
                    OpdDrugOrder opdDrugOrder = new OpdDrugOrder();
                    opdDrugOrder.setOpdDrugOrderId(opd_drug_order_id);
                    opdDrugOrder.setPatient(patient_id);
                    opdDrugOrder.setEncounter(encounter_id);
                    opdDrugOrder.setInventoryDrug(Context.getService(InventoryCommonService.class).getDrugById(inventory_drug_id));
                    opdDrugOrder.setInventoryDrugFormulation(Context.getService(InventoryCommonService.class).getDrugFormulationById(formulation_id));
                    opdDrugOrder.setFrequency(Context.getConceptService().getConcept(frequency_concept_id));
                    opdDrugOrder.setNoOfDays(no_of_days);
                    opdDrugOrder.setComments(comments);
                    opdDrugOrder.setCreator(created_by);
                    opdDrugOrder.setCreatedOn(created_on);
                    opdDrugOrder.setOrderStatus(order_status);
                    opdDrugOrder.setCancelStatus(cancel_status);
                    opdDrugOrder.setReferralWardName(referral_ward_name);
                    opdDrugOrder.setDosage(dosage);
                    opdDrugOrder.setDosageUnit(Context.getConceptService().getConcept(dosage_unit));
                    //save an order
                    Context.getService(PatientDashboardService.class).saveOrUpdateOpdDrugOrder(opdDrugOrder);
                    count++;
                }
                System.out.println("The number of OPD Drug orders registered so far is >>"+count);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updatePatientBillingServices() {
        InputStream patientPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/billing_patient_service_billmigration.csv");
        String line = "";
        String cvsSplitBy = ",";
        Integer patient_service_bill_id = null;
        String description = null;
        User creator = Context.getAuthenticatedUser();
        String amount = null;
        Integer printed = null;
        Date created_date = null;
        //int voided = 0;
        //Date voided_date = null;
        Patient patient_id = null;
        Integer receipt_id = null;
        String comment = null;
        Integer free_bill = null;
        Integer actual_amount = null;
        Integer waiver_amount = null;
        String payment_mode = null;
        Encounter encounter = null;
        Integer discharge_status = null;
        Integer admitted_days = null;
        String patient_category = null;
        String rebate_amount = null;
        String category_number = null;
        String patient_subcategory = null;
        String transaction_code = null;
        int count=0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(patientPath, "UTF-8"));
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                patient_service_bill_id = Integer.valueOf(records[0]);
                description = records[1];
                amount = records[3];
                printed = Integer.valueOf(records[4]);
                created_date = DateUtils.getDateFromString(getSafeString(records[5]), "yyyy-MM-dd HH:mm:ss");
                patient_id = getPatient(Integer.parseInt(records[8]));
                receipt_id = Integer.valueOf(records[9]);
                comment = records[10];
                free_bill = Integer.valueOf(records[11]);
                actual_amount = Integer.valueOf(records[12]);
                waiver_amount = Integer.valueOf(records[13]);
                payment_mode = records[14];
                encounter = getEncounter(Integer.parseInt(records[15]));
                discharge_status = Integer.valueOf(records[16]);
                admitted_days = Integer.valueOf(records[17]);
                patient_category = records[18];
                rebate_amount = records[19];
                category_number = records[20];
                patient_subcategory = records[21];
                transaction_code = records[22];
                //Construct the object as expected
                if(patient_id != null) {
                    PatientServiceBill patientServiceBill = new PatientServiceBill();
                    patientServiceBill.setPatientServiceBillId(patient_service_bill_id);
                    patientServiceBill.setDescription(description);
                    patientServiceBill.setAmount(new BigDecimal(amount));
                    patientServiceBill.setPrinted(getBooleanStatus(printed));
                    patientServiceBill.setCreatedDate(created_date);
                    patientServiceBill.setCreator(creator);
                    patientServiceBill.setPatient(patient_id);
                    patientServiceBill.setReceipt(Context.getService(BillingService.class).getPatientServiceBillByReceiptId(receipt_id).getReceipt());
                    patientServiceBill.setComment(comment);
                    patientServiceBill.setFreeBill(free_bill);
                    patientServiceBill.setActualAmount(new BigDecimal(actual_amount));
                    patientServiceBill.setWaiverAmount(new BigDecimal(waiver_amount));
                    patientServiceBill.setPaymentMode(payment_mode);
                    patientServiceBill.setEncounter(encounter);
                    patientServiceBill.setDischargeStatus(discharge_status);
                    patientServiceBill.setAdmittedDays(admitted_days);
                    patientServiceBill.setPatientCategory(patient_category);
                    patientServiceBill.setRebateAmount(new BigDecimal(rebate_amount));
                    patientServiceBill.setCategoryNumber(category_number);
                    patientServiceBill.setPatientSubCategory(patient_subcategory);
                    patientServiceBill.setTransactionCode(transaction_code);
                    patientServiceBill.setVoided(false);

                    //save the patient service bill
                    Context.getService(BillingService.class).savePatientServiceBill(patientServiceBill);
                    count++;
                }
                System.out.println("The number of patient service bill generated are >>"+count);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
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

        InputStream obsPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_obs_1_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";

        Integer old_obs_id = null;//0
        Patient  person_id = null;//1
        Concept concept_id = null;//2
        Encounter encounter_id = null;//3
        Order order_id = null;//4
        String obs_datetime = "";//5
        Location location_id = Context.getService(KenyaEmrService.class).getDefaultLocation();//6
        //obs_group_id,//7
        //String accession_number = "";//8
        //value_group_id, //9
        Concept value_coded = null; //10
        //Integer value_coded_name_id = null; //11
        //String value_drug = null; //12
        String value_datetime = null; //13
        Double value_numeric = null;//14
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
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(obsPath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                old_obs_id = Integer.valueOf(records[0]);
                if(getLastMigrationObsTracking() != null && !(getLastMigrationObsTracking().getOldObsId().equals(old_obs_id))) {
                    //get into the observations populating
                    person_id = getPatient(Integer.parseInt(records[1]));
                    concept_id = Context.getConceptService().getConcept(records[2]);
                    encounter_id = getEncounter(Integer.parseInt(records[3]));
                    order_id = getOrders(Integer.parseInt(records[4]));
                    obs_datetime = records[5];
                    Date obs_datetime_converted = DateUtils.getDateFromString(obs_datetime, "yyyy-MM-dd HH:mm:ss");
                    value_coded = Context.getConceptService().getConcept(records[10]);
                    value_datetime = records[13];
                    Date value_datetime_converted = DateUtils.getDateFromString(value_datetime, "yyyy-MM-dd HH:mm:ss");
                    value_numeric = Double.valueOf(records[14]);
                    value_text = records[16];
                    comments = records[18];
                    date_created = records[20];
                    Date date_created_converted = DateUtils.getDateFromString(date_created, "yyyy-MM-dd HH:mm:ss");
                    status = records[28];

                    //construct an observation row
                    Obs obs = new Obs();
                    obs.setObsDatetime(obs_datetime_converted);
                    obs.setPerson(person_id);
                    obs.setConcept(concept_id);
                    obs.setEncounter(encounter_id);
                    if(order_id != null) {
                        obs.setOrder(order_id);
                    }
                    if(value_coded != null) {
                        obs.setValueCoded(value_coded);
                    }
                    if(value_datetime_converted != null) {
                        obs.setValueDatetime(value_datetime_converted);
                    }
                    if(value_numeric != null) {
                        obs.setValueNumeric(value_numeric);
                    }
                    if(StringUtils.isNotBlank(comments)) {
                        obs.setComment(comments);
                    }
                    if(date_created_converted != null) {
                        obs.setDateCreated(date_created_converted);
                    }
                    if(StringUtils.isNotBlank(value_text)) {
                        obs.setValueText(value_text);
                    }
                    //Save the obs
                    Obs savedObs = Context.getObsService().saveObs(obs, "Data migration");
                    //save the Migation Obs tracker
                    MigrationObsTracking migrationObsTracking = new MigrationObsTracking();
                    migrationObsTracking.setOldObsId(old_obs_id);
                    migrationObsTracking.setNewObsId(savedObs.getObsId());
                    Context.getService(HospitalCoreService.class).createMigrationObsTrackingDetails(migrationObsTracking);

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
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                order_id = Integer.valueOf(records[0]);
                order_type_id = getOrderType(Integer.parseInt(records[1]));
                concept_id = Context.getConceptService().getConcept(records[2]);
                encounter_id = getEncounter(Integer.parseInt(records[4]));
                instructions = records[5];
                date_activated = records[6];
                Date date_activated_1 = DateUtils.getDateFromString(date_activated, "yyyy-MM-dd HH:mm:ss");
                auto_expire_date = records[7];
                Date auto_expire_date_1 = DateUtils.getDateFromString(auto_expire_date, "yyyy-MM-dd HH:mm:ss");
                date_stopped = records[8];
                Date date_stopped_1 = DateUtils.getDateFromString(date_stopped, "yyyy-MM-dd HH:mm:ss");
                order_reason = Context.getConceptService().getConcept(records[9]);
                order_reason_non_coded = records[10];
                date_created = records[12];
                Date date_created_1 = DateUtils.getDateFromString(date_created, "yyyy-MM-dd HH:mm:ss");
                patient_id = getPatient(Integer.parseInt(records[17]));
                accession_number = records[18];
                order_number = records[21];
                comment_to_fulfiller = records[24];
                scheduled_date = records[26];
                Date scheduled_date_1 = DateUtils.getDateFromString(scheduled_date, "yyyy-MM-dd HH:mm:ss");
                fulfiller_comment = records[29];

                //create order
                Order order = new Order();
                order.setOrderType(order_type_id);
                order.setOrderer(orderer);
                order.setPatient(patient_id);
                order.setOrderReason(order_reason);
                order.setAccessionNumber(accession_number);
                order.setAction(action);
                order.setAutoExpireDate(auto_expire_date_1);
                order.setConcept(concept_id);
                order.setCareSetting(careSetting);
                order.setCommentToFulfiller(comment_to_fulfiller);
                order.setDateActivated(date_activated_1);
                order.setCreator(creator);
                order.setEncounter(encounter_id);
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
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePersonAddress() {
        InputStream addressPath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/person_address_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        Person person_id = null; //1
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
        int count=0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(addressPath, "UTF-8"));
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                person_id = getPatient(Integer.parseInt(records[1]));
                address1 = records[3];
                address2 = records[4];
                city_village = records[5];
                state_province = records[6];
                postal_code = records[7];
                country = records[8];
                latitude = records[9];
                longitude = records[10];
                start_date = records[11];
                Date start_date_1 = DateUtils.getDateFromString(start_date, "yyyy-MM-dd HH:mm:ss");
                end_date = records[12];
                Date end_date_1 = DateUtils.getDateFromString(end_date, "yyyy-MM-dd HH:mm:ss");
                date_created = records[14];
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
                if(person_id != null) {
                    PersonAddress personAddress = new PersonAddress();
                    personAddress.setCreator(Context.getAuthenticatedUser());
                    personAddress.setPerson(person_id);
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

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Map<Integer, String> getEncounterTypeMappings() {
        Map<Integer, String> encounterMap = new HashMap<Integer, String>();
        InputStream encounterTypePath = OpenmrsClassLoader.getInstance().getResourceAsStream("metadata/patient_encounter_type_migration.csv");
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(encounterTypePath, "UTF-8"));
            headLine = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split(cvsSplitBy);
                encounterMap.put(Integer.valueOf(records[0]), records[9]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
