/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js(
  "BhsOrders_List_Js",
  {},
  {
    registerDatatable: function() {
      loadDataTable({
        idx: 0,
        // "bFilter": true,
        scrollX: true,
        ordering: false,
        lengthChange: true,
      });
    },

    registerCustomEvents: function() {
      $("body").on("click", ".check-all", function(e) {
        var c = this.checked;
        $("#tblMailList0 .check-item:checkbox").prop("checked", c);
      });

      $("body").on("click", "#assign-agent", function(e) {
        var array = [];
        $("#tblMailList0 .check-item:checkbox:checked").each(function() {
          array.push(parseInt($(this).data("id"))); // store in an array for reuse
        });
        var selectedAgent = $("#selected-agent").val();
        if (!selectedAgent) {
          app.helper.showErrorNotification({
            message: app.vtranslate("PLEASE_SELECT_ONE_AGENT")
          });
          return true;
        }
        var leadStatus = 0;
        if ($("#add-to-order-list").prop("checked")) {
          leadStatus = 1;
        }

        var params = {
          module: app.getModuleName(),
          action: "ActionAjax",
          mode: "assignAgentToOrder",
          leadIds: JSON.stringify(array),
          agentId: selectedAgent,
          leadStatus: leadStatus
        };
        app.helper.showProgress();
        app.request.post({ data: params }).then(function(err, data) {
          app.helper.hideProgress();
          $("#tblMailList0").attr("data-selectedids", "");
          if (err === null) {
            var res = JSON.parse(data);
            if(res.body.code == 400) {
              app.helper.showErrorNotification({
                message: app.vtranslate(res.body.message)
              });
            }
            app.helper.hideModal();
            reloadDataTable();
          } else {
          }
        });
      });
      $("body").on("click", "#btnReassign", function(e) {
        var array = [];
        $("#tblMailList0 .check-item:checkbox:checked").each(function() {
          array.push(parseInt($(this).data("id"))); // store in an array for reuse
        });
        if (!array.length) {
          app.helper.showErrorNotification({
            message: app.vtranslate("PLEASE_SELECT_ONE_ITEM")
          });
          return true;
        }
        var params = {
          module: app.getModuleName(),
          view: "ViewAjax",
          mode: "showReassignForm"
        };
        app.helper.showProgress();
        app.request.post({ data: params }).then(function(err, data) {
          app.helper.hideProgress();
          if (err === null) {
            app.helper.showModal(data);
          } else {
          }
        });
      });
      $("body").on("click", ".btnExportExcel ", function(e) {
        var params = {
          module: app.getModuleName(),
          action: "ExportToExcel"
        };
        var columns = iTables[0].ajax.params().columns;
        for (var i = 0; i < columns.length; i++) {
          if (columns[i].search.value !== "") {
            params[columns[i].name] = columns[i].search.value;
          }
        }
        var url = "index.php?" + $.param(params);
        window.open(url, "blank");
      });

      $("body").on("click", "#btnCreateLead", function(e) {
        var url = "";
        url = "index.php?module=BhsOrders&view=CreateLead";
        $(location).attr("href", url);
      });

      function reloadDataTable( idx ) {
        if ( !idx ) {
          idx = 0;
        }
        iTables[idx].ajax.reload(null, false);
      }
    },

    initializePaginationEvents: function() {
      return true;
    },

    registerEvents: function() {
      this._super();

      this.registerDatatable();
      this.registerCustomEvents();
    }
  }
);

$(document).ready(function() {

});
