package Models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Person {
    private int prsId;
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
        Pass = hash(pass);
        Email = email;
        this.prsId = -1;
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

    public void setPrsId(int prsId) {
        this.prsId = prsId;
    }

    public int getPrsId() {
        return prsId;
    }

    public static String hash(String input){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
            md.update(input.getBytes());
            byte[] output = md.digest();
            return bytesToHex(output);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String bytesToHex(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buf = new StringBuffer();
        for (int j=0; j<b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }
}
