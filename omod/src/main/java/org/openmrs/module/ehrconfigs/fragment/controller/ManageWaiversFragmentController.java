package org.openmrs.module.ehrconfigs.fragment.controller;

import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.WaiverType;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class ManageWaiversFragmentController {
    public void controller(FragmentModel model) {

        model.addAttribute("waiverTypes", Context.getService(BillingService.class).getWaiverTypes());
        model.addAttribute("waiverRoles", Context.getUserService().getAllRoles());

    }

    public void createWaiverType(String name, String userRole, String description) {
        BillingService billingService = Context.getService(BillingService.class);
        WaiverType waiverType = new WaiverType();
        waiverType.setName(name);
        waiverType.setUserRole(Context.getUserService().getRole(userRole));
        waiverType.setDescription(description);
        billingService.saveWaiverType(waiverType);
    }

}
