package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.Item;
import com.example.demo.dto.ItemFile;
import com.example.demo.service.ItemFileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemFileController {
	
   private final ItemFileService itemFileService;
   
   @GetMapping("/insertFile")
   public String insert() {
      return "insertFile";
   }
   
   @PostMapping("/insertFile")
   public String insertItem(Item item) throws IOException {
      
      itemFileService.insertItem(item);
      
      return "redirect:insertFile";
   }
   
   @GetMapping("/{itemNum}")
   public String findItem(@PathVariable("itemNum") long itemNum, Model model) {
      Item item = itemFileService.findItem(itemNum);
      model.addAttribute("item", item);
      
      List<ItemFile> itemFileList = itemFileService.findFile(itemNum);
      model.addAttribute("itemFileList", itemFileList);
      
      return "detail";
   }
}