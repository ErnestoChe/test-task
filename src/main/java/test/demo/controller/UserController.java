package test.demo.controller;

import com.example.api.UserControllerApi;
import com.example.dto.UserSearchFilter;
import com.example.dto.UserSearchPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import test.demo.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserController implements UserControllerApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserSearchPage> usersSearchPost(UserSearchFilter userSearchFilter) {
        UserSearchPage userSearchPage = userService.searchUsers(userSearchFilter);
        return ResponseEntity.ok(userSearchPage);
    }
}
