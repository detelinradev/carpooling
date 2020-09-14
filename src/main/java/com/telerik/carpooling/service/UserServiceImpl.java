package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.UserDtoEdit;
import com.telerik.carpooling.model.dto.UserDtoRequest;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TripUserStatusRepository tripUserStatusRepository;
    private final DtoMapper dtoMapper;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    @Transactional
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {

        User user = dtoMapper.dtoToObject(userDtoRequest);

        if (user.getPassword() != null) {

            user.setPassword(bCryptEncoder.encode(userDtoRequest.getPassword()));

        } else throw new IllegalArgumentException("User should specify a password");

        return dtoMapper.objectToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDtoResponse updateUser(UserDtoEdit userDtoEdit, String loggedUserName) {

        User user = dtoMapper.dtoToObject(userDtoEdit);
        User loggedUser = findUserByUsername(loggedUserName);

        if (isRole_AdminOrSameUser(user, loggedUser)) {

            if (user.getPassword() != null) {

                user.setPassword(bCryptEncoder.encode(user.getPassword()));
            }

            return dtoMapper.objectToDto(userRepository.save(user));

        } else throw new IllegalArgumentException("You are not authorized to edit the user");

    }

    @Override
    public UserDtoResponse getUser(String username, String loggedUserUsername) {

        User user = findUserByUsername(username);
        User loggedUser = findUserByUsername(loggedUserUsername);

        if (isRole_AdminOrSameUser(user, loggedUser)) {

            return dtoMapper.objectToDto(user);

        } else throw new IllegalArgumentException("You are not authorized to get information for the user");
    }

    @Override
    public List<UserDtoResponse> getUsers(Integer pageNumber, Integer pageSize, String username, String firstName,
                                          String lastName, String email, String phone) {

        Slice<User> users = userRepository.findUsers(username, firstName, lastName, email, phone,
                PageRequest.of(pageNumber, pageSize, Sort.by("lastName").ascending().and(Sort.by("firstName").ascending())));

        return dtoMapper.userToDtoList(users.getContent());
    }

    @Override
    @Transactional
    public void deleteUser(String username) {

        User user = findUserByUsername(username);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByUserAndIsDeletedFalse(user);

        tripUserStatusList.forEach(tripUserStatus -> tripUserStatus.setIsDeleted(true));
        tripUserStatusList.forEach(tripUserStatusRepository::save);

        user.setIsDeleted(true);

        userRepository.save(user);
    }

    @Override
    public List<UserDtoResponse> getTopRatedUsers(@NotNull final Boolean isPassenger) {

        Slice<User> result = userRepository.findUsers(null, null, null,
                null, null, null);
        List<User> users = new ArrayList<>(result.getContent());

        if (isPassenger) {

            users.sort((a, b) -> b.getRatingAsPassenger().compareTo(a.getRatingAsPassenger()));
        } else {

            users.sort((a, b) -> b.getRatingAsDriver().compareTo(a.getRatingAsDriver()));
        }
        return dtoMapper.userToDtoList(users.stream()
                .limit(10)
                .collect(Collectors.toList()));
    }

    private boolean isRole_AdminOrSameUser(User user, User loggedUser) {

        return loggedUser.equals(user) || loggedUser.getRole().equals(UserRole.ADMIN);
    }

    private User findUserByUsername(String username) {

        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("Username is not recognized"));
    }
}
