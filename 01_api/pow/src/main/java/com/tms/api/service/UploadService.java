package com.tms.api.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tms.api.response.TMSResponse;

public interface UploadService {

    public TMSResponse createLeadByExcel(@RequestParam("file") MultipartFile file, @PathVariable Integer cpId,
            @PathVariable Integer clId, String SESSION_ID, int userId, int orgId) throws Exception;
}
