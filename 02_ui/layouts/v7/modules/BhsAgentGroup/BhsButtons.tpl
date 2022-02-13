{strip}
    {if $smarty.request.view eq 'List'}
      <div class="mx-auto" style="display:flex">
{*        <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="fa fa-download" aria-hidden="true" style="color:white"></i><span class="hidden-sm hidden-xs">{vtranslate('Export CSV', $MODULE)}</span></button>*}
    </div>
    {elseif $smarty.request.view eq 'ViewName'}
    {/if}
{/strip}