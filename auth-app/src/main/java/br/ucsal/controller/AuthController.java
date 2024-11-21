package br.ucsal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ucsal.dto.users.*;
import br.ucsal.service.interfaces.IUserService;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication", description = "Operations related to user authentication")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping
    @Operation(summary = "Authenticate a user", description = "Trying to authenticate a user with the given username and password")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest dto) {
        LoginResponse response = userService.login(dto);

        if (response.success())
            return ResponseEntity.status(HttpStatus.OK).body(response);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}