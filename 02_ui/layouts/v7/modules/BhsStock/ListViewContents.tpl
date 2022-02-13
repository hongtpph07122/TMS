<div  style="margin: 0px 15px;">
	<table id="tblMailList0" class="table table-bordered nowrap custom-dt" style="width:100%"
		   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList" data-columnDefs='{$COLUMN_DEFS}'>
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