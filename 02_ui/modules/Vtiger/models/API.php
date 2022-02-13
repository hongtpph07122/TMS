<?php
require 'api/guzzle-master/vendor/autoload.php';
require 'vendor/autoload.php';

use Monolog\Logger;
use Monolog\Handler\StreamHandler;
use Monolog\Handler\FirePHPHandler;
/**
 * Vtiger call API
 * refer: http://docs.guzzlephp.org/en/stable/overview.html
 */
class API
{
    /**
     * Call api
     * @param $method : GET, POST, PUT
     * @param $endpoint: url API
     * @param $request: array params ['json' => ["foo" => "bar"], ... ]
     */
    protected static $client;

    public function call($method, $function, $request) {
        try {
            global $API_ENDPOINT, $API_ENDPOINT_GRIDVIEW;
            if(!self::$client) {
                self::$client = new \GuzzleHttp\Client();
            }
            $is_gridview = $request['is_gridview'];
            unset($request['is_gridview']);
            $url = $API_ENDPOINT;
            if (!empty($is_gridview)){
                $url = $API_ENDPOINT_GRIDVIEW;
                $request['headers']['Content-Type'] = 'application/x-www-form-urlencoded';
            }

            $endpoint = $url . $function;

            $response = self::$client->request($method, $endpoint, $request);

            $body = '';
            $bodyStream = $response->getBody();
             while (!$bodyStream->eof()) {
                $body .= $bodyStream->read(102400);
            }
            if ($response->getHeader("Content-Type")[0] != "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
                $body = json_decode($bodyStream.'');
            }
            return [
                'code' => $response->getStatusCode(),
                'contentType' => $response->getHeaderLine('content-type'),
                'body' => $body,
            ];
        } catch (Exception $exception){
            $res = array(
                'code' => $exception->getCode(),
                'message' => $exception->getMessage(),
            );
            $this->logApi('Exception', $function, $res, $method);

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
        $data = ($data) ? $data : [];
        $method = ($method) ? $method : 'GET';

        $local_date = date('Y-m-d');
        $local_hour = date('H');
        $file_name = 'logs/tms-' . $local_date . '-from-' . $local_hour . '_00_00' . '-to-' . $local_hour . '_59_59' . '.log';

        $stream = new StreamHandler($file_name, Logger::DEBUG);
        // Create the logger
        $logger = new Logger('API ' . $type);
        // Now add some handlers
        $logger->pushHandler($stream);
        //$logger->pushHandler(new FirePHPHandler());

        //Only log if data is an array
        if (gettype($data) == 'array') {
            // You can now use your logger
            $logger->addInfo($method . ' : '. $function, $data);
        }
    }

    protected function getApiUserInfo(){
        global $current_user, $API_ENDPOINT_AUTH;
        $api_user = $current_user->tms_user;
        $api_pass = $current_user->tms_pass;
        if (empty($api_user)){
            $api_user = $API_ENDPOINT_AUTH['username'];
            $api_pass = $API_ENDPOINT_AUTH['password'];
        }
        return array('username' => $api_user, 'password' => $api_pass);
    }

    public function getAccessToken($api_info = null){
        global $API_ENDPOINT_AUTH;
        $endpoint = $API_ENDPOINT_AUTH['url'] . '/oauth/token';

        try{
            if(!self::$client) {
                self::$client = new \GuzzleHttp\Client();
            }
            if(!$api_info) {
                $api_info = $this->getApiUserInfo();
            }
            
            $request = array(
                'form_params'=>array(
                    'grant_type' => $API_ENDPOINT_AUTH['grant_type'],
                    'username' => $api_info['username'],
                    'password' => $api_info['password'],
                ),
                'connect_timeout' => 5,
                'timeout' => 5

            );
            $this->logApi('Request', $endpoint, $request, 'POST');
            $response = self::$client->request('POST', $endpoint, $request);

            $body = '';
            $bodyStream = $response->getBody();
            while (!$bodyStream->eof()) {
                $body .= $bodyStream->read(1024);
            }
            $body = json_decode($body);

            $statusCode = $response->getStatusCode();
            if ($statusCode == 200){
                Vtiger_Session::set('access_token', $body->access_token);
                setcookie("access_token",  $body->access_token);
                Vtiger_Session::set('token_type', $body->token_type);
            }

            $res = [
                'code' => $statusCode,
                'contentType' => $response->getHeaderLine('content-type'),
                'body' => $body,
            ];

            $this->logApi('Response', $endpoint, $res, 'POST');

            return $res;
        } catch (Exception $exception){
            $res = array(
                'code' => $exception->getCode(),
                'message' => $exception->getMessage(),
            );
            $this->logApi('Exception', $endpoint, $res, 'POST');
            return $res;
        }
    }
}
