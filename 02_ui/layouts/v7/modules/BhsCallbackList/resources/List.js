/*+*************************BhsCampaign**********************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsCallbackList_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            'idx': 0,
            'multitable':true,
            'lengthChange': true,
            columns: [
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
                { data: null, render:'bhs_action' },
            ],
        });
        loadDataTable({
            'idx': 1,
            'multitable':true,
            'lengthChange': true,
            columns: [
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
                { data: null, render:'takeAction' },
                { data: null, render:'bhs_action' },
            ],
        });
    },

    registerCustomEvents: function(){

        $('body').on('click', '.btnExportExcel', function (e) {
            var params = {
                'module': app.getModuleName(),
                'action': "ExportToExcel",
            };
            var url = 'index.php?' + $.param(params);
            window.open(url, 'blank');
        });

        $('body').on('click', '.btnCallbackTake', function (e) {
            var array = [];
            $('#tblMailList1 .check-item:checkbox:checked').each(function() {
                array.push(parseInt($(this).data("id"))); // store in an array for reuse
            });
            if (!array.length){
                app.helper.showErrorNotification({
                    message: app.vtranslate('CHOSE_ONE')
                });
                return true;
            }
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "CallbackTake",
                id: array,
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        reloadDataTable(0);
                        reloadDataTable(1);
                    }else{
                        alert(err);
                    }
                }
            );
        });

        // $('body').on('click', '.btnBhsCallBackEdit', function (e) {
        //    var dataId = $(this).attr('data-id');
        //    $('#tblMailList0 tr[data-id*=dataId]').addClass('123');
        //
        //     // $(this).closest('tr').addClass('123');
        // });
        $('body').on('click', '.btnBhsCallBackEdit', function (e) {
            var dataId = $(this).attr('data-id');
            var time=$('tr[data-id="'+dataId+'"] td:nth-child(3)').html();
            $('tr[data-id="'+dataId+'"] td:nth-child(3)').html('<span class="input-group inputDate"><input name="UpdateRequestTime" placeholder="Update Request time" class="paddingLeft5px inputElement dateField"><span class="input-group-addon"><i class="fa fa-calendar "></i></span></span>');
            $('tr[data-id="'+dataId+'"] td:last-child').html('<a class="btnBhsSave btn btn-success" style="color:#fff" data-id="'+dataId+'" href="#">Save</a>');
            $('tr[data-id="'+dataId+'"] td:nth-child(3) input').val(time)
            $('.inputDate').datetimepicker({
                format: 'DD/MM/YYYY HH:mm:ss'
            });
        });
        function convertdate(dt) {
            dt=dt.split("/");
            dt= dt[2].split(" ")[0]+","+dt[1]+","+dt[0]+","+dt[2].split(" ")[1];
            dt= new Date(dt);
            current_date = dt.getDate(),
                current_month = dt.getMonth() + 1,
                current_year = dt.getFullYear(),
                current_hrs = dt.getHours(),
                current_mins = dt.getMinutes(),
                current_sec= dt.getSeconds()
                // Add 0 before date, month, hrs, mins or secs if they are less than 0
                current_date = current_date < 10 ? '0' + current_date : current_date;
            current_month = current_month < 10 ? '0' + current_month : current_month;
            current_hrs = current_hrs < 10 ? '0' + current_hrs : current_hrs;
            current_mins = current_mins < 10 ? '0' + current_mins : current_mins;
            current_sec = current_sec < 10 ? '0' + current_sec : current_sec;
            current_datetime = current_year + "" + current_month + "" + current_date + "" + current_hrs + "" + current_mins + ""+current_sec;
            return current_datetime;
        }
        $('body').on('click', '.btnBhsSave', function (e) {
            var dataId = $(this).attr('data-id');
            var time=$('tr[data-id="'+dataId+'"] td:nth-child(3) input').val();
            if(!time){
                    var params = {
                        message:  app.vtranslate('Please input request time') ,
                    };
                    app.helper.showErrorNotification(params);
                    return;
            }
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "updateRequestTime",
                id: dataId,
                requestTime:convertdate(time)
            };
            // $('tr[data-id="'+dataId+'"] td:nth-child(3)').html(time);
            // $('tr[data-id="'+dataId+'"] td:last-child').html('<a style="font-size: 20px;" class="btnBhsCallBackEdit has-arrow" href="javascript: void(0);" data-id="'+dataId+'"><i class="fa fa-pencil"></i></a>');
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        reloadDataTable();
                    }else{
                        var params = {
                            message: err,
                        };
                        app.helper.showErrorNotification(params);
                    }
                }
            );
        });
        $(".bhs-filter .filter").not( ".btn-filter-header" ).on('click',function(){
            $('tr.bhs-filter th input, tr.bhs-filter ').val('');
            $('th select').val('');
            $('th select').val('').trigger('change');
            $("#tblMailList0,#tblMailList1").DataTable().search( '' ).columns().search( '' ).draw();
        })
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