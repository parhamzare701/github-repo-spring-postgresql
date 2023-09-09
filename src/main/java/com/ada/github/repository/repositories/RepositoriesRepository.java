package com.ada.github.repository.repositories;

import com.ada.github.model.repositories.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoriesRepository extends JpaRepository<Repositories, Integer> {
    @Query(value = """
            SELECT repositories.repository
            FROM repositories
                INNER JOIN users ON repositories.userid = users.id
            WHERE (users.username = :#{#username})""", nativeQuery = true)
    List findRepositoryByUsername(@Param("username") String username);
}
