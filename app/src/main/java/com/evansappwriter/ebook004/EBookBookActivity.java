package com.evansappwriter.ebook004;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class EBookBookActivity extends EBookActivity implements OnClickListener {
	SharedPreferences mEbookSettings;
	ArrayList<HashMap<String,String>> mChapterList;
	int mChapter = 0;
    
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        int fileNum;
        mChapterList = new ArrayList<HashMap<String,String>>();
        
        Bundle extras = getIntent().getExtras();
		mChapterList = (ArrayList<HashMap<String,String>>) extras.getSerializable(CHAPTER_LIST);
		mChapter = extras.getInt(CURRENT_CHAPTER);
		
		// Set title bar with the Chapter
		setTitle(mChapterList.get(mChapter).get(FIRST_LINE));
		
		String fileNumStr = mChapterList.get(mChapter).get(THIRD_LINE);
		fileNum = Integer.parseInt(fileNumStr,16);		
		
		// Get Handle on the Chapter Title and populate
		TextView chpTitle = (TextView)findViewById(R.id.chapterTitle);
		chpTitle.setText(mChapterList.get(mChapter).get(SECOND_LINE));
		
		// Display Workbook Button only if it has questions
		View workbookButton = findViewById(R.id.workbook_button);
		if (mChapter < WORKBOOK_START_CHAPTER || mChapter > WORKBOOK_END_CHAPTER) {
			workbookButton.setVisibility(View.GONE);
		} else {
			workbookButton.setOnClickListener(this);
		}        
		
		// Get Handles on the back either disable or set listener depending on current chapter
		View backButton = findViewById(R.id.backButton);
		if (mChapter == FIRST_CHAPTER) {
			backButton.setEnabled(false);
			backButton.getBackground().setAlpha(125);
		} else {
			backButton.setOnClickListener(this);
			backButton.getBackground().setAlpha(255);
		}		
		// Get Handles on the next either disable or set listener depending on current chapter
		View nextButton = findViewById(R.id.nextButton);
		if (mChapter == LAST_CHAPTER) {
			nextButton.setEnabled(false);
			nextButton.getBackground().setAlpha(125);
		} else {
			nextButton.setOnClickListener(this);
			nextButton.getBackground().setAlpha(255);
		}		
				
        InputStream iFile = getResources().openRawResource(fileNum);
        try {
        	TextView helpText = (TextView)findViewById(R.id.TextView_BookText);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        } catch (Exception e) {
        	Log.e(EBookActivity.DEBUG_TAG, "InputStreamToString failure", e);
        }
        
        // Set the current chapter in the shared preferences
        mEbookSettings = getSharedPreferences(EBOOK_PREFERENCES, Context.MODE_PRIVATE);       
        Editor editor = mEbookSettings.edit();
        editor.putInt(EBOOK_PREFERENCES_CHAPTER, mChapter);
        editor.commit();
    }
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.workbook_button:		
    		Intent i = new Intent(this, EBookWorkbookActivity.class);
    		i.putExtra(CHAPTER_DESC, mChapterList.get(mChapter).get(FIRST_LINE));
    		i.putExtra(WORKBOOK_CHAPTER, mChapter);
    		startActivity(i); 
    		break;
		case R.id.backButton:
			goToChapter(mChapter-1);
    		break;   	
		case R.id.nextButton:
			goToChapter(mChapter+1);
    		break;
		}
		
	}
	
	private void goToChapter(int chapter) {
    	Intent i = new Intent(this, EBookBookActivity.class);
    	i.putExtra(EBookActivity.CHAPTER_LIST,mChapterList);
    	i.putExtra(EBookActivity.CURRENT_CHAPTER, chapter);	
		startActivity(i); 
		this.finish();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		getMenuInflater().inflate(R.menu.bookoptions, menu);
		menu.findItem(R.id.onlinecontent_menu_item).setIntent(new Intent(this, EBookOnlineContentActivity.class));
		menu.findItem(R.id.workbook_menu_item).setIntent(new Intent(this, EBookWorkbookActivity.class));
		menu.findItem(R.id.instructions_menu_item).setIntent(new Intent(this, EBookInstructionsActivity.class));
		menu.findItem(R.id.about_menu_item).setIntent(new Intent(this, EBookAboutAuthorActivity.class));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.onlinecontent_menu_item:
		case R.id.workbook_menu_item:
		case R.id.instructions_menu_item:
		case R.id.about_menu_item:
			startActivity(item.getIntent());
			break;
		case R.id.share_menu_item:
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("message/rfc822");
    	    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    	    break;
		}
		return true;
	}
    
    /**
     * Converts an input stream to a string
     * 
     * @param is
     *            The {@code InputStream} object to read from
     * @return A {@code String} object representing the string for of the input
     * @throws IOException
     *             Thrown on read failure from the input
     */
    
    public String inputStreamToString(InputStream is) throws IOException {
    	StringBuffer sBuffer = new StringBuffer();
    	DataInputStream dataIO = new DataInputStream(is);
    	String strLine = null;
    	while ((strLine = dataIO.readLine()) != null) {
    		sBuffer.append(strLine + "\n");
    	}
    	dataIO.close();
    	is.close();
    	return (sBuffer.toString());
    }
	
}