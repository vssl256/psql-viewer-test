package vessel;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("PSQL Viewer");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("ico.png")));
        stage.show();
    }
    public static void main(String[] args) throws SQLException {
        Application.launch(args);
    }
}