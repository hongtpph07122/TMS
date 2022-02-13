<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCallcenterReport_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getCallDuration');
        $this->exposeMethod('getCallSummaryByDate');
        $this->exposeMethod('getCustomerAdded');
        $this->exposeMethod('getDialledListsDetails');
        $this->exposeMethod('getDialledListsSummary');
    }

    function getDialledListsSummary(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $users = $moduleModel->getUsers();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            for ($i = 0; $i < 10; $i++) {
                $data[] = ['Dial List: Name-'. $i, '', '', '', '', '', '', '', 'bhsSpanDefs' => array(0, 7, 1), 'bhsRowClass' => 'gray-cell'];
                $n_r = rand(3, 7);
                for ($j = 0; $j < $n_r; $j++){
                    $data[] = ['', 'Agent name-'.$j, (10 + $j) . '/04/2019', rand(20, 30), rand(15, 20), rand(3, 5), rand(3, 5), 'bhsCellClass' => array('gray-cell' => [0])];
                }
            }
            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception){
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

    function getDialledListsDetails(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $users = $moduleModel->getUsers();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            for ($i = 1; $i < 16; $i++) {
                $row = ['Name - '. $i];
                $row[] = '09' . rand(10000000, 30000000);
                $row[] = rand(101, 120);
                $row[] = 'ANSWERED';
                $row[] = rand(1, 5);
                $row[] = '09' . rand(10000000, 30000000);
                $row[] = 'Address - '. $i;
                $row[] = 'email'. $i. '@mail.com';
                $row[] = 'Description - '. $i;
                $row[] = 'Province - '. $i;
                $row[] = 'District - '. $i;
                $row[] = 'Subdistrict - '. $i;
                $row[] = rand(1000, 5000);
                $row[] = rand(1000000, 5000000);
                $row[] = 'Payment info - '. $i;
                $row[] = 'Gcode - '. $i;
                $row[] = rand(1000000, 5000000);
                $row[] = 'Uncall';
                $data[] = $row;
            }
            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception){
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

    function getCustomerAdded(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $users = $moduleModel->getUsers();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            for ($i = 1; $i < 16; $i++) {
                $row = ['11-'. (10 + $i) .'-2018'];
                foreach ($users as $user){
                    $row[] = rand(10, 20);
                }
                $row[] = rand(150, 300);
                $data[] = $row;
            }
            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception){
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

    function getCallSummaryByDate(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();

            $data[] = ['11-04-2019', '103', '173', '11341', '62', '83', '6', '22'];
            $data[] = ['11-04-2019', '104', '36', '1448', '16', '13', '2', '5'];
            $data[] = ['11-04-2019', '105', '91', '8352', '48', '22', '9', '12'];
            $data[] = ['11-04-2019', '106', '27', '2154', '15', '4', '4', '4'];
            $data[] = ['11-04-2019', '107', '74', '5397', '39', '13', '10', '12'];
            $data[] = ['11-04-2019', '109', '104', '8607', '45', '27', '9', '23'];
            $data[] = ['11-04-2019', '111', '108', '8022', '43', '40', '8', '17'];
            $data[] = ['11-04-2019', '114', '105', '6353', '59', '26', '10', '10'];
            $data[] = ['11-04-2019', '115', '97', '8002', '51', '24', '5', '17'];
            $data[] = ['11-04-2019', '116', '120', '10140', '51', '33', '11', '25'];
            $data[] = ['11-04-2019', '119', '81', '5198', '36', '23', '3', '19'];
            $data[] = ['11-04-2019', '120', '73', '9038', '31', '24', '5', '13'];
            $data[] = ['11-04-2019', '121', '136', '8596', '55', '54', '6', '21'];
            $data[] = ['11-04-2019', '122', '119', '8703', '70', '19', '10', '20'];
            $data[] = ['11-04-2019', '123', '80', '6906', '43', '23', '5', '9'];
            $data[] = ['11-04-2019', '124', '101', '6625', '30', '57', '6', '8'];
            $data[] = ['11-04-2019', '126', '73', '6142', '41', '11', '10', '11'];

            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception){
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

    function getCallDuration(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            $exts = $moduleModel->getExts();
            for($i = 0; $i < 10; $i++){
                $row = ['Fri', '12/04/2019'];
                foreach ($exts as $ext){
                    $row[] = rand(10, 20);
                }
                $row1 = [rand(1000, 2000), '06:'. rand(20, 30) .':' . rand(10, 59), '00:'. rand(0, 5) .':' . rand(10, 59)];
                $row = array_merge($row, $row1);
                $data[] = $row;
            }
            $row = ['', 'Total'];
            foreach ($exts as $ext){
                $row[] = rand(100, 200);
            }
            $row1 = [rand(10000, 20000), '06:'. rand(20, 30) .':' . rand(10, 59), '10:'. rand(0, 5) .':' . rand(10, 59)];
            $row = array_merge($row, $row1);
            $data[] = $row;

            $res = array(
                // 'recordsTotal' => $moduleModel->getRecordTotal(),
                // 'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'recordsTotal' => 100,
                'recordsFiltered' => 100,
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception){
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
