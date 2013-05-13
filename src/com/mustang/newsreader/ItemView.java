package com.mustang.newsreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView extends LinearLayout {
    private Context context;
    private Article m_article;
    private WebView m_vwImage;
    private TextView m_vwTitleText;
    private TextView m_vwDescText;

	public ItemView(Context context) {
		super(context);
		this.context = context;
	}
	
	public ItemView(Context context, Article item) {
		super(context);
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.item_view, this, true);
        this.m_vwTitleText = (TextView) findViewById(R.id.titleView);
        this.m_vwDescText = (TextView) findViewById(R.id.descView);
	    setItem(item);
	}
	
	public void setItem(Article item) {
		m_article = item;
		
        if(m_article.getImgsrc() != null && m_article.getImgsrc().size() > 0) {
            String imgSrc = m_article.getImgsrcAt(0);
            m_vwImage = new WebView(context);
            this.addView(m_vwImage);
            this.m_vwImage.loadUrl(imgSrc);
        }
		
		this.m_vwTitleText.setText(m_article.getTitle());
        this.m_vwDescText.setText(m_article.getDescription());

		this.requestLayout();
	}
}
