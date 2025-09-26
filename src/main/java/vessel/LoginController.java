package vessel;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class LoginController {
    @FXML private Button loginBtn;

    @FXML private TextField hostTextField;
    @FXML private TextField portTextField;
    @FXML private TextField userTextField;
    @FXML private PasswordField passwordField;
    @FXML private TextField dbTextField;

    String host,
    port,
    user ,
    pass,
    db;

    private MainController mainController;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        loginBtn.setOnAction(event -> {
            host = hostTextField.getText();
            if (host.isEmpty()) host = "localhost";

            port = portTextField.getText();
            if (port.isEmpty()) port = "5432";

            user = userTextField.getText();
            if (user.isEmpty()) user = "postgres";

            pass = passwordField.getText();

            db = dbTextField.getText();
            if (db.isEmpty()) db = user;

            connect();
        });
    }

    private void connect() {
        if (pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Enter password for " + user);
            alert.showAndWait();
            return;
        }
        mainController.setConnection(
            host,
            port,
            user,
            pass,
            db
        );
        loginBtn.getScene().getWindow().hide();
    }
}
