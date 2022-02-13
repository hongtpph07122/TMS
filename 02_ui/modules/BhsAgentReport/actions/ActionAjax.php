<?php

/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */

class BhsAgentReport_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action
{

    function __construct()
    {
        parent::__construct();
        $this->exposeMethod('getActivityDuration');
        $this->exposeMethod('getAgentLoginTime');
        $this->exposeMethod('getActivitySummary');
        $this->exposeMethod('getCallDetail');

    }

    function getActivityDuration(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCampaignReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            for ($id = 1; $id <= 10; $id++) {
                $item = array();
                $data[$item];
            }
            $data[] = ['Date and Time: 07/04/2019 07:30:00', '', '', '', '', '', '', 'bhsSpanDefs' => array(0, 7, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', '07/04/2019 07:30:00', 102, 1452, 0, 0, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'Summary', 1452, 0, 0, 0, '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['Date and Time: 07/04/2019 08:00:00', '', '', '', '', '', '', 'bhsSpanDefs' => array(0, 7, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', '07/04/2019 08:00:00', 100, 1728, 0, 0, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:00:00', 128, 354, 0, 487, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:00:00', 130, 34462, 0, 0, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:00:00', 102, 28, 0, 842, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:00:00', 110, 220, 0, 0, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'Summary', 36792, 0, 1329, 0, '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['Date and Time: 07/04/2019 08:15:00', '', '', '', '', '', '', 'bhsSpanDefs' => array(0, 7, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', '07/04/2019 08:15:00', 128, 331, 0, 440, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:15:00', 102, 281, 0, 947, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:15:00', 118, 205, 0, 0, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:15:00', 110, 235, 0, 756, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:15:00', 123, 86, 0, 756, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', '07/04/2019 08:15:00', 120, 637, 0, 756, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'Summary', 1775, 0, 2143, 0, '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];


            $data[] = ['', 'Total Summary', 2747862, 2388, 656487, 26, '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];

            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception) {
            $res = array(
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    function getCallDetail(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCampaignReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            for ($id = 1; $id <= 10; $id++) {
                $item = array();
                $data[$item];
            }

            $data[] = ['', 'HuynhNguyen', '18/02/2019 14:24:40', '18/02/2019 14:24:52', 'Outbound_non-ACD', 967265150, 842471083388, 'BUSY', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'NhiTran', '18/02/2019 14:55:13', '18/02/2019 14:55:40', 'Outbound_non-ACD', 909340064, 842471083388, 'NO ANSWER', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'AnhThuNguyen', '18/02/2019 14:57:22', '18/02/2019 14:57:29', 'Outbound_non-ACD', 967265150, 842471083388, 'BUSY', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'DungCao', '18/02/2019 14:59:59', '18/02/2019 15:00:07', 'Outbound_non-ACD', 967265150, 842471083388, 'BUSY', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'TrangLe', '18/02/2019 15:07:20', '18/02/2019 15:07:45', 'Outbound_non-ACD', 909340064, 842471083388, 'NO ANSWER', 'bhsCellClass' => array('gray-cell' => [0])];


            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception) {
            $res = array(
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    function getAgentLoginTime(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCampaignReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            for ($id = 1; $id <= 7; $id++) {
                $item = array();
                $data[$item];
            }
            $data[] = ['Sign in time: 12/04/2019', '', '', '', '', '', 'bhsSpanDefs' => array(0, 6, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', 911, 911, '12/04/2019 13:48:59', '12/04/2019 14:06:30', '00:17:31', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 100, 100, '12/04/2019 00:11:43', '12/04/2019 00:18:34', '00:06:51', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['Sign in time: 11/04/2019', '', '', '', '', '', 'bhsSpanDefs' => array(0, 6, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', 100, 100, '11/04/2019 20:30:40', '11/04/2019 22:26:48', '	01:56:08', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 100, 100, '11/04/2019 19:30:16', '	11/04/2019 20:20:40', '	00:50:24', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 130, 130, '11/04/2019 17:43:00', '	11/04/2019 17:44:13', '00:01:13', 'bhsCellClass' => array('gray-cell' => [0])];


            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception) {
            $res = array(
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    function getActivitySummary(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCampaignReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            for ($id = 1; $id <= 10; $id++) {
                $item = array();
                $data[$item];
            }
            $data[] = ['Date and Time: 07/04/2019 07:30:00', '', '', '', '', '', '', 'bhsSpanDefs' => array(0, 6, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', 102, '01:38', '00:00', '00:00', '00:00', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'Summary', '00:00', 0, 0, 0, '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['Date and Time: 07/04/2019 08:00:00', '', '', '', '', '', '', 'bhsSpanDefs' => array(0, 7, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', 100, '00:00', '00:00', '00:00', '00:00', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 128, '00:00', '00:00', '00:00', '00:00', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 130, '00:00', '00:00', '00:00', '02:27', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 102, '02:27', '02:27', '02:27', '02:27', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 110, '02:27', '02:27', '02:27', '02:27', 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'Summary', '02:27', '02:27', '02:27', '02:27', '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['Date and Time: 07/04/2019 08:15:00', '', '', '', '', '', '', 'bhsSpanDefs' => array(0, 6, 1), 'bhsRowClass' => 'gray-cell'];
            $data[] = ['', 128, 331, 0, 440, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 102, 281, 0, 947, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 118, 205, 0, 0, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 110, 235, 0, 756, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 123, 86, 0, 756, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 120, 637, 0, 756, 0, 'bhsCellClass' => array('gray-cell' => [0])];
            $data[] = ['', 'Summary', 1775, 0, 2143, 0, '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];

            $data[] = ['', 'Total Summary', 2747862, 2388, 656487, 26, '', 'bhsSpanDefs' => array(0, 2, 1), 'bhsRowClass' => 'gray-cell'];

            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception) {
            $res = array(
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }
}
