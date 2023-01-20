package com.uni.restapi.section02.responseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {

    /* ResponseEntity란?
     * 결과 데이터와 http 상태 코드를 직접 제어할 수 있는 클래스이다.
     * HttpStatus, HttpHeaders, HttpBody를 포함한다.
     *
     * https://restfulapi.net/resource-naming/  네이밍참고 RESTful API를 설계할 때 복수형으로 설계하도록 권장
     * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html
     * */
    private List<UserDTO> users;

    public ResponseEntityTestController() {

        users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new java.util.Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new java.util.Date()));
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap); // 빌더로 만들때 이걸 바로 넣어도 괜찮음

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @GetMapping("/user/{userNo}")
    public ResponseEntity<ResponseMessage> findUser(@PathVariable int userNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        UserDTO foundUser = users.stream()
                .filter(user -> user.getNo() == userNo)
                .collect(Collectors.toList()).get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap); // 빌더로 만들때 이걸 바로 넣어도 괜찮음

        // 빌더 형태
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(responseMessage);
    }

    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser) throws URISyntaxException {

        System.out.println(newUser);

        int lastUserNo = users.get(users.size() - 1).getNo();
        newUser.setNo(lastUserNo + 1);
        newUser.setEnrollDate(new Date());
        users.add(newUser);
        System.out.println("newUser" + newUser);

        // 생성(create)시 201코드 반환
        return ResponseEntity
                .created(URI.create("/entity/users/" + users.get(users.size() - 1).getNo()))
                .build();
    }

//    @PostMapping("/users/{userNo}")
    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO modifyInfo, @PathVariable int userNo) throws URISyntaxException {

        System.out.println(modifyInfo);

        UserDTO ModifiedUser = users.stream()
                .filter(user -> user.getNo() == userNo)
                .collect(Collectors.toList()).get(0);

        ModifiedUser.setId(modifyInfo.getId());
        ModifiedUser.setPwd(modifyInfo.getPwd());
        ModifiedUser.setName(modifyInfo.getName());
//        ModifiedUsr.setEnrollDate(modifyInfo.getEnrollDate());
//        users.add(ModifiedUser);

        System.out.println("ModifiedUsr" + ModifiedUser);

        return ResponseEntity
                .created(URI.create("/entity/users/" + userNo))
                .build();
    }

    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);
        users.remove(foundUser);

        return ResponseEntity
                .noContent() //204
                .build();
    }

}
