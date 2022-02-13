/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsMissedCalls_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            isDynamicCols: false,
            "columnDefs": [
                // { "bSortable": false, "aTargets": [ 0 ] },
                { className: "text-left", "targets": [ 5 ] },
                // { className: "text-right", "targets": [ 6 ] },
                // { "width": "5%", "targets": [0] },
            ],
            "lengthChange": true,
        });
    },

    registerCustomEvents: function(){

        $('body').on('click', '.btnExportExcel ', function (e) {
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