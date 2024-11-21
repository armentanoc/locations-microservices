package br.ucsal.service;

import br.ucsal.domain.logs.Action;
import br.ucsal.domain.logs.ActionLog;
import br.ucsal.domain.logs.ApprovalLog;
import br.ucsal.domain.requests.Request;
import br.ucsal.domain.requests.RequestStatus;
import br.ucsal.domain.users.UserRole;
import br.ucsal.dto.requests.*;
import br.ucsal.infrastructure.*;
import br.ucsal.service.interfaces.IRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestService implements IRequestService {

    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ILocationRepository locationRepository;

    @Autowired
    private IApprovalLogRepository approvalLogRepository;

    @Autowired
    private IActionLogRepository actionLogRepository;

    @Override
    public AddRequestResponse add(RequestRequest request) {
        var response = new AddRequestResponse(false, "Erro desconhecido", null);

        if (request.startTime() == null || request.endTime() == null || request.userId() == null || request.locationId() == null || request.reason() == null) {
            return new AddRequestResponse(false, "Os campos userId, locationId, startTime, endTime e reason são obrigatórios", null);
        }

        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());

        if (request.startTime().before(currentTime) || request.endTime().before(request.startTime())) {
            return new AddRequestResponse(false, "É obrigatório que startTime seja no futuro e endTime seja após startTime", null);
        }

        var user = userRepository.findById(request.userId());
        if (user.isEmpty()) {
            return new AddRequestResponse(false, "Usuário não encontrado (userId: " + request.userId() + ").", null);
        }

        var location = locationRepository.findById(request.locationId());
        if (location.isEmpty()) {
            return new AddRequestResponse(false, "Local não encontrado (locationId: " + request.locationId() + ").", null);
        }

        var requestToAdd = new Request(user.get(), location.get(), request.startTime(), request.endTime(), RequestStatus.PENDENTE, request.reason());

        try {
            requestRepository.save(requestToAdd);
            var log = new ActionLog(user.get(), "requests", requestToAdd.getId(), Action.CREATE, "Solicitação criada com sucesso.");
            actionLogRepository.save(log);
            response = new AddRequestResponse(true, "Solicitação adicionada com sucesso", requestToAdd.getId());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    @Override
    public Optional<RequestResponse> get(Long requestId) {
        return requestRepository.findById(requestId).map(request -> new RequestResponse(request.getId(),
                request.getUser().getId(), request.getLocation().getId(), request.getStatus(),
                request.getStartTime(), request.getEndTime(), request.getReason()));
    }

    @Override
    public List<RequestResponse> getAll() {
        var locations = requestRepository.findAllByOrderByCreatedAtDesc();
        return locations.stream().map(request -> new RequestResponse(request.getId(),
                request.getUser().getId(), request.getLocation().getId(), request.getStatus(),
                request.getStartTime(), request.getEndTime(), request.getReason())).collect(Collectors.toList());
    }

    @Override
    public List<RequestResponse> getAllByUserId(Long userId) {
        var requests = requestRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return requests.stream().map(request -> new RequestResponse(request.getId(),
                request.getUser().getId(), request.getLocation().getId(), request.getStatus(),
                request.getStartTime(), request.getEndTime(), request.getReason())).collect(Collectors.toList());
    }

    @Override
    public List<RequestResponse> getAllByStatus(RequestStatus status) {
        var locations = requestRepository.findByStatusOrderByCreatedAtDesc(status);
        return locations.stream().map(request -> new RequestResponse(request.getId(),
                request.getUser().getId(), request.getLocation().getId(), request.getStatus(),
                request.getStartTime(), request.getEndTime(), request.getReason())).collect(Collectors.toList());
    }

    @Override
    public DeleteResponse delete(Long requestId, DeleteRequest request) {
        var optionalRequest = requestRepository.findById(requestId);

        if (optionalRequest.isEmpty())
            return new DeleteResponse(false, "Solicitação não encontrada.");

        var requestToDelete = optionalRequest.get();

        if(request.userId() != requestToDelete.getUser().getId())
            return new DeleteResponse(false, "Apenas o usuário de ID " + requestToDelete.getUser().getId() + " está autorizado a excluir essa solicitação.");

        if (requestToDelete.getStatus() != RequestStatus.PENDENTE)
            return new DeleteResponse(false, "Apenas solicitações PENDENTES podem ser excluídas (Status Atual: " + requestToDelete.getStatus() + ").");

        requestRepository.delete(requestToDelete);

        var log = new ActionLog(requestToDelete.getUser(), "requests", requestId, Action.DELETE, "Solicitação excluída com sucesso.");
        actionLogRepository.save(log);

        return new DeleteResponse(true, "Solicitação excluída com sucesso.");
    }

    @Override
    public UpdateResponse update(Long requestId, RequestRequest request) {
        var response = new UpdateResponse(false, "Erro desconhecido.");
        var optionalRequest = requestRepository.findById(requestId);

        if (optionalRequest.isEmpty())
            return new UpdateResponse(false, "Solicitação não encontrada.");

        var requestToUpdate = optionalRequest.get();

        if(request.userId() != requestToUpdate.getUser().getId())
            return new UpdateResponse(false, "Apenas o usuário de ID " + requestToUpdate.getUser().getId() + " está autorizado a alterar essa solicitação.");

        try {

            var requestBefore = requestToUpdate.copy();

            if (request.userId() != null) {
                var user = userRepository.findById(request.userId());
                if (user.isEmpty()) {
                    return new UpdateResponse(false, "Usuário não encontrado (userId: " + request.userId() + ").");
                }
                requestToUpdate.setUser(user.get());
            }

            if (request.locationId() != null) {
                var location = locationRepository.findById(request.locationId());
                if (location.isEmpty()) {
                    return new UpdateResponse(false, "Local não encontrado (locationId: " + request.locationId() + ").");
                }
                requestToUpdate.setLocation(location.get());
            }
            requestToUpdate.setReason(request.reason());
            requestToUpdate.setStartTime(request.startTime());
            requestToUpdate.setEndTime(request.endTime());
            requestRepository.save(requestToUpdate);
            var log = new ActionLog(requestToUpdate.getUser(), "requests", requestId, Action.UPDATE, "Solicitação atualizada com sucesso. Antes: "+requestBefore.toString()+" Depois: "+requestToUpdate.toString());
            actionLogRepository.save(log);
            response = new UpdateResponse(true, "Solicitação atualizada com sucesso.");
        } catch (Exception ex) {
            if (ex.getMessage().contains("duplicar valor da chave") || ex.getMessage().contains("duplicated key")) {
                System.out.println(ex.getMessage());
            }
        }

        return response;
    }

    @Override
    public ApprovalResponse approveOrReject(Long requestId, ApprovalRequest approvalRequest) {
        var response = new ApprovalResponse(false, "Erro desconhecido.");
        var optionalRequest = requestRepository.findById(requestId);

        if (optionalRequest.isEmpty())
            return new ApprovalResponse(false, "Solicitação não encontrada.");

        var user = userRepository.findById(approvalRequest.userId());

        if (user.isEmpty()) {
            return new ApprovalResponse(false, "Usuário não encontrado (userId: " + approvalRequest.userId() + ").");
        }

        var manager = user.get();

        if (manager.getRole() != UserRole.GESTOR)
            return new ApprovalResponse(false, "Somente usuários com o papel GESTOR estão autorizados a realizar essa ação (UserRole: " + manager.getRole() + ").");

        var requestToApproveOrReject = optionalRequest.get();
        var oldValue = requestToApproveOrReject.getStatus();

        if(approvalRequest.isApproved()) {
            List<Request> conflictingRequests = requestRepository.findConflictingRequests(
                    requestToApproveOrReject.getLocation().getId(), requestToApproveOrReject.getStartTime(), requestToApproveOrReject.getEndTime());

            if (!conflictingRequests.isEmpty()) {
                String commaSeparatedIds = conflictingRequests.stream()
                        .map(r -> String.valueOf(r.getId()))
                        .collect(Collectors.joining(","));
                return new ApprovalResponse(false, "Conflito de horário: já existem solicitações aprovadas para este local em um intervalo de tempo conflitante (RequestIds: "+commaSeparatedIds+").");
            }
        }

        if (requestToApproveOrReject.getStatus() != RequestStatus.PENDENTE)
            return new ApprovalResponse(false, "Apenas solicitações com status PENDENTE podem ser avaliadas (Status atual: " + requestToApproveOrReject.getStatus() + ").");

        if (approvalRequest.isApproved()) {
            requestToApproveOrReject.setStatus(RequestStatus.APROVADO);
        } else {
            requestToApproveOrReject.setStatus(RequestStatus.RECUSADO);
        }

        try {
            requestRepository.save(requestToApproveOrReject);
            var log = new ApprovalLog(manager,requestToApproveOrReject,oldValue,requestToApproveOrReject.getStatus());
            approvalLogRepository.save(log);
            return new ApprovalResponse(true, "Solicitação atualizada. ");
        } catch (Exception ex) {
            if (ex.getMessage().contains("duplicar valor da chave") || ex.getMessage().contains("duplicated key")) {
                System.out.println(ex.getMessage());
            }
        }
        return response;
    }
}
