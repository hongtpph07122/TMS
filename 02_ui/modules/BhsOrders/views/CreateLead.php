<?php
/**
 * Created by PhpStorm.
 * User: DucVA
 * Date: 4/4/2019
 * Time: 4:28 PM
 */

class BhsOrders_CreateLead_View extends Vtiger_BhsCreateLead_View {
    function __construct() {
        parent::__construct();
    }

    public function process(Vtiger_Request $request) {
        $moduleModel = new BhsOrderProcessing_Module_Model();
        $modelProduct = new BhsProduct_Module_Model();
        $modelOrder = new BhsOrders_Module_Model();
        $modelCombo = new BhsCombo_Module_Model();
        $products = $modelProduct->getAllProductsActive();
        $combos = $modelCombo->getAllCombosActive();
        $leadId = $request->get('recordId');
        $type = $request->get('type');
        $leadId = intval($leadId);
//        if ($leadId <= 0){
//            header('location: index.php?module=BhsOrderProcessing&view=List');
//        }
        if($type == 'order') {
            $res = $moduleModel->makeApiCall('GET', 'SO/' . $leadId);
            $so  = $res['body']->data;
            $lead = $so->leadData;
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
        $campaignList=$modelOrder->getCampaignList();
        $viewer->assign('PRODUCTS', $products);
        $viewer->assign('COMBOS', $combos);
        $viewer->assign('PROVINCES', $provices);
        $viewer->assign('STOCKS', $stock->lst);
        $viewer->assign('RECORD', $lead);
        $viewer->assign('RECORD_STATUS_CLASS',  $moduleModel->getClassStatusLead($lead->leadStatusName) );
        $viewer->assign('RECORD_ID', $leadId);
        $viewer->assign('PAYMENT_METHODS', $paymentMethods);
        $viewer->assign('LEAD_STATUS', $leadStatus);
        $viewer->assign('LEAD_STATUS_TEXT', $leadStatusText['rejected']);
        $viewer->assign('CAMPAIGNS', $campaignList);
        global $API_ENDPOINT;
        global $show_warning_located;
        global $show_Neighborhood;
        $viewer->assign('API_ENDPOINT', $API_ENDPOINT);
        $viewer->assign('SHOW_WARNING', $show_warning_located);
        $viewer->assign('SHOW_NEIGHBORHOOD', $show_Neighborhood);
        if($type == 'order') {
            $viewer->assign('paymentMethodValue', $paymentMethodValue);
            $viewer->assign('LEAD_TYPE', $type);
        }
        parent::process($request);
    }


}