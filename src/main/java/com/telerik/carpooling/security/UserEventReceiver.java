//package com.telerik.carpooling.security;
//
//import com.telerik.carpooling.exceptions.ApplicationException;
//import com.telerik.carpooling.models.User;
//import com.telerik.carpooling.repositories.UserRepository;
//import com.telerik.carpooling.services.services.contracts.UserService;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//
//import java.util.Optional;
//
//@Log4j2
//@RequiredArgsConstructor
//@Data
//public class UserEventReceiver {
//
//
//    private final UserService userService;
//    private final UserRepository userRepository;
//
//
//    public void receive(UserEvent<?> userEvent) {
//        UserEventType userEventType = userEvent.getUserEventType();
//        Long userId = userEvent.getUserId();
//
//        try {
//            log.warn("Handling event {} for user #{}.", userEventType, userId);
//
//            switch (userEventType) {
//                case SIGNUP_REQUESTED:
//
//                    handleSignupRequested(userId);
//                    break;
//                default:
//                    log.warn("Event {} hasn't been implemented yet.", userEventType);
//            }
//        } catch (Exception e) {
//            log.warn(new StringBuilder("Couldn't handle event ")
//                    .append(userEventType)
//                    .append(" for user #")
//                    .append(userEvent.getUserId())
//                    .append("; reason: ")
//                    .append(e.getMessage())
//                    .toString(), e);
//        }
//    }
//
//    protected void handleSignupRequested(Long userId) throws ApplicationException {
//       Optional<User> user = userService.findUser(userId);
//       if(user.isPresent()) {
//           user.addConfirmationToken(EMAIL);
//           userRepository.save(user.get());
//       }
//    }
//
//}
