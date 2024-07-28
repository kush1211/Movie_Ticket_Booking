package model;
public class User {
    // Instance variables
    private String username;
    private String email;
    private String password;

    // Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // Setter methods (if needed)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Method to check if provided password matches user's password
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
