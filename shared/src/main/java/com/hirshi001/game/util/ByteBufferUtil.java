package com.hirshi001.game.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hirshi001.buffer.buffers.ByteBuffer;

public class ByteBufferUtil {

    public static void writeRectangle(ByteBuffer out, Rectangle rectangle) {
        out.writeFloat(rectangle.x);
        out.writeFloat(rectangle.y);
        out.writeFloat(rectangle.width);
        out.writeFloat(rectangle.height);
    }

    public static void readRectangle(ByteBuffer in, Rectangle rectangle) {
        rectangle.x = in.readFloat();
        rectangle.y = in.readFloat();
        rectangle.width = in.readFloat();
        rectangle.height = in.readFloat();
    }

    public static void writeVector2(ByteBuffer out, Vector2 vector) {
        out.writeFloat(vector.x);
        out.writeFloat(vector.y);
    }

    public static void readVector2(ByteBuffer in, Vector2 vector) {
        vector.x = in.readFloat();
        vector.y = in.readFloat();
    }


}
