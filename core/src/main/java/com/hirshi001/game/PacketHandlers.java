package com.hirshi001.game;

import com.badlogic.gdx.Gdx;
import com.hirshi001.game.packets.GameInitPacket;
import com.hirshi001.game.packets.PlayerJoinedPacket;
import com.hirshi001.game.packets.SetColorPacket;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public class PacketHandlers {

    public static void handlePlayerJoinedPacket(PacketHandlerContext<PlayerJoinedPacket> ctx) {
        Field field = GameApp.field;
        Gdx.app.postRunnable(()-> field.addPlayer(ctx.packet.player, ctx.packet.player.id));
    }

    public static void handleGameInitPacket(PacketHandlerContext<GameInitPacket> ctx) {
        Field field = GameApp.field;
        Gdx.app.postRunnable(()-> {
            for(int i = 0; i < field.field.length; i++){
                for(int j = 0; j < field.field[i].length; j++){
                    field.field[i][j] = ctx.packet.colors[i][j];
                }
            }
        });
    }

    public static void handleSetColorPacket(PacketHandlerContext<SetColorPacket> ctx){
        SetColorPacket packet = ctx.packet;

        Field field = GameApp.field;
        field.set(packet.x, packet.y, packet.color);
    }
}
