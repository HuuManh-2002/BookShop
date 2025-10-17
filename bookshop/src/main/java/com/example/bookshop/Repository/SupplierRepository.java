package com.example.bookshop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{

}
