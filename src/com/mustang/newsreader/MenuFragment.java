package com.mustang.newsreader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MenuFragment extends Fragment implements TabHost.TabContentFactory {
	private TabHost m_tabHost;
	private LinearLayout m_tabsFrame;
	private FrameLayout m_contentFrame;
	private ArrayList<Article> m_arrItems;
	private ArrayList<Article> m_filteredItems;
	protected ItemListAdapter m_adapter;
	protected ListView m_vwListView;
	protected String[] m_categories;
	protected OnArticleSelectedListener m_listener;
	private static final int m_textSize = 18;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("onCreateView","hi!");
		View view = inflater.inflate(R.layout.tab_menu, container, false);
		m_tabHost = (TabHost)view.findViewById(android.R.id.tabhost);
		m_tabHost.setup();
		m_tabsFrame = (LinearLayout) view.findViewById(R.id.tabs);
		m_contentFrame = (FrameLayout) view.findViewById(android.R.id.tabcontent);
		TabSpec spec;
		Resources res = this.getResources();
		m_categories = res.getStringArray(R.array.categories);
		int i = 0;
		for(String tab : m_categories){
			spec = m_tabHost
				  .newTabSpec(tab)
				  .setIndicator(tab)
				  .setContent(this);
			m_tabHost.addTab(spec);
			Button button = new Button(getActivity());
			button.setText(tab);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, m_textSize);
			button.setId(1000 + i);
			button.setSelected(false);
			button.setBackgroundColor(getTabColor(tab));
			button.setTextColor(Color.WHITE);
			
			/*LayoutParams layoutParams = button.getLayoutParams();
			layoutParams.width = LayoutParams.MATCH_PARENT;*/
			button.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1.0f));
			button.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	v.setSelected(true);
	            	int id = v.getId();
	            	switch(id){
	            	case 1000 :
	            		m_tabHost.setCurrentTab(0);
	            		break;
	            	case 1001 :
	            		m_tabHost.setCurrentTab(1);
	            		break;
	            	case 1002 :
	            		m_tabHost.setCurrentTab(2);
	            		break;
	            	case 1003 :
	            		m_tabHost.setCurrentTab(3);
	            		break;
	            	}
	            }
			});
			m_tabsFrame.addView(button);
			i++;
		}
		return view;
	}
	
	@Override
	public View createTabContent(String tag) {
		if (this.m_arrItems == null) {
		   DataHandler handler = DataHandler.getInstance();
		   this.m_arrItems = handler.getArticles();
		}
		this.m_filteredItems = new ArrayList<Article>();
		m_vwListView = new ListView(this.getActivity());
		this.m_adapter = new ItemListAdapter(this.getActivity(), this.m_filteredItems, false);
		int bColor = getTabColor(tag);
		this.m_adapter.setStyle(Color.WHITE, bColor, m_textSize);
		m_vwListView.setAdapter(this.m_adapter);
		m_vwListView.setBackgroundColor(bColor);
		filterArticles(tag);
		//Toast.makeText(this.getActivity(), Integer.toString(m_filteredItems.size()), Toast.LENGTH_LONG).show();
		m_vwListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				//Toast.makeText(getActivity(), Integer.toString(pos) + " selected.", Toast.LENGTH_LONG).show();
				m_listener.onArticleSelected(translate(pos));
			}
			
			private int translate(int pos) {
				int result = 0;
				if (pos >= m_filteredItems.size())
					pos = m_filteredItems.size() - 1;
				if (pos < 0)
					pos = 0;
				Article item = m_filteredItems.get(pos);
				result = m_arrItems.indexOf(item);
				return result;
			}
		});
	    return m_vwListView;
	}

	public int setArticles(ArrayList<Article> articles) {
		this.m_arrItems = articles;
		Log.d("setArticles", "set");
		Log.d("setArticles", Integer.toString(this.m_arrItems.size()));
		return this.m_arrItems.size();
	}
	
	public void filterArticles(String tag) {
		Log.d(tag, "filter before");
		this.m_filteredItems.clear();
		m_adapter.notifyDataSetChanged();
		for(Article article : this.m_arrItems) {
			Log.d(tag, "in loop");
			if (article.getCategories() != null &&
					(article.getCategories().get(0).equalsIgnoreCase(tag) ||
				   (article.getCategories().size() > 1 &&
					article.getCategories().get(1).equalsIgnoreCase(tag)))) {
				Log.d(tag, article.getCategories().get(0));
				this.m_filteredItems.add(article);
				m_adapter.notifyDataSetChanged();
			}
		}
		Log.d(tag, "filter after");
	}
	
	public int getTabColor(String tag) {
		int color = 0;
		for(int i = 0; i < m_categories.length; i++) {
			if (m_categories[i] == tag) {
				color = Color.rgb(i*50, 200 + i*10, 100 + i*50);
			}
		}
		return color;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			m_listener = (OnArticleSelectedListener) activity;
		} catch (ClassCastException e) {
			Log.d("EXCEPTION",e.toString());
		}
	}

	/*@Override
	public void onResume() {
		Log.d("OnResume","hi!");
		super.onResume();
		if (this.m_arrItems == null) {
			this.m_arrItems = new ArrayList<Article>();
		}
		
	}*/

}
