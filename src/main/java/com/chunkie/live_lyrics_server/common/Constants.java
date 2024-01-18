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
        String BUCKET = "pmp-server-bucket";
    }

    interface Auth{

        long EXP = 30;
    }

}
