<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getCustomerAdded"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
            <tr>
                <th>{vtranslate('LBL_DATE', $MODULE)}</th>
                {foreach from=$USERS item=USER}
                <th>{$USER}</th>
                {/foreach}
                <th>{vtranslate('LBL_TOTAL_AE', $MODULE)}</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
  
</div>