<%
    ui.includeJavascript("financials", "jquery.dataTables.min.js")
    ui.includeCss("financials", "jquery.dataTables.min.css")
    ui.includeJavascript("ehrconfigs", "emr.js")
%>

<script>
    var jq = jQuery;
    jq(function () {
        jQuery("#details").DataTable();

        var waiverDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#waiver-dialog',
            actions: {
                confirm: function () {
                    if (!page_verified()) {
                        jq().toastmessage('showErrorToast', 'Ensure fields marked in red have been properly filled before you continue')
                        return false;
                    }
                    // TODO call post method to save new waiver
                    waiverDialog.close();
                },
                cancel: function () {
                    waiverDialog.close();
                }
            }
        });

        jq("#newWaiverType").on("click", function (e) {
            e.preventDefault();
            waiverDialog.show();
        });
    });

    function page_verified() {
        var error = 0;

        if (jq("#waiverName").val().trim() === '') {
            jq("#waiverName").addClass('red');
            error++;
        } else {
            jq("#waiverName").removeClass('red');
        }
        if (jq("#waiverDescription").val().trim() === '') {
            jq("#waiverDescription").addClass('red');
            error++;
        } else {
            jq("#waiverDescription").removeClass('red');
        }
        if (jq("#role").val().trim() === '') {
            jq("#role").addClass('red');
            error++;
        } else {
            jq("#role").removeClass('red');
        }

        if (error === 0) {
            return true;
        } else {
            return false;
        }
    }
</script>

<div class="ke-panel-frame" style="background-color: #ffffff">
    <div class="ke-panel-heading">Waiver Management</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <table id="details">
            <thead>
            <tr>
                <td>#</td>
                <td>Name</td>
                <th>Description</th>
                <th>Required roles</th>
            </tr>
            </thead>
            <tbody>
            <% if (waiverTypes.empty) { %>

            <tr>
                <td colspan="9">
                    No records found
                </td>
            </tr>
            <% } %>
            <% waiverTypes.each { %>
            <tr>
                <td>${it.name}</td>
                <td>${it.description}</td>
                <td>${it.userRole}</td>
                <td>Action</td>
            </tr>
            <% } %>
            </tbody>
            <tr>
                <button id="newWaiverType" class="success">New Type</button>
            </tr>
        </table>

        <div id="waiver-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>New Waiver</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Name<span>*</span></label>
                        <input id="waiverName" type="text">
                    </li>
                    <li>
                        <label>Description<span>*</span></label>
                        <input id="waiverDescription" type="text" style="width: 60px!important;">
                    </li>

                    <li>
                        <label>Associated user Role<span>*</span></label>
                        <select name="role" id="role" style="width: 200px;">
                            <option disabled>--Please Select--</option>
                            <% waiverRoles.each { role -> %>
                            <option value="${role.role}">${role.role}</option>
                            <% } %>
                        </select>

                    </li>
                </ul>

                <label class="button confirm right">Confirm</label>
                <label class="button cancel">Cancel</label>
            </div>
        </div>

    </div>
</div>