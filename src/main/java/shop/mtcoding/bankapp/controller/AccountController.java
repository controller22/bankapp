package shop.mtcoding.bankapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.h2.mvstore.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.bankapp.dto.account.AccountDepositReqDto;
import shop.mtcoding.bankapp.dto.account.AccountDetailRespDto;
import shop.mtcoding.bankapp.dto.account.AccountSaveReqDto;
import shop.mtcoding.bankapp.dto.account.AccountTransferReqDto;
import shop.mtcoding.bankapp.dto.account.AccountWithdrawReqDto;
import shop.mtcoding.bankapp.dto.history.HistoryRespDto;
import shop.mtcoding.bankapp.handler.ex.CustomException;
import shop.mtcoding.bankapp.model.account.Account;
import shop.mtcoding.bankapp.model.account.AccountRepository;
import shop.mtcoding.bankapp.model.history.HistoryRepository;
import shop.mtcoding.bankapp.model.user.User;
import shop.mtcoding.bankapp.paging.Criteria;
import shop.mtcoding.bankapp.paging.Paging;
import shop.mtcoding.bankapp.service.AccountService;

@Controller
public class AccountController {

    @Autowired
    private HttpSession session;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private HistoryRepository historyRepository;

    @GetMapping("/account/{id}")
    public String detail(@PathVariable int id, @RequestParam(name = "gubun", defaultValue = "all") String gubun,
     Model model, @RequestParam(name = "page", defaultValue = "1") Integer page) {

        // 1. 인증체크

        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }

        // 2. 레파지토리 호출 (메서드를 3개 or 마이바티스 동적쿼리)
        AccountDetailRespDto aDto = accountRepository.findByIdWithUser(id);
        if (aDto.getUserId() != principal.getId()) {
            throw new CustomException("해당 계좌를 볼 수 있는 권한이 없습니다",
                    HttpStatus.FORBIDDEN);
        }
       
        // 전체 글 개수
        double historyListCnt = historyRepository.historyListCnt(gubun, id);
        double lastpage = Math.ceil(historyListCnt/5);


        Criteria cri = new Criteria(page);
        List<HistoryRespDto> hDtoList = historyRepository.findByGubun(gubun, id, cri.getPageStart(), cri.getPerPageNum());
        
        // 페이징 객체
        Paging paging = new Paging(page);
         
        cri.setPage(page); 
        
        System.out.println( "test12 : " + lastpage);
        // System.out.println("test8 "+paging.isPrev()); 
        // System.out.println("test9 "+paging.getEndPage());
        // System.out.println("test10 "+paging.getStartPage());

        
        model.addAttribute("cri",  cri);
        model.addAttribute("lastpage",  lastpage);//이 값을 왜 못받아와?

        model.addAttribute("paging",  paging);
        model.addAttribute("aDto", aDto); 
        model.addAttribute("hDtoList", hDtoList);
        
        return "account/detail";
    }

    // @GetMapping("/api/account/{id}")
    // public ResponseEntity<?> detailApi(@PathVariable int id, @RequestParam(name = "gubun", defaultValue = "all") String gubun,
    //  Model model, @RequestParam(name = "page", defaultValue = "1") Integer page) {

    //     // 1. 인증체크

    //     User principal = (User) session.getAttribute("principal");
    //     if (principal == null) {
    //         return "redirect:/loginForm";
    //     }

    //     // 2. 레파지토리 호출 (메서드를 3개 or 마이바티스 동적쿼리)
    //     AccountDetailRespDto aDto = accountRepository.findByIdWithUser(id);
    //     if (aDto.getUserId() != principal.getId()) {
    //         throw new CustomException("해당 계좌를 볼 수 있는 권한이 없습니다",
    //                 HttpStatus.FORBIDDEN);
    //     }
       
    //     // 전체 글 개수
    //     double historyListCnt = historyRepository.historyListCnt(gubun, id);
    //     double lastpage = Math.ceil(historyListCnt/5);


    //     Criteria cri = new Criteria(page);
    //     List<HistoryRespDto> hDtoList = historyRepository.findByGubun(gubun, id, cri.getPageStart(), cri.getPerPageNum());
        
    //     // 페이징 객체
    //     Paging paging = new Paging(page);
         
    //     cri.setPage(page); 
        
    //     System.out.println( "test12 : " + lastpage);
    //     // System.out.println("test8 "+paging.isPrev()); 
    //     // System.out.println("test9 "+paging.getEndPage());
    //     // System.out.println("test10 "+paging.getStartPage());

        
    //     model.addAttribute("cri",  cri);
    //     model.addAttribute("lastpage",  lastpage);//이 값을 왜 못받아와?

    //     model.addAttribute("paging",  paging);
    //     model.addAttribute("aDto", aDto); 
    //     model.addAttribute("hDtoList", hDtoList);
        
    //     return "account/detail";
    // }

    @GetMapping("/account/{id}/next")
    @ResponseBody
    public List<HistoryRespDto> getNextPage(@PathVariable int id, @RequestParam(name = "gubun", defaultValue = "all") String gubun,
    Model model, Integer page) {

        int historyListCnt = historyRepository.historyListCnt(gubun, id);
        // int lastpage =Math.round(historyListCnt/5)-1;
        // System.out.println("test11 "+lastpage);
        
        Paging paging = new Paging(page);
        Criteria cri = new Criteria(page);
        List<HistoryRespDto> hDtoList = historyRepository.findByGubun(gubun, id, cri.getPageStart(), cri.getPerPageNum());
        
        paging.setCri(cri);
        paging.setTotalCount(historyListCnt);    
        
        System.out.println( "test1 : " + cri.getPage());

        
        model.addAttribute("cri",  cri);
        // model.addAttribute("lastpage",  lastpage);
        model.addAttribute("paging",  paging);
        model.addAttribute("hDtoList", hDtoList);
        
        
        return hDtoList;
    }

  


    @PostMapping("/account/transfer")
    public String transfer(AccountTransferReqDto accountTransferReqDto) {
        // 1. 인증 필요
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("로그인을 먼저 해주세요", HttpStatus.UNAUTHORIZED);
        }

        // 2. 유효성 검사
        if (accountTransferReqDto.getWAccountNumber().equals(accountTransferReqDto.getDAccountNumber())) {
            throw new CustomException("출금계좌와 입금계좌가 동일할 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getAmount() == null) {
            throw new CustomException("amount를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getAmount().longValue() <= 0) {
            throw new CustomException("이체액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getWAccountNumber() == null || accountTransferReqDto.getWAccountNumber().isEmpty()) {
            throw new CustomException("출금 계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getDAccountNumber() == null || accountTransferReqDto.getDAccountNumber().isEmpty()) {
            throw new CustomException("입금 계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getWAccountPassword() == null
                || accountTransferReqDto.getWAccountPassword().isEmpty()) {
            throw new CustomException("출금 계좌비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        // 3. 서비스 호출
        int accountId = accountService.이체하기(accountTransferReqDto, principal.getId());

        return "redirect:/account/" + accountId;
    }

    @PostMapping("/account/deposit")
    public String deposit(AccountDepositReqDto accountDepositReqDto) {
        if (accountDepositReqDto.getAmount() == null) {
            throw new CustomException("amount를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountDepositReqDto.getAmount().longValue() <= 0) {
            throw new CustomException("출금액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (accountDepositReqDto.getDAccountNumber() == null || accountDepositReqDto.getDAccountNumber().isEmpty()) {
            throw new CustomException("계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        accountService.입금하기(accountDepositReqDto);

        return "redirect:/";
    }

    @PostMapping("/account/withdraw")
    public String withdraw(AccountWithdrawReqDto accountWithdrawReqDto) {
        if (accountWithdrawReqDto.getAmount() == null) {
            throw new CustomException("amount를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountWithdrawReqDto.getAmount().longValue() <= 0) {
            throw new CustomException("출금액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (accountWithdrawReqDto.getWAccountNumber() == null || accountWithdrawReqDto.getWAccountNumber().isEmpty()) {
            throw new CustomException("계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountWithdrawReqDto.getWAccountPassword() == null
                || accountWithdrawReqDto.getWAccountPassword().isEmpty()) {
            throw new CustomException("계좌비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        int accountId = accountService.계좌출금(accountWithdrawReqDto);

        return "redirect:/account/" + accountId;
    }

    @PostMapping("/account")
    public String save(AccountSaveReqDto accountSaveReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("로그인을 먼저 해주세요", HttpStatus.UNAUTHORIZED);
        }

        if (accountSaveReqDto.getNumber() == null || accountSaveReqDto.getNumber().isEmpty()) {
            throw new CustomException("number를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountSaveReqDto.getPassword() == null || accountSaveReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        accountService.계좌생성(accountSaveReqDto, principal.getId());

        return "redirect:/";
    }

    @GetMapping({ "/", "/account" })
    public String main(Model model) { // model에 값을 추가하면 request에 저장된다
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }

        List<Account> accountList = accountRepository.findByUserId(principal.getId());
        model.addAttribute("accountList", accountList);

        return "account/main";
    }

    @GetMapping("/account/saveForm")
    public String saveForm() {
        return "account/saveForm";
    }

    @GetMapping("/account/withdrawForm")
    public String withdrawForm() {
        return "account/withdrawForm";
    }

    @GetMapping("/account/depositForm")
    public String depositForm() {
        return "account/depositForm";
    }

    @GetMapping("/account/transferForm")
    public String transferForm() {
        return "account/transferForm";
    }

}
