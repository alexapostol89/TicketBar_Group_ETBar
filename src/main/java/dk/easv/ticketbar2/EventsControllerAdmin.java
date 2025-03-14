package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;

public class EventsControllerAdmin {

    @FXML
    private FlowPane flowPaneAdmin;

    public void addAdmin() {
        // Load the image from resources
        Image image = new Image(getClass().getResourceAsStream("/dk/easv/ticketbar2/pictures/background4.jpg"));


        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label label = new Label("Event Details");


        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.getChildren().addAll(imageView, label);

        flowPaneAdmin.getChildren().add(vBox);

        flowPaneAdmin.setHgap(10);
        flowPaneAdmin.setVgap(10);
    }
}
