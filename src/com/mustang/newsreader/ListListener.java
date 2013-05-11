package com.mustang.newsreader;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListListener implements OnItemClickListener {

	List<Article> listItems;
	Activity activity;
	
	public ListListener(List<Article> listItems, Activity activity) {
		this.listItems = listItems;
		this.activity = activity;
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		//Intent i = new Intent(Intent.ACTION_VIEW);
		//i.setData(Uri.parse(listItems.get(pos).getLink()));
		//activity.startActivity(i);
		//Intent intent = new Intent(view.getContext(), PageActivity.class);
		//intent.putExtra("article",listItems.get(pos).getArticle());
		//view.getContext().startActivity(intent);
		Toast.makeText(view.getContext(), listItems.get(pos).getContent(), Toast.LENGTH_LONG).show();
	}
}
