package com.mustang.newsreader;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ArticleListFragment extends ListFragment {
	private ArrayList<Article> m_arrItems;
	protected ItemListAdapter m_adapter;
	protected ListView m_vwItemLayout;
	protected OnArticleSelectedListener m_listener;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_view, container, false);
		this.m_arrItems = new ArrayList<Article>();
		this.m_adapter = new ItemListAdapter(container.getContext(), m_arrItems);
		setListAdapter(this.m_adapter);
		//this.m_vwItemLayout = getListView();
		//this.m_vwItemLayout.setAdapter(this.m_adapter);
		/*this.m_vwItemLayout.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				Log.d("onItemClick","clicked");
				Toast.makeText(view.getContext(), m_arrItems.get(pos).getContent(), Toast.LENGTH_LONG).show();
			}
			
		});*/

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id){
		m_listener.onArticleSelected(m_arrItems.get(pos).getContent());
		//Toast.makeText(v.getContext(), m_arrItems.get(pos).getContent(), Toast.LENGTH_LONG).show();
	}
	
	public int addArticle(Article article) {
		this.m_arrItems.add(article);
		m_adapter.notifyDataSetChanged();
		return this.m_arrItems.size();
	}
	
	public interface OnArticleSelectedListener {
		public void onArticleSelected(String content);
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
	
	@Override
	public void onResume() {
		Log.d("OnResume","hi!");
		super.onResume();
		if (this.m_arrItems != null) {
	        Log.d("OnResume",Integer.toString(this.m_arrItems.size()));
	        if (this.m_arrItems.size() == 0)
	        	((MainActivity)getActivity()).resetList();
		}
		
	}
}
