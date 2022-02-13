<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getAgentLoginTime"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
            <tr>
                <th></th>
                <th>{vtranslate('LBL_USER', 'Vtiger')}</th>
                <th>{vtranslate('LBL_USER_EXTENSION', $MODULE)}</th>
                <th>{vtranslate('LBL_SIGN_IN', $MODULE)}</th>
                <th>{vtranslate('LBL_SIGN_OUT', $MODULE)}</th>
                <th>{vtranslate('LBL_DURATION', $MODULE)}</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    
</div>