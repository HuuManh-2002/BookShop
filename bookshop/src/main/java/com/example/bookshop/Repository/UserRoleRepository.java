package com.example.bookshop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshop.Entity.User;
import com.example.bookshop.Entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    void deleteByUser(User user);

    List<UserRole> findAllByUser(User user);

    Optional<UserRole> findByUser_idAndRole_id(Long user_id, Long role_id);
}
