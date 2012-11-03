package com.idi.arau;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Game extends Activity implements OnClickListener {

	private TimeThread timeThread;
	private static final int DIALOG_GAMEOVER_ID = 1;
	private static final int DIALOG_FINISH_GAME = 2;
	private static final int TIME_X_WORD = 20;
	private Dialog gameOverDialog = null;
	private Dialog finishDialog = null;
	private ProgressBar timeBar;
	private ManagerGame manager;
	private LinearLayout layout;
	private ViewGame view;
	Button playAgain;
	Button goStart;

	
	///////////////////////////////////////////////////////////////////////////////
	/////	EVENTS
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		this.manager = ManagerGame.getInstanceManager(this);
		manager.restartIndex();
	}
	
	
	// DIALOG
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);

		switch (id) {
		case DIALOG_GAMEOVER_ID:
			dialog.setContentView(R.layout.game_over_dialog);
			Button playAgain = (Button) dialog.findViewById(R.id.playAgain);
			playAgain.setOnClickListener(this);
			Button goStart = (Button) dialog.findViewById(R.id.goStart);
			dialog.setCancelable(false);
			goStart.setOnClickListener(this);
			gameOverDialog = dialog;
			break;
		case DIALOG_FINISH_GAME:
			dialog.setContentView(R.layout.finish_game_dialog);
			Button playAgn = (Button) dialog.findViewById(R.id.playAgn);
			playAgn.setOnClickListener(this);
			Button goStrt = (Button) dialog.findViewById(R.id.goStrt);
			dialog.setCancelable(false);
			goStrt.setOnClickListener(this);
			finishDialog = dialog;
			break;
		default:
			dialog = null;
		}
		return dialog;
	}	

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {	
		super.onSaveInstanceState(outState);		
	}

	@Override
	protected void onPause() {
		super.onPause();		
		killTimeThread();
		view.killGameThread();
		view = null;
	};
	
	@Override
	protected void onResume() {	
		super.onResume();
		layout = defineLayout();
		this.timeBar = defineProgressTimeBar();
		layout.addView(timeBar);
		view = new ViewGame(this, viewToGame);
		layout.addView(view);
		timeInit(timeBar);
		setContentView(layout);
		view.setTimeThread(timeThread, TIME_X_WORD);			
		
	}
	
	///////////////////////////////////////////////////////////////////////////////
	/////	PUBLIC

	public void timeInit(ProgressBar timeBar) {
		timeThread = new TimeThread(this, timeBar, TIME_X_WORD);
		timeThread.setRunning(true);
		timeThread.start();
	}

	public void onTimeOut() {
		if (timeThread.isTimeOut()) {
			this.runOnUiThread(showGameOverDialog);
		}
	}

	
	// IMPLEMENT onClick Dialog buttons 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playAgain:
			dismiss(gameOverDialog);
			playAgain();
			break;
		case R.id.goStart:
			dismiss(gameOverDialog);
			goToStart();
			break;
		case R.id.playAgn:
			dismiss(finishDialog);
			playAgain();
			break;
		case R.id.goStrt:
			dismiss(finishDialog);
			goToStart();
			break;
		default:
			finish();
		}		
	}

	public void restartTime() {
		timeInit(timeBar);
	}

	public void killTimeThread() {
		timeThread.setRunning(false);
		timeThread.interrupt();		
	}
	
	// INTERFACE IMPLEMENTATION
	ViewToGame viewToGame = new ViewToGame() {

		@Override
		public void restartTime() {
			timeInit(timeBar);
		}

		@Override
		public void killOldThread() {
			killTimeThread();		
		}

		@Override
		public void resetView() {
			resetViewFromActivity();
		}		
		
		@Override
		public void finishGame() {
			showFinishDialog();
		}
		
		@Override
		public void gameOver() {
			showGameOverDialog();
		}
	};

	
	///////////////////////////////////////////////////////////////////////////////
	/////	PRIVATE

	
	private LinearLayout defineLayout() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		return layout;
	}

	private ProgressBar defineProgressTimeBar() {
		int progressBarStyle = android.R.attr.progressBarStyleHorizontal;
		ProgressBar timeBar = new ProgressBar(this, null, progressBarStyle);
		timeBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.time_bar_def));
		return timeBar;
	}
	

	private Runnable showGameOverDialog = new Runnable() {
		public void run() {
			showDialog(DIALOG_GAMEOVER_ID);
		}
	};
	
	private Runnable showFinishDialog = new Runnable () {
		public void run() {			
			showDialog(DIALOG_FINISH_GAME);
		}		
	};
	
	private void showFinishDialog() {
		this.runOnUiThread(showFinishDialog);
	}
	
	private void showGameOverDialog() {
		this.runOnUiThread(showGameOverDialog);
	}
	
	protected void playAgain() {
		Intent i = new Intent(this, Game.class);
		startActivity(i);
	}

	protected void goToStart() {
		killTimeThread();
		view.killGameThread();
		finish();
	}
	
	protected void stop() {
		int wordIndex = manager.getIndex();
		// estem aqui per guardar el state
	}

	private void resetViewFromActivity() {
		layout.removeView(view);
		view = new ViewGame(this, viewToGame);
		layout.addView(view);
		
		killTimeThread();
		restartTime();
		
		setContentView(layout);
		view.setTimeThread(timeThread, TIME_X_WORD);
	}		
	
	private void dismiss(Dialog dialog) {
		dialog.dismiss();
		dialog = null;
	}
}
