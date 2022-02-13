{strip}
    <div class='modal-dialog'>
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                <span aria-hidden="true" class='fa fa-close'></span>
            </button>
            <h3>{vtranslate('RECORD_OF', $MODULE)} {$LEAD_NAME} ({$LEAD_PHONE})</h3>
        </div>
        <div class="modal-content">
            <div class="modal-body table-responsive">
                <table class="table table-bordered dataTable" style="width: 100%">
                    <tbody>
                    {if (empty($CDR))}
                        <tr>
                            <td class = "text-center" colspan = "3">
                                {vtranslate('NO_DATA', $MODULE)}
                            </td>
                        </tr>
                    {else}
                    {foreach from=$CDR  item=cdr}
                    <tr>
                        <td class="text-center">
                            {if (!empty($cdr['playbackUrl']))}
                                <audio controls autoplay style="width: 300px;">
                                    <source src="{$cdr['playbackUrl']}" type="audio/wav">
                                </audio>
                            {/if}
                        </td>
                    </tr>
                    {/foreach}
                    {/if}
                    </tbody>
                </table>
            </div>
        </div>
    </div>
{/strip}