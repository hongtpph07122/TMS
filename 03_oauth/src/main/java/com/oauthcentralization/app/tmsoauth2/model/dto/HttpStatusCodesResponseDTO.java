package com.oauthcentralization.app.tmsoauth2.model.dto;

public class HttpStatusCodesResponseDTO {

    public static final StatusCodeResponseDTO CONTINUE = new StatusCodeResponseDTO(100, "Continue", "Informational", "");
    public static final StatusCodeResponseDTO SWITCHING_PROTOCOLS = new StatusCodeResponseDTO(101, "Switching Protocols", "Informational", "");
    public static final StatusCodeResponseDTO PROCESSING = new StatusCodeResponseDTO(102, "Processing", "Informational", "");

    public static final StatusCodeResponseDTO OK = new StatusCodeResponseDTO(200, "OK", "Successful", "");
    public static final StatusCodeResponseDTO CREATED = new StatusCodeResponseDTO(201, "Created", "Successful", "");
    public static final StatusCodeResponseDTO ACCEPTED = new StatusCodeResponseDTO(202, "Accepted", "Successful", "");
    public static final StatusCodeResponseDTO NON_AUTHORITATIVE_INFORMATION = new StatusCodeResponseDTO(203, "Non-Authoritative Information", "Successful", "");
    public static final StatusCodeResponseDTO NO_CONTENT = new StatusCodeResponseDTO(204, "No Content", "Successful", "");
    public static final StatusCodeResponseDTO RESET_CONTENT = new StatusCodeResponseDTO(205, "Reset Content", "Successful", "");
    public static final StatusCodeResponseDTO PARTIAL_CONTENT = new StatusCodeResponseDTO(206, "Partial Content", "Successful", "");
    public static final StatusCodeResponseDTO MULTI_STATUS = new StatusCodeResponseDTO(207, "Multi-Status", "Successful", "");
    public static final StatusCodeResponseDTO ALREADY_REPORTED = new StatusCodeResponseDTO(208, "Already Reported", "Successful", "");
    public static final StatusCodeResponseDTO IM_USED = new StatusCodeResponseDTO(226, "IM Used", "Successful", "");


    public static final StatusCodeResponseDTO MULTIPLE_CHOICES = new StatusCodeResponseDTO(300, "Multiple Choices", "Redirection", "");
    public static final StatusCodeResponseDTO MOVED_PERMANENTLY = new StatusCodeResponseDTO(301, "Moved Permanently", "Redirection", "");
    public static final StatusCodeResponseDTO FOUND = new StatusCodeResponseDTO(302, "Found", "Redirection", "");
    public static final StatusCodeResponseDTO SEE_OTHER = new StatusCodeResponseDTO(303, "See Other", "Redirection", "");
    public static final StatusCodeResponseDTO NOT_MODIFIED = new StatusCodeResponseDTO(304, "Not Modified", "Redirection", "");
    public static final StatusCodeResponseDTO USE_PROXY = new StatusCodeResponseDTO(305, "Use Proxy", "Redirection", "");
    public static final StatusCodeResponseDTO RESERVED = new StatusCodeResponseDTO(306, "Reserved", "Redirection", "");
    public static final StatusCodeResponseDTO TEMPORARY_REDIRECT = new StatusCodeResponseDTO(307, "Temporary Redirect", "Redirection", "");
    public static final StatusCodeResponseDTO PERMANENT_REDIRECT = new StatusCodeResponseDTO(308, "Permanent Redirect", "Redirection", "");


    public static final StatusCodeResponseDTO BAD_REQUEST = new StatusCodeResponseDTO(400, "Bad Request", "Client Error", "");
    public static final StatusCodeResponseDTO UNAUTHORIZED = new StatusCodeResponseDTO(401, "Unauthorized", "Client Error", "");
    public static final StatusCodeResponseDTO PAYMENT_REQUIRED = new StatusCodeResponseDTO(402, "Payment Required", "Client Error", "");
    public static final StatusCodeResponseDTO FORBIDDEN = new StatusCodeResponseDTO(403, "Forbidden", "Client Error", "");
    public static final StatusCodeResponseDTO NOTFOUND = new StatusCodeResponseDTO(404, "Not Found", "Client Error", "");
    public static final StatusCodeResponseDTO METHOD_NOT_ALLOWED = new StatusCodeResponseDTO(405, "Method Not Allowed", "Client Error", "");
    public static final StatusCodeResponseDTO NOT_ACCEPTABLE = new StatusCodeResponseDTO(406, "Not Acceptable", "Client Error", "");
    public static final StatusCodeResponseDTO PROXY_AUTHENTICATION_REQUIRED = new StatusCodeResponseDTO(407, "Proxy Authentication Required", "Client Error", "");
    public static final StatusCodeResponseDTO REQUEST_TIMEOUT = new StatusCodeResponseDTO(408, "Request Timeout", "Client Error", "");
    public static final StatusCodeResponseDTO CONFLICT = new StatusCodeResponseDTO(409, "Conflict", "Client Error", "");
    public static final StatusCodeResponseDTO GONE = new StatusCodeResponseDTO(410, "Gone", "Client Error", "");
    public static final StatusCodeResponseDTO LENGTH_REQUIRED = new StatusCodeResponseDTO(411, "Length Required", "Client Error", "");
    public static final StatusCodeResponseDTO PRECONDITION_FAILED = new StatusCodeResponseDTO(412, "Precondition Failed", "Client Error", "");
    public static final StatusCodeResponseDTO REQUEST_ENTITY_TOO_LARGE = new StatusCodeResponseDTO(413, "Request Entity Too Large", "Client Error", "");
    public static final StatusCodeResponseDTO REQUEST_URI_TOO_LONG = new StatusCodeResponseDTO(414, "Request-URI Too Long", "Client Error", "");
    public static final StatusCodeResponseDTO UNSUPPORTED_MEDIA_TYPE = new StatusCodeResponseDTO(415, "Unsupported Media Type", "Client Error", "");
    public static final StatusCodeResponseDTO REQUESTED_RANGE_NOT_SATISFIABLE = new StatusCodeResponseDTO(416, "Requested Range Not Satisfiable", "Client Error", "");
    public static final StatusCodeResponseDTO EXPECTATION_FAILED = new StatusCodeResponseDTO(417, "Expectation Failed", "Client Error", "");
    public static final StatusCodeResponseDTO IM_A_TEAPOT = new StatusCodeResponseDTO(418, "I’m a teapot", "Client Error", "");
    public static final StatusCodeResponseDTO ENHANCE_YOUR_CALM = new StatusCodeResponseDTO(420, "Enhance Your Calm", "Client Error", "");
    public static final StatusCodeResponseDTO UNPROCESSABLE_ENTITY = new StatusCodeResponseDTO(422, "Unprocessable Entity", "Client Error", "");
    public static final StatusCodeResponseDTO LOCKED = new StatusCodeResponseDTO(423, "Locked", "Client Error", "");
    public static final StatusCodeResponseDTO FAILED_DEPENDENCY = new StatusCodeResponseDTO(424, "Failed Dependency", "Client Error", "");
    public static final StatusCodeResponseDTO UNORDERED_COLLECTION = new StatusCodeResponseDTO(425, "Unordered Collection", "Client Error", "");
    public static final StatusCodeResponseDTO UPGRADE_REQUIRED = new StatusCodeResponseDTO(426, "Upgrade Required", "Client Error", "");
    public static final StatusCodeResponseDTO PRECONDITION_REQUIRED = new StatusCodeResponseDTO(428, "Precondition Required", "Client Error", "");
    public static final StatusCodeResponseDTO TOO_MANY_REQUESTS = new StatusCodeResponseDTO(429, "Too Many Requests", "Client Error", "");
    public static final StatusCodeResponseDTO REQUEST_HEADER_FIELDS_TOO_LARGE = new StatusCodeResponseDTO(431, "Request Header Fields Too Large", "Client Error", "");
    public static final StatusCodeResponseDTO NO_RESPONSE = new StatusCodeResponseDTO(444, "No Response", "Client Error", "");
    public static final StatusCodeResponseDTO RETRY_WITH = new StatusCodeResponseDTO(449, "Retry With", "Client Error", "");
    public static final StatusCodeResponseDTO BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS = new StatusCodeResponseDTO(450, "Blocked by Windows Parental Controls", "Client Error", "");
    public static final StatusCodeResponseDTO UNAVAILABLE_FOR_LEGAL_REASONS = new StatusCodeResponseDTO(451, "Unavailable For Legal Reasons", "Client Error", "");
    public static final StatusCodeResponseDTO CLIENT_CLOSED_REQUEST = new StatusCodeResponseDTO(499, "Client Closed Request", "Client Error", "");


    public static final StatusCodeResponseDTO INTERNAL_SERVER_ERROR = new StatusCodeResponseDTO(500, "Internal Server Error", "Server Error", "");
    public static final StatusCodeResponseDTO NOT_IMPLEMENTED = new StatusCodeResponseDTO(501, "Not Implemented", "Server Error", "");
    public static final StatusCodeResponseDTO BAD_GATEWAY = new StatusCodeResponseDTO(502, "Bad Gateway", "Server Error", "");
    public static final StatusCodeResponseDTO SERVICE_UNAVAILABLE = new StatusCodeResponseDTO(503, "Service Unavailable", "Server Error", "");
    public static final StatusCodeResponseDTO GATEWAY_TIMEOUT = new StatusCodeResponseDTO(504, "Gateway Timeout", "Server Error", "");
    public static final StatusCodeResponseDTO HTTP_VERSION_NOT_SUPPORTED = new StatusCodeResponseDTO(505, "HTTP Version Not Supported", "Server Error", "");
    public static final StatusCodeResponseDTO VARIANT_ALSO_NEGOTIATES = new StatusCodeResponseDTO(506, "Variant Also Negotiates", "Server Error", "");
    public static final StatusCodeResponseDTO INSUFFICIENT_STORAGE = new StatusCodeResponseDTO(507, "Insufficient Storage", "Server Error", "");
    public static final StatusCodeResponseDTO LOOP_DETECTED = new StatusCodeResponseDTO(508, "Loop Detected", "Server Error", "");
    public static final StatusCodeResponseDTO BANDWIDTH_LIMIT_EXCEEDED = new StatusCodeResponseDTO(509, "Bandwidth Limit Exceeded", "Server Error", "");
    public static final StatusCodeResponseDTO NOT_EXTENDED = new StatusCodeResponseDTO(510, "Not Extended", "Server Error", "");
    public static final StatusCodeResponseDTO NETWORK_AUTHENTICATION_REQUIRED = new StatusCodeResponseDTO(511, "Network Authentication Required", "Server Error", "");
    public static final StatusCodeResponseDTO NETWORK_READ_TIMEOUT_ERROR = new StatusCodeResponseDTO(598, "Network Read Timeout Error", "Server Error", "");
    public static final StatusCodeResponseDTO NETWORK_CONNECT_TIMEOUT_ERROR = new StatusCodeResponseDTO(599, "Network Connect Timeout Error", "Server Error", "");
}
