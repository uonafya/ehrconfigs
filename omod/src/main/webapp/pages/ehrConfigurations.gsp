<%
    ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])
    def menuItems = [
            [label       : "Back to home",
             iconProvider: "kenyaui",
             icon        : "buttons/back.png",
             href        : ui.pageLink("kenyaemr", "userHome")
            ],
            [
                    label       : "Pharmacy Department Summaries",
                    href        : ui.pageLink("financials", "financials", [section: "pharmacyRevenueSummaries"]),
                    active      : (selection == "section-manageWaivers"),
                    iconProvider: "financials",
                    icon        : "buttons/pharmacy_summary.png"
            ]
    ]
%>
<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Configurations", items: menuItems])}
</div>

<div class="ke-page-content">

    <% if (section == "overview") { %>
    ${ui.includeFragment("ehrconfigs", "manageWaivers")}
    <% } %>
</div>
