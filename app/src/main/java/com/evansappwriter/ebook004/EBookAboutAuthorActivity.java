package com.evansappwriter.ebook004;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class EBookAboutAuthorActivity extends EBookActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_author);
        
        setTitle(getResources().getString(R.string.about_author_title));
        
        InputStream iFile = getResources().openRawResource(R.raw.aboutauthor);
        try {
        	TextView aboutText = (TextView)findViewById(R.id.about_author);
            String strFile = inputStreamToString(iFile);
            aboutText.setText(strFile);
        } catch (Exception e) {
        	Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }
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