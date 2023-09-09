package com.ada.github.controller;


import com.ada.github.model.GithubServiceModel;
import com.ada.github.model.users.Users;
import com.ada.github.repository.repositories.RepositoriesRepository;
import com.ada.github.repository.users.UsersRepository;
import com.ada.github.service.GithubRepositoryInterface;
import com.ada.github.service.RepositoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UsersController {
    private final UsersRepository usersRepository;
    private final RepositoriesRepository repositoriesRepository;

    private final GithubRepositoryInterface githubRepositoryInterface;

    private final RepositoriesService repositoriesService;

    public UsersController(UsersRepository usersRepository, RepositoriesRepository repositoriesRepository, GithubRepositoryInterface githubRepositoryInterface, RepositoriesService repositoriesService) {
        this.usersRepository = usersRepository;
        this.repositoriesRepository = repositoriesRepository;
        this.githubRepositoryInterface = githubRepositoryInterface;
        this.repositoriesService = repositoriesService;
    }

    public Users addUser(Users users) {
        try {
            users.setUsername(users.getUsername());
            return usersRepository.save(users);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "some thing wents wrong", exception);
        }
    }

    @Transactional
    @GetMapping("")
    public List getUsers(@RequestParam String username) throws IOException {
        Users users = new Users();

        List selectedRepository = repositoriesRepository.findRepositoryByUsername(username);
        if (selectedRepository.size() > 0) {
            return selectedRepository;
        } else {
            List<GithubServiceModel> githubGetRepositories;
            var result = githubRepositoryInterface.getGithubRepository(username).execute();
            githubGetRepositories = result.body();

            Users addedUser = addUser(users.setUsername(username));

            for (int index = 0; index < githubGetRepositories.size(); index++) {
                repositoriesService.saveRepository(githubGetRepositories.get(index).getName(), addedUser.getId());
            }

            if (githubGetRepositories.isEmpty()) {
                repositoriesService.saveRepository(null, addedUser.getId());
            }

            return repositoriesRepository.findRepositoryByUsername(addedUser.getUsername());
        }
    }
}
