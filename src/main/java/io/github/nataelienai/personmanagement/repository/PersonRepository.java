package io.github.nataelienai.personmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nataelienai.personmanagement.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
