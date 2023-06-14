package org.openmrs.module.ehrconfigs.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.ehrinventory.InventoryService;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.List;

public class ManageDrugFormulationsFragmentController {

    public void controller(FragmentModel model) {
        //model.addAttribute("formulation", getAllFormulations());
    }
    private List<InventoryDrugFormulation> getAllFormulations() {

        return Context.getService(InventoryService.class).listDrugFormulation("", 0, 1500);
    }

}

