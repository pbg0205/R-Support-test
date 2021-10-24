- # 🧑‍💻 R-Support-test [지원자 : 박병기]

  ## 1. 핵심 문제 해결 전략

  ### (1) 대용량 트래픽 대비, 성능 저하 요소

  - **대용량 트래픽 대비, 성능 개선** : RDB는 범용적이고 안정적이며 데이터의 일관성을 보장하는 반면, **대량의 데이터 입력, 조회시 성능이 저하되는 문제가 존재합니다.** 수백만의 요청이 들어올 경우 RDB의 성능으로 인한 응답이 늦어지는 문제가 발생할 것으로 예상하였습니다. 예상되는 RDB 조회 성능으로는 아래와 같습니다.

    <br>

    1. `조회 수 증가로 인한 성능 저하` : 조회수를 공지 테이블의 컬럼으로 관리해 매번 조회 수를 증가의 변경 사항을 반영할 경우, **두번의 쿼리(update + select)가 발생**합니다. 이는 대용량의 트래픽이라는 점에서 고려해보았을 때, **조회 요청 x 2의 쿼리**를 수행하며 RDB 성능의 저하를 유발시킬 수 있는 원인으로 예상했습니다.
    2. `조회 쿼리 시, 발생하는 N + 1 문제` : 요구사항에 명시된 공지 조회의 경우, 공지와 연관된 첨부파일을 모두 조회합니다. 이 상황에서 발생할 수 있는 문제가 `N + 1쿼리`라고 생각합니다. 공지를 조회할 경우, 그와 연관된 엔티티를 조회하는 N개의 조회 쿼리가 발생할 것을 예상했습니다.

    <br>

  <br>

  ### (2) 성능 개선을 위한 핵심 전략

  <center><img src="https://user-images.githubusercontent.com/48561660/138586946-47c2f006-4dbe-4480-812e-e096c5ba707e.png" width="50%"></center>

  1. `캐싱을 통한 조회 성능 개선` : 조회 성능을 개선하기 위해 **Redis**를 사용하였습니다. 도메인이 공지사항인 점을 미뤄보았을 때, **커맨드(Insert, Update, Delete)에 비해 쿼리(Query)가 많이 발생**합니다. 이 점을 두고 고려해보았을 때, 공지 내용을 캐싱해두고  RDB의 부하를 낮춰 성능을 개선하고자 하였습니다.

     - `일정 조회 수 도달 시, RDB 반영` :  특정 조회 수일 경우, RDB의 조회 수를 갱신하였습니다. **매번 조회 수를 갱신할 경우, 조회 수 X 2의 쿼리하는 점을 개선**하고 싶었습니다. 그래서 캐싱된 공지의 조회 수를 증가하고 특정 조회 수에 도달할 경우 RDB의 조회 수를 갱신하도록 구현하여 조회 수 **갱신 커맨드를 최소화**하고자 하였습니다.

       ```java
       @Transactional
       public NoticeResponse getNotice(Long id) {
         ...
         if (redisNotice.isSynViewCount()) {
       		synchronizeViewCountBetweenRedisAndRdb(id, redisNotice);
         }
       	...
       }
       
       @Getter
       @ToString
       @RedisHash(("Notice"))
       public class RedisNotice {
             public boolean isSynViewCount() {
               return viewCount % 10 == 0; //조회 수가 10에 도달할 경우, 갱신 쿼리가 발생합니다.
           }
       }
       ```

       <br>

     - `패치 조인을 통한 N + 1 쿼리 개선` : 주로 캐싱된 데이터를 통해 공지를 조회하지만 만약 1차캐시에 등록되어 있지 않을 경우, 조회 시, N + 1 쿼리 문제가 발생할 것으로 예상됩니다. 이를 개선하고자 공지 조회하는 쿼리를 을 통해 부가적인 쿼리 요청을 개선하고자 하였습니다.

       ```java
       public interface RdbNoticeRepository extends JpaRepository<RdbNotice, Long> {
           @Query("SELECT rdb FROM RdbNotice rdb JOIN FETCH rdb.rdbAttachments WHERE rdb.id = :id")
           Optional<RdbNotice> findByIdUsingJoin(@Param("id") Long id);
       }
       ```

       <br>

     <br>

  ## 2.  개발 스택

  >- Java11
  >- Gradle 
  >- SpringBoot
  >- JPA
  >

  <br>

  ## 3. 의존성 관리

  > - Spring-Web
  > - Spring-Data-JPA
  >
  > - Spring-Data-Redis
  >
  > - H2-database (RDB)
  >
  > - ozimov:embedded-redis (Redis)

  <br>

  ## 4.  실행 방법

  1. git clone 명령어를 통해 프로젝트를 clone합니다.

     ```
     git clone https://github.com/pbg0205/R-Support-test.git
     ```

     <br>

  2. 프로젝트 파일을 실행합니다.

     <br>

  3. 해당 프로젝트 파일의 `request_test.http`를 통해 각 요청에 맞는 결과를 확인합니다.

     - (🚨주의!) 정상 요청을 확인을 위해 `POST` 요청을 우선적으로 부탁드립니다 😅

     ```
     rsupport-homework/src/main/resources/request_test.http
     ```

  <br>

  ## 4. 요청 처리 예시

  ### 1. 공지 등록

  - **`Request`**

    -  `POST`:localhost:8080/api/v1/notices

      ```JSON
      {
        "title" : "제목1",
        "content" : "내용1",
        "author" : "작성자",
        "startTime" : "2021-10-20T12:00",
        "endTime" : "2021-11-20T12:00",
        "attachments" : [
          {
            "name" : "첨부파일1"
          },
          {
            "name" : "첨부파일2"
          }
        ]
      }
      ```

  - **`Response`**

    ```JSON
    {
      "data": {
        "id": 1,
        "title": "제목1",
        "content": "내용1",
        "author": "작성자",
        "viewCount": 0,
        "startTime": "2021-10-20T12:00:00",
        "endTime": "2021-11-20T12:00:00",
        "attachmentResponses": [
          {
            "id": 1,
            "name": "첨부파일1"
          },
          {
            "id": 2,
            "name": "첨부파일2"
          }
        ]
      },
      "message": null,
      "httpStatus": 201
    }
    
    ```
    
    <br>

  ### 2. 공지 조회

  - **`Request`** :  `GET`:localhost:8080/api/v1/notices/{noticeId}

  - **`Response`**

    ```java
    {
      "data": {
        "id": 1,
        "title": "제목1",
        "content": "내용1",
        "author": "작성자",
        "viewCount": 1,
        "startTime": "2021-10-20T12:00:00",
        "endTime": "2021-11-20T12:00:00",
        "attachmentResponses": [
          {
            "id": 1,
            "name": "첨부파일1"
          },
          {
            "id": 2,
            "name": "첨부파일2"
          }
        ]
      },
      "message": null,
      "httpStatus": 200
    }
    ```

    

  ### 3. 공지 수정

  - **`Request`** : `PUT`:localhost:8080/api/v1/notices/{noticeId}

    ```json
    {
      "title" : "수정된 제목1",
      "content" : "수정된 내용1",
      "author" : "수정된 작성자",
      "startTime" : "2021-10-20T12:00",
      "endTime" : "2021-11-20T12:00"
    }
    ```

  - **`Response`**

    ```json
    {
      "data": {
        "id": 1,
        "title": "수정된 제목1",
        "content": "수정된 내용1",
        "author": "수정된 작성자",
        "viewCount": 1,
        "startTime": "2021-10-20T12:00:00",
        "endTime": "2021-11-20T12:00:00",
        "attachmentResponses": [
          {
            "id": 1,
            "name": "첨부파일1"
          },
          {
            "id": 2,
            "name": "첨부파일2"
          }
        ]
      },
      "message": null,
      "httpStatus": 200
    }
    ```

  <br>

  ### 4. 첨부파일 수정

  - **`Request`** : `PUT` localhost:8080/api/v1/notices/{noticeId}/attachments/{attachmentId}

    ```json
    {
      "id" : 1,
      "name" : "수정된 첨부 파일"
    }
    
    ```

  - **`Response`**

    ```json
    {
      "data": {
        "id": 1,
        "name": "수정된 첨부 파일"
      },
      "message": null,
      "httpStatus": 200
    }
    
    ```

  <br>

  ### 5. 공지 삭제 

  - **`Request`** : `DELETE` localhost:8080/api/v1/notices/{noticeId}
  - **`Response`**
    - `HttpStatusCode` : 204

  <br>

  ### 6. 잘못된 공지id 요청 시, Response

  - **`Response`**

    ```json
      "data": null,
      "message": "원하는 공지가 존재하지 않습니다",
      "httpStatus": 400
    }
    ```

  <br>

  ### 7. 잘못된 첨부파일 수정 요청시, Response

  - **`Response`**

    ```json
    {
      "data": null,
      "message": "원하는 첨부파일이 존재하지 않습니다",
      "httpStatus": 400
    }
    ```

    
