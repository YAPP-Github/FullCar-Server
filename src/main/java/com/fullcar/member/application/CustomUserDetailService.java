package com.fullcar.member.application;

import com.fullcar.member.domain.Member;
import com.fullcar.member.domain.MemberId;
import com.fullcar.member.presentation.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = userService.findByMemberId(new MemberId(Long.parseLong(memberId)));
        return new CustomUserDetails(member);
    }
}
