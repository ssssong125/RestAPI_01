package com.uni.restapi.section04.hateoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hateoas")
public class HateoasTestController {

    /*
     * Dependency 추가
     *
     *  * dependencies {
     *  * 	...
     *  * 	implementation 'org.springframework.boot:spring-boot-starter-hateoas:2.6.6'
     *  *     ...
     *  * }
     *  *
     *  * 		<dependency>
     *  * 			<groupId>org.springframework.boot</groupId>
     *  * 			<artifactId>spring-boot-starter-hateoas</artifactId>
     *  * 		</dependency>
     *  **/
    private List<UserDTO> users;
    private final DtoModelAssembler assembler;

    @Autowired
    public HateoasTestController(DtoModelAssembler assembler) {

        users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new java.util.Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new java.util.Date()));

        this.assembler = assembler;
    }

    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByUserNo(@PathVariable int userNo) {

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

//    @GetMapping("/users")
//    public ResponseEntity<ResponseMessage> findAllUsers() {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        List<EntityModel<UserDTO>> userWithRel =
//                users.stream().map(user -> EntityModel.of(user,
//                        linkTo(methodOn(HateoasTestController.class).findUserByUserNo(user.getNo())).withSelfRel(),
//                        linkTo(methodOn(HateoasTestController.class).findAllUsers()).withRel("users") // 자기 자신 걸때
//                        )).collect(Collectors.toList());
////        링크투 링크 생성
////        메소드on 해당 메소드 참고하겠다
//
//        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("users", userWithRel);
//
//        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap);
//
////        return ResponseEntity
////                .ok()
////                .headers(headers);
//        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
//    }

    // 위에꺼랑 똑같음
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<EntityModel<UserDTO>> usersWithRel =
//				users.stream().map(user -> assembler.toModel(user)).collect(Collectors.toList());
                users.stream().map(assembler::toModel).collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", usersWithRel);


        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}
