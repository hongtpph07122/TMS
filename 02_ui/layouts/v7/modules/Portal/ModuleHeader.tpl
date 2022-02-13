{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
*************************************************************************************}

{strip}
	<div class="col-sm-12 col-xs-12 module-action-bar clearfix coloredBorderTop">
		<div class="module-action-content clearfix">
			<div class="col-lg-7 col-md-7 module-breadcrumb">
				<span>
					{assign var=MODULE_MODEL value=Vtiger_Module_Model::getInstance($MODULE)}
					{assign var=DEFAULT_FILTER_ID value=$MODULE_MODEL->getDefaultCustomFilter()}
					{if $DEFAULT_FILTER_ID}
						{assign var=CVURL value="&viewname="|cat:$DEFAULT_FILTER_ID}
						{assign var=DEFAULT_FILTER_URL value=$MODULE_MODEL->getListViewUrl()|cat:$CVURL}
					{else}
						{assign var=DEFAULT_FILTER_URL value=$MODULE_MODEL->getListViewUrlWithAllFilter()}
					{/if}
					<a title="{vtranslate($MODULE, $MODULE)}" href='{$DEFAULT_FILTER_URL}'><h4 class="module-title pull-left text-uppercase">&nbsp;{vtranslate($MODULE, $MODULE)}&nbsp;</h4></a>
				</span>
				<span>
					<p class="current-filter-name pull-left">&nbsp;<span class="fa fa-angle-right" aria-hidden="true"></span>&nbsp;{$VIEW}&nbsp;</p>
				</span>
				{if $VIEWID}
					{foreach item=FILTER_TYPES from=$CUSTOM_VIEWS}
						{foreach item=FILTERS from=$FILTER_TYPES}
							{if $FILTERS->get('cvid') eq $VIEWID}
								{assign var=CVNAME value=$FILTERS->get('viewname')}
								{break}
							{/if}
						{/foreach}
					{/foreach}
					<span>
						<p class="current-filter-name filter-name pull-left"><span class="fa fa-angle-right" aria-hidden="true"></span>&nbsp;{$CVNAME}&nbsp;</p>
					</span>
				{/if}
			</div>
			<div class="col-lg-5 col-md-5 pull-right">
				<div id="appnav" class="navbar-right">
					<ul class="nav navbar-nav">
						{assign var=BASIC_ACTION value=$MODULE_BASIC_ACTIONS}
						<li>
							<button class="btn btn-default addBookmark addButton module-buttons" >
								<i class="fa fa-plus"></i>&nbsp;&nbsp;{vtranslate('LBL_ADD_BOOKMARK',$MODULE)}</i>
							</button>
						</li>
					</ul>
				</div>
			</div>
		</div>
		{if $FIELDS_INFO neq null}
			<script type="text/javascript">
				var uimeta = (function () {
					var fieldInfo = {$FIELDS_INFO};
					return {
						field: {
							get: function (name, property) {
								if (name && property === undefined) {
									return fieldInfo[name];
								}
								if (name && property) {
									return fieldInfo[name][property]
								}
							},
							isMandatory: function (name) {
								if (fieldInfo[name]) {
									return fieldInfo[name].mandatory;
								}
								return false;
							},
							getType: function (name) {
								if (fieldInfo[name]) {
									return fieldInfo[name].type
								}
								return false;
							}
						}
					};
				})();
			</script>
		{/if}
	</div> 
{/strip}



<div class="app-menu hide" id="app-menu">
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-2 col-xs-2 cursorPointer app-switcher-container">
				<div class="row app-navigator">
					<span id="menu-toggle-action" class="app-icon fa fa-bars"></span>
				</div>
			</div>
		</div>
		{assign var=USER_PRIVILEGES_MODEL value=Users_Privileges_Model::getCurrentUserPrivilegesModel()}
		{assign var=HOME_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Home')}
		{assign var=DASHBOARD_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Dashboard')}
		<div class="app-list row">
			{if $USER_PRIVILEGES_MODEL->hasModulePermission($DASHBOARD_MODULE_MODEL->getId())}
				<div class="menu-item app-item dropdown-toggle" data-default-url="{$HOME_MODULE_MODEL->getDefaultUrl()}">
					<div class="menu-items-wrapper">
						<span class="app-icon-list fa fa-dashboard"></span>
						<span class="app-name textOverflowEllipsis"> {vtranslate('LBL_DASHBOARD',$MODULE)}</span>
					</div>
				</div>
			{/if}
			{assign var=APP_GROUPED_MENU value=Settings_MenuEditor_Module_Model::getAllVisibleModules()}
			{assign var=APP_LIST value=Vtiger_MenuStructure_Model::getAppMenuList()}
			{foreach item=APP_NAME from=$APP_LIST}
				{if $APP_NAME eq 'ANALYTICS'} {continue}{/if}
				{if count($APP_GROUPED_MENU.$APP_NAME) gt 0}
					<div class="dropdown app-modules-dropdown-container">
						{foreach item=APP_MENU_MODEL from=$APP_GROUPED_MENU.$APP_NAME}
							{assign var=FIRST_MENU_MODEL value=$APP_MENU_MODEL}
							{if $APP_MENU_MODEL}
								{break}
							{/if}
						{/foreach}
						<div class="menu-item app-item dropdown-toggle app-item-color-{$APP_NAME}" data-app-name="{$APP_NAME}" id="{$APP_NAME}_modules_dropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" data-default-url="{$FIRST_MENU_MODEL->getDefaultUrl()}&app={$APP_NAME}">
							<div class="menu-items-wrapper app-menu-items-wrapper">
								<span class="app-icon-list fa {$APP_IMAGE_MAP.$APP_NAME}"></span>
								<span class="app-name textOverflowEllipsis"> {vtranslate("LBL_$APP_NAME")}</span>
								<span class="fa fa-chevron-right pull-right"></span>
							</div>
						</div>
						<ul class="dropdown-menu app-modules-dropdown" aria-labelledby="{$APP_NAME}_modules_dropdownMenu">
							{foreach item=moduleModel key=moduleName from=$APP_GROUPED_MENU[$APP_NAME]}
								{assign var='translatedModuleLabel' value=vtranslate($moduleModel->get('label'),$moduleName )}
								<li>
									<a href="{$moduleModel->getDefaultUrl()}&app={$APP_NAME}" title="{$translatedModuleLabel}">
										<span class="module-icon">{$moduleModel->getModuleIcon()}</span>
										<span class="module-name textOverflowEllipsis"> {$translatedModuleLabel}</span>
									</a>
								</li>
							{/foreach}
						</ul>
					</div>
				{/if}
			{/foreach}
			<div class="app-list-divider"></div>
			{if false}
				{assign var=MAILMANAGER_MODULE_MODEL value=Vtiger_Module_Model::getInstance('MailManager')}
				{if $USER_PRIVILEGES_MODEL->hasModulePermission($MAILMANAGER_MODULE_MODEL->getId())}
					<div class="menu-item app-item app-item-misc" data-default-url="index.php?module=MailManager&view=List">
						<div class="menu-items-wrapper">
							<span class="app-icon-list fa">{$MAILMANAGER_MODULE_MODEL->getModuleIcon()}</span>
							<span class="app-name textOverflowEllipsis"> {vtranslate('MailManager')}</span>
						</div>
					</div>
				{/if}
				{assign var=DOCUMENTS_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Documents')}
				{if $USER_PRIVILEGES_MODEL->hasModulePermission($DOCUMENTS_MODULE_MODEL->getId())}
					<div class="menu-item app-item app-item-misc" data-default-url="index.php?module=Documents&view=List">
						<div class="menu-items-wrapper">
							<span class="app-icon-list fa">{$DOCUMENTS_MODULE_MODEL->getModuleIcon()}</span>
							<span class="app-name textOverflowEllipsis"> {vtranslate('Documents')}</span>
						</div>
					</div>
				{/if}
				{if $USER_MODEL->isAdminUser()}
					{if vtlib_isModuleActive('ExtensionStore')}
						<div class="menu-item app-item app-item-misc" data-default-url="index.php?module=ExtensionStore&parent=Settings&view=ExtensionStore">
							<div class="menu-items-wrapper">
								<span class=""></span>
								<span class="app-name textOverflowEllipsis"> {vtranslate('LBL_EXTENSION_STORE', 'Settings:Vtiger')}</span>
							</div>
						</div>
					{/if}
				{/if}
			{/if}
			{if $USER_MODEL->isAdminUser()}
				<div class="dropdown app-modules-dropdown-container dropdown-compact">
					<div class="menu-item app-item dropdown-toggle app-item-misc" data-app-name="TOOLS" id="TOOLS_modules_dropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" data-default-url="{if $USER_MODEL->isAdminUser()}index.php?module=Vtiger&parent=Settings&view=Index{else}index.php?module=Users&view=Settings{/if}">
						<div class="menu-items-wrapper app-menu-items-wrapper">
							<span class="app-icon-list fa fa-cog"></span>
							<span class="app-name textOverflowEllipsis"> {vtranslate('LBL_SETTINGS', 'Settings:Vtiger')}</span>
							{if $USER_MODEL->isAdminUser()}
								<span class="fa fa-chevron-right pull-right"></span>
							{/if}
						</div>
					</div>
					<ul class="dropdown-menu app-modules-dropdown dropdown-modules-compact" aria-labelledby="{$APP_NAME}_modules_dropdownMenu" data-height="0.27">
						<li>
							<a href="?module=Vtiger&parent=Settings&view=Index">
								<span class="fa fa-cog module-icon"></span>
								<span class="module-name textOverflowEllipsis"> {vtranslate('LBL_CRM_SETTINGS','Vtiger')}</span>
							</a>
						</li>
						<li>
							<a href="?module=Users&parent=Settings&view=List">
								<span class="fa fa-user module-icon"></span>
								<span class="module-name textOverflowEllipsis"> {vtranslate('LBL_MANAGE_USERS','Vtiger')}</span>
							</a>
						</li>
					</ul>
				</div>
			{/if}
		</div>
	</div>
</div>
