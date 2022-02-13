<div  style="margin: 0px 15px;">
    <div class="row">
        <div class="col-md-2 col-sm-3 marginBottom15px">
            <select class="select2" id="cbbCustomerStatus" placeholder="{vtranslate('LBL_SELECT_CUSTOMER_STATUS', $MODULE)}" style="width: 100%">
                <option value="">{vtranslate('LBL_SELECT_CUSTOMER_STATUS', $MODULE)}</option>
                {foreach from=$CUSTOMER_STATUS key=CUSTOMER_STATUS_KEY item=CUSTOMER_STATUS_NAME}
                    <option value="{$CUSTOMER_STATUS_KEY}">{$CUSTOMER_STATUS_NAME}</option>
                {/foreach}
            </select>
        </div>
        <div class="col-md-2 col-sm-3 marginBottom15px">
            <select class="select2" id="cbbProvinces" placeholder="{vtranslate('LBL_SELECT_PROVINCE', $MODULE)}" style="width: 100%">
                <option value="">{vtranslate('LBL_SELECT_PROVINCE', $MODULE)}</option>
                {foreach from=$PROVINCES key=PROVINCE_KEY item=PROVINCE_NAME}
                    <option value="{$PROVINCE_KEY}">{$PROVINCE_NAME}</option>
                {/foreach}
            </select>
        </div>
        <div class="col-md-2 col-sm-3 marginBottom15px">
            <select class="select2" id="cbbDistricts" placeholder="{vtranslate('LBL_SELECT_DISTRICT', $MODULE)}" style="width: 100%">
            </select>
        </div>
        <div class="col-md-1 col-sm-3 marginBottom15px">
            <button id="btnFindCustomer" class="btn btn-blue" style="min-width: 120px; max-width:100%;"><i class="fa fa-search"></i>&nbsp;{vtranslate('LBL_FIND', $MODULE)}</button>
        </div>
    </div>
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-bordered custom-dt"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
                <tr>
                    <th><input class="check-all" type="checkbox"></th>
                    {include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
                </tr>
                <tr class="bhs-filter">
                    <th></th>
                    {include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
</div>