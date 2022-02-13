{strip}
    <div class='modal-dialog modal-lg modal-dialog-product'>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                <h3>{vtranslate('LBL_PRODUCT_DETAIL', $MODULE)}</h3>
            </div>
            <div class="modal-body">
                <form action="POST" id="frmProductDetail">
                    <div class="row form-group">
                        <div class="col-md-2"><label>{vtranslate('LBL_CODE', $MODULE)}</label></div>
                        <div class="col-md-4">{$RECORD->code}</div>
                        <div class="col-md-2"><label>{vtranslate('LBL_NAME', $MODULE)}</label></div>
                        <div class="col-md-4">{$RECORD->name}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-2"><label>{vtranslate('LBL_STATUS', $MODULE)}</label></div>
                        <div class="col-md-4">
                            <button id="btnToggleStatus" type="button" class="btn btn-sm btn-toggle {if $RECORD->status eq 1}active{/if} disabled" data-toggle="button" aria-pressed="true" autocomplete="off">
                                <div class="handle"></div>
                            </button>
                        </div>
                        <div class="col-md-2"><label>{vtranslate('LBL_CATEGORY', $MODULE)}</label></div>
                        <div class="col-md-4">{$RECORD->category}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-2"><label>{vtranslate('LBL_PRICE', $MODULE)}</label></div>
                        <div class="col-md-10">{$RECORD->price}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6"><label>{vtranslate('LBL_DSCR', $MODULE)}</label></div>
                        <div class="col-md-6"><label>{vtranslate('LBL_DSCRFORAGENT', $MODULE)}</label></div>
                        <div class="col-md-6">{$RECORD->dscr}</div>
                        <div class="col-md-6">{$RECORD->dscrForAgent}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-2"><label>{vtranslate('LBL_MODIFYBY', $MODULE)}</label></div>
                        <div class="col-md-4">{$RECORD->modifyby}</div>
                        <div class="col-md-2"><label>{vtranslate('LBL_MODIFYDATE', $MODULE)}</label></div>
                        <div class="col-md-4">{$RECORD->modifydate}</div>
                    </div>
                </form>
            </div>
            <div class="modal-footer text-right">
                <button class="btn btn-default" data-dismiss="modal">{vtranslate('LBL_CLOSE', 'Vtiger')}</button>
            </div>
        </div>
    </div>
{/strip}