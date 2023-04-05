package io.github.nataelienai.usermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nataelienai.usermanager.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
