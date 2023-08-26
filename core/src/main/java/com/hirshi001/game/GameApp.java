package com.hirshi001.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.game.packets.GameInitPacket;
import com.hirshi001.game.packets.GameStartPacket;
import com.hirshi001.game.packets.PlayerJoinedPacket;
import com.hirshi001.game.packets.SetColorPacket;
import com.hirshi001.game.screens.FirstScreen;
import com.hirshi001.game.settings.GameSettings;
import com.hirshi001.game.settings.Network;
import com.hirshi001.networking.network.NetworkFactory;
import com.hirshi001.networking.network.channel.AbstractChannelListener;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.networkdata.DefaultNetworkData;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.networking.packetregistrycontainer.SinglePacketRegistryContainer;

import java.io.IOException;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameApp extends Game {

    public static BufferFactory bufferFactory;
    public static NetworkFactory networkFactory;

    public static Client client;
    public static Field field;

   // public static ClientField field;
    public Disposable disposeWhenClose;
    public AssetManager assetManager;
    public Skin uiSkin;

    public final String ip;
    public final int port;
    public Texture whitePixel;




    public NetworkData networkData;
    public PacketRegistryContainer container;



    public static GameApp Game(){
        return (GameApp) Gdx.app.getApplicationListener();
    }


    public GameApp(Disposable disposeWhenClose, BufferFactory bufferFactory, NetworkFactory networkFactory, String ip, int port) {
        super();
        this.disposeWhenClose = disposeWhenClose;
        this.ip = ip;
        this.port = port;
        GameApp.bufferFactory = bufferFactory;
        GameApp.networkFactory = networkFactory;
        field = new Field(GameSettings.FIELD_WIDTH, GameSettings.FIELD_HEIGHT);
    }



    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // setScreen(new FirstScreen(this));
        setScreen(new FirstScreen(this));
        try {
            setClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if(client!=null) client.close();
        if(disposeWhenClose!=null) disposeWhenClose.dispose();
    }

    private void setClient() throws IOException {
        container = new SinglePacketRegistryContainer();
        networkData = new DefaultNetworkData(Network.PACKET_ENCODER_DECODER, container);
        client = networkFactory.createClient(networkData, bufferFactory, ip, port);
        client.setChannelInitializer(channel -> {
            channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, true);
            channel.setChannelOption(ChannelOption.TCP_NODELAY, true);
        });
        client.addClientListener(new AbstractChannelListener() {
            @Override
            public void onTCPConnect(Channel channel) {
                super.onTCPConnect(channel);
                Gdx.app.debug("MainGame", "Connected to server");
            }

            @Override
            public void onTCPDisconnect(Channel channel) {
                super.onTCPDisconnect(channel);
                Gdx.app.debug("MainGame", "Disconnected from server");
            }
        });


        PacketRegistry packetRegistry = networkData.getPacketRegistryContainer().getDefaultRegistry();
        packetRegistry.registerDefaultPrimitivePackets()
            .register(GameStartPacket::new, null, GameStartPacket.class, 0)
            .register(PlayerJoinedPacket::new, PacketHandlers::handlePlayerJoinedPacket, PlayerJoinedPacket.class, 1)
            .register(GameInitPacket::new, PacketHandlers::handleGameInitPacket, GameInitPacket.class, 2)
            .register(SetColorPacket::new, PacketHandlers::handleSetColorPacket, SetColorPacket.class, 3);
        /*packetRegistry
            .register(LoginPacket::new, null, LoginPacket.class, 0)
            .register(PlayerPacket::new, PacketHandlers::handlePlayerPacket, PlayerPacket.class, 1)
            .register(MovePacket::new, PacketHandlers::handleMovePacket, MovePacket.class, 2)
            .register(ChallengePacket::new, PacketHandlers::handleChallengePacket, ChallengePacket.class, 3)
            .register(AcceptChallengePacket::new, null, AcceptChallengePacket.class, 4)
            .register(StartGamePacket::new, PacketHandlers::handleStartGamePacket, StartGamePacket.class, 5)
            .register(GameOverPacket::new, PacketHandlers::handleGameOverPacket, GameOverPacket.class, 6)
            .register(ReloginPacket::new, null, ReloginPacket.class, 7);


         */

    }

}
