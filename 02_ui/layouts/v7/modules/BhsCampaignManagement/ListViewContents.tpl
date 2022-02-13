<div class="col-md-12 table-responsive">
    <table id="tblMailList0" class="table table-bordered custom-dt" style="width:100%"
           data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList"
           data-columnDefs='{$COLUMN_DEFS}'>
        <thead>
            <tr>
                {include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
            </tr>
            <tr class="bhs-filter">
                {include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>