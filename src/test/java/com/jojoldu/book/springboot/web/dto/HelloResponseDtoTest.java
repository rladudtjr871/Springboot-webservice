package com.jojoldu.book.springboot.web.dto;


import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트() {
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        //then
        //assertTaht > assertj라는 테스트 검증 라이브러리의 검증 메소드 > 검증하고싶은 대상이 인자값(메소드 체이닝 지원)
        //isEqualTo > assertj의 동등 비교 메소드 > 같으면 참
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }

}
