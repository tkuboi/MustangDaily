package com.mustang.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity 
                       implements OnArticleSelectedListener {

	private static final String feedurl = "http://mustangdaily.net/feed/";
	private static final String listTag = "TAG_LIST";
	private static final String itemTag = "TAG_ITEM";
	private static final String menuTag = "TAG_MENU";
	private static final String SAVED_UPDSETTING = "AUTOUPDATE";
	private ArrayList<Article> m_arrItems;
	private ArrayList<Article> m_tempItems;
	private ArticleListFragment m_listFragment;
	private ArticleFragment m_articleFragment;
	private MenuFragment m_menuFragment;
	private DataHandler m_dataHandler;
	private String m_activeFragTag; //tag of active fragment when the orientation changed
	private int m_articlePosition;  //postition of article viewed when the orientation changed
	private boolean m_autoUpdate;
	private ImageView m_headerImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.m_autoUpdate = true;
		m_dataHandler = DataHandler.getInstance();
		this.m_arrItems = m_dataHandler.getArticles();
		openArticleListFragment();
		//download xml only in fresh-start
		if (savedInstanceState == null || this.m_arrItems.size() == 0 || this.m_autoUpdate)
		    xmlHandler();
	}
	
	/*@Override
	public void onStart(){
		super.onStart();
		if (this.m_autoUpdate || this.m_arrItems.size() == 0)
			xmlHandler();
		//openArticleListFragment();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_home :
			openArticleListFragment();
			break;
		case R.id.menu_menu :
			openMenuFragment();
			break;
		case R.id.menu_refresh :
			xmlHandler();
			break;
		case R.id.menu_settings :
			if (this.m_autoUpdate)
				this.m_autoUpdate = false;
			else
				this.m_autoUpdate = true;
			break;
		}
		return true;
	}
	@Override
	public void onSaveInstanceState(Bundle outState){
		//m_dataHandler.setArticles(m_arrItems);
		String tag = getActiveFragment();
		//if (tag != null && !tag.contentEquals(listTag)) {
		if (tag != null) {
			outState.putString("FRAGMENT_TAG", tag);
			outState.putInt("ARTICLE_POSITION", m_articlePosition);
		}
		else {
			outState.putString("FRAGMENT_TAG", listTag);
			outState.putInt("ARTICLE_POSITION", 0);			
		}
		
		outState.putBoolean(SAVED_UPDSETTING, this.m_autoUpdate);
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
			if (this.m_activeFragTag == itemTag)
				openArticleFragment(this.m_articlePosition);
			if (this.m_activeFragTag == menuTag)
				openMenuFragment();
		}
		this.m_autoUpdate = inState.getBoolean(SAVED_UPDSETTING, true);
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
        	Toast.makeText(getApplicationContext(), "No network connection available.", Toast.LENGTH_LONG).show();
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
            	m_tempItems = (ArrayList<Article>) downloadXml(feedurl);
            	if (m_tempItems.size() > 0){
                    return "success";
            	}
            	return "empty";
            } catch (IOException e) {
            	Log.e("IOException", e.getMessage());
                return "failed : " + e.getMessage();
            }
            
        }
        
        @Override
        protected void onPostExecute(String result) {
        	if (result.equalsIgnoreCase("success")){
        		Log.d("parser",result);
        		m_arrItems.clear();
        		int count = 0;
        		for(Article a : m_tempItems){
        			m_arrItems.add(a);
            		count++;
            		m_listFragment.notifyDataChanged();
        		}
        		
        		Log.d("addItems",count + " added.");
        	}
        	else {
        		Toast.makeText(getApplicationContext(), "The server not available.", Toast.LENGTH_LONG).show();
        		Log.e("parser",result);
        	}
        }
        
    }
    
    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private List<Article> downloadXml(String myurl) throws IOException {
       InputStream is = null;
       ArrayList<Article> items = new ArrayList<Article>();
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
	       items = parser.parse(is);
      
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } catch (XmlPullParserException e) {
        	Log.e("XmlPullParserException",e.getMessage());
		   e.printStackTrace();
	    } finally {
           if (is != null) {
              is.close();
           } 
        }
        return items;
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
	
	/*public void resetList() {
		int count = 0;
		for(Article a : m_arrItems){
			count = m_listFragment.addArticle(a);
		}
		Log.d("resetList",count + " added.");
	}*/
	
	public String getActiveFragment() {
		int count = getSupportFragmentManager().getBackStackEntryCount();
		if (count == 0) {
			return null;
		}
		String tag = getSupportFragmentManager().getBackStackEntryAt(count - 1).getName();
		return tag;
	}

	public void openArticleListFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		m_listFragment = new ArticleListFragment();
		Log.d("onCreate", "after new ArticleListFragment");
		//replace fragment instead of adding it for handling orientation change
		fragmentTransaction.replace(R.id.fragment_container, m_listFragment, listTag);
		fragmentTransaction.addToBackStack(listTag);
		fragmentTransaction.commit();
		this.m_activeFragTag = listTag;
		this.m_articlePosition = 0;
	}
	
	public void openMenuFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		m_menuFragment = new MenuFragment();
		//m_menuFragment.setArticles(this.m_arrItems);
		fragmentTransaction.replace(R.id.fragment_container, m_menuFragment, menuTag);
		fragmentTransaction.addToBackStack(menuTag);
		fragmentTransaction.commit();
		int size = m_menuFragment.setArticles(this.m_arrItems);
		//Toast.makeText(this, Integer.toString(size), Toast.LENGTH_LONG).show();
	}
}
