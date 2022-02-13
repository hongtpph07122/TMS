<?php

/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */

class BhsOrderProcessing_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action
{
    private $_partnerId;
    private $_listStock;
    private $_moduleModel;
    private $ORDER_STATUS_NEW = 41;
    private $CURENCY_VND = 1;
    private $PRODUCT_TYPE = [1 => 'Normal', 2 => 'Combo'];

    public function __construct()
    {
        parent::__construct();
        $this->exposeMethod('getProducts');
        $this->exposeMethod('getStocks');
        $this->exposeMethod('getRecentOrders');
        $this->exposeMethod('callCustomer');
        $this->exposeMethod('hangupToCustomer');
        $this->exposeMethod('changeProvince');
        $this->exposeMethod('changeDistrict');
        $this->exposeMethod('changeTown');
        $this->exposeMethod('changeNeighborhood');
        $this->exposeMethod('getRelatedZipcode');
        $this->exposeMethod('setCallbackSchedule');
        $this->exposeMethod('saveSaleOrder');
        $this->exposeMethod('saveNewSaleOrder');
        $this->exposeMethod('getSimilarOrder');
        $this->exposeMethod('getAllProducts');
        $this->exposeMethod('getProductsDetail');
        $this->exposeMethod('getAgent');
        $this->exposeMethod('savePhoneNumber');
        $this->exposeMethod('getLeadHistory');
        $this->exposeMethod('getProductInfo');
    }

    public function setCallbackSchedule(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $data = array();
            $data['leadId'] = $request->get('so_id');
            $data['note'] = $request->get('note');
            $data['phone'] = $request->get('phone');
            $data['phoneType'] = $request->get('phone_type');
            $data['name'] = $request->get('phoneName');
            $data['status'] = $request->get('callback_type');
            $data['userDefine5'] = $request->get('userDefin05');
            $callbackDate = $request->get('callback_date');
            $callbackTime = $request->get('callback_time');
            $callbackDate = explode('/', $callbackDate);
            $callbackDate = $callbackDate[2] . '-' . $callbackDate[1] . '-' . $callbackDate[0];
            $callbackDate = new DateTime($callbackDate);
            $time = $callbackDate->format('Y-m-d') . ' ' . $callbackTime;
            $time = new DateTime($time);
            $data['callbackTime'] = $time->format('YmdHis');
            $result = $moduleModel->setCallbackSchedule($data);
            if ($result->code == 200) {
                $dataStatus = [
                    "leadId" => $data['leadId'],
                    "address" => $request->get('address'),
                    "name" => $request->get('name'),
                    "district" => $request->get('district'),
                    "province" => $request->get('province'),
                    "subdistrict" => $request->get('subdistrict'),
                    "neighborhood" => $request->get('neighborhood'),
                    "postalCode" => $request->get('postalCode'),
                    "comment" => $request->get('comment'),
                    "leadStatus" => $request->get('leadStatus'),
                    "userDefin05" => $request->get('userDefin05'),
                ];
                $status = $moduleModel->saveLeadInfo($dataStatus);
                if ($status->code == 200) {
                    $res['success'] = true;
                    $res['message'] = 'success';
                } else {
                    $res['success'] = false;
                    $res['message'] = $status->message;
                }
            } else {
                $res['success'] = false;
                $res['message'] = $result->message;
            }

        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function saveSaleOrder(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $orderres = [];
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            // @Todo
            $data = array();
            $SOtype = $request->get('SOtype');
            if ($SOtype == 'order') {
                $data['soId'] = $request->get('leadId');
                $so = $moduleModel->makeApiCall('GET', 'SO/' . $data['soId']);
            }
            $data['leadStatus'] = $request->get('leadStatus');
            $data['status'] = empty($so) ? $this->ORDER_STATUS_NEW : $so["body"]->data->soData->status;
            $data['amount'] = $request->get('amount');
            $data['saleDiscount'] = $request->get('saleDiscount');
            $data['comboDiscount'] = $request->get('comboDiscount');
            $data['salePercent'] = $request->get('salePercent') ? $request->get('salePercent') : 0;
            $data['comboPercent'] = $request->get('comboPercent') ? $request->get('comboPercent') : 0;
            $data['unit'] = $this->CURENCY_VND;
            $data['leadId'] = ($SOtype == 'order') ? $request->get('leadIdUpdate') : $request->get('leadId');
            $data['paymentMethod'] = $request->get('paymentMethod');
            $data['products'] = array();
            $products = $request->get('products');
            foreach ($products as $k => $product) {
                $data['products'][$k] = $product;
            }
            // update order tu man hinh validation
            if ($SOtype == 'order') {

                $result = $moduleModel->updateOrder($data);
                if (!empty($result) && $result->code == 200) {
                    $dataStatus = [
                        "leadId" => ($SOtype == 'order') ? $request->get('leadIdUpdate') : $request->get('leadId'),
                        "address" => $request->get('address'),
                        "name" => $request->get('name'),
                        "district" => $request->get('district'),
                        "province" => $request->get('province'),
                        "subdistrict" => $request->get('subdistrict'),
                        "neighborhood" => $request->get('neighborhood'),
                        "postalCode" => $request->get('postalCode'),
                        "comment" => $request->get('comment'),
                        "userDefin05" => $request->get('userDefin05'),
                        "leadType" => $request->get('leadType'),
                        "customerEmail" => $request->get('customerEmail'),
                        "customerAge" => $request->get('customerAge'),
                        "phone" => $request->get('phone'),
                        "leadStatus" => 2
                    ];
                    $status = $moduleModel->saveLeadInfo($dataStatus);
                }
            } else {
                // tao moi order tu man  hinh orderlist hoac man hinh order managerment
                if ($request->get('leadStatus') == 2) {
                    $data['leadStatus'] = $request->get('leadStatus');
                    $data['phone'] = $request->get('phone');
                    $orderres = $moduleModel->createOrder($data);
                }

                // update lead tư man hinh order list, hoac tu man hinh order management
                $dataStatus = [
                    "leadId" => ($SOtype == 'order') ? $request->get('leadIdUpdate') : $request->get('leadId'),
                    "address" => $request->get('address'),
                    "name" => $request->get('name'),
                    "district" => $request->get('district'),
                    "province" => $request->get('province'),
                    "subdistrict" => $request->get('subdistrict'),
                    "neighborhood" => $request->get('neighborhood'),
                    "postalCode" => $request->get('postalCode'),
                    "comment" => $request->get('comment'),
                    "userDefin05" => $request->get('userDefin05'),
                    "customerEmail" => $request->get('customerEmail'),
                    "customerAge" => $request->get('customerAge'),
                    "leadType" => $request->get('leadType'),
                    "phone" => $request->get('phone'),
                    "leadStatus" => $request->get('leadStatus')
                ];
                $status = $moduleModel->saveLeadInfo($dataStatus);
            }


            if (!empty($status) && $status->code == 200) {
                $res['success'] = true;
                $res['message'] = 'success';
                if (!empty($orderres) && $status->code == 200) {
                    $res['message'] = $orderres->message;
                }

            } else {
                $res['success'] = false;
                $res['message'] = vtranslate('LBL_CANNOTSAVE', $request->getModule());
            }

        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function saveNewSaleOrder(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $dataStatus = [
                "address" => $request->get('address'),
                "name" => $request->get('name'),
                "cpId" => $request->get('cpId'),
                "assigned" => $request->get('agentId'),
                "district" => $request->get('district'),
                "province" => $request->get('province'),
                "subdistrict" => $request->get('subdistrict'),
                "neighborhood" => $request->get('neighborhood'),
                "postalCode" => $request->get('postalCode'),
                "comment" => $request->get('comment'),
                "leadStatus" => 2,
                "leadType" => "M",
                "customerEmail" => $request->get('customerEmail'),
                "customerAge" => $request->get('customerAge'),
                "phone" => $request->get('phone'),
            ];
            $status = $moduleModel->saveLeadInfo($dataStatus);
            if ($status->code == 200) {
                $data = array();
                $data['leadStatus'] = $request->get('leadStatus');
                $data['amount'] = $request->get('amount');
                $data['saleDiscount'] = $request->get('saleDiscount');
                $data['comboDiscount'] = $request->get('comboDiscount');
                $data['salePercent'] = $request->get('salePercent') ? $request->get('salePercent') : 0;
                $data['comboPercent'] = $request->get('comboPercent') ? $request->get('comboPercent') : 0;
                $data['unit'] = $this->CURENCY_VND;
                $data['cpId'] = $request->get('cpId');
                $data['agentId'] = $request->get('agentId');
                $data['leadId'] = $status->message;
                $data['paymentMethod'] = $request->get('paymentMethod');
                $data['leadType'] = "M";
                $data['products'] = array();
                $products = $request->get('products');
                $data['phone'] = $request->get('phone');
                foreach ($products as $k => $product) {
                    $data['products'][$k] = $product;
                }
                $moduleModel->createOrder($data);

            } else {
                $res['success'] = false;
                $res['message'] = $status->message;
            }
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function getRelatedZipcode(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $postalCode = $request->get('postalCode');
            $zipcodes = $moduleModel->getRelatedZipcode($postalCode);
//            if (!$postalCode) {
//                $zipcode = '';
//            }
            $res['zipcodes'] = $zipcodes;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function changeNeighborhood(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $nbhId = $request->get('nbh_id');
            $zipcode = $moduleModel->getZipcode($nbhId);
            if (!$nbhId) {
                $zipcode = '';
            }
            $res['zipcode'] = $zipcode;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function changeTown(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $townId = $request->get('town_id');
            $neighborhoods = $moduleModel->getNeighborhood($townId);
            if (!$townId) {
                $neighborhoods = '';
            }
            $res['neighborhoods'] = $neighborhoods;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function changeDistrict(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $districtId = $request->get('district_id');
            $subdistricts = $moduleModel->getSubdistrict($districtId);
            if (!$districtId) {
                $subdistricts = '';
            }
            $res['towns'] = $subdistricts;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function changeProvince(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $province_id = $request->get('province_id');
            $districts = $moduleModel->getDistrict($province_id);
            if (!$province_id) {
                $districts = '';
            }
            $res['districts'] = $districts;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function callCustomer(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $phone = $request->get('phone');
            $leadId = $request->get('leadId');
            $r = $moduleModel->callToCustomer($phone, $leadId);
            if ($r->code == 200 && $r->data->channelId != '') {
                $res['success'] = true;
                $res['message'] = 'success';
                $res['data'] = $r->data;
            } else {
                $res['success'] = false;
                $res['message'] = $r[message];
                $res['data'] = [];
            }
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function hangupToCustomer(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $userTMS = Vtiger_Session::get('agent_info');
            $phone = $userTMS->info->phone;
            $channelId = $request->get('channelId');
            $r = $moduleModel->hangupToCustomer($channelId);
            if ($r->code == 200) {
                $res['success'] = true;
                $res['message'] = 'success';
                $res['data'] = [];
            } else {
                $res['success'] = false;
                $res['message'] = $r[message];
            }
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function getRecentOrders(Vtiger_Request $request)
    {
        global $adb;
        $moduleName = $request->getModule();
        $res = array();
        $draw = intval($request->get('draw'));
        $phone = $request->get('phone');

        try {
            $data = array();
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $rows = $moduleModel->getRowsRefactor($request, 'SO/getRecent/' . $phone);
            for ($i = 0; $i < count($rows); $i++) {
                $createdate = new DateTime($rows[$i]['createdate']);
                $createdate = $createdate->format('d-m-Y H:m:i');
                $lead = $moduleModel->getRowsRefactor($request, 'lead/' . $rows[$i]['leadId']);
                $data[] = array(
                    $lead[0]['prodName'],
                    $rows[$i]['soId'],
                    $createdate,
                    $rows[$i]['leadName'],
                    CurrencyField::convertToUserFormat($rows[$i]['amount'], null, true),
                );
            }

            $res = array(
                'recordsTotal' => count($data),
                'recordsFiltered' => count($data),
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

    public function getStocks(Vtiger_Request $request)
    {
        global $adb;
        $moduleName = $request->getModule();
        $res = array();
        $draw = intval($request->get('draw'));
        $soId = $request->get('soId'); //?$request->get('soId'):1;
        try {
            $data = array();
            $moduleModel = new BhsOrderProcessing_Module_Model();
            if (!$request->get('type')) {
                $_REQUEST['prodId'] = $request->get('prodId');
                $listProductIds[] = $request->get('prodId');
            }
            $params['query'] = [
                'status' => 1,
                'prodId' => $request->get('prodId')
            ];
            $res1 = $moduleModel->makeApiCall('GET', 'products/StockByProduct', $params);
            $rows = $res1['body']->data;
            $listStock = $moduleModel->getListStock()->lst;
            $res2 = $moduleModel->makeApiCall('GET', 'SO/SaleOrderItems/' . $soId);
            $listProducts = $res2['body']->data;

            foreach ($listProducts as $prod) {
                $listProductIds[] = $prod->prodId;
            }

            foreach ($rows as $row) {
                if (in_array($row->prodId, $listProductIds)) {
                    foreach ($listStock as $st) {
                        if ($st->pnId == $row->partnerId) {
                            $stockName = $st->shortname;
                            break;
                        }
                    }
                    $data[] = [
                        $row->prodId,
                        $row->name,
                        $stockName,
                        $row->quantityAvailable,
                        $row->quantityTotal,
                    ];
                }

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

    public function getProducts(Vtiger_Request $request)
    {
        $moduleModel = new BhsOrderProcessing_Module_Model();
        $moduleModelProduct = new BhsProduct_Module_Model();
        $res = array();
        $draw = intval($request->get('draw'));
        try {
            $data = array();

            $type = $request->get('type');
            $leads = [];
            if (isset($type) && $type == 'order') {
                $soId = $request->get('leadId');
                $res2 = $moduleModel->makeApiCall('GET', 'SO/SaleOrderItems/' . $soId);
                $leads = $res2['body']->data;
            } else {
                $leadId = $request->get('leadId');
                $res2 = $moduleModel->makeApiCall('GET', 'lead/' . $leadId);
                $leads[] = $res2['body']->data;

            }
            foreach ($leads as $lead) {
                if (empty($lead->prodId)) {
                    continue;
                }
                $productId = $lead->prodId;
                $productType = $lead->productType;
                if (!$productType || !$lead->discountCash) {
                    $productDetail = $moduleModel->getRecordById('products', $productId);
                    $productType = $productDetail->productType;
                    $lead->discountCash = $productDetail->discountCash;
                }
                if ($productType == 1) {
                    $allProductPrice = explode('|', $moduleModelProduct->getRecordById('products', $productId)->price);
                    $priceIndex = 0;
                    if (isset($type) && $type == 'order') {
                        $price = $lead->price;
                        $priceIndex = array_search($lead->price, $allProductPrice);
                    } else {
                        $price = $allProductPrice[0];
                    }
                } else {
                    $allProductPrice = explode('|', $moduleModelProduct->getRecordById('products', $productId)->listPrice);
                    $priceIndex = 0;
                    if (isset($type) && $type == 'order') {
                        $price = $lead->listPrice;
                        $priceIndex = array_search($lead->listPrice, $allProductPrice);
                    } else {
                        $price = $allProductPrice[0];
                    }
                }

                $total = $price * ($lead->quantity ? $lead->quantity : 1);
                $price = CurrencyField::convertToUserFormat($price, null, true);
                $total = CurrencyField::convertToUserFormat($total, null, true);
                $inlineButtons = '<div class="btn-group btn-group-action">
                      <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <img src="layouts/v7/resources/Images/icon/icon-action.png" alt="">
                      </a>
                      <div class="dropdown-menu dropdown-menu-right">
                        <a class="btnInlineEdit"><i class="fa fa-pencil"></i> Edit</a>
                        <a class="btnInlineView"><i class="fa fa-eye"></i> View</a>
                        <a class="btnInlineRemove"><i class="fa fa-trash"></i> Trash</a>
                        <a class=" btnInlineSave" style="display: none;"><i class="fa fa-check"></i> Save</a>
                        <a class="btnInlineCancel" style="display: none;"><i class="fa fa-close"></i> Cancel</a>
                      </div>
                    </div>';
                $listStock = $moduleModel->getListStock()->lst;
                $this->_listStock = $listStock;
                $_REQUEST['prodId'] = $productId;
                $stockProduct = $moduleModel->getRowsRefactor($request, 'products/StockByProduct');
                $stockName = '';
                if (count($stockProduct)) {
                    //$stock = $stockProduct[0]['partnerName'];
                    $this->_partnerId = $stockProduct[0]['partnerId'];
                    foreach ($listStock as $st) {
                        if ($st->pnId == $stockProduct[0]['partnerId']) {
                            $stockName = $st->shortname;
                            break;
                        }
                    }
                }
                $lead->prodName = '<span class="text-info">' . $lead->prodName . '</span>';
                $data[] = array(
                    $productId,
                    $productType ? $this->PRODUCT_TYPE[$productType] : $this->PRODUCT_TYPE[1],
                    $lead->prodName,
                    $stockName,
                    $lead->quantity ? $lead->quantity : 1,
                    '<span data-priceindex = "' . $priceIndex . '" class="text-info">' . $price . '</span>',
                    $total,
                    $inlineButtons,
                    $lead->discountCash ? $lead->discountCash : 0,
                    'bhs_record_id' => $productId

                );
            }
            $res = array(
                'recordsTotal' => count($data),
                'recordsFiltered' => count($data),
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

    public function getProductsDetail(Vtiger_Request $request)
    {
        global $adb;
        $moduleName = $request->getModule();
        $moduleModel = new BhsOrderProcessing_Module_Model();
        $moduleModelProduct = new BhsProduct_Module_Model();
        $res = array();
        $draw = intval($request->get('draw'));
        $data = array();
        $leadId = $request->get('leadId');
        try {
            $type = $request->get('type');
            $leads = [];
            if (isset($type) && $type == 'order') {
                $res2 = $moduleModel->makeApiCall('GET', 'SO/SaleOrderItems/' . $leadId);
                $leads = $res2['body']->data;
            } else {
                $res2 = $moduleModel->makeApiCall('GET', 'lead/' . $leadId);
                $leads[] = $res2['body']->data;
            }
            foreach ($leads as $lead) {
                $productId = $lead->prodId;
                $product = $moduleModelProduct->getRecordById('products', $productId);
                $data[] = array(
                    ' <span class="text-info" data-id="' . $product->prodId . '">' . $product->name . '</span> ',
                    $product->dscr,
                    $product->dscrForAgent,
                );
            }
            $res = array(
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'draw' => $draw,
                'data' => $data,
            );
        } catch (Exception $exception) {
            $res = array(
                'success' => false,
                'error' => $exception->getMessage(),
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    public function getList(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $moduleModel = new BhsOrderProcessing_Module_Model();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'lead');
            foreach ($rows as $row) {
                $id = $row['leadId'];
                $item = $row;
                $item['checkbox'] = '<input type="checkbox" class="check-item" />';
                $item['leadId'] = '<a class="text-info" href="index.php?module=' . $moduleName . '&view=Edit&type=UpdateLead&recordId=' . $id . '">' . $id . '</a>';
                if ($item['leadStatusName'] == 'new') {
                    $item['leadStatusName'] = '<span class="btn btn-custom btnYellor text-tr-bold">' . vtranslate('LBL_NEW', $moduleName) . '</span>';
                } elseif ($item['leadStatusName'] == 'rejected') {
                    $item['leadStatusName'] = '<span  title="' . $this->getLastLines($item['userDefin05'], 1) . '" class="btn btn-custom btnOrage ">' . vtranslate('LBL_REJECTED', $moduleName) . ' <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatusName'] == 'callback propect' || $item['leadStatusName'] == 'callback consult') {
                    $item['leadStatusName'] = '<span  title="' . $this->getLastLines($item['userDefin05'], 1) . '" class="btn btn-custom btnblue ">' . vtranslate('LBL_CALLBACK', $moduleName) . '<img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatusName'] == 'unreachable' || $item['leadStatusName'] == 'busy' || $item['leadStatusName'] == 'noanswer') {
                    $item['leadStatusName'] = '<span  title="' . $this->getLastLines($item['userDefin05'], 1) . '" class="btn btn-custom btnViolet ">' . vtranslate('UnCall', $moduleName) . '<img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatusName'] == 'invalid' || $item['leadStatusName'] == 'duplicated') {
                    $item['leadStatusName'] = '<span class="btn btn-custom btnblack">' . vtranslate('LBL_TRASH', $moduleName) . '</span>';
                } else {
                    $item['leadStatusName'] = '<span class="btn btn-custom btnGreen ">' . $item['leadStatusName'] . '</span>';
                }
                $item['ccCode'] = CurrencyField::convertToUserFormat($item['ccCode'], null, true);
                $item['address'] = '<span title="' . $row['address'] . '">' . $row['address'] . '</span>';
                $item['prodName'] = (isset($row['prodName'])) ? $row['prodName'] : '';
                $item['agcId'] = (isset($row['agcId'])) ? $row['agcId'] : '';
                $item['phone'] = (isset($row['phone'])) ? $row['phone'] : '';
                $item['amount'] = (isset($row['amount'])) ? CurrencyField::convertToUserFormat($row['amount'], null, true) : '';
                $item['assignedName'] = (isset($row['assignedName'])) ? $row['assignedName'] : '';
                $item['createdate'] = !empty($item['createdate'])? Date('d/m/Y H:i:s', strtotime($item['createdate'])):"";
                $item['modifydate'] = !empty($item['modifydate'])? Date('d/m/Y H:i:s', strtotime($item['modifydate'])):"";
                $item['totalCall'] = (isset($row['totalCall'])) ? $row['totalCall'] : '';

                $data[] = $item;
            }

            $res = array(
                'success' => true,
                'message' => 'success',
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception) {
            $res = array(
                'success' => false,
                'message' => $exception->getMessage(),
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    public function getLastLines($string, $n = 1)
    {
        $lines = explode("\n", $string);
        // if(!$Lines[count($lines)-1]) {
        //     $n = $n +1;
        // }
        $lines = array_slice($lines, -$n);

        return implode("\n", $lines);
    }

    public function getSimilarOrder(Vtiger_Request $request)
    {
        global $adb;
        $moduleName = $request->getModule();
        $res = array();
        $draw = intval($request->get('draw'));
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $prodId = $request->get('prodId');
            $rows = $moduleModel->getRowsRefactor($request, 'SO/getSimilarOrder/' . $prodId);
            $data = [];
            foreach ($rows as $row) {
                $data[] = [
                    $row['soId'],
                    $row['leadName'],
                    $row['leadPhone'],
                    $row['amount'],
                    //$row['paymentMethod'],
                    $row['status'],
                ];
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

    public function getAllProducts(Vtiger_Request $request)
    {
        $res = array();
        $prodId = $request->get('prodId');
        try {
            $moduleModel = new BhsOrderProcessing_Module_Model();
            $stockProduct = $moduleModel->makeApiCall('GET', 'products/StockByProduct?prodId=' . $prodId);
            $stockProduct = $stockProduct['body'];
            $res = array(
                'success' => true,
                'error' => '',
                'data' => $stockProduct,
            );
        } catch (Exception $exception) {
            $res = array(
                'success' => false,
                'error' => $exception->getMessage(),
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    public function getProductInfo(Vtiger_Request $request)
    {
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

    public function getAgent(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $moduleModel = new BhsOrderProcessing_Module_Model();
        try {
            $data = array();
            $cpid = $request->get('id');
            $agents = $moduleModel->makeApiCall('GET', 'campaign/' . $cpid . '/agents')['body']->data;
            foreach ($agents as $agent) {
                if ($agent->userType == "agent") {
                    $data[] = [
                        'id' => $agent->userId,
                        'name' => $agent->userName
                    ];
                }
            }
            $res = $data;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function savePhoneNumber(Vtiger_Request $vRequest)
    {
        $request = [];
        $response = new Vtiger_Response();
        $moduleModel = new BhsOrderProcessing_Module_Model();
        $request['query'] = [
            'index' => $vRequest->get('index'),
            'phone' => $vRequest->get('phoneNum')
        ];
        $res = $moduleModel->makeApiCall('PUT', 'lead/' . $vRequest->get('leadId') . '/phone', $request);
        $response->setResult($res);
        $response->emit();
    }

    public function getLeadHistory(Vtiger_Request $request)
    {
        $moduleModel = new BhsOrderProcessing_Module_Model();
        $currentRole = $moduleModel->userTMS->roleId[0];
        $roleAgent = $moduleModel->roleAgent;
        $draw = intval($request->get('draw'));

        try {
            $data = array();
            $leadId = $request->get('leadId');
            $limit = $request->get('length');
            $offset = $request->get('start');
            $res2 = $moduleModel->makeApiCall('GET', "lead/$leadId/history?limit=$limit&offset=$offset");
            $leadHistories = $res2['body']->data;
            if ($leadHistories != null) {
                foreach ($leadHistories as $leadHistory) {
                    if (intval($leadHistory->newValue) !== 12 || $currentRole !== $roleAgent) {
                        $data[] = array(
                            'dateTime' => date('d/m/Y H:i:s', strtotime($leadHistory->changetime)),
                            'userName' => $leadHistory->userName,
                            'statusName' => $leadHistory->statusName,
                            'comment' => '<span title="' . $leadHistory->comment . '">' . $leadHistory->comment . '</span>'
                        );
                    }
                }
            }

            $res = array(
                'recordsTotal' => count($res2['body'] ? $res2['body']->total : 0),
                'recordsFiltered' => count($data),
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
