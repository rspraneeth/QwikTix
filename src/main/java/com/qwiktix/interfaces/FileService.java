package com.qwiktix.interfaces;

import com.qwiktix.exceptions.FileDownloadException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

   String uploadFile(MultipartFile multipartFile) throws FileUploadException, IOException;

   Object downloadFile(String fileName) throws FileDownloadException, IOException;

   boolean delete(String fileName);
}