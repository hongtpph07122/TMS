{strip}
    <div class='modal-dialog ' style="min-width: 70%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span aria-hidden="true" class='fa fa-close'></span>
                </button>
                {if empty($RECORD)}
                <h3>{vtranslate('LBL_ADD_NEW_PRODUCT', $MODULE)}</h3>
                {else}
                    <h3>{vtranslate('LBL_EDIT_PRODUCT', $MODULE)}</h3>
                {/if}
            </div>
            <div class="modal-body">
            
                <form action="POST" id="frmEditProduct">
                    <input type="hidden" id="hfRecordId" value="{$RECORD_ID}">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group row">
                                <label for="ProductName" class="col-sm-3 col-form-label">{vtranslate('LBL_PRODUCTNAME', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <input type="text" {if !empty($RECORD->name)}readonly{/if} class="inputElement" name="name" value="{$RECORD->name}" id="ProductName" placeholder="Enter name">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="form-group row">
                                <label for="ProductCategory" class="col-sm-3 col-form-label">{vtranslate('LBL_PRODUCTCATEGORY', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <input type="text" class="inputElement" name="category" value="{$RECORD->category}" id="ProductCategory" placeholder="{vtranslate('Enter category', $MODULE)}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12"></div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group row">
                                <label for="DescriptionProduct" class="col-sm-3 col-form-label">{vtranslate('LBL_DESCRIPTION', $MODULE)}</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" id="txtDescriptionProduct" name="dscr" rows="3">{$RECORD->dscr}</textarea>
                                </div>
                            </div>
                        </div>
                        </div>
                        <div class="row">
                        <div class="col-md-12">
                            <div class="form-group row">
                                <label for="AgentScriptProduct" class="col-sm-3 col-form-label">{vtranslate('LBL_AGENTSCRIPT', $MODULE)}</label>
                                <div class="col-md-9">
                                    <textarea class="form-control" id="txtAgentScriptProduct" name="dscrForAgent" rows="3">{$RECORD->dscrForAgent}</textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group row">
                                <label for="PriceList" class="col-sm-3 col-form-label">{vtranslate('LBL_PRICELIST', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <input type="text" readonly class="inputElement" name="price" value="{$RECORD->price}" id="txtPriceList" placeholder="Enter Price">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="FileAttachment" class="col-sm-3 col-form-label">{vtranslate('LBL_FILEATTACHMENT', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <a href="" style="font-size: 30px;"><i class="fa fa-upload" aria-hidden="true"></i></a>
                                    <input type="text" readonly class="inputElement" id="FileAttachment" hidden>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="ListExtension" class="col-sm-3 col-form-label">{vtranslate('LBL_LISTEXTENSION', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <input type="text" readonly class="inputElement" id="ListExtension" placeholder="Only enter Number">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="btnToggleStatus" class="col-sm-3 col-form-label">{vtranslate('LBL_STATUS', $MODULE)}</label>
                                <div class="col-sm-9">
                                    <button id="btnToggleStatus" type="button" class="btn btn-sm btn-toggle {if $RECORD->status eq 1}active{/if}" data-toggle="button" aria-pressed="true" autocomplete="off">
                                        <div class="handle"></div>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer text-right">
                <button class="btn btn-blue" id="btnSaveProduct">{vtranslate('LBL_SAVE', 'Vtiger')}</button>
                <button class="btn btn-default" data-dismiss="modal">{vtranslate('LBL_CANCEL', 'Vtiger')}</button>
            </div>
        </div>
    </div>
{/strip}