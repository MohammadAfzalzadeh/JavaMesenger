package Models;

public class Groups {
    private String name;
    private String detail;

    public Groups(String name, String detail) {
        this.name = name;
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
}
