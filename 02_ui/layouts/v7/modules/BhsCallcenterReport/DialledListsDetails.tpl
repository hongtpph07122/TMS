<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:130%; min-width: 2200px"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getDialledListsDetails"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
                <tr>
                    <th>{vtranslate('LBL_NAME', $MODULE)}</th>
                    <th>{vtranslate('LBL_PHONE_NUMBER', $MODULE)}</th>
                    <th>{vtranslate('LBL_OWNER', $MODULE)}</th>
                    <th>{vtranslate('LBL_DISPOSITION', $MODULE)}</th>
                    <th>{vtranslate('LBL_MAX_TRY_COUNT', $MODULE)}</th>
                    <th>{vtranslate('LBL_PHONE_NUMBER', $MODULE)}</th>
                    <th>{vtranslate('LBL_ADDRESS', $MODULE)}</th>
                    <th>{vtranslate('LBL_EMAIL', $MODULE)}</th>
                    <th>{vtranslate('LBL_DESCRIPTION', $MODULE)}</th>
                    <th>{vtranslate('LBL_PROVINCE', $MODULE)}</th>
                    <th>{vtranslate('LBL_DISTRICT', $MODULE)}</th>
                    <th>{vtranslate('LBL_SUBDISTRICT', $MODULE)}</th>
                    <th>{vtranslate('LBL_ORIGIN_CODE', $MODULE)}</th>
                    <th>{vtranslate('LBL_COD_AMOUNT', $MODULE)}</th>
                    <th>{vtranslate('LBL_PAYMENT_INFO', $MODULE)}</th>
                    <th>{vtranslate('LBL_GCODE', $MODULE)}</th>
                    <th>{vtranslate('LBL_AMOUNT', $MODULE)}</th>
                    <th>{vtranslate('LBL_CALL_STATUS', $MODULE)}</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    
</div>