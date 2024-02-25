package com.blog.travel.dto;

import lombok.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long userId;

    @NotBlank(message = "Username is required.")
    private String name;

    @NotBlank(message = "Password is required.")
    private String passWord;

    @Email(message = "Enter a valid email address.")
    @NotEmpty(message = "Email is required.")
    private String email;

    /* Additional properties could be added here if needed. */

    // Ensure that both 'password' and 'confirmPassword' match
    @AssertTrue(message = "Passwords do not match!")
    private Boolean arePasswordMatching() {
        return passWord != null && passWord.equals(confirmPassword);
    }

    private String confirmPassword;
}
