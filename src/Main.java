import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import Models.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {

    private SqlServerConnection sql;
    private AtomicReference<Groups> defGrp;

    @Override
    public void start(Stage primaryStage) {
        sql = new SqlServerConnection();
        defGrp = new AtomicReference<>();
        defGrp.set(new Groups(-1 , "alaki" , null));
        setLogInPage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setLogInPage(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        HBox userName = new HBox(10);
        userName.setAlignment(Pos.CENTER);
        Label userNameLabel = new Label("نام کاربری");
        TextField userNameTextField = new TextField();
        userName.getChildren().addAll(userNameLabel, userNameTextField);
        root.getChildren().add(userName);
        HBox pass = new HBox(10);
        pass.setAlignment(Pos.CENTER);
        Label passLabel = new Label("رمز عبور");
        PasswordField passField = new PasswordField();
        pass.getChildren().addAll(passLabel, passField);
        root.getChildren().add(pass);
        Button logIn = new Button("ورود");
        logIn.setOnAction(event -> {
            Person prs = new Person(userNameTextField.getText() , passField.getText());
            if (sql.LogInPerson(prs)){
                showAlert("ورود به سامانه" , "" , "می توانید وارد شوید" , false);
                setMainPage(stage , prs);
            }
            else
                showAlert("ورود به سامانه" , "" , "نمی توانید وارد شوید" , true);

        });
        root.getChildren().add(logIn);
        Button signIn = new Button("ثبت نام");
        root.getChildren().add(signIn);
        signIn.setOnMouseClicked(event -> { setSignInPage(stage); });
        Scene firstPage = new Scene(root);
        stage.setScene(firstPage);
        stage.setTitle("صفحه ورود");
    }

    private void setSignInPage(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        HBox name = new HBox(10);
        name.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("نام و نام خانوادگی");
        TextField nameTextField = new TextField();
        name.getChildren().addAll(nameLabel, nameTextField);
        root.getChildren().add(name);
        HBox userName = new HBox(10);
        userName.setAlignment(Pos.CENTER);
        Label userNameLabel = new Label("نام کاربری");
        TextField userNameTextField = new TextField();
        userName.getChildren().addAll(userNameLabel, userNameTextField);
        root.getChildren().add(userName);
        HBox pass = new HBox(10);
        pass.setAlignment(Pos.CENTER);
        Label passLabel = new Label("رمز عبور");
        PasswordField passField = new PasswordField();
        pass.getChildren().addAll(passLabel, passField);
        root.getChildren().add(pass);
        HBox email = new HBox(10);
        email.setAlignment(Pos.CENTER);
        Label emailLabel = new Label("ايميل ");
        TextField emailField = new TextField();
        email.getChildren().addAll(emailLabel, emailField);
        root.getChildren().add(email);
        Button submitSI = new Button(" ثبت نام كردن");
        root.getChildren().add(submitSI);
        submitSI.setOnMouseClicked(event -> {
            Person nPrs = new Person(nameTextField.getText() , userNameTextField.getText()
                    , passField.getText() , emailField.getText());

            sql.SignInPerson(nPrs);

            setLogInPage(stage);

        });
        Button firstPage = new Button("بازگشت به صفحه ورود");
        root.getChildren().add(firstPage);
        firstPage.setOnMouseClicked(event -> { setLogInPage(stage); });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("صفحه ثبت نام");
    }

    private void setMainPage(Stage stage , Person person){
        HBox root = new HBox(20);
        root.setAlignment(Pos.CENTER);
        VBox right = new VBox(20);
        right.setAlignment(Pos.CENTER);
        /*Button showPrs = new Button("نمایش اطلاعات کاربر");
        showPrs.setOnAction(event -> {

        });*/
        ScrollPane Groups = new ScrollPane();
        VBox myGrps = new VBox(10);
        // get all Group To Scroll Pane
        ArrayList<Groups> grps = sql.GetAllGroupOfOnePerson(person);
        for (Groups g: grps) {
            Button grpBtn = new Button(g.getName());
            if (g.getGrpId() == defGrp.get().getGrpId())
                grpBtn.setStyle("-fx-background-color: #00ff00");
            else
                grpBtn.setStyle("-fx-background-color: #000000");
            grpBtn.setOnAction(event -> {
                defGrp.set(new Groups(g.getGrpId(), g.getName(), g.getDetail()));
                setMainPage(stage , person);
            });
            myGrps.getChildren().add(grpBtn);
        }
        Groups.setContent(myGrps);
        Button addGrp = new Button("افزودن گروه");
        addGrp.setOnAction(event -> {
            setAddToGrpPage(stage , person);
        });

        Button newGrp = new Button("ساخت گروه جدید");
        newGrp.setOnAction(event -> {
            setMakeGrp(stage , person);
        });
        right.getChildren().addAll(/*showPrs ,*/ Groups , addGrp , newGrp);


        VBox left = new VBox(20);
        left.setAlignment(Pos.CENTER);
        ScrollPane messages = new ScrollPane();

        Label txtMsgs = new Label();
        txtMsgs.setAlignment(Pos.CENTER);
        String lblMsg = "";
        if (defGrp.get().getGrpId() == -1)
            lblMsg = "برای نمایش پیام لطفا یک گروه را انتخاب کنید";
        else{
            ArrayList<Message> msgOfOneGrp= sql.GetAllMsgOfOneGrp(defGrp.get());
            for (Message msg : msgOfOneGrp) {
                lblMsg += "\n"+msg.toString();
            }
        }
        txtMsgs.setText(lblMsg);
        messages.setContent(txtMsgs);


        HBox msg = new HBox(1);
        msg.setAlignment(Pos.CENTER);
        TextField txtMsg = new TextField();
        Button send = new Button("s");
        send.setOnAction(event -> {
            if (defGrp.get().getGrpId() == -1)
                showAlert("ارسال پیام ناموفق" , "گروهی انتخاب نشده است" ,
                        "لطفا برای ارسال پیام یک گروه را انتخاب کند" + "\n" +
                                "گروهی که انتاب کنید سبز می شود" ,true);
            else
            if (txtMsg.getText() != "" && txtMsg.getText() != null)
                sql.SendMessage(txtMsg.getText() , defGrp.get() , person);

            setMainPage(stage , person);
        });
        msg.getChildren().addAll(send , txtMsg);
        left.getChildren().addAll(messages , msg );
        root.getChildren().add(left);

        root.getChildren().add(right);

        Scene mainPage = new Scene(root);
        stage.setScene(mainPage);
        stage.setTitle("صفحه اصلی");

    }

    private void setAddToGrpPage(Stage stage , Person p){
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        TextField txt = new TextField();
        Button btn = new Button("Submit");
        btn.setOnAction(event -> {
            if (sql.JoinExGrp(txt.getText(), p))
                showAlert("افزودن به گروه جدید"  , "موفق" , "شما به گروه اضافه شدید" , false);
            else
                showAlert("افزودن به گروه جدید"  , "نا موفق" ,
                        "به دلایل زیر شما به گروه اضافه نشدید"+
                                "\n1)  گروهی با این نام وجود ندارد " +
                                "\n2) شما قبلا عضو هستید" , true);
            setMainPage(stage , p);
        });
        root.getChildren().addAll(txt , btn);
        Scene mainPage = new Scene(root);
        stage.setScene(mainPage);
        stage.setTitle("عضویت در گروه");
    }

    private void setMakeGrp(Stage stage , Person p){
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        TextField txt = new TextField();
        Button btn = new Button("Submit");
        btn.setOnAction(event -> {
            if (sql.addNewGrp(txt.getText(), p))
                showAlert("ساخت گروه جدید"  , "موفق" ,
                        "گروه جدید با موفقیت ساخته شد" , false);
            else
                showAlert("ساخت گروه جدید"  , "نا موفق" ,
                        "به دلیل زیر گروه ساخته نشد"+
                                "\n گروه از قبل وجود دارد" , true);
            setMainPage(stage , p);
        });
        root.getChildren().addAll(txt , btn);
        Scene mainPage = new Scene(root);
        stage.setScene(mainPage);
        stage.setTitle("عضویت در گروه");
    }

    private static void showAlert(String title , String headerText ,String mainText , boolean isError) {
        Alert alert ;
        if (isError)
            alert = new Alert(Alert.AlertType.ERROR);
        else
            alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(mainText);
        alert.showAndWait();

    }



}
