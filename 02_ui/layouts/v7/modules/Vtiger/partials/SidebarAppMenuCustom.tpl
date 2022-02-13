{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
************************************************************************************}

<aside class="left-sidebar hidden-lg hidden-md" id="parent">
    <div class="user-panel">
        <div class="image hide">
            <span class="link-text-xs-only hidden-lg hidden-md hidden-sm">{$USER_MODEL->get('first_name')} {$USER_MODEL->get('last_name')} ({$USER_MODEL->get('user_name')})</span>
        </div>
    </div>
    <!-- Sidebar scroll-->

    <!-- search mobile-->
    <div class="col-xs-12 visible-xs" id="searchmobile">
        <div class="search-links-container">
            <div class="search-link">
                <span class="ti-search" aria-hidden="true"></span>
                <input class="mobile-search-key keyword-input" type="text" placeholder="{vtranslate('LBL_TYPE_SEARCH')}" value="{$GLOBAL_SEARCH_VALUE}">
            </div>
        </div>
    </div>

    <!--/ search mobile-->

    <div class="clearfix"></div>

    <div class="scroll-sidebar ps ps--active-x ps--active-y">

        <!-- Sidebar navigation-->
        <nav class="sidebar-nav active" style="padding-bottom: 50px">
            <ul id="sidebarnav" class="in">
                <li class="sidebar-logo " style="height: 20px">
                    {* {assign var=userTMS value=Vtiger_Session::get('agent_info')}
                    {if $userTMS->theme->logoPath}
                    <img src="{$userTMS->theme->logoPath}" alt="{$userTMS->theme->company}"/>
                    {else}
                    <img src="{$COMPANY_LOGO->get('imagepath')}" alt="{$COMPANY_LOGO->get('alt')}"/>
                    {/if} *}
                    {*<img src="layouts/v7/resources/Images/LogoEcommerce.png" alt=""/>*}
                </li>
                <li class="nav-small-cap hide">APPS</li>
                <!-- <li class="active"> <a class="has-arrow waves-effect waves-dark" href="#" aria-expanded="false"><i class="material-icons">dashboard</i><span class="hide-menu">Dashboard <span class="label label-rouded label-themecolor pull-right">4</span></span></a>
                    <ul aria-expanded="true" class="collapse in">
                        <li class="active"><a href="index.html" class="active"><i class="fa fa-dashboard"></i> Minimal </a></li>
                        <li><a href="index2.html">Analytical</a></li>
                        <li><a href="index3.html">Demographical</a></li>
                        <li><a href="index4.html">Modern</a></li>
                    </ul>
                </li>
                -->
                {assign var=userTMS value=Vtiger_Session::get('agent_info')}
                {assign var=USER_PRIVILEGES_MODEL value=Users_Privileges_Model::getCurrentUserPrivilegesModel()}
                {assign var=HOME_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Home')}
                {assign var=DASHBOARD_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Dashboard')}

                    {if $USER_PRIVILEGES_MODEL->hasModulePermission($DASHBOARD_MODULE_MODEL->getId())}
                        <li class=""  title="{vtranslate('LBL_DASHBOARD',$MODULE)}">
                            <a class=" waves-effect waves-dark" href="{$HOME_MODULE_MODEL->getDefaultUrl()}" >
                                <i class="fa fa-pie-chart"></i>
                                <span class="hide-menu" style="text-transform: uppercase"> {vtranslate('LBL_DASHBOARD',$MODULE)}</span>
                            </a>
                        </li>
                    {/if}
                    {assign var=APP_GROUPED_MENU value=Settings_MenuEditor_Module_Model::getAllVisibleModules()}
                    {assign var=APP_LIST value=Vtiger_MenuStructure_Model::getAppMenuList()}
                    {foreach item=APP_NAME from=$APP_LIST}
                        {if !empty($userTMS) && $userTMS->roleId[0] eq $ROLE_TEAM_LEADER && $APP_NAME eq 'BHS_PRODUCT' && $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID} {continue}{/if}
                        {if !empty($userTMS) && $userTMS->roleId[0] eq $ROLE_AGENT && $APP_NAME eq 'BHS_REPORTS' && $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID} {continue}{/if}
                        {if !empty($userTMS) && $userTMS->roleId[0] eq $ROLE_VALIDATION && $APP_NAME eq 'BHS_CUSTOMER' && $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID} {continue}{/if}
                        {if $APP_NAME eq 'ANALYTICS'} {continue}{/if}
                        {if count($APP_GROUPED_MENU.$APP_NAME) gt 0}
                            {assign var=APP_LIST_MENU value=array_keys($APP_GROUPED_MENU[$APP_NAME])}
                            <li class="with-childs{if in_array($MODULE,$APP_LIST_MENU) } active-menu{/if}">
                                {foreach item=APP_MENU_MODEL from=$APP_GROUPED_MENU.$APP_NAME}
                                    {assign var=FIRST_MENU_MODEL value=$APP_MENU_MODEL}
                                    {if $APP_MENU_MODEL}
                                        {break}
                                    {/if}
                                {/foreach}
                                <a class="has-arrow waves-effect waves-dark" href="#" aria-expanded="false" title='{vtranslate("LBL_$APP_NAME")}'>
                                    {if  {$APP_IMAGE_MAP.$APP_NAME} == 'assignment'}
                                        <i class="material-icons module-icon">{$APP_IMAGE_MAP.$APP_NAME} </i>
                                    {else}
                                        <i class="app-icon-list  {$APP_IMAGE_MAP.$APP_NAME}"></i>
                                    {/if}
                                    <span class="hide-menu">{vtranslate("LBL_$APP_NAME")}</span>
                                </a>
                                <ul aria-expanded="false" class="collapse" style="padding-top: 4px; padding-left: 0px;">
                                    {foreach item=moduleModel key=moduleName from=$APP_GROUPED_MENU[$APP_NAME]}
                                        {assign var='translatedModuleLabel' value=vtranslate($moduleName,$moduleName )}
                                        {if !empty($userTMS) && $moduleName eq 'BhsOrders' && $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID}
                                            {assign var='translatedModuleLabel' value=vtranslate('BhsOrdersNori',$moduleName )}
                                        {/if}
                                        {if !empty($userTMS) && $userTMS->roleId[0] eq $ROLE_TEAM_LEADER && $moduleName eq 'BhsCampaign' && $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID} {continue}{/if}
                                        {if (in_array(moduleName, ['BhsReportLeadAndPostBack','BhsReportDelivery','BhsReportMarketingSummary','BhsReportCampaignByCreateDate','BhsReportCampaignByUpdateDate','BhsReportCampaignByAgent']))}
                                            <li class="{if $MODULE == $moduleModel->getName() }active-menu{/if}">
                                                <a class="waves-effect waves-dark" href="http://27.71.226.210:9102/" title="{$translatedModuleLabel}">
                                                    <i class="material-icons module-icon">{$APP_IMAGE_MAP.{$moduleModel->getName()}}</i>
                                                    <span class="hide-menu"> {$translatedModuleLabel}</span>
                                                </a>
                                            </li>
                                        {else}
                                            <li class="{if $MODULE == $moduleModel->getName() }active-menu{/if}">
                                                <a class="waves-effect waves-dark" href="{$moduleModel->getDefaultUrl()}&app={$APP_NAME}" title="{$translatedModuleLabel}">
                                                    <i class="material-icons module-icon">{$APP_IMAGE_MAP.{$moduleModel->getName()}}</i>
                                                    <span class="hide-menu"> {$translatedModuleLabel}</span>
                                                </a>
                                            </li>
                                        {/if}
                                    {/foreach}

                                </ul>
                            </li>
                        {/if}
                    {/foreach}

                    {if false}
                        {assign var=MAILMANAGER_MODULE_MODEL value=Vtiger_Module_Model::getInstance('MailManager')}
                        {if $USER_PRIVILEGES_MODEL->hasModulePermission($MAILMANAGER_MODULE_MODEL->getId())}
                            <li class="">
                                <a class=" waves-effect waves-dark" href="index.php?module=MailManager&view=List">
                                    <i class="app-icon-list fa">{$MAILMANAGER_MODULE_MODEL->getModuleIcon()}</i>
                                    <span class="hide-menu" style="text-transform: uppercase"> {vtranslate('MailManager')}</span>
                                </a>
                            </li>
                        {/if}
                        {assign var=DOCUMENTS_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Documents')}
                        {if $USER_PRIVILEGES_MODEL->hasModulePermission($DOCUMENTS_MODULE_MODEL->getId())}
                            <li class="">
                                <a class=" waves-effect waves-dark" href="index.php?module=Documents&view=List">
                                    <i class="app-icon-list fa">{$DOCUMENTS_MODULE_MODEL->getModuleIcon()}</i>
                                    <span class="hide-menu" style="text-transform: uppercase"> {vtranslate('Documents')}</span>
                                </a>
                            </li>
                        {/if}
                        {if $USER_MODEL->isAdminUser()}
                            {if vtlib_isModuleActive('ExtensionStore')}
                                <li class="">
                                    <a class=" waves-effect waves-dark" href="index.php?module=ExtensionStore&parent=Settings&view=ExtensionStore">
                                        <i class="app-icon-list fa fa-shopping-cart"></i>
                                        <span class="hide-menu" style="text-transform: uppercase"> {vtranslate('LBL_EXTENSION_STORE', 'Settings:Vtiger')}</span>
                                    </a>
                                </li>
                            {/if}
                        {/if}
                    {/if}

                    {if $USER_MODEL->isAdminUser()}
                        <li class="with-childs">
                            <a class="has-arrow waves-effect waves-dark " href="#" aria-expanded="false">
                                <span class="app-icon-list fa fa-cog"></span>
                                <span class="hide-menu">{vtranslate('LBL_SETTINGS', 'Settings:Vtiger')}</span>
                            </a>
                            <ul aria-expanded="false" class="collapse " style="padding-top: 4px; padding-left: 0px;">
                                <li>
                                    <a class="waves-effect waves-dark " href="?module=Vtiger&parent=Settings&view=Index">
                                        <span class="fa fa-cog module-icon"></span>
                                        <span class="hide-menu"> {vtranslate('LBL_CRM_SETTINGS','Vtiger')}</span>
                                    </a>
                                </li>
                                <li>
                                    <a class="waves-effect waves-dark " href="?module=Users&parent=Settings&view=List">
                                        <span class="fa fa-user module-icon"></span>
                                        <span class="hide-menu"> {vtranslate('LBL_MANAGE_USERS','Vtiger')}</span>
                                    </a>
                                </li>
                            </ul>

                        </li>
                {/if}
                {assign var=userTMS value=Vtiger_Session::get('agent_info')}
                {if $userTMS }
                <input type="hidden" id="user_info" value='{json_encode($userTMS)}'>
                {/if}

                {* <li class="">
                    <a id="menubar_item_right_LBL_SIGN_OUT" href="index.php?module=Users&action=Logout">
                        <span class="fa fa-power-off"></span>
                        <span class="hide-menu" style="text-transform: uppercase">{vtranslate('LBL_SIGN_OUT')}</span>
                    </a>
                </li> *}
            </ul>
        </nav>
        <div class="ps__rail-x" style="width: 0px; left: 0px; bottom: 0px;">
            <div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div>
        </div>
        <div class="ps__rail-y" style="top: 0px; height: 591px; right: 0px;">
            <div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 421px;"></div>
        </div>
        <div class="ps__rail-x" style="width: 0px; left: 0px; bottom: 0px;">
            <div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div>
        </div>
        <div class="ps__rail-y" style="top: 0px; height: 591px; right: 0px;">
            <div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 421px;"></div>
        </div>
    </div>
</aside>

