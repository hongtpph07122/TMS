<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");
use PhpOffice\PhpSpreadsheet\Cell\DataType;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Style\Fill;
use PhpOffice\PhpSpreadsheet\Writer\Xls;

class BhsCallcenterReport_ExportToExcel_Action extends Vtiger_BhsExport_Action {

    function exportCallDuration(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $exts = $moduleModel->getExts();
        $rows = [];
        for($i=0; $i < 10; $i++) {
            $row = [
                ['Fri', DataType::TYPE_STRING],
                ['12/04/2019', DataType::TYPE_STRING],
            ];
            foreach ($exts as $ext) {
                $row[] = [rand(10, 20), DataType::TYPE_STRING];
            }
            $row = array_merge($row, array(
                [rand(1000, 2000), DataType::TYPE_STRING],
                ['06:' . rand(20, 30) . ':' . rand(10, 59), DataType::TYPE_STRING],
                ['00:' . rand(0, 5) . ':' . rand(10, 59), DataType::TYPE_STRING]
            ));
            $rows[] = $row;
        }
        $row = [
            ['', DataType::TYPE_STRING],
            ['Total', DataType::TYPE_STRING],
        ];
        foreach ($exts as $ext) {
            $row[] = [rand(10, 20), DataType::TYPE_STRING];
        }
        $row = array_merge($row, array(
            [rand(1000, 2000), DataType::TYPE_STRING],
            ['06:' . rand(20, 30) . ':' . rand(10, 59), DataType::TYPE_STRING],
            ['00:' . rand(0, 5) . ':' . rand(10, 59), DataType::TYPE_STRING]
        ));
        $rows[] = $row;
        $lastcolumn = 5;
        $headers = array(
            vtranslate('LBL_DAY', $moduleName),
            vtranslate('LBL_DATE', $moduleName),
        );
        $lastcolumn += count($exts);
        $headers = array_merge($headers, $exts);
        $headers = array_merge($headers, array(
            vtranslate('LBL_TOTAL_NUMBER_OF_CALLS', $moduleName),
            vtranslate('LBL_TOTAL_CALL_DURATION', $moduleName),
            vtranslate('LBL_AVERAGE_CALL_DURATION', $moduleName),
        ));
        $data = array(
            'headers' => $headers,
            'body' => $rows,
            'fileName' => 'CallDuration',
            'columnWidths' => array(
                [$lastcolumn - 1, 25], [$lastcolumn, 25]
            )
        );
        $this->exportDataXLS($data);
    }

    function exportCallSummaryByDate(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $rows = [];
        for($i=0; $i<15; $i++) {
            $rows[] = [
                ['11-04-2019', DataType::TYPE_STRING],
                [rand(100, 120), DataType::TYPE_STRING],
                [rand(100, 200), DataType::TYPE_STRING],
                [rand(10000, 20000), DataType::TYPE_STRING],
                [rand(50, 100), DataType::TYPE_STRING],
                [rand(50, 80), DataType::TYPE_STRING],
                [rand(1, 10), DataType::TYPE_STRING],
                [rand(1, 10), DataType::TYPE_STRING]];
        }
        $headers = array(
            vtranslate('LBL_CALL_DATE', $moduleName),
            vtranslate('LBL_EXT', $moduleName),
            vtranslate('LBL_TOTAL_CALL', $moduleName),
            vtranslate('LBL_TOTAL_TALK_TIME', $moduleName),
            vtranslate('LBL_ANSWER', $moduleName),
            vtranslate('LBL_NO_ANSWER', $moduleName),
            vtranslate('LBL_BUSY', $moduleName),
            vtranslate('LBL_FAILED_CONGRESTITION', $moduleName),
        );

        $data = array(
            'headers' => $headers,
            'body' => $rows,
            'fileName' => 'CallSummaryByDate',
            'columnWidths' => array(
                [1, 15], [4, 15], [8, 15]
            )
        );
        $this->exportDataXLS($data);
    }

    function exportCustomerAdded(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $users = $moduleModel->getUsers();
        $rows = [];

        for ($i = 1; $i < 16; $i++) {
            $row = [['11-'. (10 + $i) .'-2018', DataType::TYPE_STRING]];
            foreach ($users as $user){
                $row[] = [rand(10, 20), DataType::TYPE_STRING];
            }
            $row[] = [rand(150, 300), DataType::TYPE_STRING];
            $rows[] = $row;
        }
        $headers = array(
            vtranslate('LBL_DATE', $moduleName),
        );
        foreach ($users as $user){
            $headers[] = $user;
        }
        $headers[] = vtranslate('LBL_TOTAL_AE', $moduleName);

        $data = array(
            'headers' => $headers,
            'body' => $rows,
            'fileName' => 'CustomerAdded',
            'columnWidths' => array(
                [1, 15]
            )
        );
        $this->exportDataXLS($data);
    }

    function exportDialledListsDetails(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $users = $moduleModel->getUsers();
        $rows = [];
        for ($i = 1; $i < 16; $i++) {
            $row = [];
            $row[] = ['Name - '. $i, DataType::TYPE_STRING];
            $row[] = ['09' . rand(10000000, 30000000), DataType::TYPE_STRING];
            $row[] = [rand(101, 120), DataType::TYPE_STRING];
            $row[] = ['ANSWERED', DataType::TYPE_STRING];
            $row[] = [rand(1, 5), DataType::TYPE_STRING];
            $row[] = ['09' . rand(10000000, 30000000), DataType::TYPE_STRING];
            $row[] = ['Address - '. $i, DataType::TYPE_STRING];
            $row[] = ['email'. $i. '@mail.com', DataType::TYPE_STRING];
            $row[] = ['Description - '. $i, DataType::TYPE_STRING];
            $row[] = ['Province - '. $i, DataType::TYPE_STRING];
            $row[] = ['District - '. $i, DataType::TYPE_STRING];
            $row[] = ['Subdistrict - '. $i, DataType::TYPE_STRING];
            $row[] = [rand(1000, 5000), DataType::TYPE_STRING];
            $row[] = [rand(1000000, 5000000), DataType::TYPE_STRING];
            $row[] = ['Payment info - '. $i, DataType::TYPE_STRING];
            $row[] = ['Gcode - '. $i, DataType::TYPE_STRING];
            $row[] = [rand(1000000, 5000000), DataType::TYPE_STRING];
            $row[] = ['Uncall', DataType::TYPE_STRING];
            $rows[] = $row;
        }
        $headers = array(
            vtranslate('LBL_NAME', $moduleName),
            vtranslate('LBL_PHONE_NUMBER', $moduleName),
            vtranslate('LBL_OWNER', $moduleName),
            vtranslate('LBL_DISPOSITION', $moduleName),
            vtranslate('LBL_MAX_TRY_COUNT', $moduleName),
            vtranslate('LBL_PHONE_NUMBER', $moduleName),
            vtranslate('LBL_ADDRESS', $moduleName),
            vtranslate('LBL_EMAIL', $moduleName),
            vtranslate('LBL_DESCRIPTION', $moduleName),
            vtranslate('LBL_PROVINCE', $moduleName),
            vtranslate('LBL_DISTRICT', $moduleName),
            vtranslate('LBL_SUBDISTRICT', $moduleName),
            vtranslate('LBL_ORIGIN_CODE', $moduleName),
            vtranslate('LBL_COD_AMOUNT', $moduleName),
            vtranslate('LBL_PAYMENT_INFO', $moduleName),
            vtranslate('LBL_GCODE', $moduleName),
            vtranslate('LBL_AMOUNT', $moduleName),
            vtranslate('LBL_CALL_STATUS', $moduleName),
        );

        $data = array(
            'headers' => $headers,
            'body' => $rows,
            'fileName' => 'DialledListsDetails',
            'columnWidths' => array(
                [1, 15]
            )
        );
        $this->exportDataXLS($data);
    }

    function exportDialledListsSummary(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $rows = [];

        $data = array(
            'headers' => array(
                '',
                vtranslate('LBL_AGENT_NAME', $moduleName),
                vtranslate('LBL_DATE', $moduleName),
                vtranslate('LBL_TOTAL_CALLS', $moduleName),
                vtranslate('LBL_ANSWER', $moduleName),
                vtranslate('LBL_NO_ANSWER', $moduleName),
                vtranslate('LBL_BUSY', $moduleName),
            ),
            'body' => $rows,
            'fileName' => 'DialledListsSummary',
        );

        if (true) {
            $spreadsheet = new Spreadsheet();
            $Excel_writer = new Xls($spreadsheet);
            $spreadsheet->setActiveSheetIndex(0);
            $activeSheet = $spreadsheet->getActiveSheet();

            $activeSheet->getColumnDimension('A')->setWidth(3);
            $activeSheet->getColumnDimension('B')->setWidth(25);

            $headers = $data['headers'];
            $rows = $data['body'];
            $fileName = $data['fileName'];
            if (empty($fileName)) {
                $fileName = 'test';
            }
            $count = 1;
            $rowcount = 1;
            foreach ($headers as $header) {
                $activeSheet->setCellValueExplicitByColumnAndRow($count, $rowcount, $header, true);
                $count += 1;
            }

            $grayBG = '7a7a7a';
            $grayColor = 'ffffff';
            $activeSheet->getStyle('A1:G1')->getFont()->setBold(true)->getColor()->setRGB($grayColor);
            $activeSheet->getStyle('A1:G1')->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);

            $grayBG = 'e8e8e8';
            $grayColor = '222222';

            for ($i = 0; $i < 10; $i++) {
                $rowcount += 1;
                $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 7, $rowcount);
                $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, 'Dial List: Name-'. $i, true);

                $t = rand(3, 7);
                for ($j = 0; $j < $t; $j++) {
                    $rowcount += 1;
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFont()->setBold(true)->getColor()->setRGB($grayColor);
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                    $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(2, $rowcount, 'Agent name-'.$j, true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, (10 + $j) . '/04/2019', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, rand(20, 30), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, rand(15, 20), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, rand(3, 5), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(7, $rowcount, rand(3, 5), true);
                }
            }

            header('Content-Type: application/vnd.ms-excel');
            header('Content-Disposition: attachment;filename="' . $fileName . '.xls"'); /*-- $filename is  xsl filename ---*/
            header('Cache-Control: max-age=0');
            $Excel_writer->save('php://output');
            exit();
        }
    }
}