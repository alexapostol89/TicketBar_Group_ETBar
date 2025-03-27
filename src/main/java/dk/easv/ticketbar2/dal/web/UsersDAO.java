package dk.easv.ticketbar2.dal.web;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {
    private DBConnection conn = new DBConnection();

    public List<Users> getUsers() {
        List<Users> users = new ArrayList<>();
        try {
            Connection c = conn.getConnection();
            String sql = "SELECT * FROM Users";
            System.out.println("connected to users table");
            PreparedStatement statement = c.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                int userid = rs.getInt("UserId");
                String username = rs.getString("Username");
                String passwordHash = rs.getString("PasswordHash");
                int rank = rs.getInt("Rank");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String lastLogin = rs.getString("LastLogin");
                String createdDate = rs.getString("CreatedDate");
                String phone = rs.getString("Phone");

                Users usersTable = new Users(userid, username, passwordHash, rank, firstName,
                        lastName, email, phone, lastLogin, createdDate);
                users.add(usersTable);

            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return users;
}
}