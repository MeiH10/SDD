package SDD.Server.Account;


import org.springframework.data.annotation.Id;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
public class Account {

    @Id
    private String email;

    private String username;
    private String password;
    private String first_name;
    private String last_name;

    public Account(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Account(String username, String password, String email, String first_name, String last_name){
        this.username = username;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    // Getters and Setters
    public String getUsername() {return this.username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return this.password;}
    public void setPassword(String password) {this.password = password;}

    public String getFirstName() {return this.first_name;}
    public void setFirstName(String first_name) {this.first_name = first_name;}

    public String getLastName() {return this.last_name;}
    public void setLastName(String last_name) {this.last_name = last_name;}

    public String getEmail() {return this.email;}

}
