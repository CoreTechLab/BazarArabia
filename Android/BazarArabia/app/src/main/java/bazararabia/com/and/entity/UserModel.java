package bazararabia.com.and.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class UserModel {
    private String id;
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String password = "";
    private String photo = "";
    private String type = "";
    private String socialToken = "";
    public UserModel(){}
    public UserModel(String id, String firstName, String lastName, String email, String password, String type){
        this.id = id;
        this.firstName = lastName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Exclude
    public HashMap<String , Object> toMap()
    {
        HashMap<String, Object> res = new HashMap<String,  Object>();
        res.put("id" , id);
        res.put("firstName" , firstName);
        res.put("lastName" , lastName);
        res.put("email" , email);
        res.put("password" , password);
        return res;
    }
}
