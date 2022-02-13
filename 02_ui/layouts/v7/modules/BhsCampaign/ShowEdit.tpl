{strip}
    <div class='modal-dialog modal-lg'>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                <h3>{vtranslate('LBL_EDIT_CAMPAIGN', $MODULE)}</h3>
            </div>
            <div class="modal-body">
                <form action="POST" id="frmEditCampaign">
                    <input type="hidden" name="module" value="{$MODULE}" />
                    <input type="hidden" name="action" value="ActionAjax" />
                    <input type="hidden" name="mode" value="saveCampaign" />
                    <input type="hidden" name="recordId" value="" />
                    <div class="row form-group">
                        <div class="col-md-4"><label>Field1</label></div>
                        <div class="col-md-8"><input name="field1" class="inputElement"></div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>Field1</label></div>
                        <div class="col-md-8">
                            <select name="field2" class="inputElement select2">
                                <option value=""></option>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>Field1</label></div>
                        <div class="col-md-8">
                            <select name="field2" class="inputElement select2" multiple>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>Field1</label></div>
                        <div class="col-md-8">
                            <div class="input-group inputElement">
                                <input name="field4" class="dateField form-control ">
                                <span class="input-group-addon"><i class="fa fa-calendar "></i></span>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer text-right">
                <button class="btn btn-success" id="btnSaveCampaign">{vtranslate('LBL_SAVE', 'Vtiger')}</button>
                <button class="btn btn-default" data-dismiss="modal">{vtranslate('LBL_CANCEL', 'Vtiger')}</button>
            </div>
        </div>
    </div>
{/strip}