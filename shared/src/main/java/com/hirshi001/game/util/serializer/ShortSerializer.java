package com.hirshi001.game.util.serializer;

import com.hirshi001.buffer.buffers.ByteBuffer;

public class ShortSerializer implements ByteBufSerializer<Short> {
    @Override
    public void serialize(Short object, ByteBuffer buffer) {
        buffer.writeShort(object);
    }

    @Override
    public Short deserialize(ByteBuffer buffer) {
        return buffer.readShort();
    }

}
