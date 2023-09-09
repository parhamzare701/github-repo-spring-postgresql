package com.ada.github.service;

import com.ada.github.model.repositories.Repositories;
import com.ada.github.repository.repositories.RepositoriesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RepositoriesService {

    private final RepositoriesRepository repositoriesRepository;

    public RepositoriesService(RepositoriesRepository repositoriesRepository) {
        this.repositoriesRepository = repositoriesRepository;
    }

    public void saveRepository(String repositoryName, Integer userId) {
        try {
            Repositories repositories = new Repositories();
            repositoriesRepository.save(repositories.setRepository(repositoryName).setUserid(userId));
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
    }
}
