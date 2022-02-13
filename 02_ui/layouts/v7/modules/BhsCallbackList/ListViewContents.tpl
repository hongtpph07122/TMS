<div class="contents tabbable clearfix tabCallBack">
	<ul class="nav nav-tabs layoutTabs massEditTabs">
		<li class="tab-item taxesTab active"><a data-toggle="tab" href="#assigned"><strong> {vtranslate('LBL_ASSIGNED', $MODULE)}</strong></a></li>
		<li class="tab-item chargesTab"><a data-toggle="tab" href="#unassigned"><strong>{vtranslate('LBL_UNASSIGNEDCALLBACKS', $MODULE)}</strong></a></li>
	</ul>
</div>
<div class="tab-content layoutContent overflowVisible marginTop10px">
	<div class="tab-pane active" id="assigned">
		<div class="table-responsive">
			<table id="tblMailList0" class="table table-bordered nowrap custom-dt" style="width:100%"
				   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getAssignedList"
				   data-columnDefs='{$COLUMN_DEFS}'>
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
	</div>
	<div class="tab-pane" id="unassigned">
		<div class="table-responsive">
			<table id="tblMailList1" class="table table-bordered nowrap custom-dt" style="width:100%"
				   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getUnassignedList&list_type=unassigned"
				   data-columnDefs='{$COLUMN_DEFS_UNASSIGN}'>
				<thead>
				<tr>
					{include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$UNASSIGNHEADERS MODULE=$MODULE}
				</tr>
				<tr class="bhs-filter">
					{include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$UNASSIGNHEADERS MODULE=$MODULE}
				</tr>
				</thead>
				<tbody></tbody>
			</table>

		</div>
	</div>
