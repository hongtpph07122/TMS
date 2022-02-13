/*+*************************BhsStock**********************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsSearchOrders_List_Js",{},{

    registerDatatable: function(){
        loadDataTable({
            // "bSort": false,
            columns: [
                { data: null, render: 'odId' },
                { data: null, render: 'marketing_source' },
                { data: null, render: 'agent'  },
                { data: null, render: 'status' },
                { data: null, render: ( row ) =>{
                    return convertDateTime(row.createdate);
                  } },  
            ],
        });
    },

    registerCustomEvents: function(){
        $( '#tblMailList0 th .input-group input').on( 'keyup change', function () {
            var nodes = Array.prototype.slice.call( $(this).closest('tr')[0].children );
            var i = nodes.indexOf( $(this).closest('th')[0] );
            if ( iTables[0].column(i).search() !== this.value ) {
                iTables[0].column(i).search( this.value ).draw();
            }
        } );
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


