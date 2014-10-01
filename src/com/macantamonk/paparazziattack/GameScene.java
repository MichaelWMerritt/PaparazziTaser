package com.macantamonk.paparazziattack;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;

import android.util.Log;

import com.macantamonk.paparazziattack.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener {
	private HUD gameHUD;
	private Text scoreText;
	private Text healthText;
	private int score = 0;
	private int health = 100;
	private PhysicsHandler physicsHandler;
	private AnimatedSprite player;
	private static final int PLAYER_SPEED = 200;
	boolean taserFire = false;
	private int round;
	private int shootTimer = 0;
	private Text gameOverText;
	private boolean gameOver = false;
	private boolean roundComplete = false;
	private GameScene scene = this;
	private ArrayList<AnimatedSprite> sprites = new ArrayList<AnimatedSprite>();
	protected int zIndex = 100;
	
	public GameScene(int round) {
		this.round = round;
	}

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

	private void reduceHealth(int i) {
		health -= i;
		healthText.setText("Health: " + health);
	}

	private void createGameOverText() {
		gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
		gameOverText.setScaleX(1.5f);
	}

	private void displayGameOverText() {
		gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
		attachChild(gameOverText);
	}

	private void createHUD() {
		gameHUD = new HUD();

		// CREATE SCORE TEXT
		scoreText = new Text(Game.WIDTH / 8, Game.HEIGHT - 30,
				resourcesManager.font, "Score: 0123456789", vbom);
		scoreText.setTextOptions(new TextOptions(HorizontalAlign.LEFT));
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);

		healthText = new Text(Game.WIDTH - 150, Game.HEIGHT - 30,
				resourcesManager.font, "Health: 0123456789", vbom);
		healthText.setTextOptions(new TextOptions(HorizontalAlign.LEFT));
		healthText.setText("Health: 100");
		gameHUD.attachChild(healthText);

		final Sprite left = new Sprite(60, 50, 60, 60,
				resourcesManager.left_region, vbom) {
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				if (touchEvent.isActionDown()
						|| (touchEvent.isActionMove() && !touchEvent
								.isActionDown())) {
					// move player left
					physicsHandler.setVelocity(-(PLAYER_SPEED), 0);
				} else {
					physicsHandler.setVelocity(0, 0);
				}
				return true;
			}
		};

		final Sprite right = new Sprite(140, 50, 60, 60,
				resourcesManager.right_region, vbom) {
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				if (touchEvent.isActionDown() || touchEvent.isActionMove()) {
					// move player right
					physicsHandler.setVelocity(PLAYER_SPEED, 0);
				} else {
					physicsHandler.setVelocity(0, 0);
				}
				return true;
			}
		};

		final Sprite fire = new Sprite(Game.WIDTH - 100, 50, 256, 256,
				resourcesManager.fire_region, vbom) {
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
				final ScaleModifier entityModifier = new ScaleModifier(1.5f,
						0.6f, 0.7f);
				if (touchEvent.isActionDown()) {
					// do firing here
					player.registerEntityModifier(entityModifier);
					player.animate(new long[]{50,50,50,50,50}, 1,5,true);
					player.setCurrentTileIndex(1);
					resourcesManager.taser.setLooping(true);
					resourcesManager.taser.play();
					taserFire = true;
				} else {
					player.clearEntityModifiers();
					player.stopAnimation();
					player.setCurrentTileIndex(0);
					player.setScale(0.6f);
					resourcesManager.taser.stop();
					taserFire = false;
				}
				return true;
			}
		};

		fire.setScale(0.4f);
		gameHUD.registerTouchArea(left);
		gameHUD.registerTouchArea(right);
		gameHUD.registerTouchArea(fire);
		gameHUD.attachChild(left);
		gameHUD.attachChild(right);
		gameHUD.attachChild(fire);
		player = createPlayer();
		physicsHandler = new PhysicsHandler(player);
		player.registerUpdateHandler(physicsHandler);

		camera.setHUD(gameHUD);
	}

	@Override
	public void createScene() {
		resourcesManager.bgMusic.setVolume(1.0f);
		createBackground();
		createHUD();
		createGameOverText();
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 1.5f;
		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						if (!gameOver && !roundComplete)
							createPaparazzi();
					}
				});
		engine.registerUpdateHandler(spriteTimerHandler);
		setOnSceneTouchListener(this);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		resourcesManager.taser.stop();
		camera.setCenter(400, 240);
	}

	private void createPaparazzi() {
		Random x = new Random();
		final int pX = x.nextInt(400) + 140;
		final AnimatedSprite sprite = new AnimatedSprite(pX, Game.HEIGHT / 4,
				resourcesManager.paparazzi_region, vbom) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (this.getScaleX() > 1.0f) {
					this.setCurrentTileIndex(1);
					if (this.getScaleX() >= 1.5f) {
						shootTimer++;
						if (shootTimer > 50) {
							this.setCurrentTileIndex(2);
							resourcesManager.shutter.play();
							shootTimer = 0;
							reduceHealth(1);
							if (health == 0) {
								gameOver = true;
								myGarbageCollection();
								displayGameOverText();
								engine.stop();
							}
						}
					}
					if ((player.getChildByIndex(0)).collidesWith(this) && taserFire) {
						final AnimatedSprite explosion = new AnimatedSprite(pX,
								Game.HEIGHT / 4,
								resourcesManager.explosion_region, vbom);
						explosion.setScale(1.5f);
						explosion.animate(25, false, new IAnimationListener() {
							@Override
							public void onAnimationFinished(
									AnimatedSprite pAnimatedSprite) {
								engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										explosion.detachSelf();
									}
								});

							}

							@Override
							public void onAnimationStarted(
									AnimatedSprite pAnimatedSprite,
									int pInitialLoopCount) {

							}

							@Override
							public void onAnimationFrameChanged(
									AnimatedSprite pAnimatedSprite,
									int pOldFrameIndex, int pNewFrameIndex) {

							}

							@Override
							public void onAnimationLoopFinished(
									AnimatedSprite pAnimatedSprite,
									int pRemainingLoopCount,
									int pInitialLoopCount) {
							}
						});
						scene.attachChild(explosion);
							resourcesManager.explosion.play();
						addToScore(1);
						this.setVisible(false);
						this.setIgnoreUpdate(true);
						int starsCount;
						if (score == round * 5) {
							if(health > 75){
								starsCount = 3;
							}else if(health > 50){
								starsCount = 2;
							}else{
								starsCount = 1;
							}
							roundComplete = true;
							resourcesManager.taser.pause();
							resourcesManager.bgMusic.setVolume(0.3f);
							myGarbageCollection();
							round++;
							SceneManager.getInstance().loadLevelCompleteScene(engine, round, starsCount);
						}
					}
				}
			}
		};

		ScaleModifier towardsPlayer = new ScaleModifier(4.5f, 0.05f, 1.5f);
		sprite.registerEntityModifier(towardsPlayer);
		sprites.add(sprite);
		attachChild(sprite);
		sprite.setZIndex(zIndex--);
		sortChildren(true);
	}

	private void myGarbageCollection() {

		if (sprites.size() > 0) {
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < sprites.size(); i++) {
						try {
							final int myI = i;
							scene.detachChild((IEntity) sprites.get(myI));
						} catch (Exception e) {
							Debug.d("SPK - THE SPRITE DOES NOT WANT TO DIE: "
									+ e);
						}
					}
				}
			});
		}
		sprites.clear();

		health = 100;
		score = 0;
		System.gc();
	}

	private AnimatedSprite createPlayer() {
		AnimatedSprite sprite = new AnimatedSprite(Game.WIDTH / 2,
				Game.HEIGHT / 9, resourcesManager.player_region, vbom);
		sprite.setScale(.6f);
		sprite.setCurrentTileIndex(0);
		final Rectangle playerCenter = new Rectangle(sprite.getWidth() / 3,
				sprite.getHeight() - 20, 20, 20, vbom);
		playerCenter.setVisible(false);
		sprite.attachChild(playerCenter);
		gameHUD.attachChild(sprite);
		return sprite;
	}

	private void createBackground() {
		Log.d("width", "" + Game.WIDTH);
		Log.d("height", "" + Game.HEIGHT);

		attachChild(new Sprite(Game.WIDTH / 2, Game.HEIGHT / 2,
				resourcesManager.game_background_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {

		}
		return false;
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