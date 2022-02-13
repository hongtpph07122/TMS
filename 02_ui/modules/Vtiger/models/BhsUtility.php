<?php

class Vtiger_BhsUtility_Model extends Vtiger_Utility_Model {
    public static function displayDatetimeWithUserFormat($strDateTime, $opts){
        global $current_user;
        $strDateTime = explode('+', $strDateTime)[0];
        $delimiterDateTime = $opts['delimiterDateTime'];
        if (empty($delimiterDateTime)){
            $delimiterDateTime = 'T';
        }
        $strDateTime = explode($delimiterDateTime, $strDateTime);
        $strDateTime[1] = explode('.', $strDateTime[1]);
        $strDateTime[1] = $strDateTime[1][0];
        $strDateTime = implode(' ', $strDateTime);
        $obj = new Vtiger_Datetime_UIType();
        $oldTimeZone = $current_user->time_zone;
        $current_user->time_zone = 'UTC';
        $strDateTime = $obj->getDisplayValue($strDateTime);
        $current_user->time_zone = $oldTimeZone;
        return $strDateTime;
    }

    public static function displayDateWithUserFormat($strDateTime, $opts){
        global $current_user;
        $strDateTime = explode('+', $strDateTime)[0];
        $delimiterDateTime = $opts['delimiterDateTime'];
        if (empty($delimiterDateTime)){
            $delimiterDateTime = 'T';
        }
        $strDateTime = explode($delimiterDateTime, $strDateTime)[0];
        $obj = new Vtiger_Date_UIType();
        $strDateTime = $obj->getDisplayValue($strDateTime);
        return $strDateTime;
    }

    public static function formatDateTimeAPI($strDateTime, $opts){
        global $current_user;
        if (empty($datetime)) {
            $datetime = new DateTime();
            // $datetime = DateTime::createFromFormat('Y-m-d H:i:s', '2019-05-04 11:30:21');
        }
        $display_date_1 = $datetime->format('Ymd');
        $display_date_2 = $datetime->format('His');
        return $display_date_1 . 'H' . $display_date_2;
    }

    public static function formatDateAPI($datetime){
        global $current_user;
        if (empty($datetime)) {
            $datetime = new DateTime();
            // $datetime = DateTime::createFromFormat('Y-m-d H:i:s', '2019-05-04 11:30:21');
        }
        $display_date = $datetime->format('Ymd');
        return $display_date;
    }

    public static function getDateTimeFromString($str, $format='Y-m-d H:i:s'){
        $datetime = DateTime::createFromFormat($format, $str);
        return $datetime;
    }

    public static function getDateFromString($str, $format=''){
        global $current_user;
        if (empty($format)){
            $format = $current_user->date_format;
        }
        if ($format == 'yyyy-mm-dd'){
            $format = 'Y-m-d';
        } elseif ($format == 'yyyy-dd-mm'){
            $format = 'Y-d-m';
        } elseif ($format == ''){
            $format = 'd-m-Y';
        } elseif ($format == 'dd-mm-yyyy'){
            $format = 'd-m-Y';
        } elseif ($format == 'yyyyddmm'){
            $format = 'Ydm';
        } elseif ($format == 'yyyymmdd'){
            $format = 'Ymd';
        }
        $datetime = DateTime::createFromFormat($format, $str);
        return $datetime;
    }

    public static function getDispNameOfConstant($type, $id){
        $label = '';
        $constants = Vtiger_Session::get('tms_constants');
        if (!empty($constants)){
            $label = $constants[$type]["$id"];
        }
        if (empty($label)){
            $label = "$id";
        }
        return $label;
    }

    public static function getConstantByType($type){
        $constants = Vtiger_Session::get('tms_constants');
        $res = $constants[$type];
        if (empty($res)){
            $res = array();
        }
        return $res;
    }
}