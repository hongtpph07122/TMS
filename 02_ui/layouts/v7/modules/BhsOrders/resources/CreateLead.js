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
Vtiger_Edit_Js("BhsOrders_CreateLead_Js", {}, {

    registerDatatable: function () {
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

        $('body').on('click', '#btnAddManual ', function (e) {
            // $('#btnImportExcel').attr('disabled', 'disabled');
            $('.box-importExcel').addClass('box-disabled');
            $('.box-addManual').removeClass('box-disabled');
        });

        $('body').on('click', '#btnImportExcel ', function (e) {
            // $('#btnImportExcel').attr('disabled', 'disabled');
            $('.box-addManual').addClass('box-disabled');
            $('.box-importExcel').removeClass('box-disabled');
        });
        $('body').on('change', '.SELECTCAMPAIGN', function (e) {
            $('.SELECTCALLINGLIST').prop('selectedIndex', 0);
            $('.SELECTCALLINGLIST').trigger("liszt:updated");
            $('.SELECTCALLINGLIST').trigger('change.select2');
            thisInstance.changeCampaign($(this).val());
        });
        $('body').on('click', '#btnImport', function (e) {
            var params = {
                module: 'BhsOrders',
                action: "ActionAjax",
                mode: "getToken"
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, token) {
                    //app.helper.hideProgress();
                    var dd = document.getElementById("form-import-excel");
                    var formData = new FormData(dd);
                    var cpId = $('#SELECTCAMPAIGN').select2().val();
                    var clId = $('#SELECTCALLINGLIST').select2().val();
                    if(!cpId || ! clId) {
                        app.helper.showErrorNotification({ 'message' : 'Please select Campaign and calling list'});
                        app.helper.hideProgress();
                        return;
                    }
                    var endpoint = $('#API_ENDPOINT').val();
                    $.ajax({
                        url: endpoint + "lead/excel/upload/" + cpId + '/' + clId,
                        headers: {
                            'Authorization':'Bearer ' + token,
                        },
                        type: "POST",
                        data: formData,
                        contentType:false,
                        cache: false,
                        processData:false,
                        success:function (res) {
                            console.log('res.data: ' + res.data);
                            if(res.code != 200) {
                                var message = res.data,
                                    messageOutput = '',
                                    messageArr = message.split(/[\n\r]/g);
                                messageArr.pop();
                                for (var i in messageArr) {
                                    messageOutput += '<p>' + messageArr[i] + '</p>';
                                }
                                app.helper.showErrorNotification({ 'message' : messageOutput });
                            }else {
                                app.helper.showSuccessNotification({ 'message' : res.message});
                            }
                            app.helper.hideProgress();
                        },
                        error: function(res) {
                            app.helper.hideProgress();
                            app.helper.showErrorNotification({
                                message: app.vtranslate(res.responseJSON.message)
                            });
                        }
                    });
                }
            );
            
        });
        //create order
        var thisInstance = this;
        var productsAdd = [];
        var phone = $('#txtPhoneNumber').val();
        String.prototype.capitalize = function () {
            return this.charAt(0).toUpperCase() + this.slice(1);
        }
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
           $('#depositAmount').text(thisInstance.formatNumber(this.value));
           total=thisInstance.parseNumber($('.tdTotal').text())-thisInstance.parseNumber(this.value);
           $('#codAmount').text(thisInstance.formatNumber(total));
        })
        $('body').on('change', 'select.cbbInlineProduct', function () {
            var parent = $(this).closest('tr');
            var cbbInlinePrice = parent.find('select.txtInlinePrice');
            cbbInlinePrice.html('');
            var productId = $(this).val();
            var prices = $(this).find('option:selected').attr('data-price');
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
        });

        $('body').on('click', '.btnRefreshStock', function () {
            // iTables[1].ajax.reload(() => {
            //     $('#tblMailList1 .dataTables_empty').hide();
            //     productsAdd.forEach((pro) => {
            //         $('#tblMailList1').find('tbody').append(pro);
            //     });
            // });

        });

        $('body').on('click', '.btnSetLeadStatus', function (e) {
            var leadStatusText = $(e.target).data('label') + ': ' + $('#selectSetRejected').val();
            $('.lead-status-selected').text(leadStatusText);

        });
        $('body').on('click', '.btnInlineRemove', function (e) {
            var thisBtn = $(this);
            app.helper.showConfirmationBox({ message: app.vtranslate('Are you sure want to remove?') }).then(function () {
                var parent = thisBtn.closest('tr');
                prodId = $(parent.find('td')[productIdCol]).text();
                quantity = thisInstance.parseNumber($(parent.find('td')[qtyCol]).text());
                console.log('quantity: ' + quantity);
                var amount = thisInstance.parseNumber($('.tdTotal').text());
                console.log('amount: ' + amount);
                amount = amount - quantity;
                console.log('amount: ' + amount);
                $('.tdTotal').text(thisInstance.formatNumber(amount));
                thisBtn.closest('tr').remove();
                trStocks = $('#tblMailList1').find('tr');
                for (i = 0; i < trStocks.length; i++) {
                    tr = $(trStocks[i]);
                    id = $(tr.find('td')[0]).text();
                    if (id == prodId) {
                        tr.remove();
                        // Remove product stock
                        productsAdd.forEach((row, index) => {
                            id = $(row.find('td')[0]).text();
                            if (id == prodId) {
                                productsAdd.splice(index, 1);
                            }
                        });
                    }
                };
                trProduct = $('#tblMailList5').find('tr');
                for (i = 0; i < trProduct.length; i++) {
                    tr = $(trProduct[i]);
                    id = $(tr.find('td')[0]).text();
                    if (id == prodId) {
                        tr.remove();
                    }
                };
                $('#txtDeposit').change()
                if (false) { // @Todo
                    var params = {
                        module: 'BhsOrderProcessing',
                        action: "ActionAjax",
                        mode: "deleteCampaign",
                        id: $(this).data('id'),
                    };
                    app.helper.showProgress();
                    app.request.post({ 'data': params }).then(
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
                }
                thisInstance.calculateTotal();
            });
        });

        $('body').on('click', '.btnInlineEdit', function (e) {
            var productId = $(this).closest('tr').attr('data-id');
            if (thisInstance.checkIfEdittingProduct()) {
                return true;
            }

            var parent = $(this).closest('tr');
            // Remove class if yes
            parent.find('.btnInlineEdit').removeClass('clone');
            parent.find('.btnInlineView').removeClass('clone');
            parent.find('.btnInlineRemove').removeClass('clone');
            parent.find('.btnInlineSave').removeClass('clone');
            parent.find('.btnInlineCancel').removeClass('clone');

            var tds = parent.find('td');
            oldType = $(parent.find('td')[typeCol]).text();
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
                $(this).closest('tr').find('select.cbbInlineProduct').val(productId);
                vtUtils.showSelect2ElementView(productTd.find('select.cbbInlineProduct'));
            }

            // hien tai chua can chon Stock
            // if (true) {
            //     var stockIdx = 2;
            //     var stockTd = $(tds[stockIdx]);
            //     var stock = stockTd.html();
            //     stockTd.attr('data-oldValue', stock);
            //     html = '<select class="cbbInlineStock inputElement select2">';
            //     html += $(this).closest('table').find('.cloneRow').find('select.cbbInlineStock').html();
            //     html += '</select>';
            //     stockTd.html(html);
            //     var cbbInlineStock = stockTd.find('.cbbInlineStock');
            //     vtUtils.showSelect2ElementView(cbbInlineStock);
            // }

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
                var price = priceTd.html();
                priceTd.attr('data-oldValue', price);
                var _price = thisInstance.parseNumber(price);
                html = '<select class="txtInlinePrice inputElement select2">';
                //html += $(this).closest('table').find('.cloneRow').find('select.txtInlinePrice').html();
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

            action = '<a class="btnInline btnInlineSave clone"><i class="fa fa-check"></i></a><a class="btnInline btnInlineCancel clone"><i class="fa fa-trash"></i></a><a class="btnInline btnInlineEdit clone" style="display: none;"><i class="fa fa-pencil"></i></a><a class="btnInline btnInlineView clone" style="display: none;"><i class="fa fa-eye"></i></a><a class="btnInline btnInlineRemove clone" style="display: none;"><i class="fa fa-trash"></i></a>';
            $(tds[actionCol]).html(action);
            return;
            var params = {
                module: 'BhsOrderProcessing',
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

            // Hien tai chua can Stock
            // if (true) {
            //     var stockIdx = 2;
            //     var stockTd = $(tds[stockIdx]);
            //     var stock = stockTd.attr('data-oldValue');
            //     stockTd.html(stock);
            // }

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
            $('#txtDeposit').change()
            var parent = $(this).closest('tr');
            var cbbInlineProduct = parent.find('select.cbbInlineProduct');
            var cbbInlineType = parent.find('select.cbbInlineType option:selected');
            var old_product_id = parent.attr('data-id');
            var productId = cbbInlineProduct.val();

            parent.attr('data-id', productId);
            $(parent.find('td')[productIdCol]).html(productId);
            var productName = cbbInlineProduct.find('option:selected').html();
            var cbbInlineStock = parent.find('select.cbbInlineStock option:selected');
            var type =cbbInlineType.text();
            var stock = cbbInlineStock.text();
            if (stock == 'Select stock') {
                stock = '';
            }
            // Validate
            if (!productId) {
                var params = {
                    message: 'Please select product and stock',
                };
                app.helper.showErrorNotification(params);
                return;
            }
            var txtInlineQuantity = parent.find('input.txtInlineQuantity');
            var quantity = txtInlineQuantity.val();
            if (!quantity) {
                var params = {
                    message: 'Please input quantity',
                };
                app.helper.showErrorNotification(params);
                return;
            }
            old_product = $($(parent.find('td')[productNameCol]).attr('data-oldvalue')).text();
            if (old_product != '') {
                checkExistProduct(old_product, 5, 1);
            }

            if (old_product_id != '') {
                checkExistProduct(old_product_id, 1, 0);
            }

            $(parent.find('td')[productNameCol]).attr('data-oldvalue', '<span class="text-info">' + productName + '</span>');
            //$(parent.find('td')[1]).html('<span class="text-info">' + productName + '</span>');
            // $(parent.find('td')[2]).attr('data-oldvalue', stock);

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
                //console.log(ck);
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
                        data.forEach(st => {
                            if (st.prodId == productId) {
                                var row = $('#tblMailList1 tr').eq(1).clone();
                                stock_parent = $(row);
                                if (stock_parent.text() == 'No data found in the server' || stock_parent.text() == 'No data available in table') {
                                    $('#tblMailList1 tr').eq(1).remove();
                                    strTr = '<td class="hidden"></td><td></td><td></td><td></td><td></td>';
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

                        var row = $('#tblMailList5 tr').eq(1).clone();
                        pro_parent = $(row);
                        getProductInfo(productId, function (prod) {
                            // $(pro_parent.find('td')[0]).html(prod.prodId);
                            const prod_name = '<span class="text-info">' + prod.name + '</span>';
                            $(pro_parent.find('td')[0]).html(prod_name);
                            $(pro_parent.find('td')[1]).html(prod.dscr);
                        });


                        // $('#tblMailList5').find('tbody').append(row);
                    });
                }
            });
        });
        //
        $(document).on('click', '#tblMailList0 tbody tr td:nth-child(3)', function () {
            var productId = $(this).closest("tr").find(".hidden").html();
            var row = $('#tblMailList5 tr').eq(1).clone();
            var row_agent = $('#tblMailList6 tr').eq(1).clone();
            pro_parent = $(row);
            pro_agent = $(row_agent);
            getProductInfo(productId, function (prod) {
                const prod_name = '<span class="text-info">' + prod.name + '</span>';
                $(pro_parent.find('td')[0]).html(prod_name);
                $(pro_parent.find('td')[1]).html(prod.dscr);
                //
                $(pro_agent.find('td')[0]).html(prod_name);
                $(pro_agent.find('td')[1]).html(prod.dscrForAgent);
            });
            $('#tblMailList5 tr').eq(1).replaceWith(row);
            $('#tblMailList6 tr').eq(1).replaceWith(row_agent);
        })
        //
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

                if (name == productName) {
                    if (tblIndex == 1) {
                        productsAdd.forEach((row, index) => {
                            id = $(row.find('td')[0]).text();
                            if (id == productName) {
                                productsAdd.splice(index, 1);
                            }
                        });
                    }
                    tr.remove();
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
                module: 'BhsOrderProcessing',
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
            $('#tblMailList0').find('tbody').append(row);
            vtUtils.showSelect2ElementView($('#' + productElementId));
            vtUtils.showSelect2ElementView($('#' + stockElementId));
            $($.fn.dataTable.tables(true)).DataTable().columns.adjust();
            if($('#tblMailList0 tbody tr:first-child th').html()=="No data found in the server"){
                $('#tblMailList0 tbody tr:first-child ').remove();
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
                var phoneNumber = $('#txtPhoneNumber').val();
                if (!phoneNumber) {
                    return true;
                }
                var params = {
                    module: 'BhsOrderProcessing',
                    action: "ActionAjax",
                    mode: "callCustomer",
                    phone: phoneNumber,
                };

            } else if ($(this).attr('data-source') == 'hangup') {
                $(this).attr('data-source', 'call');
                icon_link = 'layouts/v7/resources/Images/icon/icon-phone.png';
                var params = {
                    module: 'BhsOrderProcessing',
                    action: "ActionAjax",
                    mode: "hangupToCustomer",
                };

            }

            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            $('#btnClickToDial img').attr('src', icon_link);
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
        });
        $('body').on('change', '.cbbProvinces', function (e) {
            $('.cbbTowns').prop('selectedIndex', 0);
            $('.cbbTowns').trigger("liszt:updated");
            $('.cbbTowns').trigger('change.select2');
            updateDestinationAddress();
            thisInstance.changeProvince($(this).val());
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

        $('body').on('click', '.btnSaveLead', function (e) {
            // @Todo: validation


            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "saveLead",
            };

            // Get params
            if (true) {
                var amount = thisInstance.parseNumber($('.tdTotal').text());
                params['amount'] = amount;
                //params['leadId'] = $('#hfRecordId').val();
                params['leadIdUpdate'] = $('#hfRecordIdUpdate').val();
                var paymentMethod = $(".select-payment").val();// $('.payment-method').find('[name=payment]:checked').val();
                if (!paymentMethod) {
                    paymentMethod = 1;
                }
                params['paymentMethod'] = paymentMethod;
                var products = {};
                var rows = $('#tblMailList0 tbody tr');
                var total = rows.length;
                if(!$('#txtFullname').val()){
                    var params = {
                        message: app.vtranslate('LBL_INPUT_NAME'),
                    };
                    app.helper.showErrorNotification(params);
                    return;
                }
                if(!$('#txtPhoneNumber').val()){
                    var params = {
                        message:app.vtranslate('LBL_INPUT_PHONE'),
                    };
                    app.helper.showErrorNotification(params);
                    return;
                }
                for (var i = 0; i < total; i++) {
                    var row = $(rows[i])
                    var prodId = row.attr('data-id');
                    if (prodId) {
                        var priceTd = $(row.find('td')[priceCol]);
                        var prodPrice = priceTd.text();
                        if (!prodPrice) {
                            prodPrice = 0;
                        }
                        params['prodId'] = prodId.toString();
                        params['price'] = prodPrice;
                        params['prodName'] = $(row.find('td')[productNameCol]).text();
                    }
                }
                params['products'] = products;
                params['name'] = $('#txtFullname').val();
                params['phone'] = $('#txtPhoneNumber').val();
                params['address'] = $('#txtDestinationAddress').val(); // @Todo: not cleared
                params['callinglistId'] = $('select.SELECTCALLINGLIST').val(); 
                params['comment'] = $('#txtComment').val();
                params['cpId'] = $('select.SELECTCAMPAIGN').val();
                params['district'] = $("select.cbbDistricts").val();
                params['province'] = $("select.cbbProvinces").val();
                params['subdistrict'] = $("select.cbbTowns").val();
                params['neighborhood'] = $("select.cbbNeighborhoods").val();
                params['postalCode'] = $("select.cbbZipcode").val();
                params['leadType'] =$('#addNew_leadType').val();
                params['leadStatus'] = $("#leadStatus").val();
                params['customerEmail'] =$('#customerEmail').val();
                params['customerAge'] = $('#customerAge').val();
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
                            var url = 'index.php?module=BhsOrders&view=List&app=BHS_ORDERS'; //$('.btnBackSO').attr('href');
                            window.location.href = url;
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

        $('#txtAddress').change(function () {
            updateDestinationAddress();
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
            module: 'BhsOrderProcessing',
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
            module: 'BhsOrderProcessing',
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
            module: 'BhsOrderProcessing',
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
            module: 'BhsOrderProcessing',
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
            module: 'BhsOrderProcessing',
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
    changeCampaign: function(campaign_id, selectedId = null) {
        var params = {
            module: app.getModuleName(),
            action: "ActionAjax",
            mode: "getCallingList",
            id: campaign_id,
        };
        $('select.SELECTCALLINGLIST option').remove();
        app.helper.showProgress();
        app.request.post({ 'data': params }).then(
            function (err, data) {
                app.helper.hideProgress();
                if (err === null) {
                        var callList = $('select.SELECTCALLINGLIST');
                        callList.html('<option></option>');
                        var total = data.length;
                        for (var i = 0; i < total; i++) {
                            callList.append(new Option(data[i].clName, data[i].callinglistId));
                        }
                        callList.trigger("liszt:updated");
                        callList.trigger('change.select2');
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
    calculateTotal: function () {
        var total = 0;
        var rows = $('#tblMailList0').find('tbody').find('tr');
        if (rows.length > 0) {
            $('#addProduct').addClass('hidden');
        } else  {
            $('#addProduct').removeClass('hidden');
        }
        for (var i = 0; i < rows.length; i++) {
            var tds = $(rows[i]).find('td');
            var t = $(tds[totalCol]).text();
            if (!t) {
                t = 0;
            }
            t = this.parseNumber(t);
            total += t;
        }
        total = this.formatNumber(total);
        $('.tdTotal').html(total);
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
            number2 = trim(number2);
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
    }
});

$(document).ready(function () {

});