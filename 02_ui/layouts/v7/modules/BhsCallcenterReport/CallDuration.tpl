<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getCallDuration"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
                <tr>
                    <th>{vtranslate('LBL_DAY', $MODULE)}</th>
                    <th>{vtranslate('LBL_DATE', $MODULE)}</th>
                    {foreach from=$EXTS item=EXT}
                    <th>{$EXT}</th>
                    {/foreach}
                    <th>{vtranslate('LBL_TOTAL_NUMBER_OF_CALLS', $MODULE)}</th>
                    <th>{vtranslate('LBL_TOTAL_CALL_DURATION', $MODULE)}</th>
                    <th>{vtranslate('LBL_AVERAGE_CALL_DURATION', $MODULE)}</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
   
</div>