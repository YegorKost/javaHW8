package com.yegor.news;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YegorKost on 15.03.2017.
 */
public class ReadNewsImpl implements ReadNews{
    private List<String> parserNews = new ArrayList<>();
    private String rss;

    public void setRss(String rss) {
        this.rss = rss;
    }

    @Override
    public List<String> readNews() {
        List<String> news = null;
        try {
            URL url = new URL(rss);
            URLConnection urlConnection = url.openConnection();

            try (InputStream inputStream = urlConnection.getInputStream();
                 BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream))){

                SAXReader reader = new SAXReader();
                Document document = reader.read(buff);
                Element root = document.getRootElement();
                news = getNewsFromRSS(root);

            } catch (DocumentException e) {
                String err = "Resource cannot be parsed!";
                System.out.printf("%s (%s)\n", err, e.getMessage());
            }

        } catch (IOException e) {
            String err = "Resource is not available!";
            System.out.printf("%s (%s)\n", err, e.getMessage());
        }
        return news;
    }

    private List<String> getNewsFromRSS(Element e) {
        parserNews.clear();
        List<Element> elements = e.elements();
        for (Element element: elements) {
            if (element.getName().equals("item")){
                parserNews.add(element.selectSingleNode("title").getText());
            } else getNewsFromRSS(element);
        }
        return parserNews;
    }
}
