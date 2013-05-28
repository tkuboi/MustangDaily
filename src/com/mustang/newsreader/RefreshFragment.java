package com.mustang.newsreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;

public class RefreshFragment extends Fragment {
    private CheckBox autoCheckBox;
    private Spinner intervalSpinner;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refresh_view, container, false);
        
        //intervalSpinner = (Spinner)getView().findViewById(R.id.intervalSpinner);
        
        return view;
    }
    
	@Override
	public void onResume() {
		super.onResume();
		autoCheckBox = (CheckBox)getView().findViewById(R.id.autoRefreshCheckbox);
	}
    
    public boolean isAutoRefresh() {
        return autoCheckBox.isChecked();
    }
    
    /*public int getInterval() {
        return intervalSpinner.getSelectedItemPosition();
    }*/
}
