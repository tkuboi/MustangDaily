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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ArticleFragment extends Fragment {
    
    private MainActivity activityContent;
	private WebView m_articleView;
	private String m_content;
	private String style = "<style type='text/css'>" +
            "img {height:250px; width:250px}" +
            ".toggle-head-open,.toggle-head-close {display:none;}" +
            "iframe {height:250px; width:250px}" +
            "</style>";
	
	//Used to display webview
    private View mCustomView;
    private LinearLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private FrameLayout.LayoutParams LayoutParameters = 
            new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);
	
	public void setActivityContent(MainActivity content) {
	    activityContent = content;
	}
	
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
		
		m_articleView.setWebChromeClient(new WebChromeClient() {		    
		    @Override
		    public void onShowCustomView(View view, CustomViewCallback callback) {
		        if (mCustomView != null) {
		            callback.onCustomViewHidden();
		            return;
		        }
		        mContentView = (LinearLayout)activityContent.findViewById(R.id.parentFrame);
		        mContentView.setVisibility(View.GONE);
		        mCustomViewContainer = new FrameLayout(activityContent);
		        mCustomViewContainer.setLayoutParams(LayoutParameters);
		        mCustomViewContainer.setBackgroundResource(android.R.color.black);
		        view.setLayoutParams(LayoutParameters);
		        mCustomViewContainer.addView(view);
		        mCustomView = view;
		        mCustomViewCallback = callback;
		        mCustomViewContainer.setVisibility(View.VISIBLE);
		        activityContent.setContentView(mCustomViewContainer);
		    }
		    
		    @Override
		    public void onHideCustomView() {
		        hideCustomView();
		    }
		});
		
		m_articleView.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        return true;
		    }
		});
		
        m_articleView.getSettings().setJavaScriptEnabled(true);
        m_articleView.getSettings().setPluginState(PluginState.ON_DEMAND);
		m_articleView.loadDataWithBaseURL(null, style + m_content, "text/html", "UTF-8", null);
	}
	
	public FrameLayout getCustomViewContainer() {
	    return mCustomViewContainer;
	}
	
	public WebView getWebView() {
	    return m_articleView;
	}
	
	public void hideCustomView() {
	    if (mCustomView == null) {
            return;
        } else {
            // Hide the custom view.  
            mCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.  
            mCustomViewContainer.removeView(mCustomView);
            mCustomView = null;
            mCustomViewContainer.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();
            // Show the content view.  
            mContentView.setVisibility(View.VISIBLE);
            activityContent.setContentView(mContentView);
            mCustomViewContainer = null;
        }
	}
	
	public void setContent(String content) {
		m_content = content;
	}
}
