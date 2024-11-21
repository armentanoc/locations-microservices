package br.ucsal.service.interfaces;

import br.ucsal.domain.requests.RequestStatus;
import br.ucsal.dto.requests.*;

import java.util.List;
import java.util.Optional;

public interface IRequestService {
    AddRequestResponse add(RequestRequest request);
    Optional<RequestResponse> get(Long requestId);
    List<RequestResponse> getAll();
    List<RequestResponse> getAllByUserId(Long userId);
    List<RequestResponse> getAllByStatus(RequestStatus status);
    DeleteResponse delete(Long userId, DeleteRequest request);
    UpdateResponse update(Long requestId, RequestRequest request);
    ApprovalResponse approveOrReject(Long requestId, ApprovalRequest approvalRequest);
}