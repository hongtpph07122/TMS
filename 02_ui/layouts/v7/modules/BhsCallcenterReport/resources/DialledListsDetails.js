/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsCallcenterReport_DialledListsDetails_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            "bSort": false,
            "sDom": 't',
        });
    },

    registerCustomEvents: function(){

        $('body').on('click', '.btnExportExcel ', function (e) {
            var params = {
                'module': app.getModuleName(),
                'action': "ExportToExcel",
                'mode': 'exportDialledListsDetails',
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