package com.mustang.newsreader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ItemListAdapter extends BaseAdapter {

	private Context m_context;
	private List<Article> m_itemList;
	private int m_backgroundColor;
	private int m_textColor;
	private int m_textSize;
	private boolean m_showDescription;
	
	public ItemListAdapter(Context context, ArrayList<Article> list) {
		m_context = context;
		m_itemList = list;
		m_showDescription = true;
		m_textSize = 26;
		m_backgroundColor = Color.WHITE;
		m_textColor = Color.BLACK;
	}
	
	public ItemListAdapter(Context context, ArrayList<Article> list, boolean show) {
		m_context = context;
		m_itemList = list;
		m_showDescription = show;
		m_textSize = 26;
		m_backgroundColor = Color.WHITE;
		m_textColor = Color.BLACK;
	}

	public void setStyle(int text, int back, int size) {
		m_textColor = text;
		m_backgroundColor = back;
		m_textSize = size;
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
		if (!m_showDescription) {
			view.setStyle(m_textColor, m_backgroundColor, m_textSize, m_showDescription);
		}
		return view;
	}

}
