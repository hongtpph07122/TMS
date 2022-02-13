{strip}
    <div class='modal-dialog ' style="min-width: 70%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                {if empty($RECORD)}
                <h3>{vtranslate('LBL_ADD_NEW_COMBO', $MODULE)}</h3>
                {else}
                    <h3>{vtranslate('LBL_EDIT_COMBO', $MODULE)}</h3>
                {/if}
            </div>
            <div class="modal-body">
                <form id="frmEditCombo">
                    <input type="hidden" id="hfRecordId" value="{$RECORD_ID}">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group row">
                                <label for="ComboName" class="col-sm-3 col-form-label">{vtranslate('LBL_COMBONAME', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <input type="text" class="inputElement" name="comboName" value="{$RECORD->name}" id="ComboName" placeholder="Enter name">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label for="btnToggleStatus" class="col-sm-3 col-form-label">{vtranslate('LBL_STATUS', $MODULE)}</label>
                            <div class="col-sm-9">
                                <button id="btnToggleStatus" type="button" class="btn btn-sm btn-toggle {if $RECORD->status eq 1}active{/if}" data-toggle="button" aria-pressed="true" autocomplete="off">
                                    <div class="handle"></div>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group row">
                                <label for="ComboName" class="col-sm-3 col-form-label">{vtranslate('LBL_CURRENCY', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <select class="inputElement" id="currency">
                                        {foreach from=$CURRENCY item=CURR}
                                            <option {if ($RECORD->unit ==$CURR->value)} selected {/if} value="{$CURR->value}">{$CURR->name}</option>
                                        {/foreach}
                                    </select>
                                </div>
                            </div>
                        </div>
{*                        <div class="col-md-6">*}
{*                            <label for="" class="col-sm-3 col-form-label">{vtranslate('LBL_COMBOTYPE', $MODULE)}</label>*}
{*                            <div class="col-sm-9">*}
{*                                <input type="text">*}
{*                            </div>*}
{*                        </div>*}
                    </div>
{*                    <div class="row">*}
{*                        <div class="col-md-6">*}
{*                            <div class="form-group row">*}
{*                                <label for="ComboName" class="col-sm-3 col-form-label">{vtranslate('LBL_STARTDATE', $MODULE)}</label>*}
{*                                <div class="col-sm-9">*}
{*                                    <div class="input-group inputElement padding0px" id='datetimepicker1'>*}
{*                                        <input name="start-date" class="start-date dateTimeField form-control inputElement" >*}
{*                                        <span class="input-group-addon" style="padding: 0px;"><img src="layouts\v7\modules\BhsCallStrategy\resources\DateTime.svg" style="width: 16px;height: 18px;" ></span>*}
{*                                    </div>*}
{*                                </div>*}
{*                            </div>*}
{*                        </div>*}
{*                        <div class="col-md-6">*}
{*                            <label for="" class="col-sm-3 col-form-label">{vtranslate('LBL_ENDDATE', $MODULE)}</label>*}
{*                            <div class="col-sm-9">*}
{*                                <div class="input-group inputElement padding0px" id='datetimepicker2'>*}
{*                                    <input name="end-date" class="end-date dateTimeField form-control inputElement" >*}
{*                                    <span class="input-group-addon" style="padding: 0px;"><img src="layouts\v7\modules\BhsCallStrategy\resources\DateTime.svg" style="width: 16px;height: 18px;" ></span>*}
{*                                </div>*}
{*                            </div>*}
{*                        </div>*}
{*                    </div>*}
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group row">
                                <label for="DescriptionCombo" class="col-sm-3 col-form-label">{vtranslate('LBL_DESCRIPTION', $MODULE)}</label>
                                <div class="col-md-12">
                                    <textarea class="form-control" id="txtDescriptionCombo" name="dscr" rows="3">{$RECORD->dscr}</textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group row">
                                <label for="AgentScriptCombo" class="col-sm-3 col-form-label">{vtranslate('LBL_AGENTSCRIPT', $MODULE)}</label>
                                <div class="col-md-12">
                                    <textarea class="form-control" id="txtAgentScriptCombo" name="dscrForAgent" rows="3">{$RECORD->dscrForAgent}</textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <h3 class="header-single-content marginBottom10px marginTop15px">{vtranslate('LBL_PRODUCT_BLOCK', $MODULE)}
                                <button id="addProduct" class="btn addProduct pull-right"> + {vtranslate('LBL_ADD_PRODUCT', $MODULE)}</button>
                            </h3>
                            <table id="combo-product" class="table table-striped table-bordered custom-dt  dataTable no-footer" style="width:100%" data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getProducts&leadId={$RECORD_ID} {if $LEAD_TYPE == 'order'}&type=order{/if}">
                                <thead>
                                <tr>
                                    <th>{vtranslate('LBL_TYPE', $MODULE)}</th>
                                    <th>{vtranslate('LBL_PRODUCT_NAME', $MODULE)}</th>
                                    <th>{vtranslate('LBL_QUANTITY', $MODULE)}</th>
                                    <th>{vtranslate('LBL_PRICE', $MODULE)}</th>
                                    <th>{vtranslate('LBL_TOTAL', $MODULE)}</th>
                                    <th width="10%"></th>
                                </tr>
                                </thead>
                                <tbody>
                                    {foreach from=$RECORD->items key=k item=product }

                                        <tr>
                                            <td class="text-left type-td" data-value="{if ($k==0)}1{else}2{/if}" data-display="">{if ($k==0)}MAIN{else}OPTIONAL{/if}</td>
                                            <td class="text-left prod-td" data-value="{$product->productId}" data-display="">{$product->productName}</td>
                                            <td class ="quantity-td" data-value="{$product->quantity}" data-display="">{$product->quantity}</td>
                                            <td  class ="price-td" data-value="{$product->price}" data-display="" data-price-index="{$product->price_index}">{number_format($product->price)}</td>
                                            <td class ="total-td" data-value="{$product->price*$product->quantity}" data-display="" >{number_format($product->price*$product->quantity)}</td>
                                            <td class ="action-td" style="">
                                                <div class="btn-group btn-group-action">
                                                    <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                        <img src="layouts/v7/resources/Images/icon/icon-action.png" alt="">
                                                    </a>
                                                    <div class="dropdown-menu dropdown-menu-right">
                                                        <a class="btnInlineEdit"><i class="fa fa-pencil"></i> Edit</a>
                                                        <a class="btnInlineRemove"><i class="fa fa-trash"></i> Trash</a>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    {/foreach}

                                </tbody>
                                <tfoot style="border-top: 1px solid #ddd;">
                                <tr>
                                    <td colspan="1"></td>
                                    <td colspan="1"></td>
                                    <td colspan="1"></td>
                                    <td colspan="1">{vtranslate('LBL_COMBO_LIST_PRICE', $MODULE)}:</td>
                                    <td colspan="1" id = "comboListPrice" >{if ($RECORD->listPrice)}{number_format($RECORD->listPrice)} {else}0{/if}</td>
                                    <td colspan="1"></td>
                                </tr>
                                <tr >
                                    <td colspan="1"></td>
                                    <td colspan="1">{vtranslate('LBL_COMBO_DISCOUNT_PERCENT', $MODULE)}:</td>
                                    <td colspan="1"><input id="comboDiscountPercent" class="formatNumber full-width" min="1" max="100" type="number" value="{$RECORD->discountPercent}" /></td>
                                    <td colspan="1">{vtranslate('LBL_COMBO_DISCOUNT', $MODULE)}:</td>
                                    <td colspan="1"><input id="comboDiscountCash" class="formatNumber full-width"  value="{number_format($RECORD->discountCash)}" /></td>
                                    <td colspan="1"></td>
                                </tr>
                                <tr >
                                    <td colspan="1"></td>
                                    <td colspan="1"></td>
                                    <td colspan="1"></td>
                                    <td colspan="1">{vtranslate('LBL_COMBO_NET_PRICE', $MODULE)}</td>
                                    <td colspan="1" id = "comboNetPrice" class="text-center" style="color: #FF9800;">{if ($RECORD->price)}{number_format($RECORD->price)} {else}0{/if}</td>
                                    <td colspan="1"></td>
                                </tr>
                                <tr role="row" class="cloneRow hidden" >
                                    <td class="text-left type-td" data-value="1" data-display="">
                                        <select class="cbbInlineType inputElement">
                                            <option value="1">{vtranslate('LBL_SELECT_TYPE_MAIN', $MODULE)}</option>
                                            <option value="2">{vtranslate('LBL_SELECT_TYPE_OPTIONAL', $MODULE)}</option>
                                        </select>
                                    </td>
                                    <td class="text-left prod-td" data-value="" data-display="">
                                        <select class="cbbInlineProduct inputElement">
                                            <option class="defaultItem" value="-1">{vtranslate('LBL_PLS_SELECT', $MODULE)}</option>
                                            {foreach from=$PRODUCTS item=PRODUCT}
                                                <option class="productItem" value="{$PRODUCT['prodId']}" data-price="{$PRODUCT['price']}">{$PRODUCT['name']}</option>
                                            {/foreach}
                                        </select>
                                    </td>
                                    <td class ="quantity-td" data-value="1" data-display="">
                                        <input value="1" type="number" min="1"  oninput="this.value = this.value.replace(/[^0-9.]/g, ''); this.value = this.value.replace(/(\..*)\./g, '$1');" class="txtInlineQuantity inputElement" />
                                    </td>
                                    <td  class ="price-td" data-value="" data-display="" data-price-index="0">
                                        <select class="cbbInlinePrice inputElement">
                                        </select>
                                    </td>
                                    <td class ="total-td" data-value="" data-display="" >
                                        0
                                    </td>
                                    <td class ="action-td" style="">
                                        <a class="btnInline btnInlineSave clone"><i class="fa fa-check"></i></a>
                                        <a class="btnInline btnInlineCancel clone"><i class="fa fa-trash"></i></a>
                                    </td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer text-right">
                <button class="btn btn-blue" id="btnSaveCombo">{vtranslate('LBL_SAVE', 'Vtiger')}</button>
                <button class="btn btn-default" data-dismiss="modal">{vtranslate('LBL_CANCEL', 'Vtiger')}</button>
            </div>
        </div>
    </div>
    <script>
        $('#datetimepicker1').datetimepicker({
            format: 'DD/MM/YYYY HH:mm:ss'
        });
        $('#datetimepicker2').datetimepicker({
            useCurrent: false,
            format: 'DD/MM/YYYY HH:mm:ss'
        });
    </script>>
{/strip}
