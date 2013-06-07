package com.mustang.newsreader;

import android.content.Context;
import android.util.TypedValue;
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
    private LinearLayout m_lLayout;

	public ItemView(Context context) {
		super(context);
		this.context = context;
	}
	
	public ItemView(Context context, Article item) {
		super(context);
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.item_view, this, true);
	    this.m_lLayout = (LinearLayout) findViewById(R.id.parent);
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
	
	public void setStyle(int text, int back, int size, boolean show) {
		if(!show)
			this.m_vwDescText.setVisibility(GONE);
		this.m_vwTitleText.setTextColor(text);
		this.m_vwTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		this.m_lLayout.setBackgroundColor(back);
	}
	
	public Article getArticle() {
		return this.m_article;
	}
}
