<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");
use PhpOffice\PhpSpreadsheet\Cell\DataType;

class BhsKerry_ExportToExcel_Action extends Vtiger_BhsExport_Action {

    function exportData(Vtiger_Request $request) {
        $params = (array)$request->getAll();
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
        $url = "DO/kerry/csv";
        $moduleModel = new BhsKerry_Module_Model();
        if (!empty($params['doIds'])){
            $params = $params['doIds'];
            $url = 'DO/kerry/ids/csv?doIds='.implode('&doIds=',$params);
            $result = $moduleModel->makeApiCall('GET', $url);
        } else {
            $request2['query'] = $params;
            $result = $moduleModel->makeApiCall('GET', $url, $request2);
        }
        $decoded =$result["body"];

        header('Content-Description: File Transfer');
        header('Content-Type: application/octet-stream');
        header('Content-Disposition: attachment; filename=Kerry.csv');
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