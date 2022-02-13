<?php

require 'modules/Vtiger/models/API.php';
/**
 * Created by PhpStorm.
 * User: DucVA
 * Date: 4/11/2019
 * Time: 10:58 AM
 */
class Vtiger_BhsModule_Model extends Vtiger_Module_Model
{
    protected $API;
    protected $firstRecord;
    protected $recordsTotal = 0;
    protected $recordsFiltered = 0;
    protected $filterCols = array();

    public function __construct()
    {
        $this->API = new API();
        $this->firstRecord = array();
        parent::__construct();
    }

    public function setFilters($arr)
    {
        // $arr = array(
        //     'name' => 'col_name',
        //     'datatype' => 'date', // datatype: number, text, date, currency...
        // );
        $this->filterCols = $arr;
    }

    public function getRecordTotal()
    {
        return $this->recordsTotal;
    }

    public function getRecordsFiltered()
    {
        return $this->recordsFiltered;
    }

    public function getTotalFromApiResult($apiResult)
    {

        $body = $apiResult['body'];
        $this->recordsTotal = !empty($body->recordsTotal) ? $body->recordsTotal : 0;
        $this->recordsFiltered = !empty($body->recordsFiltered) ? $body->recordsFiltered : 0;
    }

    public function buildDummyRequest()
    {
        $request = array();
        $params = array();
        $params['columns'] = array(
            array(
                'data' => 0,
                'name' => '',
                'searchable' => false,
                'orderable' => false,
                'search' => array(
                    'value' => '',
                    'regex' => false,
                )
            )
        );
        $params['start'] = 0;
        $params['length'] = 1;
        $params['draw'] = 1;
        $params['search'] = array(
            'value' => '',
            'regex' => false,
        );
        $request['is_gridview'] = true;
        $request['form_params'] = $params;
        return $request;
    }

    public function getHeaders()
    {
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        return $this->getHeadersFromApiResult();
    }

    public function getFirstRecord()
    {
        $this->firstRecord = array();
    }

    public function getColumnDefs()
    {
        return array();
    }

    public function removeFirstColumns($columns, $ids)
    {
        $rows = array();
        foreach ($ids as $id) {
            unset($columns[$id]);
        }
        $idx = 0;
        foreach ($columns as $column) {
            $row = $column;
            $rows[] = $row;
            $idx += 1;
        }
        return $rows;
    }

    public function convertFirstColumns($columns, $ids) {
        foreach ($ids as $key => $item) {
            foreach ($item as $id => $value) {
                $columns[$key][$id] =  $value;
            }
        }

        $idx = 0;
        foreach ($columns as $column) {
            $row = $column;
            $rows[] = $row;
            $idx += 1;
        }
        return $rows;
    }

    public function getRows(Vtiger_Request $request, $function, $needRemove)
    {
        try {
            if (empty($needRemove)) {
                $needRemove = array();
            }
            $request2 = array();
            $request2['is_gridview'] = true;
            $form_params = $_REQUEST;
            $columns = $form_params['columns'];
            if (!empty($needRemove)) {
                $columns = $this->removeFirstColumns($columns, $needRemove);
            }
            $form_params['columns'] = $columns;
            $form_params['draw'] = $request->get('draw');
            $form_params['columns'] = $request->get('columns');
            $form_params['order'] = $request->get('order');
            $form_params['search']['value'] = $request->get('keyword');
            $request2['form_params'] = $form_params;
            $apiResult = $this->makeApiCall('POST', $function, $request2);
            $this->getTotalFromApiResult($apiResult);
            return $this->getContentsFromApiResult($apiResult);
        } catch (Exception $exception) {
            return array();
        }
    }

    public function getDatatypeByColumnName($name)
    {
        $datatype = '';
        foreach ($this->filterCols as $filterCol) {
            if ($filterCol['name'] == $name) {
                $datatype = $filterCol['datatype'];
                break;
            }
        }
        return $datatype;
    }

    public function getData($name)
    {
        $datatype = '';
        foreach ($this->filterCols as $filterCol) {
            if ($filterCol['name'] == $name) {
                $datatype = $filterCol['data'];
                break;
            }
        }
        return $datatype;
    }

    public function getHeadersFromApiResult($result)
    {
        if (empty($result)) {
            $result = $this->firstRecord;
        }
        $result = $result['body'];
        $headersResult = $result->headers;
        $header = array();
        foreach ($headersResult as $item) {
            $label = 'LBL_' . $item;
            $datatype = $this->getDatatypeByColumnName($item); // datatype: number, text, date, currency...
            $data = $this->getData($item);
            $has_filter = 0;
            if (empty($datatype)) {
                $datatype = 'text';
            } else {
                $has_filter = 1;
            }
            $name = $item;
            $header[] = array('label' => strtoupper($label), 'datatype' => $datatype, 'name' => $name, 'has_filter' => $has_filter, 'datax' => $data);
        }
        return $header;
    }

    public function getColumnDefsFromApiResult($firstIdx = 0)
    {
        $result = $this->firstRecord;
        $result = $result['body'];
        $headersResult = $result->headers;
        $columnDefs = array();
        $idx = $firstIdx;
        $allowSearch = array();
        $notAllowSearch = array();
        foreach ($headersResult as $header) {
            $flag = false;
            foreach ($this->filterCols as $filterCol) {
                if ($filterCol['name'] == $header) {
                    $flag = true;
                    $allowSearch[] = $idx;
                    break;
                }
            }
            if (!$flag) {
                $notAllowSearch[] = $idx;
            }
            $columnDefs[] = array('name' => $header, 'targets' => $idx);
            $columnDefs[] = array('data' => $header, 'targets' => $idx);
            $idx += 1;
        }
        $columnDefs[] = array('searchable' => true, 'targets' => $allowSearch);
        $columnDefs[] = array('searchable' => false, 'targets' => $notAllowSearch);

        // $columnDefs = array(
        //     array(
        //         'className' => 'text-left',
        //         'targets' => array(2)
        //     ),
        //     array(
        //         'className' => 'text-right',
        //         'targets' => array(6)
        //     ),
        // );
        return $columnDefs;
    }

    public function getContentsFromApiResult($result)
    {
        $body = $result['body'];
        $rows = array();
        if (is_array($body->data)) {
            foreach ($body->data as $result) {
                $row = array();
                foreach ($result as $k => $v) {
                    $row[$k] = $v;
                }
                $rows[] = $row;
            }
        } else {
            $row = array();
            foreach ($body->data as $k => $v) {
                $row[$k] = $v;
            }
            $rows[] = $row;
        }

        // $rows = $body->rows;
        return $rows;
    }

    public function makeApiCall($method, $function, $request = array())
    {
        try {
            $method = ($method) ? $method : 'GET';
            $function = ($function) ? $function : '/callback/tables';
            if (!isset($request)) {
                $request = array();
            }
            $access_token = Vtiger_Session::get('access_token');
            $token_type = Vtiger_Session::get('token_type');
            if (empty($request['is_gridview'])) {
                $request['is_gridview'] = false;
            }
            $orgId = Vtiger_Session::get('agent_info')->info->orgId;
            $orgId = $orgId ? $orgId : 1;
            $request['headers'] = array(
                'Content-Type' => 'application/json;charset=UTF-8',
                'Authorization' => $token_type . ' ' . $access_token,
                'org_id' => $orgId
            );
            $result = $this->API->call($method, $function, $request);
            if ($result['code'] == 401) {
                $res2 = $this->getAccessToken();
                if ($res2['code'] == 200) {
                    $request['headers'] = array();
                    return $this->makeApiCall($method, $function, $request);
                }
            }

            return $result;
        } catch (Exception $exception) {
            $res = array(
                'code' => $exception->getCode(),
                'message' => $exception->getMessage(),
            );
            $this->API->logApi('Exception', $function, $res, $method);

            return $res;
        }
    }

    /**
     * @param string $type
     * @param string $function
     * @param  array  $data
     * @param  string  $method
     */
    public function logApi($type, $function, $data, $method)
    {
        $this->API->logApi($type, $function, $data, $method);
    }

    public function initializeConstants()
    {
        $constants = array();
        // @Todo: Integrate with API
        if (true) {
            $constants["business partner type"]["110"] = "agency";
            $constants["business partner type"]["111"] = "publisher";
            $constants["business partner type"]["120"] = "fulfillment";
            $constants["business partner type"]["121"] = "lastmile";
            $constants["business partner type"]["122"] = "fulfillment + lastmile";
            $constants["business partner type"]["130"] = "PBX";
            $constants["call status"]["71"] = "answer";
            $constants["call status"]["72"] = "noanswer";
            $constants["call status"]["73"] = "aban";
            $constants["campaign configuration status"]["0"] = "inactive";
            $constants["campaign configuration status"]["1"] = "active";
            $constants["campaign status"]["31"] = "running";
            $constants["campaign status"]["32"] = "stopped";
            $constants["campaign status"]["33"] = "waiting";
            $constants["campaign status"]["34"] = "stopping";
            $constants["campaign status"]["35"] = "deleted";
            $constants["capaign configuration type"]["81"] = "call strategy";
            $constants["capaign configuration type"]["82"] = "distribution rule";
            $constants["capaign configuration type"]["85"] = "group agent";
            $constants["capaign configuration type"]["86"] = "calling list";
            $constants["CDR direction"]["1"] = "inbound";
            $constants["CDR direction"]["2"] = "outbound";
            $constants["deleviry order status"]["51"] = "new";
            $constants["deleviry order status"]["52"] = "ready to pick";
            $constants["deleviry order status"]["53"] = "cancel";
            $constants["deleviry order status"]["54"] = "picking";
            $constants["deleviry order status"]["55"] = "in transit";
            $constants["deleviry order status"]["56"] = "return";
            $constants["deleviry order status"]["57"] = "reject";
            $constants["deleviry order status"]["58"] = "in preparation";
            $constants["deleviry order status"]["59"] = "delivered";
            $constants["lead status"]["1"] = "new";
            $constants["lead status"]["2"] = "approved";
            $constants["lead status"]["3"] = "rejected";
            $constants["lead status"]["4"] = "duplicated";
            $constants["lead status"]["5"] = "invalid";
            $constants["lead status"]["6"] = "closed";
            $constants["lead status"]["7"] = "unreachable";
            $constants["lead status"]["8"] = "callback consult";
            $constants["lead status"]["9"] = "callback propect";
            $constants["payment mothod"]["1"] = "COD";
            $constants["payment mothod"]["2"] = "banking";
            $constants["product status"]["0"] = "inactive";
            $constants["product status"]["1"] = "active";
            $constants["product status"]["2"] = "deleted";
            $constants["sale order status"]["41"] = "new";
            $constants["sale order status"]["42"] = "waiting for delivery";
            $constants["sale order status"]["43"] = "waiting for payment";
            $constants["sale order status"]["44"] = "cancel";
            $constants["sale order status"]["45"] = "success";
        }
        Vtiger_Session::set('tms_constants', $constants);
    }

    public function getRecordById($function, $id)
    {
        $res = $this->makeApiCall('GET', $function . '/' . $id);
        return $res['body']->data[0];
    }

    public function getAccessToken($api_info = null)
    {
        $res = $this->API->getAccessToken($api_info);
        if ($res['code'] == 200) {
            $apiMethod = 'GET';
            $function = 'agent/me';
            $res = $this->makeApiCall($apiMethod, $function);
            $this->logApi('Response', $function, $res);
            if ($res['code'] == 200) {
                $agentInfo = $res['body'];
                Vtiger_Session::set('agent_info', $agentInfo);
            }
        }
    }

    public function getRowsRefactor(Vtiger_Request $request, $function, $needRemove, $needConvert = [])
    {

        try {
            if (empty($needRemove)) {
                $needRemove = array();
            }
            if (empty($needConvert)) {
                $needConvert = array();
            }
            $request2 = array();
            //$request2['is_gridview'] = true;
            $form_params = $_REQUEST;
            $columns = $form_params['columns'];

            if (!empty($needConvert)) {
                $columns = $this->convertFirstColumns($columns, $needConvert);
            }
            if (!empty($needRemove)) {
                $columns = $this->removeFirstColumns($columns, $needRemove);
            }

            $dateFieldsFilter = ['stopdate', 'startdate', 'createdate', 'modifyDate', 'starttime', 'requestTime'];

            $form_params['columns'] = $columns;
            $form_params['draw'] = $request->get('draw');
            $form_params['columns'] = $request->get('columns');
            $form_params['order'] = $request->get('order');
            $form_params['search']['value'] = $request->get('keyword');
            $form_params['limit'] = $request->get('length');
            $form_params['offset'] = $request->get('start');
            foreach ($columns as $column) {
                if (isset($column['search']['value']) && $column['search']['value'] != '') {
                    if (in_array($column['name'], $dateFieldsFilter) && !empty($column['search']['value'])) {
                        $dateValue = explode('-', $column['search']['value']);
                        $startDate = date('YmdHis', strtotime(str_replace('/', '-', $dateValue[0])));
                        $endDate = date('YmdHis', strtotime(str_replace('/', '-', $dateValue[1])));
                        $dateValue = $startDate . '|' . $endDate;
                        $form_params[$column['name']] = $dateValue;
                    } else {
                        $form_params[$column['name']] = $column['search']['value'];
                    }

                }
            }
            $request2['query'] = $form_params;
            $apiResult = $this->makeApiCall('GET', $function, $request2);
            $apiResult['body']->recordsTotal = $apiResult['body']->total;
            $apiResult['body']->recordsFiltered = $apiResult['body']->total;
            $this->getTotalFromApiResult($apiResult);
            return $this->getContentsFromApiResult($apiResult);
        } catch (Exception $exception) {
            return array();
        }
    }

    public function getProvinces($opts = array())
    {
        $res = $this->makeApiCall('GET', 'location/province');
        $provinces = array();
        if ($res['code'] == 200) {
            $body = $res['body'];
            $provinces = $body->data;
        }
        return $provinces;
    }

    public function getDistrict($provinceId, $opts = array())
    {
        $districts = array();
        $params = array('provinceId' => $provinceId);
        $res = $this->makeApiCall('GET', 'location/district', array('query' => $params));
        if ($res['code'] == 200) {
            $body = $res['body'];
            $districts = $body->data;
        }
        return $districts;
    }

    public function getSubdistrict($districtId, $opts = array())
    {
        $districts = array();
        $params = array('districtId' => $districtId);
        $res = $this->makeApiCall('GET', 'location/subdistrict', array('query' => $params));
        if ($res['code'] == 200) {
            $body = $res['body'];
            $districts = $body->data;
        }

        return $districts;
    }

    public function getNeighborhood($townId, $opts = array())
    {
        $neighborhoods = array();
        $params = array('sdtId' => $townId);
        $res = $this->makeApiCall('GET', 'location/neighborhood', array('query' => $params));
        if ($res['code'] == 200) {
            $body = $res['body'];
            $neighborhoods = $body->data;
        }

        return $neighborhoods;
    }

    public function getZipcode($nbhId, $opts = array())
    {
        $zipcode = array();
        $params = array('nbhId' => $nbhId);
        $res = $this->makeApiCall('GET', 'location/zipcode', array('query' => $params));
        if ($res['code'] == 200) {
            $body = $res['body'];
            $zipcode = $body->data;
        }

        return $zipcode;
    }

    public function getRelatedZipcode($postalCode, $opts = array())
    {
        $zipcodes = array();
        $params = array('zipcode' => $postalCode);
        $res = $this->makeApiCall('GET', 'location/zipcode-location', array('query' => $params));
        if ($res['code'] == 200) {
            $body = $res['body'];
            $zipcodes = $body->data;
        }

        return $zipcodes;
    }

    public function roleList()
    {
        $roles = array();
        $res = $this->makeApiCall('GET', 'roles/all');
        if ($res['code'] == 200) {
            $body = $res['body'];
            $roles = $body->data;
        }

        return $roles;
    }
}
