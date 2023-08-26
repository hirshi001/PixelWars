package com.hirshi001.game.settings;

import com.hirshi001.game.util.ByteCounterPacketEncoderDecoder;
import com.hirshi001.networking.packetdecoderencoder.SimplePacketEncoderDecoder;

public class Network {

    public static final ByteCounterPacketEncoderDecoder PACKET_ENCODER_DECODER = new ByteCounterPacketEncoderDecoder(new SimplePacketEncoderDecoder(Integer.MAX_VALUE));


}
