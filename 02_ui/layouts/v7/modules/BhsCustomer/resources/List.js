/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsCustomer_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            // "bSort": true,
            "availableCols": 1,
            "dt_params": {
                'keyword': function () {
                    return $('#txtKeyword').val();
                },
                'province_id': function () {
                    return $('#cbbProvinces').val();
                },
                'district_id': function () {
                    return $('#cbbDistricts').val();
                },
                'customer_status': function () {
                    return $('#cbbCustomerStatus').val();
                },
            }
        });
    },

    registerCustomEvents: function(){

        $('body').on('click', '#btnSaveCustomer', function (e) {
            var params = jQuery("#frmEditCustomer").serializeFormData();
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

        $('body').on('click', '#btnAddCustomer', function (e) {
            var params = {
                module: app.getModuleName(),
                view: "ViewAjax",
                mode: "showEdit",
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

        $('body').on('click', '.btnExportExcel', function (e) {
            var params = {
                'module': app.getModuleName(),
                'action': "ExportToExcel",
                'keyword': $('#txtKeyword').val(),
                'province_id': $('#cbbProvinces').val(),
                'district_id': $('#cbbDistricts').val(),
                'customer_status': $('#cbbCustomerStatus').val(),
            };
            var url = 'index.php?' + $.param(params);
            window.open(url, 'blank');
        });

        $('body').on('click', '#btnFindCustomer', function (e) {
            reloadDataTable();
        });

        $('body').on('change', '#cbbProvinces', function (e) {
            var params = {
                module: 'BhsOrderProcessing',
                action: "ActionAjax",
                mode: "changeProvince",
                province_id: $(this).val(),
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        if (data.success) {
                            var cbbDistricts = $('select#cbbDistricts');
                            cbbDistricts.html('<option></option>');
                            var districts= data.districts;
                            for (var districtId in districts){
                                if (districtId){
                                    cbbDistricts.append(new Option(districts[districtId.toString()], districtId));
                                }
                            }
                            cbbDistricts.trigger("liszt:updated");
                            cbbDistricts.trigger('change.select2');
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

        $('body').on('click', '#btnDeleteCustomer', function (e) {
            var ids = $('#tblMailList0').attr('data-selectedIds');
            if (!ids){
                app.helper.showErrorNotification({
                    message: app.vtranslate('Please select at least one record to delete.')
                });
                return true;
            }
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to delete?')}).then(function () {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "deleteCustomer",
                    ids: ids,
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
});