package com.example.testapp;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.testapp.ProdukteFragment.BackgroundTask;

public class WorksFragment extends Fragment{

	private int index;
	List<String> mList;


	public WorksFragment(List<String> aList){
		mList = aList;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.result_fragment, null);
			ListView listView = (ListView) v.findViewById(R.id.resultList);
	        ArrayAdapter<String> aa = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, mList);
	        listView.setAdapter(aa);
	   		return v;
		}
	

}

