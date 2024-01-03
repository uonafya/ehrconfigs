<script>
    var jq = jQuery;
    jq(function () {
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
                    var formulations = jq("#formulationForNewDrug").val();

                    jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "addNewInventoryDrug")}',
                        {
                            newDrugName: jq("#newDrugName").val().trim(),
                            conceptDrugName: jq("#conceptDrugName").val().trim(),
                            formulationForNewDrug: jq("#formulationForNewDrug").val(),
                            categoryForNewDrug: jq("#categoryForNewDrug").val(),
                            reorderLevel: jq("#reorderLevel").val()
                        }
                    ).success(function (data) {
                       location.reload();
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

        jq.getJSON('${ui.actionLink("ehrinventoryapp", "drugFormulationDetails", "fetchInventoryCategories")}',
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
    <div class="ke-panel-heading">Ward Management</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <div style="float: left;">
            <button id="newWardBtn" class="task">Add New Ward</button>
        </div>
        <div>
           ${ ui.includeFragment("ehrinventoryapp", "drugFormulationDetails") }
        </div>


        <div id="new-drug-formulation-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>New Ward</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Ward Name<span style="color:red;">*</span></label>
                        <input type="text" name="wardName" id="wardName" style="width: 90%!important;" />

                    </li>
                    <li>
                        <label>Ward Code<span style="color:red;">*</span></label>

                        <input type="text" name="wardCode" id="wardCode" style="width: 90%!important;">

                    </li>
                    <li>
                         <label>Bed Strength<span style="color:red;">*</span></label>

                         <input type="text" name="wardStrength" id="wardStrength" style="width: 90%!important;">
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