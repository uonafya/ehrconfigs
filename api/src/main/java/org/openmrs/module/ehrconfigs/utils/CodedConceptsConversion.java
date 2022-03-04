package org.openmrs.module.ehrconfigs.utils;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptSet;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CodedConceptsConversion {

    private static List<String> convertIntoCodedValues() {
        return Arrays.asList(
                "162810AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161166AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "164051AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161199AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "116688AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "148346AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159364AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160463AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "112930AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1482AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "142452AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "127639AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "e799649c-6508-4660-9d9d-4df3449df20e",
                "1185AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161164AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "150518AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "163411AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "148934AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "148934AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "142677AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "113030AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> addedSetMembersUuidsToSrviceOrdered() {
        return Arrays.asList(
                "eb458ded-1fa0-4c1b-92fa-322cada4aff2",
                "0da0f5a5-15f8-4871-ba02-2aa82377223a",
                "160463AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c8cb4aac-be8d-49f1-a98b-2165c3e21ab1",
                "1651AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c0f775f5-bcc3-4900-a39e-35069b3a08ef"
        );
    }

    private static List<String> radiologyDepartmentTests() {
        return Arrays.asList(
                "f65ecf8b-add3-4c58-9697-48cd9cdbbe43",
                "163591AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161165AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "846AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "163004AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> procedurePerformed() {
        return Arrays.asList(
                "441AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "163857AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "367a3526-4a8b-4704-a686-a746e74032f3",
                "d86aa2a3-8ac7-4505-9c3b-3df678174e7b",
                "0ba0f575-ec75-4a51-86c4-97b6ce467684",
                "36AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "54AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "432AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "781AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "782AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "886AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1063AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1148AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1171AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1465AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1472AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1489AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1591AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1595AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1595AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1623AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1636AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1637AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1686AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1691AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1765AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1766AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1912AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1932AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1933AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1934AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1935AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "5276AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "5483AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "5484AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "5490AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "151134AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "151217AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "151331AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "151596AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "151750AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159364AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159391AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159595AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159611AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159612AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159619AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159627AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159628AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159728AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159739AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159837AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159853AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159896AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159897AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159899AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159900AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159901AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159902AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "8ff8a983-2a3d-4c56-936d-06e5496ffcd9",
                "d8c2d969-bd65-4d39-9434-ebbb6195852d",
                "91ab29ba-bcaa-4873-86fb-4317fcc0c8bd",
                "37da9e7a-ce74-4223-8073-2ead09c1391d",
                "4139f1dd-3157-4e40-97ec-56768bb0bdc1",
                "492c1b4c-dc68-479e-8700-8a9160211db6",
                "0ba0f575-ec75-4a51-86c4-97b6ce467684",
                "159920AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159922AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160227AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160326AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160333AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160351AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "160413AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> addedSetMembersUuidsToDosingUnits() {
        return Arrays.asList(
                "162335AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162376AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162351AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162354AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162355AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162356AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162357AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162358AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162359AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161554AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162360AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162362AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162363AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162364AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162365AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162366AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161553AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162263AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162371AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162372AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162374AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162375AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1513AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162378AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162379AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162380AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> addedSetMembersUuidsToUrinalysisOrder() {
        return Arrays.asList(
                "e8710a77-23c0-4a27-bf6b-559ff4d4a0d4",
                "56AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c7af0007-3193-4770-9e0c-76ce046cf7fc"
        );
    }
    private static List<String> addedSetMembersUuidsToOvaTest() {
        return Arrays.asList(
                "824a7421-210c-4b7a-818f-b82e0b361d95",
                "fc27856d-d8c1-4db3-a7a8-6623dd30a4a7",
                "4d025f49-a660-4f28-b6a1-087101e4f03b",
                "8412007b-201c-49b6-839e-4b7f55102142",
                "925da9ee-f436-445f-ada3-a71b7131222d",
                "6d351243-7792-4498-930a-daf365723573",
                "62eff937-603f-41a0-89ae-098759ce110e",
                "beb0f91d-f2c6-4a04-a480-c04af2cb0945",
                "784f2e5c-068d-4e4c-aa6f-a8026135b600",
                "598673d3-d185-4290-8e09-a68668a36b69",
                "fdef603d-4cef-4eaa-acf3-3d6408110804"
        );
    }

    private static List<String> getUuidConceptsToChageToMmolL() {
        return Arrays.asList(
                "160912AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "887AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "d0d107f7-9452-4129-a209-b9a4d1b46d4a"
                );
    }

    private static List<String> getUuidConceptsToAntenatalCareProgram() {
        return Arrays.asList(
                "299AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "21AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "300AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161470AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1356AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "302AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> getUuidConceptsToUrinePhysicalExamination() {
        return Arrays.asList(
                "be405745-bac7-4ead-a3aa-7c2fa91d7e9d",
                "163684AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161440AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "163682AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "1875AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161438AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "162096AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161439AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "161442AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "163680AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159734AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "159733AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> getUuidConceptsToStoolForOvaandCyst() {
        return Arrays.asList(
                "1368AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "716AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "182AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "709AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }

    private static List<String> getUuidConceptsToUrineMicroscopyDeposits() {
        return Arrays.asList(
                "165561AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "163686AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "c55d1bcf-5e79-44f3-ac8c-de99cb7bebc3",
                "6ed1b116-6d67-4995-8515-6470cc6b88a0",
                "163693AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "a34cbd53-0cca-40b3-a9ec-c34be876a2b6"
        );
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
    public static void doGeneralSetConversionAndSetup(String conceptSetUuid, List<String> stringList){
        ConceptService conceptService = Context.getConceptService();
        Concept setConcept = conceptService.getConceptByUuid(conceptSetUuid);
        List<Integer> setConceptIdsFromSetMembers = new ArrayList<Integer>();
        List<Integer>  setMembersToBeAdded = new ArrayList<Integer>();
        for(ConceptSet conceptSet1: setConcept.getConceptSets()) {
            setConceptIdsFromSetMembers.add(conceptSet1.getConcept().getConceptId());
        }
        for(String uuids: stringList) {
            Concept partOf = conceptService.getConceptByUuid(uuids);
            if(partOf != null) {
                setMembersToBeAdded.add(partOf.getConceptId());
            }
        }
        //loop through the list to be added vs what is available already
        //if the element is already available, just skip
        for (Integer index : setMembersToBeAdded) {
            if(!setConceptIdsFromSetMembers.contains(index)) {
                setConcept.addSetMember(conceptService.getConcept(index));
                setConcept.setChangedBy(Context.getAuthenticatedUser());
                setConcept.setDateChanged(new Date());
                //save the concept back into the data model
                conceptService.saveConcept(setConcept);
            }
        }

    }
    public static void addSetsToServiceOrderedConcept() {
        doGeneralSetConversionAndSetup("31AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", addedSetMembersUuidsToSrviceOrdered());
    }

    public static void addSetsToDosingUnits() {
        doGeneralSetConversionAndSetup("162384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", addedSetMembersUuidsToDosingUnits());
    }

    public static void addSetsToUrinalysisOrder() {
        doGeneralSetConversionAndSetup("ce5068f1-0ecc-43a8-95c4-cf876dec79bc", addedSetMembersUuidsToUrinalysisOrder());
    }
    public static void addSetsToOvaOrder() {
        doGeneralSetConversionAndSetup("716AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", addedSetMembersUuidsToOvaTest());
    }
    public static void changeTestsUnits() {
        ConceptService conceptService = Context.getConceptService();
        AdministrationService administrationService = Context.getAdministrationService();
        for(String string: getUuidConceptsToChageToMmolL()) {
            Concept concept = conceptService.getConceptByUuid(string);
            administrationService.executeSQL("UPDATE concept_numeric SET units='Mmol/L' WHERE concept_id="+concept.getConceptId(), false);
        }

    }
    /////////Add the Antenatal Care Profile member sets here///////////////////////
    public static void addSetsToAntenatalCareProfileOrder() {
        doGeneralSetConversionAndSetup("fa2b4427-820c-46d3-bf91-7fca6b7cfcbe", getUuidConceptsToAntenatalCareProgram());
    }

    public static void addSetsToUrinePhysicalExamination() {
        doGeneralSetConversionAndSetup("e8710a77-23c0-4a27-bf6b-559ff4d4a0d4", getUuidConceptsToUrinePhysicalExamination());
    }

    public static void addSetsToStoolForOvaandCyst() {
        doGeneralSetConversionAndSetup("a02587cb-82dd-4ee3-90db-0c3f16f247c0", getUuidConceptsToStoolForOvaandCyst());
    }

    public static void addSetsToUrineMicroscopyDeposits() {
        doGeneralSetConversionAndSetup("c7af0007-3193-4770-9e0c-76ce046cf7fc", getUuidConceptsToUrineMicroscopyDeposits());
    }

    public static void addAnswersToQuestions(String conceptQuestionUuid, List<String> stringListAnswers){
        ConceptService conceptService = Context.getConceptService();
        Concept question = conceptService.getConceptByUuid(conceptQuestionUuid);
        Concept conceptAnswer;
        List<Integer> answersToAquestion = new ArrayList<Integer>();
        //pick all the answers for this question

        for(ConceptAnswer answer : question.getAnswers()){
            //poppulate a list
            answersToAquestion.add(answer.getConcept().getConceptId());
        }
        for(String uuid: stringListAnswers) {
            conceptAnswer = conceptService.getConceptByUuid(uuid);

            if(!answersToAquestion.contains(conceptAnswer.getConceptId())) {
                //add this as an answer to the question given
                ConceptAnswer conceptAnswerObject = new ConceptAnswer();
                conceptAnswerObject.setAnswerConcept(conceptAnswer);
                conceptAnswerObject.setCreator(Context.getAuthenticatedUser());
                conceptAnswerObject.setDateCreated(new Date());

                //add the question to this answer
                question.addAnswer(conceptAnswerObject);

                //save only if there is no any other answer that has similar question and answer
                //loop through all the answers for this question

                conceptService.saveConcept(question);
            }


        }
    }

    public static void addAnswersToRadiologyDepartment() {
        doGeneralSetConversionAndSetup("160463AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", radiologyDepartmentTests());
    }

    public static void addAnswersToProcedurePerformed() {
        addAnswersToQuestions("1651AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", procedurePerformed());
    }

    private static List<String> typhoidRDT() {
        return Arrays.asList(
                "703AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "664AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        );
    }
    public static void addAnswersToTyphoidRDT() {
        addAnswersToQuestions("165562AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", typhoidRDT());
    }

    //concepts to be retired
    private static List<String> conceptsToRetire() {
        return Arrays.asList(
                "7edfca4f-4690-45c8-8569-71e8b49f52f5", "5bc4313c-b70c-4c1a-ab30-6cacb0220160"
        );
    }
    public static void conceptsToRetireFromDb() {
        ConceptService conceptService = Context.getConceptService();
        for(String str:conceptsToRetire()) {
            Concept concept = conceptService.getConceptByUuid(str);
            if(concept != null) {
                concept.setRetired(true);
                concept.setRetiredBy(Context.getAuthenticatedUser());
                concept.setRetireReason("Duplicate and unwanted concept, already provided by CIEL");
                concept.setDateRetired(new Date());

                //commit to the database
                conceptService.saveConcept(concept);
            }

        }
    }
    public static void addAnswersToSalmonellaTyphiRapidDiagnosisTest() {
        addAnswersToQuestions("165562AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", typhoidRDT());
    }

}
