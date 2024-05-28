package com.example.pasteleria.service;

import com.example.pasteleria.model.Usuarios;
import com.example.pasteleria.repository.UsuarioRepositorio;
import com.example.pasteleria.response.LoadUserResponse;
import com.example.pasteleria.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Slf4j
public class UserDetailService implements UserDetailsService {

    private final UsuarioRepositorio userRepository;


    public String flag;

    public UserDetailService(UsuarioRepositorio userRepository) {
        this.userRepository = userRepository;
    }

    public void setFlag(String flag){
        this.flag=flag;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        LoadUserResponse loadUserResponse = new LoadUserResponse();
        try {
            Optional<Usuarios> clientRecord = userRepository.findByEmail(username);
            if(clientRecord.isPresent() && this.flag=="CLIENT"){
                loadUserResponse.setId(clientRecord.get().getId());
                loadUserResponse.setUsername(clientRecord.get().getEmail());
                loadUserResponse.setPassword(clientRecord.get().getPassword());
            }
        } catch (Exception e){
            log.error("Authentication Failed. Username or Password not valid.");
        }
        return new UserResponse(loadUserResponse.getId(), loadUserResponse.getUsername(),  loadUserResponse.getPassword(),true, true, true, true, loadUserResponse.isEnable());
    }

}
