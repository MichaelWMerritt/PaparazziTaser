package com.macantamonk.paparazziattack;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.macantamonk.paparazziattack.SceneManager.SceneType;

public class LoadingScene extends BaseScene{
	private LoadingScene scene = this;

	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		setBackground(new Background(Color.WHITE));
		attachChild(new Text(Game.WIDTH/2,Game.HEIGHT/2, resourcesManager.font, "Loading...", vbom));
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseScene() {
		// TODO Auto-generated method stub

		scene.setIgnoreUpdate(true);
	}

	@Override
	public void resumeScene() {
		// TODO Auto-generated method stub
		scene.setIgnoreUpdate(false);
	}

}
