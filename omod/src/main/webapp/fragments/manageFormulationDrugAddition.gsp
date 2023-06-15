<script>
    var jq = jQuery;
    jq(function () {
         var inventoryDrugDialog = emr.setupConfirmationDialog({
                    dialogOpts: {
                        overlayClose: false,
                        close: true
                    },
                    selector: '#add-formulation-to-inventory-drug-dialog',
                    actions: {
                        confirm: function () {
                            if (!page_verified()) {
                                jq().toastmessage('showErrorToast', 'Ensure fields marked in red have been properly filled before you continue')
                                return false;
                            }
                            // TODO call post method to save a new formulation
                            jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "attachFormulationToAnInventoryDrug")}',
                                {
                                    'inventoryDrugName': jq("#inventoryDrugName").val(),
                                    'drugFormulationSlt': jq("#drugFormulationSlt").val()
                                }
                            ).success(function (data) {
                                inventoryDrugDialog.close();
                                location.reload();
                            });
                        },
                        cancel: function () {
                            inventoryDrugDialog.close();
                            location.reload();
                        }
                    }
                });

                jq("#addFormulationToDrugBtn").on("click", function (e) {
                    e.preventDefault();
                    inventoryDrugDialog.show();
                });
             jq("#inventoryDrugName").on("focus.autocomplete", function () {
                    jq(this).autocomplete({
                        source: function(request, response) {
                                jq.getJSON('${ ui.actionLink("ehrinventoryapp", "addReceiptsToStore", "searchDrugNames") }', {
                                  q: request.term
                                }).success(function(data) {
                                  var results = [];
                                  for (var i in data) {
                                      var result = {
                                        label: data[i].name,
                                        value: data[i].id
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
                 jq.getJSON('${ui.actionLink("ehrinventoryapp", "drugFormulationDetails", "fetchInventoryFormulation")}',
                    {
                    }
                ).success(function (data) {
                    for (var index = 0; index <= data.length; index++) {
                        jq('#drugFormulationSlt').append('<option value="' + data[index].id + '">' + data[index].name + '-' + data[index].dozage + '</option>');
                    }
                });
    });
    function page_verified() {
            var error = 0;

            if (jq("#inventoryDrugName").val().trim() === '') {
                jq("#inventoryDrugName").addClass('red');
                error++;
            } else {
                jq("#inventoryDrugName").removeClass('red');
            }
            if (jq("#drugFormulation").val() === '') {
                jq("#drugFormulation").addClass('red');
                error++;
            } else {
                jq("#drugFormulation").removeClass('red');
            }

            if (error === 0) {
                return true;
            } else {
                return false;
            }
        }
</script>
<div class="ke-panel-frame" style="background-color: #ffffff">
    <div class="ke-panel-heading">Formulation  Drug Combination</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <div>
            <button id="addFormulationToDrugBtn" class="task">Add Formulation to Drug</button>
        </div>
        <div>
            ${ ui.includeFragment("ehrinventoryapp", "inventoryDrugList") }
        </div>
        <div id="add-formulation-to-inventory-drug-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>Drug Formulation Combination</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Drug Name<span>*</span></label>
                        <input type="text" name="inventoryDrugName" id="inventoryDrugName" style="width: 90%!important;" />
                    </li>
                    <li>
                        <label>Drug Formulation<span>*</span></label>
                        <select name="drugFormulationSlt" id="drugFormulationSlt" style="width: 90%!important;">
                        </select>

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


