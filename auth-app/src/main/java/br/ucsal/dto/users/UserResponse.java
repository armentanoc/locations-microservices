package br.ucsal.dto.users;

import br.ucsal.domain.users.UserRole;

public record UserResponse(Long id, String name, String email, String username, UserRole role) {
}
