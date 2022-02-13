$(function(){

    $('.main-container').on('click', function () {
        // $(".main-container, .dashBoardContainer").removeClass("blurredContent");
        $(".main-container, .dashBoardContainer").css("margin-left","0");
    });

    $("#dropdownMenuButtonDesk").on("click",function(e){

        $("#dropdownMenuButtonDesk").dropdown();

        if($("#dropdownMenuButtonDesk").parent().hasClass("open")){
            // $(".main-container, .dashBoardContainer").removeClass("blurredContent");
            $(".main-container, .dashBoardContainer").css("margin-left","0");
        }
        else{
            // $(".main-container, .dashBoardContainer").addClass("blurredContent");
            $(".main-container, .dashBoardContainer").css("margin-left","205px");
        }

    });

    var orScroll = false;
    $(".scroll-sidebar").bind('touchmove', function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        e.stopPropagation();

        $("#page").scrollTop(orScroll);
    });


    $("li.with-childs ul li a").click(function(e){
        e.preventDefault();
        e.stopPropagation();
        e.stopImmediatePropagation();

        window.location = $(this).attr("href");
    });

    $("li.with-childs").click(function(e){
        e.preventDefault();
        e.stopPropagation();
        e.stopImmediatePropagation();
        $(this).closest('.mini-sidebar').addClass('iconmenu-click');
        $('.sidebar-nav ul li ul').removeClass('hover');


        if($(this).hasClass("active")){
            $(".left-sidebar li").removeClass("active");
            $(this).find("a[aria-expanded='true']").attr("aria-expanded","false");
            $(this).find("ul").attr("aria-expanded","false").removeClass("in");
        }
        else{
            $(".left-sidebar li").removeClass("active");
            $(this).addClass("active")
            $(this).find("a[aria-expanded='false']").attr("aria-expanded","true");
            $(this).find("ul").attr("aria-expanded","true").addClass("in");
        }
    });

    $('.app-trigger, .app-icon, .app-navigator').on('click',function(e){
        e.preventDefault();
        e.stopPropagation();
        e.stopImmediatePropagation();

        if($("#sidebarnav").hasClass("mini-sidebar")){
            mycOpenLeftSidebar();
            $(".main-container, .dashBoardContainer").css("margin-left","205px");
            // $(".main-container, .dashBoardContainer").addClass("blurredContent");
             sessionStorage.miniMenu=0;
        }
        else{
            mycCloseLeftSidebar();
            $(".main-container, .dashBoardContainer").css("margin-left","0");
            // $(".main-container, .dashBoardContainer").removeClass("blurredContent");
            sessionStorage.miniMenu=1;
        }
        $($.fn.dataTable.tables(true)).DataTable().columns.adjust();
    });

    var hoverTimeout;
    /*
    $('#sidebarnav').hover(function() {

        clearTimeout(hoverTimeout);
        $(".main-container, .dashBoardContainer").addClass("blurredContent");

        mycOpenLeftSidebar();
    }, function() {
        var $self = $(this);
        hoverTimeout = setTimeout(function() {
            mycCloseLeftSidebar();
            $(".main-container, .dashBoardContainer").removeClass("blurredContent");

        }, 10);
    });
    */

    $(".keyword-input").on("click",function(e){
        e.preventDefault();
        e.stopPropagation();
        e.stopImmediatePropagation();
        $(this).focus();
    });

    $("#searchmobile .ti-search").on("click",function(e){
        e.preventDefault();
        e.stopPropagation();
        e.stopImmediatePropagation();

        var element = $(".mobile-search-key");
        var searchValue = element.val();
        var data = {};
        data['searchValue'] = searchValue;
        console.log(data);

        var indexv = new Vtiger_Index_Js();
        element.trigger(indexv._SearchIntiatedEventName,data);
    });


    // $("#page").click(function(e){
    //     if($("#sidebarnav").hasClass("mini-sidebar")){
    //         return;
    //     }
    //     else{
    //         //e.preventDefault();
    //         //e.stopPropagation();
    //         //e.stopImmediatePropagation();
    //         mycCloseLeftSidebar();
    //     }
    // });
    $("#page").click(function(e){
        if($("#sidebarnav").hasClass("mini-sidebar")){
         return;
        }else{
         mycOpenLeftSidebar();
        }
     })
     if ( typeof(Storage) !== 'undefined') {
         if(!sessionStorage.miniMenu){
            sessionStorage.setItem('miniMenu',1);
         }
    } else {
        console.log('Trình duyệt của bạn không hỗ trợ sessionStorage!');
    }
    mycCloseLeftSidebar();
   if(sessionStorage.getItem('miniMenu')==1){
        mycCloseLeftSidebar();
   }else{
    mycOpenLeftSidebar();
   }

    const globscrollbar = new PerfectScrollbar('#page');
    const sidebar = new PerfectScrollbar('.scroll-sidebar',{
        wheelPropagation: false,
    });
    if($('.sidebar-essentials').length > 0 && window.innerWidth>900){
        const sidebaress = new PerfectScrollbar('.sidebar-essentials');
    }
    if($('.settingsNav').length > 0 && window.innerWidth>900){
        const sidebaress_w = new PerfectScrollbar('.settingsNav');
    }

    if($('#table-content').length > 0){
        const list_w = new PerfectScrollbar('#table-content');
    }

    if($('.lineItemTableDiv').length > 0){
        const prod_w = new PerfectScrollbar('.lineItemTableDiv');
    }
    else if($('.lineitemTableContainer').length > 0){
        const prod_w = new PerfectScrollbar('.lineitemTableContainer');
    }



});


//hover mini sidebar
$(document).ready(function() {
    $('.scroll-sidebar').css('overflow', 'inherit');

        $('.sidebar-nav > ul > li').hover(function(){
            if($('#sidebarnav').hasClass('mini-sidebar')){
                $(this).find("ul").attr("aria-expanded","false").removeClass("in");
                $(this).addClass('active');
                $(this).find('ul').addClass('hover');
            }
        }, function(){
            $(this).removeClass('active');
            $(this).find('ul').removeClass('hover');
            $(this).closest('.mini-sidebar').removeClass('iconmenu-click');
        });
        //daterangr for create date search validation
        $('.daterange').daterangepicker({
            format: 'DD/MM/YYYY HH:mm:ss',
            // dateFormat: "dd-mm-yy", 
            // timeFormat: "HH:mm:ss",
             timePicker: true,
             timePicker24Hour: true,
             timePickerIncrement : 1,
             timePickerSeconds : true,
            //  locale : {
            //     format : 'hh:mm:ss'
            // }
            showOn: "button",
            showSecond: true,
        }).on('show.daterangepicker', function(ev, picker) {
            picker.container.find(".calendar-table").hide();
            // locale: {
            //   format: 'DD/MM/YYYY hh:mm:ss'
            // }
        }	
        );
});

function mycOpenLeftSidebar(){
    $("#sidebarnav").removeClass("mini-sidebar");
    var user_info = $('#user_info').val();
    try {
        user_info = JSON.parse(user_info);
        var logoPath = user_info.theme.logoPath;
        $("#sidebarnav .sidebar-logo img").attr('src', logoPath);
    } catch (e) {
    }
    // $(".main-container, .dashBoardContainer").addClass("blurredContent");
    $(".main-container, .dashBoardContainer").css("margin-left","205px");
    if(window.innerWidth<=900){
        $(".left-sidebar").css("width","220px");
        // $("#appnavigator > .app-navigator").css("left","280px");
        $("#appnavigator").css("margin-left","265px");
    }
    else{
        $(".left-sidebar").css("width","265px");
        // $("#appnavigator > .app-navigator").css("left","280px");
        $("#appnavigator").css("margin-left","265px");
    }
    $(".quickTopButtons").attr('style','display:none !important');
    $(".with-childs").find("ul").css("margin-left","10px");
    $("a").addClass("has-arrow");
    // $("#appnavigator > .app-navigator").css("position","fixed");
    // $("#appnavigator > .app-navigator").css("z-index","99");
    // $(".app-navigator").css("padding","10px");

}

function mycCloseLeftSidebar(){

    // $(".main-container, .dashBoardContainer").removeClass("blurredContent");
    $(".main-container, .dashBoardContainer").css("margin-left","0");
    $(".module-action-bar").removeClass("hide");

    $("#sidebarnav").addClass("mini-sidebar");
    try {
        var user_info = $('#user_info').val();
        user_info = JSON.parse(user_info);
        var logoPath = user_info.theme.logo;
        $("#sidebarnav .sidebar-logo img").attr('src', logoPath);
    } catch (e) {
    }
    if(window.innerWidth<=900){
        $(".left-sidebar").css("width","60px");
    }
    else $(".left-sidebar").css("width","60px");
    $("a").removeClass("has-arrow");
    $(".quickTopButtons").removeAttr("style");
    $(".with-childs").find("ul").css("margin-left","-20px");
    $("#appnavigator").css("margin-left","65px");
    $(".main-container").css("padding-left","60px");
    // $("#appnavigator > .app-navigator").css("position","relative");
    // $("#appnavigator > .app-navigator").css("left","5px");
    // $(".module-action-bar").css("left","0px");
    // $(".app-navigator").css("padding","0px");
}
function convertDateTime(datetime,gettime=true){
    var res=[];
    const tz = moment().utcOffset();
    var mo = moment.utc(datetime);
    mo.utcOffset(tz);
     res[0]=mo.format('DD/MM/YYYY');
     if(gettime){
        res[1]=mo.format('HH:mm:ss');
     }else{
        res[1]='';
     }
     if((res[0]=='Invalid date')){
        res[1]=res[0]="";
            return '';
    }
    return res.join(' ');
}