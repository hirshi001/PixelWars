package com.hirshi001.game.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hirshi001.game.GameApp;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen extends GameScreen {

	SpriteBatch batch;

	public FirstScreen(GameApp app) {
		super(app);

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		if(app.assetManager == null){
            app.assetManager = new AssetManager();
            app.assetManager.load("ui/uiskin.json", Skin.class);

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            app.whitePixel = new Texture(pixmap);
		}

	}

	@Override
	public void render(float delta) {
		// Draw your screen here. "delta" is the time since last render in seconds.
		// batch.enableBlending();
		ScreenUtils.clear(Color.WHITE);
        app.assetManager.update();
        if(app.assetManager.isFinished()){
            app.uiSkin = app.assetManager.get("ui/uiskin.json", Skin.class);
            System.out.println("Finished loading assets, connecting to server now");
            app.setScreen(new ConnectScreen(app));
        }
	}

	@Override
	public void resize(int width, int height) {
		// Resize your screen here. The parameters represent the new window size.
	}

	@Override
	public void pause() {
		// Invoked when your application is paused.
	}

	@Override
	public void resume() {
		// Invoked when your application is resumed after pause.
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		// Destroy screen's assets here.
		batch.dispose();
	}
}
