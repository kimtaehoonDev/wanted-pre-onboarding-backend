# 원티드 프리온보딩 백엔드 인턴십 - 선발 과제

사전설명회 때, 제출 이후 수정이 가능하다는 피드백을 받아, 먼저 제출하겠습니다.<br>
구현을 끝마친 이후, 사전에 원티드측에서 안내해주신 이메일을 통해 연락 드리겠습니다. 감사합니다!
<br></br>

## ✅ 이름 : 김태훈

##  ✅ 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)
### Docker Compose를 사용하는 방법 
Docker Compose를 사용해 애플리케이션을 실행할 수 있습니다.<br>
> 데이터베이스는 Docker 환경에서 띄우지 않아 외부의 DB에 접근해야 합니다.<br>
> 데이터베이스를 Docker에 띄우면 데이터를 관리하는데 어려움이 있다고 생각해 별도의 환경으로 구성했습니다.<br>
> 환경변수를 사용해 외부 DB에 접근이 가능하도록 만들어줬습니다.

애플리케이션을 실행하기 위한 순서는 다음과 같습니다.<br>
1. 작업 디렉토리를 생성합니다.
2. .env 파일을 생성합니다. 파일의 내용은 다음과 같습니다.
   ```
   DB_PASSWORD=[패스워드를 입력합니다]
   DB_USERNAME=[계정명을 입력합니다]
   DB_ADDRESS=[DB 주소를 입력합니다]
   DB_DATABASENAME= [데이터베이스 이름을 입력합니다]
   ```
3. docker-compose.yml 파일을 생성합니다.
   ```
   version: '3'
   services:
    web:
      image: kimtaehoondev/wanted-be-precourse:latest
      ports:
        - "80:8080"
      env_file:
        - .env
      ```
4. ```docker-compose up``` 명령을 실행하면 서버가 띄워진 걸 확인할 수 있습니다.<br>
   엔드포인트들은 [API 명세](https://documenter.getpostman.com/view/24050935/2s9XxyStk2)를 확인해주세요.

### 직접 배포
Docker Compose를 사용하지 않고 직접 배포할 수도 있습니다.<br>

1. ```git clone https://github.com/kimtaehoonDev/wanted-pre-onboarding-backend.git``` 명령을 실행합니다.
2. ```/src/main/resources``` 경로에 application.yml 파일을 생성합니다.
   <br> application.yml 파일은 아래와 같이 작성합니다.
    ```
    jwt:
      secret: VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa
      access-token-expires: 1800000 # 30분 (30 * 60 * 1000)
      refresh-token-expires: 604800000 # 일주일 (7 * 24 * 60 * 1000)
    
    spring:
      datasource:
        url: jdbc:mysql://localhost:[사용할 포트번호]/[사용할 DB명]
        username: [사용할 계정명]
        password: [사용할 비밀번호]
        driver-class-name: com.mysql.cj.jdbc.Driver
      jpa:
        hibernate:
          ddl-auto: create
        properties:
          hibernate:
            dialect: org.hibernate.dialect.MySQL8Dialect
          show-sql: false
    
    server:
      servlet:
        encoding:
          charset: UTF-8
          enabled: true
          force: true
    
    ```
3. 2와 동일한 방식으로 ```/src/main/resources``` 경로에 application-test.yml을 만들어줍니다.
   >**❗️ ️application.yml에서 사용한 DB와 다른 DB를 사용해주세요!!!**
   <br>application-test는 테스트 환경을 분리하기 위해 사용하기 때문입니다.
   <br>동일한 DB를 쓰면 분리한 이유가 없어집니다.
4. 프로젝트의 최상단으로 이동 후, ```./gradlew test``` 명령어로 테스트가 잘 동작하는지 확인합니다.
5. ```./gradlew build``` 명령어로 빌드를 합니다.
   > 빌드 결과물은 ```/build/libs``` 경로에 ```board-0.0.1-SNAPSHOT.jar``` 이름으로 생성됩니다.
6. ```java -jar 빌드결과물``` 명령어로 서버를 실행합니다.
7. Postman 등 HTTP 요청을 보낼 수 있는 프로그램에 접속합니다. (엔드포인트 호출)
8. ```POST localhost:8080/api/auth/signup``` URL로 요청을 보냅니다
    - Body에는 JSON 양식으로 아래와 같은 요청을 보냅니다.
    ```
    {
        "email": "[이메일을 입력합니다]",
        "pwd": "[비밀번호를 입력합니다]"
    }
    ```
9. 그 외 사용가능한 엔드포인트들은 바로 하단의 API 명세에서 확인 가능합니다.


## ✅ [API 명세(request/response 포함)](https://documenter.getpostman.com/view/24050935/2s9XxyStk2)
링크를 확인해주세요.<br>


노란색 동그라미 부분을 눌러 여러 응답값을 확인할 수 있습니다.

<img width="1157" alt="스크린샷 2023-08-07 오후 1 12 57" src="https://github.com/kimtaehoonDev/Mini-Dooray/assets/67636607/ced5c049-366b-47a0-ae64-479802449c0f">

## ✅ 데이터베이스 테이블 구조
- Members와 Member_Roles는 식별 관계입니다.

<img width="771" alt="스크린샷 2023-08-07 오후 2 13 18" src="https://github.com/lordmyshepherd-edu/wanted-pre-onboardung-backend-selection-assignment/assets/67636607/26a8befd-ce44-42b3-a1af-30ab03746397">

## ✅ [구현한 API의 동작을 촬영한 데모 영상 링크](https://www.youtube.com/watch?v=wmFwLQj2Phs)
링크를 확인해주세요.

## ✅ 구현 방법 및 이유에 대한 간략한 설명

### Controller

---
#### Controller - Service - Repository 구조를 가져갑니다.
  - 하나의 객체가 너무 많은 책임을 가져가지 않도록 역할에 따라 구조를 분리했습니다.
#### Controller에서는 객체들에 대해 RequestBody 어노테이션을 사용합니다.
  - API 서버에서는 객체로 요청과 응답을 관리하는 게 더 간편하다 생각했습니다.
  - HTML Form 태그를 사용하지 않는다 생각해 모든 요청에 대해 RequestBody 어노테이션을 사용했습니다. 

#### Controller의 Request DTO에 Bean Validation을 사용했습니다.
  - 검증에 대한 로직을 Controller에서 분리하기 위함입니다.

### Repository

---
#### 각 엔티티에 createdAt, updatedAt 필드를 넣어주기 위해 BaseEntity를 만들었습니다.
  - 해당 정보는 엔티티에 상관없이 사용되는 부분이라 생각해 BaseEntity를 만들어 상속을 해줬습니다.
#### 데이터를 조회할 때, DTO Projection을 사용해 DB에서 값을 가져옵니다.
  - 영속성 컨텍스트를 사용하지 않는 상황에서는 엔티티 대신 DTO를 가져오는게 권장되기 때문입니다.

#### 통합테스트, RepositoryTest에서 H2를 사용하지 않고 실제 DB를 사용하도록 만들었습니다.
  - DB가 바뀌면서 문법이 달라 생기는 여러 문제들이 생겨, 실제 DB와 같은 환경에서 테스트가 일어나도록 변경했습니다.  

### 예외

---

#### GlobalExeptionHandler를 사용해 예외를 처리합니다.
- 컨트롤러에서 예외를 처리하는 중복 로직을 제거하기 위해 RestControllerAdvice 어노테이션을 사용했습니다.
#### 커스텀 예외를 사용했습니다.
- 이름만으로 의미를 명확하게 전달할 수 있어 커스텀 예외를 사용했습니다.
- 관리를 편하게 하기 위해 ErrorCode라는 ENUM을 사용해 커스텀 예외의 에러메세지를 관리합니다.
#### 응답값은 원시타입을 반환하지 않고 객체를 반환하도록 만들었습니다.
- 원시값을 사용하면 추후 요구사항의 변경될 때 변경해야 할 부분이 많아진다 생각해 분리해냈습니다.
- 더불어, key-value 형태로 응답을 전달해 조금 더 의미를 넣어줄 수 있을거라 생각했습니다.

### Member 도메인

---
#### 멤버 단건 조회 기능을 만들었습니다.
- HTTP 201 상태가 발생한 경우 생성된 리소스에 접근할 수 있는 URL을 보내주는 걸 권장합니다.
- 회원가입이 일어났을 때, 해당 멤버에 접근할 수 있는 URL을 사용하기 위해 기능을 만들었습니다.

### Auth 도메인

---
#### 인증에 성공하면 JWT Token을 사용자에게 반환합니다.
- 올바른 ID와 Password가 입력되면 Authentication 객체를 만들었습니다.
- 만들어진 Authentication 객체를 사용해 Access Token을 만들어줍니다.
#### SecurityContextHolder에 인증 정보를 저장해 인가를 처리합니다.
- 필터에서는 JWT 토큰을 해석해 적절한 토큰이라면 SecurityContextHolder에 저장합니다.
- 컨트롤러는 SecurityContextHolder에서 Authentication 객체를 꺼내 사용자 정보를 확인합니다.
- 서비스에게 해당 정보를 넘겨 인가를 검사합니다.

### Post 도메인

--- 
#### 게시물의 목록을 조회할 때 사용자에게 page에 대한 정보만 입력받고, size는 고정된 값을 사용해 Pageable 객체를 만듭니다.
  - 한 번에 요청으로 몇 개의 게시물을 보내줄지는 사용자가 선택하는 게 아니라 서버에서 정한 일정한 개수가 있을거라 판단하였습니다.
  - 테스트를 위해 Pageable 객체를 만들어주는 책임을 가진 PageRequestFactory 객체를 만들어 의존성을 주입했습니다.
  - 0보다 작은 page를 입력한 경우 값을 0으로 변경해줬습니다.
    <br>오류를 발생시키는 것보다 0페이지를 반환하는게 사용자에게 더 편리할거라 생각했습니다.
#### Soft Delete를 사용했습니다.
  - MySQL 등 RDB는 데이터를 삭제할 때 인덱스를 재구성해야해 성능상 문제가 생길 수 있기 때문입니다.

## AWS 환경
API 주소 : http://3.35.21.130 <br>
단순하게 API 서버와 DB 서버만을 분리했습니다.<br>
<img width="670" alt="스크린샷 2023-08-07 오후 11 07 12" src="https://github.com/kimtaehoonDev/Mini-Dooray/assets/67636607/898f1e9d-095a-4539-a694-d6d7a2971654">
