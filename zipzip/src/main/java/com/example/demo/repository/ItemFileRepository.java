package com.example.demo.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Item;
import com.example.demo.dto.ItemFile;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemFileRepository {

   private final SqlSessionTemplate sql;
   
   public Item insertItem(Item item) {
      return item;
   }
   
   public void saveFile(ItemFile itemFile) {
      sql.insert("Item.saveFile", itemFile);
   }
   
   public Item findItem(long itemNum) {
      return sql.selectOne("Item.findItem", itemNum);
   }
   
   public List<ItemFile> findFile(long itemNum) {
      return sql.selectList("Item.findFile", itemNum);
   }
}