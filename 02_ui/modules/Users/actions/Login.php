<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class Users_Login_Action extends Vtiger_Action_Controller
{
    private $user_type;
    public function loginRequired()
    {
        return false;
    }

    public function checkPermission(Vtiger_Request $request)
    {
        return true;
    }

    public function process(Vtiger_Request $request)
    {
        $username = $request->get('username');
        $password = $request->getRaw('password');
        $loginType = $request->get('login_type');
        $userLogin = '';
        // Hard code for user admin, It must be remove when system is deployed
        if($username != 'admin' && !$password != 'admin') {
            $userLogin = $this->apiLogin($username, $password, $loginType);
        }
        if($userLogin) {
            $username = $userLogin['username'];
            $password = $userLogin['password'];
        }
        
        $user = CRMEntity::getInstance('Users');
        $user->column_fields['user_name'] = $username;
        if ($user->doLogin($password)) {

            session_regenerate_id(true); // to overcome session id reuse.

            $userid = $user->retrieve_user_id($username);
            Vtiger_Session::set('AUTHUSERID', $userid);

            // For Backward compatability
            // TODO Remove when switch-to-old look is not needed
            $_SESSION['authenticated_user_id'] = $userid;
            $_SESSION['app_unique_key'] = vglobal('application_unique_key');
            $_SESSION['authenticated_user_language'] = vglobal('default_language');

            //Enabled session variable for KCFINDER
            $_SESSION['KCFINDER'] = array();
            $_SESSION['KCFINDER']['disabled'] = false;
            $_SESSION['KCFINDER']['uploadURL'] = "test/upload";
            $_SESSION['KCFINDER']['uploadDir'] = "../test/upload";
            $deniedExts = implode(" ", vglobal('upload_badext'));
            $_SESSION['KCFINDER']['deniedExts'] = $deniedExts;
            // End

            //Track the login History
            $moduleModel = Users_Module_Model::getInstance('Users');
            $moduleModel->saveLoginHistory($user->column_fields['user_name']);
            //End

            if (isset($_SESSION['return_params'])) {
                $return_params = $_SESSION['return_params'];
			}
			
            header('Location: index.php');
            //header ('Location: index.php?module=Users&parent=Settings&view=SystemSetup');
            exit();
        } else {
            if($loginType == 'employee') {
                header('Location: index.php?module=Users&view=Employee&error=login');
            }else {
                header('Location: index.php?module=Users&parent=Settings&view=Login&error=login');
            }
            exit;
        }
    }
    /**
     * Login to API and get user infomation
     */
    public function apiLogin($username, $password, $loginType) {
        $bhModule = new Vtiger_BhsModule_Model();
        $bhModule->getAccessToken([
            'username' => $username,
            'password' => $password,
        ]);
        $this->getListRolesVtiger();
        $userTMS = Vtiger_Session::get('agent_info');
        
        if (!$userTMS) {
            if($loginType == 'employee') {
                header('Location: index.php?module=Users&view=Employee&error=login');
            }else {
                header('Location: index.php?module=Users&parent=Settings&view=Login&error=login');
            }
            exit;
        }
        $user_info = $userTMS->info;
        $numRole = count($userTMS->roleId);
        if($numRole == 0) {
            if($loginType == 'employee') {
                header('Location: index.php?module=Users&view=Employee&error=login');
            }else {
                header('Location: index.php?module=Users&parent=Settings&view=Login&error=login');
            }
            exit;
        }
        if($numRole == 1) {
            $user_roleId = $userTMS->roleId[0];
        }else {
            $user_roleId = max($userTMS->roleId);

        }

		global $application_unique_key;
        $userId = $user_info->userId;
        
		$username = md5('username' . $application_unique_key . $userId);
		$password = md5('password' . $application_unique_key . $userId);
        $last_name = md5('last_name' . $application_unique_key . $userId);
       
		$newUserId = $this->saveUser([
			'last_name' =>$last_name,
			'user_name' => $username,
            'user_password' => $password,
            'roleId' => $user_roleId,
            'login_type' => $loginType
        ]);
        //die('$newUserId: ' . $newUserId);
        if($newUserId === -1) {
            if($loginType == 'employee') {
                header('Location: index.php?module=Users&view=Employee&error=login');
            }else {
                header('Location: index.php?module=Users&parent=Settings&view=Login&error=login');
            }
            exit;
        }

        return [
            'username' => $username, 
            'password' => $password
        ];
    }
    /**
     * Save fake user mapping with API by user Id
     */
    public function saveUser($data)
    {
        $isUserExists = Users_Record_Model::isUserExists($data['user_name']);
        $role_id_to_assign = $this->mappingRole2User($data['roleId']);
        if($isUserExists || !$role_id_to_assign) {
            return 0;
        }

        $user_email = $data['user_name'] . '@admin.com';
        
        $USERS_LDAP = array(
            'mode' => '',
            'user_name' => $data['user_name'],
            'email1' => $user_email,
			'last_name' => $data['last_name'],
            'user_password' => $data['user_password'],
            'confirm_password' => $data['user_password'],
            'roleid' => $role_id_to_assign,
            'status' => 'Active',
            'is_admin' => ($this->user_type == 'admin') ? 'on' : 'off',
            'tz' => 'Europe/Berlin',
            'holidays' => 'de,en_uk,fr,it,us',
            'workdays' => '0,1,2,3,4,5,6',
            'weekstart' => '1',
            'namedays' => '',
            'currency_id' => '1',
            'reminder_interval' => '1 Minute',
            'reminder_next_time' => date('Y-m-d H:i'),
            'date_format' => 'yyyy-mm-dd',
            'hour_format' => '12',
            'start_hour' => '08:00',
            'end_hour' => '23:00',
            'no_of_currency_decimals' => 2,
            'truncate_trailing_zeros' => 1,
            'currency_grouping_pattern' => '123,456,789',
            'internal_mailer' => '1',
            'activity_view' => 'This Week',
			'lead_view' => 'Today');

        $user = CRMEntity::getInstance('Users');
        $user->column_fields = $USERS_LDAP;
        $isUserExists = Users_Record_Model::isUserExists($data['user_name']);
        if (!$isUserExists) {
            $new_user_id = $user->saveentity("Users");
            //Insert to vtiger_crmsetup table
            $db = PearDatabase::getInstance();
            $query = "INSERT IGNORE INTO  vtiger_crmsetup(userid,setup_status) VALUES(?,?)";
            $result = $db->pquery($query, array($user->id, 1));
            //Update to User Privilege
            global $root_directory;
            require_once $root_directory . 'modules/Users/CreateUserPrivilegeFile.php';
            require_once $root_directory . 'vtlib/Vtiger/AccessControl.php';
            createUserPrivilegesfile($user->id);
            createUserSharingPrivilegesfile($user->id);
            Vtiger_AccessControl::clearUserPrivileges($user->id);

            return $user->id;
        }

        return -1;
	}
    /**
     * Get role ID mirror role from API
     */
	function mappingRole2User($roleId) {
        $rolesAPI = ['admin', 'management', 'team_leader', 'validation', 'agent', 'accountant', 'customer_service', 'agent mkt'];
        $bhModule = new Vtiger_BhsModule_Model();
        $roleList = $bhModule->roleList();
        $roleVtiger = $this->getListRolesVtiger();
        $roleName = '';
        $roleVtigerId = '';

        if($roleList) {
            foreach($roleList as $role) {
                if($role->roleId == $roleId) {
                    $roleName = $role->name;
                    break;
                }
            }
            if( $roleName== '' || !in_array($roleName, $rolesAPI)) {
                $roleName = 'employee';
                $this->user_type = 'employee';
            }
            if($roleName) {
                $this->user_type = $roleName;
                foreach($roleVtiger as $rv) {
                    $vRolename = str_replace(' ', '_', $rv['rolename']);
                    $vRolename = strtolower($vRolename);
                    if($vRolename == $roleName) {
                        $roleVtigerId = $rv['roleid'];
                        break;
                    }
                }
             }
             return $roleVtigerId;
        }else {
            return '';
        }
    }
    /**
     * Get all roles of Vtiger
     */
    function getListRolesVtiger() {
        $db = PearDatabase::getInstance();
        $query = "SELECT * FROM vtiger_role";
        $result = $db->pquery($query);
        $roleCount = $db->num_rows($result);
		$roles = array();
		for($i=0; $i < $roleCount; $i++) {
			$roles[] = $db->query_result_rowdata($result, $i);
        }
        
        return $roles;
    }
}
