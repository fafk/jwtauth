package io.github.com.fafk.jwtauth.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid UserResource userResource) {
        userService.createUser(userResource.getUsername(), userResource.getPassword());
    }

    @ResponseBody
    @ControllerAdvice
    public class UserControllerAdvice {
        @ExceptionHandler({ UserAlreadyExistsException.class })
        @ResponseStatus(CONFLICT)
        public ErrorResource userAlreadyExists(final Exception exception,
                                               final HttpServletRequest httpServletRequest) {
            return error(exception, httpServletRequest, CONFLICT);
        }

        private ErrorResource error(final Exception exception,
                                    final HttpServletRequest httpServletRequest,
                                    final HttpStatus httpStatus) {
            log.warn(exception.getMessage());
            return ErrorResource.builder()
                    .status(httpStatus.value())
                    .error(httpStatus.name())
                    .timestamp(now())
                    .path(httpServletRequest.getRequestURI())
                    .message(exception.getClass().getSimpleName())
                    .build();
        }
    }

}
