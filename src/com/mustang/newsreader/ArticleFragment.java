package com.mustang.newsreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleFragment extends Fragment {
    
	private WebView m_articleView;
	private String m_content;
	private String style = "<style type='text/css'>" +
            "img {height:250px; width:250px}" +
            ".toggle-head-open,.toggle-head-close {display:none;}" +
            "iframe {height:250px; width:250px}" +
            "</style>";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("onCreateView","hi!");
		View view = inflater.inflate(R.layout.article_view, container, false);
		//m_articleView = new WebView(container.getContext());
		//setContentView(m_articleView);
		//m_articleView = (WebView) getView().findViewById(R.id.webView);
		//m_textView = (TextView) getView().findViewById(R.id.textView);
		//Log.d("onCreateView",m_articleView.toString());
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		m_articleView = (WebView) getView().findViewById(R.id.webView);
		m_articleView.setWebChromeClient(new WebChromeClient());
		m_articleView.setWebViewClient(new WebViewClient());
        m_articleView.getSettings().setJavaScriptEnabled(true);
        m_articleView.getSettings().setPluginState(PluginState.ON_DEMAND);
		m_articleView.loadDataWithBaseURL(null, style + m_content, "text/html", "UTF-8", null);
	}
	
	public void setContent(String content) {
		m_content = content;
	}
}
