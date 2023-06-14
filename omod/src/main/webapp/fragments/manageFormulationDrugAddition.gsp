<script>
    var jq = jQuery;
    jq(function () {
        jq('#addFormulationToDrugTable').DataTable();
        populateDrugCategoryTable();
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
                            jq.getJSON('${ui.actionLink("laboratoryapp", "foodHandling", "addFoodHandlerTest")}',
                                {
                                    'testName': jq("#testName").val().trim(),
                                    'conceptReference': jq("#conceptReference").val().trim(),
                                    'testDescription': jq("#testDescription").val().trim(),
                                }
                            ).success(function (data) {
                                inventoryDrugDialog.close();
                                populateDrugCategoryTable();
                                location.reload();
                            });
                        },
                        cancel: function () {
                            inventoryDrugDialog.close();
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
                                  query: request.term
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
    });
    function page_verified() {
            var error = 0;

            if (jq("#inventoryDrugName").val().trim() === '') {
                jq("#inventoryDrugName").addClass('red');
                error++;
            } else {
                jq("#inventoryDrugName").removeClass('red');
            }
            if (jq("#drugFormulation").val().trim() === '') {
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
        function populateDrugCategoryTable() {
            jq('#addFormulationToDrugTable').DataTable().clear();
            {
                jq.getJSON('${ui.actionLink("ehrinventoryapp", "drugFormulationDetails", "fetchInventoryDrugs")}')
                    .success(function (data) {
                        data.map((item) => {
                            jq('#addFormulationToDrugTblBody').append("<tr><td>" + item.name + "</td><td>" + item.formulations.name + "</td><td>" + "</td><td>" + item.category.name + "</td></tr>");
                        });
                    });

            }
        }
</script>
<div class="ke-panel-frame" style="background-color: #ffffff">
    <div class="ke-panel-heading">Food Handling Management</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <div>
            <button id="addFormulationToDrugBtn" class="task">Add Formulation to Drug</button>
        </div>
        <table id="addFormulationToDrugTable">
            <thead>
            <tr>
                <th>Drug Name</th>
                <th>Formulation</th>
                <th>Drug Category</th>
            </tr>
            </thead>
            <tbody id="addFormulationToDrugTblBody">
            </tbody>
        </table>


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
                        <select name="drugFormulation" id="drugFormulation" style="width: 90%!important;">
                            <option value="">Select a formulation to link with</option>
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


