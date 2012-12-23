package com.saywhat.bookofchanges;

import com.saywhat.opengl.openGLView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
  
	private static final String LOG_TAG = MainActivity.class.getSimpleName();
	private openGLView _OpenGL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_OpenGL = new openGLView(this);
		setContentView(_OpenGL);
		
	}

}
