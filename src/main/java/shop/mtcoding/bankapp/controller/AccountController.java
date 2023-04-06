package shop.mtcoding.bankapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bankapp.dto.ResponseDto;
import shop.mtcoding.bankapp.dto.account.AccountDepositReqDto;
import shop.mtcoding.bankapp.dto.account.AccountDetailPageRespDto;
import shop.mtcoding.bankapp.dto.account.AccountDetailRespDto;
import shop.mtcoding.bankapp.dto.account.AccountSaveReqDto;
import shop.mtcoding.bankapp.dto.account.AccountTransferReqDto;
import shop.mtcoding.bankapp.dto.account.AccountWithdrawReqDto;
import shop.mtcoding.bankapp.dto.history.HistoryRespDto;
import shop.mtcoding.bankapp.dto.search.DetailSearchRespDto;
import shop.mtcoding.bankapp.handler.ex.CustomException;
import shop.mtcoding.bankapp.model.account.Account;
import shop.mtcoding.bankapp.model.account.AccountRepository;
import shop.mtcoding.bankapp.model.history.HistoryRepository;
import shop.mtcoding.bankapp.model.user.User;
import shop.mtcoding.bankapp.paging.Criteria;
import shop.mtcoding.bankapp.service.AccountService;
import shop.mtcoding.bankapp.service.HistoryService;

@Controller
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private HttpSession session;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryService historyService;

    @GetMapping("/account/{id}")
    public String detail(@PathVariable int id, @RequestParam(name = "gubun", defaultValue = "all") String gubun) {


         // 1. 인증체크

         User principal = (User) session.getAttribute("principal");
         if (principal == null) {
            throw new CustomException("해당 계좌를 볼 수 있는 권한이 없습니다",
                     HttpStatus.FORBIDDEN);
         }
     
         // 2. 레파지토리 호출 (메서드를 3개 or 마이바티스 동적쿼리)
         AccountDetailRespDto aDto = accountRepository.findByIdWithUser(id);
         if (aDto.getUserId() != principal.getId()) {
             throw new CustomException("해당 계좌를 볼 수 있는 권한이 없습니다",
                     HttpStatus.FORBIDDEN);
         }


        return "account/detail";
    }



    @GetMapping("/api/account/{id}")
    public ResponseEntity<?> detailApi(@PathVariable int id, @RequestParam(name = "gubun", defaultValue = "all") String gubun,
     @RequestParam(name = "page", defaultValue = "1") Integer page) {

     // 1. 인증체크    
     
     // 2. 레파지토리 호출 (메서드를 3개 or 마이바티스 동적쿼리)
     AccountDetailRespDto aDto = accountRepository.findByIdWithUser(id);
         
     
      // 전체 글 개수
      double historyListCnt = historyRepository.historyListCnt(gubun, id);
      double lastpage = Math.ceil(historyListCnt/5);


      Criteria cri = new Criteria(page);
      List<HistoryRespDto> hDtoList = historyRepository.findByGubun(gubun, id, cri.getPageStart(), cri.getPerPageNum());    
      
      AccountDetailPageRespDto accountDetailPageRespDto = new AccountDetailPageRespDto(hDtoList, aDto, lastpage);
        
      System.out.println("lastpage : "+accountDetailPageRespDto.getLastpage());
      System.out.println("fullname : "+accountDetailPageRespDto.getADto().getFullname());
      System.out.println("HDtoList : "+accountDetailPageRespDto.getHDtoList().get(id).getReceiver());

      return new ResponseEntity<>(new ResponseDto<>(1, "거래내역 불러오기 성공", accountDetailPageRespDto), HttpStatus.OK);
    }

    
    @PostMapping("/api/account/{id}/next")
    @ResponseBody
    public ResponseEntity<?>  getNextPage (@PathVariable int id, @RequestParam(name = "gubun", defaultValue = "all") String gubun,
    Integer page) {
        Criteria cri = new Criteria(page);
        // if (detailSearchRespDto.getSearchString()!=null) {
            // List<DetailSearchRespDto> detailList = historyService.거래내역검색(detailSearchRespDto.getGubun(), id, 
            // cri.getPageStart(), cri.getPerPageNum(), detailSearchRespDto.getSearchString(),detailSearchRespDto.getLocalPage());
            // System.out.println("디버깅122 : "+detailSearchRespDto.getSearchString());
            // return new ResponseEntity<>(new ResponseDto<>(1, "검색 성공", detailList),
            //         HttpStatus.OK);
        // }
            List<HistoryRespDto> hDtoList = historyRepository.findByGubun(gubun, id, cri.getPageStart(), cri.getPerPageNum());
            System.out.println("디버깅 00 "+cri.getPageStart());
    
            return new ResponseEntity<>(new ResponseDto<>(1, "검색 성공", hDtoList),
                    HttpStatus.OK);   
       
        }
    




    @PostMapping("/api/account/{id}/search")
    public ResponseEntity<?> searchList(@PathVariable int id, @RequestParam(name = "page", defaultValue = "1")
    Integer page, @RequestBody DetailSearchRespDto detailSearchRespDto) {
        
        System.out.println("디버깅123 "+page);
        
        Criteria cri = new Criteria(page);
        
        
        List<DetailSearchRespDto> detailList = historyService.거래내역검색(detailSearchRespDto.getGubun(), id, 
        cri.getPageStart(), cri.getPerPageNum(), detailSearchRespDto.getSearchString(),detailSearchRespDto.getLocalPage());

        return new ResponseEntity<>(new ResponseDto<>(1, "검색 성공", detailList),
                HttpStatus.OK);
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
