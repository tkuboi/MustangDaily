package com.mustang.newsreader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView extends LinearLayout {
	private Article m_article;
	private TextView m_vwItemText;
	private TextView m_vwSumText;
	//private WebView m_vwImage;

	public ItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ItemView(Context context, Article item) {
		super(context);
		Log.d("ItemView","constructor");
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.item_view, this, true);
	    this.m_vwItemText = (TextView) findViewById(R.id.titleView);
	    this.m_vwSumText = (TextView) findViewById(R.id.descView);
	    //this.m_vwImage = (WebView) findViewById(R.id.imageView);
	    setItem(item);
	}
	
	public void setItem(Article item) {
		m_article = item;
	    this.m_vwItemText.setText(m_article.getTitle());
	    this.m_vwSumText.setText(m_article.getDescription());
	    //this.m_vwImage.loadUrl(m_article.getImgsrcAt(0));

		this.requestLayout();
	}
}
