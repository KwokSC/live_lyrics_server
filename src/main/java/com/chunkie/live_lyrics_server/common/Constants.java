package com.chunkie.live_lyrics_server.common;

/**
 * @Description:
 * @ClassName: Constants
 * @Author: SichengGuo
 * @Date: 2021/12/14 17:22
 * @Version: 1.0
 */
public interface Constants {

    interface Msg {
        String SUCCESS = "Successful request";
        String FAIL = "Fail request";
        String NEW = "New User";
    }

    interface Code{
        Integer NORMAL = 200;
        Integer EXCEPTION = 500;
    }

    interface AWS{
        String BUCKET = "live-lyrics-bucket";
    }

    interface SQUARE{
        String TOKEN = "EAAAl6069mjeaMaICWaPziRJfZ_grIOIPp0mpBNbab236GmUisW1EuO_6Wk3oNlv";
    }

    interface Auth{
        long EXP = 30;
    }

    interface MsgType{
        // PLAYER: message related to music player.
        // e.g. play, pause, change the song etc.
        String PLAYER = "PLAYER";
        // ROOM: message related to live status.
        // e.g. if the room is online and active user count.
        String ROOM = "ROOM";
        // CHAT: message for user chatting in the room.
        String CHAT = "CHAT";

        String SUBSCRIBE = "SUBSCRIBE";
        String USER_ENTER = "USER ENTER";
        String USER_EXIT = "USER EXIT";
    }

}
