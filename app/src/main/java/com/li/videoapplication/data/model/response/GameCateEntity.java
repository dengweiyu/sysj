package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


public class GameCateEntity extends BaseResponseEntity {

    private List<Game> eventsData;
    private List<Game> gameData;

    public List<Game> getEventsData() {
        return eventsData;
    }

    public void setEventsData(List<Game> eventsData) {
        this.eventsData = eventsData;
    }

    public List<Game> getGameData() {
        return gameData;
    }

    public void setGameData(List<Game> gameData) {
        this.gameData = gameData;
    }
}
