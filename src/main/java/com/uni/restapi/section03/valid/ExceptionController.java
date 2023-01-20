package com.uni.restapi.section03.valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserNotFoundException e) {

        String code = "ERROR_CODE_00000";
        String description = "회원 정보 조회 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponse(code, description, detail), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e) {

        String code = "";
        String description = "";
        String detail = "";

        /* 에러가 있다면 */
        if(e.getBindingResult().hasErrors()) {
            detail = e.getBindingResult().getFieldError().getDefaultMessage(); //DTO에 설정한 meaasge값을 가져온다

            System.out.println(detail);

            String bindResultCode = e.getBindingResult().getFieldError().getCode();  //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            System.out.println(bindResultCode);

            switch(bindResultCode) {
                case "NotNull":
                    code = "ERROR_CODE_00001";
                    description = "필수 값이 누락되었습니다.";

            }
        }

        ErrorResponse errorResponse = new ErrorResponse(code, description, detail);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
