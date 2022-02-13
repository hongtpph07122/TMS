{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
************************************************************************************}
{* modules/Vtiger/views/List.php *}

{* START YOUR IMPLEMENTATION FROM BELOW. Use {debug} for information *}
<div  style="margin: 0px 15px;">
	
	<div class="table-responsive">
		<table id="tblMailList0" class="table table-bordered nowrap custom-dt" style="width:100%"
			   data-url="index.php?module=BhsCampaign&action=ActionAjax&mode=getList"
			   data-columnDefs='{$COLUMN_DEFS}'>
			<thead>
				<tr>
					{include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
					<th>Action</th>
				</tr>
				<tr class="bhs-filter">
					{include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
					<th></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</div>
