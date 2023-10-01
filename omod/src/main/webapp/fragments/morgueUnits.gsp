<script>
    var jq = jQuery;
    jq(function () {
        jq("#unitsTable").DataTable();

        var unitsDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#new-drug-dialog',
            actions: {
                confirm: function () {
                    if (!page_verified_drug()) {
                        jq().toastmessage('showErrorToast', 'Ensure fields marked in red have been properly filled before you continue')
                        return false;
                    }
                    jq.getJSON('${ui.actionLink("morgueapp", "morgueDetail", "addMorgueUnits")}',
                        {
                            'morgueName': jq("#morgueName").val(),
                            'description': jq("#description").val(),
                            'strength': jq("#strength").val(),
                            'retired': jq("#retired").val()
                        }
                    ).success(function (data) {
                        unitsDialog.close();
                        location.reload();
                    });
                },
                cancel: function () {
                    unitsDialog.close();
                }
            }
        });
        jq("#newUnitBtn").on("click", function (e) {
            e.preventDefault();
            unitsDialog.show();
        });
    });
    function page_verified_drug() {
        var error = 0;
        if (jq("#morgueName").val().trim() === '') {
            jq("#morgueName").addClass('red');
            error++;
        } else {
            jq("#morgueName").removeClass('red');
        }
        if (jq("#description").val().trim() === '') {
            jq("#description").addClass('red');
            error++;
        } else {
            jq("#description").removeClass('red');
        }
        if (jq("#strength").val() === '') {
            jq("#strength").addClass('red');
            error++;
        } else {
            jq("#strength").removeClass('red');
        }
        if (jq("#retired").val() === '') {
            jq("#retired").addClass('red');
            error++;
        } else {
            jq("#retired").removeClass('red');
        }

        if (error === 0) {
            return true;
        } else {
            return false;
        }
    }
</script>

<div class="ke-panel-frame" style="background-color: #ffffff">
    <div class="ke-panel-heading">Morgue Unit Management</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <div style="float: left;">
            <button id="newUnitBtn" class="task">Add New Unit</button>
        </div>
        <div>
            ${ ui.includeFragment("morgueapp", "morgueDetail") }
        </div>

        <div id="new-drug-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>Add New Unit</h3>
            </div>
            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Unit Name<span style="color:red">*</span></label>
                        <input type="text" name="morgueName" id="morgueName" style="width: 90%!important;" />
                    </li>
                    <li>
                        <label>Capacity<span style="color:red">*</span></label>
                        <input type="number" name="strength" id="strength"/>
                    </li>
                    <li>
                        <label>Status<span style="color:red">*</span></label>
                        <select id="retired" name="retired">
                            <option value="0">Active</option>
                            <option value="1">Inactive</option>
                        </select>
                    </li>
                    <li>
                        <label>Description<span style="color:red;">*</span></label>
                        <textarea name="description" id="description" style="width: 90%!important;" cols="30" rows="4">
                        </textarea>

                    </li>
                </ul>
            </div>
            <div class="onerow">
                <button class="button confirm right">Confirm</button>
                <button class="button cancel">Cancel</button>
            </div>
        </div>

    </div>
</div>