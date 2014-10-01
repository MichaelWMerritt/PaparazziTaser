package com.macantamonk.paparazziattack;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

public class ResourcesManager {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;

	public Engine engine;
	public Game activity;
	public Camera camera;

	public VertexBufferObjectManager vbom;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITiledTextureRegion paparazzi_region;
	public ITextureRegion left_region;
	public ITextureRegion right_region;
	public ITextureRegion fire_region;
	public ITiledTextureRegion player_region;
	public Sound shutter, taser, explosion;
	public Music bgMusic;
	public ITextureRegion complete_window_region;
	public ITextureRegion continue_region;
	public ITiledTextureRegion complete_stars_region;

	private BuildableBitmapTextureAtlas menuTextureAtlas;

	public ITextureRegion game_background_region;

	private BuildableBitmapTextureAtlas gameTextureAtlas;
	public Font font;
	public ITiledTextureRegion explosion_region;

	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 50, true,
				Color.WHITE, 2, Color.RED);
		font.load();
	}

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}
	
	public void loadLevelCompleteResources(){
		loadLevelCompleteGraphics();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity,
						"menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "play.png");
		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadLevelCompleteGraphics(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/levelComplete/");

		complete_window_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity,
						"levelCompleteWindow.png");
		complete_stars_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity, "star.png",
						2, 1);
		continue_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "Continue.gif");

		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.gameTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
	}
	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}

	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}

	public void unloadGameTextures() {
		gameTextureAtlas.unload();
	}

	private void loadMenuAudio() {
		try {
			bgMusic = MusicFactory.createMusicFromAsset(
					activity.getMusicManager(), activity, "sounds/bgMusic.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR);
		game_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "bg.png");
		player_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity, "taser.png",
						3, 2);
		left_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "leftArrow.png");
		right_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "rightArrow.png");
		fire_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "fireButton2.png");
		paparazzi_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity,
						"paparazzi.png", 3, 1);
		explosion_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity,
						"explosion.png", 8, 9);

		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.gameTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadGameFonts() {

	}

	private void loadGameAudio() {
		try {
			shutter = SoundFactory.createSoundFromAsset(
					activity.getSoundManager(), activity,
					"sounds/cameraShutter.mp3");
			taser = SoundFactory.createSoundFromAsset(
					activity.getSoundManager(), activity, "sounds/taser.mp3");
			explosion = SoundFactory.createSoundFromAsset(
					activity.getSoundManager(), activity,
					"sounds/explosion.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "mbluemlogo.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare
	 *            Resources Manager properly, setting all needed parameters, so
	 *            we can later access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, Game activity,
			Camera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
}
