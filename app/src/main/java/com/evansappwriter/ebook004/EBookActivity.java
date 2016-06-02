package com.evansappwriter.ebook004;

import android.app.Activity;

public class EBookActivity extends Activity {
	// This determines the book this app is activated for
	public static final int EBOOK_BOOK = 8; 
	
	// Start and end chapters for workbook questions
	public static final int WORKBOOK_START_CHAPTER = 3;
    public static final int WORKBOOK_END_CHAPTER = 7;
    public static final int FIRST_CHAPTER = 1;
    public static final int LAST_CHAPTER = 10;
	
	// To prevent the redirection to a chapter if orientation is changed in the Table of Content
	public static boolean APP_START = false;
	   
    // XML Tags
	public static final String EBOOK_XML_TAG_NAME = "name";
    public static final String EBOOK_XML_TAG_DESC = "desc";
    public static final String EBOOK_XML_TAG_NUM = "num";
    public static final String EBOOK_XML_TAG_QUESTION = "question";
    public static final String EBOOK_XML_TAG_CHAPTER = "chapter";
    public static final String EBOOK_XML_TAG_LABEL = "label";    
    public static final String EBOOK_XML_TAG_WEBCONTENT = "webcontent";
    public static final String EBOOK_XML_TAG_TITLE = "title";
    public static final String EBOOK_XML_TAG_SITE = "site";   
    
    // Intent Tags
    public static final String FIRST_LINE = "Line 1";
	public static final String SECOND_LINE = "Line 2";
	public static final String THIRD_LINE = "Line 3";
    public static final String CHAPTER_LIST = "ChapList"; 
    public static final String CURRENT_CHAPTER = "CurrentChapter"; 
    public static final String WORKBOOK_CHAPTER = "WorkbookChapter";
    
    public static final String CHAPTER_FILE = "ChapFile";
    public static final String CHAPTER_DESC = "Desc";
    
    // Preference Tags
	public static final String EBOOK_PREFERENCES = "ReaderPrefs";
	public static final String EBOOK_PREFERENCES_CHAPTER = "CurrChapter";
	
	public static final String DEBUG_TAG = "Mini eBook Log";
	
        
    // Server URLs
   /* public static final String EBOOK_SERVER_BASE = "http://messengerphoneapps.appspot.com/";
	public static final String EBOOK_SERVER_CONTENT = EBOOK_SERVER_BASE + "webcontents.jsp" +
					"?book=" + EBOOK_BOOK; */
	// 06/21/2012 - 1.2 MEE - switch servers & using cold fussion script now
	public static final String EBOOK_SERVER_BASE = "http://messengerphoneapps.com/phone/";
	public static final String EBOOK_SERVER_CONTENT = EBOOK_SERVER_BASE + "webcontents.cfm" + "?book=" + EBOOK_BOOK;
	public static final String EBOOK_VIDEO_CONTENT = "&type=1";
	public static final String EBOOK_IMAGE_CONTENT = "&type=2";
	public static final String EBOOK_AUDIO_CONTENT = "&type=3";
	public static final String EBOOK_DOCS_CONTENT = "&type=4";
		
	
   
}