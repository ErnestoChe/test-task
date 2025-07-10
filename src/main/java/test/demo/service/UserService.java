package test.demo.service;

import com.example.dto.UserSearchFilter;
import com.example.dto.UserSearchPage;

public interface UserService {
    UserSearchPage searchUsers(UserSearchFilter filter);

}
