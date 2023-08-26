package com.hirshi001.game.server;

import com.badlogic.gdx.math.Vector2;
import com.hirshi001.game.Field;
import com.hirshi001.game.Player;
import com.hirshi001.game.packets.GameInitPacket;
import com.hirshi001.game.packets.GameStartPacket;
import com.hirshi001.game.packets.PlayerJoinedPacket;
import com.hirshi001.game.packets.SetColorPacket;
import com.hirshi001.game.settings.GameSettings;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.BooleanPacket;

public class PacketHandlers {

    public static void handleGameStartPacket(PacketHandlerContext<GameStartPacket> ctx) {
        GameStartPacket packet = ctx.packet;

        Field field = ServerLauncher.field;
        Player player = new Player(new Vector2(field.width/2F, field.height/2F), packet.team, new ServerPlayerController());
        player.channel = ctx.channel;
        field.addPlayer(player);
        ctx.channel.attach(player);
        System.out.println("Received GameStartPacket from client with team: "+packet.team);

        ctx.channel.sendTCP(new GameInitPacket(field), null).perform();
        ctx.channel.sendTCP(new BooleanPacket(true), null).perform();
        for(Player p: ServerLauncher.field.playerMap.values()){
            ctx.channel.sendTCP(new PlayerJoinedPacket(p), null).perform();
        }
    }

    public static void handleSetColorPacket(PacketHandlerContext<SetColorPacket> ctx){
        SetColorPacket packet = ctx.packet;

        GameSettings.runnablePoster.postRunnable(()-> {
            Field field = ServerLauncher.field;
            field.set(packet.x, packet.y, packet.color);
            for(Player p: field.playerMap.values()){
                if(p.channel!=null){
                    p.channel.sendTCP(packet, null).perform();
                }
            }
        });

    }

}
