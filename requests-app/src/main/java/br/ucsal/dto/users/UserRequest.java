package br.ucsal.dto.users;

import br.ucsal.domain.users.UserRole;

public record UserRequest(String name, String email, String username, String password, UserRole role, Long adminId) {
}
