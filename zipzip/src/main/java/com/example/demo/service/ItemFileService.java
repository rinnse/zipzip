package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Item;
import com.example.demo.dto.ItemFile;
import com.example.demo.repository.ItemFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemFileService {
   
   private final ItemFileRepository itemFileRepository;
   
   public void insertItem(Item item) throws IOException {
      int idx = 0;
      
      for (MultipartFile file : item.getItemFile()) {
         
         long itemNum = item.getItemNum();
         System.out.println("itemNum : " + itemNum);
         
         idx++;
         
         String fileOrg = file.getOriginalFilename();
         System.out.println("fileOrg : " + fileOrg);
         
         String fileName = itemNum + "_" + idx + ".jpg";
         System.out.println("fileName : " + fileName);
         
         ItemFile itemFile = new ItemFile();
         itemFile.setItemNum(itemNum);
         itemFile.setFileIdx(idx);
         itemFile.setFileOrg(fileOrg);
         itemFile.setFileName(fileName);
         
         String savePath = "C:/project/final/zipzip/src/main/resources/static/resources/upload/item/" + fileName;
         file.transferTo(new File(savePath));
         
         itemFileRepository.saveFile(itemFile);
      }
   }
   
   public Item findItem(long itemNum) {
      return itemFileRepository.findItem(itemNum);
   }
   
   public List<ItemFile> findFile(long itemNum) {
      return itemFileRepository.findFile(itemNum);
   }
}