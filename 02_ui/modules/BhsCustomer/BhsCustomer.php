<?php
class BhsCustomer extends CRMEntity {
	var $db, $log; // Used in class functions of CRMEntity

	var $table_name = 'vtiger_bhscustomer';
	var $table_index= 'bhscustomerid';
	var $column_fields = Array();

	/** Indicator if this is a custom module or standard module */
	var $IsCustomModule = true;

	/**
	 * Mandatory table for supporting custom fields.
	 */
	var $customFieldTable = Array('vtiger_bhscustomercf', 'bhscustomerid');

	/**
	 * Mandatory for Saving, Include tables related to this module.
	 */
	var $tab_name = Array('vtiger_crmentity', 'vtiger_bhscustomer', 'vtiger_bhscustomercf');

	/**
	 * Mandatory for Saving, Include tablename and tablekey columnname here.
	 */
	var $tab_name_index = Array(
		'vtiger_crmentity' => 'crmid',
		'vtiger_bhscustomer' => 'bhscustomerid',
		'vtiger_bhscustomercf'=>'bhscustomerid');

	/**
	 * Mandatory for Listing (Related listview)
	 */
	var $list_fields = Array (
		/* Format: Field Label => Array(tablename, columnname) */
		// tablename should not have prefix 'vtiger_'
        'Name' => Array('bhscustomer', 'bhscustomer_name'),
		'CreatedTime' => Array('crmentity', 'createdtime'),
	);
	var $list_fields_name = Array (
		/* Format: Field Label => fieldname */
		'Name' => 'bhscustomer_name',
		'CreatedTime' => 'createdtime',
	);

	// Make the field link to detail view
	var $list_link_field = '';

	// For Popup listview and UI type support
	var $search_fields = Array(
		/* Format: Field Label => Array(tablename, columnname) */
		// tablename should not have prefix 'vtiger_'
        'Name' => Array('bhscustomer', 'bhscustomer_name'),
        'CreatedTime' => Array('crmentity', 'createdtime'),
	);
	var $search_fields_name = Array (
		/* Format: Field Label => fieldname */
        'Name' => 'bhscustomer_name',
        'CreatedTime' => 'createdtime',
	);

	// For Popup window record selection
	var $popup_fields = Array ();

	// Placeholder for sort fields - All the fields will be initialized for Sorting through initSortFields
	var $sortby_fields = Array();

	// For Alphabetical search
	var $def_basicsearch_col = 'bhscustomer_name';

	// Column value to use on detail view record text display
	var $def_detailview_recname = '';

	// Required Information for enabling Import feature
	var $required_fields = Array ();

	// Used when enabling/disabling the mandatory fields for the module.
	// Refers to vtiger_field.fieldname values.
	var $mandatory_fields = Array('bhscustomer_name');

	var $default_order_by = 'bhscustomer_name';
	var $default_sort_order='ASC';

	function __construct() {
		global $log;
		$this->column_fields = getColumnFields(get_class($this));
		$this->db = new PearDatabase();
		$this->log = $log;
	}
	/**
	 * Invoked when special actions are performed on the module.
	 * @param String Module name
	 * @param String Event Type
	 */
	function save_module(){

	}
	function vtlib_handler($moduleName, $eventType) {
		require_once('include/utils/utils.php');
		global $adb;

		if($eventType == 'module.postinstall') {
            $this->addUserSpecificTable();
            $this->addDefaultModuleTypeEntity();
            $this->addEntityIdentifier();
            $this->addModuleIcon();
		} else if($eventType == 'module.disabled') {
		} else if($eventType == 'module.enabled') {
            $this->addUserSpecificTable();
		} else if($eventType == 'module.preuninstall') {
			vtws_deleteWebserviceEntity('BhsCustomer');
			// TODO Handle actions when this module is about to be deleted.
		} else if($eventType == 'module.preupdate') {
			// TODO Handle actions before this module is updated.
		} else if($eventType == 'module.postupdate') {
			// TODO Handle actions after this module is updated.
            $this->addUserSpecificTable();
            $this->addDefaultModuleTypeEntity();
            $this->addEntityIdentifier();
            $this->addModuleIcon();
		}
	}

    public function addModuleIcon(){
        global $root_directory;
        global $vtiger_current_version;
        $source1 = $root_directory . '/modules/BhsCustomer/resources/BhsCustomer.png';
        $source2 = $root_directory . '/modules/BhsCustomer/resources/moduleImages/BhsCustomer.png';
        $dest1 = $root_directory . '/layouts/v7/skins/images/BhsCustomer.png';
        copy($source1, $dest1);
        $dest2 = $root_directory . '/layouts/v7/skins/images/moduleImages/BhsCustomer.png';
        copy($source2, $dest2);
    }
    
    public function addDefaultModuleTypeEntity(){
		global $adb;
        // Check entity module
        $rs=$adb->query("SELECT * FROM `vtiger_ws_entity` WHERE `name`='BhsCustomer'");
        if($adb->num_rows($rs) == 0) {
            $adb->query("UPDATE vtiger_ws_entity_seq SET id=(SELECT MAX(id) FROM vtiger_ws_entity)");
            $entityId = $adb->getUniqueID("vtiger_ws_entity");
            $adb->pquery("INSERT INTO `vtiger_ws_entity` (`id`, `name`, `handler_path`, `handler_class`, `ismodule`) VALUES (?, ?, ?, ?, ?);",
                array($entityId, 'BhsCustomer', 'include/Webservices/VtigerModuleOperation.php', 'VtigerModuleOperation', '1'));
        }
    }

    public function addEntityIdentifier(){
        global $adb;
        $moduleName = 'BhsCustomer';
        $sql = "SELECT tabid FROM vtiger_tab WHERE `name` = ?";
        $result = $adb->pquery($sql, array($moduleName));
        if($adb->num_rows($result) > 0){
            $tabid = $adb->fetchByAssoc($result, 0)['tabid'];
            $result2=$adb->pquery("SELECT tabid FROM vtiger_entityname WHERE tablename=? AND tabid=?", array($this->table_name, $tabid));
            if($adb->num_rows($result2)==0){
                $adb->pquery("INSERT INTO vtiger_entityname(tabid, modulename, tablename, fieldname, entityidfield, entityidcolumn) VALUES(?,?,?,?,?,?)",
                    Array($tabid, $moduleName, $this->table_name, 'bhscustomer_name', $this->table_index, $this->table_index));
            }else{
                $adb->pquery("UPDATE vtiger_entityname SET fieldname=?,entityidfield=?,entityidcolumn=? WHERE tablename=? AND tabid=?",
                    array('from_module,from_id', $this->table_index, $this->table_index, $this->table_name, $tabid));
            }
        }
    }
    
    public function addUserSpecificTable(){
        // Add table xyz_user_field
        global $vtiger_current_version;
        if(version_compare($vtiger_current_version, '7.0.0', '<')) {
            // Nothing
        }else{
            $moduleName = 'BhsCustomer';
            $moduleUserSpecificTable = Vtiger_Functions::getUserSpecificTableName($moduleName);
            if (!Vtiger_Utils::CheckTable($moduleUserSpecificTable)) {
                Vtiger_Utils::CreateTable($moduleUserSpecificTable,
                    '(`recordid` INT(19) NOT NULL,
					   `userid` INT(19) NOT NULL,
					   `starred` varchar(100) NULL,
					   Index `record_user_idx` (`recordid`, `userid`)
						)', true);
            }
        }
    }
}
?>