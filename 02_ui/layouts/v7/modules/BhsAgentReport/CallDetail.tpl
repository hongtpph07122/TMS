<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getCallDetail"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
            <tr>
                <th></th>
                <th>{vtranslate('LBL_AGENT_NAME', $MODULE)}</th>
                <th>{vtranslate('LBL_CALL_START', $MODULE)}</th>
                <th>{vtranslate('LBL_CALL_END', $MODULE)}</th>
                <th>{vtranslate('LBL_CALL_TYPE', $MODULE)}</th>
                <th>{vtranslate('LBL_CALLER', $MODULE)}</th>
                <th>{vtranslate('LBL_NUMBER_CALLED', $MODULE)}</th>
                <th>{vtranslate('LBL_Disposition_Codes', $MODULE)}</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
   
</div>