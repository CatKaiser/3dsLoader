public class OpenGLRenderer implements Renderer {
  
	/** Used for debug logs. */
	private static final String LOG_TAG = "OpenGLRenderer";
	private int _width;
	private int _height;
	
	/* array to hold the sprites*/
	public ArrayList<Sprite> objects = new ArrayList<Sprite>();
	public ArrayList<Model3d> objects3d = new ArrayList<Model3d>();

	private final Context mActivityContext;

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];

	/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
	private float[] mProjectionMatrix = new float[16];
	
	/** This is a handle to our cube shading program. */
	private int mProgramHandle;

	/**
	 * Initialize the model data.
	 */
	public OpenGLRenderer(final Context activityContext)
	{	
		mActivityContext = activityContext;

	}

	protected String getVertexShader()
	{
		return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
	}

	protected String getFragmentShader()
	{
		return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader);
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
	{
		// Set the background clear color to black.
		GLES20.glClearColor(0.4f, 0.0f, 0.4f, 0.0f);

		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// The below glEnable() call is a holdover from OpenGL ES 1, and is not needed in OpenGL ES 2.
		// Enable texture mapping
		// GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		// Position the eye in front of the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = -4.0f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = 0.0f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);	

		final String vertexShader = getVertexShader();
		final String fragmentShader = getFragmentShader();	

		final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);	
		final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);	

		mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
				new String[] {"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});	
		// Load the texture
		//mTextureDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.medved);
		Log.i("shader", GLES20.glGetShaderInfoLog(vertexShaderHandle));
		Log.i("shader2", GLES20.glGetShaderInfoLog(fragmentShaderHandle));
	}	

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height)
	{
		
		_width = width;
		_height = height;
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);
		/*
		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = -1.0f;
		final float far = 1.0f;
		//Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
		Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
		*/
		
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 7.0f;

		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
		
	}	

	@Override
	public void onDrawFrame(GL10 glUnused)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);	
		GLES20.glUseProgram(mProgramHandle);
		// Do a complete rotation every 10 seconds.
		long time = SystemClock.uptimeMillis() % 10000L;
		
		for(Sprite obj : objects)
		{
			obj.sWidth = _width;
			obj.sHeight = _height;
			obj.draw(mProgramHandle, mViewMatrix, mProjectionMatrix);
		}
		for(Model3d mod: objects3d)
		{
			mod.draw(mProgramHandle, mViewMatrix, mProjectionMatrix);
		}
		//mTextureDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.apple);
	}
	
	public void addChild(Sprite spr){
		objects.add(spr);
		spr.child = objects.size()-1;
		spr.sWidth = _width;
		spr.sHeight = _height;
	}
	public void addChild(Model3d mod){
		objects3d.add(mod);
	}

}
