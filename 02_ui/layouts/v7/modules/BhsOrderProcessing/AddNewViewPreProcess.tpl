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
    <div class="listViewPageDiv content-area full-width" id="listViewContent">
        <div class="row" style="padding: 0px 10px 10px 10px !important;">
            <div class="col-sm-12 col-xs-12 module-action-bar clearfix " style="padding-left: 0px;">
                <div class="module-action-content clearfix BhsCallStrategy-module-action-content">
                    <div class="col-lg-8 col-md-8 module-breadcrumb module-breadcrumb-List transitionsAllHalfSecond">
                        <h4 class="module-title pull-left"> {vtranslate('LBL_ADD_TITLE', $MODULE)} </h4>
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
                    <li class="breadcrumb-item active" aria-current="page">{vtranslate('LBL_DIR_THIRD_ADD', $MODULE)}</li>
                </ol>
            </nav>
            <div class="contactProcess-action-right  pull-right">
                <div>
                    <button class="btn  btnSaveSO btnSave " ><i class="fa fa-save"></i> Save </button>
                </div>
            </div>
        </div>

