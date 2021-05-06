## **Fallback 이란?**   

정보통신기술 용어 해설에선 다음과 같이 표현한다.

- 어떤 기능이 약해지거나 제대로 동작하지 않을 때, 이에 대처하는 기능 또는 동작
- 주로, 뒤로 물러나거나 빼거나 감소하는 동작을 의미   



> 무선 LAN에서의 자동 Fallback 기능
>
> - 무선 [AP](http://www.ktword.co.kr/abbr_view.php?nav=&m_temp1=2237&id=374) 에서 [무선단말](http://www.ktword.co.kr/abbr_view.php?nav=&m_temp1=1819&id=389)이 [AP](http://www.ktword.co.kr/abbr_view.php?nav=&m_temp1=2237&id=374) 로부터 멀어져서 [신호](http://www.ktword.co.kr/abbr_view.php?nav=&m_temp1=466&id=131)의 세기가 약해지면, [전송 속도](http://www.ktword.co.kr/abbr_view.php?nav=&m_temp1=4185&id=1320)를 자동으로 낮추는 기능을 말함 



## 챗봇에서의 Fallback

NUGU 서비스에서의 챗봇 가이드는 다음과 같이 표현하고 있다.

```
User Utterance Model으로 훈련시킨 NLU 엔진이 사용자의 발화를 분석하지 못할 경우, Fallback 처리됩니다.

Fallback으로 처리되는 사용자의 발화는 경우의 수가 무한이어서 특정할 수 없기 때문에, “이해하지 못했어요.”, “제가 처리할 수 없는 요청이에요” 등과 같이 공통적인 Fallback 응답을 하는 것이 일반적입니다.

그러나, Fallback 처리된 사용자의 발화문 전체는 _UNRESOLVED_ __라는 Entity type으로 분석되므로 사용자의 발화를 Backend proxy에서 한번 더 분석하여 처리할 수도 있습니다.
```

즉 유저의 발화 모델에서 사용자 발화를 분석하지 못할 경우 특정 Fallback을 지정한다. 



## 참고

- [정보통신기술용어해설](http://www.ktword.co.kr/abbr_view.php?m_temp1=2282)

- [NUGU 개발자 가이드](https://developers-doc.nugu.co.kr/nugu-play/create-plays-with-play-builder/define-an-action/fallback)