/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsShipping_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            "availableCols": 1,
            "responsive": true,
            "scrollX": true,
            "lengthChange": true,
        });
    },

    registerCustomEvents: function(){
        $('body').on('click', '.check-all', function (e) {
            var c = this.checked;
            $(':checkbox').prop('checked',c);
        });
        $('body').on('click', '#btnSave', function (e) {
            var array = [];
            $('#tblMailList0 .check-item:checkbox:checked').each(function() {
                array.push(parseInt($(this).data("id"))); // store in an array for reuse
            });
            if (!array.length){
                app.helper.showErrorNotification({
                    message: app.vtranslate('Please select at least one record to validate.')
                });
                return true;
            }
            let status=$("#statusValidate").val()?$("#statusValidate").val():51;
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to validate?')}).then(function () {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "validateDO",
                    status:status,
                    ids: array,
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
        $('body').on('click', '#btnSaveCampaign', function (e) {
            var params = jQuery("#frmEditCampaign").serializeFormData();
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        if (data.success) {
                            app.helper.hideModal();
                            reloadDataTable();
                        } else {
                            var params = {
                                message: data.message,
                            };
                            app.helper.showErrorNotification(params);
                        }
                    }else{
                        app.helper.hideProgress();
                    }
                }
            );
        });
        $('body').on('click', '.btnBhsShowTracking', function (e) {
           
            var params = {
                module: app.getModuleName(),
                view: "ViewEdit",
                mode: "showEdit",
                id: $(this).data('id'),  
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
        $('body').on('click', '.btnBhsCancel', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "cancelOrder",
                id: $(this).data('id'),
            }
                app.helper.showProgress();
                app.request.post({'data': params}).then(
                    function (err, data) {
                        app.helper.hideProgress();
                        if (err === null) {
                            var params = {
                                message: data.message,
                            };
                            app.helper.showAlertNotification(params);
                            setTimeout(function(){
                                reloadDataTable();
                            }, 1000);
                        } else {
                            var params = {
                                message: err,
                            };
                            app.helper.showErrorNotification(params);
                        }
                    }
                );
        });
        $('body').on('click', '.btnImportExcel', function (e) {
            $('.box-importExcel').toggleClass('hidden');
        });

        $('body').on('click', '#btnImport', function (e) {
            var params = {
                module: 'BhsShipping',
                action: "ActionAjax",
                mode: "getToken"
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, token) {
                    console.log($('#API_ENDPOINT').val());
                    var endpoint = $('#API_ENDPOINT').val();
                    var dd = document.getElementById("form-import-excel");
                    var formData = new FormData(dd);
                    $.ajax({
                        url: endpoint + "DO/import",
                        headers: {
                            'Authorization':'Bearer ' + token,
                        },
                        type: "POST",
                        data: formData,
                        contentType:false,
                        cache: false,
                        processData:false,
                        success:function (res) {
                            var message = res.message,
                                messageOutput = '',
                                messageArr = message.split(' | ');
                            for (var i in messageArr) {
                                messageOutput += '<p>' + messageArr[i] + '</p>';
                            }
                            app.helper.showSuccessNotification({ 'message' : messageOutput}, {delay: 5000});
                            app.helper.hideProgress();

                        },

                        error: function(res) {
                            app.helper.hideProgress();
                            app.helper.showErrorNotification({
                                message: app.vtranslate(res.responseJSON.message)
                            });
                        }

                    });
                }
            );

        });

        $('body').on('click', '.btnUpdateRescue', function (e) {
            var params = {
                'module': 'BhsShipping',
                'action': "ActionAjax",
                'mode': 'updateRescue',
            };

            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if (data.code = 200 ) {
                        var message = data.message,
                            messageOutput = '',
                            messageArr = message.split(' | ');
                        for (var i in messageArr) {
                            messageOutput += '<p>' + messageArr[i] + '</p>';
                        }
                        app.helper.showSuccessNotification({ 'message' : messageOutput}, {delay: 5000});
                    } else {
                        app.helper.hideProgress();
                        app.helper.showErrorNotification({
                            message: data.message
                        });
                    }
                }
            );
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
