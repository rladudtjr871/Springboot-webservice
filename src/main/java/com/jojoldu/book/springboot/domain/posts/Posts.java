package com.jojoldu.book.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;



@Getter //롬복 어노테이션 > 클래스 내 모든 필드의 Getter 메소드를 자동 생성
@NoArgsConstructor //롬복 어노테이션 > 기본 생성자 자동 추가
@Entity //JPA 어노테이션 > 테이블과 링크될 클래스임을 나타낸다.
public class Posts extends BaseTimeEntity { //Entity 생성, 수정 시간 클래스를 상속 받는다.
    @Id // 해당 테이블의 PK필드를 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK의 생성 규칙을 나타낸다.
    private Long id;


    //테이블의 컬럼을 나타내며 굳이 선언하지 않아도 해당 클래스의 필드는 모두 컬럼이 된다.
    //보통 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용 > 여기선 보통 VARCHAR를 255로 주는데 그걸 500으로 늘리느라 사용
    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder //해당 클래스의 빌더 패턴 클래스를 생성 > 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
