package br.ucsal.dto.requests;

public record ApprovalRequest(Long userId, boolean isApproved) {
}