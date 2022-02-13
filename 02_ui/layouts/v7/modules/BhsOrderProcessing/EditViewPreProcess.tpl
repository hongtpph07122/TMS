{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
************************************************************************************}

{include file="modules/Vtiger/partials/Topbar.tpl"}

<div class="container-fluid app-nav">
	<div class="row">
		{include file="partials/SidebarHeader.tpl"|vtemplate_path:$MODULE}

	</div>
</div>
</nav>
<div id='overlayPageContent' class='fade modal overlayPageContent content-area overlay-container-60' tabindex='-1' role='dialog' aria-hidden='true'>
	<div class="data">
	</div>
	<div class="modal-dialog">
	</div>
</div>  
<div class="main-container main-container-{$MODULE}">
		{assign var=LEFTPANELHIDE value=$CURRENT_USER_MODEL->get('leftpanelhide')}
{*		<div id="modnavigator" class="module-nav">*}
{*			<div class="hidden-xs hidden-sm mod-switcher-container">*}
{*				{include file="partials/Menubar.tpl"|vtemplate_path:$MODULE}*}
{*			</div>*}
{*		</div>*}
	<div class="listViewPageDiv content-area full-width" id="listViewContent">
    <div class="row" style="padding: 0px 10px 10px 10px !important;">
            <div class="col-sm-12 col-xs-12 module-action-bar clearfix " style="padding-left: 0px;">
                <div class="module-action-content clearfix BhsCallStrategy-module-action-content">
                    <div class="col-lg-8 col-md-8 module-breadcrumb module-breadcrumb-List transitionsAllHalfSecond">
                        <h4 class="module-title pull-left">{vtranslate('LBL_EDIT_TITLE', $MODULE)}</h4>
                    </div>
                    <div class="col-lg-4 col-md-4 pull-right" style="margin:  0px; padding: 0px;">
                        <div id="appnav" class="pull-right">
                            <ul class="nav navbar-nav"></ul>
                        </div>
                    </div>
                </div>
            </div>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb " style="margin-bottom: 0">
                    <li class="breadcrumb-item"><a href="{$HOME_MODULE_MODEL->getDefaultUrl()}">{vtranslate('LBL_DIR_FIRST', $MODULE)}</a></li>
                    <li class="breadcrumb-item"><a href="index.php?module=BhsOrderProcessing&view=List&app=BHS_ORDERS">{vtranslate('LBL_DIR_SECOND', $MODULE)}</a></li>
                    <li class="breadcrumb-item active" aria-current="page">{vtranslate('LBL_DIR_THIRD', $MODULE)}</li>
                </ol>
            </nav>
        <div class="contactProcess-action-right  pull-right">
            {assign var=userTMS value=Vtiger_Session::get('agent_info')}

            {if $PROCESSING_TYPE == 'UpdateLead'}
                {if  !empty($userTMS) && ($userTMS->info->orgId eq $NORI_CUSTOMER_ORGID || $userTMS->roleId[0] neq $ROLE_AGENT || empty($GET_PARENT_URL)) }
                    <button type="button" class="btn  btn-bhs btnApproved" data-id="2" data-name="approved"><span class="hidden-sm hidden-xs">{vtranslate('LBL_APPROVED', $MODULE)}</span></button>
                    <button type="button" class="btn  btn-bhs btnUncalled" data-id="3" data-name="uncalled"><span class="hidden-sm hidden-xs">{vtranslate('Uncalled', $MODULE)}</span></button>
                    <button type="button" class="btn  btn-bhs btnRejected" data-id="4" data-name="rejected"><span class="hidden-sm hidden-xs">{vtranslate('LBL_REJECTED', $MODULE)}</span></button>
                    <button type="button" class="btn  btn-bhs btnTrash" data-id="5" data-name="trash"><span class="hidden-sm hidden-xs">{vtranslate('LBL_TRASH', $MODULE)}</span></button>
                    <div class="button-action">
                        <div class="select_reason hidden">
                            <span class=""  style="display: flex;text-align: center;vertical-align: middle;">{vtranslate('LBL_YOU_HAVE_CHOSEN', $MODULE)}: <div class="alert alert-red" style="margin-left: 20px;padding: 2px 10px;margin-bottom: 13px;margin-top: -4px;">{vtranslate('LBL_REJECTED', $MODULE)} </div></span>
                            <div class="list_reason" ></div>
                        </div>
                    </div>
                {/if}
            {/if}

            {if $LEAD_TYPE == 'order'}
                {if  !(!empty($userTMS)) }
                    <button type="button" class="btn  btn-bhs btnApproved" data-id="2" data-name="approved"><span class="hidden-sm hidden-xs">{vtranslate('LBL_APPROVED', $MODULE)}</span></button>
                    <button type="button" class="btn  btn-bhs btnUncalled" data-id="3" data-name="uncalled"><span class="hidden-sm hidden-xs">{vtranslate('Uncalled', $MODULE)}</span></button>
                    <button type="button" class="btn  btn-bhs btnRejected" data-id="4" data-name="rejected"><span class="hidden-sm hidden-xs">{vtranslate('LBL_REJECTED', $MODULE)}</span></button>
                    <button type="button" class="btn  btn-bhs btnTrash" data-id="5" data-name="trash"><span class="hidden-sm hidden-xs">{vtranslate('LBL_TRASH', $MODULE)}</span></button>
                    <div class="button-action">
                        <div class="select_reason hidden">
                            <span class=""  style="display: flex;text-align: center;vertical-align: middle;">{vtranslate('LBL_YOU_HAVE_CHOSEN', $MODULE)}: <div class="alert alert-red" style="margin-left: 20px;padding: 2px 10px;margin-bottom: 13px;margin-top: -4px;">{vtranslate('LBL_REJECTED', $MODULE)} </div></span>
                            <div class="list_reason" ></div>
                        </div>
                    </div>
                {/if}
            {/if}
            <div>
                <button  class="btn  btnCanel hidden"> {vtranslate('LBL_CANCEL', $MODULE)}</button>
                {if $PROCESSING_TYPE == 'UpdateLead'}
                    {if  !empty($userTMS) && ($userTMS->info->orgId eq $NORI_CUSTOMER_ORGID || $userTMS->roleId[0] neq $ROLE_AGENT || empty($GET_PARENT_URL)) }
                        <button  class="btn  btnCallback"> {vtranslate('LBL_CALLBACK', $MODULE)}</button>
                    {/if}
                {/if}

                {if $LEAD_TYPE == 'order'}
                    {if  !(!empty($userTMS)) }
                        <button  class="btn  btnCallback"> {vtranslate('LBL_CALLBACK', $MODULE)}</button>
                    {/if}
                {/if}

                <button class="btn  btnSaveSO btnSave" data-return-url = "{$RETURN_URL}" {if (empty($GET_PARENT_URL))} disabled="disabled" {/if} style="{if ($LEAD_TYPE == 'order' && $userTMS->roleId[0] eq $ROLE_AGENT)} display:none {/if}" data-type = {$TYPE}>
                    <i class="fa fa-save"></i>
                    {vtranslate('LBL_SAVE', $MODULE)}
                </button>
            </div>
        </div>
</div>
        
