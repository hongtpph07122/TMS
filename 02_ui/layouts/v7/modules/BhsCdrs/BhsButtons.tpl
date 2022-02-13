{strip}
    {if $smarty.request.view eq 'List'}
        <div class="mx-auto" style="display:flex">
{*     <button id="btnDeleteCDRs" type="button" class="btn module-buttons btn-bhs"><i class="material-icons">delete</i><span class="hidden-sm hidden-xs">{vtranslate('Delete', $MODULE)}</span></button>*}
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
    </div>
    {elseif $smarty.request.view eq 'ViewName'}
    {/if}
{/strip}