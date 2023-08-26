package com.hirshi001.game.packets;

import com.badlogic.gdx.graphics.Color;
import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.game.Field;
import com.hirshi001.networking.packet.Packet;

public class GameInitPacket extends Packet {

    public Color[][] colors;

    public GameInitPacket() {
        super();
    }

    public GameInitPacket(Field field){
        this.colors = field.field;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeInt(colors.length);
        out.writeInt(colors[0].length);
        for(int i = 0; i < colors.length; i++){
            for(int j = 0; j < colors[i].length; j++){
                out.writeInt(colors[i][j].toIntBits());
            }
        }
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        int width = in.readInt();
        int height = in.readInt();
        colors = new Color[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                colors[i][j] = new Color(in.readInt());
            }
        }
    }
}
