package com.oauthcentralization.app.tmsoauth2.model.abstracts;


import com.oauthcentralization.app.tmsoauth2.utils.FileUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

/**
 * @author phuocnguyen
 * @apiNote - xlsx, pdf, json, xls, csv
 */
@SuppressWarnings({"all"})
public class TMSDocsResponse {

    private static final Logger logger = LoggerFactory.getLogger(TMSDocsResponse.class);

    private static final String PARSE_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static ResponseEntity<?> buildScrapeDocsResponse(byte[] dope, String fileName, String extension) {
        HttpHeaders headers = new HttpHeaders();

        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.csv";
        }

        if (!ObjectUtils.allNotNull(extension)) {
            extension = ".csv";
        }

        if (fileName.contains(FileUtils.exactCompleteExt(fileName))) {
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

    public static ResponseEntity<?> buildScrapeDocsResponse(List<byte[]> dopes, String fileName, String extension) {
        HttpHeaders headers = new HttpHeaders();

        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.csv";
        }

        if (!ObjectUtils.allNotNull(extension)) {
            extension = ".csv";
        }

        if (fileName.contains(FileUtils.exactCompleteExt(fileName))) {
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
                .body(dopes);
    }

    public static ResponseEntity<?> buildScrapePdfResponse(byte[] dope, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.pdf";
        }

        if (fileName.contains(FileUtils.exactCompleteExt(fileName))) {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        } else {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName.concat(".pdf") + "\"");
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(APPLICATION_PDF_VALUE))
                .headers(headers)
                .body(dope);
    }

    public static ResponseEntity<?> buildScrapeDocsResponse(ByteArrayInputStream dope, String fileName, String extension) {
        HttpHeaders headers = new HttpHeaders();

        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.csv";
        }

        if (!ObjectUtils.allNotNull(extension)) {
            extension = ".csv";
        }

        if (fileName.contains(FileUtils.exactCompleteExt(fileName))) {
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
                .body(new InputStreamResource(dope));
    }

    public static ResponseEntity<?> buildScrapeDocsCSVResponse(ByteArrayInputStream dope, String fileName, String extension) {
        HttpHeaders headers = new HttpHeaders();

        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.csv";
        }

        if (!ObjectUtils.allNotNull(extension)) {
            extension = ".csv";
        }

        if (fileName.contains(FileUtils.exactCompleteExt(fileName))) {
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
                .contentType(MediaType.parseMediaType("text/csv"))
                .headers(headers)
                .body(new InputStreamResource(dope));
    }

    public static ResponseEntity<?> buildScrapePdfResponse(ByteArrayInputStream dope, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.pdf";
        }

        if (fileName.contains(FileUtils.exactCompleteExt(fileName))) {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        } else {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName.concat(".pdf") + "\"");
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(APPLICATION_PDF_VALUE))
                .headers(headers)
                .body(new InputStreamResource(dope));
    }

    public static ResponseEntity<?> buildScrapeJsonsResponse(byte[] dope, String fileName, String extension) {
        HttpHeaders headers = new HttpHeaders();

        if (!ObjectUtils.allNotNull(fileName)) {
            fileName = "supported-by-tms.json";
        }

        if (!ObjectUtils.allNotNull(extension)) {
            extension = ".json";
        }

        if (fileName.contains(FileUtils.exactCompleteExt(fileName))) {
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
                .contentType(MediaType.parseMediaType(APPLICATION_STREAM_JSON_VALUE))
                .headers(headers)
                .body(dope);
    }
}
