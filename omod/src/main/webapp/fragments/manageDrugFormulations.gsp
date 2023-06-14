<script>
    var jq = jQuery;
    jq(function () {
        jq("#formulationsTable").DataTable();

        var formulationsDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#new-drug-formulation-dialog',
            actions: {
                confirm: function () {
                    if (!page_verified()) {
                        jq().toastmessage('showErrorToast', 'Ensure fields marked in red have been properly filled before you continue')
                        return false;
                    }
                    // TODO call post method to save a new formulation
                    jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "updateDrugFormulation")}',
                        {
                            'formulationName': jq("#formulationName").val().trim(),
                            'dosage': jq("#dosage").val().trim(),
                            'description': jq("#description").val().trim()
                        }
                    ).success(function (data) {
                        location.reload();
                        formulationsDialog.close();
                    });
                },
                cancel: function () {
                    formulationsDialog.close();
                }
            }
        });

        jq("#newDrugFormulationBtn").on("click", function (e) {
            e.preventDefault();
            formulationsDialog.show();
        });
    });
    function page_verified() {
        var error = 0;

        if (jq("#formulationName").val().trim() === '') {
            jq("#formulationName").addClass('red');
            error++;
        } else {
            jq("#formulationName").removeClass('red');
        }
        if (jq("#dosage").val().trim() === '') {
            jq("#dosage").addClass('red');
            error++;
        } else {
            jq("#dosage").removeClass('red');
        }

        if (error === 0) {
            return true;
        } else {
            return false;
        }
    }
</script>

<div class="ke-panel-frame" style="background-color: #ffffff">
    <div class="ke-panel-heading">Drug Management</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <div>
            <button id="newDrugFormulationBtn" class="task">Add New Formulation</button>
        </div>
        <div>
           ${ ui.includeFragment("ehrinventoryapp", "drugFormulationDetails") }
        </div>


        <div id="new-drug-formulation-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>New Drug Formulation</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Formulation<span>*</span></label>
                        <input type="text" name="formulationName" id="formulationName" style="width: 90%!important;" />

                    </li>
                    <li>
                        <label>Dosage<span>*</span></label>

                        <input type="text" name="dosage" id="dosage" style="width: 90%!important;">

                    </li>
                    <li>
                        <textarea col="30" rows="5" id="description" name="description"></textarea>
                    </li>
                </ul>

                <div class="onerow">
                    <button class="button confirm right">Confirm</button>
                    <button class="button cancel">Cancel</button>
                </div>
            </div>
        </div>

    </div>
</div>