package com.mustang.newsreader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ItemListAdapter extends BaseAdapter {

	private Context m_context;
	private List<Article> m_itemList;
	
	public ItemListAdapter(Context context, ArrayList<Article> list) {
		m_context = context;
		m_itemList = list;
	}
	
	@Override
	public int getCount() {
		return this.m_itemList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.m_itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemView view = null;
		if (convertView == null) {
			Log.d("getView", "if " + Integer.toString(position));
			view = new ItemView(m_context, m_itemList.get(position));
		}
		else {
			Log.d("getView", "else " + Integer.toString(m_itemList.size()));
			view = (ItemView)convertView;
		}
		view.setItem(m_itemList.get(position));
		return view;
	}

}
