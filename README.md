# AL Runner (using CATS) - version 1
능동 학습 도구 적용의 자동화를 위한 코드

## 실행 요구 사항
Understand 설치 및 Understand command line 사용을 위한 path 설정

Understand 설치 시, path 자동 설정을 체크했다면 별도로 설정할 필요는 없다.

## 시스템 input
### 1. Run Configuration (실행 옵션)
* --trace_m (default 50) : 트레이스 집합 묶음을 총 몇 세트 생성할지에 대한 설정값
* --trace_n (default 50) : 하나의 트레이스 집합 묶음을 만들기 위해 몇 번의 모델 스텝을 반복할지에 대한 설정값
* --unwind (default 10) : CBMC unwind 옵션 설정값
* --k-induction (default 10) : CBMC k-induction 옵션 설정값
* --object-bits (default 16) : CBMC object-bits 옵션 설정값
* --filename : 능동 학습 적용을 위한 원본 C 코드 파일 이름
  * 능동 학습 적용을 위한 원본 C 코드 및 헤더 파일은 ALautomation/code 디렉토리 안에 저장되어야 한다.
  * 만약 filename이 'test'라면, ALautomation/code/test 디렉토리가 필요하고, test 디렉토리 안에 test.c 파일이 존재해야 한다.

### 2. spec.txt & global.txt (입력 파일)
* spec.txt에 입력되어야 하는 내용 및 입력 형식
  * 관심 변수 리스트 (주 관심 변수 제외)
  ```plaintext
  {관심 변수 이름} {input/state/output} {변수 범위(ex. 0,5)}
  ``` 
  * 주 관심 변수
  ```plaintext
  >> key : {주 관심 변수 이름} {state/output} {변수 범위(ex. 0,5)}
  ```
  * 관심 변수 및 주 관심 변수가 배열 변수일 경우
  ```plaintext
  [관심 변수일 경우] {관심 변수 이름} {input/state/output} {변수 범위(ex. 0,5)} arr,{배열 크기}
  [주 관심 변수일 경우] >> key : {주 관심 변수 이름} {state/output} {변수 범위(ex. 0,5)} arr,{배열 크기}
  ``` 
  * 타겟 함수 이름
  ```plaintext
  >> func : {타겟 함수 이름}
  ```
* global.txt에 입력되어야 하는 내용 및 입력 형식
  * 열거형 정보
  ```plaintext
  >> enum : {변수 이름} {열거 상수 나열 (ex.{enum1, enum2, enum3})}
  ```
  * 구조체 정보
  ```plaintext
  >> struct name : {구조체 이름} {구조체 닉네임}
  >> struct element start **반드시 필요**
  ...
  {구조체 내부 변수 정보 나열}
  ...
  >> struct element finish **반드시 필요**
  ```
  
* spec.txt & global.txt example
  * spec.txt example
  ```plaintext
  // interest variables
  packetStartByte state 0,255
  packetEndByte state 0,255
  contentsStartByte state 0,255
  contentsEndByte state 0,255
  message input 0,255 arr,30
  >> constant : PACKAGE_START_BYTE_VALUE, PACKAGE_END_BYTE_VALUE, CONTENTS_START_BYTE_VALUE, CONTENTS_END_BYTE_VALUE
  // key interest variable
  >> key : fail_status state 0,2
  // target function name
  >> func : main
  ```
 
  * global.txt example
  ```plaintext
  // enumeration
  >> enum : fail_status {NO_ACTIVE, FAIL, SUCCESS}
  // struct
  >> struct name : _EQMSProtocol EQMSProtocol
  >> struct element start
  unsigned char PACKAGE_START_BYTE;
  unsigned char PACKAGE_END_BYTE;
  unsigned char CONTENTS_START_BYTE;
  unsigned char CONTENTS_END_BYTE;
  unsigned char EQMS_TYPE_ACC;
  unsigned char EQMS_TYPE_ENV;
  unsigned char EQMS_TYPE_AIR;
  unsigned char* _buf;
  size_t _buf_length;
  int _parse_stage;
  int _current_packet_usim;
  int _expected_length;
  int _inbound_bytes;
  int _processed_msg;
  int _failed;
  int _success;
  >> struct element finish
  ```

## 시스템 output
* 원본 C 코드 파일이 저장되어 있는 디렉토리에 generate라는 폴더가 생성되고, 해당 폴더 안에 시스템 output이 저장된다.
  * model.c
  * model.h
  * make_trace.c
  * generate_trace.sh
  * gen_assume_assert_from_model.py

## 참고 문헌
1. 능동 학습 논문 : https://doi.org/10.48550/arXiv.2112.05990
2. 능동 학습 저널 : https://link.springer.com/article/10.1007/s10703-023-00433-y
