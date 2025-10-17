package com.example.bookshop.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookshop.Entity.Role;
import com.example.bookshop.Entity.User;
import com.example.bookshop.Entity.UserRole;
import com.example.bookshop.EntityDto.Reponse.UserReponse;
import com.example.bookshop.EntityDto.Request.UserRequest;
import com.example.bookshop.EntityDto.Update.UserUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.UserMapper;
import com.example.bookshop.Repository.RoleRepository;
import com.example.bookshop.Repository.UserRepository;
import com.example.bookshop.Repository.UserRoleRepository;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userroleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserMapper userMapper;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<UserReponse> getAll() {
        List<User> users = userRepository.findAll();
        List<UserReponse> userReponses = new ArrayList<>();
        for (User user : users) {
            userReponses.add(userMapper.toUserReponse(user));
        }
        return userReponses;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public UserReponse get(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserReponse(user);
    }

    public UserReponse create(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        try {
            userRepository.save(user);
            Role role = roleRepository.findById((Long.valueOf(2))).get();
            UserRole user_role = UserRole.builder()
                    .user(user)
                    .role(role)
                    .createdTime(LocalDateTime.now())
                    .build();
            userroleRepository.save(user_role);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        return userMapper.toUserReponse(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public UserReponse update(UserUpdate userUpdate, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setEmail(userUpdate.getEmail());
        user.setDob(userUpdate.getDob());
        user.setPhoneNumber(userUpdate.getPhoneNumber());
        user.setGender(userUpdate.getGender());

        userRepository.save(user);

        return userMapper.toUserReponse(user);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userroleRepository.deleteByUser(user);
        userRepository.deleteById(id);
    }

    public UserReponse getMyInfor() {
        return userMapper.toUserReponse(getUserformToKen());
    }

    public UserReponse updateMyInfor(UserUpdate userUpdate) {
        User user = getUserformToKen();
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setEmail(userUpdate.getEmail());
        user.setDob(userUpdate.getDob());
        user.setPhoneNumber(userUpdate.getPhoneNumber());
        user.setGender(userUpdate.getGender());

        userRepository.save(user);

        return userMapper.toUserReponse(user);

    }

    @Transactional
    public void deleteMyInfor() {
        User user = getUserformToKen();
        userroleRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    public User getUserformToKen() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!userRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return userRepository.findByUsername(username).get();
    }
}
