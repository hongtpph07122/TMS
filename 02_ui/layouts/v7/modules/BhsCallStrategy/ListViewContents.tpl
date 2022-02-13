
    <div class="steps">
        <div class="steps-navigation">
            <ul class="list-unstyled">
                <li class="active"><a href="#step1" step-id="1"><span class="stepx active">01</span><strong>{vtranslate('Campaign Name', $MODULE)}</strong></a></li>
                <li><a href="#step2" step-id="2" ><span class="stepx">02</span><strong>{vtranslate('Calling Lists', $MODULE)}</strong></a></li>
                <li><a href="#step3" step-id="3" ><span class="stepx">03</span><strong>{vtranslate('LBL_STRATEGY', $MODULE)}</a></strong></li>
                <li><a href="#step4" step-id="4"><span class="stepx">04</span><strong>{vtranslate('Distribution Rule', $MODULE)}</strong></a></li>
                <li><a href="#step5" step-id="5"><span class="stepx">05</span><strong>{vtranslate('LBL_AGENT', $MODULE)}</strong></a></li>
                <li><a href="#step6" step-id="6"><span class="stepx">06</span><strong>{vtranslate('Summary', $MODULE)}</strong></a></li>
            </ul>
        </div>
          <div class="alert" style="display: none;" >
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong><i class="fa fa-bell" style="color:red"></i></strong>  Some activities may not appear yet</a>.
             </div>
        <div class=" steps-content">
            <div class=" step callback_call_list active " step-id="1"  >
            <input  class="createdate hidden" value="{$CAMPAIN->createdate}">
             <input  class="owner hidden" value="{$CAMPAIN->owner}">
              <input  class="status hidden" value="{$CAMPAIN->status}">
              <input  class="orgId hidden" value="{$CAMPAIN->orgId}">
              <input  class="startdate hidden" value="{$CAMPAIN->startdate}">
              <input  class="stopdate hidden" value="{$CAMPAIN->stopdate}">
                <div class="row form-group">
                    <div class="col-sm-4">
                        <label>{vtranslate('LBL_CAMPAIGN_NAME', $MODULE)}:</label>
                    </div>
                    <div class="col-sm-8">
                        <input name="campaingn_name" class="campaingn_name inputElement" value="{$CAMPAIN->cpName}">
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-sm-4 text">
                        <label>{vtranslate('LBL_CAMPAIGN_ID', $MODULE)}:</label>
                    </div>
                    <div class="col-sm-8">
                        <input name="campaingn_id" class="campaingn_id inputElement" readonly 
                        value="{if $CAMPAIN->cpId}{$CAMPAIN->cpId}{else}  0{/if}">
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-sm-4">
                        <label>{vtranslate('LBL_START_DATE', $MODULE)}:</label>
                    </div>
                    <div class="col-sm-8" >
                        <div class="input-group inputElement padding0px" id='datetimepicker1'>
                            <input name="start-date" class="start-date dateTimeField form-control inputElement" value="{$CAMPAIN->startdate}" >
                            <span class="input-group-addon" style="padding: 0px;"><img src="layouts\v7\modules\BhsCallStrategy\resources\DateTime.svg" style="width: 16px;height: 18px;" ></span>
                        </div>
                    </div>
                    </div>
                <div class="row form-group">
                    <div class="col-sm-4  ">
                        <label>{vtranslate('LBL_END_DATE', $MODULE)}:</label>
                    </div>
                    <div class="col-sm-8">
                        <div class="input-group inputElement padding0px" id='datetimepicker2'>
                            <input name="end-date" class="end-date dateTimeField form-control inputElement " value="{$CAMPAIN->stopdate}">
                            <span class="input-group-addon" style="padding: 0px;"><img src="layouts\v7\modules\BhsCallStrategy\resources\DateTime.svg" style="width: 16px;height: 18px;" ></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="step callback_call_list1 " step-id="2" style="padding: 1% 1% 5% 1%;">
                <div class="row">
                    <div class="col-md-6 ">
                        <div class="table_connectedSortable">
                        <div class="title">
                            <h4>{vtranslate('Calling list', $MODULE)}</h4>
                            <div class=" search-links-container"><div class="search-link-page marginTop0px"><span class="fa fa-search" aria-hidden="true"></span><input class="keyword-input" id="search" type="text" placeholder="Search Calling list"></div></div>
                        </div>    
                            <ul id="call_option_gents" class="list-unstyled connectedSortable">
                                {foreach from=$CALLS item=call}
                               <li class="ui-state-default" data-id="{$call->callinglistId}"><i class="fa fa-plus-square-o"></i>     {$call->clName}</li>     
                                {/foreach}
                            </ul>
                        </div>
                    </div>
                    {* <div class="col-md-1 align-self-center text-center">
                        <span>
                            <input type="button" class="select_call" value=">>">
                                <p>
                                <p>
                             <input type="button" class="unselect_call" value="<<">
                        </span>
                    </div> *}
                    <div class="col-md-6">
                        <div class="table_connectedSortable">
                        <div class="title">
                            <h4>{vtranslate('Selected', $MODULE)}</h4>
                            <div class="remove" >
                             <i class='fa fa-trash' >  Remove All</i>
                            </div>
                        </div>    
                            <ul id="call_seleted" class="list-unstyled connectedSortable">
                              {foreach from=$CALL_SELECTED item=call}
                                
                                   <li class="ui-state-default" data-id="{$call->callinglistId}"><i class="fa fa-minus-square-o"></i>     {$call->clName}</li>     
                              
                              {/foreach}
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="step callback_strategy" step-id="3" style="padding: 1% 1% 5% 1%;">
                <div class="row d-flex">
                    <div class="col-md-6 ">
                        <div class="table_connectedSortable">
                         <div class="title">
                            <h4>{vtranslate('Select Call Strategy', $MODULE)}</h4>
                            <div class=" search-links-container" ><div class="search-link-page marginTop0px"><span class="fa fa-search" aria-hidden="true"></span><input class="keyword-input" id="search1" type="text" placeholder="Search Call Strategy"></div></div>
                         </div>   
                            <ul class="list-unstyled connectedSortable " id="callstrategylist" >
                                {foreach from=$CALLSTRATEGYS item=call}
                                <label>
                                <li class="ui-state-default callstrategy {if $call->csId==$RECORD->strategyId}selected " style='color: #367BF5;'{else}" {/if}data-id="{$call->csId}">  {$call->name}</li>
                                </label>
                                {/foreach}
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <table id="tblCallStrategy" class="table table-striped " style="width:100%">
                            <thead >
                                <tr class="text-center">
                                    <th>{vtranslate('PHONE', $MODULE)}</th>
                                    <th>{vtranslate('CALL STATUS', $MODULE)}</th>
                                    <th>{vtranslate('ATTEMPT', $MODULE)}</th>
                                    <th>{vtranslate('DURATION', $MODULE)}</th>
                                    <th>{vtranslate('DAY', $MODULE)}</th>
                                    <th>{vtranslate('NEXT ACTION', $MODULE)}</th>
                                </tr>
                            </thead>
                            <tbody class="text-center">
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                            <tfoot>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
            <div class="step callback_strategy" step-id="4" style="padding: 1% 1% 5% 1%;">
                <div style="display: flex;" class="row">
                    <div class=" col-md-3 " >
                     <div class="table_connectedSortable">
                         <div class="title">
                            <h4>{vtranslate('LBL_DISTRIBUTION_RULE', $MODULE)}</h4>
                         </div>   
                            <ul class="list-unstyled connectedSortable overflow-y-scroll">
                                {foreach from=$RULES item=rule}
                               <label> <li class="ui-state-default DISTRIBUTION {if $rule->drId==$RECORD->ruleId}selected "  style='color: #367BF5;'{else}" {/if}  data-name="{$rule->name}" data-dscr="{htmlspecialchars($rule->dscr)}" data-id="{$rule->drId}" >  {$rule->shortname}</li></label>
                                {/foreach}
                            </ul>
                            <input type="hidden" id="txtDistributionRule" value="{$rule->shortname}">
                        </div>
                    </div>
                    <div class="col-md-9">
                        <h4 class="table_connectedSortable title" style="min-height: auto;margin-bottom: 8px;">{vtranslate('Descriptions', $MODULE)}:</h4>
                        <table id="tblRule" class="table table-striped table-bordered nowrap custom-dt" style="width:120%">
                            <tbody>
                                <tr>
                                   
                                </tr>
                            </tbody>
                            <tfoot>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
            <div class="step callback_agent_group row" step-id="6" style="padding: 3% 1% 5% 1%;">
            <div class=" col-md-6">
             <div class="row form-group">
                    <div class="col-sm-4">
                        <label>{vtranslate('LBL_CAMPAIGN_NAME', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                        <input class="name inputElement"  readonly>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-sm-4 text">
                        <label>{vtranslate('LBL_CAMPAIGN_ID', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                        <input  class="id inputElement" readonly >
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-sm-4">
                        <label>{vtranslate('LBL_START_DATE', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                        <div class="input-group inputElement">
                            <input class="start-date dateTimeField form-control inputElement" readonly >
                            <span class="input-group-addon padding0px" style="width: 35px; border-width: 0px;"><img src="layouts\v7\modules\BhsCallStrategy\resources\DateTime.svg" style="width: 19.8px;height: 18px;" ></span>
                        </div>
                    </div>
                    </div>
                <div class="row form-group">
                    <div class=" col-sm-4  ">
                        <label>{vtranslate('LBL_END_DATE', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                        <div class="input-group inputElement">
                            <input  class="end-date dateTimeField form-control inputElement "  readonly>
                            <span class="input-group-addon padding0px" style="width: 35px; border-width: 0px;"><img src="layouts\v7\modules\BhsCallStrategy\resources\DateTime.svg" style="width: 19.8px;height: 18px;" ></span>

                        </div>
                    </div>
                </div>
                 <div class="row form-group">
                    <div class="col-sm-4">
                        <label>{vtranslate('Calling list', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                        <div class="calling_list input-group ">
                        </div>
                    </div>
                </div>
                </div>
                <div class=" col-md-6">
                 <div class="row form-group">
                    <div class=" col-sm-4">
                         <label>{vtranslate('Rule ', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                         <div  class="rule summaryRule inputElement" ></div>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-sm-4 text">
                       <label>{vtranslate('Strategy', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                       <div  class="strategy inputElement"></div>
                       <div style="margin-top: 10px;">
                       <table id="tblCallStrategy" class="table table-striped " style="width:100%">
                            <thead class="text-center">
                                <tr>
                                    <th>{vtranslate('PHONE', $MODULE)}</th>
                                    <th>{vtranslate('CALL STATUS', $MODULE)}</th>
                                    <th>{vtranslate('ATTEMPT', $MODULE)}</th>
                                    <th>{vtranslate('DURATION', $MODULE)}</th>
                                    <th>{vtranslate('DAY', $MODULE)}</th>
                                    <th>{vtranslate('NEXT ACTION', $MODULE)}</th>
                                </tr>
                            </thead>
                            <tbody class="text-center">
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                            <tfoot>
                            </tfoot>
                        </table>
                        </div>
                    </div>
                </div>
                 <div class="row form-group">
                    <div class="col-sm-4">
                       <label>{vtranslate('Agent group', $MODULE)}:</label>
                    </div>
                    <div class="col-md-8 col-sm-8">
                        <div class="agent_group input-group ">
                        </div>
                    </div>
                </div>
                </div>
            </div>
            <div class="step callback_agent_group1" step-id="5" style="padding: 1% 1% 5% 1%;">
                <div class="row d-flex">
                    <div class="col-md-6 ">
                        <div class="table_connectedSortable">
                         <div class="title">
                            <h4>{vtranslate('Select Agent Group', $MODULE)}</h4>
                             <div class=" search-links-container"><div class="search-link-page marginTop0px"><span class="fa fa-search" aria-hidden="true"></span><input class="keyword-input" id="search2" type="text" placeholder="Search group"></div></div>
                         </div>   
                            <ul id="group_option_gents" class="list-unstyled connectedSortable overflow-y-scroll">
                                {foreach from=$AGENTGROUP item=group}
                                {if $group->groupId|in_array:$RECORD->agentGroupIdList}
                                   {continue}
                                {/if}
                                <li class="ui-state-default" data-id="{$group->groupId}"><i class="fa fa-plus-square-o"></i>  {$group->name}</li>
                                {/foreach}
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="table_connectedSortable">
                         <div class="title">
                            <h4>{vtranslate('Selected', $MODULE)}</h4>
                            <div class="remove1" >
                             <i class='fa fa-trash' >  Remove All</i>
                            </div>
                         </div>   
                            <ul id="call_option_gents_seleted" class="list-unstyled connectedSortable overflow-y-scroll">
                                {foreach from=$AGENTGROUP item=group}
                                {if $group->groupId|in_array:$RECORD->agentGroupIdList}
                                    <li class="ui-state-default" data-id="{$group->groupId}"><i class="fa fa-minus-square-o"></i>  {$group->name}</li>
                                {/if}
                                
                                {/foreach}
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="steps-control">
<button  class="btn btnCancel"> {vtranslate('Cancel', $MODULE)}</button>
<div class="control row col-md-12">

    <div class="pre">
        <button style="display: none;" class="btn btn-pre btn-default"><img src="layouts\v7\modules\BhsCallStrategy\resources\left.png" > {vtranslate('Back', $MODULE)}</button>
    </div>
    <div class="next">
        <button class="btn btn-next btn-success">{vtranslate('LBL_NEXT', $MODULE)} <img src="layouts\v7\modules\BhsCallStrategy\resources\right.png" ></i></button>
    </div>
    <div class="save">
        <button class="btn btn-save  btn-success" style="display: none;">{vtranslate('Finish', $MODULE)}</button>
    </div>
    </div>
</div>
</div>
