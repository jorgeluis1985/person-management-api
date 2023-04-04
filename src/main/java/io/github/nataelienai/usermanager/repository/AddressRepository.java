package io.github.nataelienai.usermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.nataelienai.usermanager.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
