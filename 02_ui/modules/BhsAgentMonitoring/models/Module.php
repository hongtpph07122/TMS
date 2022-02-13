<?php
require 'api/guzzle-master/vendor/autoload.php';
class BhsAgentMonitoring_Module_Model extends Vtiger_BhsModule_Model {
    public function getToken() {
        $access_token = Vtiger_Session::get('agent_info');
        return $access_token;
    }
    public function postCreateUser($param) {
        $request = array();
        $request['json'] = $param;
        $res = $this->makeApiCall('POST', 'agent', $request);
        return $res['body'];
    }
    // public function createUser($data){
    //     $request = array();
    //     $request['json'] = $data;
    //     $res = $this->makeApiCall('POST', 'agent', $request);
    //     return $res['body'];
    // }
    public function putEditUser($param) {
        $request = array();
        $request['json'] = $param;
        $res = $this->makeApiCall('PUT', 'agent', $request);
        return $res['body'];
    }
    public function postDeactiveUser($UserID) {
        $func = 'agent/'. $UserID . '/deactive';
        $res = $this->makeApiCall('POST',$func ,$request);
        return $res['body'];
    }
    public function postActiveUser($UserID) {
        $func = 'agent/'. $UserID . '/active';
        $res = $this->makeApiCall('POST',$func ,$request);
        return $res['body'];
    }
    public function deleteUser($id) {
        $func = 'agent/' .  $id ;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall('DELETE', $func, $request);
        return $res['body'];
    }
    public function getQuota() {
        $res = $this->makeApiCall('GET', 'agent/quota');
        if($res['code'] == 200) {
            return $res['body'];
        }
        
        return [];
    }
    public function getUserType() {
        $res = $this->makeApiCall('GET', 'agent/userType');
        if($res['code'] == 200) {
            return $res['body'];
        }
        
        return [];
    }
    public function getResetPassword($ID) {
        $func = 'agent/resetpass?userId=' . $ID;
        $res = $this->makeApiCall('GET', $func);
        if($res['code'] == 200) {
            return $res['body'];
        }
        
        return [];
    }
}
?>