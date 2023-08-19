package com.example.mammabackend.domain.user.api;

import static com.example.mammabackend.global.exception.ResponseCodes.SUCCESS;

import com.example.mammabackend.domain.user.application.interfaces.AuthService;
import com.example.mammabackend.domain.user.application.interfaces.MemberService;
import com.example.mammabackend.domain.user.domain.Member;
import com.example.mammabackend.domain.user.dto.AuthDto.LoginParam;
import com.example.mammabackend.domain.user.dto.AuthDto.LoginView;
import com.example.mammabackend.domain.user.dto.AuthDto.ReIssueParam;
import com.example.mammabackend.domain.user.dto.MemberDto;
import com.example.mammabackend.domain.user.dto.MemberDto.FindMemberEmailParam;
import com.example.mammabackend.domain.user.dto.MemberDto.FindMemberEmailView;
import com.example.mammabackend.domain.user.dto.MemberDto.IsDuplicatedEmailView;
import com.example.mammabackend.domain.user.dto.MemberDto.MemberInfoView;
import com.example.mammabackend.domain.user.dto.MemberDto.RegisterMemberParam;
import com.example.mammabackend.domain.user.dto.MemberDto.VerifyConfirmEmailView;
import com.example.mammabackend.global.common.Helper;
import com.example.mammabackend.global.common.Response;
import com.example.mammabackend.global.common.Response.Body;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final Response response;
    private final Helper helper = Helper.getInstance();

    @GetMapping("/duplication/email")
    public ResponseEntity<Body> isDuplicatedEmail(
        @Valid MemberDto.IsDuplicatedEmailParam request,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        Boolean isDuplicated = memberService.isDuplicatedEmail(request.getEmail());

        return response.ok(new IsDuplicatedEmailView(isDuplicated));
    }

    @PostMapping("/validation/request/email")
    public ResponseEntity<Body> verifyRequestEmail(
        @Valid MemberDto.IsDuplicatedEmailParam request,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        memberService.verifyRequestEmail(request.getEmail());

        return response.okMessage(SUCCESS);
    }

    @PostMapping("/validation/confirm/email")
    public ResponseEntity<Body> verifyConfirmEmail(
        @Valid MemberDto.VerifyConfirmEmailParam request,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        boolean isValid = memberService.verifyConfirmEmail(request.getEmail(),
            request.getEmailValidationCode());

        return response.ok(new VerifyConfirmEmailView(isValid));
    }

    @PostMapping("/registration")
    public ResponseEntity<Body> registerMember(@Valid RegisterMemberParam request,
        BindingResult bindingResult) {

        bindingResult = request.verifyAdditional(bindingResult);

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        memberService.registerMember(request);

        return response.okMessage(SUCCESS);
    }

    @PutMapping
    public ResponseEntity<Body> updateMember(Principal principal,
        @Valid MemberDto.UpdateMemberParam request,
        BindingResult bindingResult) {

        bindingResult = request.verifyAdditional(bindingResult);

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        Long memberSq = helper.getMemberSq(principal);

        memberService.updateMember(memberSq, request);

        return response.okMessage(SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<Body> withdrawMember(Principal principal) {

        Long memberSq = helper.getMemberSq(principal);

        memberService.withdrawMember(memberSq);

        return response.okMessage(SUCCESS);
    }

    @GetMapping
    public ResponseEntity<Body> getMemberInfo(Principal principal) {

        Long memberSq = helper.getMemberSq(principal);

        Member member = memberService.getMemberInfo(memberSq);

        return response.ok(MemberInfoView.fromEntity(member));
    }

    @PostMapping("/email")
    public ResponseEntity<Body> findMemberEmail(@Valid FindMemberEmailParam request,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        String email = memberService.findMemberEmail(request);

        return response.ok(new FindMemberEmailView(email));
    }

    @PutMapping("/password")
    public ResponseEntity<Body> findMemberPassword(@Valid MemberDto.FindMemberPasswordParam request,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        memberService.findMemberPassword(request);

        return response.ok(SUCCESS);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<Body> loginMember(@Valid LoginParam request,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        LoginView result = authService.login(request);

        return response.ok(result);
    }

    @PutMapping("/auth/reissue")
    public ResponseEntity<Body> reissueMemberToken(@Valid ReIssueParam request,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return response.fail(bindingResult);
        }

        LoginView result = authService.reissue(request.getRefreshToken());

        return response.ok(result);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Body> logoutMember(Principal principal) {

        Long memberSq = helper.getMemberSq(principal);

        authService.logout(memberSq);

        return response.okMessage(SUCCESS);
    }
}
