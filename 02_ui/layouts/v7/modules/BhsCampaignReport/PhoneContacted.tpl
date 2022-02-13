<div  style="margin: 0px 30px;">
    <div class="table-responsive">
        <table id="tblMailList0" class="table table-striped table-bordered custom-dt" style="width:100%;"
               data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getPhoneContacted"
               data-columnDefs='{$COLUMN_DEFS}'>
            <thead>
                <tr>
                    <th>{vtranslate('LBL_DIRECTION', $MODULE)}</th>
                    <th>{vtranslate('LBL_NAME', $MODULE)}</th>
                    <th>{vtranslate('LBL_PHONE_NUMBER', $MODULE)}</th>
                    <th>{vtranslate('LBL_USER_EXTENSION', $MODULE)}</th>
                    <th>{vtranslate('LBL_DISPOSITION', $MODULE)}</th>
                    <th>{vtranslate('LBL_HANGEDUPCALL', $MODULE)}</th>
                    <th>{vtranslate('LBL_NOTPOTENTIAL', $MODULE)}</th>
                    <th>{vtranslate('LBL_CONSIDER', $MODULE)}</th>
                    <th>{vtranslate('LBL_CLOSINGTIME', $MODULE)}</th>
                    <th>{vtranslate('LBL_DESCRIBE', $MODULE)}</th>
                    <th>{vtranslate('LBL_CALL_DATE', $MODULE)}</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    
</div>