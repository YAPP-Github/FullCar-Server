package com.fullcar.member.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApplePublicKeyList {
    private List<ApplePublicKey> keys;

    // kid, alg 값과 일치하는지 확인
    public ApplePublicKey getMatchesKey(String alg, String kid) {
        return keys.stream()
                .filter(k -> k.getAlg().equals(alg) && k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Apple JWT 값의 alg, kid 정보가 올바르지 않습니다."));
    }
}
