package com.mustang.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.mustang.newsreader.ArticleListFragment.OnArticleSelectedListener;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends FragmentActivity 
                       implements OnArticleSelectedListener {

	private static String feedurl = "http://mustangdaily.net/feed/";
	private static String listTag = "TAG_LIST";
	private static String itemTag = "TAG_ITEM";
	private ArrayList<Article> m_arrItems;
	private ArticleListFragment m_listFragment;
	private ArticleFragment m_articleFragment;
	private DataHandler m_dataHandler;
	private String m_activeFragTag; //tag of active fragment when the orientation changed
	private int m_articlePosition;  //postition of article viewed when the orientation changed

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//this.m_arrItems = new ArrayList<Article>();
		m_dataHandler = DataHandler.getInstance();
		this.m_arrItems = m_dataHandler.getArticles();
	    FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		m_listFragment = new ArticleListFragment();
		Log.d("onCreate", "after new ArticleListFragment");
		//replace fragment instead of adding it for handling orientation change
		fragmentTransaction.replace(R.id.fragment_container, m_listFragment, listTag);
		fragmentTransaction.addToBackStack(listTag);
		fragmentTransaction.commit();
		if (savedInstanceState == null || this.m_arrItems.size() == 0) //download xml only in fresh-start
		    xmlHandler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		m_dataHandler.setArticles(m_arrItems);
		String tag = getActiveFragment();
		if (tag != null && !tag.contentEquals(listTag)) {
			outState.putString("FRAGMENT_TAG", tag);
			outState.putInt("ARTICLE_POSITION", m_articlePosition);
		}
		
		super.onSaveInstanceState(outState);
		Log.d("onSaveInstanceState","onSaveInstanceState");
	}

	@Override
	public void onRestoreInstanceState(Bundle inState){
		super.onRestoreInstanceState(inState);
		m_dataHandler = DataHandler.getInstance();
		m_arrItems = m_dataHandler.getArticles();
		String tag = inState.getString("FRAGMENT_TAG");
		if (tag != null) {
			this.m_articlePosition = inState.getInt("ARTICLE_POSITION");
			this.m_activeFragTag = tag;
			openArticleFragment(this.m_articlePosition);
		}
		Log.d("onRestoreInstanceState","onRestoreInstanceState");
	}
	
    // When user strat the app, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void xmlHandler() {
        // Gets the URL from the UI's text field.
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadXmlTask().execute(feedurl);
        } else {
            //textView.setText("No network connection available.");
        }
    }

     // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
     // URL string and uses it to create an HttpUrlConnection. Once the connection
     // has been established, the AsyncTask downloads the contents of the webpage as
     // an InputStream. Finally, the InputStream is converted into a string, which is
     // displayed in the UI by the AsyncTask's onPostExecute method.
     private class DownloadXmlTask extends AsyncTask<String, Void, String> {
    	//private ArrayList<Article> list;
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
            	m_arrItems = (ArrayList<Article>) downloadXml(feedurl);
            	if (m_arrItems.size() > 0){
                    return "success";
            	}
            	return "empty";
            } catch (IOException e) {
                return "failed";
            }
        }
        
        @Override
        protected void onPostExecute(String result) {
        	if (result.equalsIgnoreCase("success")){
        		Log.d("parser",result);
        		//m_listFragment.notifyDataChanged();
        		int count = 0;
        		for(Article a : m_arrItems){
            		count = m_listFragment.addArticle(a);
        		}
        		Log.d("addItems",count + " added.");
        	}
        	else
        		Log.d("parser",result);
        }
        
    }
    
    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private List<Article> downloadXml(String myurl) throws IOException {
       InputStream is = null;
          
       try {
           URL url = new URL(myurl);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setReadTimeout(10000 /* milliseconds */);
	       conn.setConnectTimeout(15000 /* milliseconds */);
	       conn.setRequestMethod("GET");
	       conn.setDoInput(true);
	       // Starts the query
	       conn.connect();
	       int response = conn.getResponseCode();
	       Log.d("DEBUG", "The response is: " + response);
	       is = conn.getInputStream();
	
	       NewsFeedXmlParser parser = new NewsFeedXmlParser();
	       ArrayList<Article> items = parser.parse(is);
	       return items;
      
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } catch (XmlPullParserException e) {
		   e.printStackTrace();
	    } finally {
           if (is != null) {
              is.close();
           } 
        }
        return null;
    }

	@Override
	public void onArticleSelected(int pos) {
		openArticleFragment(pos);
	}     

	public void openArticleFragment(int pos) {
		this.m_articlePosition = pos;
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		m_articleFragment = new ArticleFragment();
		
		fragmentTransaction.replace(R.id.fragment_container, m_articleFragment, itemTag);
		fragmentTransaction.addToBackStack(itemTag);
		fragmentTransaction.commit();
		m_articleFragment.setContent(this.m_arrItems.get(pos).getContent());		
	}
	
	public void resetList() {
		int count = 0;
		for(Article a : m_arrItems){
			count = m_listFragment.addArticle(a);
		}
		Log.d("resetList",count + " added.");
	}
	
	public String getActiveFragment() {
		int count = getSupportFragmentManager().getBackStackEntryCount();
		if (count == 0) {
			return null;
		}
		String tag = getSupportFragmentManager().getBackStackEntryAt(count - 1).getName();
		return tag;
	}
}
