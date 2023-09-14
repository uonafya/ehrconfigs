use openmrs;
SET SQL_SAFE_UPDATES = 0;
INSERT INTO kenyaemr_extended_triage_patient_data(
        person_uuid,
        triage_log_id,
        weight,
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
        mua,
        chest,
        abdominal,
        encounter_opd,
        BMI,
        oxygen_saturation,
        patient_id
    )
      select
        pt.uuid,
        tpd.triage_log_id,
        tpd.weight,
        tpd.height,
        tpd.temperature,
        tpd.systolic,
        tpd.daistolic,
        tpd.respiratory_rate,
        tpd.pulse_rate,
        tpd.blood_group,
        tpd.last_menstrual,
        tpd.rhesus_factor,
        tpd.pitct,
        tpd.created_on,
        tpd.mua,
        tpd.chest,
        tpd.abdominal,
        tpd.encounter_opd,
        tpd.BMI,
        tpd.oxygen_saturation,
        tpd.patient_id
      from triage_patient_data tpd
        inner join person pt on pt.person_id=tpd.patient_id and pt.voided=0
      where pt.voided=0
    ;
SET SQL_SAFE_UPDATES = 1;