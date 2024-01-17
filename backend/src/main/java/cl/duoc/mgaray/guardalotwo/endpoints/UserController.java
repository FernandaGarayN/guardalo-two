package cl.duoc.mgaray.guardalotwo.endpoints;

import cl.duoc.mgaray.guardalotwo.endpoints.request.RequestLogin;
import cl.duoc.mgaray.guardalotwo.endpoints.response.ResposeLogin;
import cl.duoc.mgaray.guardalotwo.service.UserService;
import cl.duoc.mgaray.guardalotwo.service.cmd.FindUserNameCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResposeLogin> login(@RequestBody RequestLogin request){
        var user = userService.findByUserName(toCmd(request));
        return ResponseEntity.ok(ResposeLogin.builder().success(true).build());
    }

    private FindUserNameCmd toCmd(RequestLogin request) {
        return FindUserNameCmd.builder().userName(request.getUserName()).password(request.getPassword()).build();
    }
}
