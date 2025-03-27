package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CoordinatorListController {

    @FXML
    private TableView<String> tableView; // TableView should hold Strings now

    @FXML
    private TableColumn<String, String> colEvent; // Ensure it matches String type

    private EventsManager eventsManager = new EventsManager();

    @FXML
    public void initialize() {
        colEvent.setCellValueFactory(data -> new SimpleStringProperty(data.getValue())); // Bind String values
        loadEventNames();
    }

    private void loadEventNames() {
        try {
            ObservableList<String> eventNames = eventsManager.getEventNames();
            tableView.setItems(eventNames);
        } catch (EventsException e) {
            e.printStackTrace(); // Handle properly
        }
    }
}










