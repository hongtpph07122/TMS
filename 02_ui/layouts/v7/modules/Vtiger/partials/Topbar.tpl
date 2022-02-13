{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
************************************************************************************}

{strip}
	{include file="modules/Vtiger/Header.tpl"}

	{assign var=APP_IMAGE_MAP value=Vtiger_MenuStructure_Model::getAppIcons()}
	<nav class="navbar navbar-default navbar-fixed-top app-fixed-navbar">
	<div class="container-fluid global-nav">
		<div class="row">
			<div class="col-lg-3 col-md-5 col-sm-4 col-xs-8 paddingRight0 app-navigator-container">
				<div class="row">
					<div id="appnavigator" class="col-sm-2 col-xs-2 cursorPointer app-switcher-container" data-app-class="{if $MODULE eq 'Home' || !$MODULE}fa-dashboard{else}{$APP_IMAGE_MAP[$SELECTED_MENU_CATEGORY]}{/if}">
						<div class="row app-navigator">
							<img src="layouts/v7/resources/Images/icon/icon-menu.png" alt="">
						</div>
					</div>
{*					<div class="logo-container col-lg-9 col-md-9 col-sm-9 col-xs-9">*}
{*						<div class="row">*}
{*							<a href="index.php" class="company-logo">*}
{*								<img src="{$COMPANY_LOGO->get('imagepath')}" alt="{$COMPANY_LOGO->get('alt')}"/>*}
{*							</a>*}
{*						</div>*}
{*					</div>*}
				</div>
			</div>
{*			<div class="search-links-container col-md-3 col-lg-3 hidden-sm">*}
{*				<div class="search-link hidden-xs">*}
{*					<span class="fa fa-search" aria-hidden="true"></span>*}
{*					<input class="keyword-input" type="text" placeholder="{vtranslate('LBL_TYPE_SEARCH')}" value="{$GLOBAL_SEARCH_VALUE}">*}
{*					<span id="adv-search" class="adv-search fa fa-chevron-circle-down pull-right cursorPointer" aria-hidden="true"></span>*}
{*				</div>*}
{*			</div>*}
			<div id="navbar" class="col-sm-6 col-md-3 col-lg-3 collapse navbar-collapse navbar-right global-actions">
				<ul class="nav navbar-nav">
					<li style="display: none">
						<div class="search-links-container hidden-sm">
							<div class="search-link hidden-xs">
								<span class="ti-search" aria-hidden="true"></span>
								<input class="keyword-input" type="text" placeholder="{vtranslate('LBL_TYPE_SEARCH')}" value="{$GLOBAL_SEARCH_VALUE}">
								<span id="adv-search" title="Advanced Search" class="adv-search ti-arrow-circle-down pull-right cursorPointer" aria-hidden="true"></span>
							</div>
						</div>
					</li>
					<!-- <li>
						<div class="dropdown gmailHeader">
							<button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<span class="icon-header">
									<i class="fa fa-envelope-o" aria-hidden="true"></i>
								</span>
{*								<span class="number-notifi">*}
{*								</span>*}
							</button>
							<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
								<a class="dropdown-item" href="#">Action</a>
								<a class="dropdown-item" href="#">Another action</a>
								<a class="dropdown-item" href="#">Something else here</a>
							</div>
						</div>
					</li>
					<li>
						<div class="dropdown notificationHeader">
							<button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<span class="icon-header">
									<i class="fa fa-bell-o" aria-hidden="true"></i>
								</span>
{*								<span class="number-notifi">*}
{*								</span>*}
							</button>
							<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
								<a class="dropdown-item" href="#">Action</a>
								<a class="dropdown-item" href="#">Another action</a>
								<a class="dropdown-item" href="#">Something else here</a>
							</div>
						</div>
					</li> -->
{*					<li>*}
{*						<div class="dropdown">*}
{*							<div class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">*}
{*								<a href="#" id="menubar_quickCreate" class="qc-button" title="{vtranslate('LBL_QUICK_CREATE',$MODULE)}" aria-hidden="true">*}
{*									<i class="material-icons">add</i>*}
{*								</a>*}
{*							</div>*}
{*							<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" style="width:500px;">*}
{*								<li class="title" style="padding: 5px 0 0 15px;">*}
{*									<strong>{vtranslate('LBL_QUICK_CREATE',$MODULE)}</strong>*}
{*								</li>*}
{*								<hr/>*}
{*								<li id="quickCreateModules" style="padding: 0 5px;">*}
{*									<div class="col-lg-12" style="padding-bottom:15px;">*}
{*										{foreach key=moduleName item=moduleModel from=$QUICK_CREATE_MODULES}*}
{*											{if $moduleModel->isPermitted('CreateView') || $moduleModel->isPermitted('EditView')}*}
{*												{assign var='quickCreateModule' value=$moduleModel->isQuickCreateSupported()}*}
{*												{assign var='singularLabel' value=$moduleModel->getSingularLabelKey()}*}
{*												{assign var=hideDiv value={!$moduleModel->isPermitted('CreateView') && $moduleModel->isPermitted('EditView')}}*}
{*												{if $quickCreateModule == '1'}*}
{*													{if $count % 3 == 0}*}
{*														<div class="row">*}
{*													{/if}*}
{*													*}{* Adding two links,Event and Task if module is Calendar *}
{*													{if $singularLabel == 'SINGLE_Calendar'}*}
{*														{assign var='singularLabel' value='LBL_TASK'}*}
{*														<div class="{if $hideDiv}create_restricted_{$moduleModel->getName()} hide{else}col-lg-4{/if}">*}
{*															<a id="menubar_quickCreate_Events" class="quickCreateModule" data-name="Events"*}
{*															   data-url="index.php?module=Events&view=QuickCreateAjax" href="javascript:void(0)">{$moduleModel->getModuleIcon('Event')}<span class="quick-create-module">{vtranslate('LBL_EVENT',$moduleName)}</span></a>*}
{*														</div>*}
{*														{if $count % 3 == 2}*}
{*															</div>*}
{*															<br>*}
{*															<div class="row">*}
{*														{/if}*}
{*														<div class="{if $hideDiv}create_restricted_{$moduleModel->getName()} hide{else}col-lg-4{/if}">*}
{*															<a id="menubar_quickCreate_{$moduleModel->getName()}" class="quickCreateModule" data-name="{$moduleModel->getName()}"*}
{*															   data-url="{$moduleModel->getQuickCreateUrl()}" href="javascript:void(0)">{$moduleModel->getModuleIcon('Task')}<span class="quick-create-module">{vtranslate($singularLabel,$moduleName)}</span></a>*}
{*														</div>*}
{*														{if !$hideDiv}*}
{*															{assign var='count' value=$count+1}*}
{*														{/if}*}
{*													{else if $singularLabel == 'SINGLE_Documents'}*}
{*														<div class="{if $hideDiv}create_restricted_{$moduleModel->getName()} hide{else}col-lg-4{/if} dropdown">*}
{*															<a id="menubar_quickCreate_{$moduleModel->getName()}" class="quickCreateModuleSubmenu dropdown-toggle" data-name="{$moduleModel->getName()}" data-toggle="dropdown"*}
{*															   data-url="{$moduleModel->getQuickCreateUrl()}" href="javascript:void(0)">*}
{*																{$moduleModel->getModuleIcon()}*}
{*																<span class="quick-create-module">*}
{*																			{vtranslate($singularLabel,$moduleName)}*}
{*																			<i class="fa fa-caret-down quickcreateMoreDropdownAction"></i>*}
{*																		</span>*}
{*															</a>*}
{*															<ul class="dropdown-menu quickcreateMoreDropdown" aria-labelledby="menubar_quickCreate_{$moduleModel->getName()}">*}
{*																<li class="dropdown-header"><i class="fa fa-upload"></i> {vtranslate('LBL_FILE_UPLOAD', $moduleName)}</li>*}
{*																<li id="VtigerAction">*}
{*																	<a href="javascript:Documents_Index_Js.uploadTo('Vtiger')">*}
{*																		<img style="  margin-top: -3px;margin-right: 4%;" title="Vtiger" alt="Vtiger" src="layouts/v7/skins//images/Vtiger.png">*}
{*																		{vtranslate('LBL_TO_SERVICE', $moduleName, {vtranslate('LBL_VTIGER', $moduleName)})}*}
{*																	</a>*}
{*																</li>*}
{*																<li class="dropdown-header"><i class="fa fa-link"></i> {vtranslate('LBL_LINK_EXTERNAL_DOCUMENT', $moduleName)}</li>*}
{*																<li id="shareDocument"><a href="javascript:Documents_Index_Js.createDocument('E')">&nbsp;<i class="fa fa-external-link"></i>&nbsp;&nbsp; {vtranslate('LBL_FROM_SERVICE', $moduleName, {vtranslate('LBL_FILE_URL', $moduleName)})}</a></li>*}
{*																<li role="separator" class="divider"></li>*}
{*																<li id="createDocument"><a href="javascript:Documents_Index_Js.createDocument('W')"><i class="fa fa-file-text"></i> {vtranslate('LBL_CREATE_NEW', $moduleName, {vtranslate('SINGLE_Documents', $moduleName)})}</a></li>*}
{*															</ul>*}
{*														</div>*}
{*													{else}*}
{*														<div class="{if $hideDiv}create_restricted_{$moduleModel->getName()} hide{else}col-lg-4{/if}">*}
{*															<a id="menubar_quickCreate_{$moduleModel->getName()}" class="quickCreateModule" data-name="{$moduleModel->getName()}"*}
{*															   data-url="{$moduleModel->getQuickCreateUrl()}" href="javascript:void(0)">*}
{*																{$moduleModel->getModuleIcon()}*}
{*																<span class="quick-create-module">{vtranslate($singularLabel,$moduleName)}</span>*}
{*															</a>*}
{*														</div>*}
{*													{/if}*}
{*													{if $count % 3 == 2}*}
{*														</div>*}
{*														<br>*}
{*													{/if}*}
{*													{if !$hideDiv}*}
{*														{assign var='count' value=$count+1}*}
{*													{/if}*}
{*												{/if}*}
{*											{/if}*}
{*										{/foreach}*}
{*									</div>*}
{*								</li>*}
{*							</ul>*}
{*						</div>*}
{*					</li>*}
					{assign var=USER_PRIVILEGES_MODEL value=Users_Privileges_Model::getCurrentUserPrivilegesModel()}
					{assign var=CALENDAR_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Calendar')}
					{if $USER_PRIVILEGES_MODEL->hasModulePermission($CALENDAR_MODULE_MODEL->getId())}
						<li><div><a href="index.php?module=Calendar&view={$CALENDAR_MODULE_MODEL->getDefaultViewName()}" title="{vtranslate('Calendar','Calendar')}" aria-hidden="true"><i class="material-icons">event</i></a></div></li>
					{/if}
					{assign var=REPORTS_MODULE_MODEL value=Vtiger_Module_Model::getInstance('Reports')}
					{if $USER_PRIVILEGES_MODEL->hasModulePermission($REPORTS_MODULE_MODEL->getId())}
						<li><div><a href="index.php?module=Reports&view=List" title="{vtranslate('Reports','Reports')}" aria-hidden="true"><i class="material-icons">show_chart</i></a></div></li>
					{/if}
					{if $USER_PRIVILEGES_MODEL->hasModulePermission($CALENDAR_MODULE_MODEL->getId())}
						<li><div><a href="#" class="taskManagement" title="{vtranslate('Tasks','Vtiger')}" aria-hidden="true"><i class="material-icons">card_travel</i></a></div></li>
					{/if}
					<li>
						<div>
							<div class="modal fade" id="modalChangePassForm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
							aria-hidden="true">
								<div class="modal-dialog" role="document">
									<div class="modal-content">
										<div class="modal-header text-center">
											<h4 class="modal-title w-100 font-weight-bold">Change Password</h4>
											<button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: -25px;">
											<span aria-hidden="true">&times;</span>
											</button>
										</div>
										<div class="modal-body mx-3">
											<div class="md-form mb-5" style="margin-bottom: 3%;">
											
											<label data-error="wrong" data-success="right" for="password-old" style="margin-top: 5px;float: left;">Old Password:</label>
											<input type="password" id="password-old" class="form-control validate" style="width: 72%;margin-left: 28%;">
											</div>

											<div class="md-form mb-5" style="margin-bottom: 3%;">
											
											<label data-error="wrong" data-success="right" for="password-new" style="margin-top: 5px;float: left;">New Password:</label>
											<input type="password" id="password-new" class="form-control validate" style="width: 72%;margin-left: 28%;">
											</div>

											<div class="md-form mb-5">
											
											<label data-error="wrong" data-success="right" for="password-confirm" style="margin-top: 5px;float: left;">Confirm New Password:</label>
											<input type="password" id="password-confirm" class="form-control validate" style="width: 72%;margin-left: 28%;">
											</div>
										</div>
										<div class="modal-footer d-flex justify-content-center">
											<button id="changepassword" class="btn btn-info">Change Password</button>
										</div>
									</div>
								</div>
							</div>
						</div>

					</li>
					<li class="dropdown" style="margin-top: -5px;">
						{assign var=userTMS value=Vtiger_Session::get('agent_info')}
						<div>
							<a href="#" class="userName dropdown-toggle" data-toggle="dropdown" role="button"  title="{$userTMS->info->fullname}
										  ({$userTMS->info->userName})">
{*								<i class="material-icons">perm_identity</i>*}
								<span class="avatar">
									{assign var=IMAGE_DETAILS value=$USER_MODEL->getAvataDetails()}
									{if $IMAGE_DETAILS neq '' && $IMAGE_DETAILS[0] neq '' && $IMAGE_DETAILS[0].path eq ''}
{*										<i class='vicon-vtigeruser'></i>*}
										<img src="layouts/v7/resources/Images/icon/avatar.png" alt="">
											{else}
												{foreach item=IMAGE_INFO from=$IMAGE_DETAILS}
										{if !empty($IMAGE_INFO.path) && !empty({$IMAGE_INFO.orgname})}
											<img src="{$IMAGE_INFO.path}_{$IMAGE_INFO.orgname}" width="100px" height="100px">
										{/if}
									{/foreach}
									{/if}
								</span>
								<span class="link-text-xs-only hidden-lg hidden-md hidden-sm">{$USER_MODEL->getName()}</span>
							</a>
							<div class="dropdown-menu logout-content" role="menu" style="min-width:470px;">
								<div class="row">
									<div class="col-lg-4 col-sm-4">
										<div class="profile-img-container">
											{assign var=IMAGE_DETAILS value=$USER_MODEL->getAvataDetails()}
											
											{if $IMAGE_DETAILS neq '' && $IMAGE_DETAILS[0] neq '' && $IMAGE_DETAILS[0].path eq ''}
												<i class='vicon-vtigeruser' style="font-size:90px"></i>
											{else}
												{foreach item=IMAGE_INFO from=$IMAGE_DETAILS}
													{if !empty($IMAGE_INFO.path) && !empty({$IMAGE_INFO.orgname})}
														<img src="{$IMAGE_INFO.path}_{$IMAGE_INFO.orgname}" width="100px" height="100px">
													{/if}
												{/foreach}
											{/if}
										</div>
									</div>
									<div class="col-lg-8 col-sm-8">
										<div class="profile-container">
											<h4>{$userTMS->info->fullname}</h4>
											<h5 class="textOverflowEllipsis" title='{$userTMS->info->userName}'>{$userTMS->info->userName}</h5>
											<!--<p>{$USER_MODEL->getUserRoleName()}</p> -->
										</div>
									</div>
								</div>
								<div class="logout-footer clearfix">
									<hr style="margin: 10px 0 !important">
									<div class="">
											<span class="pull-left">
												<span class="fa fa-cogs"></span>
												<a id="menubar_item_right_LBL_MY_PREFERENCES" href="{$USER_MODEL->getPreferenceDetailViewUrl()}">{vtranslate('LBL_MY_PREFERENCES')}</a>			
												<span class="fa fa-lock"></span>
												<a id="menubar_item_right_LBL_CHANGE_PASSWORD" href="" data-toggle="modal" data-target="#modalChangePassForm">{vtranslate('LBL_CHANGE_PASSWORD')}</a>
											</span>
										<span class="pull-right">
												<span class="fa fa-power-off"></span>
												<a id="menubar_item_right_LBL_SIGN_OUT" href="index.php?module=Users&action=Logout">{vtranslate('LBL_SIGN_OUT')}</a>
											</span>
									</div>
								</div>								
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
{/strip}