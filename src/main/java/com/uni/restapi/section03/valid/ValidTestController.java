package com.uni.restapi.section03.valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/valid")
public class ValidTestController {

    @GetMapping("/users/{userNo}")
    public ResponseEntity<?> findUserByNo() throws UserNotFoundException {

        boolean check = true;

        if (check) {
            throw new UserNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * @Valid Annotation은 javax.validation에 포함된 Dependency로,
     *
     * @RequestBody Annotation으로 Mapping되는 Java 객체의 유효성 검증을 수행하는 Annotation이다.
     *
     *  valid시 pom.xml 에 추가
     * 		<dependency>
     * 		    <groupId>org.springframework.boot</groupId>
     * 		    <artifactId>spring-boot-starter-validation</artifactId>
     * 		</dependency>
     *
     * implementation 'org.springframework.boot:spring-boot-starter-validation'
     * */

    @PostMapping("/users")
    public ResponseEntity<?> registUser(@Valid @RequestBody UserDTO user) throws URISyntaxException {

        System.out.println(user);

        return ResponseEntity
                .created(URI.create("/valid/users/" + "userNo"))
                .build();
    }

}
