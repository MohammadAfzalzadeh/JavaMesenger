package Models;

public class Message {
    private String txt;
    private String date;
    private String fullNameOfSender;

    public Message(String txt, java.sql.Timestamp date, String fullNameOfSender) {
        this.txt = txt;
        if (date == null)
            this.date = "";
        else
            this.date = date.toString();
        this.fullNameOfSender = fullNameOfSender;
    }

    @Override
    public String toString() {
        return  fullNameOfSender +  " :\t" + txt + "\n" + date;
    }
}
