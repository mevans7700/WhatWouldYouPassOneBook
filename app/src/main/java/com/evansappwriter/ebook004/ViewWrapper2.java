package com.evansappwriter.ebook004;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewWrapper2 {
	View base;
	TextView item=null;
	TextView item2=null;
	Button item3=null;
	
	ViewWrapper2(View base) {
		this.base=base;
	}
	
	public TextView getItem() {
		if (item==null) {
			item=(TextView)base.findViewById(R.id.filler);
		}
		return item;
	}
	
	public TextView getItem2() {
		if (item2==null) {
			item2=(TextView)base.findViewById(R.id.titleItem);
		}
		return item2;
	}
	
	public Button getItem3() {
		if (item3==null) {
			item3=(Button)base.findViewById(R.id.gotoBtn);
		}
		return item3;
	}

}
