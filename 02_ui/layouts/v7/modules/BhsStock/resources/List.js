/*+*************************BhsStock**********************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsStock_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            lengthChange: true,
            // "columnDefs": [
            //     { className: "text-left", "targets": [ 1 ] },
            //     { "width": "5%", "targets": [0] },
            //     { "width": "30%", "targets": [1, 2] },
            //     { "width": "10%", "targets": [3] },
            //     { "width": "10%", "targets": [4] },
            // ]
        });
    },

    registerCustomEvents: function(){
        $('body').on('click', '.btnNewInbound', function (e) {
            var params = {
                module: app.getModuleName(),
                view: "ViewEdit",
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
        $('.modal').on('hidden.bs.modal', function () {
            location.reload();

        }) 
        //save
        $('body').on('click', '.btnsave', function(e) {
            var quantity= parseInt( $(this).closest('tr').find('.quantity').val());
            var proId=parseInt( $(this).closest('tr').find('#productId').val());
            var partnerId=parseInt( $(this).closest('tr').find('#pnId').val());
            if (quantity > 0) {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "createStock",
                    partnerId: partnerId,
                    prodId: proId,
                    quantityAvailable: quantity
                };
              app.helper.showProgress();
                app.request.post({'data': params}).then(
                    function(err, data){
                         app.helper.hideProgress();
                        if(err === null) {
                            var params = {
                                message: app.vtranslate("Successfull!"),
                            };
                              app.helper.showSuccessNotification(params);
                        }else{
                            alert(err);
                        }
                    }
                );
            } else {
                var params = {
                    message: app.vtranslate("Quantity must be positive"),
                };
                app.helper.showErrorNotification(params);
                return;
            }
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


