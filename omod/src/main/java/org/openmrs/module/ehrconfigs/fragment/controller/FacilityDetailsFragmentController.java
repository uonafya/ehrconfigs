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
        List<GlobalProperty> globalPropertyList = new ArrayList<GlobalProperty>();
        if(StringUtils.isNotBlank(countyCode)){
            GlobalProperty countyCodeGlobalProperty = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_CODE);
            countyCodeGlobalProperty.setPropertyValue(countyCode);
            globalPropertyList.add(countyCodeGlobalProperty);
        }
        if(StringUtils.isNotBlank(countyName)){
            GlobalProperty countyNameGlobalProperty = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_NAME);
            countyNameGlobalProperty.setPropertyValue(countyCode);
            globalPropertyList.add(countyNameGlobalProperty);
        }
        if(StringUtils.isNotBlank(address)){
            GlobalProperty addressGlobalProperty = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_ADDRESS);
            addressGlobalProperty.setPropertyValue(countyCode);
            globalPropertyList.add(addressGlobalProperty);
        }
        if(StringUtils.isNotBlank(email)){
            GlobalProperty emailGlobalProperty = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_EMAIL);
            emailGlobalProperty.setPropertyValue(countyCode);
            globalPropertyList.add(emailGlobalProperty);
        }
        if(StringUtils.isNotBlank(phone)){
            GlobalProperty phoneGlobalProperty = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_PHONE);
            phoneGlobalProperty.setPropertyValue(countyCode);
            globalPropertyList.add(phoneGlobalProperty);
        }
        if(StringUtils.isNotBlank(website)){
            GlobalProperty websiteGlobalProperty = administrationService.getGlobalPropertyObject(EHRConfigurationConstants.GP_PROPERTY_COUNTY_WEBSITE);
            websiteGlobalProperty.setPropertyValue(countyCode);
            globalPropertyList.add(websiteGlobalProperty);
        }

        if(!globalPropertyList.isEmpty()) {
            administrationService.saveGlobalProperties(globalPropertyList);
        }
    }
}
