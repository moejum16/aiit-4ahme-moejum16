/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Julian
 */
public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public Employee() {
    }

    public Employee(Integer id, String firstName, String lastName, Date birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
    
    //1. Default constructor
    Gson gson = new Gson();
    
        

    @Override
    public String toString() {
        return "Employees [id=" + id + ",firstName=" + firstName+ ", "+
                "lastName=" + lastName + ",roles=" + roles + "]";
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Gson getGson() {
        return gson;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
