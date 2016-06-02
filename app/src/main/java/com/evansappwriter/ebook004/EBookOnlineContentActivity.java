package com.evansappwriter.ebook004;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class EBookOnlineContentActivity extends EBookActivity {    
	int mProgressCounter = 0;
	WebcontentDownLoader videoLoader;
	WebcontentDownLoader imageLoader;
	WebcontentDownLoader audioLoader;
	WebcontentDownLoader docsLoader; 
	TabHost host;
	ListAdapter myVideoListAdapter;	
	ListAdapter myImageListAdapter;
	ListAdapter myAudioListAdapter;		
	ListAdapter myDocListAdapter;
	ListView videolv;
	ListView imagelv;
	ListView audiolv;	
	ListView doclv;
	ArrayList<DisplayItem> mVideoList;
	ArrayList<DisplayItem> mImageList;
	ArrayList<DisplayItem> mAudioList;
	ArrayList<DisplayItem> mDocList;
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.online_content);
        
        setTitle(getResources().getString(R.string.app_name) + ": Online Content");
        
        // Set up the tabs
        host = (TabHost) findViewById(R.id.TabHost01);
        host.setup();
        
        // Videos tab
        TabSpec videoTab = host.newTabSpec("videoTab");
        videoTab.setIndicator(getResources().getString(R.string.online_content_videos),
        		getResources().getDrawable(R.drawable.videos));
        videoTab.setContent(R.id.videoList);
        host.addTab(videoTab);
        
        // Images tab
        TabSpec imageTab = host.newTabSpec("imageTab");
        imageTab.setIndicator(getResources().getString(R.string.online_content_images),
        		getResources().getDrawable(R.drawable.images));
        imageTab.setContent(R.id.imageList);
        host.addTab(imageTab);
        
        // Audio tab
        TabSpec audioTab = host.newTabSpec("audioTab");
        audioTab.setIndicator(getResources().getString(R.string.online_content_audio),
        		getResources().getDrawable(R.drawable.audio));
        audioTab.setContent(R.id.audioList);
        host.addTab(audioTab);
        
        // Docs tab
        TabSpec docTab = host.newTabSpec("docTab");
        docTab.setIndicator(getResources().getString(R.string.online_content_docs),
        		getResources().getDrawable(R.drawable.docs));
        docTab.setContent(R.id.docList);
        host.addTab(docTab);
        
        for(int i=0;i<host.getTabWidget().getChildCount();i++) {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.BLUE);
        }
        
        // Set the default tab
        host.setCurrentTabByTag("videoTab");
        
        // ********************************** //
        // set up the video list
        mVideoList = new ArrayList<DisplayItem>();        
        videolv = (ListView) findViewById(R.id.videoList);
        
        // set up the video adapter
        myVideoListAdapter = new ListAdapter(this, R.id.videoList, mVideoList);
        videolv.setAdapter(myVideoListAdapter);        
                               
        // Get Video Data
        videoLoader = new WebcontentDownLoader();
        videoLoader.execute(EBOOK_SERVER_CONTENT + EBOOK_VIDEO_CONTENT, mVideoList, myVideoListAdapter);
        // ********************************** //
        
        // ********************************** //
        // set up the image list
        mImageList = new ArrayList<DisplayItem>();        
        imagelv = (ListView) findViewById(R.id.imageList);
        
        // set up the audio adapter
        myImageListAdapter = new ListAdapter(this, R.id.imageList, mImageList);
        imagelv.setAdapter(myImageListAdapter); 
        
        imageLoader = new WebcontentDownLoader();
        imageLoader.execute(EBOOK_SERVER_CONTENT + EBOOK_IMAGE_CONTENT, mImageList, myImageListAdapter);
        // ********************************** //
        
        // ********************************** //
        // set up the audio list
        mAudioList = new ArrayList<DisplayItem>();        
        audiolv = (ListView) findViewById(R.id.audioList);
        
        // set up the audio adapter
        myAudioListAdapter = new ListAdapter(this, R.id.audioList, mAudioList);
        audiolv.setAdapter(myAudioListAdapter);   
        
        // Get Audio Data
        audioLoader = new WebcontentDownLoader();
        audioLoader.execute(EBOOK_SERVER_CONTENT + EBOOK_AUDIO_CONTENT, mAudioList, myAudioListAdapter);
        // ********************************** //
        
        // ********************************** //
        // set up the doc list
        mDocList = new ArrayList<DisplayItem>();        
        doclv = (ListView) findViewById(R.id.docList);
        
        // set up the doc adapter
        myDocListAdapter = new ListAdapter(this, R.id.docList, mDocList);
        doclv.setAdapter(myDocListAdapter);   
        
        // Get Doc Data
        docsLoader = new WebcontentDownLoader();
        docsLoader.execute(EBOOK_SERVER_CONTENT + EBOOK_DOCS_CONTENT, mDocList, myDocListAdapter); 
        // ********************************** //
    }
       
    
    @Override
	protected void onPause() {
		if (videoLoader != null && videoLoader.getStatus() != AsyncTask.Status.FINISHED) {
			videoLoader.cancel(true);			
		}
		if (imageLoader != null && imageLoader.getStatus() != AsyncTask.Status.FINISHED) {
			imageLoader.cancel(true);			
		}
		if (audioLoader != null && audioLoader.getStatus() != AsyncTask.Status.FINISHED) {
			audioLoader.cancel(true);			
		}
		if (docsLoader != null && docsLoader.getStatus() != AsyncTask.Status.FINISHED) {
			docsLoader.cancel(true);			
		}
		super.onPause();
	}
    
    public void myClickHandler(View v) {
    	String link = "";
    	switch (host.getCurrentTab()) {
    	case 0:
    		link = mVideoList.get(v.getId()).getLink();
    		break;
    	case 1:
    		link = mImageList.get(v.getId()).getLink();
    		break;
    	case 2:
    		link = mAudioList.get(v.getId()).getLink();
    		break;
    	case 3:
    		link = mDocList.get(v.getId()).getLink();
    		break;
    	}
    	
    	Intent i;
    	if (host.getCurrentTab() == 2 && (link.endsWith(".mp3") || !(link.contains("playaudio.cfm?uid=")))) {
            i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse(link), "audio/*");
    	} else {
    		i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
    	}
    	
    	startActivity(i);       	
    }
    
    private class ListAdapter extends ArrayAdapter<DisplayItem> {
		ArrayList<DisplayItem> items;
				
        public ListAdapter(Context context, int resource,
                        ArrayList<DisplayItem> items) {
                super(context, resource, items);
                this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	ViewWrapper2 wrapper = null;

        	// if we got no view, inflate one of our rows
        	if (convertView == null) {
        		LayoutInflater vi = (LayoutInflater) getContext()
                	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		convertView = vi.inflate(R.layout.online_content_row, null);
        		wrapper = new ViewWrapper2(convertView);
        		        		
        		convertView.setTag(wrapper);
        	} else {
        		wrapper = (ViewWrapper2)convertView.getTag();
        	}       	
        	
        	// get the current object from the list
        	DisplayItem row = items.get(position);
            
        	// get the views for setting data after and set the values...         	
        	wrapper.getItem().setText("");
        	wrapper.getItem2().setText(row.getTitle());
        	wrapper.getItem3().setId(position);
        	
        	// return the view
        	return convertView;			 
        }   				
    }
    
    private class DisplayItem {
		private String title;
		private String link;
		
		public DisplayItem(String title, String link) {
			super();
			this.title = title;
			this.link = link;
		}

		public String getTitle() {
			return title;
		}		

		public String getLink() {
			return link;
		}	
	}   
    
    private class WebcontentDownLoader extends AsyncTask<Object, String, Boolean> {
    	private static final String DEBUG_TAG = "WebcontentDownLoader";
    	ArrayList<DisplayItem> items;
    	ListAdapter adapter;
    	
		@SuppressWarnings("unchecked")
		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			String pathToContent = (String) params[0];
			items = (ArrayList<DisplayItem>) params[1];	
			adapter = (ListAdapter) params[2];
			
			XmlPullParser content = null;
			try {
				URL xmlUrl = new URL(pathToContent);
				content = XmlPullParserFactory.newInstance().newPullParser();
				content.setInput(xmlUrl.openStream(), null);								
			} catch (XmlPullParserException e) {
				content = null;
            } catch (IOException e) {
            	content = null;
            }
            
            if (content != null) {
                try {
                    processContent(content);
                    result = true;
                } catch (XmlPullParserException e) {
                    Log.e(DEBUG_TAG, "Pull Parser failure", e);
                } catch (IOException e) {
                    Log.e(DEBUG_TAG, "IO Exception parsing XML", e);
                }
            }
			
			return result;			
		}
		
		/**
	     * Churn through an XML online content information and populate a the list
	     * 
	     * @param contents
	     *            A standard {@code XmlPullParser} containing the scores
	     * @throws XmlPullParserException
	     *             Thrown on XML errors
	     * @throws IOException
	     *             Thrown on IO errors reading the XML
	     */
	    private void processContent(XmlPullParser contents) throws XmlPullParserException, IOException {
	        int eventType = -1;
	        boolean bContent = false;
	        
	        // Find Content records from XML
	        while (eventType != XmlResourceParser.END_DOCUMENT) {
	            if (eventType == XmlResourceParser.START_TAG) {
	            	// Get the name of the tag (eg webcontent)
	                String strName = contents.getName();
	                if (strName.equals(EBOOK_XML_TAG_WEBCONTENT)) {
	                	bContent = true;
	                    String contentTitle = contents.getAttributeValue(null, EBOOK_XML_TAG_TITLE);
	                    String contentSite = contents.getAttributeValue(null, EBOOK_XML_TAG_SITE);
	                    items.add(new DisplayItem(contentTitle,contentSite));
	                }
	            }
	            eventType = contents.next();
	        }
	     // Refresh the list
	        if (bContent == true) {
	        	publishProgress();
	        }
	    }
	    
	    @Override
		protected void onPreExecute() {
			mProgressCounter++;
			EBookOnlineContentActivity.this.setProgressBarIndeterminateVisibility(true);
		}
	    
	    @Override
		protected void onPostExecute(Boolean result) {
			mProgressCounter--;
			if (mProgressCounter <=0) {
				mProgressCounter = 0;
				EBookOnlineContentActivity.this.setProgressBarIndeterminateVisibility(false);
			}
		}
	    
	    @Override
		protected void onCancelled() {
			mProgressCounter--;
			if (mProgressCounter <=0) {
				mProgressCounter = 0;
				EBookOnlineContentActivity.this.setProgressBarIndeterminateVisibility(false);
			}
		}
	    
	    @Override
		protected void onProgressUpdate(String... values) {
			adapter.notifyDataSetChanged();
		} 
    }
}