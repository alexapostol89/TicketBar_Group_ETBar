package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.web.UsersDAO;
import javafx.collections.ObservableList;

import java.util.List;

public class UsersManager {
    private final UsersDAO usersDAO = new UsersDAO();

    public List<Users> getUsers() {
        List<Users> users = usersDAO.getUsers();
        return users;
    }

    public ObservableList<String> getUsersNames() throws EventsException {
        return UsersDAO.getUsersNames();
    }
}