<div  style="margin: 0px 30px;">
	<div class="row">
		<div class="col-md-6 col-sm-6 marginBottom15px inline">
			<button id="btnGetOrders" type="submit" class="btn btn-blue" style="min-width: 140px;">{vtranslate('LBL_GETORDERS', $MODULE)}</button>
		</div>
	</div>
	<table id="tblMailList0" class="table table-bordered dataTable custom-dt" style="width:100%"
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

