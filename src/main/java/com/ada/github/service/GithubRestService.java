package com.ada.github.service;

import com.ada.github.model.GithubServiceModel;
import com.ada.github.repository.repositories.RepositoriesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GithubRestService {

    private final RestTemplate restTemplate;
    private final RepositoriesRepository repositoriesRepository;

    public GithubRestService(RepositoriesRepository repositoriesRepository) {
        this.restTemplate = new RestTemplate();
        this.repositoriesRepository = repositoriesRepository;
    }

    public GithubServiceModel[] getGithubRepository(String username) {
        try {
            String apiUrl = "https://api.github.com/users/" + username + "/repos";
            ResponseEntity<GithubServiceModel[]> response = restTemplate.getForEntity(apiUrl, GithubServiceModel[].class);
            GithubServiceModel[] responseBody = response.getBody();


            return responseBody;
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }
}
