package com.jojoldu.book.springboot.domain.posts;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //JPA Entity 클래스들이 이 클래스를 상속할 경우 필드(createDate, modifiedDate)들도 컬럼으로 인식하도록 함
@EntityListeners(AuditingEntityListener.class) //이 클래스에 Auditing 기능을 포함시킨다,
public abstract class BaseTimeEntity {

    @CreatedDate //Entity가 생성되어 저장될 때 시간이 자동 저장
    private LocalDateTime createDate;

    @LastModifiedDate //조회한 Entity의 값을 변경한 시간이 자동 저장
    private LocalDateTime modifiedDate;
}
