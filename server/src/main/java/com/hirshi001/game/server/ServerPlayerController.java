package com.hirshi001.game.server;

import com.hirshi001.game.Controller;

public class ServerPlayerController implements Controller {

    private int direction;
    @Override
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
