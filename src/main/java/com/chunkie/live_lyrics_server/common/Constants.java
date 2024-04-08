package com.chunkie.live_lyrics_server.common;

public interface Constants {

    interface Msg {

    }

    interface Code{

    }

    interface AWS{
        String BUCKET = "live-lyrics-bucket";
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

        String USER_ENTER = "USER ENTER";
        String USER_EXIT = "USER EXIT";
    }

}
