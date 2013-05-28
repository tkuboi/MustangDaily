package com.mustang.newsreader;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ArticleListFragment extends ListFragment {
	private ArrayList<Article> m_arrItems;
	protected ItemListAdapter m_adapter;
	protected ListView m_vwItemLayout;
	protected OnArticleSelectedListener m_listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("ListFragment","onCreateView");
		View view = inflater.inflate(R.layout.list_view, container, false);
		DataHandler dataHandler = DataHandler.getInstance();
		this.m_arrItems = dataHandler.getArticles();
		this.m_adapter = new ItemListAdapter(container.getContext(), m_arrItems);
		setListAdapter(this.m_adapter);
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id){
		m_listener.onArticleSelected(pos);
		//Toast.makeText(v.getContext(), m_arrItems.get(pos).getContent(), Toast.LENGTH_LONG).show();
	}
	
	public void setArticles(ArrayList<Article> articles) {
		this.m_arrItems = articles;
	}
	
	/*public int addArticle(Article article) {
		this.m_arrItems.add(article);
		m_adapter.notifyDataSetChanged();
		return this.m_arrItems.size();
	}*/
	
	public void notifyDataChanged() {
		m_adapter.notifyDataSetChanged();
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
		if (this.m_arrItems != null) {
	        Log.d("OnResume",Integer.toString(this.m_arrItems.size()));
	        if (this.m_arrItems.size() == 0)
	        	((MainActivity)getActivity()).resetList();
		}
	}
	
	public void clearData() {
		this.m_arrItems.clear();
		m_adapter.notifyDataSetChanged();
	}*/
}
