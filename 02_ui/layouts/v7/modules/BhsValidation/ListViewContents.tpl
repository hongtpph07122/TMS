<div  style="margin: 0px 15px;">
	<div class="row">
		<div class="col-md-2 col-sm-3">
			<select class="select2" style="width: 100%" id="statusValidate">
				<option value="">{vtranslate('LBL_SELECTSTATUSVALIDATE', $MODULE)}</option>
				{foreach from=$STATUS  item=status}
					<option value="{$status->id}">{$status->name}</option>
				{/foreach}
			</select>
		</div>
		<div class="col-md-6 col-sm-6 marginBottom15px inline">
			<button id="btnValidationValidate" type="submit" class="btn btn-blue" style="min-width: 140px;">{vtranslate('LBL_SUBMIT', $MODULE)}</button>
			{assign var=userTMS value=Vtiger_Session::get('agent_info')}
			{if !(!empty($userTMS) && $userTMS->roleId[0] eq $ROLE_VALIDATION && $userTMS->info->orgId eq $NORI_CUSTOMER_ORGID)}
				<button id="btnAddNewOrder" type="submit" class="btn btn-blue" style="min-width: 140px;">{vtranslate('LBL_CREATE_ORDER', $MODULE)}</button>
			{/if}
		</div>
	</div>
	<table id="tblMailList0" class="table table-bordered custom-dt"
		   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList " data-columnDefs='{$COLUMN_DEFS}'>
		<thead>
				<tr>
					{include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
				</tr>
				<tr class="bhs-filter">
					{include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
				</tr>
		</thead>
		<tbody></tbody>
	</table>
</div>