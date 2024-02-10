package com.fullcar.member.application.member;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.exception.NotFoundException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.car.CarId;
import com.fullcar.member.domain.car.CarRepository;
import com.fullcar.member.domain.mail.Mail;
import com.fullcar.member.domain.mail.MailRepository;
import com.fullcar.member.domain.member.*;
import com.fullcar.member.presentation.member.dto.request.NicknameRequestDto;
import com.fullcar.member.presentation.member.dto.request.OnboardingRequestDto;
import com.fullcar.member.presentation.member.dto.response.MemberGetResponseDto;
import com.fullcar.member.presentation.member.dto.response.NicknameResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CarRepository carRepository;
    private final MailRepository mailRepository;

    /**
     * 회원을 식별자로 조회합니다.
     * @param memberId 조회하고자 하는 회원 아이디
     * @return 회원 엔티티
     * @throws NotFoundException 존재하지 않는 회원아이디의 경우
     */
    @Transactional(readOnly = true)
    public Member findByMemberId(MemberId memberId) {
        return memberRepository.findByIdAndIsDeleted(memberId, false)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXIST_USER));
    }

    @Transactional
    public void registerOnboarding(Member member, OnboardingRequestDto onboardingRequestDto) {
        findByMemberId(member.getId()).saveOnBoardingInfo(memberMapper.toEntity(onboardingRequestDto));
    }

    @Transactional(readOnly = true)
    public MemberGetResponseDto getMember(Member member) {
        return memberMapper.toDto(member);
    }

    @Transactional(readOnly = true)
    public NicknameResponseDto checkNicknameDuplication(NicknameRequestDto nicknameRequestDto) {
        if (memberRepository.existsByNickname(nicknameRequestDto.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        else  {
            return NicknameResponseDto.builder()
                    .nickname(nicknameRequestDto.getNickname())
                    .build();
        }
    }

    @Transactional
    public void withdrawMember(Member member) {
        deleteCar(member.getCarId());
        deleteMail(member.getId());

        memberRepository.saveAndFlush(member.deleted());

    }

    private void deleteCar(CarId carId) {
        if (carRepository.existsByCarId(carId)) {
            Car car = carRepository.findByCarIdAndIsDeletedOrThrow(carId, false);
            carRepository.delete(car);
        }
    }

    private void deleteMail(MemberId memberId) {
        Mail mail = mailRepository.findByMemberId(memberId);
        mailRepository.delete(mail);
    }
}
