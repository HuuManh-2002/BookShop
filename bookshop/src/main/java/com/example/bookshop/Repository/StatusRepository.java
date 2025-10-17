package com.example.bookshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

}
