package com.zjy.girlspic.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zjy.girlspic.data.Photo;
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
 * Created by jiyoung.tsang on 16/7/2.
 */
public class PicDetailsParser {

    public static List<Photo> getData(@NonNull String url){
        checkNotNull(url);

        List<Photo> photos = new ArrayList<Photo>(){};

        try {
            Document document = Jsoup.parse(new URL(url), Constant.timeoutMillis);

            Element picture = document.getElementById("picture");
            Elements elements = picture.getElementsByTag("img");

            for(Element element:elements){
                Photo photo = new Photo();

                photo.title = element.attr("alt");
                photo.src = element.attr("src");

                photos.add(photo);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return photos;
    }
}
