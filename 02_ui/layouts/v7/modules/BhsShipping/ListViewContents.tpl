<div  style="margin: 0px 30px;">
{if $ROLE == 14}
	<div class="row">
		<div class="col-md-2 col-sm-3 marginBottom15px">
			<select class="select2" style="width: 100%" id="statusValidate">
				<option value="">{vtranslate('LBL_SELECTSTATUS', $MODULE)}</option>
				{foreach from=$STATUS  item=status}
					<option value="{$status->id}">{$status->name}</option>
				{/foreach}
			</select>
		</div>
		<div class="col-md-6 col-sm-6 marginBottom15px inline">
			<button id="btnSave" type="submit" class="btn btn-blue" style="min-width: 140px;">{vtranslate('LBL_SAVE', $MODULE)}</button>
		</div>
	</div>
{/if}
	<div class="tabbable padding15px marginBottom25px box-importExcel hidden">
		<form enctype="multipart/form-data" name="formUpload" id="form-import-excel" action="/index.php?module=BhsOrders&amp;action=ActionAjax&amp;mode=uploadExcel">
			<div class="importexcel">
				<input type="file" class="form-control" placeholder="import excel" name="file" accept=".xls,.xlsx">
				<button id="btnImport" type="button" class="btn btn-blue marginTop10px" >{vtranslate('LBL_IMPORT', $MODULE)}</button>
			</div>
		</form>
		<input type="hidden" id="API_ENDPOINT" value="{$API_ENDPOINT}">
	</div>

	<table id="tblMailList0" class="table table-bordered nowrap custom-dt" style="width:100%"
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

