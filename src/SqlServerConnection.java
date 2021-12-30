import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlServerConnection {
    private Connection connection;

    public SqlServerConnection()  {

        String url = "jdbc:sqlserver://localhost;databaseName=Db_Messenger;";

        String user = "msg";
        String pass = "asd";
        String insertQuery = "INSERT INTO [dbo].[People]([FullName],[UserName],[Pass],[Email])" +
                "VALUES('1محمد جدید','MoHoko1','alialiali123','moho@123.com')";

        try{
            connection = DriverManager.getConnection(url , user , pass);
            Statement statement = connection.createStatement();
            statement.executeQuery(insertQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
