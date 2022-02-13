<div  style="margin: 0px 30px;">
    <div class="row">
        <div class="table-responsive">
            <table id="tblMailList0" class="table table-bordered custom-dt " style="width:100%"
                   data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList"
                   data-columnDefs='{$COLUMN_DEFS}'>
                <thead>
                    <tr>
                        {include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>