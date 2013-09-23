package com.example.testapp;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProdukteFragment extends Fragment{

	List<String> mAuthorsList;
	List<String> mWorksList;
	private WorksFragment resultFragment;
	String[] urls = {"http://dl.dropboxusercontent.com/u/1631817/bildtasks/books.plist",
			"http://dl.dropboxusercontent.com/u/1631817/bildtasks/audiocds.plist"};

	public ProdukteFragment(List<String> aList){
		mAuthorsList = aList;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.fragment_layout, null);
			ListView listView = (ListView) v.findViewById(R.id.mainList);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, mAuthorsList);
	        listView.setAdapter(adapter);
	        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){

	        	@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String key = mAuthorsList.get(arg2);
					// Search the XML from both urls using the selected element
					getWorksOfAuthors(arg0.getContext(),key,urls);
				}
	        });
		return v;

	}
	
	public void getWorksOfAuthors(Context context,String key, String[] url){		
		new BackgroundTask(context,key).execute(url);
	}
	
	class BackgroundTask extends AsyncTask<String, Void, List<String>> {

	    String key;
	    Context mContext;
	    public BackgroundTask(Context acontext,String aKey){
	    	key = aKey;
	    	mContext = acontext;
	    	
	    }
	    
	    protected List<String> doInBackground(String... urls) {
	    	List<String> items = new ArrayList<String>();
			XmlParserUtil parser = new XmlParserUtil();
			
			//Loop over each URL
			for(int i=0;i<urls.length;i++){
				String xml = parser.getXmlFromUrl(urls[i]); // getting XML
				Document doc = parser.getDomElement(xml);
				NodeList nl = doc.getElementsByTagName("dict");
				
				//Loop over each "dict" tag block
				for(int j = 0; j<nl.getLength();j++){
					Element e = (Element) nl.item(j);
					NodeList n = e.getElementsByTagName("string"); 
					if(parser.getElementValue(n.item(1)).equals(key) ||
	        			 parser.getElementValue(n.item(2)).equals(key)){
						items.add(parser.getElementValue(n.item(0))); 
					}
				}
			}
			return items;
	    }

	    protected void onPostExecute(List<String> result) {
	    	mWorksList = result;
	    	//Add new fragment 
	    	android.app.FragmentManager fragmentManager = getFragmentManager();
	    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			resultFragment = new WorksFragment(mWorksList);
			fragmentTransaction.replace(android.R.id.content, resultFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			
	    }
	}
}
