package com.macantamonk.paparazziattack;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.macantamonk.paparazziattack.BaseScene;
import com.macantamonk.paparazziattack.SceneManager.SceneType;

public class SplashScene extends BaseScene
{
	private Sprite splash;
	private SplashScene scene = this;
	
    @Override
    public void createScene()
    {
    	splash = new Sprite(0, 0, resourcesManager.splash_region, vbom)
    	{
    	    @Override
    	    protected void preDraw(GLState pGLState, Camera pCamera) 
    	    {
    	       super.preDraw(pGLState, pCamera);
    	       pGLState.enableDither();
    	    }
    	};
    	
    	splash.setScale(1.5f);
    	splash.setPosition(Game.WIDTH/2, Game.HEIGHT/2);
    	attachChild(splash);
    }

    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneType getSceneType()
    {
    	return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene()
    {
    	splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }

	@Override
	public void pauseScene() {
		scene.setIgnoreUpdate(true);
	}

	@Override
	public void resumeScene() {
		scene.setIgnoreUpdate(false);
	}
}
