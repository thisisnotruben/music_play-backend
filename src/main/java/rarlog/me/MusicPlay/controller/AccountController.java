package rarlog.me.MusicPlay.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import rarlog.me.MusicPlay.service.AppUserService;
import rarlog.me.dto.AccountCreateDto;
import rarlog.me.dto.AppUserDto;
import rarlog.me.dto.ErrorResponseDto;
import rarlog.me.dto.LoginResponseDto;

@Validated
@RestController
@RequestMapping(AccountController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class AccountController {

    public final static String REQUEST_MAPPING = "/api/v1/account";

    private final AppUserService appUserService;

    @Operation(summary = "Create an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Account already exists or bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PostMapping("/create")
    public void createAccount(@RequestBody AccountCreateDto accountCreateDto) {
        appUserService.createAccount(accountCreateDto);
    }

    @Operation(summary = "Login to an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponseDto login(@RequestParam("username") String username,
            @RequestParam("password") String password) {
        return new LoginResponseDto(appUserService.verify(username, password));
    }

    @Operation(summary = "Get account info")
    @GetMapping(path = "getAccountInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserDto getAccountInfo() {
        return appUserService.getAccountInfo(getUsername());
    }

    @Operation(summary = "Edit username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("edit/username")
    public void editUsername(
            @RequestParam("username") @NotNull(message = "Username cannot be null.") @NotEmpty(message = "Username cannot be empty.") @Size(min = 4, max = 24, message = "Username must be >= 4 characters and <= 24 characters.") String username) {
        this.appUserService.editUsername(getUsername(), username);
    }

    @Operation(summary = "Edit password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("edit/password")
    public void editPassword(
            @RequestParam("password") @NotNull(message = "Password cannot be null.") @NotEmpty(message = "Password cannot be empty.") @Size(min = 8, max = 24, message = "Password must be >= 8 characters and <= 24 characters.") String password) {
        this.appUserService.editPassword(getUsername(), password);
    }

    @Operation(summary = "Edit e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("edit/email")
    public void editEmail(
            @RequestParam("email") @NotNull(message = "E-mail cannot be null.") @Email(message = "E-mail should be in proper format.") String email) {
        this.appUserService.editEmail(getUsername(), email);
    }

    @Operation(summary = "Edit first name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("edit/firstName")
    public void editFirstName(
            @RequestParam("firstName") @NotNull(message = "First name cannot be null.") @NotEmpty(message = "First name cannot be empty.") String firstName) {
        this.appUserService.editFirstName(getUsername(), firstName);
    }

    @Operation(summary = "Edit last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("edit/lastName")
    public void editLastName(
            @RequestParam("lastName") @NotNull(message = "Last name cannot be null.") @NotEmpty(message = "Last name cannot be empty.") String lastName) {
        this.appUserService.editLastName(getUsername(), lastName);
    }

    private String getUsername() {
        return "admin";
    }

}
