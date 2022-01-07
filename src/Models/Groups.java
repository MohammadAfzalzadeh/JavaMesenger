package Models;

public class Groups {
    private long gId;
    private String name;
    private String detail;

    public Groups(long groupId , String name, String detail) {
        this.name = name;
        gId = groupId;
        if (detail == "")
            this.detail = null;
        else
            this.detail = detail;
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
}
