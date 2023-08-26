package com.hirshi001.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.packet.ByteBufSerializable;

public class Player implements ByteBufSerializable {

    public Team team;
    public int id;
    public Controller controller;

    public Field field;
    public Vector2 position; // center of player which is 1x1

    public Channel channel;


    public Player(Vector2 position, Team team, Controller controller) {
        this.position = position;
        this.team = team;
        this.controller = controller;
    }

    public void tick(float delta) {
        int dir = controller.getDirection();
        Vector2 dirVec = new Vector2(0, 0);
        if (dir == Controller.UP) {
            dirVec.y++;
        } else if (dir == Controller.DOWN) {
            dirVec.y--;
        } else if (dir == Controller.LEFT) {
            dirVec.x--;
        } else if (dir == Controller.RIGHT) {
            dirVec.x++;
        }

        dirVec.setLength(delta);
        position.add(dirVec);
        field.set((int) position.x, (int) position.y, team.getColor());
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        buffer.writeFloat(position.x);
        buffer.writeFloat(position.y);
        buffer.writeInt(team.ordinal());
        buffer.writeInt(id);
    }

    @Override
    public void readBytes(ByteBuffer buffer) {
        position = new Vector2(buffer.readFloat(), buffer.readFloat());
        team = Team.values()[buffer.readInt()];
        id = buffer.readInt();
    }
}
