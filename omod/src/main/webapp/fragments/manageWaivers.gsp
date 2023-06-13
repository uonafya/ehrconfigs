<script>
    var jq = jQuery;
    jq(function () {
        jq("#details").DataTable();

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
                    var name = jq('#waiverName').val();
                    var description = jq('#waiverDescription').val();
                    var userRole = jq('#role').val();
                    jq.getJSON('${ui.actionLink("ehrconfigs", "manageWaivers", "createWaiverType")}',
                        {
                            'waiverName': name,
                            'waiverDescription': description,
                            'userRole': userRole
                        }
                    ).success(function (data) {
                        console.log("Data saved", data);
                    });

                    waiverDialog.close();
                },
                cancel: function () {
                    waiverDialog.close();
                }
            }
        });

        jQuery("#newWaiverType").on("click", function (e) {
            alert("we got covered");
            console.log("This button is reached by a click");
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
        <div>
            <button id="newWaiverType" class="task">
                New Waiver Type
            </button>
        </div>
        <table id="details">
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Required roles</th>
                <th>Action</th>
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
        </table>

    <div id="waiver-dialog" class="dialog" style="display:none;">
        <div class="dialog-header">
            <i class="icon-folder-open"></i>

            <h3>New Waiver Type</h3>
        </div>

        <div class="dialog-content">
            <ul>
                <li>
                    <label>Name<span>*</span></label>
                    <input id="waiverName" name="waiverName" type="text" style="width: 90%!important;">
                </li>
                <li>
                    <label>Description<span>*</span></label>
                    <input id="waiverDescription" name="waiverDescription" type="text"
                           style="width: 90%!important;">
                </li>

                <li>
                    <label>Associated user Role<span>*</span></label>
                    <select name="role" id="role" style="width: 90%!important;">
                        <option disabled>--Please Select--</option>
                        <% waiverRoles.each { role -> %>
                        <option value="${role.role}">${role.role}</option>
                        <% } %>
                    </select>

                </li>
            </ul>

            <label class="button confirm right" id="saveWaiverType">Confirm</label>
            <label class="button cancel">Cancel</label>
        </div>
    </div>

    </div>
</div>