package br.ucsal.dto.users;

import java.util.Optional;

public record LoginResponse(Optional<UserResponse> user, boolean success, String message) {
}
