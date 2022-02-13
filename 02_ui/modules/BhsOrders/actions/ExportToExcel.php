<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");

use PhpOffice\PhpSpreadsheet\Cell\DataType;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Style\Fill;
use PhpOffice\PhpSpreadsheet\Writer\Xls;

class BhsOrders_ExportToExcel_Action extends Vtiger_BhsExport_Action
{

    function exportData(Vtiger_Request $request) {
        $params = $request->getAll();
        $moduleModel = new BhsOrders_Module_Model();
        unset($params['module']);
        unset($params['action']);
        $dateFieldsFilter = ['stopdate', 'startdate','createdate','modifyDate'];
        foreach($params as $key => &$val) {
            if(in_array($key, $dateFieldsFilter) && !empty($val)) {
                $dateValue = explode('-',$val);
                $startDate = date('YmdHis',strtotime(str_replace('/','-',$dateValue[0])));
                $endDate = date('YmdHis',strtotime(str_replace('/','-',$dateValue[1])));
                $val = $startDate.'|'.$endDate;
            }
        }
        $request2['query'] = $params;
        $result = $moduleModel->makeApiCall('GET', 'SO/csv', $request2);
        $decoded =$result["body"];
        $fileName = 'Order_Managerment.csv';

        header('Content-Description: File Transfer');
        header('Content-Type: application/octet-stream');
        header('Content-Disposition: attachment; filename='.$fileName);
        header('Content-Transfer-Encoding: binary');
        header('Expires: 0');
        header('Cache-Control: must-revalidate');
        header('Pragma: public');
        header('Content-Length: '.strlen($decoded));
        ob_clean();
        flush();
        echo $decoded;
        exit;
    }
}