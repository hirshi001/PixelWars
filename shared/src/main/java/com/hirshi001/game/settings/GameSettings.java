package com.hirshi001.game.settings;

import com.badlogic.gdx.math.Vector2;
import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.game.util.RunnablePoster;
import com.hirshi001.game.util.props.PropertiesManager;
import com.hirshi001.game.util.serializer.*;

public class GameSettings {

    public static RunnablePoster runnablePoster;
    public static final int TICKS_PER_SECOND = 20;
    public static final float SECONDS_PER_TICK = 1F / TICKS_PER_SECOND;
    public static final int FIELD_WIDTH = 100, FIELD_HEIGHT = 100;
    public static BufferFactory BUFFER_FACTORY;

    public static final PropertiesManager MANAGER = new PropertiesManager();

    public static void registerSerializers() {
        MANAGER.register(Boolean.class, new BooleanSerializer(), 0);
        MANAGER.register(Byte.class, new ByteSerializer(), 1);
        MANAGER.register(Short.class, new ShortSerializer(), 2);
        MANAGER.register(Integer.class, new IntegerSerializer(), 3);
        MANAGER.register(Long.class, new LongSerializer(), 4);
        MANAGER.register(Double.class, new DoubleSerializer(), 5);
        MANAGER.register(Float.class, new FloatSerializer(), 6);
        MANAGER.register(String.class, new StringSerializer(), 7);
        MANAGER.register(Vector2.class, new VectorSerializer(), 8);
    }


}
