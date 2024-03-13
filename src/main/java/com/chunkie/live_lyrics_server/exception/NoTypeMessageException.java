package com.chunkie.live_lyrics_server.exception;

public class NoTypeMessageException extends RuntimeException{
    public NoTypeMessageException(){super("No specific Websocket Message type defined.");}
}
