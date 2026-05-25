package rarlog.me.MusicPlay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDto {

    @NotNull(message = "Username cannot be null.")
    @NotEmpty(message = "Username cannot be empty.")
    @Size(min = 4, max = 24, message = "Username must be >= 4 characters and <= 24 characters.")
    private String username;

    @NotNull(message = "Password cannot be null.")
    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 8, max = 24, message = "Password must be >= 8 characters and <= 24 characters.")
    private String password;

    @NotNull(message = "E-mail cannot be null.")
    @Email(message = "E-mail should be in proper format.")
    private String email;

    @NotNull(message = "First name cannot be null.")
    @NotEmpty(message = "First name cannot be empty.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    @NotEmpty(message = "Last name cannot be empty.")
    private String lastName;

}
