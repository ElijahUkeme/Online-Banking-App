package com.elijah.onlinebankingapp.security.service;

import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.security.model.Role;
import com.elijah.onlinebankingapp.security.model.UserModel;
import com.elijah.onlinebankingapp.security.repository.RoleRepository;
import com.elijah.onlinebankingapp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(username);
        if (Objects.isNull(userModel)){
            throw new UsernameNotFoundException("There is no user with such username");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userModel.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(userModel.getUsername(),new BCryptPasswordEncoder().encode(userModel.getPassword()),authorities);
    }

    @Override
    public UserModel saveUser(UserModel userModel) throws DataAlreadyExistException {
        UserModel userModel1 = userRepository.findByUsername(userModel.getUsername());
        if (Objects.nonNull(userModel1)){
            throw new DataAlreadyExistException("There is already a user with this username");
        }
        //userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }

    @Override
    public Role saveRole(Role role) throws DataAlreadyExistException {
        if (Objects.nonNull(roleRepository.findByName(role.getName()))){
            throw new DataAlreadyExistException("You have already added this role");
        }
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) throws DataNotFoundException, DataAlreadyExistException {
        UserModel userModel = userRepository.findByUsername(username);
        if (Objects.isNull(userModel)){
            throw new DataNotFoundException("There is no user with this username");
      }
        Role role = roleRepository.findByName(roleName);
        if (Objects.isNull(role)){
            throw new DataNotFoundException("There is no role with the entered name");
        }
        for (Role roles: userModel.getRoles()){
            if (roles.getName().equalsIgnoreCase(role.getName())){
                throw new DataAlreadyExistException("You have already added this role to this user");
            }
        }
            userModel.getRoles().add(role);
        }


    @Override
    public UserModel getUser(String username) throws DataNotFoundException {
        UserModel userModel = userRepository.findByUsername(username);
        if (Objects.isNull(userModel)){
            throw new DataNotFoundException("There is no user with such username");
        }
        return userModel;
    }

    @Override
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }


}
