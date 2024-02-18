package com.qwiktix.exceptions;

public class FileEmptyException extends BaseFileUploadException {
   public FileEmptyException(String message) {
       super(message);
   }
}