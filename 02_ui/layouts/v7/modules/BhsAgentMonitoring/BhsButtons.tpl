{strip}
    {if $smarty.request.view eq 'List'}
        <div class="mx-auto" style="display:flex">               
        <button type="button" class="btn module-buttons btn-bhs btnCreateUser"><i class="fa fa-user-plus" aria-hidden="true" style="color:white"></i><span class="hidden-sm hidden-xs">New User</span></button>
        <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="fa fa-download" aria-hidden="true" style="color:white"></i><span class="hidden-sm hidden-xs">{vtranslate('Export CSV', $MODULE)}</span></button>
        <button type="button" class="btn module-buttons btn-bhs btn-filter-header filter"
              data-open="{vtranslate('OPEN_FILTER', $MODULE)}" data-close="{vtranslate('CLOSE_FILTER', $MODULE)}">
          <i class="material-icons">filter_list</i>
          <span class="hidden-sm hidden-xs">
            {vtranslate('OPEN_FILTER', $MODULE)}
         </span>
        </button>
        <button type="button" class="btn module-buttons btn-bhs filter btn-clear-filter">
            <i class="material-icons">block</i>
            <span class="hidden-sm hidden-xs">
                {vtranslate('CLEAR_FILTER_TABLE', $MODULE)}
            </span>
        </button>
        <div style="margin-top: 10px;font-size: 17px;margin-left: 10px">
            <span style="font-weight: bold;">Quota:&nbsp;</span><span id="totalUser">2</span>/<span id="totalQuota">4</span>
        </div> 
    </div>
    {elseif $smarty.request.view eq 'ViewName'}
    {/if}
{/strip}