package com.evansappwriter.ebook004;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EBookTableContentActivity extends ListActivity {
	SharedPreferences mEbookSettings;
	ArrayList<HashMap<String,String>> mChapterList;
	private ListAdapter mAdapter;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_content);
        
        TextView header = (TextView)findViewById(R.id.table_content_header);
        header.setText(String.format(getResources().getString(R.string.table_content_header), 
        		getResources().getString(R.string.app_name)));
        
        XmlResourceParser tablecontent = getResources().getXml(R.xml.tableofcontent);
        mChapterList = new ArrayList<HashMap<String,String>>();
    	
        try {
        	getChapters(tablecontent);
        } catch (Exception e) {
            Log.e(EBookActivity.DEBUG_TAG, "Failed to load chapters", e);
        }        
                
        mAdapter = new ListAdapter( 
				this, 
				mChapterList,
				R.layout.tablecontent_menu_item,
				new String[] { EBookActivity.FIRST_LINE, EBookActivity.SECOND_LINE },
				new int[] { R.id.Chapter, R.id.Chapter_Name }  );
       setListAdapter(mAdapter);
       mAdapter.notifyDataSetChanged();               
       
       if (!EBookActivity.APP_START) {  // Make sure this is at the start of the app
       	// Retrieve the shared preferences
           mEbookSettings = getSharedPreferences(EBookActivity.EBOOK_PREFERENCES, Context.MODE_PRIVATE);
           
           // Get our current chapter
           int currentChapter = mEbookSettings.getInt(EBookActivity.EBOOK_PREFERENCES_CHAPTER,0);
           if (currentChapter != 0) {
        	   startChapter(currentChapter);
           }
           EBookActivity.APP_START = true;
       }
              
    }
	
	@Override
	protected void onDestroy() {
		EBookActivity.APP_START = false;
		super.onDestroy();
	}

	private void startChapter(int chapter) {
    	Intent i = new Intent(EBookTableContentActivity.this, EBookBookActivity.class);
    	i.putExtra(EBookActivity.CHAPTER_LIST,mChapterList);
    	i.putExtra(EBookActivity.CURRENT_CHAPTER, chapter);	
		startActivity(i); 
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
        case 0:
        	Intent i = new Intent(this, EBookTitlePageActivity.class);
        	startActivity(i);
        	break;
        default:
        	startChapter(position);
        	break;
        }         
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
     * Churn through an XML score information and populate a {@code ArrayList}
     * 
     * @param items
     *            The {@code ArrayList} to populate
     * @param files
     *            The {@code ArrayList} to populate
     * @param chapters
     *            A standard {@code XmlResourceParser} containing the scores
     * @throws XmlPullParserException
     *             Thrown on XML errors
     * @throws IOException
     *             Thrown on IO errors reading the XML
     */
    private void getChapters(XmlResourceParser chapters) throws XmlPullParserException, IOException {
        int eventType = -1;
                        
        // Find chapter records from XML
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                // Get the name of the tag (eg scores or score)
                String strName = chapters.getName();
                if (strName.equals(EBookActivity.EBOOK_XML_TAG_CHAPTER)) {
                    HashMap<String,String> item = new HashMap<String,String>();
                    item.put(EBookActivity.FIRST_LINE, chapters.getAttributeValue(null, EBookActivity.EBOOK_XML_TAG_NAME));
                    item.put(EBookActivity.SECOND_LINE, chapters.getAttributeValue(null, EBookActivity.EBOOK_XML_TAG_DESC));
                    item.put(EBookActivity.THIRD_LINE, chapters.getAttributeValue(null, EBookActivity.EBOOK_XML_TAG_NUM));
                    mChapterList.add(item);                    
                }
            }
            eventType = chapters.next();
        }       
    }      
    
    private class ListAdapter extends SimpleAdapter {
    	private LayoutInflater mInflater;
    	ArrayList<HashMap<String,String>> items;
				
        public ListAdapter(Context context, ArrayList<HashMap<String,String>> items, 
        		int resource, String[] from, int[] to) {
                super(context, items, resource, from, to);
                // Cache the LayoutInflate to avoid asking for a new one each time.
                mInflater = LayoutInflater.from(context);
                this.items = items;
        }
        
        @Override
		public boolean areAllItemsEnabled() {
        	return false;
		}
        
		@Override
		public boolean isEnabled(int position) {
			boolean enabled = false;
	         if(items.size() > position){
	                 enabled = !items.get(position).get(EBookActivity.THIRD_LINE).startsWith("-");
	         }	         
	         return enabled;
		}

		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	ViewWrapper wrapper = null;
        	        	
        	// if we got no view, inflate one of our rows
        	if (convertView == null) {
        		convertView = mInflater.inflate(R.layout.tablecontent_menu_item, null);	       		
        		wrapper = new ViewWrapper(convertView);        		        		
        		convertView.setTag(wrapper);        		
        	} else {
        		wrapper = (ViewWrapper)convertView.getTag();
        	}       	
        	
        	// get the views for setting data after and set the values... 
        	wrapper.getItem().setText(items.get(position).get(EBookActivity.FIRST_LINE));
        	wrapper.getItem2().setText(items.get(position).get(EBookActivity.SECOND_LINE));
        	       	        	

        	// return the view
        	return convertView;			 
        }   				
    }
}