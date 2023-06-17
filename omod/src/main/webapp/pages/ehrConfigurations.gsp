<%
    ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])
    ui.includeJavascript("financials", "jquery.dataTables.min.js")
    ui.includeJavascript("ehrconfigs", "emr.js")
//    simplemodal plugin: http://www.ericmmartin.com/projects/simplemodal/
    ui.includeJavascript("ehrconfigs", "jquery.simplemodal.1.4.4.min.js")

    ui.includeCss("financials", "jquery.dataTables.min.css")
    ui.includeCss("ehrconfigs", "referenceapplication.css")

    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeCss("ehrconfigs", "custom.css")


    def menuItems = [
            [label       : "Back to home",
             iconProvider: "kenyaui",
             icon        : "buttons/back.png",
             href        : ui.pageLink("kenyaemr", "userHome")
            ],
            [
                    label       : "Drug Categories",
                    href        : ui.pageLink("ehrconfigs", "ehrConfigurations", [section: "manageDrugCategories"]),
                    active      : (selection == "section-manageDrugCategories"),
                    icon        : "buttons/pharmacy_summary.png"
            ],
            [
                    label       : "Drug and formulations",
                    href        : ui.pageLink("ehrconfigs", "ehrConfigurations", [section: "manageDrugFormulations"]),
                    active      : (selection == "section-manageDrugFormulations"),
                    icon        : "buttons/pharmacy_summary.png"
            ],
            [
                    label: "Add Formulation To Drug",
                    href: ui.pageLink("ehrconfigs", "ehrConfigurations", [section: "manageFormulationDrugAddition"]),
                    active      : (selection == "section-manageFormulationDrugAddition"),
            ],
            [
                    label: "Food Processing",
                    href: ui.pageLink("ehrconfigs", "ehrConfigurations", [section: "manageFoodProcess"]),
                    active      : (selection == "section-manageFoodProcess"),
            ]
    ]
%>
<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Configurations", items: menuItems])}
</div>

<div class="ke-page-content">
    <% if (section == "manageDrugFormulations") { %>
    ${ui.includeFragment("ehrconfigs", "manageDrugFormulations")}
    <% } %>
    <% if (section == "manageFoodProcess") { %>
    ${ui.includeFragment("ehrconfigs", "manageFoodProcess")}
    <% } %>
    <% if (section == "manageFormulationDrugAddition") { %>
    ${ui.includeFragment("ehrconfigs", "manageFormulationDrugAddition")}
    <% } %>
    <% if (section == "manageDrugCategories") { %>
    ${ui.includeFragment("ehrconfigs", "manageDrugCategories")}
    <% } %>
</div>
