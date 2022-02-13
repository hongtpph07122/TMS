<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");
use PhpOffice\PhpSpreadsheet\Cell\DataType;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Style\Fill;
use PhpOffice\PhpSpreadsheet\Writer\Xls;

class BhsOrderProcessing_ExportToExcel_Action extends Vtiger_BhsExport_Action {
   
    function exportData(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsOrderProcessing_Module_Model();
        try{
            $items = array();
            $rows = $moduleModel->getRowsRefactor($request, 'lead');
                foreach ($rows as $row){
                    $item=array();
                    $item[]= [$row['leadId'], DataType::TYPE_STRING];
                    $item[]= [$row['name'], DataType::TYPE_STRING];
                    $item[]= [$row['prodName'], DataType::TYPE_STRING];
                    $item[]= [$row['leadStatusName'], DataType::TYPE_STRING];
                    $address = $row['address'];
                    $address = trim($address);
                    if (!empty($row['subdistrict'])){
                        $address .= ' '. trim($row['subdistrict']);
                    }
                    if (!empty($row['district'])){
                        $address .= ' '. trim($row['district']);
                    }
                    if (!empty($row['province'])){
                        $address .= ' '. trim($row['province']);
                    }
                    $item[]= [CurrencyField::convertToUserFormat($item['ccCode'], null, true), DataType::TYPE_STRING];
                    $item[]= [trim($address), DataType::TYPE_STRING];
                    $item[]= [$row['phone'], DataType::TYPE_STRING];
                    $item[]= [$row['amount'], DataType::TYPE_STRING];
                    $item[]= [$row['agcId'], DataType::TYPE_STRING];
                    $item[]= [$row['assignedName'], DataType::TYPE_STRING];
                    $item[]= [$row['createdate'], DataType::TYPE_STRING];
                    $item[]= [$row['modifydate'], DataType::TYPE_STRING];
                    $item[]= [$row['totalCall'], DataType::TYPE_STRING];               
                    $items[]=$item;
                }     
        } catch (Exception $exception){
          $items= array();
        }
        $headers = array(
            vtranslate('LBL_LEADID', $moduleName),
            vtranslate('LBL_CUSTOMERNAME', $moduleName),
            vtranslate('LBL_PRODNAME', $moduleName),
            vtranslate('LBL_STATUS', $moduleName),
            vtranslate('LBL_CCCODE', $moduleName),
            vtranslate('LBL_ADDRESS', $moduleName),
            vtranslate('LBL_PHONE', $moduleName),
            vtranslate('LBL_AMOUNT', $moduleName),
            vtranslate('LBL_AGCID', $moduleName),
            vtranslate('LBL_ASSIGNEDNAME', $moduleName),
            vtranslate('LBL_CREATEDATE', $moduleName),
            vtranslate('LBL_MODIFYDATE', $moduleName),
            vtranslate('LBL_TOTALCALL', $moduleName),
        );
        $data = array(
            'headers' => $headers,
            'body' => $items,
            'fileName' => 'Order List',
            'columnWidths' => array(
                [1, 15],[2, 30],[3, 15], [4, 15],[6, 50],[7, 15], [8, 15],[11, 30],[12,30]
            )
        );
        $this->exportDataXLS($data);
    }
}