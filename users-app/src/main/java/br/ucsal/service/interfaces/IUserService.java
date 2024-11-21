package br.ucsal.service.interfaces;

import java.util.List;
import java.util.Optional;

import br.ucsal.dto.users.*;

public interface IUserService {
    AddUserResponse add(UserRequest request);
    Optional<UserResponse> get(Long userId);
    List<UserResponse> getAll();
    DeleteResponse delete(Long userId, DeleteRequest request);
    UpdateResponse update(Long userId, UserRequest request);
}
