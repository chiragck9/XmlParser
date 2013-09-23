package com.example.testapp;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Menu;

public class MainActivity extends Activity implements TabListener {

	private static final String url = "http://dl.dropboxusercontent.com/u/1631817/bildtasks/authors.plist";
	List<Fragment> fragList = new ArrayList<Fragment>();
	List<String> authorList = new ArrayList<String>();
	Tab impressum ;
	Tab produckte;
	ActionBar bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		DownloaderTask task = new DownloaderTask(this);
		task.execute(url); 		
		produckte = bar.newTab();
		produckte.setText("Produkte");
		impressum = bar.newTab();
		impressum.setText("Impressum");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ProdukteFragment produckteFragment = null;

		if (fragList.size() > tab.getPosition())
				fragList.get(tab.getPosition());

		if(tab.getPosition() == 0){
			produckteFragment = new ProdukteFragment(authorList);
			fragList.add(produckteFragment);
			ft.replace(android.R.id.content, produckteFragment);
		}else{
			ImpressumFragment impressumFragment = new ImpressumFragment();
			ft.replace(android.R.id.content, impressumFragment);
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (fragList.size() > tab.getPosition()) {
			ft.remove(fragList.get(tab.getPosition()));
		}

	}
	
	public class DownloaderTask extends AsyncTask<String, Integer, List<String>> {
		TabListener listener;
		ProgressDialog progressDialog;
		Context mContext;
		
		public DownloaderTask(TabListener activity){
			listener =  activity;
			mContext = (Context) activity;
		}
		
		@Override
		public void onPostExecute(List<String> result) {
			authorList = result;
			
			produckte.setTabListener(listener);
			bar.addTab(produckte);

			impressum.setTabListener(listener);
			bar.addTab(impressum);
			progressDialog.dismiss();  
		}
		@Override
		public void onPreExecute() {
			//Create a new progress dialog  
	        progressDialog = new ProgressDialog(mContext);   
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        progressDialog.setTitle("Please wait!!!");  
	        progressDialog.setMessage("Getting author names...");
	        progressDialog.setCancelable(true);  
	        progressDialog.setIndeterminate(false);  
	        progressDialog.setMax(100);  
	        progressDialog.show(); 
		}
		@Override
		public void onProgressUpdate(Integer... values) {
			 progressDialog.setProgress(values[0]);  
		}
		
		@Override
		protected List<String> doInBackground(String... arg0) {

			XmlParserUtil parser = new XmlParserUtil();
	        String xml = parser.getXmlFromUrl(arg0[0]); 
	        Document doc = parser.getDomElement(xml); 
	 
	        NodeList nl = doc.getElementsByTagName("string");
	        // Iterate through all node items
	        for (int i = 0; i < nl.getLength(); i++) {
	            Element e = (Element) nl.item(i);
	            authorList.add(parser.getElementValue(e));
	 	    }
	 
		    return authorList; 
		}
		
	}

}
