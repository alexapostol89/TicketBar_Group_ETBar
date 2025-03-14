package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;

public class EventsController {

    @FXML
    private FlowPane flowPane;

    @FXML
    public void initialize() {
        
        flowPane.setHgap(10);
        flowPane.setVgap(10);
    }

    public void addImage() {

        Image image = new Image(getClass().getResourceAsStream("/dk/easv/ticketbar2/pictures/background3.jpg"));


        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);


        Label label = new Label("Event Description");


        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().addAll(imageView, label);

        flowPane.getChildren().add(vBox);
    }
}
