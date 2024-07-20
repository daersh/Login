package com.daersh.login.user.aggregate;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "user_nickname")
    private String usernickname;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_enroll_date")
    private String userEnrollDate;
    @Column(name = "user_delete_date")
    private String userDeleteDate;
    @Column(name = "user_is_deleted")
    private boolean userIsDeleted;
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role userRole;
}
