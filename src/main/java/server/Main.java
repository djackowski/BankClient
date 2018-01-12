package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.lang.*;
import java.lang.Exception;
import java.net.MalformedURLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("loginView.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    //wsimport -keep http://localhost:8080/users?wsdl
    //wsimport -keep http://localhost:8080/accounts?wsdl
    //wsimport -keep http://localhost:8080/transfers?wsdl
    public static void main(String[] args) throws SOAPException, MalformedURLException, Exception_Exception {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        //TODO: check if loggedIn
        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();
        try {
            userService.logout();
        } catch (Exception_Exception e) {
            e.printStackTrace();
        }

        super.stop();
    }

    private static void test() {
        //        HelloImplService helloService = new HelloImplService();
//        Hello hello = helloService.getHelloImplPort();
//
//        System.out.println(hello.sayHello());

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();
//        userService.createUser("Daga", "Laga");
//        userService.createUser("Damian", "Password");
//        userService.createUser("Stanislave", "Newone");


//        userService.login("Daga", "Laga");
//        int balance = userService.getCurrentUser().getAccounts().get(0).getBalance();
//        System.out.println(balance);
//        userService.createUser("Daga", "Laga");
//        userService.createUser("Damian", "Password");
//
//        String currentUser = null;
//        try {
//            userService.createUser("Damian", "password");
////            userService.login("Damian", "password");
////            currentUser = userService.getCurrentUser();
////            userService.logout();
//            userService.removeUser("Damian");
//        } catch (Exception_Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(currentUser);
        AccountServiceImplService accountServiceImplService = new AccountServiceImplService();
        AccountService accountService = accountServiceImplService.getAccountServiceImplPort();
//        accountService.createAccount("Daga");
//        accountService.createAccount("Damian");
//        accountService.createAccount("Stanislave");
//        accountService.deposit("Damian", "41001344960000000000000001", 100000);
        TransferServiceImplService transferServiceImplService = new TransferServiceImplService();
        TransferService transferService = transferServiceImplService.getTransferServiceImplPort();
        System.out.println(transferService.getExternalAccountsName().getAccountUrlList().toString());
//        transferService.send("Damian", "41001344960000000000000001", "41001344960000000000000002", 4000, "Tytulem");
//        transferService.send("Damian", "41001344960000000000000004", "41001344960000000000000006", 8000);
//        int balance = accountService.getBalance("Damian", "41001344960000000000000004");
//        System.out.println(balance);
//        accountService.deposit("Damian", "41001344960000000000000004", 10000);
//        balance = accountService.getBalance("Damian", "41001344960000000000000004");
//        System.out.println(balance);
//        accountService.withdrawal("Damian", "41001344960000000000000004", 1000);
//        balance = accountService.getBalance("Damian", "41001344960000000000000004");
//        System.out.println(balance);


//        accountService.createAccount("Damian");
//        accountService.deleteAccount("Damian", "41001344960000000000000005");
    }
}
