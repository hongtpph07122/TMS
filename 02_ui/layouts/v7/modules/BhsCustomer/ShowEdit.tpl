{strip}
    <div class='modal-dialog modal-lg'>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                <h3>{vtranslate('LBL_CREATE_CUSTOMER', $MODULE)}</h3>
            </div>
            <div class="modal-body">
                <form action="POST" id="frmEditCustomer">
                    <input type="hidden" name="module" value="{$MODULE}" />
                    <input type="hidden" name="action" value="ActionAjax" />
                    <input type="hidden" name="mode" value="saveCustomer" />
                    <input type="hidden" name="recordId" value="" />
                    <div class="row form-group">
                        <div class="col-md-4"><label>{vtranslate('LBL_PRODUCT_NAME', $MODULE)}</label></div>
                        <div class="col-md-8"><input name="product_name" class="inputElement"></div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>{vtranslate('LBL_CUSTOMER_NAME', $MODULE)}</label></div>
                        <div class="col-md-8"><input name="customer_name" class="inputElement"></div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>{vtranslate('LBL_PHONE_NO', $MODULE)}</label></div>
                        <div class="col-md-8"><input name="phone_no" class="inputElement"></div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>{vtranslate('LBL_EMAIL', $MODULE)}</label></div>
                        <div class="col-md-8"><input name="email" type="email" class="inputElement"></div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>{vtranslate('LBL_ADDRESS', $MODULE)}</label></div>
                        <div class="col-md-8"><textarea name="address" rows="5" style="resize: vertical; height: auto" class="inputElement"></textarea></div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4"><label>{vtranslate('LBL_DESCRIPTION', $MODULE)}</label></div>
                        <div class="col-md-8"><textarea name="description" rows="5" style="resize: vertical; height: auto" class="inputElement"></textarea></div>
                    </div>
                </form>
            </div>
            <div class="modal-footer text-right">
                <button class="btn btn-blue" id="btnSaveCustomer">{vtranslate('LBL_SAVE', 'Vtiger')}</button>
                <button class="btn btn-default" data-dismiss="modal">{vtranslate('LBL_CANCEL', 'Vtiger')}</button>
            </div>
        </div>
    </div>
{/strip}