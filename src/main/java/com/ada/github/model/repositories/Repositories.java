package com.ada.github.model.repositories;


import jakarta.persistence.*;

@Entity
@Table(name = "repositories")
public class Repositories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer userid;

    @Column()
    private String repository;



    public Integer getId() {
        return id;
    }

    public Repositories setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserid() {
        return userid;
    }

    public Repositories setUserid(Integer userid) {
        this.userid = userid;
        return this;
    }

    public String getRepository() {
        return repository;
    }

    public Repositories setRepository(String repository) {
        this.repository = repository;
        return this;
    }
}
