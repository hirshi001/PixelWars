package com.hirshi001.game.screens;

import com.hirshi001.game.GameApp;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.restapi.RestFuture;

public class ConnectScreen extends GameScreen {

    RestFuture connectFuture;

    public ConnectScreen(GameApp app) {
        super(app);
        connectFuture = GameApp.client.startTCP().perform();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(connectFuture.isSuccess()){
            if(GameApp.client.supportsUDP()) GameApp.client.startUDP().perform();
            app.setScreen(new LobbyScreen(app));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
