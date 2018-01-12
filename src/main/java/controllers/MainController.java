package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import server.*;

import java.io.IOException;
import java.lang.Exception;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML
    ComboBox<String> externalFromComboBox;

    @FXML
    TextField externalToAccountTextField;

    @FXML
    TextField externalDestinationTextField;


    @FXML
    ComboBox<String> historyAccountsComboBox;

    @FXML
    TableView<History> historyTableView;

    @FXML
    TextField externalAmountTextField;

    @FXML
    TextArea externalTitleTextArea;

    @FXML
    ComboBox<String> internalFromComboBox;

    @FXML
    ComboBox<String> internalToComboBox;

    @FXML
    TextField internalAmountTextField;

    @FXML
    TextArea internalTitleTextArea;

    @FXML
    TextField depositAmount;

    @FXML
    TextField withdrawalAmount;

    @FXML
    ComboBox<String> availableAccountsComboBox;

    @FXML
    TextField balanceTextField;

    @FXML
    TextField currentUserLogin;

    public void onRefreshBalanceButtonClicked(){
        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();
        String selectedAccount = availableAccountsComboBox.getValue();
        int balance = 0;
        try {
            balance = userService.getCurrentUser().getAccounts().stream().filter(new Predicate<Account>() {
                @Override
                public boolean test(Account account) {
                    return account.getName().equals(selectedAccount);
                }
            }).findFirst().orElseThrow(new Supplier<Exception>() {
                @Override
                public Exception get() {
                    balanceTextField.setText(String.valueOf(0));
                    return new Exception();
                }
            }).getBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        balanceTextField.setText(String.valueOf(balance));
    }

    public void onRefreshHistoryButtonClicked() {
        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();
        String selectedAccount = historyAccountsComboBox.getValue();
        try {
            Account account = userService.getCurrentUser().getAccounts().stream().filter(new Predicate<Account>() {
                @Override
                public boolean test(Account account) {
                    return account.getName().equals(selectedAccount);
                }
            }).findFirst().orElseThrow(Exception::new);

            List<History> histories = account.getHistories();

            ObservableList<History> data =
                    FXCollections.observableArrayList(histories);

            historyTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("title"));
            historyTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("amount"));
            historyTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("source"));
            historyTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("balanceAfterOperation"));
            historyTableView.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSignOutButtonClicked(ActionEvent event) throws IOException {
        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        try {
            userService.logout();
        } catch (Exception_Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Cannot sign out user!");
            alert.showAndWait();
            return;
        }
        Parent view = FXMLLoader.load(getClass().getClassLoader().getResource("loginView.fxml"));
        Scene scene = new Scene(view);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    public void onDeleteAccountButtonClicked() {
        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        String accountToBeDeleted = availableAccountsComboBox.getValue();


        if (accountToBeDeleted == null || accountToBeDeleted.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Choose account first!");
            alert.showAndWait();
            return;
        }

        AccountServiceImplService accountServiceImplService = new AccountServiceImplService();
        AccountService accountService = accountServiceImplService.getAccountServiceImplPort();

        try {
            accountService.deleteAccount(userService.getCurrentUser().getLogin(), accountToBeDeleted);

            availableAccountsComboBox.getItems().remove(accountToBeDeleted);
            externalFromComboBox.getItems().remove(accountToBeDeleted);
            internalFromComboBox.getItems().remove(accountToBeDeleted);
            internalToComboBox.getItems().remove(accountToBeDeleted);
            historyAccountsComboBox.getItems().remove(accountToBeDeleted);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("CannotDeleteAccount!");
            alert.showAndWait();
            return;
        }
    }

    public void onDepositButtonClicked() {
        AccountServiceImplService accountServiceImplService = new AccountServiceImplService();
        AccountService accountService = accountServiceImplService.getAccountServiceImplPort();

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        String selectedAccount = availableAccountsComboBox.getValue();

        if (selectedAccount == null || selectedAccount.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Choose account first!");
            alert.showAndWait();
            return;
        }

        String amountFromText = depositAmount.getText();
        if (amountFromText == null || amountFromText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Amount cannot be empty!");
            alert.showAndWait();
            return;
        }
        int amount = Integer.parseInt(amountFromText);
        try {
            String login = userService.getCurrentUser().getLogin();
            int amountBefore = Integer.parseInt(balanceTextField.getText());
            accountService.deposit(login, selectedAccount, amount);
            int newAmount = amountBefore + amount;
            balanceTextField.setText(String.valueOf(newAmount));

        } catch (Exception_Exception e) {
            e.printStackTrace();
            //TODO: add exception
        }

    }

    private void forceOnlyNumeric(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void onCreateNewBankAccountClicked() {
        AccountServiceImplService accountServiceImplService = new AccountServiceImplService();
        AccountService accountService = accountServiceImplService.getAccountServiceImplPort();

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        try {
            String currentLogin = userService.getCurrentUser().getLogin();
            String createdAccount = accountService.createAccount(currentLogin);
            availableAccountsComboBox.getItems().add(createdAccount);
            internalFromComboBox.getItems().addAll(createdAccount);
            externalFromComboBox.getItems().addAll(createdAccount);
            internalToComboBox.getItems().addAll(createdAccount);
            historyAccountsComboBox.getItems().add(createdAccount);
        } catch (Exception_Exception e) {
            e.printStackTrace();
            //TODO: add exc
        }
    }

    public void onInternalSendButtonClicked() {
        TransferServiceImplService transferServiceImplService = new TransferServiceImplService();
        TransferService transferService = transferServiceImplService.getTransferServiceImplPort();

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        try {
            String user = userService.getCurrentUser().getLogin();
            String sourceAccountName = internalFromComboBox.getValue();
            String targetAccountName = internalToComboBox.getValue();

            if (sourceAccountName == null || sourceAccountName.isEmpty() || targetAccountName == null || targetAccountName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Accounts must be chosen!");
                alert.showAndWait();
                return;
            }
            if (targetAccountName.equals(sourceAccountName)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You cannot transfer funds to the same account!");
                alert.showAndWait();
                return;
            }

            String title = internalTitleTextArea.getText();

            if (title.length() < 1 || title.length() > 255) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Title length must be greater than 1 char and less than 255 chars!");
                alert.showAndWait();
                return;
            }
            forceOnlyNumeric(internalAmountTextField);
            String text = internalAmountTextField.getText();

            if (text.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Amount cannot be empty!");
                alert.showAndWait();
                return;
            }
            int amount = Integer.parseInt(text);

            try {
                transferService.sendInternal(user, sourceAccountName, targetAccountName, amount, title);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Insufficient balance!");
                alert.showAndWait();
                return;
            }
        } catch (Exception_Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("There is no logged in user!");
            alert.showAndWait();
            return;
        }
    }

    public void onExternalSendButtonClicked() {
        TransferServiceImplService transferServiceImplService = new TransferServiceImplService();
        TransferService transferService = transferServiceImplService.getTransferServiceImplPort();

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        try {
            String user = userService.getCurrentUser().getLogin();
            String sourceAccountName = externalFromComboBox.getValue();
            String targetAccountName = externalToAccountTextField.getText();
            String destinationName = externalDestinationTextField.getText();

            if (sourceAccountName == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Choose source account!");
                alert.showAndWait();
                return;
            }

            if (sourceAccountName.isEmpty() || targetAccountName.isEmpty() || destinationName.isEmpty() || externalAmountTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("All fields must be filled!");
                alert.showAndWait();
                return;
            }
            if (!validateAccount(targetAccountName)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Target account is not correct!");
                alert.showAndWait();
                return;
            }

            if (destinationName.length() < 1 || destinationName.length() > 255) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Destination name length must be greater than 1 char and less than 255 chars!");
                alert.showAndWait();
                return;
            }
            String title = externalTitleTextArea.getText();


            if (title.length() < 1 || title.length() > 255) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Title length must be greater than 1 char and less than 255 chars!");
                alert.showAndWait();
                return;
            }

            forceOnlyNumeric(externalAmountTextField);


            int amount = Integer.parseInt(externalAmountTextField.getText());

            try {
                transferService.sendExternal(user, sourceAccountName, targetAccountName, amount, title, destinationName);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Insufficient balance!");
                alert.showAndWait();
                return;
            }
        } catch (Exception_Exception e) {
            e.printStackTrace();

        }

    }

    public void onWithdrawalButtonClicked() {
        AccountServiceImplService accountServiceImplService = new AccountServiceImplService();
        AccountService accountService = accountServiceImplService.getAccountServiceImplPort();

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        String selectedAccount = availableAccountsComboBox.getValue();

        if (selectedAccount == null || selectedAccount.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Choose account first!");
            alert.showAndWait();
            return;
        }

        String amountFromText = withdrawalAmount.getText();
        if (amountFromText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Amount cannot be empty!");
            alert.showAndWait();
            return;
        }
        int amount = Integer.parseInt(amountFromText);
        try {
            String login = userService.getCurrentUser().getLogin();
            int amountBefore = Integer.parseInt(balanceTextField.getText());
            try {
                accountService.withdrawal(login, selectedAccount, amount);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No sufficient balance!");
                alert.showAndWait();
                return;
            }
            int newAmount = amountBefore - amount;
            balanceTextField.setText(String.valueOf(newAmount));

        } catch (Exception_Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AccountServiceImplService accountServiceImplService = new AccountServiceImplService();
        AccountService accountService = accountServiceImplService.getAccountServiceImplPort();
        initAccountList();

        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();
        getBalance(userService, availableAccountsComboBox);

        try {
            currentUserLogin.setText(userService.getCurrentUser().getLogin());
        } catch (Exception_Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("There is no logged in user!");
            alert.showAndWait();
            return;
        }

        initHistory(userService);
        forceOnlyNumeric(withdrawalAmount);
        forceOnlyNumeric(depositAmount);
        forceOnlyNumeric(externalAmountTextField);
        forceOnlyNumeric(internalAmountTextField);
    }

    private void initHistory(final UserService userService) {
        historyAccountsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Account account = userService.getCurrentUser().getAccounts().stream().filter(new Predicate<Account>() {
                        @Override
                        public boolean test(Account account) {
                            return account.getName().equals(newValue);
                        }
                    }).findFirst().orElseThrow(Exception::new);

                    List<History> histories = account.getHistories();

                    ObservableList<History> data =
                            FXCollections.observableArrayList(histories);

                    historyTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("title"));
                    historyTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("amount"));
                    historyTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("source"));
                    historyTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("balanceAfterOperation"));
                    historyTableView.setItems(data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getBalance(final UserService userService, ComboBox<String> comboBox) {
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    int balance = userService.getCurrentUser().getAccounts().stream().filter(new Predicate<Account>() {
                        @Override
                        public boolean test(Account account) {
                            return account.getName().equals(newValue);
                        }
                    }).findFirst().orElseThrow(new Supplier<Exception>() {
                        @Override
                        public Exception get() {
                            balanceTextField.setText(String.valueOf(0));
                            return new Exception();
                        }
                    }).getBalance();

                    balanceTextField.setText(String.valueOf(balance));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    //TODO; add exception
                }
            }
        });
    }

    private void initAccountList() {
        UserServiceImplService userServiceImplService = new UserServiceImplService();
        UserService userService = userServiceImplService.getUserServiceImplPort();

        try {
            List<Account> accounts = userService.getCurrentUser().getAccounts();

            List<String> collection = accounts.stream().map(new Function<Account, String>() {
                @Override
                public String apply(Account account) {
                    return account.getName();
                }
            }).collect(Collectors.toList());

            availableAccountsComboBox.getItems().setAll(collection);
            internalFromComboBox.getItems().setAll(collection);
            internalToComboBox.getItems().setAll(collection);
            externalFromComboBox.getItems().setAll(collection);
            historyAccountsComboBox.getItems().setAll(collection);


            TransferServiceImplService transferServiceImplService = new TransferServiceImplService();
            TransferService transferService = transferServiceImplService.getTransferServiceImplPort();

//            List<String> externalAccountList = transferService.getExternalAccountsName().getAccountUrlList();
//            externalToComboBox.getItems().setAll(externalAccountList);


        } catch (Exception_Exception e) {
            e.printStackTrace();
            //TODO: add exception
        }
    }

    private static boolean validateAccount(String account) {
        //TODO: exceptions
        return account.length() == 26 && validateCheckSum(account);
    }

    private static boolean validateCheckSum(String account) {
        String sum = account.substring(0, 2);

        String concated = account.substring(2, account.length()) + "2521" + sum;
        BigInteger parsedConcated = new BigInteger(concated);
        BigInteger validate = parsedConcated.mod(new BigInteger("97"));
        return validate.equals(new BigInteger("1"));
    }
}
