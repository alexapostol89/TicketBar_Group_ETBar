package dk.easv.ticketbar2;

import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import dk.easv.ticketbar2.dal.web.UsersDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ETBarApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ETBarApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ETBar Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws EventsException {
        launch();
    }

}
