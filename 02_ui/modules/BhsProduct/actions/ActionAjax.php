<?php

/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */

class BhsProduct_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action
{

    function __construct()
    {
        parent::__construct();
        $this->exposeMethod('deleteProduct');
        $this->exposeMethod('saveRecord');
        $this->exposeMethod('exportExcel');
        $this->exposeMethod('changeStatus');
        $this->exposeMethod('getProductInfo');
    }

    function getList(Vtiger_Request $request)
    {
        $moduleModel = new BhsProduct_Module_Model();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'products');
            foreach ($rows as $row) {
                if ($row['status'] == 2) {
                    continue;
                }
                $id = $row['prodId'];
                $item = $row;
                $price = explode("|", $item['price']);
                $string = "";
                foreach ($price as $p) {
                    $p = number_format($p);
                    $string .= '<span class="tag label label-info">' . $p . '</span>';
                }
                $item['price'] = $string;
                if (!empty($item['status'])) {
                    $item['status'] = '<button type="button" class="btn btn-sm btn-toggle active" data-id="' . $id . '" data-toggle="button" aria-pressed="true" autocomplete="off"><div class="handle"></div></button>';
                } else {
                    $item['status'] = '<button type="button" class="btn btn-sm btn-toggle" data-id="' . $id . '"  data-toggle="button" aria-pressed="true" autocomplete="off"><div class="handle"></div></button>';
                }
                $item['bhs_action'] = '<div class="btn-group btn-group-action btn-group-action-custom">
                      <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                         <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                      </a>
                      <div class="dropdown-menu dropdown-menu-right">' . $this->createActionButtonsOnList($id) . '</div>
                    </div>';
                $item['bhs_record_id'] = $id; // Id or primary key
                $data[] = $item;
            }

            $res = array(
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
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

    function deleteProduct(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $res = array('success' => true);
        $moduleModel = new BhsProduct_Module_Model();
        try {
            // @Todo
            $prodId = intval($request->get('id'));
            $result = $moduleModel->deleteRecordById($prodId);
            if (empty($result['body'])) {
                $res['success'] = false;
                $res['message'] = vtranslate('Cannot delete.', $moduleName);
            }
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function getProductInfo(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $res = array('success' => true);
        $moduleModel = new BhsProduct_Module_Model();
        try {
            $prodId = intval($request->get('prod_id'));
            $result = $moduleModel->getRecordById('products', $prodId);
            $res['record'] = $result;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function saveRecord(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsProduct_Module_Model();
            $params = array();
            $params['prodId'] = intval($request->get('id'));
            $params['category'] = $request->get('category');
            $params['name'] = $request->get('name');
            $params['dscr'] = $request->get('dscr');
            $params['dscrForAgent'] = $request->get('dscrForAgent');
            $params['price'] = str_replace(',', '|', $request->get('price'));
            $status = $request->get('status');
            $params['status'] = $status;
            $record = $moduleModel->saveRecord($params);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function changeStatus(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsProduct_Module_Model();
            $params = array();
            $params['prodId'] = intval($request->get('id'));
            $params['status'] = $request->get('status');
            $record = $moduleModel->saveRecord($params);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function exportExcel()
    {
        $res = array('success' => true);
        try {
            // @Todo
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
