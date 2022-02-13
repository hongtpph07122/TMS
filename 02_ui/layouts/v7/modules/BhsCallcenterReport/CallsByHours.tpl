<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getCallsByHours"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
            <tr>
                <th>{vtranslate('LBL_', $MODULE)}</th>
            </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    <div class="btnExportExcel">
        <a class="act" href="javascript: void(0);" title="Export to Excel"><i class="fa fa-download" aria-hidden="true"></i>Export to Excel</a>
        <a class="pull-right" href="index.php?module={$MODULE}&view=List" title="Back"><i class="fa fa-left"></i>Back</a>
    </div>
</div>