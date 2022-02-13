{strip}
    {if $smarty.request.view eq 'List'}
      <div class="mx-auto" style="display: flex;margin-right: 15px;">
       <button id="btnAddCustomer" type="button" class="btn module-buttons btn-bhs btnBhsUpdate"><i class="material-icons">add</i><span class="hidden-sm hidden-xs">{vtranslate('LBL_NEW_CAMPAIN', $MODULE)}</span></button>
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

       
        {* <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="material-icons">save_alt</i><span class="hidden-sm hidden-xs">{vtranslate('Export', $MODULE)}</span></button>
        <button type="button" class="btn module-buttons btn-bhs btnImportExcel"><i class="material-icons">import_export</i><span class="hidden-sm hidden-xs">{vtranslate('Import', $MODULE)}</span></button> *}
    {elseif $smarty.request.view eq 'ViewName'}
    {/if}
{/strip}