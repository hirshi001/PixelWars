package com.hirshi001.game.packets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.game.Player;
import com.hirshi001.networking.packet.Packet;

public class PlayerJoinedPacket extends Packet {

    public Player player;

    public PlayerJoinedPacket(Player player) {
        this.player = player;
    }

    public PlayerJoinedPacket() {
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        player.writeBytes(out);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        player = new Player(null, null, null);
        player.readBytes(in);
    }
}
