package com.fullcar.member.domain;

import org.springframework.stereotype.Service;

@Service
public class SocialIdService {

    public SocialId generateSocialId(String socialId) {
        return new SocialId(socialId);
    }
}
