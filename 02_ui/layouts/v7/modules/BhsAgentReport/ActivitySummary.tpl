<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getActivitySummary"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
            <tr>
                <th></th>
                <th>{vtranslate('LBL_AGENT_NAME', $MODULE)}</th>
                <th>{vtranslate('LBL_Average_Available_Time', $MODULE)}</th>
                <th>{vtranslate('LBL_Average_Unavailable_Time', $MODULE)}</th>
                <th>{vtranslate('LBL_Average_Oncall_Time', $MODULE)}</th>
                <th>{vtranslate('LBL_Average_ACW_Time', $MODULE)}</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    
</div>