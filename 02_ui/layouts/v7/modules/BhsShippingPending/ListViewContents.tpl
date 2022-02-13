<div  style="margin: 0px 15px;">
<div class="row">
		<div class="col-md-2 col-sm-3 bulk_actions">
			<select class="select2 status" style="width: 100%" id="statusResent">
				<option value="">{vtranslate('LBL_SELECTSTATUS', $MODULE)}</option>
					<option value="out_stock">{vtranslate('LBL_ResentOutstock', $MODULE)}</option>
					<option value="remove">{vtranslate('LBL_Remove', $MODULE)}</option>
			</select>
		</div>
		<div class="col-md-6 col-sm-6  marginBottom15px inline">
			<button id="btnCommit" type="submit" class="btn btn-blue btnCommit" style="min-width: 140px;">{vtranslate('LBL_COMMIT', $MODULE)}</button>
		</div>
	</div>
	<table id="tblMailList0" class="table table-bordered nowrap custom-dt" style="width:100%"
		   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList" data-columnDefs='{$COLUMN_DEFS}'>
		<thead>
			<tr>
{*				<th><input class="check-all" type="checkbox"></th>*}
				{include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
			</tr>
			<tr class="bhs-filter">
				{include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	
</div>