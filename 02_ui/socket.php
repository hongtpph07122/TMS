<?php
//Code by: Nabi KAZ <www.nabi.ir>

// set some variables
$host = "127.0.0.1";
$port = 5353;
// Set socket from API
$host_api = "127.0.0.1";
$port_api = 5354;
// don't timeout!
set_time_limit(0);

// create socket
$socket = socket_create(AF_INET, SOCK_STREAM, 0)or die("Could not create socket\n");
$socket_api = socket_create(AF_INET, SOCK_STREAM, 0)or die("Could not create socket\n");
// bind socket to port
$result = socket_bind($socket, $host, $port)or die("Could not bind to socket\n");
$result_api = socket_bind($socket_api, $host_api, $port_api)or die("Could not bind to socket API\n");
// start listening for connections
$result = socket_listen($socket, 20)or die("Could not set up socket listener\n");
$result_api = socket_listen($socket_api, 20)or die("Could not set up socket API listener\n");
$flag_handshake = false;
$flag_handshake_api = false;
$client = null;
$client_api = null;
do {
    if (!$client) {
        // accept incoming connections
        // client another socket to handle communication
        $client = socket_accept($socket)or die("Could not accept incoming connection\n");
    }
    if (!$client_api) {
        // accept incoming connections
        // client another socket to handle communication
        $client_api = socket_accept($socket_api)or die("Could not accept incoming connection\n");
    }
    $bytes =  @socket_recv($client, $data, 2048, 0);
    $bytes_api =  @socket_recv($client_api, $data_api, 2048, 0);
    if ($flag_handshake == false) {
        if ((int)$bytes == 0)
            continue;
        //print("Handshaking headers from client: ".$data."\n");
        if (handshake($client, $data, $socket)) {
            $flag_handshake = true;
        }
    }
    elseif($flag_handshake == true) {
        if ($data != "") {
            print("> Received data from UI\n");
            $decoded_data = unmask($data);
            print("< ".$decoded_data."\n");
            $response = strrev($decoded_data);
            socket_write($client_api, encode($response));
            print("> ".$response."\n");
            socket_close($client_api);
            $client_api = null;
            $flag_handshake = false;
        }
    }

    

    if ($flag_handshake_api == false) {
        if ((int)$bytes_api == 0)
            continue;
        //print("Handshaking headers from client: ".$data."\n");
        if (handshake($client_api, $data_api, $socket_api)) {
            $flag_handshake_api = true;
        }
    }
    elseif($flag_handshake_api == true) {
        if ($data_api != "") {
            print("> Received data from API\n");
            $decoded_data = unmask($data_api);
            print("< ".$decoded_data."\n");
            $response = strrev($decoded_data);
            socket_write($client, encode($response));
            print("> ".$response."\n");
            socket_close($client);
            $client = null;
            $client_api = null;
            $flag_handshake_api = false;
        }
    }


    //socket_close($client);
} while (true);

// close sockets
socket_close($client);
socket_close($socket);
socket_close($client_api);
socket_close($socket_api);
function handshake($client, $headers, $socket) {

    if (preg_match("/Sec-WebSocket-Version: (.*)\r\n/", $headers, $match))
        $version = $match[1];
    else {
        print("The client doesn't support WebSocket");
        return false;
    }

    if ($version == 13) {
        // Extract header variables
        if (preg_match("/GET (.*) HTTP/", $headers, $match))
            $root = $match[1];
        if (preg_match("/Host: (.*)\r\n/", $headers, $match))
            $host = $match[1];
        if (preg_match("/Origin: (.*)\r\n/", $headers, $match))
            $origin = $match[1];
        if (preg_match("/Sec-WebSocket-Key: (.*)\r\n/", $headers, $match))
            $key = $match[1];

        $acceptKey = $key.'258EAFA5-E914-47DA-95CA-C5AB0DC85B11';
        $acceptKey = base64_encode(sha1($acceptKey, true));

        $upgrade = "HTTP/1.1 101 Switching Protocols\r\n".
            "Upgrade: websocket\r\n".
            "Connection: Upgrade\r\n".
            "Sec-WebSocket-Accept: $acceptKey".
            "\r\n\r\n";

        socket_write($client, $upgrade);
        return true;
    } else {
        print("WebSocket version 13 required (the client supports version {$version})");
        return false;
    }
}

function unmask($payload) {
    $length = ord($payload[1]) & 127;

    if ($length == 126) {
        $masks = substr($payload, 4, 4);
        $data = substr($payload, 8);
    }
    elseif($length == 127) {
        $masks = substr($payload, 10, 4);
        $data = substr($payload, 14);
    }
    else {
        $masks = substr($payload, 2, 4);
        $data = substr($payload, 6);
    }

    $text = '';
    for ($i = 0; $i < strlen($data); ++$i) {
        $text .= $data[$i] ^ $masks[$i % 4];
    }
    return $text;
}

function encode($text) {
    // 0x1 text frame (FIN + opcode)
    $b1 = 0x80 | (0x1 & 0x0f);
    $length = strlen($text);

    if ($length <= 125)
        $header = pack('CC', $b1, $length);
    elseif($length > 125 && $length < 65536)$header = pack('CCS', $b1, 126, $length);
    elseif($length >= 65536)
    $header = pack('CCN', $b1, 127, $length);

    return $header.$text;
}