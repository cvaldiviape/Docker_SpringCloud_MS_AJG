package com.yaksha.users.service;

import com.yaksha.users.entity.UserEntity;
import com.yaksha.users.mapper.UserMapper;
import com.yaksha.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public UserEntity create(UserEntity user) {
        return this.userRepository.save(user);
    }

    @Override
    public UserEntity update(UserEntity user, Long id) throws Exception {
        UserEntity userUpdate = this.userRepository.findById(id).orElseThrow(() -> new Exception("Usuario no existe"));
        userUpdate.setName(user.getName());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setPassword(user.getPassword());
        return this.userRepository.save(userUpdate);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        UserEntity userDelete = this.userRepository.findById(id).orElseThrow(() -> new Exception("Usuario no existe"));
        this.userRepository.deleteById(userDelete.getId());
    }

}