<%
    ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])
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
</div>
