package dk.easv.ticketbar2.be;


import java.sql.Timestamp;

public class Users {
    private int Userid;
    private String Username;
    private String PasswordHash;
    private int Rank;
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Phone;
    private String CreatedDate;
    private Timestamp LastLogin;
    private String RankName;

    public Users(int userid, String username, String passwordHash, int rank, String firstname, String lastname, String email, String phone, Timestamp lastLogin , String createdDate, String rankName) {
        Userid = userid;
        Username = username;
        PasswordHash = passwordHash;
        Rank = rank;
        Firstname = firstname;
        Lastname = lastname;
        Email = email;
        Phone = phone;
        CreatedDate= createdDate;
        LastLogin = lastLogin;
        RankName = rankName;

    }

    public Users() {

    }

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public Timestamp getLastLogin() {
        return LastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        LastLogin = lastLogin;
    }

    public String getRankName() {
        return RankName;
    }

    public void setRankName(String rankName) {
        RankName = rankName;
    }
}

