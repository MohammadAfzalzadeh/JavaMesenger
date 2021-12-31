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
        logIn.setOnMouseClicked(event -> {
            Person prs = new Person(userNameTextField.getText() , passField.getText());
            if (sql.LogInPerson(prs))
                showAlert("ورود به سامانه" , "" , "می توانید وارد شوید" , false);
            else
                showAlert("ورود به سامانه" , "" , "نمی توانید وارد شوید" , true);

        });
        root.getChildren().add(logIn);
        Button signIn = new Button("ثبت نام");
        root.getChildren().add(signIn);
        signIn.setOnMouseClicked(event -> { setSignInPage(stage); });
        Scene firstPage = new Scene(root);
        stage.setScene(firstPage);
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
