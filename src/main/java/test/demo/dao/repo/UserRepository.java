package test.demo.dao.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import test.demo.dao.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // Поиск по email (если он уникальный в email_data)
    Optional<User> findByEmails_Email(String email);

    // Поиск по телефону
    Optional<User> findByPhones_Phone(String phone);

    @EntityGraph(attributePaths = {"phones", "emails"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);
}