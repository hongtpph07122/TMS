{strip}
<script type="text/javascript">
{if $data.owner}
   $('#owner').val({$data.owner_id}).trigger('change');
$('#status').val({$data.status}).trigger('change');
{/if}


</script>
    <div class='modal-dialog modal-lg'>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                <h3>{vtranslate('CAMPAIGN', $MODULE)}</h3>
            </div>
            <div class="modal-body">
                <form action="POST" id="frmEditCampaign">
                    <input type="hidden" name="module" value="{$MODULE}" />
                    <input type="hidden" name="action" value="ActionAjax" />
                    <input type="hidden" name="mode" value="saveCampaign" />
                    <input type="hidden" name="cpId" value="{$data.cpId}" />
                    <div class="row form-group">
                        <div class="col-md-4"><label>Name </label></div>
                        <div class="col-md-8"><input name="name" class="inputElement"  value="{$data.name}" ></div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>Owner</label></div>
                        <div class="col-md-8">
                            <select id="owner" name="owner" class="inputElement select2" >
                                <option value="">-- Select agent --</option>
                                 {foreach from=$AGENT_OPTIONS key=AGENT_KEY item=AGENT_NAME}
                                    <option value="{$AGENT_KEY}">{$AGENT_NAME}</option>
                                {/foreach}
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>Status</label></div>
                        <div class="col-md-8">
                            <select name="status" id="status" class="inputElement select2">
                           
                                <option value="1">Status One</option>
                                <option value="2">Status Two</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>Start Date</label></div>
                        <div class="col-md-8">
                            <div class="input-group inputElement">
                                <input name="startdate" class="dateField form-control " value='{$data.startdate}'>
                                <span class="input-group-addon"><i class="fa fa-calendar "></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>Stop Date</label></div>
                        <div class="col-md-8">
                            <div class="input-group inputElement">
                                <input name="stopdate" class="dateField form-control " value='{$data.stopdate}'>
                                <span class="input-group-addon"><i class="fa fa-calendar "></i></span>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer text-right">
                <button class="btn btn-blue" id="btnSaveCampaign">{vtranslate('LBL_SAVE', 'Vtiger')}</button>
                <button class="btn btn-default" data-dismiss="modal">{vtranslate('LBL_CANCEL', 'Vtiger')}</button>
            </div>
        </div>
    </div>
{/strip}