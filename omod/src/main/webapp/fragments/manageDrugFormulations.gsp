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
                            'dosage': jq("#dosage").val().trim()
                        }
                    ).success(function (data) {
                        populateDrugFormulationsTable();
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
            populateDrugFormsOptions();
            populateDosageOptions();
        });


    });

    function populateDrugFormsOptions() {
        jq('#formulationName').change();
        jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "getAllInventoryDrugForms")}'
        ).success(function (data) {
            // console.log("Saved  :" + data.toString());
            for (const x in data) {
                jq('#formulationName').append("<option value='" + data[x] + "'>" + data[x] + "</option>");
            }
        });
    }

    function populateDosageOptions() {
        jq('#dosage').empty();
        jq('#mappedFormulation').text('');

        var sourceValues = []
        jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "getAllInventoryDrugDozages")}'
        ).success(function (data) {
            for (var i in data) {
                var result = {label: data[i], value: data[i]};
                sourceValues.push(result);
            }
        });

        jq('#dosage').autocomplete({
            source: sourceValues,
            minLength: 2,
            select: function (event, ui) {
                event.preventDefault();
                jq(this).val(ui.item.label);
                if (jq('#formulationName').val() !== '') {
                    jq('#mappedFormulation').text("Formulation :" + " " + jq('#formulationName').val() + ": " + jq('#dosage').val());
                }
            },
            open: function () {
                jq(this).removeClass("ui-corner-all").addClass("ui-corner-top");
            },
            close: function () {
                jq(this).removeClass("ui-corner-top").addClass("ui-corner-all");
            }
        });
    }

    function populateDrugFormulationsTable() {
        jq('#formulationsTable').DataTable().clear();
        {
            jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "getAllDrugFormulationList")}')
                .success(function (data) {
                    data.map((item) => {
                        jq('#formulationsTblBody').append("<td>" + item.name + "</td><td>" + name.dozage + "</td><td>" + "</td><td>" + item.createdOn + "</td><td>" + item.createdBy + "</td>");
                    });
                });

        }
        jq('#formulationsTable').DataTable();
    }

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
        <table id="formulationsTable">
            <thead>
            <tr>
                <th>Name</th>
                <th>Dosage</th>
                <th>Date created</th>
                <th>Created By</th>
            </tr>
            </thead>
            <tbody id="formulationsTblBody">
            </tbody>
        </table>


        <div id="new-drug-formulation-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>New Drug Formulation</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Formulation<span>*</span></label>
                        <select name="formulationName" id="formulationName" style="width: 90%!important;">
                            <option disabled>--Please Select--</option>
                        </select>
                    </li>
                    <li>
                        <label>Dosage<span>*</span></label>

                        <input name="dosage" id="dosage" style="width: 90%!important;">

                    </li>
                    <li>
                        <label id="mappedFormulation"></label>
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