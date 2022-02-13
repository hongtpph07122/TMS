/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

Vtiger_List_Js("BhsAgentMonitoring_List_Js", {}, {

    registerDatatable: function () {
        loadDataTable({
            "availableCols": 1,
            scrollX: true,
            columns: [
                { data: null, render: 'userId' },
                { data: null, render: 'userType' },
                { data: null, render: 'fullname', "sWidth": "10%" },
                { data: null, render: 'personalPhone1' },
                { data: null, render: 'email' },
                { data: null, render: 'homeAddress' },
                { data: null, render: 'phone' },
                {
                    data: null, render: (row) => {
                        return convertDateTime(row.birthday, gettime = false);
                    }
                },
                {
                    data: null, render: 'modifyby'
                },
                {
                    data: null, render: (row) => {
                        return convertDateTime(row.modifydate);
                    }
                },

            ],
            "lengthChange": true,
        });
    },

    registerCustomEvents: function () {
        // $('body').on('click', '.btnBhsDelete', function (e) {
        //     app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to delete?')}).then(function () {
        //         var params = {
        //             module: app.getModuleName(),
        //             action: "ActionAjax",
        //             mode: "deleteCampaign",
        //             id: $(this).data('id'),
        //         };
        //         app.helper.showProgress();
        //         app.request.post({'data': params}).then(
        //             function (err, data) {
        //                 app.helper.hideProgress();
        //                 if (err === null) {
        //                     if (data.success) {
        //                         reloadDataTable();
        //                     } else {
        //                         var params = {
        //                             message: data.message,
        //                         };
        //                         app.helper.showErrorNotification(params);
        //                     }
        //                 } else {
        //                     var params = {
        //                         message: err,
        //                     };
        //                     app.helper.showErrorNotification(params);
        //                 }
        //             }
        //         );
        //     });
        // });

        // $('body').on('click', '.btnBhsUpdate', function (e) {
        //     var params = {
        //         module: app.getModuleName(),
        //         view: "ViewAjax",
        //         mode: "showEdit",
        //         id: $(this).data('id'),
        //     };
        //     app.helper.showProgress();
        //     app.request.post({'data': params}).then(
        //         function(err, data){
        //             app.helper.hideProgress();
        //             if(err === null) {
        //                 app.helper.showModal(data);
        //             }else{
        //             }
        //         }
        //     );
        // });
        $('body').on('click', '.btnExportExcel', function (e) {
            var params = {
                'module': app.getModuleName(),
                'action': "ExportToExcel",
            };
            var url = 'index.php?' + $.param(params);
            window.open(url, 'blank');
        });
        $('body').on('click', '.headphone', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "showCalling",
                id: $(this).data('id'),
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        alert(data.message + ': ' + data.data.id);
                        //app.helper.showModal(data.message);
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );
        });
        var userID = '';
        var tokenAdmin = '';
        $('body').on('click', '#resetpassword', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getToken",
                userID: $(this).data('id'),
                email: $(this).data('email'),
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        $("#myModal").modal();
                        $(".email").text(data.data.email);
                        userID = data.data.id;
                        tokenAdmin = data.token;
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );
        });
        $('body').on('click', '#confirmReset', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getResetPassword",
                user_id: userID
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (!data.success) {
                            var params = {
                                message: data.message,
                            };
                            app.helper.showErrorNotification(params);
                        } else {
                            app.helper.showSuccessNotification({ 'message': "Reset successful!" });
                            $('#myModal').modal('toggle');
                        }
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );
        });
        function getQuota() {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getTotalRecords",
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        $("#totalUser").text(data.length);
                        var param = {
                            module: app.getModuleName(),
                            action: "ActionAjax",
                            mode: 'getQuota'
                        }
                        app.request.post({ 'data': param }).then(
                            function (error, dataResult) {
                                if (error === null) {
                                    if (dataResult.success) {
                                        $('#totalQuota').text(dataResult.data.total);
                                    }
                                    else {
                                        var params = {
                                            message: data.message,
                                        };
                                        app.helper.showErrorNotification(params);
                                    }
                                }
                                else {

                                }
                            }
                        );
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );
        }
        getQuota();
        $('body').on('click', '#BhsDisable', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getToken",
                userID: $(this).data('id'),
                email: $(this).data('fullname'),
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        $("#myDisableModal").modal();
                        $(".Disablefullname").text(data.data.email);
                        userID = data.data.id;
                        tokenAdmin = data.token;

                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );

        });
        var user_Lock = 0;
        var user_LockEdit;
        //getUserType();
        $("#CreateuserlockActive").click(function () {
            var value = $('#CreateuserlockActive').attr('aria-pressed');
            if (value == 'true') {
                user_Lock = 1
            }
            else {
                user_Lock = 0
            }
        });
        $('#EdituserlockActive').click(function () {
            if (user_LockEdit == 0) {
                user_LockEdit = 1;
            }
            else {
                user_LockEdit = 0;
            }
        });
        $('body').on('click', '#BhsEnable', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getToken",
                userID: $(this).data('id'),
                email: $(this).data('fullname'),
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        $("#myEnableModal").modal();
                        $(".Enablefullname").text(data.data.email);
                        userID = data.data.id;
                        tokenAdmin = data.token;
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );

        });
        $('body').on('click', '#BhsDelete', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getToken",
                userID: $(this).data('id'),
                email: $(this).data('fullname'),
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        $("#myDeleteModal").modal();
                        $(".Deletefullname").text(data.data.email);
                        userID = data.data.id;
                        tokenAdmin = data.token;
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );
        });
        $('body').on('click', '#confirmDeleteUser', function (e) {
            $.ajax({
                type: 'DELETE',
                url: 'http://demo.tmsapp.vn:9002/api/v1/agent/' + userID,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + cookie);
                },
                success: function (result) {
                    if (result.code != 200) {
                        app.helper.showErrorNotification({ 'message': result.message });
                    } else {
                        app.helper.showSuccessNotification({ 'message': "Delete successful!" });
                        $('#myDeleteModal').modal('toggle');
                        reloadDataTable();
                        getQuota();
                    }
                }
            });
            // var params = {
            //     module: app.getModuleName(),
            //     action: "ActionAjax",
            //     mode: "deleteUser",
            //     id: userID,
            // };
            // app.helper.showProgress();
            // app.request.post({'data': params}).then(
            //     function (err, data) {
            //         app.helper.hideProgress();
            //         if (err === null) {
            //             if (data.success) {
            //                 app.helper.showSuccessNotification({ 'message': "Delete successful!" });
            //                 $('#myDeleteModal').modal('toggle');
            //                 reloadDataTable();
            //                 getQuota();
            //             } else {
            //                 var params = {
            //                     message: data.message,
            //                 };
            //                 app.helper.showErrorNotification(params);
            //             }
            //         } else {
            //             var params = {
            //                 message: err,
            //             };
            //             app.helper.showErrorNotification(params);
            //         }
            //     }
            // );
        });
        $('body').on('click', '#confirmDisableUser', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "deactiveUser",
                userID: userID,
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            console.log(data);
                            app.helper.showSuccessNotification({ 'message': "Disable successful!" });
                            $('#myDisableModal').modal('toggle');
                            reloadDataTable();
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
        $('body').on('click', '#confirmEnableUser', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "activeUser",
                userID: userID,
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            app.helper.showSuccessNotification({ 'message': "Enable successful!" });
                            $('#myEnableModal').modal('toggle');
                            reloadDataTable();
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
        $('body').on('click', '#BhsEdit', function (e) {
            //getUserType();
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getEditUserAgent",
                userID: $(this).data('id'),
                fullname: $(this).data('fullname'),
                usertype: $(this).data('usertype'),
                userlock: $(this).data('userlock'),
                email: $(this).data('email'),
                sipphone: $(this).data('sipphone'),
                born: $(this).data('born'),
                phone: $(this).data('phone'),
                address: $(this).data('address')
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        $("#modalEditUserInfo").modal();

                        $("#user-id").text(data.data.id);
                        $("#full-name").val(data.data.fullname);
                        $("#user-phone").val(data.data.phone);
                        $("#user-sipphone").val(data.data.sipphone);
                        $("#user-address").val(data.data.address);
                        var from = new Date(data.data.born);
                        var dt_to = $.datepicker.formatDate('dd/mm/yy', from);
                        $("#user-birthday").val(dt_to);
                        $("#user-email").val(data.data.email);
                        //$('input:radio[name=userlockActive]').val([data.data.userlock]);
                        if (data.data.userlock == '0') {
                            $('#EdituserlockActive').addClass('active');
                            user_LockEdit = 0;
                        }
                        else {
                            $('#EdituserlockActive').removeClass('active');
                            user_LockEdit = 1;
                        }
                        if (data.data.usertype == 'CS') {
                            data.data.usertype = 'customer_service';
                        }
                        else if (data.data.usertype == 'team leader') {
                            data.data.usertype = 'team_leader';
                        }
                        else if (data.data.usertype == 'manager') {
                            data.data.usertype = 'management';
                        }
                        else if (data.data.usertype == 'validator') {
                            data.data.usertype = 'validation';
                        }
                        $("#user-type").val(data.data.usertype);
                        $('#select2-chosen-1').text(data.data.usertype);
                        $('#datetimepickerEdit').datetimepicker({
                            format: 'YYYY-MM-DD'
                        });
                        tokenAdmin = data.token;
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );
        });
        $('body').on('click', '#btnEditUserInfo', function (e) {
            var from = new Date($("#user-birthday").val());
            var dt_to = $.datepicker.formatDate('yy-mm-dd', from);
            if (dt_to == 'NaN-NaN-NaN') {
                dt_to = $.datepicker.formatDate('yy-mm-dd', new Date());
            }
            var sipphoneReplace = $("#user-sipphone").val();
            sipphoneReplace = sipphoneReplace.replace(/\D/g, '');
            var param = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "editUser",
                user_type: $("#user-type").val(),
                fullname: $("#full-name").val(),
                email: $("#user-email").val(),
                phone: sipphoneReplace,
                birthday: dt_to,
                user_lock: user_LockEdit,
                user_id: $("#user-id").text(),
                home_phone_1: '',
                home_phone_2: '',
                personal_phone_1: $("#user-phone").val(),
                personal_phone_2: '',
                work_mail: '',
                personal_mail: '',
                home_address: $("#user-address").val(),
                chat_id: []
            };
            if (param.fullname == '' || param.personal_phone_1 == '' || param.email == '' || param.user_type == '') {
                app.helper.showErrorNotification({ 'message': "Please fill the (*) options!" });
                return;
            }
            $.ajax({
                type: 'PUT',
                url: 'http://demo.tmsapp.vn:9002/api/v1/agent',
                data: JSON.stringify(param),
                contentType: "application/json",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + cookie);
                },
                success: function (result) {
                    if (result.code != 200) {
                        app.helper.showErrorNotification({ 'message': result.message });
                    } else {
                        app.helper.showSuccessNotification({ 'message': "Edit successful!" });
                        $('#modalEditUserInfo').modal('toggle');
                        reloadDataTable();
                    }
                }
            });
            // app.helper.showProgress();
            // app.request.post({ 'data': param }).then(
            //     function (err, data) {
            //         app.helper.hideProgress();
            //         if (err === null) {
            //             if (data.success && data.data.code == 200) {
            //                 app.helper.showSuccessNotification({ 'message': "Edit successful!" });
            //                 $('#modalEditUserInfo').modal('toggle');
            //                 reloadDataTable();
            //             } else {
            //                 var params = {
            //                     message: data.data.message,
            //                 };
            //                 app.helper.showErrorNotification(params);
            //             }
            //         } else {
            //             app.helper.hideProgress();
            //         }
            //     }
            // );
        });
        $("#datetimepickerEdit").datetimepicker({
            format: 'DD/MM/YYYY',
            useCurrent: false,
            showClose: true,
            minDate: '01/01/1950',
            maxDate: '01/01/2002',
        });
        $('#datetimepickerCreate').datetimepicker({
            format: 'DD/MM/YYYY',
            useCurrent: false,
            showClose: true,
            minDate: '01/01/1950',
            maxDate: '01/01/2002',
        });
        function getCookie(cname) {
            var name = cname + "=";
            var decodedCookie = decodeURIComponent(document.cookie);
            var ca = decodedCookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        }

        var cookie = getCookie("access_token");


        $('body').on('click', '.btnCreateUser', function (e) {
            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "getToken",
                userID: 'abc',
                email: 'xyz',
            };
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        $("#modalCreateUserInfo").modal();
                        //getUserType();
                        tokenAdmin = data.token;
                    } else {
                        //app.helper.showModal(err);
                    }
                }
            );
        });
        function validate(evt) {
            var theEvent = evt || window.event;

            // Handle paste
            if (theEvent.type === 'paste') {
                key = event.clipboardData.getData('text/plain');
            } else {
                // Handle key press
                var key = theEvent.keyCode || theEvent.which;
                key = String.fromCharCode(key);
            }
            var regex = /[0-9]|\./;
            if (!regex.test(key)) {
                theEvent.returnValue = false;
                if (theEvent.preventDefault) theEvent.preventDefault();
            }
        }
        $('body').on('click', '#btnCreateUserInfo', function (e) {
            var from = new Date($("#Createuser-birthday").val());
            var dt_to = $.datepicker.formatDate('yy-mm-dd', from);
            var param = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "saveUser",
                user_type: $("#Createuser-type").val(),
                fullname: $("#Createfull-name").val(),
                email: $("#Createuser-email").val(),
                phone: $("#Createuser-sipphone").val(),
                birthday: dt_to,
                user_lock: user_Lock,
                user_name: $("#Createuser-name").val(),
                password: $("#CreatePassword").val(),
                home_phone_1: '',
                home_phone_2: '',
                personal_phone_1: $("#Createuser-phone").val(),
                personal_phone_2: '',
                work_mail: '',
                personal_mail: '',
                home_address: $("#Createuser-address").val(),
                chat_id: []
            };
            if (param.fullname == '' || param.personal_phone_1 == '' || param.email == '' || param.user_type == '' || param.user_name == '' || param.password == '') {
                app.helper.showErrorNotification({ 'message': "Please fill the (*) options!" });
                return;
            }
            app.helper.showProgress();
            app.request.post({ 'data': param }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success && data.data.code == 200) {
                            app.helper.showSuccessNotification({ 'message': "Create successful!" });
                            $('#modalCreateUserInfo').modal('toggle');
                            reloadDataTable();
                            getQuota();
                        } else {
                            var params = {
                                message: data.data.message,
                            };
                            app.helper.showErrorNotification(params);
                        }
                    } else {
                        app.helper.hideProgress();
                    }
                }
            );
        });
        $('body').on('click', '#btnSaveCampaign', function (e) {
            var params = jQuery("#frmEditCampaign").serializeFormData();
            app.helper.showProgress();
            app.request.post({ 'data': params }).then(
                function (err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        if (data.success) {
                            app.helper.hideModal();
                            reloadDataTable();
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
    },

    initializePaginationEvents: function () {
        return true;
    },

    registerEvents: function () {
        this._super();

        this.registerDatatable();
        this.registerCustomEvents();
    }
});

$(document).ready(function () {
});