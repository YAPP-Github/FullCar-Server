package com.fullcar.member.application.auth;


import com.fullcar.member.domain.member.Member;

public interface AuthService {
    void deleteUser(Member member);
}
