package org.openmrs.module.ehrconfigs.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.WaiverType;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ManageWaiversFragmentController {
    public void controller(FragmentModel model) {

        model.addAttribute("waiverTypes", Context.getService(BillingService.class).getWaiverTypes());
        model.addAttribute("waiverRoles", Context.getUserService().getAllRoles());

    }

    public void createWaiverType(@RequestParam(required = false, value = "waiverName") String waiverName, @RequestParam(required = false, value = "userRole") String role, @RequestParam(required = false, value = "waiverDescription") String waiverDescription) {
        BillingService billingService = Context.getService(BillingService.class);
        WaiverType waiverType = new WaiverType();
        waiverType.setName(waiverName);
        waiverType.setUserRole(Context.getUserService().getRole(role));
        waiverType.setDescription(waiverDescription);
        billingService.saveWaiverType(waiverType);
    }

}
