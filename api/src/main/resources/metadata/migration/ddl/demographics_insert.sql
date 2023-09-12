insert into kenyaemr_extended_patient_demographics(
    patient_id,
    uuid,
    given_name,
    middle_name,
    family_name,
    Gender,
    DOB,
    dead,
    date_created,
    date_last_modified,
    voided,
    death_date
    )
select
       p.person_id,
       p.uuid,
       p.given_name,
       p.middle_name,
       p.family_name,
       p.gender,
       p.birthdate,
       p.dead,
       p.date_created,
       if((p.date_last_modified='0000-00-00 00:00:00' or p.date_last_modified=p.date_created),NULL,p.date_last_modified) as date_last_modified,
       p.voided,
       p.death_date
FROM (
     select
            p.person_id,
            p.uuid,
            pn.given_name,
            pn.middle_name,
            pn.family_name,
            p.gender,
            p.birthdate,
            p.dead,
            p.date_created,
            greatest(ifnull(p.date_changed,'0000-00-00 00:00:00'),ifnull(pn.date_changed,'0000-00-00 00:00:00')) as date_last_modified,
            p.voided,
            p.death_date
     from person p
            left join patient pa on pa.patient_id=p.person_id
            left join person_name pn on pn.person_id = p.person_id and pn.voided=0
     where p.voided=0
     GROUP BY p.person_id
     ) p
ON DUPLICATE KEY UPDATE given_name = p.given_name, middle_name=p.middle_name, family_name=p.family_name;

-- update etl_patient_demographics with patient attributes: birthplace, citizenship, mother_name, phone number and kin's details
update kenyaemr_extended_patient_demographics d
left outer join
(
select
       pa.person_id,
       max(if(pat.uuid='8d8718c2-c2cc-11de-8d13-0010c6dffd0f', pa.value, null)) as birthplace,
       max(if(pat.uuid='8d871afc-c2cc-11de-8d13-0010c6dffd0f', pa.value, null)) as citizenship,
       max(if(pat.uuid='8d871d18-c2cc-11de-8d13-0010c6dffd0f', pa.value, null)) as Mother_name,
       max(if(pat.uuid='b2c38640-2603-4629-aebd-3b54f33f1e3a', pa.value, null)) as phone_number,
       max(if(pat.uuid='342a1d39-c541-4b29-8818-930916f4c2dc', pa.value, null)) as next_of_kin_contact,
       max(if(pat.uuid='d0aa9fd1-2ac5-45d8-9c5e-4317c622c8f5', pa.value, null)) as next_of_kin_relationship,
       max(if(pat.uuid='7cf22bec-d90a-46ad-9f48-035952261294', pa.value, null)) as next_of_kin_address,
       max(if(pat.uuid='830bef6d-b01f-449d-9f8d-ac0fede8dbd3', pa.value, null)) as next_of_kin_name,
       max(if(pat.uuid='b8d0b331-1d2d-4a9a-b741-1816f498bdb6', pa.value, null)) as email_address,

       max(if(pat.uuid='e191b0b8-f069-11ea-b498-2bfd800847e8', pa.value, null)) as paying_category_type,
       max(if(pat.uuid='0a8ae818-f06a-11ea-ab82-2f183f30d954', pa.value, null)) as non_paying_category_type,
       max(if(pat.uuid='341ee8fa-f06a-11ea-aca0-03d040bd88c8', pa.value, null)) as special_scheme_category_type,
       max(if(pat.uuid='09cd268a-f0f5-11ea-99a8-b3467ddbf779', pa.value, null)) as payment_category,
       max(if(pat.uuid='858781dc-282f-11eb-8741-8ff5ddd45b7c', pa.value, null)) as file_number,
       max(if(pat.uuid='a804c03e-f1bc-11ea-ae43-dfa0f52ad887', pa.value, null)) as waiver_number,
       max(if(pat.uuid='a22892ce-f1d2-11ea-a512-138a123d324a', pa.value, null)) as exemption_number,
       max(if(pat.uuid='bde64e04-026f-11eb-bb17-73c196be052b', pa.value, null)) as nhif_card_number,
       max(if(pat.uuid='88546440-0271-11eb-b43f-c392cfe8f5df', pa.value, null)) as student_id,
       max(if(pat.uuid='972a32aa-6159-11eb-bc2d-9785fed39154', pa.value, null)) as payment_sub_category,
      greatest(ifnull(pa.date_changed,'0000-00-00'),pa.date_created) as latest_date
from person_attribute pa
       inner join
         (
         select
                pat.person_attribute_type_id,
                pat.name,
                pat.uuid
         from person_attribute_type pat
         where pat.retired=0
         ) pat on pat.person_attribute_type_id = pa.person_attribute_type_id
                    and pat.uuid in (
        '8d8718c2-c2cc-11de-8d13-0010c6dffd0f', -- birthplace
        '8d871afc-c2cc-11de-8d13-0010c6dffd0f', -- citizenship
        '8d871d18-c2cc-11de-8d13-0010c6dffd0f', -- mother's name
        'b2c38640-2603-4629-aebd-3b54f33f1e3a', -- telephone contact
        '342a1d39-c541-4b29-8818-930916f4c2dc', -- next of kin's contact
        'd0aa9fd1-2ac5-45d8-9c5e-4317c622c8f5', -- next of kin's relationship
        '7cf22bec-d90a-46ad-9f48-035952261294', -- next of kin's address
        '830bef6d-b01f-449d-9f8d-ac0fede8dbd3', -- next of kin's name
        'b8d0b331-1d2d-4a9a-b741-1816f498bdb6', -- email address

        'e191b0b8-f069-11ea-b498-2bfd800847e8', -- paying_category_type
        '0a8ae818-f06a-11ea-ab82-2f183f30d954', -- non_paying_category_type
        '341ee8fa-f06a-11ea-aca0-03d040bd88c8' -- special_scheme_category_type
        '09cd268a-f0f5-11ea-99a8-b3467ddbf779' -- payment_category
        '858781dc-282f-11eb-8741-8ff5ddd45b7c' -- file_number
        'a804c03e-f1bc-11ea-ae43-dfa0f52ad887' -- waiver_number
        'a22892ce-f1d2-11ea-a512-138a123d324a' -- exemption_number
        'bde64e04-026f-11eb-bb17-73c196be052b' -- nhif_card_number
        '88546440-0271-11eb-b43f-c392cfe8f5df' -- student_id
        '972a32aa-6159-11eb-bc2d-9785fed39154' -- payment_sub_category

        )
where pa.voided=0
group by pa.person_id
) att on att.person_id = d.patient_id
set d.phone_number=att.phone_number,
    d.next_of_kin=att.next_of_kin_name,
    d.next_of_kin_relationship=att.next_of_kin_relationship,
    d.next_of_kin_phone=att.next_of_kin_contact,
    d.phone_number=att.phone_number,
    d.birth_place = att.birthplace,
    d.citizenship = att.citizenship,
    d.email_address=att.email_address,
    d.paying_category_type=att.paying_category_type,
    d.non_paying_category_type=att.non_paying_category_type,
    d.special_scheme_category_type=att.special_scheme_category_type,
    d.payment_category=att.payment_category,
    d.file_number=att.file_number,
    d.waiver_number=att.waiver_number,
    d.exemption_number=att.exemption_number,
    d.nhif_card_number=att.nhif_card_number,
    d.student_id=att.student_id,
    d.payment_sub_category=att.payment_sub_category,
    d.date_last_modified=if(att.latest_date > ifnull(d.date_last_modified,'0000-00-00'),att.latest_date,d.date_last_modified)
;


update kenyaemr_extended_patient_demographics d
join (select pi.patient_id,
             coalesce (
             max(if(pit.uuid='b4d66522-11fc-45c7-83e3-39a1af21ae0d',pi.identifier,null)) patient_clinic_number,
             max(if(pit.uuid='49af6cdc-7968-4abb-bf46-de10d7f4859f',pi.identifier,null)) national_id,
             max(if(pit.uuid='6428800b-5a8c-4f77-a285-8d5f6174e5fb',pi.identifier,null)) huduma_number,
             max(if(pit.uuid='be9beef6-aacc-4e1f-ac4e-5babeaa1e303',pi.identifier,null)) Passport_number,
             max(if(pit.uuid='68449e5a-8829-44dd-bfef-c9c8cf2cb9b2',pi.identifier,null)) Birth_cert_number,
             max(if(pit.uuid='dfacd928-0370-4315-99d7-6ec1c9f7ae76',pi.identifier,null)) openmrs_id,
             max(if(pit.uuid='ca125004-e8af-445d-9436-a43684150f8b',pi.identifier,null)) driving_license_no,
             max(if(pit.uuid='f85081e2-b4be-4e48-b3a4-7994b69bb101',pi.identifier,null)) national_unique_patient_identifier,
             max(if(pit.uuid='61A354CB-4F7F-489A-8BE8-09D0ACEDDC63',pi.identifier,null)) patient_opd_number,
             greatest(ifnull(max(pi.date_changed),'0000-00-00'),max(pi.date_created)) as latest_date
      from patient_identifier pi
             join patient_identifier_type pit on pi.identifier_type=pit.patient_identifier_type_id
      where voided=0
      group by pi.patient_id) pid on pid.patient_id=d.patient_id
set d.unique_patient_no=pid.upn,
    d.national_id_no=pid.National_id,
    d.huduma_no=pid.huduma_number,
    d.passport_no=pid.passport_number,
    d.birth_certificate_no=pid.Birth_cert_number,
    d.patient_clinic_number=pid.Patient_clinic_number,
    d.openmrs_id=pid.openmrs_id,
    d.driving_license_no=pid.driving_license_no,
    d.national_unique_patient_identifier=pid.national_unique_patient_identifier,
    d.patient_opd_number=pid.patient_opd_number,
    d.date_last_modified=if(pid.latest_date > ifnull(d.date_last_modified,'0000-00-00'),pid.latest_date,d.date_last_modified)
;

update kenyaemr_extended_patient_demographics d
join (select o.person_id as patient_id,
             max(if(o.concept_id in(1054),cn.name,null))  as marital_status,
             max(if(o.concept_id in(1712),cn.name,null))  as education_level,
             max(if(o.concept_id in(1542),cn.name,null))  as occupation,
             max(o.date_created) as date_created
                   from obs o
             join concept_name cn on cn.concept_id=o.value_coded and cn.concept_name_type='FULLY_SPECIFIED'
                                       and cn.locale='en'
      where o.concept_id in (1054,1712,1542) and o.voided=0
      group by person_id) pstatus on pstatus.patient_id=d.patient_id
set d.marital_status=pstatus.marital_status,
    d.education_level=pstatus.education_level,
    d.occupation=pstatus.occupation,
    d.date_last_modified=if(pstatus.date_created > ifnull(d.date_last_modified,'0000-00-00'),pstatus.date_created,d.date_last_modified)
;
