<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");
use PhpOffice\PhpSpreadsheet\Cell\DataType;
class BhsCallbackList_ExportToExcel_Action extends Vtiger_BhsExport_Action {

    function exportData(Vtiger_Request $request) {
        $data = array(
            'headers' => array('Title1', 'Title2'),
            'body' => array(
                array(
                    array('value1', DataType::TYPE_STRING),
                    array('value2', DataType::TYPE_STRING)
                ),
                array(
                    array('value21', DataType::TYPE_STRING),
                    array('value22', DataType::TYPE_STRING)
                ),
                array(
                    array('value31', DataType::TYPE_STRING),
                    array('value32', DataType::TYPE_STRING)
                ),
            )
        );
        $this->exportDataXLS($data);
    }
}