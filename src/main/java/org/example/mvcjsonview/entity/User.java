package org.example.mvcjsonview.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(UserViews.UserSummary.class)
    private Long id;

    @JsonView(UserViews.UserSummary.class)
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @JsonView(UserViews.UserSummary.class)
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonView(UserViews.UserDetails.class)
    private List<Order> orders;

}
