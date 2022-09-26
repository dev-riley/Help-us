# Help:us : 기부, 봉사 플랫폼
### 기부와 봉사자가 필요한 기관과 투명한 기부와 봉사를 실천하고 싶은 사용자를 연결해주는 서비스
- 불우한 사람들을 돕기위한 기부금을 중간에 횡령하는 사건이 빈번히 발생 
→ 투명한 기부에 대한 필요성
- 물품을 직접사서 기관에 보냄으로서 기부금 횡령을 방지
- 기부만이 아닌 봉사활동에 대한 서비스 제공 -> 기관은 봉사자를 구할 수 있고, 개인은 봉사지원 및 재능기부를 통한 따뜻한 사회 실현

서비스 URL: http://k6c106.p.ssafy.io

## 팀원 역할
| 이름 | 역할 |
| ---- | ---- |
| 최다운 | 팀장 & Backend & Deploy |
| 박소희 | Backend |
| 이명원 | Backend |
| 김나영 | Frontend |
| 이다예 | Frontend |
| 이제민 | Frontend |

## 기술 스택
- Front-end
  
    `React` `Next.js` `Typescript` 
    
- Back-end
  
    `SpringBoot` `Gateway` `JPA`
    
- DB
  
    `MySQL` `S3`
    
- Deploy
  
    `AWS EC2` `Jenkins CI/CD`


## 주요 기능
- 증명서
    - 자신이 한 기부 및 봉사를 바탕으로 한 증명서 발급
    - 증명서 진위여부를 검증할 수 있는 증명서 확인
    - 증명서 진위확인시 사진을 업로드하면 사진에서 번호를 인식해서 자동입력
- 물품 기부
    - 물품 기부 요청 글 작성을 통해 기부자들 모집
    - 유통기한 가이드 제공
    - 기부 진행률 확인 가능
    - 배송 조회 가능 (택배사, 송장번호 입력시)
    - 종료기간 지나면 자동 마감
    - 물품 기부 (개인 회원)
    - 기관에서 도착 확인시 개인회원 물품기부 완료처리
    - 대댓글
- 봉사
    - 봉사 요청 글 작성을 통해 봉사자들 모집 
    - 상세 정보 제공(운영 기관, 사진, 기관 전화번호, 기관 이메일 등)
    - 지도를 통해 봉사장소 파악 가능
    - 봉사 참여 신청 (개인 회원)
    - 봉사 지원율 확인 가능
    - 봉사자 가득차거나 봉사날짜 지나면 자동 마감
    - 기관에서 봉사자 참석여부 처리 (불참석 처리되었을시 개인회원 봉사기록에 기록되지 않음)
    - 대댓글
- 재능기부
    - 재능 기부 글 작성을 통해 기관과 연결하여 재능기부 실천
    - 검색 기능
    - 대댓글
- 관리자 페이지
    - 기관 가입 승인
    - 사업자 등록증 사진으로 자동인식
    - 사업자 등록증 검증 기능
    - 회원 관리 (신고 3번 당했을시 회원 정지)
- 마이페이지
    - 활동한 기부 및 봉사 확인 가능
    - 작성한 재능기부 글 확인 가능 
- 고객센터
    - 문의, 정보수정, 신고, 도움요청
    - 댓글 관리자만 작성가능
- 뉴스
    - 기부 관련 최신 뉴스 제공



## 서비스 흐름도

![image-20220919083257074](C:\Users\qlrqo\AppData\Roaming\Typora\typora-user-images\image-20220919083257074.png)



## 주요 기능

![image-20220919083321740](C:\Users\qlrqo\AppData\Roaming\Typora\typora-user-images\image-20220919083321740.png)



## 아키텍처

![image-20220919083403343](C:\Users\qlrqo\AppData\Roaming\Typora\typora-user-images\image-20220919083403343.png)
