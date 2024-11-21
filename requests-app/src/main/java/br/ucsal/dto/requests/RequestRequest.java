package br.ucsal.dto.requests;

import java.sql.Timestamp;

public record RequestRequest(Long userId, Long locationId, Timestamp startTime, Timestamp endTime, String reason) {
}