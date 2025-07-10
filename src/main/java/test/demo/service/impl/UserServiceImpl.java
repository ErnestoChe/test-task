package test.demo.service.impl;

import com.example.dto.UserDto;
import com.example.dto.UserSearchFilter;
import com.example.dto.UserSearchPage;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import test.demo.dao.entity.EmailData;
import test.demo.dao.entity.PhoneData;
import test.demo.dao.entity.User;
import test.demo.dao.repo.UserRepository;
import test.demo.service.UserService;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserSearchPage searchUsers(UserSearchFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

        Specification<User> spec = buildSpecification(filter);
        Page<User> userPage = userRepository.findAll(spec, pageable);

        return UserSearchPage.builder()
                .page(filter.getPage())
                .size(filter.getSize())
                .totalPages(userPage.getTotalPages())
                .totalElements((int) userPage.getTotalElements())
                .content(
                        userPage.getContent().stream()
                                .map(this::toSummary)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private Specification<User> buildSpecification(UserSearchFilter filter) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filter.getName() != null) {
                predicate = cb.and(predicate, cb.like(root.get("name"), filter.getName() + "%"));
            }
            if (filter.getDateOfBirth() != null) {
                predicate = cb.and(predicate, cb.greaterThan(root.get("dateOfBirth"), filter.getDateOfBirth()));
            }
            if (filter.getEmail() != null) {
                Join<Object, Object> emailJoin = root.join("emails", JoinType.LEFT);
                predicate = cb.and(predicate, cb.equal(emailJoin.get("email"), filter.getEmail()));
                query.distinct(true);
            }
            if (filter.getPhone() != null) {
                Join<Object, Object> phoneJoin = root.join("phones", JoinType.LEFT);
                predicate = cb.and(predicate, cb.equal(phoneJoin.get("phone"), filter.getPhone()));
                query.distinct(true);
            }

            return predicate;
        };
    }

    private UserDto toSummary(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .emails(user.getEmails().stream().map(EmailData::getEmail).collect(Collectors.toList()))
                .phones(user.getPhones().stream().map(PhoneData::getPhone).collect(Collectors.toList()))
                .build();
    }
}