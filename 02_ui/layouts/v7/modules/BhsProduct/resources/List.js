/*+*************************BhsCampaign**********************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsProduct_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            "availableCols": 1,
            "lengthChange": true,
        });
    },

    showPopupEdit: function(prodId){
        var params = {
            module: app.getModuleName(),
            view: "ViewAjax",
            mode: "showEdit",
            id: prodId,
        };

        app.helper.showProgress();
        app.request.post({'data': params}).then(
            function(err, data){
                app.helper.hideProgress();
                if(err === null) {
                    app.helper.showModal(data, {
                        'cb': function () {
                            var ckEditorInstance = new Vtiger_CkEditor_Js();
                            ckEditorInstance.loadCkEditor($('#txtDescriptionProduct'));
                            ckEditorInstance.loadCkEditor($('#txtAgentScriptProduct'));
                            $("input#txtPriceList").tagsinput('items');
                        }
                    });
                }else{
                }
            }
        );
    },

    registerCustomEvents: function(){
        var thisInstance = this;
        $('body').on('click', '.btn-toggle', function () {
            if($(this).attr('id')){
                return;
            }
            var status = $(this).hasClass('active');
            if (status){
                status = 0;
            } else {
                status = 1;
            }
            var id = $(this).attr('data-id');
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "changeStatus",
                id: id,
                status: status,
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function (err, data) {
                    app.helper.hideProgress();
                }
            );
        });

        $('body').on('click', '.btnCreateProduct', function (e) {
            thisInstance.showPopupEdit();
        });

        $('body').on('click', '.btnExportExcel', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "exportExcel",
                //id: $(this).data('id'),
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        //app.helper.showModal(data);
                        alert('TODO: export excel...');
                    }else{
                        alert(err);
                    }
                }
            );
        });

        $('body').on('click', '.btnBhsDelete', function (e) {
            var id = $(this).attr('data-id');
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to delete?')}).then(function () {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "deleteProduct",
                    id: id,
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

        $('body').on('click', '#btnSaveProduct', function (e) {
            var parent = $('#frmEditProduct');
            var dscr = CKEDITOR.instances.txtDescriptionProduct.getData();
            dscr = $.trim(dscr.replace(/[\t\n]+/g,' '))
            var dscrForAgent = CKEDITOR.instances.txtAgentScriptProduct.getData();
            dscrForAgent = $.trim(dscrForAgent.replace(/[\t\n]+/g,' '))
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "saveRecord",
                id: parent.find('#hfRecordId').val(),
                name: parent.find('#ProductName').val(),
                category: parent.find('#ProductCategory').val(),
                dscr: dscr,
                dscrForAgent: dscrForAgent,
                price: parent.find('#txtPriceList').val(),
                status: parent.find('#btnToggleStatus').hasClass('active') ? 1 : 0,
            };
            console.log(params);
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            app.helper.hideModal();
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

        $('body').on('click', '.btnBhsUpdate', function (e) {
            thisInstance.showPopupEdit($(this).attr('data-id'));
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

$(document).ready(function() {

} );