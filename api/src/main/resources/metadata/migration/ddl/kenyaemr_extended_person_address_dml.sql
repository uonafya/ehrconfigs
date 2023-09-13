use openmrs;
SET SQL_SAFE_UPDATES = 0;
INSERT INTO kenyaemr_extended_person_address(
      uuid,
      person_uuid,
      patient_id,
      county,
      sub_county,
      location,
      ward,
      sub_location,
      village,
      postal_address,
      land_mark,
      voided
    )
      select
        pa.uuid,
        pt.uuid
        pa.person_id,
        coalesce(pa.country,pa.county_district) county,
        pa.state_province sub_county,
        pa.address6 location,
        pa.address4 ward,
        pa.address5 sub_location,
        pa.city_village village,
        pa.address1 postal_address,
        pa.address2 land_mark,
        pa.voided voided
      from person_address pa
        inner join person pt on pt.person_id=pa.person_id and pt.voided=0
      where pa.voided=0
    ;
SET SQL_SAFE_UPDATES = 1;