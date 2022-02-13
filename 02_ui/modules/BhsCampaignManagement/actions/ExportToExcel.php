<?php
require_once "libraries/PhpSpreadsheet/vendor/autoload.php";
use PhpOffice\PhpSpreadsheet\Cell\DataType;

class BhsCampaignManagement_ExportToExcel_Action extends Vtiger_BhsExport_Action
{
    public function __construct()
    {
        parent::__construct();
        $this->exposeMethod('exportData');
    }

    public function exportData(Vtiger_Request $request)
    {
        $moduleModel = new BhsCampaignManagement_Module_Model();
        $rows = $moduleModel->getRowsRefactor($request, 'campaign');
        if (count($rows) == 0) {
            return;
        }
        $headers = [];
        $body = [];
        for ($i = 0; $i < count($rows); $i++) {
            if ($i == 0) {
                foreach ($rows[$i] as $key => $val) {
                    $headers[] = $key;
                }
            } else {
                $b = [];
                foreach ($rows[$i] as $key => $val) {
                    if ($key == 'startdate' || $key == 'stopdate' || $key == 'createdate') {
                        $val = new DateTime($val);
                        $val = $val->format('Y-m-d');
                    }
                    $b[] = [$val, DataType::TYPE_STRING];
                }
            }
            $body[] = $b;
        }

        $data = [
            'headers' => $headers,
            'body' => $body,
            'fileName' => 'Campaigns-' . Date('YmdHis'),
        ];

        $this->exportDataXLS($data);
    }
}
