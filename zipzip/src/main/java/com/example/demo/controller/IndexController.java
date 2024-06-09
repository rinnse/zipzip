package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.Event;
import com.example.demo.dto.Item;
import com.example.demo.dto.News;
import com.example.demo.dto.Notice;
import com.example.demo.dto.Sense;
import com.example.demo.service.EventService;
import com.example.demo.service.ItemService;
import com.example.demo.service.NewsService;
import com.example.demo.service.NoticeService;
import com.example.demo.service.SenseService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
   
   private final NoticeService noticeService;
   private final NewsService newsService;
   private final SenseService senseService;
   private final EventService eventService;
   private final ItemService itemService;
   
   @GetMapping("/")
   public String index(Notice notice, Sense sense, Event event, Model model) throws Exception {
      List<News> newsList = newsService.getNewsDatas();
      List<Notice> noticeList = noticeService.selectList(notice);
      List<Sense> senseList = senseService.selectList(sense);
      List<Event> eventList = eventService.selectList(event);
      
      List<Item> list = itemService.recent9item();
      if(list!=null) {
         for(int i=0;i<list.size();i++) {
            //log.info("몇개? {}",list.get(i).getItemNum());
            String tamp="";
            switch(list.get(i).getItemPtype()) {
               case "S" : tamp="매매"; break;
               case "Y" : tamp="전세"; break;
               case "M" : tamp="월세"; break;
            }
            
            list.get(i).setItemPtype(tamp);
            //보증금
            long tmp = list.get(i).getItemDeposit();
            if (tmp != 0) {
               String a="";
               if (tmp>=10000) {
                  a = String.valueOf(tmp/10000)+"억 ";
                  if (tmp % 10000 != 0) {
                       a += String.valueOf(tmp % 10000);
                   }
               }
               else {
                  a = String.valueOf(tmp);
               }
               list.get(i).setTransD(a);

            }
            //월세
            tmp = list.get(i).getItemMonthPrice();
            if (tmp != 0) {
               String a="";
               if (tmp>=10000) {
                  a = String.valueOf(tmp/10000)+"억 ";
                  if (tmp % 10000 != 0) {
                       a += String.valueOf(tmp % 10000);
                   }
               }
               else {
                  a = String.valueOf(tmp);
               }
               list.get(i).setTransM(a);
            }
         }
         model.addAttribute("list",list);
      }
   
      model.addAttribute("news", newsList);
      model.addAttribute("noticeList", noticeList);
      model.addAttribute("senseList", senseList);
      model.addAttribute("eventList", eventList);
      
      return "index";
   }
   
   @GetMapping("/401")
   public String authentication() {
      return "error/401";
   }
   
   @GetMapping("/403")
   public String authorization() {
      return "error/403";
   }
   
}