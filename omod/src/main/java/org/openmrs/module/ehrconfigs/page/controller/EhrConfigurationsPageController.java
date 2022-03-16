package org.openmrs.module.ehrconfigs.page.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.WaiverType;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@AppPage("ehrconfigs.home")
public class EhrConfigurationsPageController {
    public void controller(UiUtils ui, PageModel model, @RequestParam(required = false, value = "section") String section) {

        String selection = null;

        if (StringUtils.isEmpty(section)) {
            section = "manageWaivers";
        }
        selection = "section-" + section;

        model.addAttribute("section", section);
        model.addAttribute("selection", selection);
        model.addAttribute("waiverTypes", new ArrayList<WaiverType>());
        model.addAttribute("waiverRoles", Context.getUserService().getAllRoles());

        BillingService billingService = Context.getService(BillingService.class);
        WaiverType waiverType = new WaiverType();
        waiverType.setName("Hospital Waiver");
        waiverType.setUserRole(Context.getUserService().getRole("EHR Edit Bill"));
        waiverType.setDescription("Clients exempted from paying any services at the facility");
        billingService.saveWaiverType(waiverType);
        System.out.println(waiverType);

    }
}
