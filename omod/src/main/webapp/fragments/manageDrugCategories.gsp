<script>
    var jq = jQuery;
    jq(function () {
         var inventoryDrugCategoryDialog = emr.setupConfirmationDialog({
                    dialogOpts: {
                        overlayClose: false,
                        close: true
                    },
                    selector: '#add-drug-category-dialog',
                    actions: {
                        confirm: function () {
                            if (!page_verified()) {
                                jq().toastmessage('showErrorToast', 'Ensure fields marked in red have been properly filled before you continue')
                                return false;
                            }
                            // TODO call post method to save a new formulation
                            jq.getJSON('${ui.actionLink("ehrinventoryapp", "inventoryConfiguration", "addDrugCategory")}',
                                {
                                    'drugCategoryName': jq("#drugCategoryName").val(),
                                    'drugCategoryDesc': jq("#drugCategoryDesc").val()
                                }
                            ).success(function (data) {
                                inventoryDrugCategoryDialog.close();
                                location.reload();
                            });
                        },
                        cancel: function () {
                            inventoryDrugCategoryDialog.close();
                            location.reload();
                        }
                    }
                });

                jq("#addDrugCategoryBtn").on("click", function (e) {
                    e.preventDefault();
                    inventoryDrugCategoryDialog.show();
                });
    });
    function page_verified() {
            var error = 0;

            if (jq("#drugCategoryName").val().trim() === '') {
                jq("#drugCategoryName").addClass('red');
                error++;
            } else {
                jq("#drugCategoryName").removeClass('red');
            }
            if (jq("#drugCategoryDesc").val() === '') {
                jq("#drugCategoryDesc").addClass('red');
                error++;
            } else {
                jq("#drugCategoryDesc").removeClass('red');
            }

            if (error === 0) {
                return true;
            } else {
                return false;
            }
        }
</script>
<div class="ke-panel-frame" style="background-color: #ffffff">
    <div class="ke-panel-heading">Drug Category</div>

    <div class="ke-panel-content" style="background-color: #F3F9FF;">
        <div>
            <button id="addDrugCategoryBtn" class="task">Add Drug Category</button>
        </div>
        <div>
            ${ ui.includeFragment("ehrinventoryapp", "inventoryDrugCategoryList") }
        </div>
        <div id="add-drug-category-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>Drug Category</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Drug Category Name<span style="color:red;">*</span></label>
                        <input type="text" name="drugCategoryName" id="drugCategoryName" style="width: 90%!important;" />
                    </li>
                    <li>
                        <label>Drug Category Description<span style="color:red;">*</span></label>
                        <textarea name="drugCategoryDesc" id="drugCategoryDesc" style="width: 90%!important;" cols="30" rows="5">
                        </textarea>

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


