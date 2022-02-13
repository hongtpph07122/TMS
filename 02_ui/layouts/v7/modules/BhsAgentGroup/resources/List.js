
Vtiger_List_Js("BhsAgentGroup_List_Js",{},{

	registerDatatable: function(){
        loadDataTable({
            "columnDefs": [
                 { "bSortable": false, "aTargets": [ 0, 5 ] },
                 { className: "hidden", "targets": [ 0 ] },
                 { className: "text-left", "targets": [ 0,1,2,3,4 ] },
                 { className: "text-right", width: "100px", "targets": [ 5 ] },
            ],
            "lengthChange": true,
        });
    },
    
    getRandomUnique: function () {
        var d = new Date();
        return d.getMinutes() + '' + d.getSeconds();
    },

    checkIfEdittingAgent: function () {
        // Check if any row has been editting
        var table = $('#tblMailList0');
        var item = table.find('select.cbbInlineGroup.editing');
        if (item.length > 0) {
            return true;
        }
        return false;
    },

    getGroupDetail: function (groupId, grAgId, callback) {
        var params = {
            module: app.getModuleName(),
            action: "ActionAjax",
            mode: "getGroupDetail",
            app: "BHS_SYSTEM",
            isTransform: false,
            groupId: groupId,
            grAgId: grAgId,
        };

        app.request.get({ 'data': params }).then(
            function (err, res) {
                if (err === null) {
                    if (res.success) {
                        callback(res.data);
                    }
                } else {
                    callback('');
                }
            }
        );
    },

    delay: function(callback, ms) {
        var timer = 0;
        return function() {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = setTimeout(function () {
                callback.apply(context, args);
            }, ms || 0);
        };
    },


registerCustomEvents: function(){
        var thisInstance = this;

        $('body').on('blur change', '.searchFullName', function (e) {
            if (e.type === 'change' || e.type === 'focusout' || e.keyCode === 13) {
                var _this = this;
                var groupId = -2;
                var fullname = $(_this).val();
                var url = 'index.php?module=BhsAgentGroup&action=ActionAjax&mode=getGroupDetail&groupId=' + groupId;

                if (fullname && fullname.length > 0) {
                    url += ('&fullname='+fullname);
                }
                $('#tblMailList0').attr('data-url', url);

                reloadDataTableWithUrl(0, url);
                $('.tableLstGroup .row-item').removeClass('row-item-active');
                $('.row-item[data-id="-2"]').addClass('row-item-active');
            }
        });

        $('body').on('click', '.tableLstGroup .row-item', function (e) {
            var _this = this;
            $('.tableLstGroup .row-item').removeClass('row-item-active');
            $(this).addClass('row-item-active');
            var groupId = $(_this).data('id');
            var fullname = $('.searchFullName').val();
            var url = 'index.php?module=BhsAgentGroup&action=ActionAjax&mode=getGroupDetail&groupId=' + groupId;
            if (fullname && fullname.length > 0) {
                url += ('&fullname='+fullname);
            }
            $('#tblMailList0').attr('data-url', url);

            reloadDataTableWithUrl(0, url);
        });

        // Add new Agent Group
        $('body').on('click', '#addAgent', function (e) {
            var row = $('.cloneRow').clone();
            row.removeClass('cloneRow');
            if ($('#tblMailList0 tbody tr').last().hasClass('odd')) {
                row.addClass('even');
            } else {
                row.addClass('odd');
            }
            var groupElementId = 'cbbInlineGroup_' + thisInstance.getRandomUnique();
            var agentElementId = 'cbbInlineAgent_' + thisInstance.getRandomUnique();
            row.find('select.cbbInlineGroup').attr('id', groupElementId).addClass('editing');
            row.find('select.cbbInlineAgent').attr('id', agentElementId).addClass('editing');

            if (thisInstance.checkIfEdittingAgent()) {
                return true;
            }

            $('#tblMailList0').find('tbody').append(row[0]);
            vtUtils.showSelect2ElementView($('#' + groupElementId));
            vtUtils.showSelect2ElementView($('#' + agentElementId));
        });

        $('body').on('click', '.btnInlineEdit', function (e) {
            if (thisInstance.checkIfEdittingAgent()) {
                return true;
            }

            var parent  = $(this).closest('tr');

            var tds = parent.find('td');

            var groupId = $(tds[0]).find('.btnHiddenGroupId').val();
            var grAgId = $(tds[0]).find('.btnHiddenGrAgId').val();

            // Agent
            if (true) {
                var agentIdx = 2;
                var agentTd  = $(tds[agentIdx]);
                agentTd.attr('data-old_value', agentTd.text());
                agentTd.append(
                    '<input type="hidden" class="cbbInlineAgent editing" value="" >'
                );
            }

            // Group
            if (true) {
                var groupIdx = 3;
                var groupTd = $(tds[groupIdx]);
                groupTd.attr('data-old_value', groupTd.text());
                groupTd.html();
                var html = '<select class="cbbInlineGroup editing inputElement select2">';
                html += $(this).closest('table').find('.cloneRow').find('select.cbbInlineGroup').html();
                html += '</select>';
                groupTd.html(html);
                $(this).closest('tr').find('select.cbbInlineGroup').val();
                vtUtils.showSelect2ElementView(groupTd.find('select.cbbInlineGroup'));
            }

            // SkillLevel
            if (true) {
                var skillLevelIdx = 4;
                var skillLevelTd = $(tds[skillLevelIdx]);
                skillLevelTd.attr('data-old_value', skillLevelTd.text());
                skillLevelTd.html();
                var html = '<input value="" type="number" min="1" oninput="this.value = this.value.replace(/[^0-9.]/g, \'\'); this.value = this.value.replace(/(\\..*)\\./g, \'$1\');" class="txtInlineSkillLeve editting inputElement" />';
                skillLevelTd.html(html);
            }

            // Action
            if (true) {
                var actionIdx = 5;
                var actionTd = $(tds[actionIdx]);
                actionTd.html();
                var html = '<a class="btnInline btnInlineSave clone"><i class="fa fa-check"></i></a>' +
                           '<a class="btnInline btnInlineCancel clone"><i class="fa fa-trash"></i></a>';
                actionTd.html(html);
            }

            thisInstance.getGroupDetail(groupId, grAgId, function (data) {
                var record = data.length > 0 ? data[0] : false;

                if (record) {
                    var thisAgentId = record['userId'],
                        thisGroupId = record['groupId'],
                        thisAgSkillLevel = record['agSkillLevel'];

                    $('.cbbInlineAgent.editing').val(thisAgentId);
                    $('.cbbInlineGroup.editing').val(thisGroupId).trigger('change');
                    $('.txtInlineSkillLeve.editting').val(thisAgSkillLevel).trigger('input');
                }
            });
        });

        // Add New Agent
        $('body').on('click', '.btnInlineSave', function (e) {
            var $parent = $(this).closest('tr');

            var cbbInlineAgent = $parent.find('select.cbbInlineAgent'),
                cbbInlineGroup = $parent.find('select.cbbInlineGroup'),
                txtInlineSkillLevel = $parent.find('input.txtInlineSkillLeve');

            if (cbbInlineAgent.length < 1) {
                cbbInlineAgent = $parent.find('input.cbbInlineAgent');
            }
            var tds = $parent.find('td');

            var oGrId = $(tds[0]).find('.btnHiddenGroupId').val(),
                grAgId = $(tds[0]).find('.btnHiddenGrAgId').val(),
                agentId = cbbInlineAgent.val(),
                groupId = cbbInlineGroup.val(),
                skillLevel = txtInlineSkillLevel.val();

            var validate = {};
            if (!agentId) {
                validate.message = app.vtranslate('Please choose agent.');
            }

            if (!groupId) {
                validate.message = app.vtranslate('Please choose group agent.');
            }

            if (!skillLevel) {
                validate.message = app.vtranslate('Please choose skill level.');
            }
            if (validate.message) {
                app.helper.showErrorNotification(validate);
                return;
            }
            

            var params = {
                module: app.getModuleName(),
                action: "ActionAjax",
                mode: "saveRecord",
                oGrId: oGrId,
                agentId: agentId ? agentId : null,
                groupId: groupId,
                agSkillLevel: skillLevel,
                grAgId: grAgId
            };
            app.request.post({ 'data': params}).then(function(err, data) {
                    app.helper.hideProgress();
                    if (err === null) {
                        reloadDataTable();
                    } else {
                    }
                });
        });

	    $('body').on('click', '.btnInlineRemove', function (e) {
	        var _this = this;
            app.helper.showConfirmationBox({message: app.vtranslate('Are you sure want to delete?')}).then(function () {
                var $parent = $(_this).closest('tr'),
                 tds = $parent.find('td');

                var params = {
                    module: app.getModuleName(),
                    action: "ActionAjax",
                    mode: "deleteAgent",
                    groupId: $(tds[0]).find('.btnHiddenGroupId').val(),
                    grAgId: $(_this).data('id'),
                };
                app.helper.showProgress();
                app.request.post({'data': params}).then(
                    function (err, data) {
                        app.helper.hideProgress();
                        if (err === null) {
                            if (data.success) {
                                reloadDataTable();
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
        });

	    $('body').on('click', '.btnInlineCancel', function (e) {
            var parent  = $(this).closest('tr');
            var tds = parent.find('td');
            var orgId = $(tds[0]).text();

            $(tds[2]).html($(tds[2]).attr('data-old_value'));
            $(tds[3]).html($(tds[3]).attr('data-old_value'));
            $(tds[4]).html($(tds[4]).attr('data-old_value'));
            $(tds[5]).html(
                [
                    '<a class="btnInlineEdit btnBhsAction" href="javascript: void(0);" title="Edit"  data-id="' + orgId + '"><i class="fa fa-pencil"></i></a>',
                    '<a class="btnInlineRemove btnBhsAction" href="javascript: void(0);" title="Delete"  data-id="' + orgId + '"><i class="fa fa-trash"></i></a>',
                ].join('')
            );
        });

        $('body').on('click', '.btnInlineCancelAdd', function() {
            $(this).closest('tr').remove();
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