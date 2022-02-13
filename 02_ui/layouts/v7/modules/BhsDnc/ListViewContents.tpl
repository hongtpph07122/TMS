<div  style="margin: 0px 30px;">
	<div class="row">
		<div class="table-responsive">
			<table id="tblMailList0" class="table table-striped table-bordered nowrap custom-dt" style="width:100%"
				   data-url="index.php?module=BhsDnc&action=ActionAjax&mode=getList"
				   data-columnDefs='{$COLUMN_DEFS}'>
				<thead>
					<tr>
						{include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
						<th> {vtranslate('LBL_BHSDNC_Togle', 'BhsDnc')}</th>
					</tr>
					<tr class="bhs-filter">
						{include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
						<th></th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<button id="" type="submit" class="btn btn-success" style="min-width: 140px;">Submit</button>
		</div>
	</div>
</div>
