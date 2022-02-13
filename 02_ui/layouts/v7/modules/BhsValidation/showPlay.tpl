{strip}
    <div class='modal-dialog'>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                <h3>{vtranslate('LIST_RECORD_OF', $MODULE)} {$LEAD_NAME} ({$LEAD_PHONE})</h3>
            </div>
            <div class="modal-body table-responsive">
                <table class="table table-bordered dataTable" style="width: 100%">
                    <thead>
                        <tr>
                            <th style="width: 50%">{vtranslate('LBL_FILE_URL', $MODULE)}</th>
                            <th style="width: 25%">{vtranslate('LBL_FILE_CREATE_DATE', $MODULE)}</th>
                        </tr>
                    </thead>
                    <tbody>
                    {if (empty($LIST_CDR))}
                        <tr>
                            <td class = "text-center" colspan = "3">
                                {vtranslate('NO_DATA', $MODULE)}
                            </td>
                        </tr>
                    {else}
                    {foreach from=$LIST_CDR  item=cdr}
                    <tr>
                        <td>
                            {if (!empty($cdr['playbackUrl']))}
                                <audio controls style="width: 300px;">
                                    <source src="{$cdr['playbackUrl']}" type="audio/wav">
                                </audio>
                            {/if}
                        </td>
                        <td>{if (!empty($cdr['createtime']))}{Date('d/m/Y', strtotime($cdr['createtime']))}{/if}</td>
                    </tr>
                    {/foreach}
                    {/if}
                    </tbody>
                </table>
            </div>
        </div>
    </div>
{/strip}