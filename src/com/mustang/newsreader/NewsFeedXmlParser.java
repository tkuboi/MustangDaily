package com.mustang.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class NewsFeedXmlParser {
    // We don't use namespaces
    private static final String ns = null;
    
    private ArrayList<Article> m_arrArticles;
    private ArrayList<String> imgSrcList;
    
    public NewsFeedXmlParser() {
        m_arrArticles = new ArrayList<Article>();
        imgSrcList = new ArrayList<String>();
    }
   
    public ArrayList<Article> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
        	//Log.d("dedug","in parse");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            int count = readFeed(parser);
            Log.d("articleCount","number of articles = " + count);
            return m_arrArticles;
        } finally {
            in.close();
        }
    }

    private int readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    	int readCount = 0;
        //Log.d("debug","readFeed");
        if(parser == null)
        	Log.d("debug", "parser is null");
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
	       if (parser.getEventType() != XmlPullParser.START_TAG) {
	           continue;
	       }
	       String name = parser.getName();
	       // Starts by looking for the entry tag
	       if (name.equals("channel")) {
	    	   readCount += readChannel(parser);
	       } else {
	           skip(parser);
	       }
        }  
        return readCount;
    }

    private int readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d("debug","readFeed");
        if(parser == null)
        	Log.d("debug", "parser is null");
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
	       if (parser.getEventType() != XmlPullParser.START_TAG) {
	           continue;
	       }
	       String name = parser.getName();
	       // Starts by looking for the entry tag
	       if (name.equals("item")) {
	    	   m_arrArticles.add(readItem(parser));
	       } else {
	           skip(parser);
	       }
        }  
        return m_arrArticles.size();
    }

    private Article readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        Article article = new Article();
        imgSrcList.clear();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                article.setTitle(readTitle(parser));
            } else if (name.equals("pubDate")) {
                article.setPubdate(readPubDate(parser));
            } else if (name.equals("category")) {
                article.addCategory(readCategory(parser));
            } else if (name.equals("description")) {
                article.setDescription(readDescription(parser));
            } else if (name.equals("content:encoded")) {
                article.setContent(readContent(parser));
            } else if (name.equals("link")) {
                article.setLink(readLink(parser));
            } else {
                skip(parser);
            }
        }
        article.setImgsrc(imgSrcList);
        
        return article;
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }
  
    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "description");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return summary;
    }

    // Processes content tags in the feed.
    private String readContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "content:encoded");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "content:encoded");
        return summary;
    }

    // Processes category tags in the feed.
    private String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "category");
        String category = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "category");
        return category;
    }

    // Processes pubdate tags in the feed.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String pubdate = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pubDate");
        return pubdate;
    }
    
    private void readResult(char[] ch) {
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < ch.length; i++) {
            
            if((ch.length - i) > 6 && ch[i] == 'h' && ch[i+1] == 't' && ch[i + 2] == 't' && 
                    ch[i + 3] == 'p' && ch[i + 4] == ':' && ch[i + 5] == '/' && 
                    ch[i + 6] == '/') {
                sb = new StringBuilder();
                
                for(int j = i; j < ch.length; j++) {
                    if(ch[j] == '.' && ch[j + 1] == 'p' && ch[j + 2] == 'n' && ch[j + 3] == 'g') {
                        sb.append(".png");
                        imgSrcList.add(sb.toString());
                        
                        i = j + 4;
                        break;
                    }
                    else if(ch[j] == '>'){
                        break;
                    }
                    else {
                        sb.append(ch[j]);
                    }
                }
            }
        }
    }
    
    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            readResult(result.toCharArray());
            parser.nextTag();
        }
        
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }

}
