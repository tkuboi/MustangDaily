package com.mustang.newsreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class RefreshFragment extends Fragment {
    private MainActivity context;
    private CheckBox autoCheckBox;
    private Button applyButton;
    private Button cancelButton;
    
    public void setContext(MainActivity context) {
        this.context = context;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refresh_view, container, false);
        return view;
    }
    
	@Override
	public void onResume() {
		super.onResume();
		autoCheckBox = (CheckBox)getView().findViewById(R.id.autoRefreshCheckbox);
		autoCheckBox.setChecked(context.getAutoUpdate());
		
		applyButton = (Button)getView().findViewById(R.id.applyButton);
		applyButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View view) {
		        context.setAutoUpdate(autoCheckBox.isChecked());
		        context.getSupportFragmentManager().popBackStack();
		    }
		});
		
		cancelButton = (Button)getView().findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                context.getSupportFragmentManager().popBackStack();
            }
        });
	}
}
