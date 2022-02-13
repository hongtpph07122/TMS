<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getActivityDuration"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
            <tr>
                <th></th>
                <th>{vtranslate('LBL_DATE_AND_TIME', $MODULE)}</th>
                <th>{vtranslate('LBL_AGENT_NAME', $MODULE)}</th>
                <th>{vtranslate('LBL_AVAILABLE', $MODULE)}</th>
                <th>{vtranslate('LBL_UNAVAILABLE', $MODULE)}</th>
                <th>{vtranslate('LBL_ONCALL', $MODULE)}</th>
                <th>{vtranslate('LBL_ACW', $MODULE)}</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
</div>