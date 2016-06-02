package com.evansappwriter.ebook004;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WorkbookEdit extends EBookActivity {
	private TextView mQuestionText;
	private EditText mAnswerText;
	private Long mRowId;
	private WorkbookDbAdpater mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDbHelper = new WorkbookDbAdpater(this);
        mDbHelper.open();
        
        setContentView(R.layout.workbook_edit);
       
        mQuestionText = (TextView) findViewById(R.id.question_edit);
        mAnswerText = (EditText) findViewById(R.id.answer_edit);
      
        Button confirmButton = (Button) findViewById(R.id.confirm);
       
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(WorkbookDbAdpater.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(WorkbookDbAdpater.KEY_ROWID)
                                    : null;
        }
        
        populateFields();
        
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
          
        });
	}
	
	private void populateFields() {
        if (mRowId != null) {
            Cursor entry = mDbHelper.fetchEntry(mRowId);
            startManagingCursor(entry);
            mQuestionText.setText(entry.getString(
            		entry.getColumnIndexOrThrow(WorkbookDbAdpater.KEY_QUESTION)));
            mAnswerText.setText(entry.getString(
            		entry.getColumnIndexOrThrow(WorkbookDbAdpater.KEY_ANSWER)));
        }
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
        outState.putSerializable(WorkbookDbAdpater.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}
    
	private void saveState() {
        String answer = mAnswerText.getText().toString();

        mDbHelper.updateEntry(mRowId, answer);        
    }

}
