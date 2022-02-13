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
        <div class="row">
            <div class="col-lg-6 col-md-8 col-sm-12 col-xs-12 form-group form-addnewcustomer">
                <div class="col-md-3 col-sm-4">
                    <label>{vtranslate('LBL_CAMPAIGN', $MODULE)}:</label></div>
                <div class="col-md-9 col-sm-8">
                    <div class="marginBottom10px marginLeft10px">
                        <select class="SELECTCAMPAIGN inputElement select2" name="province" id="SELECTCAMPAIGN">
                            <option value="">{vtranslate('LBL_SELECTCAMPAIGN', $MODULE)}</option>
                            {foreach from=$CAMPAIGNS item=cp}
                             <option value="{$cp->cpId}">{$cp->cpName}</option>
                            {/foreach}
                        </select>
                    </div>
                </div>
                <div class="col-md-3 col-sm-4">
                    <label>{vtranslate('LBL_CALLINGLIST', $MODULE)}:</label></div>
                <div class="col-md-9 col-sm-8">
                    <div class="marginBottom10px marginLeft10px">
                        <select class="SELECTCALLINGLIST inputElement select2" name="province" id="SELECTCALLINGLIST">
                            <option value="">{vtranslate('LBL_SELECTCALLINGLIST', $MODULE)}</option>
                        </select>
                    </div>
                    <div class="bhs-button marginLeft10px">
                        <button id="btnAddManual" type="submit" class="btn btn-blue marginRight10px" style="min-width: 140px;">{vtranslate('LBL_ADDMANUAL', $MODULE)}</button>
                        <button id="btnImportExcel" type="submit" class="btn btn-blue" style="min-width: 140px;">{vtranslate('LBL_IMPORTEXCEL', $MODULE)}</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="tabbable padding15px marginBottom25px box-addManual">
            <div class="box-header row marginBottom15px">
                <div class="col-md-6">
                    <h3 class="header-single-content marginBottom10px marginTop0px">
                        {vtranslate('LBL_ADDMANUAL', $MODULE)}
                    </h3>
                </div>
                <div class="col-md-6 text-right">
                    <button class="btn btnSaveLead" style="padding: 10px 20px;"><i class="fa fa-save"></i> {vtranslate('LBL_SAVE', $MODULE)} </button>
                </div>
            </div>
            <input type="hidden" id="hfRecordId" value="{$RECORD_ID}">
            <input type="hidden" id="hfRecordIdUpdate" value="{$RECORD->leadId}">
            <input type="hidden" id="hfCampaignId" value="{$RECORD->cpId}">
            <input type="hidden" id="cb_province" value="{$RECORD->province}">
            <input type="hidden" id="cb_district" value="{$RECORD->district}">
            <input type="hidden" id="cb_subdistrict" value="{$RECORD->subdistrict}">
            <input type="hidden" id="cb_neighborhood" value="{$RECORD->neighborhood}">
            <input type="hidden" id="cb_zipcode" value="{$RECORD->postalCode}">
            <input type="hidden" id="addNew_leadType" value="M">
            {assign var=userTMS value=Vtiger_Session::get('agent_info')}
            <input type="hidden" id="isNoriApp" value="{if $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID}1{else}0{/if}">
            <div class="row form-group">
                <div class="col-lg-5 col-md-12">
                    <div class="contents tabbable clearfix">
                        <ul class="nav nav-tabs layoutTabs massEditTabs">
                            <li class="tab-item taxesTab active"><a data-toggle="tab" href="#contactInformation"><strong>{vtranslate('LBL_CONTACT_INFORMATION', $MODULE)}</strong></a></li>
                           <!--  <li class="tab-item chargesTab"><a data-toggle="tab" href="#stock"><strong>{vtranslate('LBL_STOCK_BLOCK', $MODULE)} <img src="layouts/v7/resources/Images/icon/icon-important.png" alt=""></strong></a></li> -->
                        </ul>
                        <div class="tab-content layoutContent padding15px overflowVisible">
                            <div class="tab-pane active" id="contactInformation">
                                {*                    <ul class="list-unstyled list-inline" style="margin-bottom: 0px !important;">*}
                                {*                        <li class="fontBold">*}
                                {*                            {vtranslate('Order ID', $MODULE)}: <span >{$RECORD_ID}</span>*}
                                {*                        </li>*}
                                {*                        <li class="pull-right">*}
                                {*                            {if $RECORD->leadStatusName}*}
                                {*                                <div id="status" class="alert {$RECORD_STATUS_CLASS} leadStatus" {if $RECORD->leadStatus|in_array:[3,7]}title="{$RECORD->userDefin05}"{/if}>*}
                                {*                                    {if $RECORD->leadStatusName|in_array:['callback consult', 'callback propect']}*}
                                {*                                        callback*}
                                {*                                    {/if}*}
                                {*                                    {if $RECORD->leadStatusName|in_array:['unreachable', 'busy', 'no answer']}*}
                                {*                                        unCall*}
                                {*                                    {/if}*}
                                {*                                    {if $RECORD->leadStatusName|in_array:['duplicated', 'invalid']}*}
                                {*                                        trash*}
                                {*                                    {/if}*}
                                {*                                    {if !$RECORD->leadStatusName|in_array:['duplicated', 'invalid','callback consult', 'callback propect','unreachable', 'busy', 'no answer']}*}
                                {*                                        {$RECORD->leadStatusName}*}
                                {*                                    {/if}*}
                                {*                                    {if $RECORD->leadStatus|in_array:[3,7]} &nbsp;<img src="layouts/v7/resources/Images/icon/icon-info.png" >{/if}*}
                                {*                                </div>*}
                                {*                            {/if}*}
                                {*                        </li>*}
                                {*                    </ul>*}
                                {*                    <ul class="list-unstyled list-inline  marginBottom25px header-info-contactinfomation">*}
                                {*                        <li>*}
                                {*                            {vtranslate('LBL_CREATED_DATE', $MODULE)}: <span>{Vtiger_BhsUtility_Model::displayDatetimeWithUserFormat($RECORD->createdate)}</span>*}
                                {*                        </li>*}
                                {*                        <li>*}
                                {*                            {vtranslate('LBL_PHONENUMBER', $MODULE)}: <span >{$RECORD->phone}</span>*}
                                {*                        </li>*}
                                {*                    </ul>*}
                                <div class="row form-group">
                                    <div class="col-md-3 col-sm-4">
                                        <label>{vtranslate('LBL_LEAD_NAME', $MODULE)}:</label>
                                    </div>
                                    <div class="col-md-9 col-sm-8">
                                        <input type="text" name="name" id="txtFullname" class="inputElement" data-rule-required="true" value="{$RECORD->name}" aria-required="true" placeholder="{vtranslate('LBL_FULLNAME', $MODULE)}">
                                    </div>
                                </div>
                                <div class="row form-group">
                                    <div class="col-md-3 col-sm-4">
                                        <label>{vtranslate('LBL_LEAD_PHONE', $MODULE)}:</label>
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
                                                <select class="cbbDistricts inputElement select2" name="district" placeholder="{vtranslate('LBL_DISTRICT', $MODULE)}">
                                                    <option value="">{vtranslate('LBL_DISTRICT', $MODULE)}</option>
                                                </select>
                                            </div>
                                            <div class="col-md-6 marginBottom25px">
                                                <select class="cbbTowns inputElement select2" name="subdistrict" placeholder="{vtranslate('LBL_WARD', $MODULE)}">
                                                    <option value="">{vtranslate('LBL_WARD', $MODULE)}</option>
                                                </select>
                                            </div>
                                            {if ($SHOW_NEIGHBORHOOD == true)}
                                            <div class="col-md-6 marginBottom25px">
                                                <select class="cbbNeighborhoods inputElement select2" name="neighborhood" placeholder="{vtranslate('LBL_NEIGHBORHOOD', $MODULE)}">
                                                    <option value="">{vtranslate('LBL_NEIGHBORHOOD', $MODULE)}</option>
                                                </select>
                                            </div>
                                            {/if}
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
                                        <label>{vtranslate('LBL_DESCRIPTION', $MODULE)}:</label>
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
                                        <textarea class="form-control " rows="3" class="form-control" id="txtDestinationAddress" placeholder="{vtranslate('PH_DESTINATION_ADDRESS', $MODULE)}" readonly>{$RECORD->address}</textarea>
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
                                        <input id = "customerAge" type ="number" class="form-control " name = "customerAge" rows="3" placeholder="{vtranslate('PH_CUSTOMER_AGE', $MODULE)}"  value="{$RECORD->customerAge}">
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
                                <input type="hidden" id="show_warning_located" value='{json_encode($SHOW_WARNING)}'>
                                <input type="hidden" id="show_neighborhood" value='{$SHOW_NEIGHBORHOOD}'>
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
                                    <tr class="odd"><td valign="top" colspan="4" class="dataTables_empty">{vtranslate('LBL_NO_DATA_IN_TABLE', $MODULE)}</td></tr>
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
                                <th>{vtranslate('LBL_QUANTITY', $MODULE)}</th>
                                <th>{vtranslate('LBL_PRICE', $MODULE)}</th>
                                <th>{vtranslate('LBL_TOTAL', $MODULE)}</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot style="border-top: 1px solid #ddd;">
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
                                    <input value="1" readonly type="number" min="1" oninput="this.value = this.value.replace(/[^0-9.]/g, ''); this.value = this.value.replace(/(\..*)\./g, '$1');" class="txtInlineQuantity inputElement" />
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
                </div>
            </div>
        </div>
        <div class="tabbable padding15px marginBottom25px box-importExcel box-disabled">
            <h3 class="header-single-content marginBottom10px marginTop0px">
                {vtranslate('LBL_IMPORTEXCEL', $MODULE)}
            </h3>
            <form enctype="multipart/form-data" name="formUpload" id = "form-import-excel" action="/index.php?module=BhsOrders&action=ActionAjax&mode=uploadExcel"> 
                <div class="importexcel">
                    <input type="file" placeholder="import excel" name="file">
                    <button id="btnImport" type="button" class="btn btn-blue marginTop10px" style="min-width: 140px;">{vtranslate('LBL_IMPORT', $MODULE)}</button>
                </div>
            </form>
            <input type="hidden" id="API_ENDPOINT" value="{$API_ENDPOINT}">
        </div>
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
{/strip}