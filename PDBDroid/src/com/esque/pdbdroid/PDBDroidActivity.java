package com.esque.pdbdroid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PDBDroidActivity extends Activity implements OnClickListener {
	ProgressDialog dialog;
	String pdbPath, line = null;
	Button bFileBrowser, bReadFile, bDisplayMolecule;
	TextView tvText;
	final static int FILE_BROWSER_FILE = 1;
	static float[] coordinates;
	static int arraylength;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initialize();
	}

	private void initialize() {
		// Initializes all the variables and views
		bFileBrowser = (Button) findViewById(R.id.bFileBrowser);
		bFileBrowser.setOnClickListener(this);

		bReadFile = (Button) findViewById(R.id.readFile);
		bReadFile.setOnClickListener(this);
		
		bDisplayMolecule = (Button) findViewById(R.id.displayMolecule);
		bDisplayMolecule.setOnClickListener(this);

		tvText = (TextView) findViewById(R.id.tvText);
	
		dialog = new ProgressDialog(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bFileBrowser:
			//Launch File Browser
			Intent i = new Intent(this, FileBrowser.class);
			startActivityForResult(i, FILE_BROWSER_FILE);
			break;

		case R.id.readFile:
			//Start reading the file in the background
			new ReadFile().execute();
			break;
			
		case R.id.displayMolecule:
			Intent j = new Intent(this, TouchRotateActivity.class);
			j.putExtra("coords", coordinates);
			j.putExtra("index", arraylength);
			startActivity(j);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			pdbPath = (String) extras.get("pdbfile");
			tvText.setText(pdbPath);
			getArrayLength();
		}
	}

	private void getArrayLength() {
		// Determines the length of the coordinate array
		BufferedReader buf;
		try {
			buf = new BufferedReader(new FileReader(pdbPath));

			String temp;
			arraylength = 0;
			while ((temp = buf.readLine()) != null) {
				if (temp.startsWith("ATOM"))
					arraylength++;
			}
			buf.close();
			tvText.setText("Array Length = " + arraylength);
			coordinates = new float[arraylength * 3]; // Because there are three coordinates per atom

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class ReadFile extends AsyncTask<Void, Void, Void> {
		//Populates the coordinate array
		
		BufferedReader buf;
		String temp;
		int index = 0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setMessage("Working ..");
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			tvText.setText("First set of coords: " + coordinates[0] + " " + coordinates[1] + " " + coordinates[2]);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				buf = new BufferedReader(new FileReader(pdbPath));

				while ((temp = buf.readLine()) != null) {
					if (temp.startsWith("ATOM")) {
						coordinates[index] = Float.parseFloat(temp.substring(
								32, 38));
						index++;
						coordinates[index] = Float.parseFloat(temp.substring(
								40, 46));
						index++;
						coordinates[index] = Float.parseFloat(temp.substring(
								48, 54));
						index++;
					}
				}

				buf.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

}