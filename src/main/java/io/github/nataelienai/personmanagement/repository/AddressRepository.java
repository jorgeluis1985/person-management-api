package io.github.nataelienai.personmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nataelienai.personmanagement.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
