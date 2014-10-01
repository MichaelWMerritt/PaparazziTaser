package com.macantamonk.paparazziattack;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.macantamonk.paparazziattack.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private MainMenuScene scene = this;

	private void createMenuChildScene()
	{
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(0, 0);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
	    
	    menuChildScene.addMenuItem(playMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(Game.WIDTH/2, Game.HEIGHT/8);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}
	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
	    resourcesManager.bgMusic.play();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
		resourcesManager.bgMusic.stop();
		
	}

	private void createBackground(){
		attachChild(new Sprite(Game.WIDTH/2,Game.HEIGHT/2,resourcesManager.menu_background_region,vbom){
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera){
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}
	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		resourcesManager.bgMusic.stop();
	}
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
        switch(pMenuItem.getID())
        {
        case MENU_PLAY:
        	SceneManager.getInstance().loadGameScene(engine, 1);
            return true;
        default:
            return false;
    }
	}
	@Override
	public void pauseScene() {
		resourcesManager.bgMusic.pause();
		scene.setIgnoreUpdate(true);
	}
	@Override
	public void resumeScene() {

		resourcesManager.bgMusic.play();
		scene.setIgnoreUpdate(false);
	}

}
