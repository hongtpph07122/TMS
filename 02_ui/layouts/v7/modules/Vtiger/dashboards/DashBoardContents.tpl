{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
************************************************************************************}
{* modules/Vtiger/views/DashBoard.php *}

{strip}
    <style>
        .dashboard-box-process-content {
            min-height: 300px;
        }
        .radio-inline label {
            font-weight: 100;
        }
    </style>
    <link rel="stylesheet" href="{vresource_url('libraries/jquery/datatables/dataTables.bootstrap.min.css')}">
    <link rel="stylesheet" href="{vresource_url('libraries/jquery/datatables/custom_datatables.css')}">
    <script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
    <script src="{vresource_url('layouts/v7/lib/Chart.min.js')}"></script>
    <script src="{vresource_url('layouts/v7/lib/chart.funnel.bundled.min.js')}"></script>
    <script src="{vresource_url('layouts/v7/lib/chartjs-plugin-datalabels.min.js')}"></script>

    <script src="{vresource_url('libraries/jquery/datatables/custom_datatables.js')}"></script>
    <style>
    </style>
    <div class="main-container dashBoardContainer clearfix" style="padding-left: 60px;">
        <div class="listViewPageDiv content-area-dashboard1 content-area full-width" id="listViewContent" >
            <div style="padding: 15px">
                <div class="dashBoard-list-top">
                    <div class="row">
                        <div class="box-md dashboard-box">
                            <div class="form-group">
                                <h4 class="module-title text-uppercase">
                                    <a title="{vtranslate($MODULE, $MODULE)}" href='{$DEFAULT_FILTER_URL}&app={$SELECTED_MENU_CATEGORY}'> {vtranslate('LBL_DASHBOARD', $MODULE)}</a>
                                </h4>
                            </div>
                            <div class="col-md-12">
                                <div class="form-group">
                                    <div class="radio-inline">
                                        <label><input id="monitor-date-now" type="radio" name ="searchDate" checked> &nbsp;{vtranslate('DATE_NOW', $MODULE)}</label>
                                    </div>
                                    <div class="radio-inline">
                                        <label><input id="monitor-date-yesterday" type="radio" name ="searchDate"> &nbsp;{vtranslate('DATE_YESTERDAY', $MODULE)}</label>
                                    </div>
                                    <div class="radio-inline">
                                        <label><input id="monitor-date-week" type="radio" name ="searchDate"> &nbsp;{vtranslate('DATE_WEEK', $MODULE)}</label>
                                    </div>
                                    <div class="radio-inline">
                                        <label><input id="monitor-date-month" type="radio" name ="searchDate"> &nbsp;{vtranslate('DATE_MONTH', $MODULE)}</label>
                                    </div>
                                    <div class="radio-inline">
                                        <label><input id="monitor-custom-date" type="radio" name ="searchDate"> &nbsp;{vtranslate('CUSTOM_DATE', $MODULE)}</label>
                                    </div>
                                </div>
                                <div class="dashboard-input-group input-group hidden">
                                    <input id = "searchDate" type="text" name="dates" />
                                    <i class="fa fa-calendar" aria-hidden="true"></i>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="row">
                        <div class="col-md-15" style="margin-bottom: 15px;">
                            <div class="dashboard-box-border">
                                <h2 class="dashboard-box-title">
                                    {vtranslate('approved',$MODULE)}
                                </h2>
                                <div class="number approvedNumber text-green">
                                    {number_format($lead->approved)}
                                </div>
                                <div class="dashboard-box-footer">
                                    {vtranslate('orders',$MODULE)}
                                </div>
                            </div>
                        </div>
                        <div class="col-md-15" style="margin-bottom: 15px;">
                            <div class="dashboard-box-border">
                                <h2 class="dashboard-box-title">
                                    {vtranslate('rejected',$MODULE)}
                                </h2>
                                <div class="number rejectedNumber text-red">
                                    {number_format($lead->rejected)}
                                </div>
                                <div class="dashboard-box-footer">
                                    {vtranslate('orders',$MODULE)}
                                </div>
                            </div>
                        </div>
                        <div class="col-md-15" style="margin-bottom: 15px;">
                            <div class="dashboard-box-border">
                                <h2 class="dashboard-box-title">
                                    {vtranslate('uncalled',$MODULE)}
                                </h2>
                                <div class="number unCallNumber text-orange">
                                    {number_format($lead->unCall)}
                                </div>
                                <div class="dashboard-box-footer">
                                    {vtranslate('orders',$MODULE)}
                                </div>
                            </div>
                        </div>
                        <div class="col-md-15" style="margin-bottom: 15px;">
                            <div class="dashboard-box-border">
                                <h2 class="dashboard-box-title">
                                    {vtranslate('callback',$MODULE)}
                                </h2>
                                <div class="number callbackNumber text-blue">
                                    {number_format($lead->callback)}
                                </div>
                                <div class="dashboard-box-footer">
                                    {vtranslate('orders',$MODULE)}
                                </div>
                            </div>
                        </div>
                        <div class="col-md-15" style="margin-bottom: 15px;">
                            <div class="dashboard-box-border">
                                <h2 class="dashboard-box-title">
                                    {vtranslate('trash',$MODULE)}
                                </h2>
                                <div class="number trashNumber">
                                    {number_format($lead->trash)}
                                </div>
                                <div class="dashboard-box-footer">
                                    {vtranslate('orders',$MODULE)}
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="dashBoard-list-center">
                    <div class="row">
                        <div class="col-md-4 chart-box">
                            <div class="dashboard-box-process dashboard-box-border">
                                <div class="dashboard-box-process-header">
                                    <h5>
                                        {vtranslate('my_performance',$MODULE)}
                                    </h5>
                                </div>
                                <div class="dashboard-box-process-content">
                                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                                        <li class="nav-item active">
                                            <a class="nav-link" id="total-calls-tab" data-toggle="tab" href="#total-calls" role="tab" aria-controls="total-calls" aria-selected="true">
                                                {vtranslate('total_calls',$MODULE)}
                                            </a>
                                        </li>
                                    </ul>
                                    <div class="tab-content" id="myTabContent">
                                        <div class="tab-pane fade in active" id="total-calls" role="tabpanel" aria-labelledby="total-calls-tab">
                                            <canvas id="total_calls" style=" min-height: 180px"></canvas>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 chart-box">
                            <div class="dashboard-box-process dashboard-box-border">
                                <div class="dashboard-box-process-header">
                                    <h5>
                                        {vtranslate('my_sale_funnel',$MODULE)}
                                    </h5>
                                </div>
                                <div class="dashboard-box-process-content">
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li class="nav-item active">
                                        </li>
                                    </ul>
                                    <canvas id="funnel-chart"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 chart-box">
                            <div class="dashboard-box-process dashboard-box-border">
                                <div class="dashboard-box-process-header">
                                    <h5>
                                        {vtranslate('performance_comparison',$MODULE)}
                                    </h5>
                                </div>
                                <div class="dashboard-box-process-content">
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li class="nav-item active">
                                        </li>
                                    </ul>
{*                                    <div class="form-group">*}
{*                                        <canvas id="performance_comparison_avg"></canvas>*}
{*                                    </div>*}
                                    <div class="">
                                        <canvas id="performance_comparison_first" height="35"></canvas>
                                    </div>
                                    <div class="">
                                        <canvas id="performance_comparison_second" height="35"></canvas>
                                    </div>
                                    <div class="">
                                        <canvas id="performance_comparison_third" height="35"></canvas>
                                    </div>
                                    <div class="">
                                        <canvas id="performance_comparison_fourth" height="35"></canvas>
                                    </div>
                                    <div class="">
                                        <canvas id="performance_comparison_fifth" height="35"></canvas>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        {*    <div class="listViewPageDiv content-area-dashboard2 content-area full-width" id="listViewContent">*}
        {*        <div class="" style="padding:15px;">*}
        {*            <ul class="nav nav-tabs" id="pills-tab" role="tablist">*}
        {*                <li class="nav-item active">*}
        {*                    <a class="nav-link " id="pills-home-tab" data-toggle="pill" href="#pills-home" role="tab" aria-controls="pills-home" aria-selected="true">My Leads</a>*}
        {*                </li>*}
        {*                <li class="nav-item">*}
        {*                    <a class="nav-link" id="pills-profile-tab" data-toggle="pill" href="#pills-profile" role="tab" aria-controls="pills-profile" aria-selected="false">My Calback</a>*}
        {*                </li>*}
        {*            </ul>*}
        {*            <div class="tab-content" id="pills-tabContent">*}
        {*                <div class="tab-pane fade in active table-responsive" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab">*}
        {*                    <table id="tblMailList0" class="table table-bordered custom-dt ">*}
        {*                        <thead>*}
        {*                        <tr>*}
        {*                            <th><input type="checkbox" class="check-item" /></th>*}
        {*                            <th>leadId</th>*}
        {*                            <th>name</th>*}
        {*                            <th>prodName</th>*}
        {*                            <th>leadStatusName</th>*}
        {*                            <th>ccCode</th>*}
        {*                            <th>address</th>*}
        {*                            <th>phone</th>*}
        {*                            <th>amount</th>*}
        {*                            <th>agcId</th>*}
        {*                            <th>assignedName</th>*}
        {*                            <th>createdate</th>*}
        {*                            <th>modifydate</th>*}
        {*                            <th>totalCall</th>*}
        {*                        </tr>*}
        {*                        </thead>*}
        {*                        <tbody></tbody>*}
        {*                    </table>*}
        {*                </div>*}
        {*                <div class="tab-pane fade" id="pills-profile" role="tabpanel" aria-labelledby="pills-profile-tab">...</div>*}
        {*            </div>*}
        {*        </div>*}
        {*    </div>*}
    </div>
{/strip}

<script>


    let arr = ({$compare|json_encode});
    let comparisionLabel = [];
    let comparisionValue = [];
    let comparisionAvg = [];
    // let comparisionTotalAvgLabel = [];
    // let comparisionTotalAvgValue = [];
    function comparisionChartData(inputArr) {
        for (let i = 0; i< inputArr.length; i++)
        {
            comparisionValue.push(inputArr[i].lst[0].value);
            comparisionAvg.push(inputArr[i].lst[1].value);

            let label = inputArr[i].label;
            if (label === 'order_process') {
                comparisionLabel.push("{vtranslate('order_process',$MODULE)}");
            } else if (label === 'sale_value') {
                comparisionLabel.push("{vtranslate('sale_value',$MODULE)}");
            } else if (label === 'sale_order') {
                comparisionLabel.push("{vtranslate('sale_order',$MODULE)}");
            } else if (label === 'rate') {
                comparisionLabel.push("{vtranslate('rate',$MODULE)}");
            } else if (label === 'avg_sale_value') {
                comparisionLabel.push("{vtranslate('avg_sale_value',$MODULE)}");
            }
        }
    }
    comparisionChartData(arr);
    var funnelChartData = {
        datasets: [{
            data: [
                {$mySale->lead},
                {$mySale->saleOrder},
                {$mySale->delivered},
                {$mySale->paid}
            ],
            backgroundColor: [
                "#3598dc",
                "#9d58b5",
                "#f2c313",
                "#18bd9b"

            ]
        }],
        labels: [
            "{vtranslate('Lead',$MODULE)}",
            "{vtranslate('Sales Order',$MODULE)}",
            "{vtranslate('Delivered',$MODULE)}",
            "{vtranslate('Paid',$MODULE)}"
        ]
    };
    var funnelChartType = 'funnel';
    var funnelChartOptions = {
        sort: 'desc',
        responsive: true,
        legend: {
            position: 'right',
            onClick: function (e) {
                e.stopPropagation();
            }
        },

        gap: 15,
        animation: {
            animateScale: true,
            animateRotate: true
        },
        tooltips: {
            enabled: true,
            mode: "index",
        },
        plugins: {
            datalabels: {
                formatter: (value, ctx) => {
                    let sum = parseFloat("{$mySale->lead}");
                    let percentage = Math.round((value / sum) * 100) + '%';
                    return percentage;
                },
                color: '#ffffff',
            }
        },
    };
    let funnelChart = new Chart(document.getElementById("funnel-chart").getContext("2d"), config = {
        type: funnelChartType,
        data: funnelChartData,
        options: funnelChartOptions
    });
    var totalCallData = {
        labels: [
            "{vtranslate('connected',$MODULE)} : {$totalCall->connected} {vtranslate('Calls',$MODULE)} ",
            "{vtranslate('busy',$MODULE)} : {$totalCall->busy} {vtranslate('Calls',$MODULE)} ",
            "{vtranslate('invalid',$MODULE)}: {$totalCall->invalid} {vtranslate('Calls',$MODULE)} "
        ],
        datasets: [
            {
                data: [{$totalCall->connected}, {$totalCall->busy}, {$totalCall->invalid}],
                backgroundColor: [
                    "#18bd9b",
                    "#f2c313",
                    "#e94c3b"
                ],
                hoverBackgroundColor: [
                    "#18bd9b",
                    "#f2c313",
                    "#e94c3b"
                ]
            }],
    };
    var totalCallOption = {
        scaleBeginAtZero: false,
        legend: {
            display: true,
            position: 'right'
        },
        elements: {
            center: {
                text: "{$totalCall->total}",
                color: '#36A2EB', //Default black
                fontStyle: 'Helvetica', //Default Arial
                sidePadding: 15 //Default 20 (as a percentage)
            }
        },
        plugins: {
            datalabels: {
                formatter: (value, ctx) => {
                    let datasets = ctx.chart.data.datasets;
                    if (datasets.indexOf(ctx.dataset) === datasets.length - 1) {
                        let sum = datasets[0].data.reduce((a, b) => a + b, 0);
                        let percentage = Math.round((value / sum) * 100) + '%';
                        return percentage;
                    } else {
                        return percentage;
                    }
                },
                color: '#ffffff',
            }
        },
        tooltips: {
            callbacks: {
                label: function(tooltipItem, data) {
                    console.log(tooltipItem);
                    return data.labels[tooltipItem.index] || '';
                }
            }
        }
    };
    var totalCallType = 'doughnut';
    let totalCall = new Chart(document.getElementById('total_calls').getContext('2d'), {
        type: totalCallType,
        data: totalCallData,
        options: totalCallOption
    });

    var performanceComparisonFirstData = {
        labels: [comparisionLabel[0]],
        datasets: [{
            // label: comparisionTooltipLabel[0],
            label: 'Agent',
            backgroundColor: "#f2c313",
            data: [comparisionValue[0]],
        }, {
            // label: comparisionTooltipLabel[1],
            label: 'Team',
            backgroundColor: "#2196f3",
            data: [comparisionAvg[0]],
        }]
    };

    var performanceComparisonSecondData = {
        labels: [comparisionLabel[1]],
        datasets: [{
            // label: comparisionTooltipLabel[0],
            label: 'Agent',
            backgroundColor: "#f2c313",
            data: [comparisionValue[1]],
        }, {
            // label: comparisionTooltipLabel[1],
            label: 'Team',
            backgroundColor: "#2196f3",
            data: [comparisionAvg[1]],
        }]
    };

    var performanceComparisonThirdData = {
        labels: [comparisionLabel[2]],
        datasets: [{
            // label: comparisionTooltipLabel[0],
            label: 'Agent',
            backgroundColor: "#f2c313",
            data: [comparisionValue[2]],
        }, {
            // label: comparisionTooltipLabel[1],
            label: 'Team',
            backgroundColor: "#2196f3",
            data: [comparisionAvg[2]],
        }]
    };

    var performanceComparisonFourthData = {
        labels: [comparisionLabel[3]],
        datasets: [{
            // label: comparisionTooltipLabel[0],
            label: 'Agent',
            backgroundColor: "#f2c313",
            data: [comparisionValue[3]],
        }, {
            // label: comparisionTooltipLabel[1],
            label: 'Team',
            backgroundColor: "#2196f3",
            data: [comparisionAvg[3]],
        }]
    };

    var performanceComparisonFifthData = {
        labels: [comparisionLabel[4]],
        datasets: [{
            // label: comparisionTooltipLabel[0],
            label: 'Agent',
            backgroundColor: "#f2c313",
            data: [comparisionValue[4]],
        }, {
            // label: comparisionTooltipLabel[1],
            label: 'Team',
            backgroundColor: "#2196f3",
            data: [comparisionAvg[4]],
        }]
    };

    var performanceComparisonOptions = {
        legend: {
            display: false,
        },
        scales: {
            yAxes: [{
                barPercentage: 0.95,
                ticks: {
                    padding: 10,
                },
                gridLines: {
                    display: false,
                },
                afterFit: function(scaleInstance) {
                    scaleInstance.width = 110; // sets the width to 100px
                }
            }],
            xAxes: [{
                display: false,
                ticks: {
                    beginAtZero: true,
                },
            }],
        },
        tooltips: {
            mode: 'nearest',
            yAlign: 'right',
            xAlign: 'center',
            callbacks: {
                title: function() { },
                label: function(tooltipItem, data) {
                    console.log(tooltipItem);
                    let padding = ' ';
                    let tooltipValue = tooltipItem.xLabel;
                    let unit = '';
                    if (tooltipItem.index === 3) {
                        unit = '%';
                    }
                    if (tooltipItem.index === 1 || tooltipItem.index === 4) {
                        tooltipValue = formatMoney(tooltipItem.xLabel);
                    }

                    return padding + tooltipValue + unit;
                }
            }
        },
        responsive: true,
        maintainAspectRatio: true,
    };

    var performanceComparisonFourthOptions = {
        plugins: {
            datalabels: {
                formatter: (value, ctx) => {
                    return value + '%';
                },
            }
        },
    };
    $.extend(performanceComparisonFourthOptions, performanceComparisonOptions);

    var performanceComparisonSecondOptions = {
        plugins: {
            datalabels: {
                formatter: (value, ctx) => {
                    return formatMoney(value);
                }
            },
        }
    };
    $.extend(performanceComparisonSecondOptions, performanceComparisonOptions);

    var performanceComparisonFifthOptions = performanceComparisonSecondOptions;

    var performanceComparisonType = 'horizontalBar';
    var performanceComparisonFirst = new Chart(document.getElementById("performance_comparison_first").getContext('2d'), {
        options: performanceComparisonOptions,
        type: performanceComparisonType,
        data: performanceComparisonFirstData
    });
    var performanceComparisonSecond = new Chart(document.getElementById("performance_comparison_second").getContext('2d'), {
        options: performanceComparisonSecondOptions,
        type: performanceComparisonType,
        data: performanceComparisonSecondData
    });
    var performanceComparisonThird = new Chart(document.getElementById("performance_comparison_third").getContext('2d'), {
        options: performanceComparisonOptions,
        type: performanceComparisonType,
        data: performanceComparisonThirdData
    });
    var performanceComparisonFourth = new Chart(document.getElementById("performance_comparison_fourth").getContext('2d'), {
        options: performanceComparisonFourthOptions,
        type: performanceComparisonType,
        data: performanceComparisonFourthData
    });
    var performanceComparisonFifth = new Chart(document.getElementById("performance_comparison_fifth").getContext('2d'), {
        options: performanceComparisonFifthOptions,
        type: performanceComparisonType,
        data: performanceComparisonFifthData
    });

    var responseBefore ;
    Chart.pluginService.register({
        beforeDraw: function (chart) {
            if (chart.config.options.elements.center) {
                //Get ctx from string
                var ctx = chart.chart.ctx;

                //Get options from the center object in options
                var centerConfig = chart.config.options.elements.center;
                var fontStyle = centerConfig.fontStyle || 'Arial';
                var txt = centerConfig.text;
                var color = centerConfig.color || '#000';
                var sidePadding = centerConfig.sidePadding || 20;
                var sidePaddingCalculated = (sidePadding/100) * (chart.innerRadius * 2)
                //Start with a base font of 30px
                ctx.font = "30px " + fontStyle;

                //Get the width of the string and also the width of the element minus 10 to give it 5px side padding
                var stringWidth = ctx.measureText(txt).width;
                var elementWidth = (chart.innerRadius * 2) - sidePaddingCalculated;

                // Find out how much the font can grow in width.
                var widthRatio = elementWidth / stringWidth;
                var newFontSize = Math.floor(30 * widthRatio);
                var elementHeight = (chart.innerRadius * 2);

                // Pick a new font size so it will not be larger than the height of label.
                var fontSizeToUse = Math.min(newFontSize, elementHeight);

                //Set font settings to draw it correctly.
                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                var centerX = ((chart.chartArea.left + chart.chartArea.right) / 2);
                var centerY = ((chart.chartArea.top + chart.chartArea.bottom) / 2);
                ctx.font = fontSizeToUse+"px " + fontStyle;
                ctx.fillStyle = color;

                //Draw text in center
                ctx.fillText(txt, centerX, centerY);
            }
        }
    });
    var updateMonitorInterval = setInterval(updateMonitor, 10000);
    function updateMonitor()
    {
        var datetime = $('#searchDate').val();

        var params = {
             module: app.getModuleName(),
             action: "ActionAjax",
             mode: "monitor",
             datetime:datetime
         };
        app.request.post({ 'data': params }).then(
         function (err, data) {
             if (data === responseBefore) {
                 return;
             }
             responseBefore = data;
             var res = JSON.parse(data).body.data;
             if (err === null) {
                 $('.approvedNumber').html(res.lead.approved);
                 $('.rejectedNumber').html(res.lead.rejected);
                 $('.callbackNumber').html(res.lead.callback);
                 $('.unCallNumber').html(res.lead.unCall);
                 $('.trashNumber').html(res.lead.trash);
                 funnelChartData.datasets[0].data = [
                     res.mySale.lead,
                     res.mySale.saleOrder,
                     res.mySale.delivered,
                     res.mySale.paid
                 ];
                 funnelChartOptions.plugins.datalabels = {
                     formatter: (value, ctx) => {
                         let sum = parseFloat(res.mySale.lead);
                         let percentage = Math.round((value / sum) * 100) + '%';
                         return percentage;
                     },
                     color: '#ffffff',
                 };
                 totalCallData.datasets[0].data = [
                     res.totalCall.connected,
                     res.totalCall.busy,
                     res.totalCall.invalid,
                 ];

                 totalCallData.labels= [
                     "{vtranslate('connected',$MODULE)} : "+res.totalCall.connected +" {vtranslate('Calls',$MODULE)} ",
                     "{vtranslate('busy',$MODULE)} :  "+res.totalCall.busy +" {vtranslate('Calls',$MODULE)} ",
                     "{vtranslate('invalid',$MODULE)}: "+res.totalCall.invalid+" {vtranslate('Calls',$MODULE)} "
                 ];
                 totalCallOption.elements.text= res.totalCall.total;
                 totalCallOption.plugins= {
                     datalabels: {
                         formatter: (value, ctx) => {
                             let datasets = ctx.chart.data.datasets;
                             if (datasets.indexOf(ctx.dataset) === datasets.length - 1) {
                                 let sum = datasets[0].data.reduce((a, b) => a + b, 0);
                                 let percentage = Math.round((value / sum) * 100) + '%';
                                 return percentage;
                             } else {
                                 return percentage;
                             }
                         },
                         color: '#ffffff',
                     }
                 };

                 comparisionValue = [];
                 comparisionAvg = [];
                 comparisionLabel = [];
                 comparisionChartData(res.compare.lst);
                 performanceComparisonFirstData.datasets[0].data = [comparisionValue[0]];
                 performanceComparisonFirstData.datasets[1].data = [comparisionAvg[0]];
                 performanceComparisonSecondData.datasets[0].data = [comparisionValue[1]];
                 performanceComparisonSecondData.datasets[1].data = [comparisionAvg[1]];
                 performanceComparisonThirdData.datasets[0].data = [comparisionValue[2]];
                 performanceComparisonThirdData.datasets[1].data = [comparisionAvg[2]];
                 performanceComparisonFourthData.datasets[0].data = [comparisionValue[3]];
                 performanceComparisonFourthData.datasets[1].data = [comparisionAvg[3]];
                 performanceComparisonFifthData.datasets[0].data = [comparisionValue[4]];
                 performanceComparisonFifthData.datasets[1].data = [comparisionAvg[4]];


                 resetChart();
                 funnelChart = new Chart(document.getElementById("funnel-chart").getContext("2d"), {
                     type: funnelChartType,
                     data: funnelChartData,
                     options: funnelChartOptions
                 });
                 totalCall = new Chart(document.getElementById('total_calls').getContext('2d'), {
                     type: totalCallType,
                     data: totalCallData,
                     options: totalCallOption
                 });
                 performanceComparisonFirst = new Chart(document.getElementById("performance_comparison_first").getContext('2d'), {
                     options: performanceComparisonOptions,
                     type: performanceComparisonType,
                     data: performanceComparisonFirstData
                 });
                 performanceComparisonSecond = new Chart(document.getElementById("performance_comparison_second").getContext('2d'), {
                     options: performanceComparisonSecondOptions,
                     type: performanceComparisonType,
                     data: performanceComparisonSecondData
                 });
                 performanceComparisonThird = new Chart(document.getElementById("performance_comparison_third").getContext('2d'), {
                     options: performanceComparisonOptions,
                     type: performanceComparisonType,
                     data: performanceComparisonThirdData
                 });
                 performanceComparisonFourth = new Chart(document.getElementById("performance_comparison_fourth").getContext('2d'), {
                     options: performanceComparisonFourthOptions,
                     type: performanceComparisonType,
                     data: performanceComparisonFourthData
                 });
                 performanceComparisonFifth = new Chart(document.getElementById("performance_comparison_fifth").getContext('2d'), {
                     options: performanceComparisonFifthOptions,
                     type: performanceComparisonType,
                     data: performanceComparisonFifthData
                 });
             }
         }
        );
    }
    function resetChart()
    {
        var funnelChartContainer = $('#funnel-chart').parent();
        var totalCallContainer = $('#total_calls').parent();
        var performanceComparisonFirstContainer = $('#performance_comparison_first').parent();
        var performanceComparisonSecondContainer = $('#performance_comparison_second').parent();
        var performanceComparisonThirdContainer = $('#performance_comparison_third').parent();
        var performanceComparisonFourthContainer = $('#performance_comparison_fourth').parent();
        var performanceComparisonFifthContainer = $('#performance_comparison_fifth').parent();

        $('#funnel-chart').remove(); // this is my <canvas> element
        funnelChartContainer.append('<canvas id="funnel-chart" ><canvas>');
        $('#total_calls').remove(); // this is my <canvas> element
        totalCallContainer.append('<canvas id="total_calls"><canvas>');
        $('#performance_comparison_first').remove(); // this is my <canvas> element
        performanceComparisonFirstContainer.append('<canvas id="performance_comparison_first" height="35"></canvas>');
        $('#performance_comparison_second').remove(); // this is my <canvas> element
        performanceComparisonSecondContainer.append('<canvas id="performance_comparison_second" height="35"></canvas>');
        $('#performance_comparison_third').remove(); // this is my <canvas> element
        performanceComparisonThirdContainer.append('<canvas id="performance_comparison_third" height="35"></canvas>');
        $('#performance_comparison_fourth').remove(); // this is my <canvas> element
        performanceComparisonFourthContainer.append('<canvas id="performance_comparison_fourth" height="35"></canvas>');
        $('#performance_comparison_fifth').remove(); // this is my <canvas> element
        performanceComparisonFifthContainer.append('<canvas id="performance_comparison_fifth" height="35"></canvas>');
    }

    function formatMoney (num) {
        var wholeAndDecimal = String(num.toFixed(2)).split(".");
        var reversedWholeNumber = Array.from(wholeAndDecimal[0]).reverse();
        var formattedOutput = [];

        reversedWholeNumber.forEach( (digit, index) => {
            formattedOutput.push(digit);
            if ((index + 1) % 3 === 0 && index < reversedWholeNumber.length - 1) {
                formattedOutput.push(",");
            }
        });

        formattedOutput = formattedOutput.reverse().join('') + "." + wholeAndDecimal[1];

        return formattedOutput;
    }

</script>