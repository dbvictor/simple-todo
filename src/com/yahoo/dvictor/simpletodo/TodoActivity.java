package com.yahoo.dvictor.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	ArrayList<String>    items;
	ArrayAdapter<String> itemsAdapter;
	ListView             lvItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		// Create our list of items, load the previously persisted copy
		items        = new ArrayList<>();
		loadItems();
		// Setup an adapter to connect our list to the view.
		itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
		lvItems      = (ListView) findViewById(R.id.lvItems);
		lvItems.setAdapter(itemsAdapter);
		// Add a long-click listener to do removals from the list.
		setupListViewListener();
	}
	
	private void setupListViewListener(){
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long rowId){
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				saveItems(); // after every change, save the list persistently.
				return true;
			}
		});
	}
	
	/** Add item entered by user into the text field when they click the add button, then clear entry once accepted. */
	public void addTodoItem(View v){
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		itemsAdapter.add(etNewItem.getText().toString());
		etNewItem.setText("");
		saveItems(); // after every change, save the list persistently.
	}
	
	/** Read the list items from the persisted file (if any) -- todo.txt. */
	private void loadItems(){
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir,"todo.txt");
		try{
			if(todoFile.exists()) items = new ArrayList<String>(FileUtils.readLines(todoFile));
		}catch(IOException e){
			items = new ArrayList<>();
			e.printStackTrace();
		}
	}
	
	/** Save the list items to the persisted file -- todo.txt. */
	private void saveItems(){
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir,"todo.txt");
		try{
			FileUtils.writeLines(todoFile, items);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
