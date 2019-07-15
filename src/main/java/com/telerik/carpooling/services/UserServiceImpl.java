package com.telerik.carpooling.services;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.UserDto;
import com.telerik.carpooling.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    public User save(final UserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(bCryptEncoder.encode(user.getPassword()));
        newUser.setAge(user.getAge());
        newUser.setEmail(user.getEmail());
        newUser.setRole("USER");
        newUser.setDeleted(false);
        return userRepository.save(newUser);
    }

    @Override
    public User updateCurrentUserPassword(final String password,final User user){
       if(isPasswordValid(password)) {
           user.setPassword(bCryptEncoder.encode(password));
           return userRepository.save(user);
       }else return null;
    }
    @Override
    public User updateCurrentUserEmail(final String email,final User user){
        if(isEmailValid(email)){
        user.setEmail(email);
        return userRepository.save(user);
        }else return null;
    }
    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    private boolean isPasswordValid(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,128}$";
        return password.matches(regex);
    }
}
