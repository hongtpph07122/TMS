/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsCallbackManagement_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            'idx': 0,
            'multitable':true,
            'lengthChange': true,
            columns: [
                { data: null, render:'bhs_check_item' },
                { data: null, render: 'leadId' },
                { data: null, render:  (  row ) => {
                    return convertDateTime(row.callbackTime);
                }},
                { data: null, render: ( row ) =>{
                    return convertDateTime(row.requestTime);
                  }},
                { data: null, render: 'name' },
                { data: null, render: 'phone' },
                { data: null, render: 'owner' },
                { data: null, render: 'assignedName' },
                { data: null, render: ( row ) =>{
                    return convertDateTime(row.createdate);
                  } }, 
            ],
        });
        loadDataTable({
            'idx': 1,
            'multitable':true,
            'lengthChange': true,
            columns: [
                { data: null, render:'bhs_check_item' },
                { data: null, render: 'leadId' },
                { data: null, render:  (  row ) => {
                    return convertDateTime(row.callbackTime);
                }},
                { data: null, render: ( row ) =>{
                    return convertDateTime(row.requestTime);
                  }},
                { data: null, render: 'name' },
                { data: null, render: 'phone' },
                { data: null, render: 'owner' },
                { data: null, render: 'assignedName' },
                { data: null, render: ( row ) =>{
                    return convertDateTime(row.createdate);
                  } }, 
            ],
        });
    },

    registerCustomEvents: function(){
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
        $('body').on('click', '.btn-reassign', function (e) {
            var selectedIds = $('#unassigned table').attr('data-selectedids');
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "assign",
                leadIds : selectedIds,
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then( function(err, data){
                    app.helper.hideProgress();

                    if(err === null) {
                        location.reload();
                    } else{
                        alert(err);
                    }
                }
            );
        });
        $('body').on('click', '.btn-unassigned', function (e) {
            var selectedIds = $('#assigned table').attr('data-selectedids');

            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "unassigned",
                leadIds : selectedIds,
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        location.reload();
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
    if($('.tabCallBack #assigned').hasClass('active')){
        $('.tabCallBack .btn-reassign').prop('disabled',true);
        $('.tabCallBack .btn-unassigned ').prop('disabled',false);
    } else {
        $('.tabCallBack .btn-reassign').removeAttr('disabled');
        $('.tabCallBack .btn-unassigned ').prop('disabled',true);
    }
    $('.tabCallBack .tab-item').click(function(){
        if(!$('.tabCallBack #assigned').hasClass('active')){
            $('.tabCallBack .btn-reassign').prop('disabled',true);
            $('.tabCallBack .btn-unassigned ').prop('disabled',false);
        } else {
            $('.tabCallBack .btn-reassign').removeAttr('disabled');
            $('.tabCallBack .btn-unassigned ').prop('disabled',true);
        }
    });

});
