/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is: vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/
jQuery.Class("Vtiger_Socket_Js",{}, {
    
    getSockets: function() {
        var socket = new WebSocket('ws://localhost:5353');

        // Open the socket
        socket.onopen = function(event) {
            var msg = 'I am the client.';

            console.log('> ' + msg);
            socket.send('start');
            // Send an initial message
            //var t = new Date();
            //    socket.send(t.toLocaleDateString());
            // setInterval( ()=> {
            //     var t = new Date();
            //     socket.send(t.toLocaleDateString());
            // }, 1000);
            

            // Listen for messages
            socket.onmessage = function(event) {
                console.log('< ' + event.data);
                var data = [];
                var messages_html = '';
                var total = '';
                var messages = [];
                if(event.data) {
                    data = JSON.parse(event.data);
                    total =  data.total;
                    noti_type = data.type;
                    messages = data.messages;
                }
                messages.forEach(function(mes) {
                    messages_html += '<a class="dropdown-item" href="#">"' + mes +'</a>';
                });
                if(noti_type == 'notification') {
                    elm_type = '.notificationHeader';
                }else {
                    elm_type = '.gmailHeader';
                }
                console.log('< ' + messages_html);
                $(elm_type + '.dropdown-menu').html(messages_html);
                if(total > 99) {
                    total = total + '+';
                }
                $(elm_type + '.notificationHeader .number-notifi').html(total);
            };

            // Listen for socket closes
            socket.onclose = function(event) {
                console.log('Client notified socket has closed', event);
            };

            // To close the socket....
            //socket.close()

        };
    },
    init: function() {
        // this.getSockets();
    }
    
    // registerEvents : function() {
    //     this.getSockets();
    // }
});

$(function(){
    var socket = new Vtiger_Socket_Js();
    //socket.getSockets();
});