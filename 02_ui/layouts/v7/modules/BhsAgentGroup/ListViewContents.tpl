<div class="col-md-12">
	<div class="row">
{*		<form action="index.php?module=BhsAgentGroup&view=List&app=BHS_SYSTEM">*}
		<div class="col-md-6 col-sm-9 marginBottom15px">
			<input name="fullname" class="inputElement searchFullName" id="searchFullName" value="{$SEARCH_FULLNAME}" placeholder="{vtranslate('LBL_SEARCH_AGENT', $MODULE)}">
		</div>
		<div class="col-md-1 col-sm-3 marginBottom15px">
{*			<button id="btnFindCustomer" class="btn btn-blue" style="min-width: 120px; max-width:100%;"><i class="fa fa-search"></i>&nbsp;{vtranslate('LBL_FIND', $MODULE)}</button>*}
		</div>
			<input type="hidden" name="module" value="BhsAgentGroup" />
			<input type="hidden" name="view" value="List" />
			<input type="hidden" name="app" value="BHS_SYSTEM" />
			<input type="hidden" name="groupId" value="{$GROUPID}" />
{*		</form>*}
	</div>

	<div class="row" style="">
		<!-- .ListOfGroup -->
		<div class="col-md-4 col-lg-3">
			<div class="box box_list_agent_groups">
				<h3 class="header-single-content marginBottom10px marginTop15px">
					{vtranslate('LBL_AGENTGROUP_BLOCK', 'BhsAgentGroup')}
				</h3>
				<div style="max-height:389px; overflow-y: scroll;">
				<table class="table table-striped table-bordered dataTable nowrap custom-dt tableLstGroup" style="width:100%">
					<tbody>
					<tr class="row-item row-item-active" data-id="-2">
						<td>{vtranslate('LBL_ALL_GROUP', 'BhsAgentGroup')}</td>
					</tr>
					{foreach $AGENT_GROUPS as $AGENT_GROUP}
						<tr class="row-item" data-id="{$AGENT_GROUP->groupId}">
							<td>{$AGENT_GROUP->name}</td>
						</tr>
					{/foreach}
					<tr class="row-item" data-id="-1">
						<td>{vtranslate('LBL_NONE_GROUP', 'BhsAgentGroup')}</td>
					</tr>
					</tbody>
				</table>
				</div>
			</div>
		</div>
		<!-- .ListOfAgent -->
		<div class="col-md-8 col-lg-9">
			<div class="box">
				<h3 class="header-single-content marginBottom10px marginTop15px">
					{vtranslate('LBL_AGENT_BLOCK', 'BhsAgentGroup')} 
					<button id="addAgent" type="submit" class="btn addAgent pull-right"> + {vtranslate('LBL_ADD_AGENT', $MODULE)}</button>
				</h3>
				<table id="tblMailList0" class="table table-striped table-bordered nowrap custom-dt" style="width:100%"
						data-url="index.php?module=BhsAgentGroup&action=ActionAjax&mode=getGroupDetail&groupId={$GROUPID}{if !empty($SEARCH_FULLNAME)}&fullname={$SEARCH_FULLNAME}{/if}"
				data-page-length='25'>
					<thead>
					<tr>
						<th class="hidden"></th>
						<th>{vtranslate('LBL_BHSAGENTGROUP_ID', 'BhsAgentGroup')}</th>
						<th>{vtranslate('LBL_BHSAGENTGROUP_agentName', 'BhsAgentGroup')}</th>
						<th>{vtranslate('LBL_BHSAGENTGROUP_group', 'BhsAgentGroup')}</th>
						<th>{vtranslate('LBL_BHSAGENTGROUP_levelskill', 'BhsAgentGroup')}</th>
						<th> {vtranslate('LBL_ACTION', 'Vtiger')}</th>
					</tr>
					</thead>
					<tbody>
					</tbody>
					<tfoot style="border-top: 1px solid #ddd;">
						<tr role="row" class="cloneRow">
							<td class="hidden">
								<input type="hidden" class="btnHiddenGroupId" value="{$GROUPID}" />
								<input type="hidden" class="btnHiddenGrAgId" value="0" />
							</td>
							<td></td>
                            <td class="text-left listAgent">
								<select class="cbbInlineAgent inputElement">
									<option value="">{vtranslate('LBL_SELECT_AGENT', 'BhsAgentGroup')}</option>
									{foreach $AGENTS as $AGENT}
										<option value="{$AGENT->userId}">{$AGENT->fullname}</option>
									{/foreach}
								</select>
                            </td>
                            <td class="text-left">
                                <select class="cbbInlineGroup inputElement">
                                    <option value="">{vtranslate('LBL_SELECT_GROUP', 'BhsAgentGroup')}</option>
                                    {foreach $AGENT_GROUPS as $AGENT_GROUP}
                                    <option value="{$AGENT_GROUP->groupId}">{$AGENT_GROUP->name}</option>
                                    {/foreach}
                                </select>
                            </td>
                            <td>
                                <input value="1" type="number" min="1" oninput="this.value = this.value.replace(/[^0-9.]/g, ''); this.value = this.value.replace(/(\..*)\./g, '$1');" class="txtInlineSkillLeve inputElement" />
                            </td>
                            <td style="">
                                <a class="btnInline btnInlineSave clone"><i class="fa fa-check"></i></a>
                                <a class="btnInline btnInlineCancelAdd clone"><i class="fa fa-trash"></i></a>
                                <a class="btnInline btnInlineEdit clone" style="display: none;"><i class="fa fa-pencil"></i></a>
                                <a class="btnInline btnInlineRemove clone" style="display: none;"><i class="fa fa-trash"></i></a>
                            </td>
                        </tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
</div>
