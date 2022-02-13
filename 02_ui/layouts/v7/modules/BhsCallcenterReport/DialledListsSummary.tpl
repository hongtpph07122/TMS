<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getDialledListsSummary"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
                <tr>
                    <th></th>
                    <th>{vtranslate('LBL_AGENT_NAME', $MODULE)}</th>
                    <th>{vtranslate('LBL_DATE', $MODULE)}</th>
                    <th>{vtranslate('LBL_TOTAL_CALLS', $MODULE)}</th>
                    <th>{vtranslate('LBL_ANSWER', $MODULE)}</th>
                    <th>{vtranslate('LBL_NO_ANSWER', $MODULE)}</th>
                    <th>{vtranslate('LBL_BUSY', $MODULE)}</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    
</div>