package com.macantamonk.paparazziattack;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;


public class Game extends BaseGameActivity {

	Scene scene;
	protected static int WIDTH = 800;
	protected static int HEIGHT = 480;
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTextureRegion;
	PhysicsWorld physicsWorld;
	SceneManager sceneManager;
	Camera mCamera;
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions){
		return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	@Override
	public void onResumeGame(){
		super.onResumeGame();
		SceneManager.getInstance().getCurrentScene().resumeScene();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SceneManager.getInstance().getCurrentScene().pauseScene();
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		
		mCamera = new Camera(0,0,WIDTH, HEIGHT);
		
		EngineOptions options = new EngineOptions(true, 
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
		options.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		options.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		options.getTouchOptions().setNeedsMultiTouch(true);
		options.getAudioOptions().setNeedsSound(true);
		options.getAudioOptions().getSoundOptions().setMaxSimultaneousStreams(100);
		options.getAudioOptions().setNeedsMusic(true);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {

		ResourcesManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());
		ResourcesManager.getInstance();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	        System.exit(0);	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {

		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
	    mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                SceneManager.getInstance().createMenuScene();
	            }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

}
