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
                    label       : "Waivers",
                    href        : ui.pageLink("ehrconfigs", "ehrConfigurations", [section: "manageWaivers"]),
                    active      : (selection == "section-manageWaivers"),
                    iconProvider: "financials",
                    icon        : "buttons/pharmacy_summary.png"
            ],
            [
                    label       : "Drug formulations",
                    href        : ui.pageLink("ehrconfigs", "ehrConfigurations", [section: "manageDrugFormulations"]),
                    active      : (selection == "section-manageDrugFormulations"),
            ]
    ]
%>
<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Configurations", items: menuItems])}
</div>

<div class="ke-page-content">

    <% if (section == "manageWaivers") { %>
    ${ui.includeFragment("ehrconfigs", "manageWaivers")}
    <% } %>
    <% if (section == "manageDrugFormulations") { %>
    ${ui.includeFragment("ehrconfigs", "manageDrugFormulations")}
    <% } %>
</div>
