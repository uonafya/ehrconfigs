package org.openmrs.module.ehrconfigs.fragment.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ehrconfigs.EHRConfigurationConstants;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class FacilityDetailsFragmentController {

    public void controller(FragmentModel model) {
        AdministrationService administrationService = Context.getAdministrationService();
        model.addAttribute("countyCode", administrationService.getGlobalProperty(EHRConfigurationConstants.GP_PROPERTY_COUNTY_CODE, ""));
        model.addAttribute("countyName", administrationService.getGlobalProperty(EHRConfigurationConstants.GP_PROPERTY_COUNTY_NAME, ""));
        model.addAttribute("address", administrationService.getGlobalProperty(EHRConfigurationConstants.GP_PROPERTY_COUNTY_ADDRESS, ""));
        model.addAttribute("email", administrationService.getGlobalProperty(EHRConfigurationConstants.GP_PROPERTY_COUNTY_EMAIL, ""));
        model.addAttribute("phone", administrationService.getGlobalProperty(EHRConfigurationConstants.GP_PROPERTY_COUNTY_PHONE, ""));
        model.addAttribute("website", administrationService.getGlobalProperty(EHRConfigurationConstants.GP_PROPERTY_COUNTY_WEBSITE, ""));
    }
    public void addFacilityDetails(
            @RequestParam(required = false, value = "countyCode") String countyCode,
            @RequestParam(required = false, value = "countyName") String countyName,
            @RequestParam(required = false, value = "address") String address,
            @RequestParam(required = false, value = "email") String email,
            @RequestParam(required = false, value = "phone") String phone,
            @RequestParam(required = false, value = "website") String website
    ) {
        AdministrationService administrationService = Context.getAdministrationService();
        if(StringUtils.isNotBlank(countyCode)){
            GlobalProperty gpCountyCode = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_CODE);
            gpCountyCode.setPropertyValue(countyCode);
            administrationService.saveGlobalProperty(gpCountyCode);
        }
        if(StringUtils.isNotBlank(countyName)){
            GlobalProperty gpCountyName = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_NAME);
            gpCountyName.setPropertyValue(countyName);
            administrationService.saveGlobalProperty(gpCountyName);
        }
        if(StringUtils.isNotBlank(address)){
            GlobalProperty gpAddress = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_ADDRESS);
            gpAddress.setPropertyValue(address);
            administrationService.saveGlobalProperty(gpAddress);
        }
        if(StringUtils.isNotBlank(email)){
            GlobalProperty gpEmail = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_EMAIL);
            gpEmail.setPropertyValue(email);
            administrationService.saveGlobalProperty(gpEmail);
        }
        if(StringUtils.isNotBlank(phone)){
            GlobalProperty gpPhone = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_PHONE);
            gpPhone.setPropertyValue(phone);
            administrationService.saveGlobalProperty(gpPhone);
        }
        if(StringUtils.isNotBlank(website)){
            GlobalProperty gpWebsite = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_WEBSITE);
            gpWebsite.setPropertyValue(website);
            administrationService.saveGlobalProperty(gpWebsite);
        }
    }
}
