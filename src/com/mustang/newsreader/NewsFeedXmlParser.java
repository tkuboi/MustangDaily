package com.mustang.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class NewsFeedXmlParser {
    // We don't use namespaces
    private static final String ns = null;
    private ArrayList<Article> m_arrArticles;
   
    public ArrayList<Article> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
        	Log.d("dedug","in parse");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            int count = readFeed(parser);
            Log.d("dedug","number of articles = " + count);
            return m_arrArticles;
        } finally {
            in.close();
        }
    }

    private int readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    	int readCount = 0;
        Log.d("debug","readFeed");
        if(parser == null)
        	Log.d("debug", "parser is null");
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        Log.d("readFeed","before while");
        while (parser.next() != XmlPullParser.END_TAG) {
	       if (parser.getEventType() != XmlPullParser.START_TAG) {
	    	   Log.d("readFeed","continue");
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
    	m_arrArticles = new ArrayList<Article>();
        Log.d("debug","readFeed");
        if(parser == null)
        	Log.d("debug", "parser is null");
        parser.require(XmlPullParser.START_TAG, null, "channel");
        Log.d("readFeed","before while");
        while (parser.next() != XmlPullParser.END_TAG) {
	       if (parser.getEventType() != XmlPullParser.START_TAG) {
	    	   Log.d("readFeed","continue");
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
        String title = null;
        String description = null;
        String link = null;
        String pubDate = null;
        String content = null;
        ArrayList<String> category = new ArrayList<String>();
        Log.d("readItem","hi!");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("readItem",name);
            if (name.equals("title")) {
                title = readTitle(parser);
                Log.d("readItem",title);
            } else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            } else if (name.equals("category")) {
                category.add(readCategory(parser));
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("content:encoded")) {
                content = readContent(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
                Log.d("readItem",link);
            } else {
                skip(parser);
            }
        }
        return new Article(title, description, content, pubDate, category);
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

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
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
