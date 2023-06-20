<script>
    var jq = jQuery;
    jq(function () {
    var facilityDialog = emr.setupConfirmationDialog({
                dialogOpts: {
                    overlayClose: false,
                    close: true
                },
                selector: '#edit-facility-details-dialog',
                actions: {
                    confirm: function () {
                        jq.getJSON('${ui.actionLink("ehrconfigs", "facilityDetails", "addFacilityDetails")}',
                            {
                                'countyCode': jq("#countyCode").val().trim(),
                                'countyName': jq("#countyName").val().trim(),
                                'website': jq("#website").val().trim(),
                                'address': jq("#address").val(),
                                'email': jq("#email").val(),
                                'phone': jq("#phone").val(),
                            }
                        ).success(function (data) {
                            facilityDialog.close();
                            location.reload();
                        });
                    },
                    cancel: function () {
                        facilityDialog.close();
                    }
                }
            });

            jq("#editFacilityDetailsBtn").on("click", function (e) {
                e.preventDefault();
                facilityDialog.show();
            });
    });
</script>


<div id="edit-facility-details-dialog" class="dialog">
        <div class="dialog-header">
            <i class="icon-folder-open"></i>
            <h3>Edit Facility Details</h3>
        </div>
        <form class="dialog-content">
            <ul>
                <li>
                    <label for="countyCode">County Code</label>
                    <input name="countyCode" id="countyCode" type="text" />
                </li>
                <li>
                    <label for="countyName">County Name</label>
                    <input name="countyName" id="countyName" type="text" />
                </li>
                <li>
                    <label for="website">Website</label>
                    <input name="website" id="website" type="text" />
                </li>

                <li>
                    <label for="address">Address</label>
                    <input name="address" id="address" type="text" />
                </li>

                <li>
                    <label for="email">Email</label>
                    <input name="email" id="email" type="text" />
                </li>

                <li>
                    <label for="phone">Phone</label>
                    <input name="phone" id="phone" type="text" />
                </li>

            </ul>
            <span class="button confirm right">Confirm</span>
            <span class="button cancel">Cancel</span>
       </form>
</div>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Facility Details</div>

    <div class="ke-panel-content">
        <div style="float: left;">
            <button id="editFacilityDetailsBtn" class="task">Edit Facility Details</button>
        </div>
        <ul>
            <li>
                <label>County Code</label>
                ${countyCode}
            </li>
            <li>
                <label for="countyName">County Name</label>
                ${countyName}
            </li>
            <li>
                <label>Website</label>
                ${website}
            </li>
            <li>
                <label>Address</label>
                ${address}
            </li>
            <li>
                <label>Email</label>
                ${email}
            </li>
            <li>
                <label>Phone</label>
                ${phone}
            </li>

        </ul>
    </div>
</div>
