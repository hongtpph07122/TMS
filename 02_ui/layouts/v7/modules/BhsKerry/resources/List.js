/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsKerry_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            // "availableCols": 0,
            idx: 0,
            "ordering": false,
            "scrollX": true,
        });
    },

    registerCustomEvents: function(){
        $('body').on('click', '.check-all', function (e) {
            var c = this.checked;
            $(':checkbox').prop('checked',c);
        });

        $('body').on('click', '.btnExportExcel ', function (e) {
            var array = [];
            $('#tblMailList0 .check-item:checkbox:checked').each(function() {
                array.push(parseInt($(this).data("id"))); // store in an array for reuse
            });
            var params = {
                'module': app.getModuleName(),
                'action': "ExportToExcel",
                'doIds': array,
            }
            var columns = iTables[0].ajax.params().columns;
            for (var i =0; i<columns.length; i++){
                if (columns[i].search.value !== '') {
                    params[columns[i].name] = columns[i].search.value;
                }
            }
            var url = 'index.php?' + $.param(params);
            console.log(columns);
            console.log(url);
            window.open(url);
        });
        $('body').on('click', '#btnGetOrders', function (e) {
            var array = [];
            $('#tblMailList0 .check-item:checkbox:checked').each(function() {
                 array.push(parseInt($(this).data("id"))); // store in an array for reuse
            });
            if (!array.length){
                app.helper.showErrorNotification({
                    message: app.vtranslate('Please select at least one record .')
                });
                return true;
            }
            let status=$("#statusValidate").val()?$("#statusValidate").val():0;
            app.helper.showConfirmationBox({message: app.vtranslate('CONFIRM')}).then(function () {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "getOrders",
                    status:52,
                    doIds: array,
                };
                app.helper.showProgress();
                app.request.post({'data': params}).then(
                    function (err, data) {
                        app.helper.hideProgress();
                        if (err === null) {
                            if (data.success) {
                                reloadDataTable();
                            } else {
                                var params = {
                                    message: data.message,
                                };
                                app.helper.showErrorNotification(params);
                            }
                        } else {
                            var params = {
                                message: err,
                            };
                            app.helper.showErrorNotification(params);
                        }
                    }
                );
            });
        });
    },

    initializePaginationEvents: function () {
        return true;
    },

    registerEvents: function () {
        this._super();

        this.registerDatatable();
        this.registerCustomEvents();
    }
});

$(document).ready(function () {
    $('.bhs-filter th:first-child').html('<a class="filter" title="filter" href="#"><img src="layouts/v7/resources/Images/icon/icon-filter-close.png" alt=""></a>');
});
