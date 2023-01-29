# XML파서
## 학습 목표
- [x] [키워드](#키워드)
  - [x] [XML](#XML)
    - [x] [XML Parser](#XML-구문-분석기(XML-Parser))
  - [x] [DOM](#DOM)
  - [x] [HTML](#HTML(HyperText-Markup-Language))
  - [x] [plist](#PLIST-파일)
  - [x] [Tokenizer](#Tokenizer)
  - [x] [Lexer](#Lexer)
  - [x] [Parser](#Parser)
  - [x] [AST](#AST(Abstract-Syntax-Tree))
  - [x] [Json](#Json(JavaScript-Object-Notation))
- [x] [정규 표현식](#정규-표현식)
- [x] [파싱](#파싱)
- [x] [HTML 파싱](#HTML-파싱)
- [x] [XML Parser](#XML-Parser)
- [x] [파싱 수행 과정](#파싱-수행-과정)

## 빌드 및 실행
```shell
~ $ git clone https://gist.github.com/4356792748e12b98c12ee8942dd176c9.git CS07
~ $ cd CS07
~/CS07 $ javac -cp "*" -encoding utf-8 *.java
~/CS07 $ java Application index.xml 
```

![image](https://user-images.githubusercontent.com/33227831/215258084-f298031d-fc90-45be-8bce-512547b9d3b3.png)

## 기능 요구 사항
- [x] XML DOM Parser

## 프로그래밍 요구사항
- [x] [Tokenizer](#Tokenizer-접근)
- [x] [Lexer](#Lexer-접근)
- [x] [Parser](#Parser-접근)
- [x] DOM Tree
- [x] XmlNode

## Tokenizer 접근
- Tokenize는 어떤 파일의 문자들을 하나 하나 읽어가며 XML Parser에서 설계한 의미 있는 최소 단위가 있는지 검사하고 최소 단위가 되도록
문자열들을 자르는 작업입니다. 의미 있는 최소 단위는 1글자일수도 있고 2글자 이상일수도 있습니다.
- 입력으로 주어진 문자열들을 차례대로 검사하여 **의미 있는 최소 단위**로 변환하도록 하였습니다.
- **의미 있는 최소 단위 기준의 이름**과 최소 단위를 매칭하기 위한 값은 다음과 같습니다.
  - TAG_OPENING : \<
  - TAG_CLOSING : \>
  - TAG_CLOSE_OPENING : \</
  - TAG_SELF_CLOSING : /\>
  - COMMENT : \<!
  - TEXT : a-zA-Z | .
  - ASSIGNMENT : =
  - ATTRIBUTE_VALUE : [^\n\r\"]*\"*

예를 들어 다음 그림은 index.xml 파일의 내용을 토큰화하기 위해 의미 있는 최소 단위로 나누는 과정입니다.

![image](https://user-images.githubusercontent.com/33227831/215306389-423e7e54-9d4b-4d17-84c0-2eacbf632bbc.png)

## Lexer 접근
- Lexer는 토큰화를 위해 잘라낸 문자열들에 의미를 붙여주는 작업입니다.
- 보통 Lexer가 Tokenize와 의미를 붙여주는 작업을 같이 합니다.
- Tokenizer로 인하여 최소한의 의미 있는 단위로 저장된 문자열 컬렉션을 순회하며 토큰 객체로 만들어 토큰 컬렉션을 만듭니다.
- 토큰 객체 정보
  - 토큰 타입
    - TAG_OPENING, TAG_CLOSING, TAG_CLOSE_OPENING, TAG_SELF_CLOSING, COMMENT, TEXT, ASSIGNMENT, ATTRIBUTE_VALUE
  - 토큰 값
    - "span", "\<", "\>", ...

예를 들어 다음 그림은 문자열 리스트에서 각각의 문자열에 의미를 붙여주는 과정입니다.

![image](https://user-images.githubusercontent.com/33227831/215306749-62e6c37c-b161-47ba-b4a4-642b9bee91d2.png)

## Parser 접근
- Parser는 각각의 토큰들을 입력받고 토큰들의 입력으로 문법이 맞는지 판단하고 문법이 맞다면 
그에 맞는 Dom 객체를 만들어서 파싱 트리에 넣는 작업을 수행합니다.
- Parser의 결과는 DomTree를 의미하는 파싱 트리를 반환합니다.
- XML Parser의 마지막 단계입니다.

1. 토큰중 태그가 시작하는 토큰(TAG_OPENING('\<'))이 들어오는 경우
1.1 태그의 제목 및 속성들을 스택에 저장
1.2 새 태그를 의미하는 노드 생성 및 부모 노드 설정
1.3 노드에 태그의 속성 설정
1.4 노드에 태그 이름 설정
1.5 만약 노드가 단일 태그(TAG_SELF_CLOSING)인 경우 부모 노드를 다시 설정함

2. 토큰중 텍스트 토큰(TEXT)이 들어오는 경우
2.1 현재 노드에 InnerText 리스트에 텍스트 값을 추가함

3. 토큰중 태그가 종료를 시작하는 토큰(TAG_CLOSE_OPENING('\</'))이 들어오는 경우
3.1 닫는 태그의 형식이 올바른지 검사
3.2 현재 노드의 부모 노드를 설정
   - 현재 노드를 현재 노드의 부모 노드를 가리키도록 설정함

4. 토큰중 코멘트 태그(COMMENT('\<!'))가 들어오는 경우
4.1 종료 태그(TAG_CLOSING) 토큰이 나올 때까지 스택에 저장
4.2 코멘트 태그는 영향을 미쳐서는 안되므로 스택을 초기화함

## XML Parser를 구현하면서 어려웠던점
1. Parser 구현중 부모 태그 노드 설정 
   - Parser를 구현하면서 현재 태그의 노드와 부모 태그간에 설정을 하는 부분이 어려웠습니다.
   - 단위 테스트를 만들고 디버깅을 통해서 부모 태그 노드를 가리키도록 조심스럽게 구현함

2. DomTree 객체의 stringify 메서드
   - stringify 메서드를 수행하여 결과로 XmlNode 객체들간에 관계를 계층적으로 콘솔에 출력하는 것이 어려웠습니다.
   - 이스케이프 문자인 탭(\t)을 쓰는 것이 아닌 특정한 단위 공백(공백 4칸)을 기준으로 계층적으로
   태그를 들어갈때마다 공백을 +4씩 추가하여 DomTree 구조를 계층적으로 보이도록 출력하였습니다.

3. Tokenize 과정중 의미가 되는 최소의 단위가 없는 경우
   - tokenize 과정중 각각의 문자열을 읽어나가면서 정규표현식에 매칭이 되다가 매칭이 안되는 순간에
   그 이전까지의 문자열까지 잘라서 결과 컬렉션에 넣어가는 방식이었습니다.
   - 하지만 위와 같은 방식은 의미가 되는 최소의 단위가 없는 경우 무한 루프에 빠지는 문제가 있었습니다.
   - 위와 같은 문제를 해결하기 위해 recentStr 변수를 사용하여 매칭이 안되는 순간에 recentStr와 비교합니다.
     - recentStr과 같다는 의미는 계속 무한루프를 돌려고 한다는 의미이기 때문에 예외를 발생시켜 프로그램을 종료하도록 해결하였습니다.

## 키워드
### XML
#### XML(Extensible Markup Language)이란 무엇인가?
- **트리구조의 데이터를 단순한 텍스트 형태로 나타낸 것**
- 데이터를 정의하는 규칙을 제공하는 마크업 언어
- **XML을 사용하면 공유 가능한 방식으로 데이터를 정의하고 저장할 수 있음**
- **모든 네트워크에서 데이터를 XML 파일로 전송할 수 있음**

#### XML 태그
- XML에서 태그라고 하는 마크업 기호를 사용하여 데이터를 정의함

예를 들어 서점에 대한 데이터를 나타내기 위해 <book>, <title>, <author>와 같은 태그를 만들 수 있습니다.

책 한권의 XML 문서에는 다음과 같은 내용이 포함됩니다.

```xml
<book>
  <title> Learning Amazon Web Services </title>
  <author> Mark Wilkins </author>
</book>
```

#### XML 사용시 장점
1. **비즈니스간 트랜잭션 지원**
회사가 다른 회사에 상품이나 서비스를 판매하는 경우 두 기업은 비용, 사양과 배송 일정과 같은 정보를 교환해야 합니다.

XML을 사용하면 모든 정보를 전자적으로 공유하여 사람이 없이도 복잡한 거래를 자동으로 수행하게 합니다.

2. **데이터 무결성 유지**
- 데이터 정확성 확인
- 다양한 사용자를 위해 자동으로 데이터 표시 사용자 지정
- 여러 플랫폼에 걸쳐 일관되게 데이터 저장

3. **검색 효율성 향상**
검색엔진과 같은 컴퓨터 프로그램은 다른 문서보다 XML 파일을 정렬하고 분류할 수 있습니다.

예를 들어 mark라는 단어는 명사일수도 동사일수도 있지만 XML 태그 기반 검색엔진은 mark를

정확히 분류할 수 있습니다. 

4. **유용한 애플리케이션 설계**
XML을 사용하면 애플리케이션 디자인을 편리하게 업그레이드하거나 수정할 수 있습니다.

#### XML의 목적
1. 데이터 전송
**XML을 사용하여 동일한 데이터를 서로 다른 형식으로 저장하는 두 시스템 간에 데이터를 전송할 수 있습니다.**

예를 들어 웹 사이트에서는 날짜를 MM/DD/YYYY 형식으로 저장하고 회계시스템은 날짜를 DD/MM/YYYY 형식으로 저장합니다.

XML을 사용하여 웹 사이트에서 회계시스템으로 데이터를 전송할 수 있습니다.

개발자는 다음을 자동으로 변환하는 코드를 작성할 수 있습니다.

- 웹 사이트 데이터 -> XML 형식
- XML 데이터 -> 회계 시스템 데이터
- 회계 시스템 데이터 -> XML
- XML 데이터 -> 웹 사이트 데이터

2. 웹 애플리케이션
**XML은 웹 페이지에서 볼 수 있는 데이터의 구조를 제공합니다.**

HTML 등의 다른 웹 사이트 기술은 XML과 함께 작동하여 웹 사이트 방문자에게 일관되고 관련성 있는 데이터를 제공합니다.

예를 들어 웹 사이트는 모든 방문자에게 모든 옷을 보여주는 대신 XML을 사용하여 사용자 기본 설정에 따라

사용자 지정된 웹 페이지를 생성합니다. <brand> 태그를 필터링하여 특정 브랜드의 제품을 보여줍니다.

3. 설명서
XML을 사용하여 기술 문서의 구조 정보를 지정할 수 있습니다.

예를 들어 단락, 번호 매기기 목록의 항목 및 제목에 대한 XML 태그가 있습니다.

4. 데이터 유형
많은 프로그래밍 언어에서 XML을 데이터 유형으로 지원합니다.

#### XML 파일의 구성 요소
1. XML 문서
   - <xml></xml> 태그는 XML 파일의 시작과 끝을 표시하는데 사용됨
2. XML 선언
   - <?xml version="1.0" encoding="UTF-8"?>
3. XML 요소
   - 텍스트
   - 속성
   - 기타 요소

```xml
<InvitationList>
<family>
       <aunt>
       <name>Christine</name>
        <name>Stephanie</name>
       </aunt>
</family>
</InvitationList>
```
- \<InvitationList\> : 루트 요소
- \<family\>, \<aunt\> : 다른 요소 이름

4. XML 속성
- XML 요소에 속성을 넣을 수 있음
- \<person age="22"\>

5. XML 콘텐츠
- XML 파일의 데이터
```xml
<friend>
       <name>Charlie</name>
       <name>Steve</name>
</friend>
```
- Charlie, Steve : XML 콘텐츠

#### XML 스키마
- XML 파일의 구조에 대한 몇가지 규칙이나 제한을 설명하는 문서

예를 들어 다음과 같은 방법으로 제약조건을 설명할 수 있습니다.
- 요소의 순서를 결정하는 규칙
- 콘텐츠가 요구되는 조건
- XML 파일의 콘텐츠에 대한 데이터 타입
- 데이터 무결성에 대한 제약 조건

예를 들어 서점에 대한 XML 스키마는 다음과 같은 제약조건을 적용할 수 있습니다.
1. 책 요소에는 title과 author 속성이 있음
2. 책 요소는 속성 이름을 가진 범주 요소 아래에 중첩됨
3. 책의 가격은 book 아래에 중첩된 별도의 요소임

위 제약 조건을 만족하기 위해 아래와 같이 XML 파일을 작성합니다.
```xml
<category name="Technology">
    <book title="Learning Amazon Web Services" author="Mark Wilkins">
        <price>20 USD</price>
    </book>
</category>
```

#### XML 구문 분석기(XML Parser)
- **XML 문서를 읽어 데이터를 추출하는 소프트웨어**
- **XML 파일의 구문이나 규칙을 확인하여 특정한 XML 스키마에 대해 검증할 수 있음**
- XML은 엄격한 마크업 언어이기 때문에 검증 또는 구문 오류가 있는 경우 XML 구문 분석기는 파일을 처리하지 않음

예를 들어 다음 조건중 하나라도 해당하면 XML 파서는 오류를 반환합니다.
- 닫는 태그 또는 종료 태그가 없음
- 속성 값에 따옴표가 없음
- 스키마 조건이 만족되지 않음

소프트웨어 애플리케이션은 XML 파서를 사용하여 XML 파일을 기본 데이터 유형으로 변환합니다.

XML 자체의 세부 사항에 들어갈 필요 없이 애플리케이션에 집중할 수 있습니다.

#### XML과 HTML 차이
- HTML의 용도는 데이터를 표시하는 것, XML은 데이터를 저장하고 전송함
- HTML에는 미리 정의된 태그가 있음, XML은 커스텀한 태그를 만들고 정의할 수 있음
- XML은 대/소문자를 구분하지만 HTML은 구분하지 않음
  - 예를 들어 <book> 대신 <Book>으로 태그를 작성하면 XML 파서에서 오류가 발생함

---

### DOM
#### DOM(Document Object Model, 문서 객체 모델)이란 무엇인가?
- **HTML, XML 문서에 접근하기 위한 인터페이스**
- **DOM은 문서 내에 모든 요소를 정의하고, 각각의 요소에 접근하는 방법을 제공함**
- **DOM은 웹 브라우저가 html 페이지를 인식하는 방법**
- 웹 페이지에서 \<html\>, \<body\>와 같은 태그들을 Javascript가 활용할 수 있는 객체로 만들면 문서 객체가 됨
- DOM은 웹 페이지의 객체지향 표현
- DOM 객체의 데이터들은 트리 구조로 저장되어 있음

![image](https://user-images.githubusercontent.com/33227831/214486906-018e94fc-b918-47b7-ad70-6c24e46c9df3.png)

자바스크립트 언어를 이용하여 DOM 객체에게 접근하여 원하는 요소의 데이터에 접근하거나

요소를 조작(추가/삭제/수정)할 수 있습니다.

### HTML(HyperText Markup Language)
- 웹을 이루는 가장 기초적인 구성 요소
- 하이퍼텍스트(HyperText)는 웹 페이지를 다른 페이지로 연결하는 링크를 의미함
- HTML은 웹 브라우저에 표시되는 글과 이미지 등의 다양한 내용을 표시하기 위해 "마크업"을 사용함
  - \<head\>, \<title\>, ...
- HTML 요소는 태그를 사용해서 다른 텍스트와 구분함

### PLIST 파일
- macOS 응용 프로그램에서 사용하는 "등록 설정 파일"
- 다양한 프로그램에 대한 등록 정보 및 구성 설정이 들어 있음
- PLIST 파일은 XML 문서로 형식화 되어 있음

### Tokenizer
- **소스코드를 토큰화하는 역할**
- 토큰이란 어휘 분석의 단위를 뜻하며, 단어, 단어구, 문자열 등 의미있는 단위로 정해짐
- 토큰은 어떤 요소들을 구조적으로 표현할 수 있도록 도와줌

### Lexer
- Tokenizer로 인해 쪼개진 토큰들의 의미를 분석하는 역할
- Lexer를 거치며 그 결과의 의미를 분석하는 과정 : **Lexical Analyze**

예를 들어 return 명령어를 분석합니다.
- return이라는 단어에서 r, e, t, u, r, n은 각각 따로보면 아무 의미도 가지지 않음
- Tokenzier를 거치며 return이라는 의미있는 단어가 됨 -> 토큰화
- Lexer를 거치며 이 토큰은 무언가를 반환하라는 명령이구나라고 의미를 분석함
- 해당 토큰은 {type:명령어, value: "return", child: []}와 같은 식으로 의미가 분석되어 Parser에게 전달됨

![image](https://user-images.githubusercontent.com/33227831/214489965-01fea108-0760-49db-ad02-0c4eb709b939.png)

### Parser
- Lexical Analyze된 데이터를 구조적으로 표현함
- 데이터가 올바른지 검증하는 역할도 수행함 : **Syntax Analyze**
- Parser에 의해 도출된 결과는 AST(Abstract Syntax Tree) 형태로 생성됨

![image](https://user-images.githubusercontent.com/33227831/214489982-8e102b66-025e-43ed-8cc8-4fd3b62dfdf5.png)

### AST(Abstract Syntax Tree)
- Parser 과정을 거치며 분석된 구문을 트리의 형태로 저장한 자료구조
- 분석된 소스코드를 컴퓨터가 이해할 수 있는 구조로 변경시킨 트리

![image](https://user-images.githubusercontent.com/33227831/214490040-8f688acf-13c7-45c6-b7c9-a09e329cff6c.png)

### Json(JavaScript Object Notation)
- Javascript 객체 문법으로 구조화된 데이터를 표현하기 위한 문자 기반의 표준 형식
- 웹 애플리케이션에서 데이터를 전송시 일반적으로 사용함
- Json은 문자열 형태로 존재함

예를 들어 Json은 다음과 같이 표현할 수 있습니다.

```json
{
  "squadName": "Super hero squad",
  "homeTown": "Metro City",
  "formed": 2016,
  "secretBase": "Super tower",
  "active": true,
  "members": [
    {
      "name": "Molecule Man",
      "age": 29,
      "secretIdentity": "Dan Jukes",
      "powers": [
        "Radiation resistance",
        "Turning tiny",
        "Radiation blast"
      ]
    },
    {
      "name": "Madame Uppercut",
      "age": 39,
      "secretIdentity": "Jane Wilson",
      "powers": [
        "Million tonne punch",
        "Damage resistance",
        "Superhuman reflexes"
      ]
    },
    {
      "name": "Eternal Flame",
      "age": 1000000,
      "secretIdentity": "Unknown",
      "powers": [
        "Immortality",
        "Heat Immunity",
        "Inferno",
        "Teleportation",
        "Interdimensional travel"
      ]
    }
  ]
}
```

위와 같은 json 객체를 프로그램에 로드하고 superHeroes라는 이름의 변수에 저장하면 다음과 같이 접근할 수 있습니다.
```javascript
superHeroes.homeTown
superHeroes['active']
```

하위 계층의 데이터에 접근하려면, 프로퍼티 이름과 배열 인덱스의 체인을 통해 접근하면 됩니다.
```javascript
superHeroes['members'][1]['powers'][2]
```

## 정규 표현식
- 특정한 규칙을 가진 문자열을 표현하는 형식 언어

### 문자 클래스(Character Classes)
| 제목       | 내용                                          | 예시       | 예시 대상                      | 예시 결과                                                     |
|----------|---------------------------------------------|----------|----------------------------|-----------------------------------------------------------|
| 문자 집합    | 중괄호안에 있는 문자들을 매칭함                           | [aeiou]  | glib jocks vex dwarves!    | gl**i**b j**o**cks v**e**x dw**a**rv**e**s!               |
| 부정 집합    | 중괄호안에 있는 문자들을 제외한 문자들을 매칭함                  | [^aeiou] | glib jocks vex dwarves!    | **gl** i **b j** o **cks v** e **x dw** a **rv** e **s!** |
| 범위 문자 집합 | 중괄호안에 있는 문자들의 범위에 해당하는 문자들을 매칭함             | [g-s]    | abcdefghijklmnopqrstuvwxyz | abcdef**ghijklmnopqrs**tuvwxyz                            |
| 점        | 줄바꿈을 제외한 모든 문자와 매칭함. [^\n\r]과 일치함           | .        | glib jocks vex dwarves!    | **glib jocks vex dwarves!**                               |
| 어떤 매칭    | 줄바꿈을 포함한 모든 문자와 매칭함                         | [\s\S]   | glib jocks vex dwarves!    | **glib jocks vex dwarves!**                               |                              |
| 단어       | 어떤 단어를 매칭함(알파벳, 숫자, 언더바). [A-Za-z0-9_]과 동일함 | \w       | bonjour, mon frère         | **bonjour**, **mon fr**è **re**                           |                          |
| 단어가 아닌것  | 단어가 아닌것을 매칭함. [^A-Za-z0-9_]과 일치함            | \W       | bonjour, mon frère         | bonjour**, **mon ** **fr **è**re                          |

### 앵커(Anchors)
| 제목       | 내용                    | 예시   | 예시 대상               | 예시 결과                                |
|----------|-----------------------|------|---------------------|--------------------------------------|
| 시작       | 입력값의 시작과 일치하는 부분을 매칭함 | ^\w+ | she sells seashells | **she** sells seashells              |
 | 종료       | 입력값의 종료와 일치하는 부분을 매칭함 | \w+$ | she sells seashells | she sells **seashells**              |
| 단어 경계    | 단어의 경계 부분 위치를 매칭함     | s\b  | she sells seashells | she sell**s** seashell**s**          |
| 단어 경계 부정 | 단어의 경계 부분이 아닌 위치를 매칭함 | s\B  | she sells seashells | **s**he **s**ells **s**ea **s**hells |

### 이스케이프 문자
| 제목                | 내용                                                   | 예시     | 예시 대상           | 예시 결과               |
|-------------------|------------------------------------------------------|--------|-----------------|---------------------|
| 예약어               | +*?^$\.[]{}() 바, 이 문자들은 예약어입니다. 그대로 사용하기 위해서는 \가 필요함 | \+     | 1 + 1 = 2       | 1 **+** 1 = 2       | 
 | 8진수 이스케이프 \000    | \000 형식의 8진수 이스케이프 문자, 값은 255(\377)보다 작아야함           | \251   | RegExr is ©2014 | RegExr is **©**2014 |
| 16진수 이스케이프 \xFF   | \FF 안에서 16진수값을 가지는 문자를 매칭함                           | \xA9   | RegExr is ©2014 | RegExr is **©**2014 |
| 유니코드 이스케이프 \uFFFF | \uFFFF 안에서 해당 유니코드 값을 가지는 문자를 매칭함                    | \u00A9 | RegExr is ©2014 | RegExr is **©**2014 |
| 제어 문자 이스케이프 \cI   | \cZ 안에서 이스케이프가 붙힌 제어 문자를 매칭함. \cA ~ \cZ 범위를 가짐.      | \cI    | 탭이 매칭됨          |
| 탭 \t              | 탭 문자가 매칭됨                                            | \t     |                 |
| 라인 피드 \n          | 개행 문자가 매칭됨                                           | \n     |                 |
| 수직 탭 \v           | 수직 탭 문자가 매칭됨                                         | \v     |                 |
| 폼 피드 \f           | 폼 피드 문자가 매칭됨                                         | \f     |                 |
| 캐리지 리턴 \r         | 캐리지 리턴 문자가 매칭됨                                       | \r     |                 |
| 널문자 \0            | 널 문자를 매칭함                                            | \0     |                 |

### 그룹 및 참조
| 제목                      | 내용                                 | 예시                    | 예시 대상           | 예시 결과                       |
|-------------------------|------------------------------------|-----------------------|-----------------|-----------------------------|
| 그룹 캡처 (ABC)             | 소괄호 안에 있는 문자열들을 매칭함                | (ha)+                 | hahaha haa hah! | **hahaha** **ha**a **ha**h! |
| 이름있는 그룹 캡처 (?<name>ABC) | 소괄호 안에 있는 문자열을 매칭하고 이름을 설정함        | /\((?<area>\d\d\d)\)/ | 010             | **010**                     |
| 비캡처 그룹                  | 캡처 그룹을 생성없이 여러개의 토큰들을 그룹핑함 (?:ABC) | (?:ha)+               | hahaha haa hah! | **hahaha** **ha**a **ha**h! |


### Lookaround
| 제목                       | 내용                                  | 예시        | 예시 대상           | 예시 결과                   |
|--------------------------|-------------------------------------|-----------|-----------------|-------------------------|
| 긍정적인 Lookaround (?=ABC)  | ABC와 일치하는 그룹을 찾되 앞쪽과 일치한 그룹을 매칭함    | \d(?=px)  | 1pt 2px 3em 4px | 1pt **2**px 3em **4**px |
| 부정적인 Lookaround (?!ABC)  | ABC와 일치하지 않는 그룹을 찾되 앞쪽과 일치한 그룹을 매칭항 | \d(?!px)  | 1pt 2px 3em 4px | **1**pt 2px **3**em 4px |
| 긍정적인 Lookaround (?<=ABC) | ABC와 일치하는 그룹을 찾되 뒤쪽과 일치한 그룹을 매칭함    | (?<=pt)\d | pt1, px2, pt3   | pt**1**, px2, pt**3**   |
| 부정적인 Lookaround (?<ABC)  | ABC와 일치하지 않는 그룹을 찾되 뒤쪽과 일치한 그룹을 매칭함 | (?<!pt)\d | pt1, px2, pt3   | pt1, px**2**, pt3       |

### 수량자 & 대신자(Quantifiers & Alternation)
| 제목               | 내용                     | 예시        | 예시 대상               | 예시 결과                               |
|------------------|------------------------|-----------|---------------------|-------------------------------------|
| plus +           | 최소 1개 이상 시작하는 것을 매칭함   | b\w+      | b be bee beer beers | b **be** **bee** **beer** **beers** |
| star *           | 최소 0개 이상 시작하는 것을 매칭함   | b\w*      | b be bee beer beers | **b be bee beer beers**             |
| quantifier {1,3} | 1글자~3글자 사이인 것을 매칭함     | b\w{2,3}  | b be bee beer beers | b be **bee** **beer** **beer**s     |
| optional ?       | 없거나 1회 가능한 많이 일치       | colou?r   | color colour        | **color colour**                    |
| lazy ?           | 없거나 1회 가능한 적게 일치(lazy) | b\w+?     | b be bee beer beers | b **be** **be**e **be**er **be**ers |
| alternation 바    | or 연산과 기능이 동일하게 매칭함    | b(a바e바i)d | bad bud bod bed bid | **bad** bud bod **bed** **bid**     |

## 파싱
- **문서 파싱** : 브라우저가 html, css 등의 코드를 이해하고 사용할 수 있는 구조로 변환한 것.
파싱 결과는 보통 문서 구조를 나타내는 노드 트리인데, 파싱 트리 또는 Syntax 트리라고 부름
- 파싱 과정은 **어휘 분석**(Lexical Analyze)과 **구문 분석**(Syntax Analyze)으로 나뉨
  - 어휘 분석 : 의미없는 공백, 줄 바꿈을 제거하고 토큰(의미있는 문자) 단위로 분해하는 과정
  - 구문 분석 : 언어의 구문 규칙을 적용하는 과정. 어휘 분석기로부터 새 토큰을 받아서 구문 규칙과 일치하는지 확인함
- 파싱 작업은 **파서**에 의해서 이루어진다. 그리고 파서는 **파서 생성기**에 의해 만들어진다.
- **파서**
  - 어휘분석기로부터 새 토큰을 받아서 구문 규칙과 일치하는지 확인함
  - 규칙에 맞으면 토큰에 해당하는 노드가 파싱 트리에 추가되고, 파서는 또 다른 토큰을 요청함
  - 규칙에 맞지 않으면 파서는 토큰을 내부적으로 저장하고 토큰과 일치하는 규칙이 발견될때까지 요청함
    - 맞는 규칙이 없으면 예외로 처리하여 구문 오류를 발생시킴
- **파서 생성기**
  - 각 프로그래밍 언어를 파싱하기 위해서, 이에 알맞은 파서를 만들어내는 도구
  - Webkit 엔진에서는 어휘 파서 생성기인 Flex와 구문 파서 생성기인 Bson을 이용함
  - Webkit에서는 두 생성기에서 나온 파서를 이용해 CSS와 JavaScript를 파싱할 수 있게됨

## HTML 파싱
- **HTML의 파싱**
  - HTML은 **문맥 자유 문법**이 아니기 때문에, 정규 파서를 이용해 파싱할 수 없음
  - 별도의 HTML 전용 파서가 필요함
  - **문맥 자유 문법**
    - BNF(배커스 나우르 표기법) 형식으로 완전히 표현 가능한 문법
    - 정해진 용어와 구문 규칙에 따라 작성되어야 함
    - 문맥 자유 문법에 따라 작성된 언어 또는 형식일 경우에만 정규 파서로 파싱이 가능함
  - 문맥 자유 문법이 아니라는 의미
    - 문서가 정확한 규칙에 맞는 상태가 아니어도 해석이 가능하다는 의미
    - 대표적 문맥 자유 문법이 아닌 사례 : HTML
- 파싱 트리
  - HTML 파서는 HTML 마크업을 파싱 트리로 변환함
  - 파싱 트리를 이용해 DOM Tree가 생성됨
- HTML DTD(Dcument Type Definition)
  - HTML의 정의는 DTD 형식으로 표현됨
  - DTD를 통해 파서에 SGML 형태의 언어가 어떤 문서 타입에 해당하는지 알려줌

## XML Parser
### Parsing 정의
- 주어진 문장을 분석하거나 문법적 관계를 해석하는 것

### Parsing 필요성
- 프로그래밍이 간편
- 플랫폼에 독립적이고 프로그래밍 언어에 구애 받지 않음
- 필요한 데이터를 빠르게 처리 가능
- 웹상의 XML이 수정되어도 프로그램을 변경하지 않아도 됨

### DOM Parser
- 문서의 모든 내용을 해석한 후 메모리에 트리 구조로 펼친 후 읽어 들임
  ![image](https://user-images.githubusercontent.com/33227831/214747683-8f3c55d1-18dc-4fcf-9575-7bc763399410.png)

### DOM Parser 장점
- 특정 노드에 대한 임의접근이 자유로움
- 원하는 노드를 몇번이고 읽을 수 있음
- 문서의 수정도 가능함

### DOM Parser 단점
- 메모리 사용이 많음
- 처음 시작이 다소 느림 (문서 전체를 로딩해야 하기 때문)

## 파싱 수행 과정
![image](https://user-images.githubusercontent.com/33227831/214750155-d4724b68-4693-464e-97b4-ad73e6444844.png)
1. Tokenizer가 소스 코드를 어떤 의미 있는 요소들로 쪼갭니다.
2. Lexer는 Tokenizer에 의해 쪼갠 것들을 의미를 분석합니다.
3. Lexer에 의해서 Lexical Analyze(Tokenizer + Lexer)를 수행하여 토큰을 생성함


```
ex)
Tom is Big.

에서 T o m 는 각각 따로 존재하면 의미가 없지만,
Tom 은 사람을 가리킨다. -> Tom이 토큰이 될 수 있다.

이렇게 "토큰" 단위로 키워드, 속성등을 정의하고 그 데이터를 parser에게 넘겨준다.
```
4. Lexical Analyze되어 토큰화된 데이터를 가지고 Parser는 구문 규칙이 맞는지 체크합니다.
5. 구문 규칙이 맞으면 파싱 트리에 추가합니다.

### Tokenizer -> Lexer -> Parser의 예시
```
입력값 : [1, [2,[3]], "he is tall"]


토크나이저 결과 

[ "1", "[2,[3]]", "['he', 'is', 'tall']"]

렉서 결과 

[
	{type: 'number', value:"1" },
	{type: 'array', value: "[2, [3]]"},
	{type: 'array', value: "['he', 'is', 'tall']"},
]

파서 결과  

{
	type: 'array',
	child: [
		{type: 'number', value:'1', child:[] },
		{type: 'array', 
			child: [
			{ type: 'number', value: '2', child:[] },
			{ type: 'array', 
				child:[ {type:'number', value:'3', child:[]}
			]
		}]
		},
		{type: 'array', 
			child:[
			{ type: 'string', value: 'he', child:[] },
			{ type: 'string', value: 'is', child:[] },
			{ type: 'string', value: 'tall', child:[] },
			]
		}]
}
```
- 토큰을 나누는 기준은 주관적, 토큰은 다양한 기준으로 나뉠 수 있음



---
## References
- [XML이란 무엇인가요?](https://aws.amazon.com/ko/what-is/xml/#:~:text=XML(Extensible%20Markup%20Language)%EC%9D%80,%EC%9D%84%20%EC%88%98%ED%96%89%ED%95%A0%20%EC%88%98%20%EC%97%86%EC%8A%B5%EB%8B%88%EB%8B%A4.)
- [DOM의 개념](http://www.tcpschool.com/javascript/js_dom_concept)
- [HTML: Hypertext Markup Language](https://developer.mozilla.org/ko/docs/Web/HTML)
- [.PLIST 파일 확장명](https://ko.scriptcult.com/2961.html)
- [\[컴파일러 이론\] Tokenizer, Lexer, Parser](https://trumanfromkorea.tistory.com/79)
- [브라우저는 어떻게 동작하는가?](https://d2.naver.com/helloworld/59361)
- [문서(HTML, CSS)가 어떻게 파싱되고 어떻게 DOM Tree가 되는가?](https://skm1104.tistory.com/79)
- [자바 XML 처리 - DOM 파서(1) XML 읽기](https://m.blog.naver.com/qbxlvnf11/221324667993)
- [XML Parser](http://contents.kocw.or.kr/document/02-06-XML_Parser.pdf)
- [Fast and Compact HTML/XML Scanner/Tokenizer](https://www.codeproject.com/Articles/14076/Fast-and-Compact-HTML-XML-Scanner-Tokenizer)
- [xml](https://github.com/recp/xml)
- [\[컴파일러\] 토크나이저, 렉서, 파서 (Tokenizer, Lexer, Parser)](https://gobae.tistory.com/94)
- [java-language-parser](https://github.com/brilacasck/java-language-parser)