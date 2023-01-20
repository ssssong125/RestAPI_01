package com.uni.restapi.section01.response;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController // @Controller + @ResponseBody 를 합친 어노테이션
@RequestMapping("/response")
public class ResponseRestController {

    /* 1. 문자열 응답 */
    @GetMapping("/hello")
    public String helloWorld() {

        return "Hello World";
    }
    /* 2. 기본자료형 응답 */
    @GetMapping("/random")
    public int getRandomNumber() {

        return (int) (Math.random() * 10) + 1;
    }
    /* 3. Object 응답 */
    @GetMapping("/message")
    public Message getMessage() {

        return new Message(200, "메세지를 응답합니다.");
    }
    /* 4. List 응답 */
    @GetMapping("/list")
    public List<String> getList() {

        return List.of(new String[] {"사과", "바나나", "복숭아"});
    }
    /* 5. Map 응답 */
    @GetMapping("/map")
    public Map<Integer, String> getMap() {

        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(200, "정상 응답"));
        messageList.add(new Message(404, "페이지를 찾을 수 없습니다."));
        messageList.add(new Message(500, "개발자의 잘못입니다."));

        return messageList.stream().collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));
    }
    // com.uni.restapi.section01.response/sample.PNG
    /* 6. ImageFile 응답
     * produces 설정을 해주지 않으면 text/html로 응답하기 때문에 이미지가 텍스트 형태로 전송되게 된다.
     */
    @GetMapping(value="/image", produces= MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage() throws IOException {
        return getClass().getResourceAsStream("/com/uni/restapi/section01/response/sample.PNG").readAllBytes();
    }
    /* 7. RepsonseEntity를 이용한 응답
    *  * 타입은 ResponseEntity<반환할 타입> 으로 지정
	 * HTTP 응답에 필요한 요소들 중 대표적인 Status, Header , Body 를 지정하여 응답을 만든다
	 *
	 * Builder Pattern  활용하는 것을 권장 - 숫자로 된 상태 코드를 넣을 때, 잘못된 숫자를 넣을 수 있는 실수 때문
	 * Builder Pattern 을 활용하면 각 상태에 매칭되는 숫자 코드를 외울 필요 없이 Builder 메소드를 선택
	 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/201
	 * return ResponseEntity.ok()
        .headers(headers)
        .body(xxxDto);
        *
       생성자일경우 return new ResponseEntity<xxxDto>(xxxDto, headers, HttpStatus.valueOf(200));
    *
    * */
    @GetMapping("/entity")
    public ResponseEntity<Message> getEntity() {

        //Constructor  사용방식
        // return new ResponseEntity<Message>(new Message(123, "hello world"),  HttpStatus.valueOf(200));

        // Builder Pattern  활용
        return ResponseEntity.ok() // of() = 코드 200
                .body(new Message(123, "hello world"));
    }
}
