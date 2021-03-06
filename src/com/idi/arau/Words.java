package com.idi.arau;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Words extends Activity {
	private Button button;
	private static final int EASY = 0;
	private static final int HARD = 1;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		Typeface font = Typeface.createFromAsset(getAssets(),
				"gloriahallelujah.ttf");
		
		
		button = (Button) findViewById(R.id.easy);
		button.setTypeface(font);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				play(EASY);
			}
		});

		button = (Button) findViewById(R.id.hard);
		button.setTypeface(font);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				play(HARD);
			}
		});

		
		button = (Button) findViewById(R.id.images);
		button.setTypeface(font);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				goGallery();
			}
		});
		
		button = (Button) findViewById(R.id.ranking);
		button.setTypeface(font);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				goUsers();
			}
		});

		button = (Button) findViewById(R.id.prefs);
		button.setTypeface(font);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				goPreferences();
			}
		});
		
		button = (Button) findViewById(R.id.help);
		button.setTypeface(font);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				goHelp();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();		
	}

	@Override
	public void onPause() {
		super.onPause();		
		this.finishActivity(0);
	}

	protected void goPreferences() {
		Intent i = new Intent(this, SetPreferenceActivity.class);
		startActivity(i);
	}
	
	protected void goUsers() {
		Intent i = new Intent(this, UserActivity.class);
		startActivity(i);		
	}
	
	protected void goGallery() {
		Intent i = new Intent(this, Gallery.class);
		startActivity(i);		
	}
	
	protected void goHelp() {
		Intent i = new Intent(this, HelpActivity.class);
		startActivity(i);		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);	
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addUser:
			goUsers();
			return true;
		case R.id.pictures:
			goGallery();
			return true;		
		case R.id.help:
			goHelp();
			return true;
		}
		return false;
	}

	private void play(int level) {
		Intent i = new Intent(this, Game.class);
		i.putExtra("level", level);
		startActivity(i);
	}
}
