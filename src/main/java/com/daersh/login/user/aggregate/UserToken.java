package com.daersh.login.user.aggregate;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "token")
public class UserToken {
    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;
    @Column(name = "token_value")
    private String tokenValue;
    @Column(name = "token_create_date")
    private String tokenCreateDate;
    @Column(name = "token_delete_date")
    private String tokenDeleteDate;
    @Column(name = "user_email")
    private String userEmail;
}
