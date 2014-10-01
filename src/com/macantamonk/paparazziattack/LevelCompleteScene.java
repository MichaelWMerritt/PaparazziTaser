package com.macantamonk.paparazziattack;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;

import com.macantamonk.paparazziattack.SceneManager.SceneType;

public class LevelCompleteScene extends BaseScene implements
		IOnMenuItemClickListener {
	
	private MenuScene menuChildScene;
	private final int CONTINUE = 0;
	private LevelCompleteScene scene = this;
	private int round; 
	private int stars;
	private TiledSprite star1;
    private TiledSprite star2;
    private TiledSprite star3;
    

	public LevelCompleteScene(int round, int stars) {
		this.round = round; 
		this.stars = stars;
	}

	
	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(CONTINUE, resourcesManager.continue_region,
						vbom), 1.2f, 1);

		menuChildScene.addMenuItem(playMenuItem);

		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(Game.WIDTH / 2, Game.HEIGHT / 5);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
		resourcesManager.bgMusic.stop();

	}

	private void createBackground() {

		setBackground(new Background(Color.WHITE));
		attachChild(new Sprite(Game.WIDTH / 2, Game.HEIGHT / 2,
				resourcesManager.complete_window_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});

        star1 = new TiledSprite(225, 225, ResourcesManager.getInstance().complete_stars_region, vbom);
        star2 = new TiledSprite(400, 225, ResourcesManager.getInstance().complete_stars_region, vbom);
        star3 = new TiledSprite(575, 225, ResourcesManager.getInstance().complete_stars_region, vbom);

        if(stars == 3){
        	star1.setCurrentTileIndex(0);
        	star2.setCurrentTileIndex(0);
        	star3.setCurrentTileIndex(0);
        }else if(stars == 2){
        	star1.setCurrentTileIndex(0);
        	star2.setCurrentTileIndex(0);
        	star3.setCurrentTileIndex(1);
        }else if(stars == 1){
        	star1.setCurrentTileIndex(0);
        	star2.setCurrentTileIndex(1);
        	star3.setCurrentTileIndex(1);
        }
        
        attachChild(star1);
        attachChild(star2);
        attachChild(star3);
 
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
		switch (pMenuItem.getID()) {
		case CONTINUE:
			SceneManager.getInstance().loadGameScene(engine, round);
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
