<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsMissedCalls_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
    }

    function getList(Vtiger_Request $request){
        global $adb, $current_user;
        $moduleName = $request->getModule();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            for($id = 1; $id <= $length; $id++){
                $start += 1;
                $data[] = array(
                    // '<a href="index.php?module='. $moduleName .'&view=Edit&recordId='. $start .'">Ext'. $start .'</a>',
                    '<a href="#">Ext'. $start .'</a>',
                    'Agent name' . $start,
                    'Name' . $start,
                    rand(1000000, 2000000),
                    DateTimeField::convertToUserFormat(date('m-d-Y')),
                    DateTimeField::convertToUserFormat(date('m-d-Y')),
                    'Note' . $start,
                    'Contacted by' . $start,
                    $start, // Id or primary key
                );
            }

            $res = array(
                'recordsTotal' => 1000,
                'recordsFiltered' => 1000,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception){
            $res = array(
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }
}
