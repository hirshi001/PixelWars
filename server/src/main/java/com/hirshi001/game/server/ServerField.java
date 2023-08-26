package com.hirshi001.game.server;

import com.badlogic.gdx.graphics.Color;
import com.hirshi001.game.Field;
import com.hirshi001.networking.network.server.Server;

public class ServerField extends Field {

    public Server server;

    public ServerField(Server server, int width, int height) {
        super(width, height);
        this.server = server;
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                set(x, y, Color.WHITE);
            }
        }
    }

    /*
    @Override
    public void tick(float delta) {
        Iterator<PlayerData> iterator = players.iterator();
        while (iterator.hasNext()) {
            PlayerData playerData = iterator.next();
            if (playerData.channel.isClosed()) {
                for (Point point : playerData.trackedChunks) {
                    ServerChunk chunk = (ServerChunk) getChunk(point);
                    chunk.trackers.remove(playerData);
                    chunk.softTrackers.remove(playerData);
                }
                iterator.remove();
            }
        }
        super.tick(delta);

        for (GamePiece gamePiece : getItems()) {
            ServerChunk chunk = (ServerChunk) gamePiece.chunk;
            if (!gamePiece.isStatic()) {
                for (PlayerData playerData : chunk.trackers) {
                    playerData.channel.sendUDP(new SyncPacket(time, gamePiece), null).perform();
                }
                for (PlayerData playerData : chunk.softTrackers) {
                    playerData.channel.sendUDP(new SyncPacket(time, gamePiece), null).perform();
                }
            }

            Properties properties = gamePiece.getProperties();
            List<String> modified = properties.getModifiedProperties();
            for (String key : modified) {
                if(properties.getId(key)==null) continue;
                for (PlayerData playerData : chunk.trackers)
                    playerData.channel.sendTCP(new PropertyPacket(gamePiece.getGameId(), properties.getId(key), properties.get(key)), null).perform();
                for (PlayerData playerData : chunk.softTrackers)
                    playerData.channel.sendTCP(new PropertyPacket(gamePiece.getGameId(), properties.getId(key), properties.get(key)), null).perform();
            }
            modified.clear();
        }
    }

     */
}
