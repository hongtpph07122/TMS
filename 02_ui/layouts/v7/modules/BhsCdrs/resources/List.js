/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsCdrs_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            // "availableCols": 0,
            idx: 0,
            "ordering": false,
            "scrollX": true,
            "lengthChange": true,
        });
    },

    registerCustomEvents: function(){
        $('body').on('click', '.check-all', function (e) {
            var c = this.checked;
            $(':checkbox').prop('checked',c);
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
        $('body').on('click', '#tblMailList0 tbody tr', function (e) {
            $(this).siblings().css('background-color', '#fff');
            $(this).css('background-color', '#c6dafe');
        });
        $('body').on('click', '.btnPlay', function (e) {
            let cdrId = $(this).data('id');
            let leadName = $(this).data('lead-name');
            let leadPhone = $(this).data('lead-phone');
            var params = {
                module: app.getModuleName(),
                view: "ViewAjax",
                mode: "showPlay",
                cdrId: cdrId,
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
