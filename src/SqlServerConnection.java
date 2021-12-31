import Models.*;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;

import java.sql.*;

public class SqlServerConnection {
    private Connection connection;
    private Statement statement;

    public SqlServerConnection()  {

        String url = "jdbc:sqlserver://localhost;databaseName=Db_Messenger;";

        String user = "msg";
        String pass = "asd";

        try{
            connection = DriverManager.getConnection(url , user , pass);
            statement = connection.createStatement();
            /*statement.executeQuery(insertQuery);*/
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void SignInPerson(Person person){
        String insertQuery = "INSERT INTO [dbo].[People]([FullName],[UserName],[Pass],[Email])" +
                "VALUES('"+person.getFullName()+"','"+person.getUserName()+"','"+person.getPass()+"','"+person.getEmail()+"')";
        runQuery(insertQuery);
    }

    public boolean LogInPerson(Person person){
        String countQry = "SELECT COUNT(*) FROM [Db_Messenger].[dbo].[People]" +
                "  WHERE UserName = '"+person.getUserName()+"' and Pass = '"+person.getPass()+"'";
        try {
            ResultSet rsSet = runQuery(countQry);
            rsSet.next();
            int ctn = (int) rsSet.getObject(1);
            if (ctn == 1)
                return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private ResultSet runQuery(String qry){
        try {
            return statement.executeQuery(qry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
