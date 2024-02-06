package com.chunkie.live_lyrics_server.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {

    private Object data;

    private int code;

    private String msg;

    public static ResponseObject success(Object data, String msg){
        return new ResponseObject(data, HttpStatus.OK.value(), msg);
    }

    public static ResponseObject fail(Object data, String msg){
        return new ResponseObject(data, HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }
}
