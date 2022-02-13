<div  style="margin: 0px 15px;">
    <div class="col-md-12 col-sm-12 marginBottom15px inline">
        <button id="btnCreateLead" type="submit" class="btn btn-blue" style="min-width: 140px;">{vtranslate('LBL_CREATELEAD', $MODULE)}</button>
        <button id="btnReassign"class="btn btn-blue" style="min-width: 140px;">{vtranslate('LBL_REASSIGN', $MODULE)}</button>
    </div>
    <table id="tblMailList0" class="table table-bordered custom-dt" style="width:200%"
           data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList"
           data-columnDefs='{$COLUMN_DEFS}'>
        <thead>
        <tr>
            <th>
                <input class="check-all" type="checkbox">
            </th>
            {include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
        </tr>
        <tr class="bhs-filter ddd">
            <th></th>
            {include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
        </tr>
        </thead>
        <tbody></tbody>
    </table>
    
</div>
