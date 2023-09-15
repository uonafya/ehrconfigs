SET SQL_SAFE_UPDATES = 0;
INSERT INTO migration_tr.tr_vitals(
Person_Id,
Encounter_Date,
location_id,
encounter_provider,
date_created,
Weight,
Systolic_pressure,
Diastolic_pressure,
Height,
Temperature,
Pulse_rate,
Respiratory_rate,
Oxygen_saturation,
Muac,
voided
)
select
e.patient_id,
date(max(e.encounter_datetime)) as visit_date,
max(e.location_id) as location_id,
null,
max(e.date_created) as date_created,
max(if(o.concept_id=5089,o.value_numeric,null)) as weight,
max(if(o.concept_id=5085,o.value_numeric,null)) as systolic_pressure,
max(if(o.concept_id=5086,o.value_numeric,null)) as diastolic_pressure,
max(if(o.concept_id=5090,o.value_numeric,null)) as height,
max(if(o.concept_id=5088,o.value_numeric,null)) as temperature,
max(if(o.concept_id=5087,o.value_numeric,null)) as pulse_rate,
max(if(o.concept_id=5242,o.value_numeric,null)) as respiratory_rate,
max(if(o.concept_id=5092,o.value_numeric,null)) as oxygen_saturation,
max(if(o.concept_id=1343,o.value_numeric,null)) as muac,
max(e.voided) as voided
from encounter e
	inner join person p on p.person_id=e.patient_id and p.voided=0
inner join encounter_type ect on ect.encounter_type_id = e.encounter_type and ect.uuid ='2af60550-f291-11ea-b725-9753b5f685ae'
left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0
	and o.concept_id in (5089,5085,5086,5090,5088,5087,5242,5092,1343)
where e.voided=0
group by e.patient_id;

SET SQL_SAFE_UPDATES = 1;