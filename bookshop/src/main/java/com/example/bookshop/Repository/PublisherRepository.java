package com.example.bookshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

}
