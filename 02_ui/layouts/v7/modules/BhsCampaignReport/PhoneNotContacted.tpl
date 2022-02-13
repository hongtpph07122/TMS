<div  style="margin: 0px 30px;">
	<div class="table-responsive">
		<table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
			   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getPhoneNotContacted"
			   data-columnDefs='{$COLUMN_DEFS}'>
			<thead>
				<tr>
					<th>{vtranslate('LBL_NAME', $MODULE)}</th>
					<th>{vtranslate('LBL_PHONE_NUMBER', $MODULE)}</th>
					<th>{vtranslate('LBL_ASSIGN', $MODULE)}</th>
					<th>{vtranslate('LBL_DIALTYPE', $MODULE)}</th>
					<th>{vtranslate('LBL_CREATE_TIME', $MODULE)}</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
	
</div>