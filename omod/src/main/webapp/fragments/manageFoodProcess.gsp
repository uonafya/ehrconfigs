<script>
    var jq = jQuery;
    jq(function () {
        jq('#foodHandlingTable').DataTable();
        populateFoodHandlingTableTable();

        var foodHandlingDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#new-food-handling-dialog',
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
                        foodHandlingDialog.close();
                        populateFoodHandlingTableTable();
                        location.reload();
                    });
                },
                cancel: function () {
                    foodHandlingDialog.close();
                }
            }
        });

        jq("#newFoodHandlingBtn").on("click", function (e) {
            e.preventDefault();
            foodHandlingDialog.show();
        });
     jq("#conceptReference").on("focus.autocomplete", function () {
            jq(this).autocomplete({
                source: function(request, response) {
                        jq.getJSON('${ ui.actionLink("laboratoryapp", "foodHandling", "getTestsConcepts") }', {
                          query: request.term
                        }).success(function(data) {
                          var results = [];
                          for (var i in data) {
                              var result = {
                                label: data[i].displayString,
                                value: data[i].uuid
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


    function populateFoodHandlingTableTable() {
        jq('#foodHandlingTable').DataTable().clear();
        {
            jq.getJSON('${ui.actionLink("laboratoryapp", "foodHandling", "getFoodHandlerTests")}')
                .success(function (data) {
                    data.map((item) => {
                        jq('#foodHandlingTblBody').append("<tr><td>" + item.testName + "</td><td>" + name.conceptReference + "</td><td>" + "</td><td>" + item.description + "</td><td>" + item.creator + "</td><td>"+ item.dateCreated + "</td></tr>");
                    });
                });

        }
    }

    function page_verified() {
        var error = 0;

        if (jq("#testName").val().trim() === '') {
            jq("#testName").addClass('red');
            error++;
        } else {
            jq("#testName").removeClass('red');
        }
        if (jq("#conceptReference").val().trim() === '') {
            jq("#conceptReference").addClass('red');
            error++;
        } else {
            jq("#conceptReference").removeClass('red');
        }

        if (error === 0) {
            return true;
        } else {
            return false;
        }
    }
</script>

<div class="ke-panel-frame" style="background-color: #ffffff">
    <div class="ke-panel-heading">Food Handling Management</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <div>
            <button id="newFoodHandlingBtn" class="task">Add New Test</button>
        </div>
        <table id="foodHandlingTable">
            <thead>
            <tr>
                <th>Name</th>
                <th>Concept Reference</th>
                <th>Description</th>
                <th>Date created</th>
                <th>Created By</th>
            </tr>
            </thead>
            <tbody id="foodHandlingTblBody">
            </tbody>
        </table>


        <div id="new-food-handling-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>New Food handle test</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Test Name<span>*</span></label>
                        <input type="text" name="testName" id="testName" style="width: 90%!important;" />
                    </li>
                    <li>
                        <label>Concept Reference<span>*</span></label>
                        <input name="conceptReference" id="conceptReference" style="width: 90%!important;">

                    </li>
                    <li>
                        <label>Description</label>
                        <textarea col="40" rows="5" id="testDescription" name="testDescription"></textarea>
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