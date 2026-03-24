package com.example.demo;

import java.util.List;
import java.util.Optional;

public class SimpleService {
    private Repository repository;

    public SimpleService(Repository repository) {
        this.repository = repository;
    }

    public Optional<Entity> findById(Long id) {
        return repository.findById(id);
    }

    public List<Entity> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}

interface Repository {
    Optional<Entity> findById(Long id);
    List<Entity> findAll();
    void delete(Long id);
}
