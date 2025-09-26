package vessel;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
    private String host, port, user, pass, db;
    public void setConnection(String host, String port, String user, String pass, String db) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.db = db;
    }

    @FXML private Button loginBtn;
    @FXML private TableView<ObservableList<String>> tableView;
    @FXML private ComboBox<String> comboBox;

    @FXML
    private void initialize() throws SQLException {
        loginBtn.setOnAction(event -> loginWindow());
        comboBox.setOnAction(event -> {
            String table = comboBox.getValue();
            if (table != null) {
                try {
                    loadTableData(table);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setMainController(this);

            Scene scene = new Scene(root);
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setTitle("Login");
            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("ico.png")));
            loginStage.setScene(scene);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.showAndWait();
            loadTableNames();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } 
            
    }

    private Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(
        "jdbc:postgresql://" + host + ":" + port + "/" + db,
        user,
        pass
        );
        return conn;
    }

    private void loadTableNames() throws SQLException {
        comboBox.getItems().clear();

        try(Connection conn = connect();
            ResultSet tables = conn.getMetaData()
            .getTables(null, "public", null, new String[]{"TABLE"})) {
                
            while (tables.next()) {
                comboBox.getItems()
                .add(tables.getString("TABLE_NAME"));
            }
        }
    }
    
    private void loadTableData(String tableName) throws SQLException {
        if (!comboBox.getItems().contains(tableName)) return;
        tableView.getItems().clear();
        tableView.getColumns().clear();

        String sql = "SELECT * FROM " + tableName;
        
        try(Connection conn = connect();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            
            for (int i = 1; i <= columnCount; i++) {
                final int colIndex = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(rsmd.getColumnName(i));
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex - 1)));
                
                col.setStyle("-fx-alignment: CENTER;");
                
                tableView.getColumns().add(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                tableView.getItems().add(row);
            }
        }
    }
}
