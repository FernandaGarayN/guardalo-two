package cl.duoc.mgaray.guardalotwo.service;

import cl.duoc.mgaray.guardalotwo.repository.UserEntity;
import cl.duoc.mgaray.guardalotwo.repository.UserRepository;
import cl.duoc.mgaray.guardalotwo.service.cmd.FindUserNameCmd;
import cl.duoc.mgaray.guardalotwo.service.domain.User;
import cl.duoc.mgaray.guardalotwo.service.exception.InvalidLogin;
import cl.duoc.mgaray.guardalotwo.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findByUserName(FindUserNameCmd cmd){
        var user = userRepository.findByUserName(cmd.getUserName()).orElseThrow(()->new NotFoundException("User Not Found"));
        boolean ok = cmd.getPassword().equals(user.getPassword());
        if (!ok){
            throw new InvalidLogin("Invalid Username or Password");
        }
        return toDomain(user);
    }

    private User toDomain(UserEntity user) {
        return User.builder().userName(user.getUserName()).id(user.getId()).address(user.getAddress()).
                subsidiary(user.getSubsidiary()).build();
    }
}
