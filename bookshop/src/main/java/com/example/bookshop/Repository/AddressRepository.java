package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Address;
import com.example.bookshop.Entity.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);
}
