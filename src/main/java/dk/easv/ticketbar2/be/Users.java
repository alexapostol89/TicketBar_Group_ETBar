package dk.easv.ticketbar2.be;

public class Users {
    private int Userid;
    private String Username;
    private String Password;
    private int Rank;
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Phone;

    public Users(int userid, String username, String password, int rank, String firstname, String lastname, String email, String phone, String lastLogin, String createdDate) {
        Userid = userid;
        Username = username;
        Password = password;
        Rank = rank;
        Firstname = firstname;
        Lastname = lastname;
        Email = email;
        Phone = phone;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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
}
