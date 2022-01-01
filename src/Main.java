import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import Models.*;

public class Main extends Application {

    private SqlServerConnection sql;

    @Override
    public void start(Stage primaryStage) {
        sql = new SqlServerConnection();
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
                setMainPage(stage);
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
        stage.setTitle("صفحه ورورد");
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

    private void setMainPage(Stage stage){
        HBox root = new HBox(20);
        root.setAlignment(Pos.CENTER);
        VBox right = new VBox(20);
        right.setAlignment(Pos.CENTER);
        /*Button showPrs = new Button("نمایش اطلاعات کاربر");
        showPrs.setOnAction(event -> {

        });*/
        ScrollPane Groups = new ScrollPane();
        Button addGrp = new Button("افزودن گروه");
        addGrp.setOnAction(event -> {

        });
        right.getChildren().addAll(/*showPrs ,*/ Groups , addGrp);


        VBox left = new VBox(20);
        left.setAlignment(Pos.CENTER);
        ScrollPane messages = new ScrollPane();

        Label txt = new Label();
        txt.setAlignment(Pos.CENTER);
        txt.setText( " سلام حسین چطوری ؟");
        for (int i = 0; i < 100; i++) {
            txt.setText(txt.getText() + "\n\n سلام حسین چطوری ؟");
        }

        messages.setContent(txt);


        HBox msg = new HBox(1);
        msg.setAlignment(Pos.CENTER);
        TextField txtMsg = new TextField();
        Button send = new Button("s");
        send.setOnAction(event -> {

        });
        msg.getChildren().addAll(send , txtMsg);
        left.getChildren().addAll(messages , msg );
        root.getChildren().add(left);

        root.getChildren().add(right);

        Scene mainPage = new Scene(root);
        stage.setScene(mainPage);
        stage.setTitle("صفحه اصلی");

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
