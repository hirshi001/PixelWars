package com.hirshi001.game.packets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.game.Team;
import com.hirshi001.networking.packet.Packet;

public class GameStartPacket extends Packet {

    public Team team;

    public GameStartPacket() {
        super();
    }

    public GameStartPacket(Team team){
        this.team = team;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        out.writeByte(team.ordinal());
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        team = Team.values()[in.readByte()];
    }
}
