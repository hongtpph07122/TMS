<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");

use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Cell\DataType;
use PhpOffice\PhpSpreadsheet\Writer\Xls;
use PhpOffice\PhpSpreadsheet\Style\Color;
use PhpOffice\PhpSpreadsheet\Style\Fill;

class Vtiger_BhsExport_Action extends Vtiger_Mass_Action
{

    function checkPermission(Vtiger_Request $request)
    {
        return true;
    }

    public function validateRequest(Vtiger_Request $request)
    {
        return true;
    }

    /**
     * Function is called by the controller
     * @param Vtiger_Request $request
     */
    function process(Vtiger_Request $request)
    {
        $mode = $request->getMode();
        if (empty($mode)) {
            $mode = 'exportData';
        }
        $this->$mode($request);
    }

    /**
     * @param $data
     *
     * $data = array(
     * 'headers' => array('Title1', 'Title2'),
     * 'body' => array(
     * array(
     * array('value1', DataType::TYPE_STRING),
     * array('value2', DataType::TYPE_STRING)
     * ),
     * array(
     * array('value21', DataType::TYPE_STRING),
     * array('value22', DataType::TYPE_STRING)
     * ),
     * array(
     * array('value31', DataType::TYPE_STRING),
     * array('value32', DataType::TYPE_STRING)
     * ),
     * )
     * );
     */
    function exportDataXLS($data)
    {
        $spreadsheet = new Spreadsheet();
        $spreadsheet->setActiveSheetIndex(0);
        $activeSheet = $spreadsheet->getActiveSheet();

        $columnWidths = $data['columnWidths'];
        if (!empty($columnWidths)) {
            foreach ($columnWidths as $columnWidth) {
                $activeSheet->getColumnDimensionByColumn($columnWidth[0])->setWidth($columnWidth[1]);
            }
        }

        $grayBG = '7a7a7a';
        $grayColor = 'ffffff';

        $headers = $data['headers'];
        $rows = $data['body'];
        $fileName = $data['fileName'];
        if (empty($fileName)) {
            $fileName = 'test';
        }
        $count = 0;
        $rowcount = 1;
        foreach ($headers as $header) {
            $count += 1;
            $activeSheet->setCellValueExplicitByColumnAndRow($count, $rowcount, $header, true);
        }
        // $activeSheet->getStyleByColumnAndRow(1, 1, $count, 1)->getFont()->setBold(true)->getColor()->setRGB($grayColor);;
        $activeSheet->getStyleByColumnAndRow(1, 1, $count, 1)->getFont()->getColor()->setRGB($grayColor);;
        $activeSheet->getStyleByColumnAndRow(1, 1, $count, 1)->getFill()->setFillType(Fill::FILL_SOLID)->getStartColor()->setRGB($grayBG);
        foreach ($rows as $row) {
            $rowcount += 1;
            $count = 1;
            foreach ($row as $column) {
                $activeSheet->setCellValueExplicitByColumnAndRow($count, $rowcount, $column[0], $column[1]);
                $count += 1;
            }
        }
        header('Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
        header('Content-Disposition: attachment; filename="' . $fileName . '.xlsx"'); /*-- $filename is  xsl filename ---*/
        header('Cache-Control: max-age=0');
        $Excel_writer =  \PhpOffice\PhpSpreadsheet\IOFactory::createWriter($spreadsheet, "Xlsx");
        $Excel_writer->save('php://output');
        die();
    }
}