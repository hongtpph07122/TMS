{strip}
<div class='modal-dialog modal-lg'>
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                <span aria-hidden="true" class='fa fa-close'></span>
            </button>
            <h3>{vtranslate('LBL_INBOUND', $MODULE)}</h3>
        </div>
        <div class="modal-body">
            <table id="tableAddInbound" class="display table table-striped table-bordered nowrap custom-dt table-hover" style="width:100%">
                <thead>
                <tr>
                    <th>{vtranslate('LBL_PRODUCT', $MODULE)}</th>
                    <th>{vtranslate('LBL_WAREHOUSENAME', $MODULE)}</th>
                    <th>{vtranslate('LBL_QTY')}</th>
                    <th><a id="BtnAddInbound" style="color:#fff" href="javascript: void(0);" class="btn btn-success">{vtranslate('LBL_ADDROW', $MODULE)}</a></th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="modal-footer text-right">
            <button class="btn btn-danger" data-dismiss="modal">{vtranslate('LBL_CLOSE')}</button>
        </div>
    </div>
   
</div>

{/strip}
<style>
.modal-header h3{
    margin-top: 14px;
}
</style>
<script>
$(document).ready(function() {
    var t = $('#tableAddInbound').DataTable({
        "lengthChange": false,
        'sDom': "<'row'<'col-sm-12'tr>><'row'<'col-sm-6'l><'col-sm-1'i><'col-sm-5'p>>",
    });
    var counter = 1;
    var product = "<select id='productId' class='select2'>{foreach from=$PRODUCTS item=pro}      <option   value='{$pro->prodId}'>   {$pro->name}</option>    {/foreach}    </select> ";
    var stock = "<select id='pnId' class='select2'>{foreach from=$STOCKS item=stock}      <option   value='{$stock->pnId}'>   {$stock->name}</option>    {/foreach}    </select> ";
        $('#BtnAddInbound').on('click', function() {
            t.row.add([
                product,
                stock,
                '<input type="text" class="quantity marginLeftZero inputElement "> ',
                '<td><a style="font-size:20px;" class="btnsave" href="javascript: void(0);"><i class="fa fa-floppy-o" aria-hidden="true"></i></a></td>',
            ]).draw(false);
            counter++;
            $("#tableAddInbound select").select2();
        });
        // Automatically add a first row of data
        $('#BtnAddInbound').click();
        
    });

</script>