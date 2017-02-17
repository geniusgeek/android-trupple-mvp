package com.github.geniusgeek.trupple_mvp.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by root on 11/23/16.
 */

public final class VideoUtils {

    public static final String DEVELOPER_KEY = "AIzaSyCnGk0aZVksrMFXMVi_jT3vGYEEGccljVc";


    /**
     * Private constructor: class cannot be instantiated
     */
    /**
     * @throws AssertionError when trying to create an instance of this class
     */
    private VideoUtils() {
        throw new AssertionError("cannot instantiate this class");
    }


    /**
     * utility to get a youtube id from a url
     *
     * @param youtubeUrl
     * @return
     */
    public final static String getYoutubeID(String youtubeUrl) {
        if (youtubeUrl == null)
            return null;

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeUrl);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String getUrlVideoRTSP(String urlYoutube) {
        try {
            String gdy = "http://gdata.youtube.com/feeds/api/videos/";
            DocumentBuilder documentBuilder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            String id = getYoutubeID(urlYoutube);
            URL url = new URL(gdy + id);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            Document doc = documentBuilder.parse(connection.getInputStream());
            Element el = doc.getDocumentElement();
            NodeList list = el.getElementsByTagName("media:content");// /media:content
            String cursor = urlYoutube;
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node != null) {
                    NamedNodeMap nodeMap = node.getAttributes();
                    HashMap<String, String> maps = new HashMap<String, String>();
                    for (int j = 0; j < nodeMap.getLength(); j++) {
                        Attr att = (Attr) nodeMap.item(j);
                        maps.put(att.getName(), att.getValue());
                    }
                    if (maps.containsKey("yt:format")) {
                        String f = maps.get("yt:format");
                        if (maps.containsKey("url")) {
                            cursor = maps.get("url");
                        }
                        if (f.equals("1"))
                            return cursor;
                    }
                }
            }
            return cursor;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return urlYoutube;

    }

}
