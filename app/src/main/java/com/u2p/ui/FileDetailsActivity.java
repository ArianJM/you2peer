package com.u2p.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.u2p.ui.component.ItemFile;

public class FileDetailsActivity extends Activity {
	
	private static final String TAG = "FileDetailsActivity";
    private static final String FILE_TO_DOWNLOAD = "download";
    private static final String RATING = "rating";
    private static final String RATED_FILE = "voted";
	private ItemFile item;
	private int rating = 0;
	private Intent result;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        result = this.getIntent();
        
        item = (ItemFile) getIntent().getSerializableExtra("CALLER_ITEM");
        
        TextView owner = (TextView)findViewById(R.id.fileOwner);
        TextView size = (TextView)findViewById(R.id.fileSize);
        TextView type = (TextView)findViewById(R.id.fileType);
        TextView rating = (TextView)findViewById(R.id.fileRating);
        ImageButton image = (ImageButton)findViewById(R.id.fileImageButton);
        
        this.setTitle(item.getName());
        
        String name = item.getName();
		String[] strs = name.split("\\.");
        
        owner.append(" "+item.getUser());
        size.append(" "+item.getSize());
        type.append(" "+strs[strs.length-1]);
        rating.append(" "+item.getRating());
        
		int imageResource = getResources().getIdentifier(item.getRutaImagen()+"_big", null, getPackageName());
        image.setImageDrawable(getResources().getDrawable(imageResource));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_file_details, menu);
        return true;
    }

    public void onImageClick(View v){
    	result.putExtra(FILE_TO_DOWNLOAD, item);
    	setResult(Activity.RESULT_OK, result);
    	Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
    	Log.d(TAG, "Download started");
    	finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	setResult(Activity.RESULT_OK, result);
            	finish();
                return true;
            case R.id.rating_up:
            	rating = 1;
            	result.putExtra(RATING, rating);
            	result.putExtra(RATED_FILE, this.item);
            	return true;
            case R.id.rating_bad:
            	rating = -1;
            	result.putExtra(RATING, rating);
            	result.putExtra(RATED_FILE, this.item);
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
    	Log.d(TAG, "Pausing");
    	setResult(Activity.RESULT_OK, result);
    	super.onBackPressed();
    }
}
