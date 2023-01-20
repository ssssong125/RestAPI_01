package com.uni.restapi.section05.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * * 		<dependency>
 * 		    <groupId>io.springfox</groupId>
 * 		    <artifactId>springfox-boot-starter</artifactId>
 * 		    <version>3.0.0</version>
 * 		</dependency>
 *
 *
 * gradle ;   implementation group: 'io.springfox', name: 'springfox-boot-starter', version: '3.0.0'
 **/
//http://localhost:8001/swagger-ui/index.html
@Api(tags = {"스프링 부트 스웨거 연동 테스트용 컨트롤러"}) // 해당하는 컨트롤러에 대한 내용 설정
@RestController
@RequestMapping("/swagger")
public class SwaggerTestController {

    private List<UserDTO> users;

    public SwaggerTestController() {
        users = new ArrayList<>();

        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new java.util.Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new java.util.Date()));
    }

    //@ApiOperation  : api method에 이름을 달고, 설명을 붙일 수 있는 어노테이션이다.
    @ApiOperation(value = "모든 회원 목록 조회")
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 번호로 회원 조회")
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);

        System.out.println(foundUser);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseMessage(200, "조회 성공!", responseMap));
    }

    @ApiOperation(value = "신규 회원 추가")
    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser) throws URISyntaxException {

        System.out.println(newUser);

        int lastUserNo = users.get(users.size() - 1).getNo();
        newUser.setNo(lastUserNo + 1);

        users.add(newUser);

        return ResponseEntity
                .created(URI.create("/entity/users/" + users.get(users.size() - 1).getNo()))
                .build();
    }

    @ApiOperation(value = "회원 정보 수정")
    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO modifyInfo, @PathVariable int userNo) throws URISyntaxException {

        System.out.println(modifyInfo);

        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        return ResponseEntity
                .created(URI.create("/entity/users/" + userNo))
                .build();
    }

    @ApiOperation(value = "회원 정보 삭제")
    @ApiResponses({
            @ApiResponse(code = 204, message = "회원 정보 삭제 성공"),
            @ApiResponse(code = 400, message = "잘못 입력 된 파라미터")
    })
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);
        users.remove(foundUser);

        return ResponseEntity
                .noContent()
                .build();
    }
}
