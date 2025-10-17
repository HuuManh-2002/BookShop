package com.example.bookshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Author;
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{

}
