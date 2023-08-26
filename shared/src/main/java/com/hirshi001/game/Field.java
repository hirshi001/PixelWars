package com.hirshi001.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Field {

    public Color[][] field;
    public int width;
    public int height;

    public Map<Integer, Player> playerMap = new ConcurrentHashMap<>();
    private int nextId = 0;

    public Field(int width, int height){
        this.width = width;
        this.height = height;
        field = new Color[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                float r = MathUtils.random();
                if(r<0.33F){
                    field[x][y] = Color.RED;
                }
                else if(r<0.66F){
                    field[x][y] = Color.BLUE;
                }
                else{
                    field[x][y] = Color.WHITE;
                }
            }
        }
    }

    public Player getPlayer(int id){
        return playerMap.get(id);
    }

    public void addPlayer(Player player){
        player.id = nextId++;
        playerMap.put(player.id, player);
    }

    public void addPlayer(Player player, int id){
        player.id = id;
        playerMap.put(player.id, player);
    }

    public void set(int x, int y, Color color){
        field[x][y] = color;
    }

    public void setRect(int x, int y, int width, int height, Color color){
        for(int i = x; i < x+width; i++){
            for(int j = y; j < y+height; j++){
                set(i, j, color);
            }
        }
    }

    public Color get(int x, int y){
        return field[x][y];
    }


}
