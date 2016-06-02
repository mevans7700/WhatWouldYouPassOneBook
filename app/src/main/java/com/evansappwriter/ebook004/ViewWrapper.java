package com.evansappwriter.ebook004;

import android.view.View;
import android.widget.TextView;

public class ViewWrapper {
	View base;
	TextView item=null;
	TextView item2=null;
		
	ViewWrapper(View base) {
		this.base=base;
	}
	
	public TextView getItem() {
		if (item==null) {
			item=(TextView)base.findViewById(R.id.Chapter);
		}
		return item;
	}
	
	public TextView getItem2() {
		if (item2==null) {
			item2=(TextView)base.findViewById(R.id.Chapter_Name);
		}
		return item2;
	}
}
