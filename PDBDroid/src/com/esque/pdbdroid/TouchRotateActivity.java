/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.esque.pdbdroid;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 *
 * Shows:
 * + How to redraw in response to user input.
 */
public class TouchRotateActivity extends Activity {

    private GLSurfaceView mGLSurfaceView;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create our Preview view and set it as the content of our
        // Activity
        mGLSurfaceView = new TouchSurfaceView(this);
        setContentView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();
        mGLSurfaceView.setFocusableInTouchMode(true);   
	}

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }

}


class TouchSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private ProteinRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    
    public TouchSurfaceView(Context context) {
        super(context);
        mRenderer = new ProteinRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override public boolean onTouchEvent(MotionEvent e) {
   
    	float x = e.getX();
        float y = e.getY();
        
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;
            mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
            mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
            requestRender();
            break;
        
        case MotionEvent.ACTION_POINTER_DOWN:
        	float dx2 = x - mPreviousX;
            float dy2 = y - mPreviousY;
            mRenderer.mPosX += dx2 * TOUCH_SCALE_FACTOR;
            mRenderer.mPosY += dy2 * TOUCH_SCALE_FACTOR;
            requestRender();
            break;
        }	
        
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class ProteinRenderer implements GLSurfaceView.Renderer {
    	
    	private Protein mProtein;
        public float mAngleX;
        public float mAngleY;
        public float mPosX;
        public float mPosY;

    	public ProteinRenderer() {
            mProtein = new Protein();
        }

        public void onDrawFrame(GL10 gl) {

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(mPosX, mPosY, -20.0f);
            gl.glRotatef(mAngleX, 0, 1, 0);
            gl.glRotatef(mAngleY, 1, 0, 0);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            mProtein.draw(gl);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
             gl.glViewport(0, 0, width, height);

             float ratio = (float) width / height;
             gl.glMatrixMode(GL10.GL_PROJECTION);
             gl.glLoadIdentity();
             gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
           
            gl.glDisable(GL10.GL_DITHER);

             gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                     GL10.GL_FASTEST);

             gl.glClearColor(1,1,1,1);
             gl.glEnable(GL10.GL_CULL_FACE);
             gl.glShadeModel(GL10.GL_SMOOTH);
             gl.glEnable(GL10.GL_DEPTH_TEST);
             gl.glEnable(GL10.GL_SMOOTH);
        }
        
    }


}


