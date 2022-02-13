<?php
require_once "libraries/PhpSpreadsheet/vendor/autoload.php";
use PhpOffice\PhpSpreadsheet\Cell\DataType;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Style\Fill;
use PhpOffice\PhpSpreadsheet\Writer\Xls;

class BhsAgentReport_ExportToExcel_Action extends Vtiger_BhsExport_Action
{

    public function exportActivityDuration(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $rows = [];

        $data = array(
            'headers' => array(
                '',
                vtranslate('LBL_DATE_AND_TIME', $moduleName),
                vtranslate('LBL_AGENT_NAME', $moduleName),
                vtranslate('LBL_AVAILABLE', $moduleName),
                vtranslate('LBL_UNAVAILABLE', $moduleName),
                vtranslate('LBL_ONCALL', $moduleName),
                vtranslate('LBL_ACW', $moduleName),
            ),
            'body' => $rows,
            'fileName' => 'ActivityDuration',
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
                $dt = '07/04/2019 07:' . (10 + $i) . ':00';
                $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 7, $rowcount);
                $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, 'Date and Time: ' . $dt, true);

                $t = rand(3, 7);
                for ($j = 0; $j < $t; $j++) {
                    $rowcount += 1;
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFont()->setBold(true)->getColor()->setRGB($grayColor);
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                    $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(2, $rowcount, $dt, true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, rand(100, 200), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, rand(1000, 10000), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, rand(0, 10), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, rand(0, 10), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(7, $rowcount, rand(0, 10), true);
                }

                $rowcount += 1;
                $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 2, $rowcount);
                for ($iCol = 1; $iCol <= 7; $iCol++) {
                    $activeSheet->getStyleByColumnAndRow($iCol, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                }
                $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
                $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, 'Summary', true);
                $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, rand(1000, 10000), true);
                $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, 7 * rand(0, 10), true);
                $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, 7 * rand(0, 10), true);
                $activeSheet->setCellValueExplicitByColumnAndRow(7, $rowcount, 7 * rand(0, 10), true);
            }

            $rowcount += 1;
            $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 2, $rowcount);
            for ($iCol = 1; $iCol <= 7; $iCol++) {
                $activeSheet->getStyleByColumnAndRow($iCol, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
            }
            $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
            $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, 'Total Summary', true);
            $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, 100 * rand(1000, 10000), true);
            $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, 70 * rand(1, 10), true);
            $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, 70 * rand(1, 10), true);
            $activeSheet->setCellValueExplicitByColumnAndRow(7, $rowcount, 70 * rand(1, 10), true);

            // $rows[] = ['', 'Total Summary', 2747862, 2388, 656487, 26, 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];

            header('Content-Type: application/vnd.ms-excel');
            header('Content-Disposition: attachment;filename="' . $fileName . '.xls"'); /*-- $filename is  xsl filename ---*/
            header('Cache-Control: max-age=0');
            $Excel_writer->save('php://output');
            exit();
        }
        // $this->exportDataXLS($data);
    }

    public function exportAgentLoginTime(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $rows = [];

        $data = array(
            'headers' => array(
                '',
                vtranslate('LBL_USER', 'Vtiger'),
                vtranslate('LBL_USER_EXTENSION', $moduleName),
                vtranslate('LBL_SIGN_IN', $moduleName),
                vtranslate('LBL_SIGN_OUT', $moduleName),
                vtranslate('LBL_DURATION', $moduleName),
            ),
            'body' => $rows,
            'fileName' => 'AgentLoginTime',
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
            $activeSheet->getStyle('A1:G1')->getFont()->setBold(true)->getColor()->setRGB('FFFFFF');
            $activeSheet->getStyle('A1:G1')->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB('6d6d6d');

            $grayBG = 'e8e8e8';
            $grayColor = '222222';
            for ($i = 0; $i < 10; $i++) {
                $rowcount += 1;
                $dt = '07/04/2019 07:' . (10 + $i) . ':00';
                $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 6, $rowcount);
                $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, 'Sign in time: ' . $dt, true);

                $t = rand(3, 6);
                for ($j = 0; $j < $t; $j++) {
                    $rowcount += 1;
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFont()->setBold(true)->getColor()->setRGB($grayColor);
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                    $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(2, $rowcount, rand(100, 200), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, rand(100, 200), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, $dt, true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, $dt, true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(7, $rowcount, '00:17:31', true);
                }

                
            }

           

            // $rows[] = ['', 'Total Summary', 2747862, 2388, 656487, 26, 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];

            header('Content-Type: application/vnd.ms-excel');
            header('Content-Disposition: attachment;filename="' . $fileName . '.xls"'); /*-- $filename is  xsl filename ---*/
            header('Cache-Control: max-age=0');
            $Excel_writer->save('php://output');
            exit();
        }
    }
    public function exportCallDetail(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $rows = [];

        $data = array(
            'headers' => array(
                '',
                vtranslate('LBL_AGENT_NAME', $moduleName),
                vtranslate('LBL_CALL_START', $moduleName),
                vtranslate('LBL_CALL_END', $moduleName),
                vtranslate('LBL_CALL_TYPE', $moduleName),
                vtranslate('LBL_CALLER', $moduleName),
                vtranslate('LBL_NUMBER_CALLED', $moduleName),
                vtranslate('LBL_Disposition_Codes', $moduleName),
            ),
            'body' => $rows,
            'fileName' => 'CallDetail',
        );

        if (true) {
            $spreadsheet = new Spreadsheet();
            $Excel_writer = new Xls($spreadsheet);
            $spreadsheet->setActiveSheetIndex(0);
            $activeSheet = $spreadsheet->getActiveSheet();

            $activeSheet->getColumnDimension('A')->setWidth(3);
            $activeSheet->getColumnDimension('B')->setWidth(25);
            $activeSheet->getColumnDimension('C')->setWidth(25);
            $activeSheet->getColumnDimension('D')->setWidth(25);
            $activeSheet->getColumnDimension('E')->setWidth(25);
            $activeSheet->getColumnDimension('F')->setWidth(25);
            $activeSheet->getColumnDimension('H')->setWidth(25);
            $activeSheet->getColumnDimension('G')->setWidth(25);
            $activeSheet->getColumnDimension('I')->setWidth(25);


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
            $activeSheet->getStyle('A1:H1')->getFont()->setBold(true)->getColor()->setRGB('FFFFFF');
            $activeSheet->getStyle('A1:H1')->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB('6d6d6d');

            $grayBG = 'e8e8e8';
            $grayColor = '222222';
            for ($i = 0; $i < 10; $i++) {
                $t = rand(3, 8);
                for ($j = 0; $j < $t; $j++) {
                    $rowcount += 1;
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFont()->setBold(true)->getColor()->setRGB($grayColor);
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                    $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(2, $rowcount, 'NhiKoy', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, '18/02/2019 15:06:05', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, '18/02/2019 15:06:14', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount,'Outbound_non-ACD', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, '901329459', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(7, $rowcount, '842471083388', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(8, $rowcount, 'NO ANSWER', true);
                }

                
            }

           

            // $rows[] = ['', 'Total Summary', 2747862, 2388, 656487, 26, 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];

            header('Content-Type: application/vnd.ms-excel');
            header('Content-Disposition: attachment;filename="' . $fileName . '.xls"'); /*-- $filename is  xsl filename ---*/
            header('Cache-Control: max-age=0');
            $Excel_writer->save('php://output');
            exit();
        }
    }
    public function exportActivitySummary(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $rows = [];

        $data = array(
            'headers' => array(
                '',
                vtranslate('LBL_AGENT_NAME', $moduleName),
                vtranslate('LBL_Average_Available_Time', $moduleName),
                vtranslate('LBL_Average_Unavailable_Time', $moduleName),
                vtranslate('LBL_Average_Oncall_Time', $moduleName),
                vtranslate('LBL_Average_ACW_Time', $moduleName),
            ),
            'body' => $rows,
            'fileName' => 'ActivitySummary',
        );

        if (true) {
            $spreadsheet = new Spreadsheet();
            $Excel_writer = new Xls($spreadsheet);
            $spreadsheet->setActiveSheetIndex(0);
            $activeSheet = $spreadsheet->getActiveSheet();
            
            $activeSheet->getColumnDimension('A')->setWidth(3);
            
            foreach(range('B','F') as $columnID) {
                $activeSheet->getColumnDimension($columnID)->setAutoSize(true);
            }
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
            $activeSheet->getStyle('A1:F1')->getFont()->setBold(true)->getColor()->setRGB('FFFFFF');
            $activeSheet->getStyle('A1:F1')->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB('6d6d6d');

            $grayBG = 'e8e8e8';
            $grayColor = '222222';
            for ($i = 0; $i < 10; $i++) {
                $rowcount += 1;
                $dt = '07/04/2019 07:' . (10 + $i) . ':00';
                $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 6, $rowcount);
                $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, 'Date and Time: ' . $dt, true);

                $t = rand(3, 6);
                for ($j = 0; $j < $t; $j++) {
                    $rowcount += 1;
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFont()->setBold(true)->getColor()->setRGB($grayColor);
                    $activeSheet->getStyleByColumnAndRow(1, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                    $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(2, $rowcount, rand(100, 200), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, rand(100, 200), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, rand(1000, 10000), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, rand(0, 10), true);
                    $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, rand(0, 10), true);
                    
                }

                $rowcount += 1;
                $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 2, $rowcount);
                for ($iCol = 1; $iCol <= 6; $iCol++) {
                    $activeSheet->getStyleByColumnAndRow($iCol, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
                }
                $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
                $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, 'Summary', true);
                $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, rand(1000, 10000), true);
                $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, 7 * rand(0, 10), true);
                $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, 7 * rand(0, 10), true);
               
            }

            $rowcount += 1;
            $spreadsheet->getActiveSheet()->mergeCellsByColumnAndRow(1, $rowcount, 2, $rowcount);
            for ($iCol = 1; $iCol <= 6; $iCol++) {
                $activeSheet->getStyleByColumnAndRow($iCol, $rowcount)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
            }
            $activeSheet->setCellValueExplicitByColumnAndRow(1, $rowcount, '', true);
            $activeSheet->setCellValueExplicitByColumnAndRow(3, $rowcount, 'Total Summary', true);
            $activeSheet->setCellValueExplicitByColumnAndRow(4, $rowcount, 100 * rand(1000, 10000), true);
            $activeSheet->setCellValueExplicitByColumnAndRow(5, $rowcount, 70 * rand(1, 10), true);
            $activeSheet->setCellValueExplicitByColumnAndRow(6, $rowcount, 70 * rand(1, 10), true);
            

            // $rows[] = ['', 'Total Summary', 2747862, 2388, 656487, 26, 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];

            header('Content-Type: application/vnd.ms-excel');
            header('Content-Disposition: attachment;filename="' . $fileName . '.xls"'); /*-- $filename is  xsl filename ---*/
            header('Cache-Control: max-age=0');
            $Excel_writer->save('php://output');
            exit();
        }
        // $this->exportDataXLS($data);
    }
}
