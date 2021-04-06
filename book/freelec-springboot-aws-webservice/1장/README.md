# 01 인텔리제이로 스프링 부트 시작하기



## 1.1 인텔리제이 소개

패스



## 1.2 인텔리제이 설치하기

패스



## 1.3 인텔리제이 커뮤니티에서 프로젝트 생성하기

![%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20%E1%84%87%E1%85%AE%E1%84%90%E1%85%B3%E1%84%8B%E1%85%AA%20AWS%E1%84%85%E1%85%A9%20%E1%84%92%E1%85%A9%E1%86%AB%E1%84%8C%E1%85%A1%20%E1%84%80%E1%85%AE%E1%84%92%E1%85%A7%E1%86%AB%E1%84%92%E1%85%A1%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%8B%E1%85%B0%E1%86%B8%20%E1%84%89%E1%85%A5%E1%84%87%E1%85%B5%E1%84%89%E1%85%B3%20c8a3f65bd4774faa9bf434474d4e18d9/Untitled.png](/Users/addpage/Downloads/Export-a9876a44-4ee0-4d23-8c44-5c50765b1906/스프링 부트와 AWS로 혼자 구현하는 웹 서비스 c8a3f65bd4774faa9bf434474d4e18d9/Untitled.png)

New Project - Gradle 선택 - Java 선택

![%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20%E1%84%87%E1%85%AE%E1%84%90%E1%85%B3%E1%84%8B%E1%85%AA%20AWS%E1%84%85%E1%85%A9%20%E1%84%92%E1%85%A9%E1%86%AB%E1%84%8C%E1%85%A1%20%E1%84%80%E1%85%AE%E1%84%92%E1%85%A7%E1%86%AB%E1%84%92%E1%85%A1%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%8B%E1%85%B0%E1%86%B8%20%E1%84%89%E1%85%A5%E1%84%87%E1%85%B5%E1%84%89%E1%85%B3%20c8a3f65bd4774faa9bf434474d4e18d9/Untitled%201.png](/Users/addpage/Downloads/Export-a9876a44-4ee0-4d23-8c44-5c50765b1906/스프링 부트와 AWS로 혼자 구현하는 웹 서비스 c8a3f65bd4774faa9bf434474d4e18d9/Untitled 1.png)

groupId, artifactId 설정 후 프로젝트 생성하면 다음과 같이 Spring 프로젝트가 생성된다.



## 1.4 그레이들 프로젝트를 스프링 부트 프로젝트로 변경하기

그레이들 프로젝트를 만들면 다음과 같이 `build.gradle` 에 간단한 코드가 있다.

```groovy
plugins {
    id 'java'
}

group 'com.itmining.book'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'
}

test {
    useJUnitPlatform()
}
```

이는 가장 자바 개발에 가장 기초적인 설정만 되어있는 상태이다. 여기에 스프링 부트에 필요한 설정들을 추가해보자.

다음의 코드를 `build.gradle` 최 상단에 붙인다.

```groovy
buildscript {
    ext {
        springBootVersion = '2.1.7.RELEASE'
    }
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugins:${springBootVersion}")
    }
}
```

이 코드는 이 프로젝트의 플러그인 의존성 관리를 위한 설정이다. 

인텔리제이의 플러그인 관리가 아니다.

각 구문에 대해서 알아보자

- `ext` : build.gradle에서 사용하는 전역변수를 설정하겠다는 의미이다. springBootVersion 전역 변수를 생성하고 그 값을 `2.1.7.RELEASE` 로 하겠다는 뜻이다.
- `dependencies` : classpath에 해당하는 플러그인을 사용한다는 것이다. spring-boot-gradle-plugins:2.1.7.RELEASE 플러그인을 사용하게된다.

```groovy
buildscript {
//전역 변수 설정
ext {
        springBootVersion = '2.1.7.RELEASE'
    }

    repositories {
        mavenCentral()
        jcenter()
    }

//의존성 플러그인 설정
dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

//선언한 플러그인 의존성들을 적용할 것인지 결정하는 코드
apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'//스프링 부트의 의존성들을 관리해주는 플러그인

group 'com.itmining.book'
version '1.0-SNAPSHOT'
sourceCompatibility = 1.8

//각종 의존성 라이브러리들을 받을 저장소
repositories {
    mavenCentral()
    jcenter()
}

//프로젝트 개발에 필요한 의존성 선언하는 곳
dependencies {
    compile('org.springframework.boot:spring-boot-starter-web')
    testCompile('org.springframework.boot:spring-boot-starter-test')
}
```

![%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20%E1%84%87%E1%85%AE%E1%84%90%E1%85%B3%E1%84%8B%E1%85%AA%20AWS%E1%84%85%E1%85%A9%20%E1%84%92%E1%85%A9%E1%86%AB%E1%84%8C%E1%85%A1%20%E1%84%80%E1%85%AE%E1%84%92%E1%85%A7%E1%86%AB%E1%84%92%E1%85%A1%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%8B%E1%85%B0%E1%86%B8%20%E1%84%89%E1%85%A5%E1%84%87%E1%85%B5%E1%84%89%E1%85%B3%20c8a3f65bd4774faa9bf434474d4e18d9/Untitled%202.png](/Users/addpage/Downloads/Export-a9876a44-4ee0-4d23-8c44-5c50765b1906/스프링 부트와 AWS로 혼자 구현하는 웹 서비스 c8a3f65bd4774faa9bf434474d4e18d9/Untitled 2.png)

## 1.2 인텔리제이 설치하기

패스

