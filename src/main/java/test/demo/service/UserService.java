package test.demo.service;

import com.example.dto.UserSearchFilter;
import com.example.dto.UserSearchPage;

public interface UserService {

    //TODO cacheable
    UserSearchPage searchUsers(UserSearchFilter filter);

}
