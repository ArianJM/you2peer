package com.u2p.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.u2p.core.db.DbDataSource;

public class FileSelectionActivity extends Activity {

    private static final String TAG = "FileSelection";
    private static final String FILES_TO_UPLOAD = "upload";
    private File mainPath = new File(Environment.getExternalStorageDirectory() + DbDataSource.getMaindir());
    private ArrayList<File> resultFileList;
    
    private ListView directoryView;
    private ArrayList<File> directoryList = new ArrayList<File>();
    private ArrayList<String> directoryNames = new ArrayList<String>();
    private ListView fileView;
	private ArrayList<File> fileList = new ArrayList<File>();
	private ArrayList<String> fileNames = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        directoryView = (ListView)findViewById(R.id.directorySelectionList);
        fileView = (ListView)findViewById(R.id.fileSelectionList);
        TextView goUpView = (TextView)findViewById(R.id.goUpTextView);
        goUpView.setClickable(true);
        
        loadLists();
        
        directoryView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mainPath = directoryList.get(position);
				loadLists();
			}
		});
    }

    public void onGoUpClickListener(View v){
    	File parent = mainPath.getParentFile();
    	Log.d(TAG, parent.toString());
    	if(mainPath.equals(Environment.getExternalStorageDirectory())){
    		Toast.makeText(this, "Can't exit external storage", Toast.LENGTH_SHORT).show();
    	}else{
    		mainPath = parent;
    		loadLists();
    	}
    }
    
	public void onUploadClick(MenuItem item){
    	Log.d(TAG, "Upload clicked, finishing activity");
        
    	ListView lv = (ListView)findViewById(R.id.fileSelectionList);
        resultFileList = new ArrayList<File>();
        
        for(int i = 0 ; i < lv.getCount() ; i++){
        	if(lv.isItemChecked(i)){
        		resultFileList.add(fileList.get(i));
        	}
        }
        if(resultFileList.isEmpty()){
        	Log.d(TAG, "Nada seleccionado");
        	finish();
        }
        Log.d(TAG, "Files: "+resultFileList.toString());
        Intent result = this.getIntent();
        result.putExtra(FILES_TO_UPLOAD, resultFileList);
        setResult(Activity.RESULT_OK, result);
    	finish();
    }
	
	private void loadLists(){
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		};
		FileFilter directoryFilter = new FileFilter(){
			public boolean accept(File file){
				return file.isDirectory();
			}
		};
		
		if(mainPath.exists() && mainPath.length()>0){
			//Lista de directorios
			File[] tempDirectoryList = mainPath.listFiles(directoryFilter);
			directoryList = new ArrayList<File>();
			directoryNames = new ArrayList<String>();
			for(File file: tempDirectoryList){
				directoryList.add(file);
				directoryNames.add(file.getName());
			}
			ArrayAdapter<String> directoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directoryNames);
	        directoryView.setAdapter(directoryAdapter);
	        
			//Lista de ficheros
			File[] tempFileList = mainPath.listFiles(fileFilter);
			fileList = new ArrayList<File>();
    		fileNames = new ArrayList<String>();
    		for(File file : tempFileList){
    			fileList.add(file);
    			fileNames.add(file.getName());
    		}
    		
    		ArrayAdapter<String> fileAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, fileNames);
            fileView.setAdapter(fileAdapter);
            Log.d(TAG, "Lists created");
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_file_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
