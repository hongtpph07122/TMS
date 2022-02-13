/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsShippingPending_List_Js",{},{
    handleOutStock: function (params, callback) {
        app.helper.showProgress();
        app.request.post({'data': params})
            .then(
            function (err, data) {
                app.helper.hideProgress();
                callback(err, data);
            });
    },

    handleRemove: function (params, callback) {
        app.helper.showProgress();
        app.request.post({'data': params})
            .then(
            function (err, data) {
                app.helper.hideProgress();
                callback(err, data);
            });
    },

    responseAndReload: function(data) {
        if (!data.success) {
            app.helper.showErrorNotification({message: data.error});
            return;
        }
        reloadDataTable();
        app.helper.hideProgress();
        app.helper.showSuccessNotification({'message': data.message || '' }, {'type': 'info'});
        $('.bulk_actions select.status').select2('val', '');

    },

    registerDatatable: function(){
        loadDataTable({
            "responsive": true,
            "scrollX": true,
            "lengthChange": true,
        });
    },

    registerCustomEvents: function(){
        var thisInstance = this;

        $('body').on('click', '.btnBhsShowTracking', function (e) {
           
            var params = {
                module: 'BhsShipping',
                view: "ViewEdit",
                mode: "showEdit",
                type:'BhsShippingPending',
                id: $(this).data('id'),  
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function(err, data){
                    app.helper.hideProgress();
                    if(err === null) {
                        app.helper.showModal(data);
                        $('#dateApproval').hide();
                        $('.edit').show();
                    }else{
                    }
                }
            );
        });
        $('body').on('click', '.check-all', function (e) {
            var c = this.checked;
            $(':checkbox').prop('checked',c);
        });

        $('body').on('click', '.btnExportExcel', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ExportToExcel",
            };
            var url = 'index.php?' + $.param(params);
            window.open(url, 'blank');
        });

        // Listen event Commit
        $('body').on('click', '.btnCommit', function () {
            var $bulkActions = $('.bulk_actions'),
            $status  = $bulkActions.find('select.status');
            
            var $ids = $('#tblMailList0 .check-item:checked');
            var ids = [];
            $ids.each(function () {
                $parent = $(this).closest('tr');
                ids.push($parent.find('.btnBhsShowTracking').data('id'));
                // ids.push($(this).data('id'));
            });
            
            var status = $status.val();

            if (ids.length < 1) {
                app.helper.showErrorNotification({
                    message: 'Please choose at least one order'
                });
                return;
            }
            if (!status) {
                app.helper.showErrorNotification({
                    message: 'Please choose status'
                });
                return;
            }

            app.helper.showProgress();

            switch (status) {
                case 'out_stock':
                    var params = {
                        module: app.getModuleName(),
                        action: "ActionAjax",
                        mode: "resendDo",
                        app: "BHS_ORDERS",
                        doIds: ids
                    };
                    thisInstance.handleOutStock(params, function (err, data) {
                        if (!data.success) {
                            app.helper.showErrorNotification({message: data.error});
                            return;
                        }

                        reloadDataTable();

                        app.helper.hideProgress();

                        var responseData = data.data;
                        responseData = responseData.split('false ').join('Failed').split('true ').join('Success');
                        var messages = responseData.split('<br />');
                        messages.pop(); /*loại từ last element*/

                        var table = '<table class="table table-bordered table-striped" style="width: 100%">'
                                    +    '<thead>'
                                    +        '<tr>'
                                    +           '<th>So ID</th>'
                                    +           '<th>Lead ID</th>'
                                    +           '<th>Result</th>'
                                    +        '</tr>'
                                    +    '</thead>'
                                    +    '<tbody>';

                        for (let i in messages) {
                            var row = messages[i].split(' : ');

                            var customColor = '';
                            if (row[2] === 'Success') {
                                customColor = 'text-success';
                            } else {
                                customColor = 'text-danger';
                            }

                            table += '<tr>'
                                    +   '<td>' + row[0] + '</td>'
                                    +   '<td>' + row[1] + '</td>'
                                    +   '<td class='+ customColor +'>' + row[2] + '</td>'
                                    + '</tr>';
                        }

                        table += '</tbody>'
                            + '</table>';

                        app.helper.showSuccessNotification({'message': table || '' }, {'delay': 10000});
                        $('.bulk_actions select.status').select2('val', '');
                    });
                    break;
                case 'remove':
                    var params = {
                        module: app.getModuleName(),
                        action: "ActionAjax",
                        mode: "removeDo",
                        app: "BHS_ORDERS",
                        doIds: ids
                    };
                    thisInstance.handleRemove(params, function (err, data) {
                        if(err) {

                        }
                        thisInstance.responseAndReload(data);
                    });
                    break;
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

$(document).ready(function () {
});