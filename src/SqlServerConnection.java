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

        String qry = "SELECT GrpId , GrpName , GrpDetail , KeyOfGrp "+
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
                        (String) groupsResSet.getObject(3),
                        (String) groupsResSet.getObject(4) )
                );

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return grps;
    }

    public boolean JoinExGrp(String grpName , Person person){
        if (IsExGrp(grpName) && IsPersonInGrp(grpName, person) ) {
            String qry ="use Db_Messenger "+
                        "INSERT INTO PersonToGrp_Tb" +
                        " SELECT  GrpId , "+person.getPrsId()+", GETDATE ()" +
                        " FROM Groups WHERE GrpName = '"+ grpName +"'";
            runQuery(qry);
            return true;
        }
        return false;
    }

    public boolean addNewGrp(String grpName , Person person){
        if (!IsExGrp(grpName)) {
            String qry = "INSERT INTO [Db_Messenger].[dbo].[Groups] " +
                    " ([GrpName],[GrpOwnerId],[KeyOfGrp]) "+
                    " VALUES ('"+grpName+"',"+person.getPrsId()+" , '"+Groups.getRandomString(10)+"')";
            runQuery(qry);
            JoinExGrp(grpName , person);
            return true;
        }
        return false;
    }

    public void SendMessage(String msg , Groups g , Person p){
        String msgEncrypt = AES.encrypt(msg , g.getKey());
        String qry = "INSERT INTO  [Db_Messenger].[dbo].[Messages] " +
                "([MsgTxt],[MsgDate],[MsgOwnerPersonId],[MsgOwnerGrpId]) " +
                "VALUES ('" + msgEncrypt + "' , GETDATE() , "+ p.getPrsId() +" , "+ g.getGrpId() +")";
        runQuery(qry);
    }

    public ArrayList<Message> GetAllMsgOfOneGrp(Groups group){
        ArrayList <Message> Msgs = new ArrayList<Message>();

        String qry = "SELECT MsgTxt , MsgDate , FullName " +
                " FROM [Db_Messenger].[dbo].[Messages] AS Msg " +
                " INNER JOIN [Db_Messenger].[dbo].People AS Prn " +
                " ON Msg.MsgOwnerPersonId = Prn.PersonId " +
                " WHERE Msg.MsgOwnerGrpId = " + group.getGrpId();

        try {
            ResultSet MsgsResSet = runQuery(qry);
            while (MsgsResSet.next())
                Msgs.add( new Message(
                        AES.decrypt((String) MsgsResSet.getObject(1) , group.getKey()) , //txt
                        (java.sql.Timestamp) MsgsResSet.getObject(2) , //date
                        (String) MsgsResSet.getObject(3)  //fullname
                ));

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return Msgs;
    }

    private boolean IsExGrp(String grpName){
        String countQry = "SELECT COUNT (*) FROM Db_Messenger.dbo.Groups" +
                " WHERE GrpName = '"+ grpName +"'";
        try {
            ResultSet rsSet = runQuery(countQry);
            rsSet.next();
            int ctn = (int) rsSet.getObject(1);
            if (ctn == 1) return true;
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return false;
    }

    //return val should correct
    private boolean IsPersonInGrp (String grpName , Person person){
        String cntQry = "SELECT COUNT(*)  FROM Db_Messenger.dbo.PersonToGrp_Tb As link" +
                " INNER JOIN Db_Messenger.dbo.Groups As grp on grp.GrpId = link.GrpIdToPer" +
                " WHERE grp.GrpName = '"+grpName+"' and link.PerIdToGrp = " + person.getPrsId();
        try {
            ResultSet rsSet = runQuery(cntQry);
            rsSet.next();
            int ctn = (int) rsSet.getObject(1);
            if (ctn == 0)  return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
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
            if (throwables.getMessage() == "The statement did not return a result set.");
            else
            throwables.printStackTrace();

        }
        return null;
    }

}
