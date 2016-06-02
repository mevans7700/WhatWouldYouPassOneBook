package com.evansappwriter.ebook004;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EBookWorkbookActivity extends ListActivity {
	private static final int ACTIVITY_EDIT = 1;
	int mChapter = 0;
    
    WorkbookDbAdpater mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workbook);
        String title = "All Workbook Lessons";
        
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			title = "Workbook Lessons for " + extras.getString(EBookActivity.CHAPTER_DESC);
			mChapter = extras.getInt(EBookActivity.WORKBOOK_CHAPTER);
		}
		setTitle(title);
        mDbHelper = new WorkbookDbAdpater(this);
        mDbHelper.open();
        
        if (WorkbookDbAdpater.mCreated) {
        	try {
            	getQuestions();
            } catch (Exception e) {
                //Log.e(EBookActivity.DEBUG_TAG, "Failed to load questions", e);
            }
        }
                
        filldata();               
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	private void filldata() {
		Cursor workbookCursor;
		if (mChapter == 0) {
			workbookCursor = mDbHelper.fetchAllEntries();
		} else {
			workbookCursor = mDbHelper.fetchChapterEntries(mChapter);
		}
    	
        startManagingCursor(workbookCursor);    
        
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{WorkbookDbAdpater.KEY_QUESTION, WorkbookDbAdpater.KEY_ANSWER};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.question, R.id.answer};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter adapt = 
        	    new SimpleCursorAdapter(this, R.layout.question_row, workbookCursor, from, to);
        setListAdapter(adapt);     	
    }
       
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, WorkbookEdit.class);
        i.putExtra(WorkbookDbAdpater.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        filldata();        
    }
    
    /**
     * Churn through an XML score information and insert into the entries table
     * 
     * @param chapter
     *            
     * @param labels
     *            
     * @throws XmlPullParserException
     *             Thrown on XML errors
     * @throws IOException
     *             Thrown on IO errors reading the XML
     */
    private void getQuestions() throws XmlPullParserException,
            IOException {
    	XmlResourceParser workbook = getResources().getXml(R.xml.workbook_questions);
        int eventType = -1;
        
        // Find Score records from XML
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                // Get the name of the tag (eg scores or score)
                String strName = workbook.getName();
                if (strName.equals(EBookActivity.EBOOK_XML_TAG_QUESTION)) {
                    mDbHelper.createEntry(Integer.parseInt(workbook.getAttributeValue(null, EBookActivity.EBOOK_XML_TAG_CHAPTER)),
                    		workbook.getAttributeValue(null, EBookActivity.EBOOK_XML_TAG_LABEL), " ");
                }
            }
            eventType = workbook.next();
        }        
    }
}