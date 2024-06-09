package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.example.demo.dto.News;

@Service
public class NewsService {
	private static String News_URL = "https://realestate.daum.net/news/all";
	
	@PostConstruct
	public List<News> getNewsDatas() throws IOException {
		List<News> newsList = new ArrayList<News>();
		
		Document document = Jsoup.connect(News_URL).get();
		
		Elements contents = document.select("ul.list_live li");
		
		for (Element content : contents) {
			News news = News.builder()
                    .subject(content.select("div.cont strong a").text())	// 제목
                    .content(content.select("div.cont p a").text())			// 내용
                    .url(content.select("a").attr("abs:href"))				// 링크
                    .info(content.select("span.info span.source").text())	// 신문사
                    .build();
            newsList.add(news);
            
            //System.out.println(newsList.size());
            
            if (newsList.size() >= 5) {
            	break;
            }
        }
		
        return newsList;
	}
	
	@PostConstruct
	public List<News> newsList() throws IOException {
		List<News> newsList = new ArrayList<News>();
		
		Document document = Jsoup.connect(News_URL).get();
		
		Elements contents = document.select("ul.list_live li");
		
		for (Element content : contents) {
			News news = News.builder()
                    .subject(content.select("div.cont strong a").text())	// 제목
                    .content(content.select("div.cont p a").text())			// 내용
                    .url(content.select("a").attr("abs:href"))				// 링크
                    .info(content.select("span.info span.source").text())	// 신문사
                    .build();
            newsList.add(news);
        }
		
        return newsList;
	}
}
