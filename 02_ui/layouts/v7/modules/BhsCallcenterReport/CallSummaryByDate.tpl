<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getCallSummaryByDate"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
                <tr>
                    <th>{vtranslate('LBL_CALL_DATE', $MODULE)}</th>
                    <th>{vtranslate('LBL_EXT', $MODULE)}</th>
                    <th>{vtranslate('LBL_TOTAL_CALL', $MODULE)}</th>
                    <th>{vtranslate('LBL_TOTAL_TALK_TIME', $MODULE)}</th>
                    <th>{vtranslate('LBL_ANSWER', $MODULE)}</th>
                    <th>{vtranslate('LBL_NO_ANSWER', $MODULE)}</th>
                    <th>{vtranslate('LBL_BUSY', $MODULE)}</th>
                    <th>{vtranslate('LBL_FAILED_CONGRESTITION', $MODULE)}</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    
</div>