<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCampaignReport_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getPhoneNotContacted');
        $this->exposeMethod('getPhoneContacted');
    }

    function getPhoneNotContacted(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCampaignReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            for ($id=1; $id<=10; $id++){
                $item = array(

                );
                $data[$item];
            }
            $data[] = ['Nguyễn thị thuật Nguyễn thị thuật -', '0912120232', '120', '1', '12/03/2019 10:24:59'];
            $data[] = ['Nguyen thi diem Huong Nguyen thi diem Huong', '0979732132', '119', '1', '12/03/2019 10:24:59'];
            $data[] = ['Diễm trần Diễm trần', '0942594739', '120', '1', '12/03/2019 10:24:59'];
            $data[] = ['Phan thị hương Giang Phan thị hương Giang', '0905224001', '121', '1', '12/03/2019 10:24:59'];
            $data[] = ['Trần thị duyên Trần thị duyên', '0334562096', '120', '1', '12/03/2019 10:24:59'];
            $data[] = ['Nguyen thi thanh huong', '0933468648', '120', '1', '12/03/2019 10:24:59'];
            $data[] = ['Nguyễn thị ngọc ánh Nguyễn Thị Ngọc Ánh', '0904164510', '125', '1', '12/03/2019 10:24:59'];
            $data[] = ['Đình Thị Hường', '0816988998', '122', '1', '12/03/2019 10:24:59'];
            $data[] = ['Lê Thị Nhung Lê thi nhung', '0376246505', '122', '1', '12/03/2019 10:24:59'];
            $data[] = ['Hoàng Thị hường Hoàng Thị hường', '0978287900', '122', '1', '12/03/2019 10:24:59'];

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

    function getPhoneContacted(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCampaignReport_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            for ($id=1; $id<=10; $id++){
                $item = array(

                );
                $data[$item];
            }

            $data[] = ['outbound', 'Sóc Sóc', '0772713164', '119', 'NO ANSWER', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', '', '0937758746', '122', 'NO ANSWER', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', '', '0962999072', '122', 'NO ANSWER', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', 'Phạm Trang', '0926874029', '119', 'NO ANSWER', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', 'Trần kim chi', '0773801412', '119', 'ANSWERED', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', 'Nguyễn Thị Ly Nơ', '0909914529', '119', 'ANSWERED', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', 'Trần thị hồng thắm', '0358285983', '119', 'ANSWERED', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', 'Nguyễn thi thien nga', '0919585449', '122', 'ANSWERED', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', 'Le thi dan', '0398348046', '122', 'NO ANSWER', '', '', '', '', '', '12/03/2019 10:24:59'];
            $data[] = ['outbound', 'Pham thi thang', '0848209105', '120', 'ANSWERED', '', '', '', '', '', '12/03/2019 10:24:59'];

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
