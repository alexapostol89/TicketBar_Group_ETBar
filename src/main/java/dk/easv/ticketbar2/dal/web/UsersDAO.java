package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.dal.exceptions.UserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {
    private DBConnection connection = new DBConnection();

    public Users validateUser(String username, String password) {
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName FROM Users u "+
                "JOIN User_rank ur ON u.Rank = ur.id "+
                "WHERE u.Username = ? AND u.PasswordHash = ?";
        try {
            Connection c = connection.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);

            // Set parameters to prevent SQL injection
            stmt.setString(1, username);
            stmt.setString(2, password); // This will need to be hashed

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Users user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setRank(rs.getInt("Rank"));
                user.setRankName(rs.getString("RankName"));
                return user;
            }
        } catch (SQLException e) {
            throw new UserException(e);
        }
    return null;
    }
}
