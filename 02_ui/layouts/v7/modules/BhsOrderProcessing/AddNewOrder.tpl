{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
************************************************************************************}
{strip}
    <div style="margin-top: 10px;">
    <input type="hidden" id="hfRecordId" value="{$RECORD_ID}">
    <input type="hidden" id="hfRecordIdUpdate" value="{$RECORD->leadId}">
    <input type="hidden" id="hfCampaignId" value="{$RECORD->cpId}">
    <input type="hidden" id="cb_province" value="{$RECORD->province}">
    <input type="hidden" id="cb_district" value="{$RECORD->district}">
    <input type="hidden" id="cb_subdistrict" value="{$RECORD->subdistrict}">
    <input type="hidden" id="cb_neighborhood" value="{$RECORD->neighborhood}">
    <input type="hidden" id="cb_zipcode" value="{$RECORD->postalCode}">
    <input type="hidden" id="addNew_leadType" value="M">
    <div class="row form-group">
    <div class="col-lg-5 col-md-12">
        <div class="contents tabbable clearfix">
            <ul class="nav nav-tabs layoutTabs massEditTabs">
                <li class="tab-item taxesTab active"><a data-toggle="tab" href="#contactInformation"><strong>{vtranslate('LBL_CONTACT_INFORMATION', $MODULE)}</strong></a></li>
                <!-- <li class="tab-item chargesTab"><a data-toggle="tab" href="#stock"><strong>{vtranslate('LBL_STOCK_BLOCK', $MODULE)} <img src="layouts/v7/resources/Images/icon/icon-important.png" alt=""></strong></a></li> -->
                </ul>
            <div class="tab-content layoutContent padding15px overflowVisible">
                <div class="tab-pane active" id="contactInformation">
                    <label>{vtranslate('LBL_ASIGNTOAGENT', $MODULE)}:</label>
                    <div class="tabbable marginBottom15px" style="padding:15px 15px 0">
                        <div class="row form-group">
                            <div class="col-md-3 col-sm-4">
                                <label>{vtranslate('LBL_CAMPAIGN', $MODULE)}:</label>
                            </div>
                            <div class="col-md-9 col-sm-8">
                                <select id = 'SELECTCAMPAIGN' class="SELECTCAMPAIGN inputElement select2" name="assignCampaign">
                                    <option value="">{vtranslate('LBL_SELECTCAMPAIGN', $MODULE)}</option>
                                {foreach from=$CAMPAIGNS item=cp}
                                     <option value="{$cp->cpId}">{$cp->cpName}</option>
                                {/foreach}
                                </select>
                            </div>
                        </div>
                        <div class="row form-group">
                            <div class="col-md-3 col-sm-4">
                                <label>{vtranslate('LBL_AGENT', $MODULE)}:</label>
                            </div>
                            <div class="col-md-9 col-sm-8">
                                <select id = 'SELECTAGENT' class="SELECTAGENT inputElement select2" name="assignAgent">
                                    <option value="">{vtranslate('LBL_SELECTAGENT', $MODULE)}</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_NAME', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <input type="text" name="name" id="txtFullname" class="inputElement" data-rule-required="true" value="{$RECORD->name}" aria-required="true" placeholder="{vtranslate('LBL_FULLNAME', $MODULE)}">
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_PHONE_NUMBER', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8" style="display:inline-block">
                            <div style="display:flex">
                                <input type="text" name="phone" class="inputElement" id="txtPhoneNumber" data-rule-required="true" aria-required="true" value="{$RECORD->phone}" placeholder="0987654321">
                                <button id="btnClickToDial" type="submit" data-source="call" class="btn btn-success ClickToDial "><img src="layouts/v7/resources/Images/icon/icon-phone.png" alt=""></button>
                            </div>
                            {* <div style="display:flex;margin-top: 10px">
                                <input type="text" name="phone" class="inputElement" id="txtPhoneNumber" data-rule-required="true" aria-required="true" value="{$RECORD->phone}" placeholder="0987654321">
                                <button id="btnClickToDial" type="submit" class="btn ClickToDial btn-success"><img src="layouts/v7/resources/Images/icon/icon-phone.png" alt=""></button>
                            </div> *}
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_Location', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <div class="row">
                                <div class="col-md-6 marginBottom25px">
                                    <select class="cbbProvinces inputElement select2" name="province">
                                        <option value="">{vtranslate('LBL_PROVINCE', $MODULE)}</option>
                                        {foreach from=$PROVINCES item=PROVINCE}
                                            <option {if $RECORD->province == $PROVINCE->prvId } selected {/if} value="{$PROVINCE->prvId}" data-status="{$PROVINCE->status}" data-statusname="{$PROVINCE->statusName}">{$PROVINCE->name}</option>
                                        {/foreach}
                                    </select>
                                </div>
                                <div class="col-md-6 marginBottom25px">
                                    <select class="cbbDistricts inputElement select2" name="district" placeholder="District">
                                        <option value="">{vtranslate('LBL_DISTRICT', $MODULE)}</option>
                                    </select>
                                </div>
                                <div class="col-md-6 marginBottom25px">
                                    <select class="cbbTowns inputElement select2" name="subdistrict" placeholder="Ward">
                                        <option value="">{vtranslate('LBL_WARD', $MODULE)}</option>
                                    </select>
                                </div>
                                <div class="col-md-6 marginBottom25px">
                                    <select class="cbbNeighborhoods inputElement select2" name="neighborhood" placeholder="{vtranslate('LBL_NEIGHBORHOOD', $MODULE)}">
                                        <option value="">{vtranslate('LBL_NEIGHBORHOOD', $MODULE)}</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_ZIPCODE', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <select id="zipcode" class="cbbZipcode inputElement select2" name="zipcode" placeholder="{vtranslate('LBL_ZIPCODE', $MODULE)}">
                                <option value="">{vtranslate('LBL_ZIPCODE', $MODULE)}</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_ADDRESS', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <textarea class="form-control " rows="3" name="address" id="txtAddress" placeholder="{vtranslate('PH_ADDRESS', $MODULE)}">{$RECORD->address}</textarea>
                        </div>
                    </div>
                    <hr>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_PRODUCT_DESCRIPTION', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <textarea class="form-control " rows="3" name="description" id="txtComment" placeholder="{vtranslate('PH_DESCRIPTION', $MODULE)}">{$RECORD->comment}</textarea>
                        </div>
                    </div>
                    <hr>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_DESTINATION_ADDRESS', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <textarea class="form-control " rows="3" class="form-control" id="txtDestinationAddress" placeholder="{vtranslate('PH_DESTINATION_ADDRESS', $MODULE)}">{$RECORD->address}</textarea>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_ADDRESS_SENT_BY_ORDER', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <textarea class="form-control " rows="3" placeholder="{vtranslate('PH_ADDRESS_SENT_BY_ORDER', $MODULE)}" readonly>{$RECORD->address}</textarea>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_CUSTOMER_EMAIL', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <input  id = "customerEmail"  class="form-control " name = "customerEmail" rows="3" placeholder="{vtranslate('PH_CUSTOMER_EMAIL', $MODULE)}" value="{$RECORD->customerEmail}">
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label>{vtranslate('LBL_CUSTOMER_AGE', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <input id = "customerAge" class="form-control " type ="number"  name = "customerAge" rows="3" placeholder="{vtranslate('PH_CUSTOMER_AGE', $MODULE)}"  value="{$RECORD->customerAge}">
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-3 col-sm-4">
                            <label for="exampleInputEmail1">{vtranslate('LBL_PAYMENT_METHOD', $MODULE)}:</label>
                        </div>
                        <div class="col-md-9 col-sm-8">
                            <select class="select-payment inputElement" style="width: 100%;">
                                {foreach from=$PAYMENT_METHODS item=PAYMENT_METHOD }
                                 <option value="{$PAYMENT_METHOD->value}">{vtranslate('LBL_PAYMENT_METHOD_'|cat:$PAYMENT_METHOD->name|upper, $MODULE)}</option>
                                {/foreach}
                            </select>
                        </div>
                    </div>
                     <div class="row form-group Deposit hidden" >
                            <div class="col-md-3 col-sm-4">
                                <label>{vtranslate('LBL_PAYMENT_METHOD_DEPOSIT_AMOUNT', $MODULE)}:</label>
                            </div>
                            <div class="col-md-9 col-sm-8">
                                <input name="name" id="txtDeposit" class="inputElement" data-rule-required="true" value="0" autocomplete="off" aria-required="true">
                            </div>
                        </div>
                    <input type="hidden" name="lead-text" id="lead-text" value='{$LEAD_STATUS_TEXT}'>
                    <select class="reason inputElement hidden" name="province">
                        <option value="" >Choose the reason</option>
                        {foreach from=$LEAD_STATUS_TEXT item=item}
                            <option value="{$item}">{$item}</option>
                        {/foreach}
                    </select>
                    <select class="reasonUncalled inputElement hidden" name="province">
                        <option value="" >Choose the reason</option>
                        <option >Busy</option>
                        <option >Unreachable</option>
                        <option >No Answer </option>
                    </select>
                    <input type="hidden" id="txtCallbackText" value=''>
                    <input type="hidden" name="lead-text" id="leadStatus" value='2'>
                    <input type="hidden" name="lead-text" id="SOtype" value="{if $LEAD_TYPE == 'order'}order{else}''{/if}">
                </div>
                <div class="tab-pane" id="stock">
                    <button type="submit" class="btn btn-success btnRefreshStock">{vtranslate('LBL_REFRESH_STOCK', $MODULE)}</button>
                    <table id="tblMailList1" class="table table-striped table-bordered custom-dt  dataTable no-footer" style="width:100%" data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getStocks">
                        <thead>
                        <tr>
                            <th>{vtranslate('LBL_PRODUCT_NAME', $MODULE)}</th>
                            <th>{vtranslate('LBL_STOCK_NAME', $MODULE)}</th>
                            <th>{vtranslate('LBL_AVAILABLE', $MODULE)}</th>
                            <th>{vtranslate('LBL_INSTOCK', $MODULE)}</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-7 col-md-12">
        <div class="box">
            <h3 class="header-single-content marginBottom10px marginTop15px">{vtranslate('LBL_PRODUCT_BLOCK', $MODULE)} <button id="addProduct" type="submit" class="btn addProduct pull-right"> + {vtranslate('LBL_ADD_PRODUCT', $MODULE)}</button></h3>
            <table id="tblMailList0" class="table table-striped table-bordered custom-dt  dataTable no-footer" style="width:100%" >
                <thead>
                <tr>
                    <th class="hidden">{vtranslate('LBL_ID', $MODULE)}</th>
                    <th>{vtranslate('LBL_TYPE', $MODULE)}</th>
                    <th>{vtranslate('LBL_PRODUCT_NAME', $MODULE)}</th>
                    <th class="hidden">{vtranslate('LBL_STOCK', $MODULE)}</th>
                    <th>{vtranslate('LBL_QUANTITY', $MODULE)} </th>
                    <th>{vtranslate('LBL_PRICE', $MODULE)}</th>
                    <th>{vtranslate('LBL_TOTAL', $MODULE)}</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
                <tfoot style="border-top: 1px solid #ddd;">
                <tr >
                    <td colspan="1" class="hidden"></td>
                    <td colspan="2"></td>
                    <td colspan="2">{vtranslate('LBL_COMBO_DISCOUNT', $MODULE)}:</td>
                    <td colspan="1"><div><input readonly id="comboDiscount" value="0"/></div></td>
                    <td colspan="2"></td>
                </tr>
                <tr >
                    <td colspan="1" class="hidden"></td>
                    <td colspan="1">{vtranslate('LBL_SALE_DISCOUNT_PERCENT', $MODULE)}:</td>
                    <td colspan="1">
                        <input id='sale-discount-percent' type="number" min="0" max="100" width="100%"/>
                    </td>
                    <td colspan="2">{vtranslate('LBL_SALE_DISCOUNT', $MODULE)}:</td>
                    <td colspan="1"><input id='sale-discount' class="formatNumber"/></td>
                    <td colspan="2"></td>
                </tr>
                <tr >
                    <td colspan="1" class="hidden">Total before sale discount</td>
                    <td colspan="2"></td>
                    <td colspan="1" class="hidden" id="totalBeforeSaleDiscount"></td>
                    <td colspan="2">{vtranslate('LBL_TOTAL_AMOUNT', $MODULE)}</td>
                    <td colspan="1" class="tdTotal text-center" style="color: #FF9800;">0</td>
                    <td colspan="2"></td>
                </tr>
                <tr role="row" class="cloneRow">
                    <td class="hidden"></td>
                    <td class="text-left">
                        <select class="cbbInlineType inputElement">
                            <option value="1" selected>{vtranslate('LBL_SELECT_TYPE_NORMAL', $MODULE)}</option>
                            <option value="2">{vtranslate('LBL_SELECT_TYPE_COMBO', $MODULE)}</option>
                        </select>
                    </td>
                    <td class="text-left cbbInlineProductComboTd">
                        <select class="cbbInlineProduct inputElement">
                            <option class="defaultItem" value="-1">{vtranslate('LBL_PLS_SELECT', $MODULE)}</option>
                            {foreach from=$PRODUCTS item=PRODUCT}
                                <option class="productItem" value="{$PRODUCT['prodId']}" data-price="{$PRODUCT['price']}" data-discount="0">{$PRODUCT['name']}</option>
                            {/foreach}
                            {foreach from=$COMBOS item=COMBO}
                                <option class="comboItem hidden" value="{$COMBO['prodId']}" data-price="{$COMBO['price']}" data-discount="{$COMBO['discountCash']}">{$COMBO['name']}</option>
                            {/foreach}
                        </select>
                    </td>
                    <td class="text-left hidden">
                        <select class="cbbInlineStock inputElement">
                            <option value="">{vtranslate('LBL_SELECT_STOCK', $MODULE)}</option>
                            {foreach from=$STOCKS item=STOCK}
                                <option value="{$STOCK->pnId}" data-pnId="{$STOCK->pnId}">{$STOCK->shortname}</option>
                            {/foreach}
                        </select>
                    </td>
                    <td>
                        <input value="1" type="number" min="1" oninput="this.value = this.value.replace(/[^0-9.]/g, ''); this.value = this.value.replace(/(\..*)\./g, '$1');" class="txtInlineQuantity inputElement" />
                    </td>
                    <td>
                        <select class="txtInlinePrice inputElement">
                        </select>
                    </td>
                    <td >
                        <input value="0" class="txtInlineTotal inputElement" />
                    </td>
                    <td style="">
                        <a class="btnInline btnInlineSave clone"><i class="fa fa-check"></i></a>
                        <a class="btnInline btnInlineCancel clone"><i class="fa fa-trash"></i></a>
                        <a class="btnInline btnInlineEdit clone" style="display: none;"><i class="fa fa-pencil"></i></a>
                        <a class="btnInline btnInlineView clone" style="display: none;"><i class="fa fa-eye"></i></a>
                        <a class="btnInline btnInlineRemove clone" style="display: none;"><i class="fa fa-trash"></i></a>
                    </td>
                </tr>
                </tfoot>
            </table>

            <input type="hidden" id="hfSelectedProducts" value="{$RECORD->prodId}">
        </div>
        <div class="modal fade-scale" id="myWarningModal" role="dialog">
            <div class="modal-dialog" style="margin: 160px auto;width: 450px;">
            
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header text-center">
                    <h4 class="modal-title">Warning</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: -25px;">
                    <span aria-hidden="true">&times;</span>
                    </button>
                    </div>
                    <div class="modal-body" style="font-size: 17px;font-family: sans-serif;">
                    <p><span class="locationname">ahihi</span> is <span class="statusname">ahihi</span>.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
                    </div>
                </div>
            
            </div>
        </div>
    </div>
{/strip}