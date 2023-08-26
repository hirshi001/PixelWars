package com.hirshi001.game.packets;

import com.badlogic.gdx.graphics.Color;
import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

public class SetColorPacket extends Packet {

        public int x, y;
        public Color color;

        public SetColorPacket() {
        }

        public SetColorPacket(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(color.toIntBits());

    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        x = in.readInt();
        y = in.readInt();
        color = new Color(in.readInt());
    }
}
