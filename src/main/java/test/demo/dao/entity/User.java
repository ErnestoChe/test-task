package test.demo.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"account", "emails", "phones"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 500)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 32)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false)
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<EmailData> emails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PhoneData> phones;

    // Getters and Setters
}