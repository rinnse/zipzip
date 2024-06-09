package com.example.demo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PhotoUtil {
   
   public String ckUpload(MultipartHttpServletRequest request) {
      
      MultipartFile uploadFile = request.getFile("upload");
      
      String fileName = getFileName(uploadFile);
      String realPath = getPath(request);
      String savePath = realPath + fileName;
      String uploadPath = request.getContextPath() + "/upload/" + fileName;   //서버
      String uploadPath1 = "../resources/upload/board/" + fileName;
      //String realPath1 = request.getServletContext().getRealPath("/upload/");
      //String savePath1 = realPath1 + fileName;
      
      log.info("[ckUpload] realPath Url -- " + realPath);
      log.info("[ckUpload] savePath Url -- " + savePath);
      log.info("[ckUpload] uploadPath Url -- " + uploadPath);
      
      uploadFile(savePath, uploadFile);
      
      return uploadPath1;
   }
   
   private void uploadFile(String savePath, MultipartFile uploadFile) {
      File file = new File(savePath);
      try {
         uploadFile.transferTo(file);
      }catch(IOException e) {
         throw new RuntimeException("Failed to upload the file", e);
      }
   }
   
   // 파일이름변경
   private String getFileName(MultipartFile uploadFile) {
      
      String orgFileName = uploadFile.getOriginalFilename();
      String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
      
      String fileName = UUID.randomUUID() + "@" + orgFileName;
      
      return fileName;
   }
   
   // 경로 확인,생성,반환
   private String getPath(MultipartHttpServletRequest request) {
      
      String realPath = "C:/project/webapps/zipzip_0514/src/main/resources/static/resources/upload/board/";   //프로젝트내로 변경
      //String realPath1 = request.getServletContext().getRealPath("/upload/");   //서버에 저장 (1)
      
      // Path 객체
      Path directoryPath = Paths.get(realPath);
      //Path directoryPath1 = Paths.get(realPath1); //서버 path객체(1)
      
      log.info("[getPath] realPath -- " + realPath);
      log.info("[getPath] directoryPath -- " + directoryPath);
      //log.info("[getPath] realPath1 -- " + realPath1); //(1)
      //log.info("[getPath] directoryPath1 -- " + directoryPath1); //(1)
      
      // 디렉토리 생성
      if(!Files.exists(directoryPath)) {
         try {
            Files.createDirectories(directoryPath);
         }catch(IOException e) {
            throw new RuntimeException("Fail to create upload directory", e);
         }
      }
      
      return realPath;
   }
   

   //매물파일 upload
   public int itemFileUpload(String fileName, MultipartFile file) {
	    int result = 0;

	    String savePath = "C:/project/webapps/zipzip_0514/src/main/resources/static/resources/upload/item/";
	    Path directoryPath = Paths.get(savePath);

	    // 디렉토리 생성
	    if (!Files.exists(directoryPath)) {
	        try {
	            Files.createDirectories(directoryPath);
	        } catch (IOException e) {
	            throw new RuntimeException("Could not create upload directory", e);
	        }
	    }

	    // 저장할 파일 객체
	    File saveFile = new File(savePath, fileName);
	    
	    try {
	        // 파일 타입 확인
	        String fileType = file.getContentType();
	        if (fileType == null || !fileType.startsWith("image/")) {
	            throw new RuntimeException("fileType Error -- " + fileType);
	        }
	        
	        // 저장
	        file.transferTo(saveFile);
	        result = 1;
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to upload the itemFile", e);
	    }

	    return result;
	}
   
   // 매물 파일 delete
   public int itemFileDelete(String fileName) {
	   int result = 0;
	   
	   String deleteFile = "C:/project/webapps/zipzip_0514/src/main/resources/static/resources/upload/item/" + fileName;
	   Path path = Paths.get(deleteFile);
	   
       try {
           Files.deleteIfExists(path);
       }
       catch (IOException e) {
           e.printStackTrace();
       }
	   
	   return result;
   }
   
   
}