package br.ucsal.infrastructure;

import br.ucsal.domain.requests.Request;
import br.ucsal.domain.requests.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface IRequestRepository extends JpaRepository<Request, Long>  {
    @Query("SELECT r FROM Request r WHERE r.location.id = ?1 " +
            "AND ((r.startTime <= ?3 AND r.endTime >= ?2) OR " +
            "(r.startTime <= ?2 AND r.endTime >= ?2) OR " +
            "(r.startTime <= ?2 AND r.endTime >= ?3)) AND " +
            "r.status = br.ucsal.domain.requests.RequestStatus.APROVADO")
    List<Request> findConflictingRequests(Long locationId, Timestamp startTime, Timestamp endTime);
    List<Request> findAllByOrderByCreatedAtDesc();
    List<Request> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    List<Request> findByUserIdOrderByCreatedAtDesc(Long userId);

}
