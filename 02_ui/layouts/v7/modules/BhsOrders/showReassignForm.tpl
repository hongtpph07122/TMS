{strip}
    <div class='modal-dialog'>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                <h3>{vtranslate('EDIT_AGENT', $MODULE)}</h3>
            </div>
            <div class="modal-body table-responsive">
                <div class="form-group">
                    <label class="control-label col-sm-2" for="select-agent">{vtranslate('SELECT_AGENT', $MODULE)}:</label>
                    <div class="col-sm-10">
                        <select class="select2" id="selected-agent" style="width: 100%">
                            <option value="">{vtranslate('SELECT_AGENT', $MODULE)}</option>
                            {foreach item=AGENT from=$AGENT_LIST }
                            <option value="{$AGENT->userId}">{$AGENT->fullname}</option>
                            {/foreach}
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <div class="checkbox">
                            <label><input type="checkbox" id="add-to-order-list"> &nbsp;{vtranslate('ADD_TO_ORDER_LIST', $MODULE)}</label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button id="assign-agent" class="btn btn-primary">{vtranslate('SAVE', $MODULE)}</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
{/strip}