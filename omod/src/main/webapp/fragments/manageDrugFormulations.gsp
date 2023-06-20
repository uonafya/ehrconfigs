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

        var drugDialog = emr.setupConfirmationDialog({
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
                    // TODO call post method to save a new formulation
                    var formulationValues = jq("#formulationForNewDrug").val();
                    console.log("The values to be passed as array", formulationValues);
                    jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "addNewInventoryDrug")}',
                        {
                            'newDrugName': jq("#newDrugName").val().trim(),
                            'conceptDrugName': jq("#conceptDrugName").val().trim(),
                            'formulationForNewDrug': jq("#formulationForNewDrug,
                            'categoryForNewDrug': jq("#categoryForNewDrug").val()
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
        jq("#conceptDrugName").on("focus.autocomplete", function () {
            jq(this).autocomplete({
                source: function(request, response) {
                        jq.getJSON('${ ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "getDrugs") }', {
                          query: request.term
                        }).success(function(data) {
                          var results = [];
                          for (var i in data) {
                              var result = {
                                label: data[i].displayName,
                                value: data[i].drugId
                              };
                              results.push(result);
                          }
                          response(results);
                        });
                      },
                      minLength: 3,
                      select: function(event, ui) {
                        event.preventDefault();
                        jq(this).val(ui.item.label);
                      }
            });
        });

        jq("#newDrugFormulationBtn").on("click", function (e) {
            e.preventDefault();
            formulationsDialog.show();
        });
        jq("#newDrugBtn").on("click", function (e) {
            e.preventDefault();
            drugDialog.show();
        });
        jq.getJSON('${ui.actionLink("ehrinventoryapp", "drugFormulationDetails", "fetchInventoryFormulation")}',
            {
            }
        ).success(function (data) {
            for (var index = 0; index <= data.length; index++) {
                jq('#formulationForNewDrug').append('<option value="' + data[index].id + '">' + data[index].name + '-' + data[index].dozage + '</option>');
            }
        });
        jq.getJSON('${ui.actionLink("ehrinventoryapp", "drugFormulationDetails", "fetchInventoryDrugs")}',
            {
            }
        ).success(function (data) {
            for (var index = 0; index <= data.length; index++) {
                jq('#categoryForNewDrug').append('<option value="' + data[index].id + '">' + data[index].name + '</option>');
            }
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
    function page_verified_drug() {
            var error = 0;
            if (jq("#newDrugName").val().trim() === '') {
                jq("#newDrugName").addClass('red');
                error++;
            } else {
                jq("#newDrugName").removeClass('red');
            }
            if (jq("#conceptDrugName").val().trim() === '') {
                jq("#conceptDrugName").addClass('red');
                error++;
            } else {
                jq("#conceptDrugName").removeClass('red');
            }
            if (jq("#formulationForNewDrug").val() === '') {
                jq("#formulationForNewDrug").addClass('red');
                error++;
            } else {
                jq("#formulationForNewDrug").removeClass('red');
            }
            if (jq("#categoryForNewDrug").val() === '') {
                jq("#categoryForNewDrug").addClass('red');
                error++;
            } else {
                jq("#categoryForNewDrug").removeClass('red');
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
        <div style="float: left;">
            <button id="newDrugFormulationBtn" class="task">Add New Formulation</button>
        </div>

        <div style="float: right;">
            <button id="newDrugBtn" class="task">Add New Drug</button>
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
                        <label>Formulation<span style="color:red;">*</span></label>
                        <input type="text" name="formulationName" id="formulationName" style="width: 90%!important;" />

                    </li>
                    <li>
                        <label>Dosage<span style="color:red;">*</span></label>

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

        <div id="new-drug-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>Add New Drug</h3>
            </div>
             <div class="dialog-content">
             <input type="hidden" name="actualFormulationSelectValues" id="actualFormulationSelectValues" />
             <ul>
                <li>
                    <label>Drug Name<span styel="color:red">*</span></label>
                    <input type="text" name="newDrugName" id="newDrugName" style="width: 90%!important;" />
                </li>
                <li>
                    <label>Concept Drug<span styel="color:red">*</span></label>
                    <input type="text" name="conceptDrugName" id="conceptDrugName" style="width: 90%!important;" />
                </li>
                <li>
                    <label>Formulation<span styel="color:red">*</span></label>
                    <select name="formulationForNewDrug" id="formulationForNewDrug" multiple>
                    </select>
                </li>
                <li>
                    <label>Drug Category<span styel="color:red">*</span></label>
                    <select name="categoryForNewDrug" id="categoryForNewDrug">
                    </select>
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