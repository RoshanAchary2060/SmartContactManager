package com.roshan.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private String email;
    private String password;
    private String about;
    private String phoneNumber;

} // end of class