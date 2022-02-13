
<table id="tblMailList0" class="table table-bordered custom-dt" style="width:100%">
    <thead>
    <tr>
        <th>{vtranslate('LBL_WIDGET_OPERATOR', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_ORDERS', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_APPROVED', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_PERCENT', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_IN_TOTAL_VND', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_REJECTED', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_PERCENT', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_BILL', $MODULE_NAME)}</th>
        <th>{vtranslate('LBL_WIDGET_COMMISSION', $MODULE_NAME)}</th>
    </tr>
    </thead>
    <tbody>
    {foreach from=$DATA item=ROW}
        <tr>
            <td>{$ROW['operator']}</td>
            <td>{$ROW['order']}</td>
            <td>{$ROW['approved']}</td>
            <td>{$ROW['approved_percent']}</td>
            <td>{$ROW['in_total']}</td>
            <td>{$ROW['rejected']}</td>
            <td>{$ROW['rejected_percent']}</td>
            <td class="text-right"><b>{$ROW['bill']}</b></td>
            <td class="text-right"><b>{$ROW['commission']}</b></td>
        </tr>
    {/foreach}
    </tbody>
</table>