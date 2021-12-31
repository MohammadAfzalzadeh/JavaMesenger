package Models;

public class Person {
    private String fullName;
    private String userName;
    private String Pass;
    private String Email;

    public Person(String userName, String pass) {
        this(null , userName , pass , null);
    }

    public Person(String fullName, String userName, String pass, String email) {
        this.fullName = fullName;
        this.userName = userName;
        Pass = pass;
        Email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return Pass;
    }

    public String getEmail() {
        return Email;
    }
}
