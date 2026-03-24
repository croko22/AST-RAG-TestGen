package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ComplexService<T, ID> {
    private final GenericRepository<T, ID> repository;
    private final EmailService emailService;
    private final AuditLogger auditLogger;

    public ComplexService(
            GenericRepository<T, ID> repository,
            EmailService emailService,
            AuditLogger auditLogger) {
        this.repository = repository;
        this.emailService = emailService;
        this.auditLogger = auditLogger;
    }

    public Optional<T> findById(ID id) {
        auditLogger.log("findById", id);
        return repository.findById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T save(T entity) {
        T saved = repository.save(entity);
        emailService.sendNotification("Entity saved: " + saved.getClass().getSimpleName());
        return saved;
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
        auditLogger.log("deleteById", id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }
}

interface GenericRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
}

class EmailService {
    public void sendNotification(String message) {}
}

class AuditLogger {
    public void log(String action, Object id) {}
}
