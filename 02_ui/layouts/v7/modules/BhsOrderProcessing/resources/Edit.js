/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/
var productIdCol = 0;
var typeCol = 1;
var productNameCol = 2;
var stockCol = 3;
var qtyCol = 4;
var priceCol = 5;
var totalCol = 6;
var actionCol = 7;
var comboType = 2;
var productType = {
    "1":"Normal",
    "2":"Combo"
};

var productTypeRevert = {
    "Normal":"1",
    "Combo":"2"
};

var typeNormal = 'Normal';
var typeCombo = 'Combo';
Vtiger_Edit_Js("BhsOrderProcessing_Edit_Js", {}, {

    registerDatatable: function () {
        var thisInstance = this;
        loadDataTable({
            'idx': 0,
            'sDom': 't',
            'bSort': false,
            responsive: true,
            "scrollY": false,
            "columnDefs": [
                // { "bSortable": false, "aTargets": [ 0, 6 ] },
                // { className: "text-info", "targets": [ 1 ] },
                { className: "hidden", "targets": [productIdCol] },
                { "width": "15%", "targets": [typeCol] },
                { "width": "20%", "targets": [productNameCol] },
                { className: "hidden", "targets": [stockCol] },
                { "width": "10%", "targets": [qtyCol] },
                { "width": "15%", "targets": [priceCol] },
                { "width": "10%", "targets": [totalCol] },
                { "width": "15%", "targets": [actionCol] },
            ],
            'customDrawCallback': function (settings) {
                thisInstance.calculateTotal();
                thisInstance.getSelectedProducts();
            },
            "createdRow": function( row, data, dataIndex ) {
                $(row).attr('data-discount', data[8]);
                $(row).attr('data-id', data[0]);
            }
        });
        loadDataTable({
            'idx': 1,
            'sDom': 't',
            'bSort': false,
            'responsive': false,
            "scrollX": false,
            "columnDefs": [
                { className: "hidden", "targets": [0] },
                // { "bSortable": false, "aTargets": [ 0, 6 ] },
                // { className: "text-left", "targets": [ 1, 2 ] },
                // { className: "text-right", "targets": [ 4,5 ] },
                // { "width": "15%", "targets": [0, 2, 3] },
                // { "width": "25%", "targets": [2] },
            ],
            "dt_params": {
                'selectedProducts': function () {
                    return $('#hfSelectedProducts').val();
                },
            }
        });
        loadDataTable({
            'idx': 2,
            'sDom': 'tp',
            'bSort': false,
            "scrollX": false,
            "columnDefs": [
                // { "bSortable": false, "aTargets": [ 0, 6 ] },
                // { className: "text-left", "targets": [ 2 ] },
                // { className: "text-right", "targets": [ 3 ] },
                // { "width": "20%", "targets": [0, 3] },
                // { "width": "30%", "targets": [1, 2] },
            ]
        });
        loadDataTable({
            'idx': 3,
            'sDom': 'tp',
            'bSort': false,
            "scrollX": false,
            "columnDefs": [
                // { "bSortable": false, "aTargets": [ 0, 6 ] },
                // { className: "text-left", "targets": [ 2 ] },
                // { className: "text-right", "targets": [ 3 ] },
                // { "width": "20%", "targets": [0, 3] },
                // { "width": "30%", "targets": [1, 2] },
            ]
        });
        loadDataTable({
            'idx': 4,
            'sDom': 'tp',
            'bSort': false,
            "columnDefs": [
                {"data":"dateTime","targets":0},
                {"data":"userName","targets":1},
                {"data":"statusName","targets":2},
                {"data":"comment","targets":3},
            ]
        });
        loadDataTable({
            'idx': 5,
            'sDom': 'tp',
            'bSort': false,
            "scrollX": false,
            "columnDefs": [
                // { "bSortable": false, "aTargets": [ 0, 6 ] },
                // { className: "text-left", "targets": [ 2 ] },
                // { className: "text-right", "targets": [ 3 ] },
                // { "width": "20%", "targets": [0, 3] },
                // { "width": "30%", "targets": [1, 2] },
            ]
        });
        loadDataTable({
            'idx': 6,
            'sDom': 'tp',
            'bSort': false,
            "scrollX": false,
            "columnDefs": [
                {
                    "targets": [1],
                    "visible": false
                }
                // { "bSortable": false, "aTargets": [ 0, 6 ] },
                // { className: "text-left", "targets": [ 2 ] },
                // { className: "text-right", "targets": [ 3 ] },
                // { "width": "20%", "targets": [0, 3] },
                // { "width": "30%", "targets": [1, 2] },
            ]
        });
    },

    getSelectedProducts: function () {
        var rows = $('#tblMailList0 tbody tr');
        var total = rows.length;
        var selectedProducts = [];
        for (var i = 0; i < total; i++) {
            var prodId = $(rows[i]).attr('data-id');
            if (prodId) {
                if (selectedProducts.indexOf(prodId) < 0) {
                    selectedProducts.push(prodId);
                }
            }
        }
        selectedProducts = selectedProducts.join(',');
        $('#hfSelectedProducts').val(selectedProducts);
    },

    calculateOnRow: function (parent) {
        var quantity = parent.find('input.txtInlineQuantity').val();
        if (quantity == '') {
            quantity = 0;
        }
        quantity = parseFloat(quantity);
        var price = parent.find('select.txtInlinePrice').val();
        if (!price) {
            price = 0;
        }
        price = parseFloat(price);
        var total = quantity * price;
        total = this.formatNumber(total);
        $(parent.find('td')[totalCol]).html(total);

        this.calculateTotal();
    },

    checkIfEdittingProduct: function () {
        // Check if any row has been editting
        var table = $('#tblMailList0');
        var item = table.find('select.cbbInlineProduct.editing');
        if (item.length > 0) {
            return true;
        }
        return false;
    },

    getProductInfo: function (productId, callback) {
        var params = {
            module: 'BhsOrderProcessing',
            action: "ActionAjax",
            mode: "getProductInfo",
            prod_id: productId,
        };
        app.helper.showProgress();
        app.request.post({ 'data': params }).then(
            function (err, data) {
                app.helper.hideProgress();
                callback(err, data);
            }
        );
    },

    registerCustomEvents: function () {
        var thisInstance = this;
        var productsAdd = [];
        var phone = $('#txtPhoneNumber').find(":selected").text();
        $('.select-payment').on('change',()=>{
            if($('.select-payment  option:selected').text()=='Deposit'){
                $('.Deposit').removeClass('hidden');
            }else{
                $('.Deposit').addClass('hidden');
                $('#txtDeposit').val('0');
                $('.dataTables_scrollFoot #depositAmount').text('0');
                $('.dataTables_scrollFoot #codAmount').text('0');
            }
        })
        $('#txtDeposit').change(function(){
            if(!this.value){
                this.value=0;
            }
            if(isNaN(this.value)||(parseInt(this.value)<0)){
                var params = {
                    message:  app.vtranslate('Please input positive number') ,
                };
                app.helper.showErrorNotification(params);
                return;
            }
            $('.dataTables_scrollFoot #depositAmount').text(thisInstance.formatNumber(this.value));
            total=thisInstance.parseNumber($('.dataTables_scrollFoot td.tdTotal').text())-thisInstance.parseNumber(this.value);
            $('.dataTables_scrollFoot #codAmount').text(thisInstance.formatNumber(total));
        })

        $('#btnSavePhone').on('click', function () {
            if ($('#editPhoneNum').val().length === 0) {
                app.helper.showErrorNotification({
                    message: app.vtranslate('LBL_ENTER_PHONE_NUMBER',self.relatedModulename),
                });
                return;
            }

            app.helper.showConfirmationBox({message: app.vtranslate('LBL_ARE_YOU_SURE', self.relatedModulename)}).then(function () {
                var phoneNum = $('#editPhoneNum').val();
                var index = $('#editPhoneNum').data('index');
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "savePhoneNumber",
                    leadId: $('#hfRecordIdUpdate').val(),
                    phoneNum: $('#editPhoneNum').val(),
                    index: $('#editPhoneNum').data('index'),
                }

                app.helper.showProgress();
                app.request.post({'data': params }).then(
                    function (err, data) {
                        app.helper.hideProgress();
                        if (err === null) {
                            $('#btnSavePhone').addClass('hidden');
                            $('#btnCancelPhone').addClass('hidden');
                            $('#editPhoneNum').addClass('hidden');
                            $('#btnEditPhone').removeClass('hidden');
                            $('#txtPhoneNumber').removeClass('hidden');
                            $('#btnClickToDial').removeClass('hidden');
                            $('#txtPhoneNumber option[value='+index+']').text(phoneNum);
                            $('#txtPhoneNumber').select2();
                            var params = {
                                message: data.message ? data.message : app.vtranslate('LBL_SAVE_PHONE_SUCCESS', self.relatedModulename),
                            };
                            app.helper.showSuccessNotification(params);
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
        $('body').on('click', '.btnCallback', function (e) {
            var currenDate = getCurrentDate('-');
            var now = getTimeNow();
            var optionHours = '';
            var optionMinutes = '';
            var date = new Date();
            for (i = 0; i <= 23; i++) {
                optionHours += '<option value="' + i + '"> ' + i + '</option>';
            }
            for (i = 0; i <= 55; i = i + 5) {
                optionMinutes += '<option value="' + i + '"> ' + i + '</option>';
            }
            var data = '\
                    <form>\
                      <div class="modal-dialog">\
                        <div class="modal-content">\
                          <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal">&times;</button>\
                            <h4 class="modal-title">' + app.vtranslate('LBL_CALLBACK_SCHEDULE') + '</h4>\
                          </div>\
                          <div class="modal-body">\
                              <div class="form-group row">\
                                  <label class="col-md-3" for="">' + app.vtranslate('LBL_CALLBACK_TYPES') + ':</label>\
                                  <div class="col-md-9">\
                                      <div class="form-check form-check-inline" style="margin-right:30px;">\
                                         <input class="form-check-input" type="radio" name="callback_type" id="callback_consulting" value="consulting" checked>\
                                         <label class="form-check-label" for="inlineRadio1">' + app.vtranslate('LBL_CONSULTING') + '</label>\
                                      </div>\
                                      <div class="form-check form-check-inline">\
                                          <input class="form-check-input" type="radio" name="callback_type" id="callback_prospect" value="prospect">\
                                          <label class="form-check-label" for="inlineRadio2">' + app.vtranslate('LBL_PROSPECT') + '</label>\
                                       </div>\
                                  </div>\
                              </div>\
                              <div class="form-group">\
                                  <label for="">' + app.vtranslate('LBL_PHONE_NO') + ':</label>\
                                  <div class="form-group-border">\
                                      <div class="row">\
                                          <div class="col-md-6">\
                                              <div class="form-check paddingLeft0 marginBottom10px">\
                                                 <input class="form-check-input" type="radio" name="callback_option_phone" id="callback_avalible" value="callback_avalible" checked>\
                                                 <label class="form-check-label" for="callback_avalible">' + app.vtranslate('LBL_AVAILABLE') + '</label>\
                                              </div>\
                                              <ul class="list-unstyled list_phone_callback">\
                                                <li>'+ phone + '</li>\
                                              </ul>\
                                          </div>\
                                          <div class="col-md-6">\
                                              <div class="form-check paddingLeft0 marginBottom10px">\
                                                    <input class="form-check-input" type="radio" name="callback_option_phone" id="callback_new" value="callback_new">\
                                                    <label class="form-check-label" for="callback_new">' + app.vtranslate('LBL_NEW') + '</label>\
                                              </div>\
                                              <div class="input-group" style="min-width: auto;">\
                                                  <input class="callback_name_new form-control" disabled name="callback_name_new" placeholder="' + app.vtranslate('LBL_ENTER_NAME') + '"/>\
                                              </div>\
                                              <div class="input-group" style="min-width: auto;">\
                                                  <input class="callback_phone_new form-control" type="tel" disabled name="callback_phone_new"  placeholder="' + app.vtranslate('LBL_ENTER_PHONE_NUMBER') + '"/>\
                                              </div>\
                                          </div>\
                                      </div>\
                                  </div>\
                              </div>\
                              <div class="form-group ">\
                                  <label for="">' + app.vtranslate('TIME_TO_CALL') + ':</label>\
                                  <div class="form-group-border">\
                                      <div class="row">\
                                          <div class="col-md-6">\
                                              <div class="form-check paddingLeft0 marginBottom10px">\
                                                 <input class="form-check-input" type="radio" name="callback_time_call" id="callback_select_date_time" value="callback_select_date_time" checked>\
                                                 <label class="form-check-label" for="callback_select_date_time">' + app.vtranslate('LBL_SELECT_DATE_TIME') + '</label>\
                                              </div>\
                                             <div class="input-group date" style="min-width: auto;"">\
                                                  <input class="marginLeftZero inputElement " value="'+ currenDate + '" name="callback_date" id="txtCallbackDate" >\
                                                  <span class="input-group-addon cursorPointer"><i class="fa fa-calendar"></i></span>\
                                             </div>\
                                             <div class="clearfix"></div> \
                                             <div class="input-group" style="min-width: auto;">\
                                                <input class="marginLeftZero inputElement"  value="'+ now + '" name="callback_time" id="txtCallbackTime" >\
                                             </div>\
                                             <div class="clearfix"></div>\
                                          </div>\
                                          <div class="col-md-6">\
                                              <div class="form-check paddingLeft0 marginBottom10px">\
                                                    <input class="form-check-input" type="radio" name="callback_time_call" id="callback_time_period" value="callback_time_period">\
                                                    <label class="form-check-label" for="callback_time_period">' + app.vtranslate('LBL_TIME_PERIOD') + '</label>\
                                              </div>\
                                              <div class="input-group row" style="min-width: auto;">\
                                                  <label class="col-md-5" for="">' + app.vtranslate('LBL_HOUR') + ':</label>\
                                                  <select disabled id="callback_select_hour" class="callback_select_hour select2 col-md-7">\
                                                    <option type="0">' + app.vtranslate('LBL_SELECT_HOURS') + '</option>\
                                                    '+ optionHours + '\
                                                  </select>\
                                              </div>\
                                              <div class="input-group row" style="min-width: auto;">\
                                                  <label class="col-md-5" for="">' + app.vtranslate('LBL_MINUTE') + ':</label>\
                                                  <select disabled id="callback_select_minute" class="callback_select_minute select2 col-md-7">\
                                                    <option  type="0" value="0">' + app.vtranslate('LBL_SELECT_MINUTE') + '</option>\
                                                    '+ optionMinutes + '\
                                                  </select>\
                                              </div>\
                                          </div>\
                                      </div>\
                                  </div>\
                              </div>\
                              <div class="form-group">\
                                  <label for="">' + app.vtranslate('LBL_NOTE') + '</label>\
                                  <textarea class="form-control" id="callback_note" rows="3" ' + app.vtranslate('LBL_ENTER_SOME_NOTE_HERE') + '></textarea>\
                              </div>\
                          </div>\
                          <div class="modal-footer">\
                            <button type="button" class="btn btn-default" data-dismiss="modal">' + app.vtranslate('BTN_CANCEL') + '</button>\
                            <button type="submit" class="btn btn-blue btnSetCallbackSchedule" data-dismiss="modal">' + app.vtranslate('BTN_SAVE') + '</button>\
                          </div>\
                        </div>\
                    </div></form>';
            app.helper.showModal(data);

            $(".input-group.date").datetimepicker({
                format: 'DD/MM/YYYY',
            });
            $('#txtCallbackTime').datetimepicker({
                format: 'HH:mm '
            });
            $('#txtCallbackTime').keyup(()=>{
                if(!this.value){
                    $('#txtCallbackTime').val('00:00')
                }
            })
            $("#txtCallbackDate").val(moment().format("DD/MM/YYYY"));
        });
        /**
         * get current date
         * @param {} sp
         */
        function getCurrentDate(sp) {
            today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //As January is 0.
            var yyyy = today.getFullYear();

            if (dd < 10) dd = '0' + dd;
            if (mm < 10) mm = '0' + mm;
            return (yyyy + sp + mm + sp + dd);
        }
        /**
         * Get time now
         */
        function getTimeNow() {
            var d = new Date(),
                h = d.getHours(),
                m = d.getMinutes();
            if (h < 10) h = '0' + h;
            if (m < 10) m = '0' + m;

            return h + ':' + m;
        }

        String.prototype.capitalize = function () {
            return this.charAt(0).toUpperCase() + this.slice(1);
        }
        // btnRejected
        $('body').on('click', '.btnRejected', function (e) {
            leadStatusModal($(this).data('name'));
            $(".select_reason .alert").addClass('alert-red');
        });
        $('body').on('click', '.btnCanel', function (e) {
            $(".list_reason .reason,.list_reason .reasonUncalled, .list_reason .reasonTrash").remove();
            $(".btn-bhs,.btnCallback").show();
            $(".select_reason,.btnCanel").addClass("hidden");
            $(".select_reason .alert").removeAttr('id');
            $(".btnSaveSO").attr("disabled", 'disabled');;

        });
        $('body').on('click', ' .btnApproved, .btnUncalled, .btnTrash', function (e) {
            setLeadStatusValue($(this).data('name'));
        });



        function formatDate(currentDate, sp) {
            //var strDate = currentDate.getDate() + sp + (currentDate.getMonth()+1) + sp + currentDate.getFullYear();
            var dd = currentDate.getDate();
            var mm = currentDate.getMonth() + 1; //As January is 0.
            var yyyy = currentDate.getFullYear();

            if (dd < 10) dd = '0' + dd;
            if (mm < 10) mm = '0' + mm;
            return (dd + sp + mm + sp + yyyy);

        }
        function timeAdd(datetime = null, value = null, type) {
            if (!datetime) {
                datetime = new Date();
            }
            if (type == 'h') {
                datetime.setHours(parseInt(datetime.getHours()) + parseInt(value));
            } else if (type == 'i') {
                datetime.setMinutes(parseInt(datetime.getMinutes()) + parseInt(value));
            }

            return datetime;
        }
        $('body').on('change', '#callback_select_hour', function (e) {
            var hour = $(this).val();
            var min = $('#callback_select_minute').val();
            if (!min) {
                min = '0';
            }

            var currentDate = new Date();
            currentDate = timeAdd(currentDate, hour, 'h');
            currentDate = timeAdd(currentDate, min, 'i');
            var newStrDate = formatDate(currentDate, '/');
            newHour = currentDate.getHours();
            newMin = currentDate.getMinutes();

            newHour = newHour < 10 ? '0' + newHour : newHour;
            newMin = newMin < 10 ? '0' + newMin : newMin;
            strTime = newHour + ':' + newMin;
            $('#txtCallbackDate').val(newStrDate);
            $('#txtCallbackTime').val(strTime);
        });

        $('body').on('change', '#callback_select_minute', function (e) {
            var min = $(this).val();
            var hour = $('#callback_select_hour').val();
            if (!hour) {
                hour = '0';
            }
            var currentDate = new Date();
            currentDate = timeAdd(currentDate, hour, 'h');
            currentDate = timeAdd(currentDate, min, 'i');
            var newStrDate = formatDate(currentDate, '/');
            newHour = currentDate.getHours();
            newMin = currentDate.getMinutes();

            newHour = newHour < 10 ? '0' + newHour : newHour;
            newMin = newMin < 10 ? '0' + newMin : newMin;
            strTime = newHour + ':' + newMin;
            $('#txtCallbackDate').val(newStrDate);
            $('#txtCallbackTime').val(strTime);
        });

        function setLeadStatusValue(name) {
            $(".btn-bhs,.btnCallback").hide();
            $(".select_reason,.btnCanel").removeClass("hidden");
            if (name == 'approved') {
                $("#leadStatus").val(2);
                $(".list_reason").hide();
                $(".select_reason .alert").removeClass('alert-red');
                $(".select_reason .alert").attr('id', 'alert-green');
            }
            if (name == 'uncalled') {
                var  statusUnCall=$('.list_reason .reasonUncalled option:selected').data('id')?$('.list_reason .reasonUncalled option:selected').data('id'):10;
                $(".reasonUncalled").clone().appendTo(".list_reason");
                $(".list_reason .reasonUncalled").removeClass("hidden");
                $(".list_reason").show();
                $("#leadStatus").val(statusUnCall);
                $(".select_reason .alert").removeClass('alert-red');
                $(".select_reason .alert").attr('id', 'alert-violet');
            }
            if (name == 'trash') {
                $("#leadStatus").val(5);
                $(".reasonTrash").clone().appendTo(".list_reason");
                $(".list_reason .reasonTrash").removeClass("hidden");
                $(".list_reason").show();
                $(".select_reason .alert").removeClass('alert-red');
                $(".select_reason .alert").attr('id', 'alert-black');
            }
            $(".select_reason .alert").text(name);
            $("#leadStatusName").val(name);
            $(".btnSaveSO").removeAttr("disabled");
        }

        // Show modal lead status
        function leadStatusModal(name) {
            $(".reason").clone().appendTo(".list_reason");
            $(".list_reason .reason").removeClass("hidden");
            $(".btn-bhs,.btnCallback").hide();
            $(".select_reason,.btnCanel").removeClass("hidden");
            $(".list_reason").show();
            $("#leadStatus").val(3);
            $("#leadStatusName").val('Reject');
            $(".select_reason .alert").text(name);
            $(".btnSaveSO").removeAttr("disabled");
        }


        $('body').on('change', 'select.cbbInlineProduct', function () {
            var parent = $(this).closest('tr');
            var cbbInlinePrice = parent.find('select.txtInlinePrice');
            var cbbInlineStock = parent.find('select.cbbInlineStock');
            cbbInlinePrice.html('');
            var selectedProdId = $(this).val();
            var prices = $(this).find('option:selected').attr('data-price');
            var discount = $(this).find('option:selected').attr('data-discount');

            parent.attr('data-discount',discount);
            if (!prices) {
                return true;
            }
            prices = prices.split('|');
            var total = prices.length;
            for (var i = 0; i < total; i++) {
                var price = parseFloat(prices[i]);
                if (price >= 0) {
                    cbbInlinePrice.append(new Option(thisInstance.formatNumber(price), price));
                }
            }
            cbbInlinePrice.val(price);
            cbbInlinePrice.trigger("liszt:updated");
            cbbInlinePrice.trigger('change.select2');
            cbbInlinePrice.trigger('change');
            // @Todo: change stock
            getStockByProduct($(this).val(), (data) => {
                data = data.data[0].partnerId;
                cbbInlineStock.val(data);
                cbbInlineStock.trigger("liszt:updated");
                cbbInlineStock.trigger('change.select2');
                cbbInlineStock.trigger('change');
            })

        });
        $('body').on('click', '.btnRefreshStock', function () {
            $('#tblMailList1 .dataTables_empty').hide();
            productsAdd.forEach((pro) => {
                $('#tblMailList1').find('tbody').append(pro);
            });
        });

        //});

        $('body').on('click', '.btnSetLeadStatus', function (e) {
            var leadStatusText = $(e.target).data('label') + ': ' + $('#selectSetRejected').val();
            $('.lead-status-selected').text(leadStatusText);

        });

        $('body').on('click', '.btnSetCallbackSchedule', function (e) {
            // if(!$('#txtDestinationAddress').val()||!$('.cbbProvinces:nth-child(2)').val()||!$('.cbbTowns:nth-child(2)').val()||!$('.cbbDistricts:nth-child(2)').val()){
            //     var params = {
            //         message:app.vtranslate('LBL_INPUT_ADDRESS'),
            //     };
            //     app.helper.showErrorNotification(params);
            //     return;
            // }
            phoneNumber = $('#txtPhoneNumber').find(":selected").text();
            is_callback_new = $('#callback_new').is(':checked');
            phoneName=$("#txtFullname").val();
            if(is_callback_new) {
                phoneNumber = $('.callback_phone_new').val();
                phoneName = $('.callback_name_new').val();
            }
            callback_date = $('#txtCallbackDate').val();
            callback_time = $('#txtCallbackTime').val();
            is_callback_time_period = $('#callback_time_period').is(':checked');
            // if(is_callback_time_period) {
            //     callback_time = $('.callback_select_hour').val() + $('.callback_select_minute').val();
            // }
            var returnUrl = $('.btnSaveSO').data("return-url");
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "setCallbackSchedule",
                callback_date: callback_date, //$('#txtCallbackDate').val(),
                callback_time: callback_time, //$('#txtCallbackTime').val(),
                phone: phoneNumber,
                so_id: $('#hfRecordId').val(),
                timecall_type: (is_callback_time_period) ? 'period' : 'select',
                callback_type: ($('#callback_consulting').is(':checked')) ? 8 : 9,
                phone_type: (is_callback_new) ? 1 : 0,
                note: $("#callback_note").val(),
                phoneName: phoneName
            };

            statusLeadText = params['leadStatus'] = $("#leadStatusName").val();//$('.lead-status-selected').text();
            reason = '';
            callback_type = ($('#callback_consulting').is(':checked')) ? 8 : 9;
            $(".alert").text("callback");
            $("#leadStatus").val(callback_type);
            callback_text = ($('#callback_consulting').is(':checked')) ? 'Consulting' : 'Prospect';
            callback_text += ', Date time: ' + callback_date + " " + callback_time;
            params['name'] = $('#txtFullname').val();
            // params['phone'] = $('#txtPhoneNumber').find(":selected").text();
            params['address'] = $('#txtAddress').val(); // @Todo: not cleared
            params['agcCode'] = ''; // @Todo: not cleared
            params['agcId'] = ''; // @Todo: not cleared
            //params['amount'] = amount;
            params['attribute'] = ''; // @Todo: not cleared
            params['calledby'] = 0; // @Todo: not cleared
            params['ccCode'] = ''; // @Todo: not cleared
            params['clGroup'] = 0; // @Todo: not cleared
            params['comment'] = $('#txtComment').val();
            params['cpId'] = $('#hfCampaignId').val();
            params['dayCall'] = 0; // @Todo: not cleared
            params['district'] = $("select.cbbDistricts").val();
            params['province'] = $("select.cbbProvinces").val();
            params['subdistrict'] = $("select.cbbTowns").val();
            params['neighborhood'] = $("select.cbbNeighborhoods").val();
            params['postalCode'] = $("select.cbbZipcode").val();
            params['userDefin05'] = 'Callback ' + callback_text;
            params['leadStatus'] = $("#leadStatus").val();// $('#leadStatus').val();//$('input[name=action]:checked').val();
            params['SOtype'] = $('#SOtype').val();

            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            window.location.href = returnUrl;
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

        $('body').on('click', '.btnInlineRemove', function (e) {

            var thisBtn = $(this);
            app.helper.showConfirmationBox({ message: app.vtranslate('Are you sure want to remove?') }).then(function () {
                var parent = thisBtn.closest('tr');
                prodId = $(parent.find('td')[productIdCol]).text();
                price = thisInstance.parseNumber($(parent.find('td')[priceCol]).text());
                console.log('quantity: ' + price);
                var amount = thisInstance.parseNumber($('.dataTables_scrollFoot td.tdTotal').text());
                console.log('amount: ' + amount);
                amount = amount - price;
                console.log('amount: ' + amount);
                $('.dataTables_scrollFoot td.tdTotal').text(thisInstance.formatNumber(amount));
                thisBtn.closest('tr').remove();
                trStocks = $('#tblMailList1').find('tr');
                for (i = 0; i < trStocks.length; i++) {
                    tr = $(trStocks[i]);
                    id = $(tr.find('td')[productIdCol]).text();
                    if (id == prodId) {
                        tr.remove();
                        // Remove product stock
                        productsAdd.forEach((row, index) => {
                            id = $(row.find('td')[productIdCol]).text();
                            if (id == prodId) {
                                productsAdd.splice(index, 1);
                            }
                        });
                    }
                };
                trProduct = $('#tblMailList5').find('tr');
                trProduct_agent = $('#tblMailList6').find('tr');
                for (i = 0; i < trProduct.length; i++) {
                    tr = $(trProduct[i]);
                    id = $(tr.find('td span')).data('id');
                    if (id == prodId) {
                        tr.remove();
                        $(trProduct_agent[i]).remove();
                    }
                };
                thisInstance.calculateTotal();
                $('#txtDeposit').change()
            });
        });
        $('body').on('click', '.btnInlineEdit', function (e) {

            var productId = $(this).closest('tr').attr('data-id');
            if (thisInstance.checkIfEdittingProduct()) {
                return true;
            }

            var parent = $(this).closest('tr');
            old_stock =$(parent.find('td')[stockCol]).text();
            oldType = $(parent.find('td')[typeCol]).text();
            // Remove class if yes
            parent.find('.btnInlineEdit').removeClass('clone');
            parent.find('.btnInlineView').removeClass('clone');
            parent.find('.btnInlineRemove').removeClass('clone');
            parent.find('.btnInlineSave').removeClass('clone');
            parent.find('.btnInlineCancel').removeClass('clone');

            var tds = parent.find('td');
            //type
            var typeTd = $(tds[typeCol]);
            var type = typeTd.html();
            typeTd.attr('data-oldValue', type);
            var html = '<select class="cbbInlineType editing inputElement select2">';
            html += $(this).closest('table').find('.cloneRow').find('select.cbbInlineType').html();
            html += '</select>';
            typeTd.html(html);
            $(this).closest('tr').find('select.cbbInlineType').val(type);
            vtUtils.showSelect2ElementView(typeTd.find('select.cbbInlineType'));
            oldTypeTd=$('.cbbInlineType option:contains("'+oldType+'")').val();
            $('.cbbInlineType.editing').val(oldTypeTd).trigger('change');
            // Product
            if (true) {
                var productTd = $(tds[productNameCol]);
                var product = productTd.html();
                productTd.attr('data-oldValue', product);
                var html = '<select class="cbbInlineProduct editing inputElement select2">';
                html += $(this).closest('table').find('.cloneRow').find('select.cbbInlineProduct').html();
                html += '</select>';
                productTd.html(html);
                productTd.addClass('cbbInlineProductComboTd');
                if (type == typeCombo) {
                    productTd.find('.comboItem').removeClass('hidden');
                    productTd.find('.productItem').addClass('hidden');
                } else {
                    productTd.find('.productItem').removeClass('hidden');
                    productTd.find('.comboItem').addClass('hidden');
                }
                $(this).closest('tr').find('select.cbbInlineProduct').val(productId);
                vtUtils.showSelect2ElementView(productTd.find('select.cbbInlineProduct'));
                old_productID=$(tds[productIdCol]).text();
                $('.cbbInlineProduct.editing').val(old_productID).trigger('change');
            }

            // Stock
            if ($('.STOCKSTATUS').text()) {
                var stockTd = $(tds[stockCol]);
                var stock = stockTd.html();
                stockTd.attr('data-oldValue', stock);
                html = '<select class="cbbInlineStock inputElement select2">';
                html += $(this).closest('table').find('.cloneRow').find('select.cbbInlineStock').html();
                html += '</select>';
                stockTd.html(html);
                var cbbInlineStock = stockTd.find('.cbbInlineStock');
                vtUtils.showSelect2ElementView(cbbInlineStock);
                stock_id=$('.cbbInlineStock option:contains("'+old_stock+'")').val();
                $('.cbbInlineStock').val(stock_id).trigger('change');
            }

            // Quantity
            if (true) {
                var quantityTd = $(tds[qtyCol]);
                var quantity = quantityTd.html();
                quantityTd.attr('data-oldValue', quantity);
                quantity = thisInstance.parseNumber(quantity);
                html = '<input value="' + quantity + '" class="txtInlineQuantity inputElement" />';
                quantityTd.html(html);
            }

            // Price
            if (true) {
                var priceTd = $(tds[priceCol]);
                var price = priceTd.children(":first").html();
                if (price == undefined || price == '') {
                    price = priceTd.html();
                }
                priceTd.attr('data-oldValue', price);
                var _price = thisInstance.parseNumber(price);
                html = '<select class="txtInlinePrice inputElement select2">';
                html += '</select>';
                priceTd.html(html);
                thisInstance.getProductInfo(productId, function (err, data) {
                    var prices = data.record['price'];
                    prices = prices.split('|');
                    var total = prices.length;
                    var cbbInlinePrice = priceTd.find('.txtInlinePrice');
                    for (var i = 0; i < total; i++) {
                        var price = parseFloat(prices[i]);
                        if (price >= 0) {
                            cbbInlinePrice.append(new Option(thisInstance.formatNumber(price), price));
                        }
                    }
                    cbbInlinePrice.val(_price);
                    vtUtils.showSelect2ElementView(cbbInlinePrice);
                    cbbInlinePrice.trigger("liszt:updated");
                    cbbInlinePrice.trigger('change.select2');
                    cbbInlinePrice.trigger('input');
                });
            }

            // Show/hide inline buttons
            // parent.find('.btnInlineEdit').hide();
            // parent.find('.btnInlineView').hide();
            // parent.find('.btnInlineRemove').hide();
            // parent.find('.btnInlineSave').show();
            // parent.find('.btnInlineCancel').show();
            action = '<a class="btnInline btnInlineSave clone"><i class="fa fa-check"></i></a><a class="btnInline btnInlineCancel clone"><i class="fa fa-trash"></i></a><a class="btnInline btnInlineEdit clone" style="display: none;"><i class="fa fa-pencil"></i></a><a class="btnInline btnInlineView clone" style="display: none;"><i class="fa fa-eye"></i></a><a class="btnInline btnInlineRemove clone" style="display: none;"><i class="fa fa-trash"></i></a>';
            $(tds[actionCol]).html(action);
            return;
            var params = {
                module: app.getModuleName(),
                view: "ViewAjax",
                mode: "showEdit",
                id: $(this).data('id'),
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        app.helper.showModal(data);
                    } else {
                    }
                }
            );
        });
        $('body').on('change', '.cbbInlineType', function (e) {
            var $selectInline = $(this).parent().parent().find('.cbbInlineProductComboTd');
            $selectInline.find('select.cbbInlineProduct').val('-1'); // Select the option with a value of '1'
            $selectInline.find('select.cbbInlineProduct').trigger('change');
            if ($(this).val() == comboType) {
                $selectInline.find('.comboItem').removeClass('hidden');
                $selectInline.find('.productItem').addClass('hidden')
            } else {
                $selectInline.find('.comboItem').addClass('hidden');
                $selectInline.find('.productItem').removeClass('hidden');
            }

        });
        $('body').on('click', '.btnInlineCancel', function (e, fromSave) {
            $('#txtDeposit').change()
            if ($(this).hasClass('clone')) {
                if (!fromSave) {
                    $(this).closest('tr').remove();
                    thisInstance.calculateTotal();
                    return true;
                }
            }
            var parent = $(this).closest('tr');
            var tds = parent.find('td');
            //type
            var typeTd = $(tds[typeCol]);
            var typeId = typeTd.attr('data-oldValue');
            typeTd.html(productType[typeId]);

            // Product
            if (true) {
                var productTd = $(tds[productNameCol]);
                var product = productTd.attr('data-oldValue');
                productTd.html(product);
            }

            // Stock
            if (true) {
                var stockTd = $(tds[stockCol]);
                var stock = stockTd.attr('data-oldValue');
                stockTd.html(stock);
            }

            // Quantity
            if (true) {
                var quantityTd = $(tds[qtyCol]);
                var quantity = quantityTd.attr('data-oldValue');
                quantityTd.html(quantity);
            }

            // Price
            if (true) {
                var priceTd = $(tds[priceCol]);
                var price = priceTd.attr('data-oldValue');
                priceTd.html(price);
            }

            // Show/hide inline buttons
            parent.find('.btnInlineEdit').show();
            parent.find('.btnInlineView').show();
            parent.find('.btnInlineRemove').show();
            parent.find('.btnInlineSave').hide();
            parent.find('.btnInlineCancel').hide();

            thisInstance.calculateTotal();
        });

        $('body').on('click', '.btnInlineSave', function (e) {
            $('#txtDeposit').change();
            var parent = $(this).closest('tr');
            var cbbInlineProduct = parent.find('select.cbbInlineProduct');
            var cbbInlineType = parent.find('select.cbbInlineType option:selected');
            var old_product_id = parent.attr('data-id');
            var productId = cbbInlineProduct.val();
            var productName = cbbInlineProduct.find('option:selected').html();
            var cbbInlineStock = parent.find('select.cbbInlineStock option:selected');
            var stock = cbbInlineStock.text();
            var type =cbbInlineType.text();

            $(parent.find('td')[productIdCol]).html(productId);
            if (stock == 'Select stock') {
                stock = '';
            }
            if (type == 'Select type') {
                type = '';
            }
            if(!$('.STOCKSTATUS').text()){
                stock=$(parent.find('td')[stockCol]).html();
            }
            if(!$('.cbbInlineType').text()){
                type=$(parent.find('td')[typeCol]).html();
            }
            // Validate
            if (!productId || productId < 0) {
                var params = {
                    message: 'Please select product and stock',
                };
                app.helper.showErrorNotification(params);
                return;
            }
            var txtInlineQuantity = parent.find('input.txtInlineQuantity');
            var quantity = txtInlineQuantity.val();
            if (quantity == 0) {
                var params = {
                    message: 'Please input quantity',
                };
                app.helper.showErrorNotification(params);
                return;
            }

            old_product = $($(parent.find('td')[productNameCol]).attr('data-oldvalue')).text();
            new_product=cbbInlineProduct.select2('data').text;
            var row = $('#tblMailList5 tr').eq(1).clone();
            var row_agent = $('#tblMailList6 tr').eq(1).clone();
            if ((old_product != '')&&(old_product!=new_product)) {
                checkExistProduct(old_product, 5, productNameCol);
                checkExistProduct(old_product, 6, productNameCol);
            }
            //old_product_id = $(parent.find('td')[0]).attr('data-oldvalue');
            if (old_product_id != '') {
                checkExistProduct(old_product_id, 1, productIdCol);
            }
            var isDuplicate = false;
            var rows = $('#tblMailList0').find('tbody').find('tr');
            for (var i = 0; i < rows.length; i++) {
                var tr = $(rows[i]);
                var prodId = tr.attr('data-id');
                if (productId == prodId && parent.index() != i) {
                    isDuplicate = true;
                    var qty = 0;
                    app.helper.showConfirmationBox({ message: app.vtranslate('ITEM_EXISTS') }).then(function () {
                        if ($(tr.find('td')[qtyCol]).children().length > 0 ){
                            qty = thisInstance.parseNumber( $(tr.find('td')[qtyCol]).find(':input').val())+thisInstance.parseNumber(quantity);
                            $(tr.find('td')[qtyCol]).find(':input').val(qty);
                        } else {
                             qty = thisInstance.parseNumber( $(tr.find('td')[qtyCol]).html())+thisInstance.parseNumber(quantity);
                            $(tr.find('td')[qtyCol]).html(qty);
                        }
                        $(tr.find('td')[qtyCol]).attr('data-oldvalue', qty);
                        thisInstance.calculateOnNotEditRow(tr);
                        parent.remove();
                        thisInstance.calculateTotal();
                    });
                    break;
                }
            }
            if (isDuplicate) {
                return;
            }
            parent.attr('data-id', productId);
            $(parent.find('td')[productNameCol]).attr('data-oldvalue', '<span class="text-info">' + productName + '</span>');
            $(parent.find('td')[stockCol]).attr('data-oldvalue', stock);
            $(parent.find('td')[qtyCol]).attr('data-oldvalue', quantity);
            var cbbInlineType = parent.find('select.cbbInlineType');
            var typeId = cbbInlineType.val();
            $(parent.find('td')[typeCol]).attr('data-oldvalue', typeId);
            var txtInlinePrice = parent.find('select.txtInlinePrice');
            var price = txtInlinePrice.val();
            $(parent.find('td')[priceCol]).attr('data-oldvalue', price);
            var priceIndex = txtInlinePrice[0].selectedIndex;
            $(parent.find('td')[priceCol]).attr('data-priceIndex', priceIndex);

            $(this).closest('td').find('.btnInlineCancel').trigger('click', [true]);
            const action = '<div class="btn-group btn-group-action">\
                <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\
                <img src="layouts/v7/resources/Images/icon/icon-action.png" alt="">\
                </a>\
                <div class="dropdown-menu dropdown-menu-right">\
                <a class="btnInlineEdit"><i class="fa fa-pencil"></i> Edit</a>\
                <a class="btnInlineView"><i class="fa fa-eye"></i> View</a>\
                <a class="btnInlineRemove"><i class="fa fa-trash"></i> Trash</a>\
                <a class=" btnInlineSave" style="display: none;"><i class="fa fa-check"></i> Save</a>\
                <a class="btnInlineCancel" style="display: none;"><i class="fa fa-close"></i> Cancel</a>\
                </div>\
            </div>';
            $(parent.find('td')[actionCol]).html(action);
            thisInstance.getSelectedProducts();
            checkExistProductOnstock(productId, (ck) => {
                // console.log(ck);
                if (ck) {
                    // Update stock product
                } else {
                    getStockByProduct(productId, (data) => {
                        data = data.data;
                        if (data == '') {
                            return;
                        }
                        //
                        trProduct = $('#tblMailList1').find('tr');
                        for (i = 0; i < trProduct.length; i++) {
                            tr = $(trStocks[i]);
                            id = $(tr.find('td')[0]).text();
                            if (id == productId) {
                                tr.remove();
                            }
                        };
                        // console.log(data);
                        data.forEach(st => {
                            if (st.prodId == productId) {
                                var row = $('#tblMailList1 tr').eq(1).clone().show();
                                stock_parent = $(row);
                                if (stock_parent.text() == 'No data found in the server' || stock_parent.text() == 'No data available in table') {
                                    $('#tblMailList1 tr').eq(1).remove();
                                    strTr = '<td></td><td></td><td></td><td></td><td></td>';
                                    stock_parent.html(strTr);
                                }
                                $(stock_parent.find('td')[0]).html(st.prodId);
                                $(stock_parent.find('td')[1]).html(productName);
                                $(stock_parent.find('td')[2]).html(st.partnerName);
                                $(stock_parent.find('td')[3]).html(st.quantityAvailable);
                                $(stock_parent.find('td')[4]).html(st.quantityTotal);

                                $('#tblMailList1').find('tbody').append(row);
                                productsAdd.push(row);
                                // Update product description
                            }
                        });
                        pro_parent = $(row);
                        pro_agent = $(row_agent);
                        getProductInfo(productId, function (prod) {
                            // $(pro_parent.find('td')[0]).html(prod.prodId);
                            const prod_name = '<span class="text-info" data-id='+prod.prodId+'>' + prod.name + '</span>';
                            $(pro_parent.find('td')[0]).html(prod_name);
                            $(pro_parent.find('td')[1]).html(prod.dscr);
                            $(pro_agent.find('td')[0]).html(prod_name);
                            $(pro_agent.find('td')[1]).html(prod.dscrForAgent);
                        });
                        $('#tblMailList6').find('tbody').append(row_agent);
                        $('#tblMailList5').find('tbody').append(row);
                    });
                }
            });
        });

        function checkExistProductOnstock(productId, callBack) {
            trStocks = $('#tblMailList1').find('tr');
            for (i = 0; i < trStocks.length; i++) {
                tr = $(trStocks[i]);
                id = $(tr.find('td')[0]).text();
                if (id == productId) {
                    callBack(true);
                    return;
                }
            };

            callBack(false);
        }

        function checkExistProduct(productName, tblIndex, columnIndex) {
            trStocks = $('#tblMailList' + tblIndex).find('tr');

            for (i = 0; i < trStocks.length; i++) {
                tr = $(trStocks[i]);
                name = $(tr.find('td')[columnIndex]).text();
                if (tblIndex != 1) {
                    $('#tblMailList' + tblIndex).find('tr:contains("'+productName+'")').remove();
                }
                if (name == productName) {
                    if (tblIndex == 1) {
                        productsAdd.forEach((row, index) => {
                            id = $(row.find('td')[productIdCol]).text();
                            if (id == productName) {
                                productsAdd.splice(index, 1);
                            }
                        });
                    }
                    tr.hide();
                }
            };
        }
        // get Product by id
        function getProductInfo(prdId, callBack) {
            var params = {
                module: 'BhsOrderProcessing',
                action: "ActionAjax",
                mode: "getProductInfo",
                prod_id: prdId,
            };

            app.request.post({ 'data': params }).then(
                function (err, data) {
                    if (err === null) {
                        if (data.success) {
                            callBack(data.record);
                        }
                    } else {
                        callBack('');
                    }
                }
            );
        }
        //
        function getStockByProduct(productId, callBack) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getAllProducts",
                prodId: productId
            };

            app.request.post({ 'data': params }).then(
                function (err, data) {
                    if (err === null) {
                        if (data.success) {
                            callBack(data.data);
                        }
                    } else {
                        callBack('');
                    }
                }
            );
        }
        $('body').on('click', '.btnInlineView', function (e) {
            var pro = $(this).closest("tr").find(".hidden").html();
            var params = {
                module: 'BhsProduct',
                view: "ViewAjax",
                mode: "showDetail",
                id: pro,
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        app.helper.showModal(data);
                    } else {
                    }
                }
            );
        });

        $('body').on('click', '.addProduct', function (e) {
            var row = $('.cloneRow').clone();
            row.removeClass('cloneRow');
            if ($('#tblMailList0 tbody tr').last().hasClass('odd')) {
                row.addClass('even');
            } else {
                row.addClass('odd');
            }
            var productElementId = 'cbbInlineProduct_' + thisInstance.getRandomUnique();
            row.find('select.cbbInlineProduct').attr('id', productElementId);
            var stockElementId = 'cbbInlineStock_' + thisInstance.getRandomUnique();
            row.find('.cbbInlineStock').attr('id', stockElementId);
            $('#tblMailList0').find('tbody').append(row[1]);
            vtUtils.showSelect2ElementView($('#' + productElementId));
            vtUtils.showSelect2ElementView($('#' + stockElementId));
            if (!$('.STOCKSTATUS').text()) {
                $(row.find('td')[stockCol]).html($($('.cbbInlineStock option:nth-child(2)')[1]).text());
            }
        });

        $('body').on('input', '.txtInlineQuantity', function (e) {
            var parent = $(this).closest('tr');
            thisInstance.calculateOnRow(parent);
        });

        $('body').on('change', '.txtInlinePrice', function (e) {
            var parent = $(this).closest('tr');
            thisInstance.calculateOnRow(parent);
        });
        var icon_link = '';

        $('body').on('click', '#btnClickToDial', function (e) {
            if ($(this).attr('data-source') == 'call') {
                $(this).attr('data-source', 'hangup');
                icon_link = 'layouts/v7/resources/Images/icon/icon-phone-hangup.png';
                var phoneNumber = $('#txtPhoneNumber').find(":selected").text();
                var leadId = $('#hfRecordIdUpdate').val();
                if (!phoneNumber) {
                    return true;
                }
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "callCustomer",
                    phone: phoneNumber,
                    leadId: leadId,
                };
                callCustomer(params, icon_link, (channelId) => {
                    if(channelId) {
                        $('#btnClickToDial').attr('data-channelId', channelId);
                    }

                })

            } else if ($(this).attr('data-source') == 'hangup') {
                $(this).attr('data-source', 'call');
                icon_link = 'layouts/v7/resources/Images/icon/icon-phone.png';
                channelId = $('#btnClickToDial').attr('data-channelId');
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "hangupToCustomer",
                    channelId: channelId
                };
                callCustomer(params, icon_link, (channelId) => {
                    $('#btnClickToDial').attr('data-channelId', null);
                });
            }
        });
        function callCustomer(params, icon_link, callBack) {
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            $('#btnClickToDial').toggleClass('is-called');
                            $('#btnClickToDial img').attr('src', icon_link);
                            if(data.data.channelId) {
                                callBack(data.data.channelId);
                            }
                        } else {
                            var params = {
                                message: data.message,
                            };
                            app.helper.showErrorNotification(params);
                        }
                    } else {
                        app.helper.hideProgress();
                    }
                }
            );
            callBack('');
        }

        $('body').on('change', '.cbbProvinces', function (e) {
            $('.cbbTowns').prop('selectedIndex', 0);
            $('.cbbTowns').trigger("liszt:updated");
            $('.cbbTowns').trigger('change.select2');
            updateDestinationAddress();
            thisInstance.changeProvince($(this).val())
            var status = $(this).children('option:selected').data('statusname');
            var status_id = $(this).children('option:selected').data('status');            
            var status_id_lst = JSON.parse($("#show_warning_located").val());
            if(status != '' && status != 'normal' && ($.inArray(status_id, status_id_lst) != -1))
            {
                $(".locationname").text(e.added.text);
                $(".statusname").text(status);
                $("#myWarningModal").modal();
            }
        });

        $('body').on('change', '.cbbDistricts', function (e) {
            updateDestinationAddress();
            thisInstance.changeDistrict($(this).val());
            var status = $(this).children('option:selected').data('statusname');
            var status_id = $(this).children('option:selected').data('status');
            var status_id_lst = JSON.parse($("#show_warning_located").val());
            if(status != '' && status != 'normal' && ($.inArray(status_id, status_id_lst) != -1))
            {
                $(".locationname").text(e.added.text);
                $(".statusname").text(status);
                $("#myWarningModal").modal();
            }
        });

        $('body').on('change', '.cbbTowns', function (e) {
            updateDestinationAddress();
            thisInstance.changeTown($(this).val());
            var status = $(this).children('option:selected').data('statusname');
            var status_id = $(this).children('option:selected').data('status');
            var status_id_lst = JSON.parse($("#show_warning_located").val());
            if(status != '' && status != 'normal' && ($.inArray(status_id, status_id_lst) != -1))
            {
                $(".locationname").text(e.added.text);
                $(".statusname").text(status);
                $("#myWarningModal").modal();
            }
        });

        $('body').on('change', '.cbbNeighborhoods', function (e) {
            updateDestinationAddress();
            thisInstance.changeNeighborhood($(this).val());
            var status = $(this).children('option:selected').data('statusname');
            var status_id = $(this).children('option:selected').data('status');
            var status_id_lst = JSON.parse($("#show_warning_located").val());
            if(status != '' && status != 'normal' && ($.inArray(status_id, status_id_lst) != -1))
            {
                $(".locationname").text(e.added.text);
                $(".statusname").text(status);
                $("#myWarningModal").modal();
            }
        });

        $('body').on('change', '.cbbZipcode', function (e) {
            let provinceId = $(this).find(':selected').data('province_id'),
                districtId = $(this).find(':selected').data('district_id'),
                subDistrictId = $(this).find(':selected').data('subdistrict_id'),
                nbhId = $(this).find(':selected').data('neighborhood_id'),
                cbbProvince = $('select.cbbProvinces');
            cbbProvince.val(provinceId);
            cbbProvince.trigger('change.select2');
            thisInstance.changeProvince(provinceId, districtId);
            thisInstance.changeDistrict(districtId, subDistrictId);
            thisInstance.changeTown(subDistrictId, nbhId);
            setTimeout(function () { updateDestinationAddress() }, 1000);
        });

        $('input.select2-input').on('keyup', function() {
            var postalCode = $("#zipcode").data('select2').search[0].value;
            if (!postalCode || !Number.isInteger(parseInt(postalCode))) {
                console.log('invalid postalCode');
            } else {
                thisInstance.getRelatedZipcode(postalCode);
            }
        });

        $('body').on("select2-open", '.cbbZipcode', function() {
            var zipCodeSelected = $(this).find(':selected').data('postalcode'),
                cbbZipcode = $('select.cbbZipcode');
            if (zipCodeSelected) {
                cbbZipcode.select2("search", zipCodeSelected);
            }
        });

        $('body').on('change input', '#sale-discount', function (e) {
            thisInstance.calculateTotal();
        });

        $('body').on('change input', '#sale-discount-percent', function (e) {
            let saleDiscountPercent = $(this).val();
            if (saleDiscountPercent.length === 0) {
                saleDiscountPercent = 0;
            }
            saleDiscountPercent = parseFloat(saleDiscountPercent).toFixed(2);
            let totalBeforeSaleDiscount = $('.dataTables_scrollFootInner table #totalBeforeSaleDiscount').html();
            let saleDiscount = Math.round(saleDiscountPercent * totalBeforeSaleDiscount / 100);
            $('.dataTables_scrollFootInner table #sale-discount').val(thisInstance.formatNumber(saleDiscount));
            thisInstance.calculateTotal(true);
        });

        $('body').on('click', '.btnSaveSO', function (e) {
            // @Todo: validation
            var btnInlineSave= $('.dataTables_scrollBody table tbody').find('.btnInline.btnInlineSave.clone');
            if (btnInlineSave.length !== 0) {
                var params = {
                    message: 'Please save your product first!',
                };
                app.helper.showErrorNotification(params);
                return;
            }

            var returnUrl = $(this).data("return-url");
            $(this).prop('disabled', true);
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "saveSaleOrder",
            };

            // Get params
            if (true) {
                var amount = thisInstance.parseNumber($('.dataTables_scrollFoot td.tdTotal').text());
                var comboDiscount = thisInstance.parseNumber($('.dataTables_scrollFoot #comboDiscount').val());
                var saleDiscount = thisInstance.parseNumber($('.dataTables_scrollFoot #sale-discount').val());
                params['amount'] = amount;
                params['saleDiscount'] = saleDiscount;
                params['comboDiscount'] = comboDiscount;
                params['comboPercent'] = comboDiscount>0? Math.round(comboDiscount*100/(amount+comboDiscount+saleDiscount)) : 0;
                params['salePercent'] = saleDiscount>0? Math.round(saleDiscount*100/(amount+comboDiscount+saleDiscount)) : 0;
                params['leadId'] = $('#hfRecordId').val();
                params['leadIdUpdate'] = $('#hfRecordIdUpdate').val();
                var paymentMethod = $('.select-payment option:selected').val();// $('.payment-method').find('[name=payment]:checked').val();
                if (!paymentMethod) {
                    paymentMethod = 1;
                }
                params['paymentMethod'] = paymentMethod;
                var products = {};
                var rows = $('#tblMailList0 tbody tr');
                var total = rows.length;
                
                if($("#leadStatus").val()==2){
                    if(!$('#txtDestinationAddress').val()||!$('.cbbProvinces:nth-child(2)').val()||!$('.cbbTowns:nth-child(2)').val()||!$('.cbbDistricts:nth-child(2)').val()|| $("#show_neighborhood").val() == 1 && !$('.cbbNeighborhoods:nth-child(2)').val()){
                        var params = {
                            message:app.vtranslate('LBL_INPUT_ADDRESS'),
                        };
                        app.helper.showErrorNotification(params);
                        $(this).prop('disabled', false);
                        return;
                    }
                }
                for (var i = 0; i < total; i++) {
                    var row = $(rows[i]);
                    var prodId = row.attr('data-id');
                    if (prodId) {
                        var typeTd = $(row.find('td')[typeCol]);
                        var prodType = typeTd.attr('data-oldvalue');
                        var quantityTd = $(row.find('td')[qtyCol]);
                        var quantity = quantityTd.attr('data-oldvalue');
                        if (!quantity) {
                            quantity = quantityTd.text();
                        }
                        if (!prodType) {
                            prodType = productTypeRevert[typeTd.text()];
                        }
                        if (!quantity) {
                            continue;
                        }
                        if (!prodType) {
                            continue;
                        }
                        var priceTd = $(row.find('td')[priceCol]);
                        var priceIndex = priceTd.attr('data-priceIndex');
                        if (!priceIndex) {
                            priceIndex = priceTd.find('span').attr('data-priceindex');
                        }
                        quantity = thisInstance.parseNumber(quantity);
                        products[prodId.toString()] = {
                            priceIndex: priceIndex,
                            quantity: quantity,
                            prodType: prodType,
                        };
                    }
                }
                statusLeadText = params['leadStatus'] = $("#leadStatusName").val();//$('.lead-status-selected').text();
                console.log(statusLeadText);
                reason = '';
                if (statusLeadText == 'callback') {
                    reason = $("#txtCallbackText").val();
                } else if (statusLeadText == 'Reject'){
                    reason = $(".reason").val();
                } else if (statusLeadText == 'uncalled') {
                    reason = $(".reasonUncalled").val();
                } else if (statusLeadText == 'trash') {
                    reason = $(".reasonTrash").val();
                }
                params['products'] = products;
                params['name'] = $('#txtFullname').val();
                params['phone'] = $('#txtPhoneNumber').find(":selected").text();
                //$("#txtPhoneNumber").find(":selected").data("type");
                params['address'] = $('#txtDestinationAddress').val(); // @Todo: not cleared
                params['agcCode'] = ''; // @Todo: not cleared
                params['agcId'] = ''; // @Todo: not cleared
                //params['amount'] = amount;
                params['attribute'] = ''; // @Todo: not cleared
                params['calledby'] = 0; // @Todo: not cleared
                params['ccCode'] = ''; // @Todo: not cleared
                params['clGroup'] = 0; // @Todo: not cleared
                params['comment'] = $('#txtComment').val();
                params['cpId'] = $('#hfCampaignId').val();
                params['dayCall'] = 0; // @Todo: not cleared
                params['district'] = $("select.cbbDistricts").val();
                params['province'] = $("select.cbbProvinces").val();
                params['subdistrict'] = $("select.cbbTowns").val();
                params['neighborhood'] = $("select.cbbNeighborhoods").val();
                params['postalCode'] = $("select.cbbZipcode").val();
                params['userDefin05'] = statusLeadText + ': ' + reason;
                params['customerEmail'] =$('#customerEmail').val();
                params['customerAge'] = $('#customerAge').val();
                params['leadStatus'] = $("#leadStatus").val();
                console.log(params['leadStatus']);
                params['SOtype'] = $('#SOtype').val();

            }
            if(params['name'] == '' || params['phone'] == '')
            {
                var params = {
                    message:"Name or Phone aren't allow empty",
                };
                app.helper.showErrorNotification(params);
                $(this).prop('disabled', false);
                return;
            }
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            window.location.href = returnUrl;
                        } else {
                            var params = {
                                message: data.message,
                            };
                            app.helper.showErrorNotification(params);
                            $(this).prop('disabled', false);
                        }
                    } else {
                        app.helper.hideProgress();
                        $(this).prop('disabled', false);
                    }
                }
            );

        });

        $('body').on('change', '.reasonUncalled', function () {
            $("#leadStatus").val($(this).children("option:selected").data('id'));
        });

        $('body').on('click', '.form-check-input', function (e) {
            if ($('#callback_avalible').is(':checked')) {
                $('.callback_name_new').attr('disabled', 'disabled');
                $('.callback_phone_new').attr('disabled', 'disabled');
            } else {
                $('.callback_name_new').removeAttr("disabled");
                $('.callback_phone_new').removeAttr("disabled");
            }
            if ($('#callback_select_date_time').is(':checked')) {
                $('.callback_select_hour').attr('disabled', 'disabled');
                $('.callback_select_minute').attr('disabled', 'disabled');
                $('#txtCallbackDate').removeAttr('disabled');
                $('#txtCallbackTime').removeAttr('disabled');
            } if ($('#callback_time_period').is(':checked')) {
                $('#txtCallbackDate').attr('disabled', 'disabled');
                $('#txtCallbackTime').attr('disabled', 'disabled');
                $('.callback_select_hour').removeAttr('disabled');
                $('.callback_select_minute').removeAttr('disabled');
            }
        });

        $('#txtAddress').change(function () {
            updateDestinationAddress();
        });
        // $('.cbbProvinces').change(function(){
        //     updateDestinationAddress();
        // });
        // $('.cbbDistricts').change(function(){
        //     updateDestinationAddress();
        // });
        $(".formatNumber").on("keyup", function(event ) {
            // When user select text in the document, also abort.
            var selection = window.getSelection().toString();
            if (selection !== '') {
                return;
            }
            // When the arrow keys are pressed, abort.
            if ($.inArray(event.keyCode, [38, 40, 37, 39]) !== -1) {
                return;
            }
            var $this = $(this);
            // Get the value.
            var input = $this.val();
            var input = input.replace(/[\D\s\._\ - ] +/g, "");
            input = input?thisInstance.parseNumber(input):0;
            $this.val(function () {
                return (input === 0)?0:input.toLocaleString("en-US");
            });
        });
        function updateDestinationAddress() {
            destinationAddress = [];
            address = $('#txtAddress').val();
            province = $('.cbbProvinces option:selected').text();
            district = $('.cbbDistricts option:selected').text();
            town = $('.cbbTowns option:selected').text();
            neighborhood = $('.cbbNeighborhoods option:selected').text();

            if (address) {
                destinationAddress.push(address);
            }
            if ($("#show_neighborhood").val() == 1 && neighborhood) {
                destinationAddress.push(neighborhood);
            }
            if (town) {
                destinationAddress.push(town);
            }
            if (district) {
                destinationAddress.push(district);
            }
            if (province) {
                destinationAddress.push(province);
            }
            //address = $('#txtAddress').val() + ', ' + province + ', ' + district + ', ' + town;

            $('#txtDestinationAddress').val(destinationAddress.join(', '));
        }
    },

    getRelatedZipcode: function (postalCode) {
        var params = {
            module: app.getModuleName(),
            action: "ActionAjax",
            mode: "getRelatedZipcode",
            postalCode: postalCode,
        };
        app.helper.showProgress();
        app.request.post({ 'data': params }).then(
            function (err, data) {
                app.helper.hideProgress();
                if (err === null) {
                    if (data.success) {
                        var cbbZipcode = $('select.cbbZipcode');
                        cbbZipcode.html('<option></option>');
                        var zipcodes = data.zipcodes;
                        if (zipcodes) {
                            var total = zipcodes.length;
                            for (var i = 0; i < total; i++) {
                                let z = zipcodes[i];
                                let zipcodeDescription = z.postalCode + ' [' + z.neighborhoodName + ', ' + z.subdistrictName + ', ' + z.districtName + ', ' + z.provinceName + ']';
                                let option = '<option value=' + z.postalCodeId
                                    + ' data-province_id=' + z.provinceId
                                    + ' data-district_id=' + z.districtId
                                    + ' data-subdistrict_id=' + z.subdistrictId
                                    + ' data-neighborhood_id=' + z.neighborhoodId
                                    + ' data-postalcode=' + z.postalCode +'>'
                                        + zipcodeDescription
                                    + '</option>';
                                cbbZipcode.append(option);
                            }
                        }
                        cbbZipcode.trigger('change.select2');
                        cbbZipcode.select2('open');
                        cbbZipcode.select2("search", postalCode);
                    } else {
                        var params = {
                            message: data.message,
                        };
                        app.helper.showErrorNotification(params);
                    }
                } else {
                    app.helper.hideProgress();
                }
            }
        );
    },

    changeNeighborhood: function (nbh_id, selectedId = null) {
        var params = {
            module: app.getModuleName(),
            action: "ActionAjax",
            mode: "changeNeighborhood",
            nbh_id: nbh_id,
        };
        app.helper.showProgress();
        app.request.post({ 'data': params }).then(
            function (err, data) {
                app.helper.hideProgress();
                if (err === null) {
                    if (data.success) {
                        var cbbZipcode = $('select.cbbZipcode');
                        cbbZipcode.html('<option></option>');
                        var z = data.zipcode[0];
                        if (z) {
                            let zipcodeDescription = z.postalCode + ' [' + z.neighborhoodName + ', ' + z.subdistrictName + ', ' + z.districtName + ', ' + z.provinceName + ']';
                            let option = '<option value=' + z.postalCodeId + ' selected'
                                + ' data-province_id=' + z.provinceId
                                + ' data-district_id=' + z.districtId
                                + ' data-subdistrict_id=' + z.subdistrictId
                                + ' data-neighborhood_id=' + z.neighborhoodId
                                + ' data-postalcode=' + z.postalCode +'>'
                                    + zipcodeDescription
                                + '</option>';
                            cbbZipcode.append(option);
                            cbbZipcode.trigger('change.select2');
                        }
                    } else {
                        var params = {
                            message: data.message,
                        };
                        app.helper.showErrorNotification(params);
                    }
                } else {
                    app.helper.hideProgress();
                }
            }
        );
    },

    changeTown: function (town_id, selectedId = null) {
        var params = {
            module: app.getModuleName(),
            action: "ActionAjax",
            mode: "changeTown",
            town_id: town_id,
        };
        app.helper.showProgress();
        app.request.post({ 'data': params }).then(
            function (err, data) {
                app.helper.hideProgress();
                if (err === null) {
                    if (data.success) {
                        var cbbNeighborhoods = $('select.cbbNeighborhoods');
                        cbbNeighborhoods.html('<option></option>');
                        var neighborhoods = data.neighborhoods;
                        var total = neighborhoods.length;
                        for (var i = 0; i < total; i++) {
                            if(selectedId == neighborhoods[i].id) {
                                //cbbNeighborhoods.append(new Option(neighborhoods[i].name, neighborhoods[i].id, false, true));
                                cbbNeighborhoods.append(`<option value="${neighborhoods[i].id}" data-status="${neighborhoods[i].status}" data-statusname="${neighborhoods[i].statusName}" selected>${neighborhoods[i].name}</option>`)
                            }else {
                                //cbbNeighborhoods.append(new Option(neighborhoods[i].name, neighborhoods[i].id));
                                cbbNeighborhoods.append(`<option value="${neighborhoods[i].id}" data-status="${neighborhoods[i].status}" data-statusname="${neighborhoods[i].statusName}">${neighborhoods[i].name}</option>`)
                            }

                        }
                        cbbNeighborhoods.trigger("liszt:updated");
                        cbbNeighborhoods.trigger('change.select2');
                    } else {
                        var params = {
                            message: data.message,
                        };
                        app.helper.showErrorNotification(params);
                    }
                } else {
                    app.helper.hideProgress();
                }
            }
        );
    },

    changeDistrict: function (district_id, selectedId = null) {
        var params = {
            module: app.getModuleName(),
            action: "ActionAjax",
            mode: "changeDistrict",
            district_id: district_id,
        };
        app.helper.showProgress();
        app.request.post({ 'data': params }).then(
            function (err, data) {
                app.helper.hideProgress();
                if (err === null) {
                    if (data.success) {
                        var cbbTowns = $('select.cbbTowns');
                        cbbTowns.html('<option></option>');
                        var towns = data.towns;
                        var total = towns.length;
                        for (var i = 0; i < total; i++) {
                            if(selectedId == towns[i].sdtId) {
                                //cbbTowns.append(new Option(towns[i].name, towns[i].sdtId, false, true));
                                cbbTowns.append(`<option value="${towns[i].sdtId}" data-status="${towns[i].status}" data-statusname="${towns[i].statusName}" selected>${towns[i].name}</option>`)
                            }else {
                                //cbbTowns.append(new Option(towns[i].name, towns[i].sdtId));
                                cbbTowns.append(`<option value="${towns[i].sdtId}" data-status="${towns[i].status}" data-statusname="${towns[i].statusName}">${towns[i].name}</option>`)
                            }

                        }
                        cbbTowns.trigger("liszt:updated");
                        cbbTowns.trigger('change.select2');
                    } else {
                        var params = {
                            message: data.message,
                        };
                        app.helper.showErrorNotification(params);
                    }
                } else {
                    app.helper.hideProgress();
                }
            }
        );
    },
    changeProvince: function(province_id, selectedId = null) {
        var params = {
            module: app.getModuleName(),
            action: "ActionAjax",
            mode: "changeProvince",
            province_id: province_id,
        };
        $('select.cbbTowns option').remove();
        app.helper.showProgress();
        app.request.post({ 'data': params }).then(
            function (err, data) {
                app.helper.hideProgress();
                if (err === null) {
                    if (data.success) {
                        var cbbDistricts = $('select.cbbDistricts');
                        cbbDistricts.html('<option></option>');
                        var districts = data.districts;
                        var total = districts.length;
                        for (var i = 0; i < total; i++) {
                            if(districts[i].dtId == selectedId) {
                                //cbbDistricts.append(new Option(districts[i].name, districts[i].dtId, false, true));
                                cbbDistricts.append(`<option value="${districts[i].dtId}" data-status="${districts[i].status}" data-statusname="${districts[i].statusName}" selected>${districts[i].name}</option>`)
                            }else {
                                //cbbDistricts.append(new Option(districts[i].name, districts[i].dtId));
                                cbbDistricts.append(`<option value="${districts[i].dtId}" data-status="${districts[i].status}" data-statusname="${districts[i].statusName}">${districts[i].name}</option>`)
                            }

                        }
                        cbbDistricts.trigger("liszt:updated");
                        cbbDistricts.trigger('change.select2');
                    } else {
                        var params = {
                            message: data.message,
                        };
                        app.helper.showErrorNotification(params);
                    }
                } else {
                    app.helper.hideProgress();
                }
            }
        );
    },
    updateDestinationAddress : function() {
        destinationAddress = [];
        address = $('#txtAddress').val()
        province = $('.cbbProvinces option:selected').text();
        district = $('.cbbDistricts option:selected').text();
        town = $('.cbbTowns option:selected').text();
        neighborhood = $('.cbbNeighborhoods option:selected').text();

        if (address) {
            destinationAddress.push(address);
        }
        if ($("#show_neighborhood").val() == 1 && neighborhood) {
            destinationAddress.push(neighborhood);
        }
        if (town) {
            destinationAddress.push(town);
        }
        if (district) {
            destinationAddress.push(district);
        }
        if (province) {
            destinationAddress.push(province);
        }
        //address = $('#txtAddress').val() + ', ' + province + ', ' + district + ', ' + town;

        $('#txtDestinationAddress').val(destinationAddress.join(', '));
    },
    calculateTotal: function (percentChange = false) {
        var total = 0;
        var totalComboDiscount = 0;
        var saleDiscount = $('.dataTables_scrollFoot #sale-discount').val();
        var rows = $('#tblMailList0').find('tbody').find('tr');
        for (var i = 0; i < rows.length; i++) {
            var comboDiscount = this.parseNumber($(rows[i]).attr('data-discount'));
            var tds = $(rows[i]).find('td');
            var qty = this.parseNumber($(tds[qtyCol]).attr('data-oldvalue'));
            if (!qty) {
                qty = this.parseNumber($(tds[qtyCol]).text());
            }
            if (!qty) {
                qty = this.parseNumber($(tds[qtyCol]).find('input').val());
            }
            var t = $(tds[totalCol]).text();
            if (!t) {
                t = 0;
            }
            t = this.parseNumber(t);
            comboDiscount = this.parseNumber(comboDiscount);
            totalComboDiscount += (comboDiscount*qty);
            total += t;

        }
        total -= this.parseNumber(totalComboDiscount);
        $('.dataTables_scrollFootInner table #totalBeforeSaleDiscount').html(total);
        // var saleDiscountPercent = (this.parseNumber(saleDiscount) / total * 100).toFixed(2);
        var saleDiscountPercent = Math.round((this.parseNumber(saleDiscount) / total * 100) * 100) / 100;
        total -= this.parseNumber(saleDiscount);
        total = this.formatNumber(total);
        $('.dataTables_scrollFootInner table .tdTotal').html(total);
        $('.dataTables_scrollFootInner table #comboDiscount').val(this.formatNumber(totalComboDiscount));
        $('.dataTables_scrollFootInner table #sale-discount').val(this.formatNumber(this.parseNumber(saleDiscount)));
        if (percentChange === false) {
            $('.dataTables_scrollFootInner table #sale-discount-percent').val(this.formatNumber(this.parseNumber(saleDiscountPercent)));
        }
    },
    calculateOnNotEditRow: function (parent) {
        var quantity = parent.find('input.txtInlineQuantity').val();
        quantity = parseFloat(quantity);
        if (!quantity ) {
            quantity = this.parseNumber($(parent.find('td')[qtyCol]).html());
        }
        quantity = parseFloat(quantity);
        if (!quantity) {
            quantity = 0;
        }
        var price = this.parseNumber(parent.find('select.txtInlinePrice').val());
        if (!price || price==0) {
            price = this.parseNumber($(parent.find('td')[priceCol]).html());
        }
        if (!price || price==0) {
            price = this.parseNumber($(parent.find('td')[priceCol]).find('span').html());
        }
        if (!price) {
            price = 0;
        }
        var total = quantity * price;
        $(parent.find('td')[totalCol]).html(this.formatNumber(total));

        this.calculateTotal();
    },
    getRandomUnique: function () {
        var d = new Date();
        return d.getMinutes() + '' + d.getSeconds();
    },

    formatNumber: function (number) {
        var groupingseparator = $('body').data('user-groupingseparator');
        var decimalseparator = $('body').data('user-decimalseparator');
        number = number.toString();
        number = number.split('.');
        var number1 = number[0];
        var number2 = number[1];
        var l = number1.length;
        if (l > 3) {
            number1 = this.formatNumber(number1.substr(0, l - 3)) + groupingseparator + number1.substr(l - 3, 3);
        }
        if (number2) {
            number2 = $.trim(number2);
            if (number2) {
                number1 += decimalseparator + number2;
            }
        }
        return number1;
    },

    parseNumber: function (number) {
        if (!number) {
            return 0;
        }
        number = number.toString();
        number = number.trim();
        var groupingseparator = $('body').data('user-groupingseparator');
        while (number.indexOf(groupingseparator) >= 0) {
            number = number.replace(groupingseparator, '');
        }
        var decimalseparator = $('body').data('user-decimalseparator');
        number = number.replace(decimalseparator, '.');
        return parseFloat(number);
    },

    initializePaginationEvents: function () {
        return true;
    },

    registerEvents: function () {
        this._super();

        this.registerDatatable();
        this.registerCustomEvents();
        var cb_province = $('#cb_province');
        var cb_district = $('#cb_district');
        var cb_subdistrict = $('#cb_subdistrict');
        var cb_neighborhood = $('#cb_neighborhood');
        var cb_zipcode = $('#cb_zipcode');

        console.log('cb_province: ' + cb_province.val());
        this.changeProvince(cb_province.val(), cb_district.val());
        this.changeDistrict(cb_district.val(), cb_subdistrict.val());
        this.changeTown(cb_subdistrict.val(), cb_neighborhood.val());
        this.changeNeighborhood(cb_neighborhood.val(), cb_zipcode.val());
    }
});

$(document).ready(function () {

});
