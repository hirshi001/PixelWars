package com.hirshi001.game.server;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.buffer.bufferfactory.DefaultBufferFactory;
import com.hirshi001.game.Player;
import com.hirshi001.game.packets.GameInitPacket;
import com.hirshi001.game.packets.GameStartPacket;
import com.hirshi001.game.packets.PlayerJoinedPacket;
import com.hirshi001.game.packets.SetColorPacket;
import com.hirshi001.game.settings.GameSettings;
import com.hirshi001.game.settings.Network;
import com.hirshi001.javanetworking.JavaNetworkFactory;
import com.hirshi001.javarestapi.JavaRestFutureFactory;
import com.hirshi001.networking.network.NetworkFactory;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.server.AbstractServerListener;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.network.server.ServerListener;
import com.hirshi001.networking.networkdata.DefaultNetworkData;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.networking.packetregistrycontainer.SinglePacketRegistryContainer;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.BooleanPacket;
import com.hirshi001.restapi.RestAPI;
import com.hirshi001.restapi.ScheduledExec;
import com.hirshi001.websocketnetworkingserver.WebsocketServer;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Launches the server application.
 */
public class ServerLauncher {

    public static NetworkFactory networkFactory;
    public static BufferFactory bufferFactory;
    public static ServerField field;
    public static ScheduledExecutorService executorService;
    public static final Array<Runnable> runnables = new Array<Runnable>(), executingRunnables = new Array<Runnable>();

    private static String KEYSTORE_PASSWORD;

    private static final Set<Class> watchedPackets = Collections.synchronizedSet(new HashSet<>());
    public static void main(String[] args) throws Exception {

        if (args.length >= 1) {
            KEYSTORE_PASSWORD = args[0];
        }

        int websocketPort = 443;
        int javaPort = 3000;

        RestAPI.setFactory(new JavaRestFutureFactory());
        executorService = Executors.newScheduledThreadPool(3);
        networkFactory = new JavaNetworkFactory(executorService);
        bufferFactory = new DefaultBufferFactory();
        GameSettings.BUFFER_FACTORY = bufferFactory;
        startServer(websocketPort, javaPort);

    }

    public static void startServer(int websocketPort, int javaPort) throws IOException, ExecutionException, InterruptedException {


        GameSettings.runnablePoster = ServerLauncher::postRunnable;
        GameSettings.registerSerializers();


        PacketRegistryContainer registryContainer = new SinglePacketRegistryContainer();
        registryContainer.getDefaultRegistry().registerDefaultPrimitivePackets()
            .register(GameStartPacket::new, PacketHandlers::handleGameStartPacket, GameStartPacket.class, 0)
            .register(PlayerJoinedPacket::new, null, PlayerJoinedPacket.class, 1)
            .register(GameInitPacket::new, null, GameInitPacket.class, 2)
            .register(SetColorPacket::new, PacketHandlers::handleSetColorPacket, SetColorPacket.class, 3);
            /*
                .register(TrackChunkPacket::new, PacketHandlers::trackChunkHandle, TrackChunkPacket.class, 0)
                .register(ChunkPacket::new, null, ChunkPacket.class, 1)
                .register(JoinGamePacket::new, PacketHandlers::joinGameHandle, JoinGamePacket.class, 2)
                .register(GameInitPacket::new, null, GameInitPacket.class, 3)
                .register(GamePieceSpawnPacket::new, null, GamePieceSpawnPacket.class, 4)
                .register(GamePieceDespawnPacket::new, null, GamePieceDespawnPacket.class, 5)
                .register(SyncPacket::new, null, SyncPacket.class, 6)
                .register(PropertyPacket::new, null, PropertyPacket.class, 7)
                .register(RequestPropertyNamePacket::new, PacketHandlers::handleRequestPropertyNamePacket, RequestPropertyNamePacket.class, 8)
                .register(PropertyNamePacket::new, null, PropertyNamePacket.class, 9)
                .register(MaintainConnectionPacket::new, null, MaintainConnectionPacket.class, 10)
                .register(PingPacket::new, (ctx) -> ctx.channel.sendNow(ctx.packet.setResponsePacket(ctx.packet), null, ctx.packetType), PingPacket.class, 11)
                .register(TroopGroupPacket::new, PacketHandlers::handleTroopGroupPacket, TroopGroupPacket.class, 12);

             */

        NetworkData networkData = new DefaultNetworkData(Network.PACKET_ENCODER_DECODER, registryContainer);

        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            public void initChannel(Channel channel) {
                channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, true);
                channel.setChannelOption(ChannelOption.UDP_AUTO_FLUSH, true);
                // channel.setChannelOption(ChannelOption.PACKET_TIMEOUT, TimeUnit.SECONDS.toMillis(10));
                channel.setChannelOption(ChannelOption.DEFAULT_SWITCH_PROTOCOL, true);
            }
        };

        ServerListener serverListener = new AbstractServerListener() {
            @Override
            public void onClientConnect(Server server, Channel channel) {
                System.out.println("Client connected " + System.identityHashCode(channel) + " : " + Arrays.toString(channel.getAddress()) + " : " + channel.getPort());

            }

            @Override
            public void onClientDisconnect(Server server, Channel channel) {
                System.out.println("Client disconnected " + System.identityHashCode(channel) + " : " + Arrays.toString(channel.getAddress()) + " : " + channel.getPort());
            }

            @Override
            public void onReceived(PacketHandlerContext<?> context) {
                // if(watchedPackets.contains(context.packet.getClass())){
                    System.out.println("Received packet: " + context.packet + " from " + Arrays.toString(context.channel.getAddress()) + " on " + context.packetType);
                // }
            }

            @Override
            public void onSent(PacketHandlerContext<?> context) {
               // if(watchedPackets.contains(context.packet.getClass())){
                    System.out.println("Received packet: " + context.packet + " from " + Arrays.toString(context.channel.getAddress()) + " on " + context.packetType);
                // }
            }
        };

        WebsocketServer websocketServer = new WebsocketServer(new ScheduledExec() {
            @Override
            public void run(Runnable runnable, long delay) {
                executorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
            }

            @Override
            public void run(Runnable runnable, long delay, TimeUnit period) {
                executorService.schedule(runnable, delay, period);
            }

            @Override
            public void runDeferred(Runnable runnable) {
                executorService.execute(runnable);
            }
        }, networkData, bufferFactory, websocketPort); // networkFactory.createServer(networkData, bufferFactory, port);

        websocketServer.setChannelInitializer(channelInitializer);
        websocketServer.addServerListener(serverListener);
        setSSL(websocketServer);
        websocketServer.startTCP().onFailure(Throwable::printStackTrace).perform().get();

        System.out.println("WebsocketServer started on " + websocketServer.getPort());

        Server javaServer = networkFactory.createServer(networkData, bufferFactory, javaPort);
        javaServer.setChannelInitializer(channelInitializer);
        javaServer.addServerListener(serverListener);
        javaServer.startTCP().perform().get();
        javaServer.startUDP().perform().get();

        System.out.println("JavaServer started on " + javaServer.getPort());

        field = new ServerField(websocketServer, GameSettings.FIELD_WIDTH, GameSettings.FIELD_HEIGHT);

        Timer timer = new Timer();
        final long period = TimeUnit.SECONDS.toMillis(1) / GameSettings.TICKS_PER_SECOND;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    synchronized (runnables) {
                        executingRunnables.clear();
                        executingRunnables.addAll(runnables);
                        runnables.clear();
                    }
                    for (Runnable runnable : executingRunnables) {
                        runnable.run();
                    }

                    // field.tick(GameSettings.SECONDS_PER_TICK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, period);

        while (true) {
            Thread.sleep(1000);

            synchronized (javaServer.getClients().getLock()) {
                Iterator<Channel> iterator = javaServer.getClients().iterator();
                while (iterator.hasNext()) {
                    Channel channel = iterator.next();
                    if (channel.isClosed()) {
                        iterator.remove();
                    } else {
                    }
                    channel.sendTCP(new BooleanPacket(true), null).onFailure((t)->{
                        channel.close();
                        iterator.remove();
                    }).perform().get();
                }
            }

            synchronized (websocketServer.getClients().getLock()) {
                Iterator<Channel> iterator = javaServer.getClients().iterator();
                while (iterator.hasNext()) {
                    Channel channel = iterator.next();
                    if (channel.isClosed()) {
                        iterator.remove();
                    } else {
                    }
                    channel.sendTCP(new BooleanPacket(true), null).onFailure((t)->{
                        channel.close();
                        iterator.remove();
                    }).perform().get();
                }
            }
        }
    }

    private static void setSSL(WebsocketServer websocketServer) {
        try {
            final String password = KEYSTORE_PASSWORD;
            final char[] passwordChars = password.toCharArray();

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("cert.jks"), passwordChars);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, passwordChars);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keyStore);


            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            websocketServer.setWebsocketSocketServerFactory(new DefaultSSLWebSocketServerFactory(sslContext));
        } catch (Exception e) {
            System.err.println("Failed to set SSL");
            e.printStackTrace();
        }
    }

    public static void postRunnable(Runnable runnable) {
        synchronized (runnables) {
            runnables.add(runnable);
        }
    }
}
