package com.hirshi001.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hirshi001.game.Field;
import com.hirshi001.game.GameApp;
import com.hirshi001.game.Team;
import com.hirshi001.game.packets.SetColorPacket;
import com.hirshi001.networking.packethandlercontext.PacketType;

import java.util.HashMap;

public class GameFieldScreen extends GameScreen {

    OrthographicCamera camera;
    Viewport viewport;
    SpriteBatch batch;
    public Team team;

    public GameFieldScreen(GameApp app, Team team) {
        super(app);
        this.team = team;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        Field field = GameApp.field;

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        final float hW = field.width / 2f;
        final float hH = field.height / 2f;
        for (int x = 0; x < field.width; x++) {
            for (int y = 0; y < field.height; y++) {
                Color color = field.get(x, y);
                batch.setColor(color);
                batch.draw(app.whitePixel, x - hW, y - hH, 1, 1);
            }
        }

        Vector3 vector3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(vector3);

        batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        batch.draw(app.whitePixel, MathUtils.floor(vector3.x), MathUtils.floor(vector3.y), 1, 1);


        batch.end();

        // GameApp.client.getChannel().sendNow(new SetColorPacket(MathUtils.random(0, field.width), MathUtils.random(0, field.height), Color.RED), null, PacketType.TCP);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public void show() {
        super.show();
        viewport = new ExtendViewport(15, 15, GameApp.field.width, GameApp.field.height);
        camera = (OrthographicCamera) viewport.getCamera();
        batch = new SpriteBatch();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
            private Vector2 oldInitialFirstPointer = null, oldInitialSecondPointer = null;
            private float oldScale;

            @Override
            public boolean pinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {

                if (!(initialFirstPointer.equals(oldInitialFirstPointer) && initialSecondPointer.equals(oldInitialSecondPointer))) {
                    oldInitialFirstPointer = initialFirstPointer.cpy();
                    oldInitialSecondPointer = initialSecondPointer.cpy();
                    oldScale = camera.zoom;
                }
                Vector3 center = new Vector3(
                        (firstPointer.x + initialSecondPointer.x) / 2,
                        (firstPointer.y + initialSecondPointer.y) / 2,
                        0
                );
                zoomCamera(center, oldScale * initialFirstPointer.dst(initialSecondPointer) / firstPointer.dst(secondPointer));
                return true;
            }

            private void zoomCamera(Vector3 origin, float scale) {
                camera.update();
                Vector3 oldUnprojection = camera.unproject(origin.cpy()).cpy();
                camera.zoom = scale; //Larger value of zoom = small images, border view
                camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 10f);
                camera.update();
                Vector3 newUnprojection = camera.unproject(origin.cpy()).cpy();
                camera.position.add(oldUnprojection.cpy().add(newUnprojection.cpy().scl(-1f)));
            }
        }));
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                OrthographicCamera camera = GameFieldScreen.this.camera;
                camera.zoom += amountY / 10f;
                camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 10f);
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                final float hW = GameApp.field.width / 2f;
                final float hH = GameApp.field.height / 2f;

                Vector3 vector3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(vector3);
                vector3.add(hW, hH, 0);
                if (vector3.x < 0 || vector3.x >= GameApp.field.width || vector3.y < 0 || vector3.y >= GameApp.field.height)
                    return false;
                GameApp.client.getChannel().sendNow(new SetColorPacket((int) vector3.x, (int) vector3.y, team.getColor()), null, PacketType.TCP);
                return true;
            }
        });

        Gdx.input.setInputProcessor(multiplexer);
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
