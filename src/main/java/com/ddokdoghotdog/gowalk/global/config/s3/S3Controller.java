package com.ddokdoghotdog.gowalk.global.config.s3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/files")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/image/{folderType}")
    public ResponseEntity<String> uploadFile(@PathVariable("folderType") String folder, @RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "imageUri", required = false) String existingImageUri,
                                             Model model) {
        try {
            String fileUri = s3Service.uploadFile(file, folder);
            model.addAttribute("imageUri", fileUri);
            if(existingImageUri != null && !existingImageUri.equals("")) {
            	s3Service.deleteFile(existingImageUri, folder);
            }
            
            return new ResponseEntity<>(fileUri, HttpStatus.OK);
        }
        catch (Exception e) {
            model.addAttribute("error", "파일 업로드 실패");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
