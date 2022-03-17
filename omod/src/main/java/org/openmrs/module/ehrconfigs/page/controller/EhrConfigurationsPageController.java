package org.openmrs.module.ehrconfigs.page.controller;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;


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

    }
}
