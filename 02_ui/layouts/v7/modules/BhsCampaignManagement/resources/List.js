/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsCampaignManagement_List_Js",{},{

	registerDatatable: function(){
        gThis =this;
        loadDataTable({
            "availableCols": 1,
            "scrollX": false,
            columns: [
                { data: null, render: 'cpId' },
                { data: null, render: 'name' },
                { data: null, render: ( row ) =>{
                    return convertDateTime(row.startdate);
                  } },
                { data: null, render:  (  row ) => {
                    return convertDateTime(row.stopdate);
                } }
            ]
        });
	},
	registerCustomEvents: function(){
        var table = $('#tblMailList0').DataTable();
	    $('body').on('click', '.btnBhsDelete', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "deleteCampaign",
                id: $(this).data('id'),
            };
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure you want to delete?')}).then(function () {
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
            // var data=  table.row(  $(this).closest('tr') ).data();
            // var params = {
            //     module: app.getModuleName(),
            //     view: "ViewEdit",
            //     mode: "showEdit",
            //     id: $(this).data('id'),
            //     data: data
            // };
            // app.helper.showProgress();
            // app.request.post({'data': params}).then(
            //     function(err, data){
            //         app.helper.hideProgress();
            //         if(err === null) {
            //             app.helper.showModal(data);
            //         }else{
            //         }
            //     }
            // );
            var url="";
            if($(this).data('id')){
                 url = "index.php?module=BhsCallStrategy&view=List&app=BHS_CAMPAIGN&cpid="+$(this).data('id');
            }else{
                url = "index.php?module=BhsCallStrategy&view=List&app=BHS_CAMPAIGN";
            }
            
            $(location).attr('href',url);
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
        $('body').on('click', '.btnExportExcel', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ExportToExcel",
                mode: "exportData",
                //id: $(this).data('id'),
            };
            app.helper.showProgress();
            var url = 'index.php?' + $.param(params);
            window.open(url, 'blank');
            app.helper.hideProgress();
            // app.request.post({'data': params}).then(
            //     function(err, data){
            //         app.helper.hideProgress();
            //         if(err === null) {
            //             //app.helper.showModal(data);
            //             //alert('TODO: export excel...');
            //             var url = 'index.php?' + $.param(params);
            //             window.open(url, 'blank');
            //         }else{
            //             alert(err);
            //         }
            //     }
            // );
        });
        $('body').on('click', '.btnBhsStart', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "start",
                id: $(this).data('id'),
            };
        
        //    $(this).parent().find( "a" ).css("color", "grey").removeClass("btnBhsPause btnBhsStart btnBhsUpdate btnBhsDelete");
        //    $(this).parent().find( ".btnBhsStop" ).css("color", "inherit");
       
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        if (data.success) {
                            reloadDataTable();
                        }else{
                            var params = {
                                message: 'Please check that all configure steps are done?',
                            };
                            app.helper.showErrorNotification(params);
                        }
                        //app.helper.showModal(data);
                        //alert('The campiagn is starting');
                       
                    }else{
                        alert(err);
                    }
                }
            );
        });

        $('body').on('click', '.btnBhsStop', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "stop",
                id: $(this).data('id'),
            };
           
            app.helper.showConfirmationBox({message: app.vtranslate('Your campaign will stop immediately. This action cannot be undone.')}).then(function () {
                app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        //app.helper.showModal(data);
                        //alert('The campiagn is stopped');
                        reloadDataTable();
                    }else{
                        alert(err);
                    }
                }
            );
        });
    });
        $('body').on('click', '.btnBhsPause', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "pause",
                id: $(this).data('id'),
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        //app.helper.showModal(data);
                        //alert('The campiagn is stopped');
                        reloadDataTable();
                    }else{
                        alert(err);
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