package com.zjy.girlspic.service;

import android.support.annotation.NonNull;

import com.zjy.girlspic.data.Image;
import com.zjy.girlspic.util.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jiyoung.tsang on 16/6/17.
 */
public class GirlsPicParser {

    private final static String Host = "http://www.meizitu.com";

    public static List<Image> getGallery(@NonNull int page){
        checkNotNull(page);

        List<Image> images = new ArrayList<Image>();
        String url = String.format("%s/a/list_1_%d.html",Host,page);
        try {
            Document document = Jsoup.parse(new URL(url), Constant.timeoutMillis);
            Elements elements = document.getElementsByClass("wp-item");

            for(Element element:elements){
                Image image = new Image();

                Element ele = element.select("img").first();
                image.title = ele.attr("alt").replace("<b>","").replace("</b>","");
                image.imgUrl = ele.attr("src");
                image.link = element.select("a").first().attr("href");

                images.add(image);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return images;
    }
}
