package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.UserService;
import server.UserServiceImplService;

import java.io.IOException;

public class LoginController  {
    @FXML
    TextField loginTextField;

    @FXML
    PasswordField passwordTextField;

    public void loginButtonClicked(ActionEvent event) {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();

        if(login.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
            return;
        }

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        try {
            userService.getAllUsers().getUserList()
                    .stream()
                    .filter(user -> user.getLogin().equals(login))
                    .findFirst()
                    .orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("There is no user with specified login!");
            alert.showAndWait();
            return;
        }

        try {
            userService.getCurrentUser();
            userService.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            userService.login(login, password);

            goToMainView(event);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Wrong password. Try again!");
            alert.showAndWait();
            return;
        }
    }

    private void goToMainView(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getClassLoader().getResource("mainView.fxml"));
        Scene scene = new Scene(view);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    public void signUpButtonClicked() {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();

        if(login.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
            return;
        }

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        try {
            userService.createUser(login, password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("User created!");

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("User with specified name exists!");
            alert.showAndWait();
            return;
        }

    }

}
