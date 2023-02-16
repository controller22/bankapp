package shop.mtcoding.bankapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.bankapp.dto.user.JoinReqDto;
import shop.mtcoding.bankapp.handler.ex.CustomException;
import shop.mtcoding.bankapp.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * POST요청과 PUT요청시에만 BODY 데이터가 있다.
     * 해당 BODY데이터는 컨트롤러 메서드의 매개변수에 주입된다.(DS)
     * 스프링은 x-www-form-urlencoded가 기본 파싱전략
     * key=value&key=value (form태그의 기본 전송 전략)
     * 컨트롤러의 메서드는 매개변수에서 두가지 방식으로 데이터를 받는다.
     * 1. 그냥 변수, 2. DTO(Object)
     * 주의 : key이름과 변수이름이 동일해야 한다.
     */
    @PostMapping("/join")
    public String join(JoinReqDto joinReqDto) { // DTO로 받는 것이 좋다.
        // 1. POST, PUT일 때만 유효성 검사 (이것보다 우선되는 것이 인증 검사이다)
        if (joinReqDto.getUsername() == null || joinReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (joinReqDto.getPassword() == null || joinReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (joinReqDto.getFullname() == null || joinReqDto.getFullname().isEmpty()) {
            throw new CustomException("fullname을 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        // 컨벤션 : post, put, delete 할때만 하기
        // 서비스 호출 => 회원가입();
        userService.회원가입(joinReqDto);

        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }
}
