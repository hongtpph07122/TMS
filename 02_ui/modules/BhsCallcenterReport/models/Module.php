<?php

class BhsCallCenterReport_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
    }

    public function getFirstRecord(){
        $request = $this->buildDummyRequest();
        $res = $this->makeApiCall('POST', 'customer', $request);
        $this->firstRecord = $res;
    }

    public function getExts(){
        return range(101, 110, 1);
    }

    public function getUsers(){
        return array(
            'LinhLe', 'HuongNguyen', 'DongLuu', 'DungCao', 'ChauHo', 'HuynhNguyen', 'ThuyVo', 'NhungNguyen', 'TrangLe', 'NguyetNguyen',
            'HienNghiem', 'NhaTran', 'AnhThuNguyen', 'NhiTran', 'KhoaLy', 'KhaNguyen', 'MiDoan', 'TrucThai', 'NguyenNguyen', 'DuongNguyen', 'ThaoDang', 'TramPhung', 'DuyenTran'
        );
    }

    public function getColumnDefs_CallDuration(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(0)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => [0, 1, 2, 3, 4]
        );
        return $columnDefs;
    }

    public function getColumnDefs_CallSummaryByDate(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array()
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => [0, 1, 2, 3, 4]
        );
        return $columnDefs;
    }

    public function getColumnDefs_CustomerAdded(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array()
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => [0, 1, 2, 3, 4]
        );
        return $columnDefs;
    }

    public function getColumnDefs_DialledListsDetails(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array()
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => [0, 1, 2, 3, 4]
        );
        return $columnDefs;
    }

    public function getColumnDefs_DialledListsSummary(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(0)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => [0, 1, 2, 3, 4]
        );
        return $columnDefs;
    }
}