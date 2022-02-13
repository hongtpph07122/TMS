package com.tms.api.dto.Response;

import com.tms.api.utils.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class StreamsResponseDTO {

    private static final String PARSE_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static String exactCompleteExt(String filenameAsPath) {
        int prefix = filenameAsPath.lastIndexOf(".");
        return (prefix > -1) ? filenameAsPath.substring(prefix) : "";
    }

    public static ResponseEntity<?> buildScrapeDocsResponse(byte[] dope, String fileName, String extension) {
        HttpHeaders headers = new HttpHeaders();

        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.csv";
        }

        if (!ObjectUtils.allNotNull(extension)) {
            extension = ".csv";
        }

        if (fileName.contains(exactCompleteExt(fileName))) {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        } else {
            if (extension.contains(".")) {
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + extension + "\"");
            } else {
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".".concat(extension) + "\"");
            }
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(PARSE_MEDIA_TYPE))
                .headers(headers)
                .body(dope);
    }
}
