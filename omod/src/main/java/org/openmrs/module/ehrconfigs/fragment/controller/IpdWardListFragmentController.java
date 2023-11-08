package org.openmrs.module.ehrconfigs.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class IpdWardListFragmentController {

    public void controller(FragmentModel model) {
        model.addAttribute("wards", Context.getService(IpdService.class).getAvailableWards());
    }
}
