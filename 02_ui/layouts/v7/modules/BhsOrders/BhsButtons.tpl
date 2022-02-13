{strip}
    {assign var=userTMS value=Vtiger_Session::get('agent_info')}
    {if $smarty.request.view eq 'List'}
        {if !(!empty($userTMS) && $userTMS->roleId[0] eq $ROLE_AGENT && $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID)}
        <div class="mx-auto" style="display:flex">
            {assign var=userTMS value=Vtiger_Session::get('agent_info')}
            <button type="button" class="btn module-buttons btn-bhs btnExportExcel {if ($userTMS->roleId[0] eq $ROLE_AGENT)} hidden {/if}"><i class="fa fa-download" aria-hidden="true" style="color:white"></i><span class="hidden-sm hidden-xs">{vtranslate('Export CSV', $MODULE)}</span></button>
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
        {/if}
    {elseif $smarty.request.view eq 'ViewName'}
    {/if}
{/strip}
