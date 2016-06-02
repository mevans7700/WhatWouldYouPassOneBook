package com.evansappwriter.ebook004;

import android.os.Bundle;
import android.widget.TextView;

public class EBookInstructionsActivity extends EBookActivity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions);
						
		TextView instructTextView = (TextView) findViewById(R.id.instructions);
		instructTextView.setText(String.format(getResources().getString(R.string.instructions_text), 
        		getResources().getString(R.string.app_name)));
		
		setTitle(getResources().getString(R.string.instructions_title));
	}
}