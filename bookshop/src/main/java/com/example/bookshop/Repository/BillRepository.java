package com.example.bookshop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.Bill;
import com.example.bookshop.Entity.Supplier;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findBySupplier(Supplier supplier);

}
