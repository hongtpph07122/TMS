
Vtiger_List_Js("BhsDnc_List_Js",{},{

	registerDatatable: function(){
        loadDataTable({
            "availableCols": 1,
        });
	},

	registerCustomEvents: function(){
	    $('body').on('click', '.btnBhsDelete', function (e) {
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to delete?')}).then(function () {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "deleteCampaign",
                    id: $(this).data('id'),
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

	    $('body').on('click', '.btnBhsUpdate', function (e) {
            var params = {
                module: app.getModuleName(),
                view: "ViewAjax",
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