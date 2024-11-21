package br.ucsal.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ucsal.domain.users.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>  {
    Optional<User> findByusername(String username);
    List<User> findAllByOrderByIdAsc();
}
