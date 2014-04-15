transcodeDetail
===============
# 테스트 코드를 이용한 안정적인 리팩토링
TDD를 이용해서 TranscodingService의 구현부터 Job의 일부 구현까지 진행되었다. 
한 번 정리해 보자. 

#### 최초의 결과물은 아래와 같았다. 
![](<src/tdd_step1_design.png>)

하지만, 결과물이 마음에 안들었고 그래서 Job으로 기능을 옮겼다. 물론, 테스트는 그대로 유지하면서..

####그래서 다음과 같이 Job이 출현하고 불필요해진 두 개의 타입이 사라졌다. 
![](<src/tdd_step2_design.png>)

그리고, 새로운 도메인 모델인 MediaSourceFile을 추가하는 과정에서 
#### 일부 로직이 MediaSourceFile로 이동했고, 이 과정에서 MediaSourceCopier 타입이 또 사라졌다. 
![](<src/tdd_step3_design.png>)

####그리고, 무엇보다도 중요한 건, 테스트 코드를 통해서 이러한 변화 과정을 안정적으로 진행했다는 점이다. 


Simple transcoder with FFmpeg4

mvn install:install-file -Dfile=src/dependentjar/jave-1.0.2.jar -DgroupId=it.sauronsoftware -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar mvn install:install-file -Dfile=src/dependentjar/jave-1.0.2-src.zip -DgroupId=it.sauronsoftware -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar -Dclassifier=sources

Xuggler 소스 로컬 리포지토리에 등록하기

mvn install:install-file -Dfile=src/dependentjar/xuggler-src.jar -DgroupId=xuggle -DartifactId=xuggle-xuggler -Dversion=5.3 -Dpackaging=jar -Dclassifier=sources


- 참고로 여기에 등록된 소스와 내용의 저작권자 최범균님에게 있습니다.  
- 출처 : http://javacan.tistory.com/ (최범균님)

