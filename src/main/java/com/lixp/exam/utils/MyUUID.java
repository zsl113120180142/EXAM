package com.lixp.exam.utils;

import java.util.UUID;

public class MyUUID {
    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }


}
