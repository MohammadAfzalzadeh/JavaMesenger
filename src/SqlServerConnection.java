import Models.*;
import java.sql.*;
import java.util.ArrayList;

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
            if (ctn == 1) {
                SetIdOfOnePerson(person);
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        person.setPrsId(-1);
        return false;
    }

    public ArrayList<Groups> GetAllGroupOfOnePerson(Person person){
        ArrayList<Groups> grps = new ArrayList<>();

        String qry = "SELECT GrpId , GrpName , GrpDetail "+
                " FROM Db_Messenger.dbo.PersonToGrp_Tb AS PrsGrp" +
                " INNER JOIN Db_Messenger.dbo.Groups AS Grp "+
                " ON (Grp.GrpId = PrsGrp.GrpIdToPer)" +
                " WHERE (PrsGrp.PerIdToGrp = "+ person.getPrsId() +" )";

        try {
            ResultSet groupsResSet = runQuery(qry);
            while (groupsResSet.next())
                grps.add(
                new Groups( (long) groupsResSet.getObject(1) ,
                        (String) groupsResSet.getObject(2) ,
                        (String) groupsResSet.getObject(3) )
                );

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return grps;
    }

    private void SetIdOfOnePerson(Person person){
        String selectTopQry = "SELECT TOP 1 [PersonId] FROM [Db_Messenger].[dbo].[People]" +
                "  WHERE UserName = '"+person.getUserName()+"' and Pass = '"+person.getPass()+"'";
        try {
            ResultSet rsSet = runQuery(selectTopQry);
            rsSet.next();
            person.setPrsId( (int) rsSet.getObject(1) );

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
