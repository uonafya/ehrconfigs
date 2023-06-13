<script>
    var jq = jQuery;
    jq(function () {
        jq("#foodHandlingTable").DataTable();

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
                        populateFoodHandlingTableTable();
                        window.reload();
                        foodHandlingDialog.close();
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
    });


    function populateFoodHandlingTableTable() {
        jq('#foodHandlingTable').DataTable().clear();
        {
            jq.getJSON('${ui.actionLink("laboratoryapp", "foodHandling", "getFoodHandlerTests")}')
                .success(function (data) {
                    data.map((item) => {
                        jq('#foodHandlingTblBody').append("<td>" + item.testName + "</td><td>" + name.conceptReference + "</td><td>" + "</td><td>" + item.description + "</td><td>" + item.creator + "</td><td>"+ item.dateCreated + "</td>");
                    });
                });

        }
        jq('#foodHandlingTable').DataTable();
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
                        <textarea col="30" rows="5" id="testDescription" name="testDescription"></textarea>
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