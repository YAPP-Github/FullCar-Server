package com.fullcar.member.presentation.member.dto.response;

import com.fullcar.member.domain.car.CarId;
import com.fullcar.member.domain.member.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원 정보 응답 모델")
public class MemberGetResponseDto {

    @Schema(description = "회원 닉네임", example = "피곤한 물개")
    private String nickname;

    @Schema(description = "회사명", example = "현대 자동차")
    private String companyName;

    @Schema(description = "회사 이메일", example = "whoareyou@yanolja.com")
    private String email;

    @Schema(description = "성별")
    private Gender gender;

    @Schema(description = "차량 id")
    private CarId carId;
}
