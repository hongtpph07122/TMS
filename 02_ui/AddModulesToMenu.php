<?php
chdir(dirname(__FILE__));
require_once('include/utils/utils.php');
require_once('includes/runtime/Globals.php');
require_once('includes/runtime/LanguageHandler.php');
require_once('includes/runtime/BaseModel.php');
require_once('includes/Loader.php');
require_once('modules/Vtiger/models/Record.php');
require_once('modules/Users/models/Record.php');
require_once("vtlib/Vtiger/Module.php");
require_once("vtlib/Vtiger/Block.php");
require_once("vtlib/Vtiger/Field.php");
//Add custom field in main table
// ini_set('display_errors', 1);error_reporting(E_ALL & ~E_NOTICE & ~E_DEPRECATED & ~E_STRICT);

global $adb;

$modules = array(
    'BhsOrders',
    'BhsCampaign',
    'BhsAgentMonitoring',
    'BhsCustomer',
    'BhsOrderProcessing',
    'BhsCdrs',
    'BhsMissedCalls',
    'BhsCampaignManagement',
    'BhsValidation',
    'BhsSap',
    'BhsShipping',
    'BhsShippingPending',
    'BhsCallbackList',
    'BhsCallbackManagement',
    'BhsProduct',
    'BhsStock',
    'BhsDnc',
    'BhsBlacklist',
    'BhsAgentGroup',
    'BhsCampaignList',
    'BhsCampaignReport',
    'BhsAgentReport',
    'BhsCallcenterReport',
);

$apps = Vtiger_MenuStructure_Model::getAppMenuList();

$idx = 1;
foreach ($modules as $module) {
    $tabid = getTabid($module);
    foreach ($apps as $app) {
        $sql = "SELECT * FROM vtiger_app2tab WHERE appname=? AND tabid=?";
        $res = $adb->pquery($sql, array($app, $tabid));
        if ($adb->num_rows($res) == 0) {
            $adb->pquery("INSERT INTO `vtiger_app2tab` (tabid, appname, sequence, visible) VALUES (?, ?, ?, ?)",
                array($tabid, $app, $idx, 0));
        }
    }
}

echo 'DONE!!!';

