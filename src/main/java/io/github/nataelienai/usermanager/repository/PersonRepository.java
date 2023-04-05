package io.github.nataelienai.usermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nataelienai.usermanager.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
