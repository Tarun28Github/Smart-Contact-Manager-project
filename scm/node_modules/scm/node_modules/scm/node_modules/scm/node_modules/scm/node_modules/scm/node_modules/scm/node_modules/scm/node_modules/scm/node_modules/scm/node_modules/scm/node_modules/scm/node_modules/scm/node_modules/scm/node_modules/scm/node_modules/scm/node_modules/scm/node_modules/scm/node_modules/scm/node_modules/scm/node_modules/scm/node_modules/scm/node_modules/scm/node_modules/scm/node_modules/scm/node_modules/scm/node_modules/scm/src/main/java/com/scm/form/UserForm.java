package com.scm.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "name is required")
    @Size(min = 3 , message = "Min 3 characters required")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email address")
    @Pattern(regexp = "^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$" )
    private String email;

    @NotBlank(message = "phonenumber is required")
    @Size(min = 8,max = 12 , message = "invalid phone number")
    private String phonenumber;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "not a valid password")
    @Pattern(regexp = "^[a-zA-Z0-9_.±@#!%&*$]+$")
    private String password;

    
    private String about;

}
