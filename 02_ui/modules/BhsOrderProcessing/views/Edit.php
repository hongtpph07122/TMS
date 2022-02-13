<?php
/**
 * Created by PhpStorm.
 * User: DucVA
 * Date: 4/4/2019
 * Time: 4:28 PM
 */

class BhsOrderProcessing_Edit_View extends Vtiger_BhsEdit_View {
    function __construct() {
        parent::__construct();
    }
    function preProcess(Vtiger_Request $request, $display=true) {
        //Vtiger7 - TO show custom view name in Module Header
        $viewer = $this->getViewer ($request);
        $moduleName = $request->getModule();
        $viewer->assign('CUSTOM_VIEWS', CustomView_Record_Model::getAllByGroup($moduleName));
        $moduleModel = Vtiger_Module_Model::getInstance($moduleName);
        $record = $request->get('record');
        if(!empty($record) && $moduleModel->isEntityModule()) {
            $recordModel = $this->record?$this->record:Vtiger_Record_Model::getInstanceById($record, $moduleName);
            $viewer->assign('RECORD',$recordModel);
        }

        $duplicateRecordsList = array();
        $duplicateRecords = $request->get('duplicateRecords');
        if (is_array($duplicateRecords)) {
            $duplicateRecordsList = $duplicateRecords;
        }
        $leadId = $request->get('recordId');
        $type = $request->get('type');
        $returnUrlParram = $request->get('returnUrl');
        $viewer->assign('GET_PARENT_URL', $returnUrlParram);
        $viewer->assign('RECORD_ID', $leadId);
        if($type == 'order') {
            $viewer->assign('LEAD_TYPE', $type);
        }
        if($type == 'UpdateLead') {
            $viewer->assign('PROCESSING_TYPE', $type);

        }

        if( $returnUrlParram == 'BhsOrders') {
            $viewer->assign('RETURN_URL', 'index.php?module=BhsOrders&view=List&app=BHS_ORDERS');
        } elseif ($returnUrlParram == 'BhsValidation') {
            $viewer->assign('RETURN_URL', 'index.php?module=BhsValidation&view=List&app=BHS_ORDERS');
        } else {
            $viewer->assign('RETURN_URL', 'index.php?module=BhsOrderProcessing&view=List&app=BHS_ORDERS');
        }
        $viewer->assign('TYPE', $type);
        $viewer = $this->getViewer($request);
        $viewer->assign('DUPLICATE_RECORDS', $duplicateRecordsList);
        parent::preProcess($request, $display);
    }
    public function process(Vtiger_Request $request) {
        global $show_warning_located;
        global $show_Neighborhood;
        $moduleModel = new BhsOrderProcessing_Module_Model();
        $modelProduct = new BhsProduct_Module_Model();
        $modelCombo = new BhsCombo_Module_Model();
        $products = $modelProduct->getAllProductsActive();
        $combos = $modelCombo->getAllCombosActive();
        $leadId = $request->get('recordId');
        $type = $request->get('type');
        $leadId = intval($leadId);
        if ($leadId <= 0){
            header('location: index.php?module=BhsOrderProcessing&view=List');
        }
        if($type == 'order') {
            $res = $moduleModel->makeApiCall('GET', 'SO/' . $leadId);
            $so  = $res['body']->data;
            $lead = $so->leadData;
            $soStatusName = $so->soData->statusName;
            $paymentMethodValue = $so->soData->paymentMethod;
        }else {
            $res = $moduleModel->makeApiCall('GET', 'lead/' . $leadId);
            $lead = $res['body']->data;
        }

        $viewer = $this->getViewer($request);
        $provices = $moduleModel->getProvinces();
        $stock = $moduleModel->getListStock();
        $paymentMethods = $moduleModel->getListPaymentMethod();//Vtiger_BhsUtility_Model::getConstantByType('payment mothod');
        $leadStatus = $moduleModel->getLeadStatus();
        $leadStatusText = $moduleModel->leadStatusText();
        $comboDiscount = 0;
        $saleDiscount = 0;
        if (!empty($so) && !empty($so->soData)) {
            $comboDiscount = $so->soData->discountCash1;
            $saleDiscount = $so->soData->discountCash2;
            $viewer->assign('SO_DATA', $so->soData);
        }
        $viewer->assign('SO_STATUS_NAME', $soStatusName);
        $viewer->assign('SO_STATUS_CLASS',  $moduleModel->getClassStatusSo($soStatusName));
        $viewer->assign('comboDiscount', $comboDiscount);
        $viewer->assign('saleDiscount', $saleDiscount);
        $viewer->assign('PRODUCTS', $products);
        $viewer->assign('COMBOS', $combos);
        $viewer->assign('PROVINCES', $provices);
        $viewer->assign('STOCKS', $stock->lst);
        $viewer->assign('STOCKSTATUS', $stock->selected);
        $viewer->assign('RECORD', $lead);
        $viewer->assign('RECORD_STATUS_CLASS',  $moduleModel->getClassStatusLead($lead->leadStatusName) );
        $viewer->assign('RECORD_ID', $leadId);
        $viewer->assign('PAYMENT_METHODS', $paymentMethods);
        $viewer->assign('LEAD_STATUS', $leadStatus);
        $viewer->assign('LEAD_STATUS_TEXT', $leadStatusText['rejected']);
        $viewer->assign('LEAD_STATUS_TRASH_REASON', $leadStatusText['trash']);
        $viewer->assign('SHOW_WARNING', $show_warning_located);
        $viewer->assign('SHOW_NEIGHBORHOOD', $show_Neighborhood);
        if($type == 'order') {
            $viewer->assign('paymentMethodValue', $paymentMethodValue);
            $viewer->assign('LEAD_TYPE', $type);
        }

        parent::process($request);
    }
    
}
