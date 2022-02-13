{strip}
    {if $smarty.request.view eq 'List'}

    {elseif $smarty.request.view eq 'DialledListsDetails'}
        <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="material-icons">save_alt</i><span class="hidden-sm hidden-xs">{vtranslate('Export', $MODULE)}</span></button>
        <a href="index.php?module={$MODULE}&view=List&app={$SELECTED_MENU_CATEGORY}" class="btn module-buttons btn-bhs btnExport"><i class="material-icons">undo</i><span class="hidden-sm hidden-xs">{vtranslate('Back', $moduleName)}</span></a>
    {elseif $smarty.request.view eq 'DialledListsSummary'}
        <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="material-icons">save_alt</i><span class="hidden-sm hidden-xs">{vtranslate('Export', $MODULE)}</span></button>
        <a href="index.php?module={$MODULE}&view=List&app={$SELECTED_MENU_CATEGORY}" class="btn module-buttons btn-bhs btnExport"><i class="material-icons">undo</i><span class="hidden-sm hidden-xs">{vtranslate('Back', $moduleName)}</span></a>  
    {elseif $smarty.request.view eq 'CallDuration'}
        <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="material-icons">save_alt</i><span class="hidden-sm hidden-xs">{vtranslate('Export', $MODULE)}</span></button>
        <a href="index.php?module={$MODULE}&view=List&app={$SELECTED_MENU_CATEGORY}" class="btn module-buttons btn-bhs btnExport"><i class="material-icons">undo</i><span class="hidden-sm hidden-xs">{vtranslate('Back', $moduleName)}</span></a>    
    {elseif $smarty.request.view eq 'CallSummaryByDate'}
        <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="material-icons">save_alt</i><span class="hidden-sm hidden-xs">{vtranslate('Export', $MODULE)}</span></button>
        <a href="index.php?module={$MODULE}&view=List&app={$SELECTED_MENU_CATEGORY}" class="btn module-buttons btn-bhs btnExport"><i class="material-icons">undo</i><span class="hidden-sm hidden-xs">{vtranslate('Back', $moduleName)}</span></a>              
    {elseif $smarty.request.view eq 'CustomerAdded'}
        <button type="button" class="btn module-buttons btn-bhs btnExportExcel"><i class="material-icons">save_alt</i><span class="hidden-sm hidden-xs">{vtranslate('Export', $MODULE)}</span></button>
        <a href="index.php?module={$MODULE}&view=List&app={$SELECTED_MENU_CATEGORY}" class="btn module-buttons btn-bhs btnExport"><i class="material-icons">undo</i><span class="hidden-sm hidden-xs">{vtranslate('Back', $moduleName)}</span></a>                  
    {/if}
{/strip}