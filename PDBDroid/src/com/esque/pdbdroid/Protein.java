/*
 * Copyright (C) 2007 The Android Open Source Project
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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

class Protein {
	private FloatBuffer mVertexBuffer;
	private IntBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;
	
	public Protein() {

		setIndexArray();
		setColorArray();
	}

	private void setColorArray() {
		int colors[] = {};

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asIntBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);

	}

	private void setIndexArray() {

		byte indices[] = 
			{ 0, 4, 1, 
			  0, 9, 4, 
			  9, 5, 4, 
			  4, 5, 8, 
			  4, 8, 1, 
			  8, 10,1, 
			  8, 3, 10, 
			  5, 3, 8, 
			  5, 2, 3, 
			  2, 7, 3, 
			  7, 10, 3, 
			  7, 6, 10, 
			  7, 11, 6, 
			  11, 0, 6, 
			  0, 1, 6, 
			  6, 1, 10, 
			  9, 0, 11, 
			  9, 11, 2, 
			  9, 2, 5, 
			  7, 2, 11
			};

		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);

	}

	private float[] createArray(float coordinates, float coordinates2,
			float coordinates3)
	// TODO Auto-generated method stub
	{
		float X = .525731112119133606f;
		float Z = .850650808352039932f;

		float[] tempArray = 
		{ 
			coordinates - X, coordinates2, coordinates3 + Z,
			coordinates + X, coordinates2, coordinates3 + Z,
			coordinates - X, coordinates2, coordinates3 - Z,
			coordinates + X, coordinates2, coordinates3 - Z, 
			coordinates, coordinates2 + Z, coordinates3 + X, 
			coordinates, coordinates2 + Z, coordinates3 - X, 
			coordinates, coordinates2 - Z, coordinates3 + X, 
			coordinates, coordinates2 - Z, coordinates3 - X, 
			coordinates + Z, coordinates2 + X, coordinates3, 
			coordinates - Z, coordinates2 + X, coordinates3, 
			coordinates + Z, coordinates2 - X, coordinates3, 
			coordinates - Z, coordinates2 - X, coordinates3 
		};
		return tempArray;
	}

	public void draw(GL10 gl) {
		
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
		int counter = 0;
		
		while (counter < PDBDroidActivity.arraylength*3){
		
		float[] vdata = createArray(PDBDroidActivity.coordinates[counter],
				PDBDroidActivity.coordinates[counter+1],
				PDBDroidActivity.coordinates[counter+2]);

			
		ByteBuffer vbb = ByteBuffer.allocateDirect(144);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vdata);
		mVertexBuffer.position(0);
	
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, 60, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
		counter = counter+3;
		}
		
		
	}
}
