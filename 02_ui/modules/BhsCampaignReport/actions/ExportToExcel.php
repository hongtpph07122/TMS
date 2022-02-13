<?php
require_once("libraries/PhpSpreadsheet/vendor/autoload.php");
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Cell\DataType;
use PhpOffice\PhpSpreadsheet\Writer\Xls;
use PhpOffice\PhpSpreadsheet\Style\Color;

class BhsCampaignReport_ExportToExcel_Action extends Vtiger_BhsExport_Action {

    function exportPhoneNotContacted(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $rows = [];
        $rows[] = [
            ['Nguyễn thị thuật Nguyễn thị thuật -', DataType::TYPE_STRING],
            ['0912120232', DataType::TYPE_STRING],
            ['120', DataType::TYPE_STRING],
            ['1', DataType::TYPE_STRING],
            ['12/03/2019 10:24:59', DataType::TYPE_STRING]];
        $rows[] = [
            ['Nguyen thi diem Huong Nguyen thi diem Huong', DataType::TYPE_STRING],
            ['0979732132', DataType::TYPE_STRING],
            ['119', DataType::TYPE_STRING],
            ['1', DataType::TYPE_STRING],
            ['12/03/2019 10:24:59', DataType::TYPE_STRING]];

        $data = array(
            'headers' => array(
                vtranslate('LBL_NAME', $moduleName),
                vtranslate('LBL_PHONE_NUMBER', $moduleName),
                vtranslate('LBL_ASSIGN', $moduleName),
                vtranslate('LBL_DIALTYPE', $moduleName),
                vtranslate('LBL_CREATE_TIME', $moduleName),
            ),
            'body' => $rows,
            'fileName' => 'PhoneNotContacted',
        );
        $this->exportDataXLS($data);
    }

    function exportPhoneContacted(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $rows = [];
        $rows[] = [
            ['outbound', DataType::TYPE_STRING],
            ['Phạm Trang', DataType::TYPE_STRING],
            ['0926874029', DataType::TYPE_STRING],
            ['119', DataType::TYPE_STRING],
            ['NO ANSWER', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['12/03/2019 10:24:59', DataType::TYPE_STRING]
        ];
        $rows[] = [
            ['outbound', DataType::TYPE_STRING],
            ['Trần kim chi', DataType::TYPE_STRING],
            ['0773801412', DataType::TYPE_STRING],
            ['120', DataType::TYPE_STRING],
            ['ANSWERED', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['12/03/2019 10:24:59', DataType::TYPE_STRING]
        ];
        $rows[] = [
            ['outbound', DataType::TYPE_STRING],
            ['Nguyễn Thị Ly Nơ', DataType::TYPE_STRING],
            ['0926874029', DataType::TYPE_STRING],
            ['121', DataType::TYPE_STRING],
            ['ANSWERED', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['', DataType::TYPE_STRING],
            ['12/03/2019 10:24:59', DataType::TYPE_STRING]
        ];

        $data = array(
            'headers' => array(
                vtranslate('LBL_DIRECTION', $moduleName),
                vtranslate('LBL_NAME', $moduleName),
                vtranslate('LBL_PHONE_NUMBER', $moduleName),
                vtranslate('LBL_USER_EXTENSION', $moduleName),
                vtranslate('LBL_DISPOSITION', $moduleName),
                vtranslate('LBL_HANGEDUPCALL', $moduleName),
                vtranslate('LBL_NOTPOTENTIAL', $moduleName),
                vtranslate('LBL_CONSIDER', $moduleName),
                vtranslate('LBL_CLOSINGTIME', $moduleName),
                vtranslate('LBL_DESCRIBE', $moduleName),
                vtranslate('LBL_CALL_DATE', $moduleName),
            ),
            'body' => $rows,
            'fileName' => 'PhoneContacted',
            'columnWidths' => array(
                [2, 40], [3, 15], [11, 25]
            )
        );
        $this->exportDataXLS($data);
    }
}