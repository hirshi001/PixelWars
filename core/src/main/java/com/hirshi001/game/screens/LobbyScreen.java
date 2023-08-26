package com.hirshi001.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hirshi001.game.GameApp;
import com.hirshi001.game.Team;
import com.hirshi001.game.packets.GameStartPacket;

import javax.naming.ldap.Control;

public class LobbyScreen extends GameScreen{

    Stage stage;
    public LobbyScreen(GameApp app) {
        super(app);
        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        TextButton redTeamButton = new TextButton("Red Team", app.uiSkin);
        redTeamButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                app.setScreen(new GameFieldScreen(app, Team.RED));
                app.client.getChannel().sendTCP(new GameStartPacket(Team.RED), null).perform();
            }
        });
        table.add(redTeamButton).padRight(10F).fill();


        TextButton blueTeamButton = new TextButton("Blue Team", app.uiSkin);
        blueTeamButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                app.setScreen(new GameFieldScreen(app, Team.BLUE));
                app.client.getChannel().sendTCP(new GameStartPacket(Team.BLUE), null).perform();
            }
        });
        table.add(blueTeamButton).padRight(10F).fill();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(Color.GRAY);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
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
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
