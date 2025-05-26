package com.TrungTinhBackend.codearena_backend.Entity;

import com.TrungTinhBackend.codearena_backend.Enum.RankEnum;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import com.TrungTinhBackend.codearena_backend.Enum.StatusUserEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String img;

    private LocalDate birthDay;

    private String address;

    private Long point;

    private Double coin;

    private RankEnum rankEnum;

    private RoleEnum roleEnum;

    private StatusUserEnum statusUserEnum;

    private LocalDateTime date;
    private LocalDateTime updateDate;

    private String provider;

    private String otp;

    private LocalDateTime otpExpiry;

    private boolean enabled;

    private boolean isDeleted;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore()
    private List<UserPointHistory> userPointHistories;

    @OneToMany(mappedBy = "user")
    @JsonIgnore()
    private List<Enrollment> enrollments;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "quiz_id")
//    @JsonIgnore()
//    private Quiz quiz;

    @ManyToMany(mappedBy = "userSubmit")
    private List<Quiz> submittedQuizzes = new ArrayList<>();


//    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
//    private List<Chat> chats;

//    @ManyToMany(mappedBy = "likedUsers")
//    @JsonIgnore
//    private Set<Blog> likedBlogs = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Blog> blogs;

    @OneToMany(mappedBy = "user")
    @JsonIgnore()
    private List<BlogComment> blogComments;

    @OneToMany(mappedBy = "user")
    @JsonIgnore()
    private List<LessonComment> lessonComments;

    @OneToMany(mappedBy = "lecturer")
    private List<CourseMaterial> courseMaterials;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<PaymentTransaction> paymentTransaction;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Course> courses;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+roleEnum.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
