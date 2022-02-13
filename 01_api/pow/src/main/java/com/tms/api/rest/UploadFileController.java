package com.tms.api.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tms.api.exception.TMSException;
import com.tms.api.response.TMSResponse;

@RestController
public class UploadFileController extends BaseController {
    private final Path fileStorageLocation;
    Logger logger = LoggerFactory.getLogger(UploadFileController.class);
    
    public UploadFileController() throws TMSException {
        fileStorageLocation = Paths.get("upload")
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            logger.error(ex.toString());
            throw new TMSException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    public String storeFile(MultipartFile file) throws TMSException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new TMSException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            logger.error(ex.toString());
            throw new TMSException("Could not store file " + fileName + ". Please try again!");
        }
    }

    @PostMapping("uploadFile")
    public TMSResponse uploadFile(@RequestParam("file") MultipartFile file) throws TMSException {
        String fileName = storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return TMSResponse.buildResponse(fileDownloadUri);
    }
    
}
