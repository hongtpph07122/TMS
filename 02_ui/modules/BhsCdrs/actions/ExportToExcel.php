<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");

use PhpOffice\PhpSpreadsheet\Cell\DataType;

class BhsCdrs_ExportToExcel_Action extends Vtiger_BhsExport_Action
{

    function exportData(Vtiger_Request $request)
    {
        $module = $request->getModule();
        $data = array(
            'headers' => array(
                vtranslate('LBL_DIRECTION', $module),
                vtranslate('LBL_START_TIME', $module),
                vtranslate('LBL_USER_EXTENS', $module),
                vtranslate('LBL_CUSTOMER_NAME', $module),
                vtranslate('LBL_CUSTOMER_NUMBER', $module),
                vtranslate('LBL_DISPOSITION', $module),
                vtranslate('LBL_BILL_DURATION', $module),
                vtranslate('LBL_CALL_NOTE', $module),
            ),
            'body' => array(
                array(
                    array('value1', DataType::TYPE_STRING),
                    array('value2', DataType::TYPE_STRING),
                    array('value3', DataType::TYPE_STRING),
                    array('value4', DataType::TYPE_STRING),
                    array('value5', DataType::TYPE_STRING),
                    array('value6', DataType::TYPE_STRING),
                    array('value7', DataType::TYPE_STRING),
                    array('value8', DataType::TYPE_STRING),
                ),
                array(
                    array('value21', DataType::TYPE_STRING),
                    array('value22', DataType::TYPE_STRING),
                    array('value23', DataType::TYPE_STRING),
                    array('value24', DataType::TYPE_STRING),
                    array('value25', DataType::TYPE_STRING),
                    array('value26', DataType::TYPE_STRING),
                    array('value27', DataType::TYPE_STRING),
                    array('value28', DataType::TYPE_STRING),
                ),
                array(
                    array('value21', DataType::TYPE_STRING),
                    array('value22', DataType::TYPE_STRING),
                    array('value23', DataType::TYPE_STRING),
                    array('value24', DataType::TYPE_STRING),
                    array('value25', DataType::TYPE_STRING),
                    array('value26', DataType::TYPE_STRING),
                    array('value27', DataType::TYPE_STRING),
                    array('value28', DataType::TYPE_STRING),
                ),
            )
        );
        $this->exportDataXLS($data);
    }
}