package com.fullcar.member.domain.auth.service;

import com.fullcar.member.domain.auth.SocialId;
import org.springframework.stereotype.Service;

@Service
public class SocialIdService {

    public SocialId generateSocialId(String socialId) {
        return new SocialId(socialId);
    }
}
