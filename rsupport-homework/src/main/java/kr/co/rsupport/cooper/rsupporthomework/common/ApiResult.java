package kr.co.rsupport.cooper.rsupporthomework.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {
    private int HttpStatus;
    private T data;
    private String message;

    public static <T> ApiResult<T> success(T data, HttpStatus httpStatus) {
        return (ApiResult<T>) ApiResult.builder()
                .data(data)
                .HttpStatus(httpStatus.value())
                .build();
    }

    public static <T> ApiResult<T> fail(HttpStatus httpStatus, String message) {
        return (ApiResult<T>) ApiResult.builder()
                .HttpStatus(httpStatus.value())
                .message(message)
                .build();
    }
}