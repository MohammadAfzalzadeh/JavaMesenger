package Models;

import java.util.Random;

public class Groups {
    private long gId;
    private String name;
    private String detail;
    private String key;

    public Groups(long groupId , String name, String detail) {
       this(groupId , name , detail , getRandomString(10));
    }

    public Groups(long groupId, String name, String detail, String key) {
        gId = groupId;
        this.name = name;
        if (detail == "")
            this.detail = null;
        else
            this.detail = detail;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        if (detail == null)
            return "";
        return detail;
    }

    public long getGrpId() {
        return gId;
    }

    public String getKey() {
        return key;
    }

    public static String getRandomString(int len){
        String str = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "1234567890" +"!@#$%^&*_-+=";
        String res = "";
        Random rand = new Random();
        for (int i = 0; i < len; i++)
            res += str.charAt(rand.nextInt(74));

        return res;
    }
}
