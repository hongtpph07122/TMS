<div class="contents tabbable clearfix tabCallBack">
	<ul class="nav nav-tabs layoutTabs massEditTabs" style="margin-right: 15px">
		<li class="tab-item taxesTab active"><a data-toggle="tab" href="#assigned"><strong> {vtranslate('LBL_ASSIGNED', $MODULE)}</strong></a></li>
		<li class="tab-item chargesTab"><a data-toggle="tab" href="#unassigned"><strong>{vtranslate('LBL_UNASSIGNED', $MODULE)}</strong></a></li>
		<div class="button-action-right mdi-format-float-right">
			<button class="btn-reassign btn btn-success">{vtranslate('LBL_REASSIGN', $MODULE)}</button>
			<button class="btn-unassigned btn btn-primary">{vtranslate('LBL_UNASSIGN', $MODULE)}</button>
			<button class="btn-delete btn btn-danger ">{vtranslate('LBL_DELETE', $MODULE)}</button>
		</div>
	</ul>

	<div class="tab-content layoutContent overflowVisible border1px marginTop10px">
		<div class="tab-pane active" id="assigned">
			<div class="table-responsive">
				<table id="tblMailList0" class="table table-bordered custom-dt" style="width:100%"
					   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getAssigned"
					   data-columnDefs='{$COLUMN_DEFS}'>
					<thead>
						<tr>
							<th><input class="check-all" type="checkbox"></th>
							{include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
						</tr>
						<tr class="bhs-filter">
							<th></th>
							{include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>

			</div>

		</div>
		<div class="tab-pane" id="unassigned">
			<div class="table-responsive">
				<table id="tblMailList1" class="table table-bordered custom-dt" style="width:100%"
					   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList&list_type=unassigned"
					   data-columnDefs='{$COLUMN_DEFS}'>
					<thead>
					<tr>
						<th><input class="check-all" type="checkbox"></th>
						{include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
					</tr>
					<tr class="bhs-filter">
						<th></th>
						{include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
					</tr>
					</thead>
					<tbody>
					</tbody>
				</table>

			</div>

		</div>

	</div>
</div>
