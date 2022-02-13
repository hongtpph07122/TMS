/*+*************************BhsStock**********************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/
Vtiger_List_Js("BhsCallStrategy_List_Js", {}, {
    registerDatatable: function() {
        //todo
    },
    registerCustomEvents: function() {
        var csid =0;//$("input[name='rule']:checked").closest("li").data("id");
        var currentActiveStep = $('.steps-navigation .active a').attr('step-id');
        $(function() {
          $('.start-date').val(convertDateTime($('.start-date').val()));
          $('.end-date').val(convertDateTime($('.end-date').val()));
            $(".callstrategy").filter('.selected').click();
            $(".DISTRIBUTION").filter('.selected').click();
            $("#call_option_gents, #call_option_gents_seleted,#call_seleted,#group_option_gents").sortable({
                connectWith: ".connectedSortable"
            }).disableSelection();
              //click selected to select
              $(document).on('click','#call_seleted li', function() {
                    $("#call_option_gents ").append($(this));
                    $("#call_option_gents i").removeClass("fa fa-minus-square-o");
                    $("#call_option_gents i").addClass("fa fa-plus-square-o");
              });
              $(document).on('click','#call_option_gents_seleted li', function() {
                     $("#group_option_gents").append($(this));  
                     $("#group_option_gents i").removeClass("fa fa-minus-square-o");
                     $("#group_option_gents i").addClass("fa fa-plus-square-o");
                     $('#call_option_gents_seleted:contains('+$(this).text()+')').remove();
                    
              });
              //click select to selected
              $(document).on('click','#call_option_gents li', function() {
                    $("#call_seleted ").append($(this));
                    $("#call_seleted i").removeClass("fa fa-plus-square-o");
                    $("#call_seleted i").addClass("fa fa-minus-square-o");
                });
                $(document).on('click','#group_option_gents li', function() {
                    $("#call_option_gents_seleted ").append($(this));  
                    $("#call_option_gents_seleted i").removeClass("fa fa-plus-square-o");
                    $("#call_option_gents_seleted i").addClass("fa fa-minus-square-o");
                });
          //

            $('#datetimepicker1').datetimepicker({
                format: 'DD/MM/YYYY HH:mm:ss'
            });
            $('#datetimepicker2').datetimepicker({
                useCurrent: false,
                format: 'DD/MM/YYYY HH:mm:ss'
            });
            $("#datetimepicker1").on("dp.change", function (e) {
                $('#datetimepicker2').data("DateTimePicker").minDate(e.date);
            });
            $("#datetimepicker2").on("dp.change", function (e) {
                $('#datetimepicker1').data("DateTimePicker").maxDate(e.date);
            });
            if ($('.campaingn_id').val() == 0) {
                $(".steps-navigation li").not(":first-child").addClass("btn disabled");
            }
           
        });
        $('body').on('click', '.btn-save', function(e) {
            var array = [];
            var array1 = [];
            $('#call_option_gents_seleted li').each(function() {
                array.push(parseInt($(this).data("id"))); // store in an array for reuse
            });
            $('#call_seleted li').each(function() {
                array1.push(parseInt($(this).data("id"))); // store in an array for reuse
            });
            var data = {
                "campaignId": parseInt($('.campaingn_id').val()),
                "clGroupId": array1,
                "orGroupIds": array,
                "ruleId": rule,
                "strategyId": csid
            };
            if (array1.length == 0) {
                var params = {
                    message: app.vtranslate('Please select calling list'),
                };
                app.helper.showErrorNotification(params);
                return;
            }
            if (!csid) {
                var params = {
                    message: app.vtranslate('Please select callstrategy'),
                };
                app.helper.showErrorNotification(params);
                return;
            }
            if (array.length == 0) {
                var params = {
                    message: app.vtranslate('Please select agent group'),
                };
                app.helper.showErrorNotification(params);
                return;
            }
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "saveConfig",
                data: data,
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function(err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            url = "index.php?module=BhsCampaignManagement&view=List&app=BHS_CAMPAIGN";
                            $(location).attr('href', url);
                        } else {
                            var params = {
                                message: data.message,
                            };
                            app.helper.showErrorNotification(params);
                        }
                    } else {
                        var params = {
                            message: err,
                        };
                        app.helper.showErrorNotification(params);
                    }
                }
            );
        });
        $('body').on('click', '.callstrategy', function(e) {
            csid = $(this).data("id");
            $(".strategy").text($(this).text());
            $('.callstrategy').removeAttr("style");
            $(this). attr("style", "color: #367BF5;");
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getCallStrategy",
                id: $(this).data("id"),
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function(err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            var cbbDistricts = $('#tblCallStrategy tbody');
                            cbbDistricts.empty();
                            var districts = data.callstrategy;
                            var total = districts.length;
                            for (var i = 0; i < total; i++) {
                                cbbDistricts.append("<tr> <td> " + districts[i].orderPhoneNumber + "</td><td> " + districts[i].callStatus + "</td> <td> " + districts[i].attempt + " </td> <td> " + districts[i].duration + " </td> <td> " + districts[i].day + " </td><td> " + districts[i].nextAction + " </td></tr>");
                            }
                            cbbDistricts.trigger("liszt:updated");
                            cbbDistricts.trigger('change.select2');
                        } else {
                            var params = {
                                message: data.message,
                            };
                            app.helper.showErrorNotification(params);
                        }
                    } else {
                        app.helper.hideProgress();
                    }
                }
            );
        });
        $('body').on('click', '.DISTRIBUTION', function(e) {
            rule=$(this).data("id");
            $('.DISTRIBUTION').removeAttr("style");
            $(this). attr("style", "color: #367BF5;");

           //$(".rule").text($(this).text());
           $('.summaryRule').html($(this).text().trim());
           $('#txtDistributionRule').val($(this).text().trim());

            var tblRule = $('#tblRule tbody');
            tblRule.empty();
            tblRule.append($(this).data('dscr'));
        });

        function convertdate(dt) {
            current_date = dt.getDate(),
                current_month = dt.getMonth() + 1,
                current_year = dt.getFullYear(),
                current_hrs = dt.getHours(),
                current_mins = dt.getMinutes(),
                current_sec= dt.getSeconds()
                // Add 0 before date, month, hrs, mins or secs if they are less than 0
                current_date = current_date < 10 ? '0' + current_date : current_date;
            current_month = current_month < 10 ? '0' + current_month : current_month;
            current_hrs = current_hrs < 10 ? '0' + current_hrs : current_hrs;
            current_mins = current_mins < 10 ? '0' + current_mins : current_mins;
            current_sec = current_sec < 10 ? '0' + current_sec : current_sec;
            current_datetime = current_year + "-" + current_month + "-" + current_date + " " + current_hrs + ":" + current_mins + ":"+current_sec;
            return current_datetime;
        }
        function validate(id){
            $(".steps-navigation a[step-id="+id+"] span").replaceWith("<i class='fa fa-check-circle' style='font-size:30px; color:#2FA84F'></i>");
        }
        function unvalidate(id){
            $(".steps-navigation a[step-id="+id+"] i").replaceWith("<span class='stepx'>0"+id+"</span>");
        }
        $('body').on('click', '.btn-pre', function(e) {
            currentActiveStep = $('.steps-navigation .active a').attr('step-id');
            if(currentActiveStep ==1){
                $(".steps-content").attr("style", "margin: 15px 15% 0% 15%;");
            }
            else{
                $(".steps-content").removeAttr("style");
            }
            // console.log(currentActiveStep);
        });
        $('body').on('click', '.btn-next ', function(e) {
            currentActiveStep = $('.steps-navigation .active a').attr('step-id');
            if (currentActiveStep == 6) {
               // $(".alert").show();
                callGroup = [];
                $('#call_option_gents_seleted li').each(function() {
                    callGroup.push(parseInt($(this).data("id"))); // store in an array for reuse
                });
                if (callGroup.length == 0) {
                    var params = {
                        message: app.vtranslate('Please select agent group'),
                    };
                    app.helper.showErrorNotification(params);
                    $('.btn-pre').click();
                    unvalidate(5);
                    return;
                }
                validate(5);
                $(".calling_list ul,.agent_group ul").remove();
                $("#call_seleted").clone().appendTo(".calling_list");
                $("#call_option_gents_seleted").clone().appendTo(".agent_group");
                $(".calling_list ul,.agent_group ul").removeAttr('id');
                $(".name").val($(".campaingn_name ").val());
                $(".id").val($(".campaingn_id ").val());
                $(".start-date").val($(".start-date").val());
                $(".end-date").val($(".end-date").val());
            }else{
                $(".alert").hide();
            }
            if (currentActiveStep == 5) {
                if(typeof(rule) != "undefined"){
                    validate(4);
                }else{
                    $('.btn-pre').click();
                    unvalidate(4);
                 var params = {
                     message: app.vtranslate('Please select distribution rule'),
                 };
                 app.helper.showErrorNotification(params);
                }
             }
            if (currentActiveStep == 4) {
               if(csid){
                   validate(3);
               }else{
                $('.btn-pre').click();
                unvalidate(3);
                var params = {
                    message: app.vtranslate('Please select call strategy'),
                };
                app.helper.showErrorNotification(params);
               }
            }
            if (currentActiveStep == 3) {
                callList = [];
                $('#call_seleted li').each(function() {
                    callList.push(parseInt($(this).data("id"))); // store in an array for reuse
                });
                if (callList.length == 0) {
                    var params = {
                        message: app.vtranslate('Please select calling list'),
                    };
                    app.helper.showErrorNotification(params);
                    $('.btn-pre').click();
                    unvalidate(2);
                    return;
                }else{
                    validate(2);
                } 
            }
            if (currentActiveStep == 2) {
                var createdate;
                $(".steps-content").removeAttr("style");
                if ($('.createdate').val() == "") {
                    createdate = new Date().toISOString();
                } else {
                    createdate = $('.createdate').val()
                }
                if ($('.campaingn_name').val() == "") {
                    var params = {
                        message: app.vtranslate("Please !! Input campaign name"),
                    };
                    app.helper.showErrorNotification(params);
                    $('.btn-pre').click();
                    unvalidate(1);
                    return;
                }else{
                    validate(1);
                }
                if($('.start-date').val()!=''){
                    date=$('.start-date').val().split("/");
                    date= date[2].split(" ")[0]+","+date[1]+","+date[0]+","+date[2].split(" ")[1];
                    startdate = new Date(date);
                }else{
                    startdate='';
                }
                if($('.end-date').val()!=''){
                    date=$('.end-date').val().split("/");
                    date= date[2].split(" ")[0]+","+date[1]+","+date[0]+","+date[2].split(" ")[1];
                    stopdate = new Date(date);
                }else{
                    stopdate='';
                }
                if(stopdate!='' && startdate!=''){
                    if ((startdate - stopdate) > 0) {
                        var params = {
                            message: app.vtranslate("Start Date should be less than End Date"),
                        };
                        app.helper.showErrorNotification(params);
                        $('.btn-pre').click();
                        return;
                    }else{
                        startdate=convertdate(startdate);
                        stopdate=convertdate(stopdate);
                    }
                }else{
                    if(stopdate!=''){
                        stopdate=convertdate(stopdate);
                    }
                    if(startdate!=''){
                        startdate=convertdate(startdate);
                    }
                }

                var data = {
                    "cpId": parseInt($('.campaingn_id').val()),
                    "createby": 0,
                    "createdate": createdate,
                    "modifyby": null,
                    "name": $('.campaingn_name').val(),
                    "owner": $('.owner').val(),
                    "startdate": startdate,
                    "status": $('.status').val(),
                    "stopdate": stopdate,
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "saveCampaign",
                };
                app.helper.showProgress();
                app.request.post({ 'data': data }).then(
                    function(err, data) {
                        app.helper.hideProgress();
                        if (err === null) {
                            if (data.success) {
                                app.helper.hideModal();
                                $(".steps-navigation li").removeClass("btn disabled");
                                if ($('.campaingn_id').val() == 0) {
                                    $('.campaingn_id').val(data.id);
                                    var params = {
                                        message: app.vtranslate("Created new campagin with ID : " + data.id),
                                    };
                                    window.history.pushState("object or string", "Title", "index.php?module=BhsCallStrategy&view=List&app=BHS_CAMPAIGN&cpid="+data.id);
                                } else {
                                    var params = {
                                        message: app.vtranslate("Update successfull"),
                                    };
                                }
                                app.helper.showSuccessNotification(params);
                              //  console.log(data.id);
                            } else {
                                $('.btn-pre').click();
                                var params = {
                                    message: data.message,
                                };
                                app.helper.showErrorNotification(params);
                            }
                        } else {
                            app.helper.hideProgress();
                        }
                    }
                );
            }

        });
        $(document).ready(function() {
            $("#search").on("keyup", function() {
                var value = $(this).val().toLowerCase();
                $("#call_option_gents li").filter(function() {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            $("#search1").on("keyup", function() {
                var value = $(this).val().toLowerCase();
                $("#callstrategylist li").filter(function() {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            $("#search2").on("keyup", function() {
                var value = $(this).val().toLowerCase();
                $("#group_option_gents li").filter(function() {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            $('.btnCancel').click(function() {
                url = "index.php?module=BhsCampaignManagement&view=List&app=BHS_CAMPAIGN";
                $(location).attr('href', url);
            });
            $(' .remove').click(function() {
                var val = [];
                // $('#call_seleted ').children().each(function(i) {
                //     val[i] = $(this);
                //     $("#call_option_gents").append(val[i]);
                //     $("#call_option_gents i").removeClass("fa fa-minus-square-o");
                //     $("#call_option_gents i").addClass("fa fa-plus-square-o");
                // });
                $("#call_option_gents").append($('#call_seleted').children());
                $("#call_option_gents i").removeClass("fa fa-minus-square-o");
                $("#call_option_gents i").addClass("fa fa-plus-square-o");

            });
            $('.remove1').click(function() {
                // var val = [];
                // $('#call_option_gents_seleted li').each(function(i) {
                //     val[i] = $(this).closest("li");
                //     $("#group_option_gents").append(val[i]);
                //     $("#group_option_gents i").removeClass("fa fa-minus-square-o");
                //     $("#group_option_gents i").addClass("fa fa-plus-square-o");
                // });
                $("#group_option_gents").append($('#call_option_gents_seleted').children());
                $('#call_option_gents_seleted li').remove();
                $("#group_option_gents i").removeClass("fa fa-minus-square-o");
                $("#group_option_gents i").addClass("fa fa-plus-square-o");
            });
            $('.callback_option_strategy').click(function() {
                if ($('#callback_mixed').is(':checked')) {
                    $('.callback_option_mixed').css('display', 'block');
                } else {
                    $('.callback_option_mixed').css('display', 'none');
                }
            });
        });
        // step
        $(function() {
            var step = $('.steps-navigation a');
            var stepItem = $('.steps-navigation ul li');
            var currentActiveStep = stepItem.filter('.active');

            // step.on('click', function(e) {
            //     e.preventDefault();
            //     $('span').removeClass('active');
            //     var getStepId = $(this).attr('step-id');
            //     step.stop().parent().removeClass('active');
            //     currentActiveStep = stepItem.filter('.active');
            //     $('.step').removeClass('active');
            //     $(this).stop().parent().addClass('active');
            //     $(this).stop().children().addClass('active');
            //     $('.step[step-id=' + getStepId + ']').stop().addClass('active');
            //     moveStep();
            //     if ($('.callback_agent_group').hasClass('active')) {
            //         $('.steps-control .btn-next').css('display', 'none');
            //         $('.steps-control .btn-save').css('display', 'block');
            //     } else {
            //         $('.steps-control .btn-next').css('display', 'block');
            //         $('.steps-control .btn-save').css('display', 'none');
            //         if ($('.callback_call_list').hasClass('active')) {
            //             $('.steps-control .btn-pre').css('display', 'none');
            //         } else {
            //             $('.steps-control .btn-pre').css('display', 'block');
            //         }
            //     }
            // });
            $('.btn-next').on('click', function(e) {
                e.preventDefault();
                currentActiveStep = stepItem.filter('.active');
                var nextStep = currentActiveStep.next();
                currentActiveStep.removeClass('active');
                if (nextStep.length) {
                    currentActiveStep = nextStep.addClass('active');
                } else {
                    currentActiveStep = stepItem.first().addClass('active');
                }

                moveStep();
                if ($('.callback_agent_group').hasClass('active')) {
                    $('.steps-control .btn-next').css('display', 'none');
                    $('.steps-control .btn-save').css('display', 'block');
                } else {
                    $('.steps-control .btn-next').css('display', 'block');
                    $('.steps-control .btn-save').css('display', 'none');
                    if ($('.callback_call_list').hasClass('active')) {
                        $('.steps-control .btn-pre').css('display', 'none');
                    } else {
                        $('.steps-control .btn-pre').css('display', 'block');
                    }
                }
            });
            $('.btn-pre').on('click', function(e) {
                e.preventDefault();
                currentActiveStep = stepItem.filter('.active');
                var preStep = currentActiveStep.prev();
                //console.log('pre step', preStep);
                currentActiveStep.removeClass('active');
                if (preStep.length) {
                    currentActiveStep = preStep.addClass('active');
                } else {
                    currentActiveStep = stepItem.first().addClass('active');
                }

                moveStep();
                if ($('.callback_agent_group').hasClass('active')) {
                    $('.steps-control .btn-next').css('display', 'none');
                    $('.steps-control .btn-save').css('display', 'block');
                } else {
                    $('.steps-control .btn-next').css('display', 'block');
                    $('.steps-control .btn-save').css('display', 'none');
                    if ($('.callback_call_list').hasClass('active')) {
                        $('.steps-control .btn-pre').css('display', 'none');
                    } else {
                        $('.steps-control .btn-pre').css('display', 'block');
                    }
                }
            });

        });
        function moveStep() {
            var currentActiveStep = $('.steps-navigation .active a').attr('step-id');
            $('span').removeClass('active');
            $('.step').stop().hide().removeClass('active');
            $('.step[step-id=' + currentActiveStep + ']').stop().show().addClass('active');
            $('.steps-navigation .active a').stop().children().addClass('active');
        }
    },
    initializePaginationEvents: function() {
        return true;
    },
    registerEvents: function() {
        this._super();
        this.registerDatatable();
        this.registerCustomEvents();
    }
});
