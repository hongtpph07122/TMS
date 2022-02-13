/*+*************************BhsCampaign**********************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsCombo_List_Js", {}, {

    registerDatatable: function () {
        loadDataTable({
            "availableCols": 1,
        });
    },

    showPopupEdit: function (prodId) {
        var params = {
            module: app.getModuleName(),
            view: "ViewAjax",
            mode: "showEdit",
            id: prodId,
        };

        app.helper.showProgress();
        app.request.post({'data': params}).then(
            function (err, data) {
                app.helper.hideProgress();
                if (err === null) {
                    app.helper.showModal(data, {
                        'cb': function () {
                            var ckEditorInstance = new Vtiger_CkEditor_Js();
                            ckEditorInstance.loadCkEditor($('#txtDescriptionCombo'));
                            ckEditorInstance.loadCkEditor($('#txtAgentScriptCombo'));
                        }
                    });
                } else {
                }
            }
        );
    },

    registerCustomEvents: function () {
        var thisInstance = this;
        $("#datetimepicker1").on("dp.change", function (e) {
            $('#datetimepicker2').data("DateTimePicker").minDate(e.date);
        });
        $("#datetimepicker2").on("dp.change", function (e) {
            $('#datetimepicker1').data("DateTimePicker").maxDate(e.date);
        });
        $('body').on('click', '.btn-toggle', function () {
            if ($(this).attr('id')) {
                return;
            }
            var status = $(this).hasClass('active');
            if (status) {
                status = 0;
            } else {
                status = 1;
            }
            var id = $(this).attr('data-id');
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "changeStatus",
                id: id,
                status: status,
            };
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function (err, data) {
                    app.helper.hideProgress();
                }
            );
        });
        $('body').on('click', '#addProduct', function (e) {
            e.preventDefault();
            var mainProductOptionValue = 1;
            var optionalProductOptionValue = 2;
            var hasMainProduct = false;
            var rows = $('#combo-product').find('tbody').find('tr');
            $('.cloneRow td.prod-td select.cbbInlineProduct option').removeClass('hidden');
            for (var i = 0; i < rows.length; i++) {
                var $row = $(rows[i]);
                var inlineProductValue = $row.find('td.prod-td').attr('data-value');
                var inlineTypeValue = $row.find('td.type-td').attr('data-value');
                $('.cloneRow td.prod-td select.cbbInlineProduct option[value="' + inlineProductValue + '"]').addClass('hidden');
                if (inlineTypeValue == mainProductOptionValue) {
                    hasMainProduct = true;
                    $('.cloneRow td.type-td select.cbbInlineType option[value="' + optionalProductOptionValue + '"]').attr("selected", true);
                }
            }
            if (!hasMainProduct) {
                $('.cloneRow td.type-td select.cbbInlineType option[value="' + optionalProductOptionValue + '"]').removeAttr('selected');
            }
            var row = $('.cloneRow').clone();
            row.removeClass('cloneRow');
            row.removeClass('hidden');
            if ($('#combo-product tbody tr').last().hasClass('odd')) {
                row.addClass('even');
            } else {
                row.addClass('odd');
            }
            var productElementId = 'cbbInlineProduct_' + thisInstance.getRandomUnique();
            row.find('select.cbbInlineProduct').attr('id', productElementId);
            $('#combo-product').find('tbody').append(row[0]);
            vtUtils.showSelect2ElementView($('#' + productElementId));
        });
        $('body').on('click', '.btnCreateProduct', function (e) {
            thisInstance.showPopupEdit();
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
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        //app.helper.showModal(data);
                        alert('TODO: export excel...');
                    } else {
                        alert(err);
                    }
                }
            );
        });

        $('body').on('click', '.btnBhsDelete', function (e) {
            var id = $(this).attr('data-id');
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to delete?')}).then(function () {
                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "deleteCombo",
                    id: id,
                };
                app.helper.showProgress();
                app.request.post({'data': params}).then(
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
            });
        });

        $('body').on('click', '#btnSaveCombo', function (e) {
            var parent = $('#frmEditCombo');
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "saveRecord",
            };
            var dscr = CKEDITOR.instances.txtDescriptionCombo.getData();
            dscr = $.trim(dscr.replace(/[\t\n]+/g, ' '))
            var dscrForAgent = CKEDITOR.instances.txtAgentScriptCombo.getData();
            dscrForAgent = $.trim(dscrForAgent.replace(/[\t\n]+/g, ' '))
            params['comboInfo'] = {
                prodId: parent.find('#hfRecordId').val(),
                name: parent.find('#ComboName').val(),
                dscr: dscr,
                productType: 2,
                dscrForAgent: dscrForAgent,
                status: parent.find('#btnToggleStatus').hasClass('active') ? 1 : 0,
                listPrice: thisInstance.parseNumber($('#comboListPrice').text()),
                price: thisInstance.parseNumber($('#comboNetPrice').text()),
                discountCash: thisInstance.parseNumber($('#comboDiscountCash').val()),
                discountPercent: thisInstance.parseNumber($('#comboDiscountPercent').val()),
                unit: $('#currency').val(),
            };

            var prods = [];
            var rows = $('#combo-product tbody tr');
            var total = rows.length;
            var countMainProd = 0;
            for (var i = 0; i < total; i++) {
                var $parentTr = $(rows[i]);
                var $inlineType = $parentTr.find('td.type-td');
                var $inlineQuantity = $parentTr.find('td.quantity-td');
                var $inlinePrice = $parentTr.find('td.price-td');
                var prodId = $parentTr.find('td.prod-td').attr('data-value');
                var quantity = $inlineQuantity.attr('data-value');
                var type = $inlineType.attr('data-value');
                if (!quantity) {
                    continue;
                }
                var priceIndex = $inlinePrice.attr('data-price-index');
                quantity = thisInstance.parseNumber(quantity);
                if (type == 1) {
                    countMainProd++;
                    prods.unshift({
                        index: priceIndex,
                        prdId: prodId,
                        qty: quantity,
                    });
                } else {
                    prods.push({
                        index: priceIndex,
                        prdId: prodId,
                        qty: quantity,
                    });
                }
            }
            if (countMainProd==0) {
                app.helper.showErrorNotification({message: 'Please select One Main Product'});
                return;
            }

            if (countMainProd>1) {
                app.helper.showErrorNotification({message: 'Only one main product for combo'});
                return;
            }
            params['prods'] = prods;
            console.log(params);
            app.helper.showProgress();
            app.request.post({'data': params}).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            app.helper.hideModal();
                            reloadDataTable();
                        } else {
                            var returnparams = {
                                message: data.message,
                            };
                            app.helper.showErrorNotification(returnparams);
                        }
                    } else {
                        var returnparams = {
                            message: err,
                        };
                        app.helper.showErrorNotification(returnparams);
                    }
                }
            );
        });

        $('body').on('click', '.btnBhsUpdate', function (e) {
            thisInstance.showPopupEdit($(this).attr('data-id'));
        });
        $('body').on('click', '.btnInlineCancel', function (e) {
            $(this).closest('tr').remove();
            thisInstance.calculateTotal();
        });
        $('body').on('click', '.btnInlineSave', function (e) {
            var $parentTr = $(this).closest('tr');
            var $cbbInlineType = $parentTr.find('select.cbbInlineType');
            var $cbbInlineProduct = $parentTr.find('select.cbbInlineProduct');
            var $txtInlineQuantity = $parentTr.find('input.txtInlineQuantity');
            var $cbbInlinePrice = $parentTr.find('select.cbbInlinePrice');
            if (!$cbbInlineProduct.val() || $cbbInlineProduct.val() < 0) {
                var params = {
                    message: 'Please select product',
                };
                app.helper.showErrorNotification(params);
                return;
            }
            $cbbInlineType.closest('td').attr('data-value', $cbbInlineType.val());
            $cbbInlineType.closest('td').html($cbbInlineType.find('option:selected').text());

            $cbbInlineProduct.closest('td').attr('data-value', $cbbInlineProduct.val());
            $cbbInlineProduct.closest('td').html($cbbInlineProduct.find('option:selected').text());

            $txtInlineQuantity.closest('td').attr('data-value', $txtInlineQuantity.val());
            $txtInlineQuantity.closest('td').html($txtInlineQuantity.val());

            $cbbInlinePrice.closest('td').attr('data-value', $cbbInlinePrice.val());
            $cbbInlinePrice.closest('td').attr('data-price-index', $cbbInlinePrice[0].selectedIndex);
            $cbbInlinePrice.closest('td').html($cbbInlinePrice.find('option:selected').text());
            var total = thisInstance.parseNumber($cbbInlinePrice.find('option:selected').text()) * thisInstance.parseNumber($txtInlineQuantity.val());
            $parentTr.find('td.total-td').html(thisInstance.formatNumber(total));

            var action = '<div class="btn-group btn-group-action">\
                <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\
                <img src="layouts/v7/resources/Images/icon/icon-action.png" alt="">\
                </a>\
                <div class="dropdown-menu dropdown-menu-right">\
                <a class="btnInlineEdit"><i class="fa fa-pencil"></i> Edit</a>\
                <a class="btnInlineRemove"><i class="fa fa-trash"></i> Trash</a>\
                </div>\
            </div>';
            $parentTr.find('td.action-td').html(action);
        });
        $('body').on('click', '.btnInlineEdit', function (e) {
            var mainProductOptionValue = 1;
            var optionalProductOptionValue = 2;
            var hasMainProduct = false;
            var rows = $('#combo-product').find('tbody').find('tr');
            $('.cloneRow td.prod-td select.cbbInlineProduct option').removeClass('hidden');
            for (var i = 0; i < rows.length; i++) {
                var $row = $(rows[i]);
                var inlineProductValue = $row.find('td.prod-td').attr('data-value');
                var inlineTypeValue = $row.find('td.type-td').attr('data-value');
                $('.cloneRow td.prod-td select.cbbInlineProduct option[value="' + inlineProductValue + '"]').addClass('hidden');
                if (inlineTypeValue == mainProductOptionValue) {
                    hasMainProduct = true;
                    $('.cloneRow td.type-td select.cbbInlineType option[value="' + optionalProductOptionValue + '"]').attr("selected", true);
                }
            }
            if (!hasMainProduct) {
                $('.cloneRow td.type-td select.cbbInlineType option[value="' + optionalProductOptionValue + '"]').removeAttr('selected');
            }
            var $parentTr = $(this).closest('tr');
            var $inlineType = $parentTr.find('td.type-td');
            var $inlineProduct = $parentTr.find('td.prod-td');
            var $inlineQuantity = $parentTr.find('td.quantity-td');
            var $inlinePrice = $parentTr.find('td.price-td');
            var $inlineAction = $parentTr.find('td.action-td');

            $inlineType.html($('.cloneRow').find('td.type-td').html());
            $inlineProduct.html($('.cloneRow').find('td.prod-td').html());
            $inlineQuantity.html($('.cloneRow').find('td.quantity-td').html());
            $inlinePrice.html($('.cloneRow').find('td.price-td').html());
            $inlineAction.html($('.cloneRow').find('td.action-td').html());


            var $cbbInlineType = $parentTr.find('select.cbbInlineType');
            var $cbbInlineProduct = $parentTr.find('select.cbbInlineProduct');
            var $txtInlineQuantity = $parentTr.find('input.txtInlineQuantity');
            var $cbbInlinePrice = $parentTr.find('select.cbbInlinePrice');

            var currentPrice = $inlinePrice.attr('data-value');
            $cbbInlineType.val($inlineType.attr('data-value'));
            $cbbInlineProduct.val($inlineProduct.attr('data-value'));
            $txtInlineQuantity.val($inlineQuantity.attr('data-value'));
            $cbbInlineProduct.trigger('change');
            $cbbInlinePrice.val(currentPrice);
            $cbbInlinePrice.trigger('change');
            $cbbInlineProduct.find('option[value="' + $inlineProduct.attr('data-value') + '"]').removeClass('hidden');
            $cbbInlineType.find('option[value="' + $inlineType.attr('data-value') + '"]').removeClass('hidden');

        });

        $('body').on('click', '.btnInlineRemove', function (e) {
            var btn = $(this);
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to remove?')}).then(function () {
                btn.closest('tr').remove();
                thisInstance.calculateTotal();
            });
        });
        $('body').on('change', 'select.cbbInlineProduct', function () {
            var parentTr = $(this).closest('tr');
            var parentTd = $(this).closest('td');
            if ( $(this).val() == -1) {
                return true;
            }
            if (thisInstance.checkDuplicateProduct($(this).val(),parentTr.index()) ) {
                $(this).val('-1').trigger('change');
                return true;
            }

            var cbbInlinePrice = parentTr.find('select.cbbInlinePrice');
            cbbInlinePrice.html('');
            parentTd.attr('data-value', $(this).val());
            var prices = $(this).find('option:selected').attr('data-price');
            if (!prices) {
                return true;
            }

            prices = prices.split('|');
            var priceListLength = prices.length;
            var priceDefault = parseFloat(prices[0]);
            for (var i = 0; i < priceListLength; i++) {
                if  (parseFloat(prices[i]) >= 0) {
                    cbbInlinePrice.append(new Option(thisInstance.formatNumber(parseFloat(prices[i])), parseFloat(prices[i])));
                }
            }
            cbbInlinePrice.closest('td').attr('data-value', priceDefault);
            thisInstance.calculateTotal();
        });
        $('body').on('change input', 'select.cbbInlinePrice, input.txtInlineQuantity, select.cbbInlineType', function () {
            var parentTd = $(this).closest('td');
            parentTd.attr('data-value', $(this).val());
            thisInstance.calculateTotal();
        });

        $('body').on('change input', 'input#comboDiscountCash', function () {
            var comboDiscountCash = thisInstance.parseNumber($('#comboDiscountCash').val());
            var comboDiscountPercent = 0;
            var comboListPrice = thisInstance.parseNumber($('#comboListPrice').html());

            if (comboListPrice > 0) {
                comboDiscountPercent = (comboDiscountCash / comboListPrice * 100).toFixed(2);
            }
            $('#comboDiscountPercent').val(comboDiscountPercent)
            thisInstance.calculateTotal();
        });

        $('body').on('change input', 'input#comboDiscountPercent', function () {
            var comboDiscountPercent = thisInstance.parseNumber($('#comboDiscountPercent').val());
            var comboDiscountCash = 0;
            var comboListPrice = thisInstance.parseNumber($('#comboListPrice').html());

            if (comboListPrice > 0) {
                comboDiscountCash = comboDiscountPercent * comboListPrice / 100;
            }
            $('#comboDiscountCash').val(thisInstance.formatNumber(comboDiscountCash));
            thisInstance.calculateTotal();
        });
        $('body').on("keyup", '.formatNumber', function (event) {
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
            input = input ? thisInstance.parseNumber(input) : 0;
            $this.val(function () {
                return (input === 0) ? 0 : input.toLocaleString("en-US");
            });
        });
        $('body').on("change", 'select.cbbInlineType', function () {
            var mainProductOptionValue = 1;
            var optionalProductOptionValue = 2;
            var rows = $('#combo-product').find('tbody').find('tr');
            var parentTrIndex = $(this).closest('tr').index();

            for (var i = 0; i < rows.length; i++) {
                if ($(this).val() == mainProductOptionValue && parentTrIndex != i) {
                    $('select.cbbInlineType').not(this).find('option[value="' + optionalProductOptionValue + '"]').attr("selected", true);
                    var $row = $(rows[i]);
                    if (!$row.find('select.cbbInlineType').length) {
                        $row.find('td.type-td').attr('data-value', 2);
                        $row.find('td.type-td').html(app.vtranslate('OPTIONAL'));
                    }
                }
            }
        });
    },
    calculateTotal: function () {
        var comboListPrice = 0;
        var mainProductOptionValue = 1;
        var optionalProductOptionValue = 2;
        var hasMainProduct = false;
        var comboDiscountCash = this.parseNumber($('#comboDiscountCash').val());
        var rows = $('#combo-product').find('tbody').find('tr');
        $('.cloneRow td.prod-td select.cbbInlineProduct option').removeClass('hidden');
        for (var i = 0; i < rows.length; i++) {
            var $row = $(rows[i]);
            var inlineQuantityValue = $row.find('td.quantity-td').attr('data-value');
            var inlinePriceValue = $row.find('td.price-td').attr('data-value');
            var inlineProductValue = $row.find('td.prod-td').attr('data-value');
            var inlineTypeValue = $row.find('td.type-td').attr('data-value');
            $('.cloneRow td.prod-td select.cbbInlineProduct option[value="' + inlineProductValue + '"]').addClass('hidden');
            if (inlineTypeValue == mainProductOptionValue) {
                hasMainProduct = true;
                $('.cloneRow td.type-td select.cbbInlineType option[value="' + optionalProductOptionValue + '"]').attr("selected", true);
            }
            var inlineTotal = this.parseNumber(inlineQuantityValue) * this.parseNumber(inlinePriceValue);
            $row.find('td.total-td').html(this.formatNumber(inlineTotal));
            comboListPrice += inlineTotal;
        }
        if (!hasMainProduct) {
            $('.cloneRow td.type-td select.cbbInlineType option[value="' + optionalProductOptionValue + '"]').removeAttr('selected');
        }
        if (comboDiscountCash > comboListPrice) {
            var params = {
                message: 'Combo discount must less than or equal to Combo List price',
            };
            app.helper.showErrorNotification(params);
            comboDiscountCash = comboListPrice;
            $('#comboDiscountCash').val(this.formatNumber(comboDiscountCash));
            $('#comboDiscountPercent').val(100);
        }
        var comboNetPrice = comboListPrice - comboDiscountCash;
        $('#comboNetPrice').html(this.formatNumber(comboNetPrice));
        $('#comboListPrice').html(this.formatNumber(comboListPrice));
    },
    checkDuplicateProduct: function (prodId, ignoreindex) {
        var rows = $('#combo-product').find('tbody').find('tr');
        for (var i = 0; i < rows.length; i++) {
            var $row = $(rows[i]);
            var inlineProductValue = $row.find('td.prod-td').attr('data-value');
            if (prodId == inlineProductValue && ignoreindex != i) {
                var params = {
                    message: 'Duplicate Product',
                };
                app.helper.showErrorNotification(params);
                return true;
            }
        }
        return false;
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
    initializePaginationEvents: function () {
        return true;
    },
    getRandomUnique: function () {
        var d = new Date();
        return d.getMinutes() + '' + d.getSeconds();
    },
    registerEvents: function () {
        this._super();

        this.registerDatatable();
        this.registerCustomEvents();
    }
});

$(document).ready(function () {

});
