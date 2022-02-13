<div  style="margin: 0px 15px;">
    <table id="tblMailList0" class="table table-bordered custom-dt" style="width:100%"
        data-url="index.php?module={$MODULE}&action=ActionAjax&mode=getList"
           data-columnDefs='{$COLUMN_DEFS}'>
        <thead>
        <tr>
            {include file='Bhs_Headers.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
            <th>Action</th>
        </tr>
        <tr class="bhs-filter">
            {include file='Bhs_Filters.tpl'|@vtemplate_path HEADERS=$HEADERS MODULE=$MODULE}
            <th></th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Information</h4>
        </div>
        <div class="modal-body">
          <p>An email will be sent to <span class="email">ahihi</span> include new password. Are you sure?.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" id="confirmReset">Confirm</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<div class="modal fade" id="myDeleteModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Information</h4>
        </div>
        <div class="modal-body">
          <p>User <span class="Deletefullname">ahihi</span> will be delete. Are you sure?.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" id="confirmDeleteUser">Confirm</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<div class="modal fade" id="myDisableModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Information</h4>
        </div>
        <div class="modal-body">
          <p>User <span class="Disablefullname">ahihi</span> will be disable. Are you sure?.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" id="confirmDisableUser">Confirm</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<div class="modal fade" id="myEnableModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Information</h4>
        </div>
        <div class="modal-body">
          <p>User <span class="Enablefullname">ahihi</span> will be enable. Are you sure?.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" id="confirmEnableUser">Confirm</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<div class="modal fade" id="modalEditUserInfo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
  aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header text-center">
          <h4 class="modal-title w-100 font-weight-bold">Edit User Information</h4>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: -25px;">
          <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body mx-3">
          <div class="md-form mb-4">
            <label data-error="wrong" data-success="right" for="user-id" style="float:left;margin: 5px 10px 0 0;">User ID</label>
            <span id="user-id" style="color: red;font-weight: bold; margin-left: 20%;width:17%;margin-top: 5px;float: left;"></span>
            <label data-error="wrong" data-success="right" style="margin: 5px 5px 0px 20px;float: left;">User Lock</label>
            <div style="float: left;margin: 5px 0 0 25px;">
              <button type="button" class="btn btn-sm btn-toggle active" data-toggle="button" aria-pressed="false" autocomplete="off" id="EdituserlockActive" name="EdituserlockActive">
                <div class="handle"></div>
              </button>
            </div>
          </div>

          <div class="md-form mb-4" style="padding-top: 7%;">
            <label data-error="wrong" data-success="right" for="full-name" style="float: left;margin: 5px 21px 0 0px;">Full Name (*)</label>
            <input type="text" id="full-name" class="form-control validate" style="float: left;width: 30%;">
            <label data-error="wrong" data-success="right" for="user-type" style="float: left;margin: 5px 5px 0 10px;">User Type (*)</label>
            <select  class="select2" id="user-type" style="width: 30%;">
              <option value=""></option>
              {foreach from=$USERTYPE  item=usertype}
                <option value="{$usertype->name}">{$usertype->name}</option>
              {/foreach} 
            </select>
          </div>

          <div class="md-form mb-4" style="padding-top: 3%;">
          <label data-error="wrong" data-success="right" for="user-phone" style="float: left;margin: 5px 45px 0 0px;">Phone (*)</label>
          <input type="text" id="user-phone" class="form-control validate" style="width: 77.7%;" maxlength="15" onkeyup="this.value=this.value.replace(/[^\d]/,'')">
          </div>

          <div class="md-form mb-4" style="padding-top: 3%;">
            <label data-error="wrong" data-success="right" for="user-birthday" style="float: left;margin: 5px 52px 0 0px;">Birthday</label>
            <div class='input-group date' id='datetimepickerEdit' style="width: 77.7%;">
                <input type='text' class="form-control" id="user-birthday"  maxlength="10"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
          </div>
          <div class="md-form mb-4" style="padding-top: 8%;">
            <label data-error="wrong" data-success="right" for="user-email" style="float: left;margin: 5px 50px 0 0px;">Email (*)</label>
            <input type="text" id="user-email" class="form-control validate" style="width: 77.7%;">
          </div>
          <div class="md-form mb-4" style="padding-top: 3%;">
            <label data-error="wrong" data-success="right" for="user-sipphone" style="float: left;margin: 5px 46px 0 0px;">Sipphone</label>
            <input type="text" id="user-sipphone" class="form-control validate" style="width: 77.7%;" maxlength="15">
          </div>
          <div class="md-form mb-4" style="padding-top: 3%;">
            <label data-error="wrong" data-success="right" for="user-address" style="float: left;margin: 5px 54px 0 0px;">Address</label>
            <input type="text" id="user-address" class="form-control validate" style="width: 77.7%;" maxlength="250">
          </div>
        </div>
        <div class="modal-footer">
          <button id="btnEditUserInfo" class="btn btn-info btn-rounded mb-4">Save</button>
          <button type="button" class="btn btn-default btn-rounded mb-4" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
<div class="modal fade" id="modalCreateUserInfo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
  aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header text-center">
          <h4 class="modal-title w-100 font-weight-bold">Create User Information</h4>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-top: -25px;">
          <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body mx-3">
          <div class="md-form mb-4" style="margin-bottom: 40px;">
            <label data-error="wrong" data-success="right" for="Createuser-name" style="float:left;margin: 5px 10px 0 0;">User name (*)</label>
            <input type="text" id="Createuser-name" class="form-control validate" style="float: left;width:30%;">
            <label data-error="wrong" data-success="right" style="margin: 5px 5px 0px 10px;float: left;">User Lock:</label>
            <div style="float: left;margin: 5px 0 0 20px;">
              <button type="button" class="btn btn-sm btn-toggle active" data-toggle="button" aria-pressed="true" autocomplete="off" id="CreateuserlockActive" name="CreateuserlockActive">
                <div class="handle"></div>
              </button>
            </div>           
            
          </div>

          <div class="md-form mb-4"  style="margin-bottom: 15%;">
            <label data-error="wrong" data-success="right" for="CreatePassword" style="float:left;margin: 5px 16px 0 0;">Password (*)</label>
            <input type="password" id="CreatePassword" class="form-control validate" style="float: left;width:30%;">
            <label data-error="wrong" data-success="right" for="Createuser-type" style="margin: 6px 10px 0px 10px;float: left;">User Type (*)</label>
            <select  class="select2" id="Createuser-type" style="float: left;width: 30%;">
              <option value=""></option> 
              {foreach from=$USERTYPE  item=usertype}
                <option value="{$usertype->name}">{$usertype->name}</option>
              {/foreach}             
            </select>
          </div>

          <div class="md-form mb-4" style="margin-bottom: 23%;">
          <label data-error="wrong" data-success="right" for="Createfull-name" style="float: left;margin: 5px 14px 0 0;">Full Name (*)</label>
          <input type="text" id="Createfull-name" class="form-control validate" style="float: left;width: 79%;">
          </div>

          <div class="md-form mb-4" style="margin-bottom: 31%;">
          <label data-error="wrong" data-success="right" for="Createuser-phone" style="float: left;margin: 5px 37px 0 0;">Phone (*)</label>
          <input type="text" id="Createuser-phone" class="form-control validate" style="float: left;width: 79%;" maxlength="15" onkeyup="this.value=this.value.replace(/[^\d]/,'')">
          </div>

          <div class="md-form mb-4" style="margin-bottom: 39%;">
            <label data-error="wrong" data-success="right" for="Createuser-birthday" style="float: left;margin: 5px 44px 0 0;">Birthday</label>
            <div class='input-group date' id='datetimepickerCreate' style="float: left;width: 79%;">
                <input type='text' class="form-control" id="Createuser-birthday"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
          </div>
          <div class="md-form mb-4" style="margin-bottom: 47%;">
            <label data-error="wrong" data-success="right" for="Createuser-email" style="float: left;margin: 5px 43px 0 0;">Email (*)</label>
            <input type="text" id="Createuser-email" class="form-control validate" style="float: left;width: 79%;">
          </div>
          <div class="md-form mb-4" style="margin-bottom: 55%;">
            <label data-error="wrong" data-success="right" for="Createuser-sipphone" style="float: left;margin: 5px 38px 0 0;">Sipphone</label>
            <input type="text" id="Createuser-sipphone" class="form-control validate" style="float: left;width: 79%;" maxlength="15" onkeyup="this.value=this.value.replace(/[^\d]/,'')">
          </div>
          <div class="md-form mb-4" style="margin-bottom: 63%;">
            <label data-error="wrong" data-success="right" for="Createuser-address" style="float: left;margin: 5px 45px 0 0;">Address</label>
            <input type="text" id="Createuser-address" class="form-control validate" style="float: left;width: 79%;"  maxlength="250">
          </div>
        </div>
        <div class="modal-footer">
          <button id="btnCreateUserInfo" class="btn btn-info btn-rounded mb-4">Create</button>
          <button type="button" class="btn btn-default btn-rounded mb-4" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <style>
  .dataTables_scrollBody {
    overflow: auto !important;
  }
  </style>