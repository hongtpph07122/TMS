
<table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%">
    <thead>
    <tr>
        <th>{vtranslate('LBL_WIDGET_LINE', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_WAITING', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_TALKING', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_TOTAL_OFFERED', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_TOTAL_LOST', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_LOST_CALL', $MODULE_NAME)}</th>
    </tr>
    </thead>
    <tbody>
    {foreach from=$DATA item=ROW}
        <tr>
            <td>{$ROW['line']}</td>
            <td>{$ROW['waiting']}</td>
            <td>{$ROW['talking']}</td>
            <td>{$ROW['total_offered']}</td>
            <td>{$ROW['total_lost']}</td>
            <td>{$ROW['lost_call']}</td>
        </tr>
    {/foreach}
    </tbody>
</table>