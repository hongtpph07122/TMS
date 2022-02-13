{strip}
    <div class='modal-dialog modal-lg'>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                <h3>{vtranslate('LBL_ORDERSHIPPING', $MODULE)}</h3>
            </div>
            <div class="modal-body">
                <h4 class="popup-title">
                    {if (!empty($ORDER->trackingCode))}
                    Order_{$ORDER->trackingCode}
                    {/if}
                </h4>
                <div class="row">
                    <div class="col-md-6">
                        <div class="box">
                            <div class="box-title">
                                <i class="fa fa-cart-arrow-down" aria-hidden="true"></i> {vtranslate('LBL_INFO', $MODULE)}
                            </div>
                            <div class="box-content">
                                <ul class="list-unstyled border1px">
                                <li class="row-col">
                                    <span class="col-5">
                                        {vtranslate('LBL_CUSTOMERNAMES', $MODULE)}: <strong> {$CUSTOMER->name}</strong>
                                    </span>
                                    <span class="col-5">
                                        {vtranslate('LBL_PHONENMUBER', $MODULE)}: <strong> {$CUSTOMER->phone}</strong>
                                    </span>
                                </li>
                                <li>
                                    {vtranslate('LBL_ADDRESS', $MODULE)}: <strong> {$CUSTOMER->address}</strong>
                                </li>
                                <li class="row-col">
                                    <span class="col-5">
                                        {vtranslate('LBL_WAREHOUSE', $MODULE)}: <strong>{$ORDER->warehouseName}</strong>
                                    </span>
                                    <span class="col-5">
                                        {vtranslate('LBL_CARRIER', $MODULE)}: <strong>{$ORDER->carrierName}</strong>
                                    </span>
                                </li>
                                <li>
                                    {vtranslate('LBL_SOURCE', $MODULE)}: <strong> {$ORDER->source}</strong>
                                </li>
                                <li>
                                    {vtranslate('LBL_STOCK', $MODULE)}: <strong> {$ORDER->ffmName}</strong>-  <b>
                        {if (!empty($ORDER->ffmCode))}
                                {$ORDER->ffmCode}
                        {/if}

                                         </b>

                                </li>
                                <li class="row-col">
                                     <span class="col-3">
                                         {vtranslate('LBL_TRACKINGCODE', $MODULE)}: <strong class="color-blue">{$ORDER->trackingCode}</strong>
                                     </span>
                                    <span class="col-3">
                                        {vtranslate('LBL_SOID', $MODULE)}: <strong class="color-blue"> {$ORDER->soId} </strong>
                                     </span>
                                    <span class="col-3">
                                        {vtranslate('LBL_DOID', $MODULE)}:  <strong class="color-blue">{$ORDER->doId}</strong>
                                     </span>
                                </li>
                                <li>
                                    {vtranslate('LBL_STATUS', $MODULE)}: <strong class="color-blue"> {$ORDER->status} </strong>
                                </li>
                                <li class="row-col">
                                     <span class="col-5">
                                         {vtranslate('LBL_DATECREATED', $MODULE)}:  <strong> {$ORDER->createdate}</strong>
                                     </span>
                                     <span class="col-5" id="dateApproval">
                                        {vtranslate('LBL_DATEAPPROVAL', $MODULE)}:  <strong>{$ORDER->approvedTime}</strong>
                                     </span>
                                </li>
                            </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="box marginBottom25px">
                            <div class="box-title">
                                <i class="fa fa-cart-arrow-down" aria-hidden="true"></i> Tracking code - {$ORDER->trackingCode}
                                   <a href="index.php?module=BhsOrderProcessing&view=Edit&type=order&recordId={$ORDER->soId}">
                                <span class="input-group-addon edit" ><i class="fa fa-pencil"></i></span></a>
                            </div>
                            <div class="box-content">
                                <table id="tblMailList0" class="table table-bordered nowrap custom-dt" style="width:100%"
                                >
                                    <thead>
                                    <tr>
                                        <th>{vtranslate('LBL_PRODUCT', $MODULE)}</th>
                                        <th>{vtranslate('LBL_QUANTITY', $MODULE)}</th>
                                        <th>{vtranslate('LBL_PRICE', $MODULE)}</th>
                                        <th>{vtranslate('LBL_TOTAL', $MODULE)}</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                     {foreach from=$PRODUCTS['products'] item=pro}
                                   <tr>
                                   {foreach from=$pro item=item}
                                    <td>{$item}</td>
                                     {/foreach}
                                   </tr>
                                    {/foreach}
                                    <tr >
                                    <td> <strong>In Total</strong></td>
                                    <td class="text-right" colspan="3"><strong>{$PRODUCTS['amount']}</strong></td>
                                    </tr>
                                    {if ($ORDER->amountcod != null || $ORDER->amountcod != '')}
                                    <tr >
                                    <td> <strong>COD Amount</strong></td>
                                    <td class="text-right" colspan="3"><strong>{$ORDER->amountcod}</strong></td>
                                    </tr>
                                    {/if}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="box">
                            <div class="box-title">
                                <i class="fa fa-history" aria-hidden="true"></i>&nbsp
                                {vtranslate('LBL_HISTORY', $MODULE)}
                            </div>
                            <div class="box-content">
                                <table id="tblMailList0" class="table table-bordered nowrap custom-dt" style="width:100%">
                                    <thead>
                                    <tr>
                                        <th>{vtranslate('LBL_CREATEDATE', $MODULE)}</th>
                                        <th>{vtranslate('LBL_STATUS', $MODULE)}</th>
                                         <th>{vtranslate('LBL_UPDATE', $MODULE)}</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {foreach from=$ORDER->history item=history}
                                        <tr>
                                            <td>{Date('d/m/Y', strtotime($history->changetime))}</td>
                                            <td>{$history->statusName}</td>
                                            <td>{$history->statusName}</td>
                                        </tr>
                                    {/foreach}
                                    </tbody>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
{/strip}
