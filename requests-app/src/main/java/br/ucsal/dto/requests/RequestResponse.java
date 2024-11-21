package br.ucsal.dto.requests;

import br.ucsal.domain.requests.RequestStatus;

import java.sql.Timestamp;

public record RequestResponse(Long id, Long userId, Long locationId, RequestStatus status, Timestamp startTime, Timestamp endTime, String reason) {
}

