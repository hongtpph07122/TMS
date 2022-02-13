/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsValidation_List_Js",{},{

    registerDatatable: function(){
            loadDataTable({
               // "availableCols": 0,
                idx: 0,
                "ordering": false,
                "scrollX": true,
                "lengthChange": true,
                 // columns: [
                 //    { data: null, render: 'checkbox' },
                 //    { data: null, render: 'play' },
                 //    { data: null, render: 'leadId' },
                 //    { data: null, render: 'soId' },
                 //    { data: null, render: 'customername' },
                 //    { data: null, render: 'phone' },
                 //    { data: null, render: 'productName' },
                 //    { data: null, render: 'totalprice' },
                 //    { data: null, render: 'agId' },
                 //    { data: null, render: 'assign' },
                 //    { data: null, render: ( row ) =>{
                 //    return convertDateTime(row.createdate);
                 //    } },
                 //     { data: null, render: 'comment'},
                 //     { data: null, render: 'address' },
                 //     { data: null, render: 'carrier' },
                 //     { data: null, render: 'leadStatus' },
                 //     { data: null, render: 'orderStatus' },
                 //     { data: null, render: 'deliveryStatus' },
                 //     { data: null, render: 'paymentMethod' }
                 // ]
            });
    },

    registerCustomEvents: function(){
        $('body').on('click', '.check-all', function (e) {
            var c = this.checked;
            $(':checkbox').prop('checked',c);
        });
        $('body').on('click', '#btnValidationValidate', function (e) {
            var array = [];
            $('#tblMailList0 .check-item:checkbox:checked').each(function() {
                array.push(parseInt($(this).data("id"))); // store in an array for reuse
            });
            if (!array.length){
                app.helper.showErrorNotification({
                    message: app.vtranslate('Please select at least one record.')
                });
                return true;
            }
            if ($('#statusValidate').val() == 0){
                app.helper.showErrorNotification({
                    message: app.vtranslate('Please select at least one status.')
                });
                return true;
            }
            let status=$("#statusValidate").val()?$("#statusValidate").val():0;
            app.helper.showConfirmationBox({message: app.vtranslate('VALIDATE_CONFIRM')}).then(function () {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "validateOrder",
                    status:status,
                    ids: array,
                };
                app.helper.showProgress();
                app.request.post({'data': params}).then(
                    function (err, data) {
                        app.helper.hideProgress();
                        console.log(data);
                        if (err === null) {
                            if (data.success) {
                                reloadDataTable();
                            } else {
                                var params = {
                                    message: data.message,
                                };
                                reloadDataTable();
                                app.helper.showErrorNotification(params);
                            }
                        } else {
                            var params = {
                                message: err,
                            };
                            reloadDataTable();
                            app.helper.showErrorNotification(params);
                        }
                    }
                );
            });
        });
        $('body').on('click', '#btnValidationSubmit', function (e) {
            reloadDataTable();
        });

        $('body').on('click', '.btnExportExcel ', function (e) {
            var params = {
                'module': app.getModuleName(),
                'action': "ExportToExcel"
            }
            var columns = iTables[0].ajax.params().columns;
            for (var i =0; i<columns.length; i++){
                if (columns[i].search.value !== '') {
                    params[columns[i].name] = columns[i].search.value;
                }
            }
            var url = 'index.php?' + $.param(params);
            window.open(url, 'blank');
        });

        $('body').on('click', '#btnAddNewOrder', function (e) {
            var url="";
            url = "index.php?module=BhsOrderProcessing&view=AddNew";
            $(location).attr('href',url);
        });

        $('body').on('click', '.btnPlay', function (e) {
            let leadId = $(this).data('id');
            let leadName = $(this).data('lead-name');
            let leadPhone = $(this).data('lead-phone');
            var params = {
                module: app.getModuleName(),
                view: "ViewAjax",
                mode: "showEdit",
                leadId: leadId,
                leadName: leadName,
                leadPhone: leadPhone,
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        app.helper.showModal(data);
                    }else{
                    }
                }
            );
        });
        $('select').on('change', function() {
            $('#tblMailList0_wrapper').DataTable( {
                "pageLength": 20
            } );
            //reloadDataTable();
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
   
});
