## 서론

  프로그래밍 중에 언제든 만날 수 있는 부분! 비슷한 기능을 하는 컴포넌트가 점점 늘어나고 있습니다. 이러한 문제에 경각심을 가지고 있는 분이라면 Vue의 Mixins 기능을 사용할 수 있습니다! 단순하게 비슷한 기능을 하지만 두개의 컴포넌트로 나눌 수 있습니다. 하지만 기능이 바뀐다면 두 컴포넌트 모두를 고쳐야하고, 실수를 유도할 수 있는 작업입니다. 

  믹스인을 사용하면 각 함수가 **함수형 프로그래밍**이라고 불리는 방식으로 동작하도록 도울 수 있습니다. 즉 각 함수의 핵심 로직을 공개하고, 다른 함수들과 공통되는 부분은 감출 수 있는 것이죠. 올바르게 작성한 함수는 그 기능 자체로 순수해야 한다는 것을 모토로 Mixins를 활용할 수 있습니다!



## 예제

  modal과 tooltip을 토글하는 예가 있다고 생각해봅시다. modal과 tooltip은 토글하는 기능을 제외하고 공통점이 많지 않습니다만, 하단의 코드는 뭔가 비슷한 일을 하는 것 처럼 보이죠?

```javascript
//modal
const Modal = {
  template: '#modal',
  data() {
    return {
      isShowing: false
    }
  },
  methods: {
    toggleShow() {
      this.isShowing = !this.isShowing;
    }
  },
  components: {
    appChild: Child
  }
}

//tooltip
const Tooltip = {
  template: '#tooltip',
  data() {
    return {
      isShowing: false
    }
  },
  methods: {
    toggleShow() {
      this.isShowing = !this.isShowing;
    }
  },
  components: {
    appChild: Child
  }
}
```



여기서 비슷한 부분을 추출하여 재사용할 수 있는 Mixins 모듈을 만들 수 있습니다.

```javascript
const toggle = {
  data() {
    return {
      isShowing: false
    }
  },
  methods: {
    toggleShow() {
      this.isShowing = !this.isShowing;
    }
  }
}

const Modal = {
  template: '#modal',
  mixins: [toggle],
  components: {
    appChild: Child
  }
};

const Tooltip = {
  template: '#tooltip',
  mixins: [toggle],
  components: {
    appChild: Child
  }
};
```





## 사용법

실제 프로젝트에서는 하나의 js 파일에 두 개의 컴포넌트와 Mixin을 같이 놓지 않겠죠. 

![ëë í ë¦¬ êµ¬ì¡°ë components ëë í ë¦¬ì í´ëììë mixinì ë³´ì¬ì¤ëë¤.](https://css-tricks.com/wp-content/uploads/2017/06/toggle.jpg)

위와 같이 mixins를 모아 놓는 폴더 내에 toogle.js라는 mixins 객체를 정의하고, Modal.vue와 Tooltips.vue에서 이를 가져와 사용합니다.

```javascript
import Child from './Child'
import { toggle } from './mixins/toggle'

export default {
  name: 'modal',
  mixins: [toggle],
  components: {
    appChild: Child
  }
}
```



## Merging

자 이제 우리는 컴포넌트의 구성요소가 아닌 타 객체를 Mixin 했음에도 라이프 사이클 메소드를 사용할 수 있다는 것을 알아봅시다. Vue 라이프 사이클 메소드 중 `mounted()` 메소드를 예로 들어 알아보도록 하죠. 

Mixin 객체와 Mixin을 당한 객체 모두 [Vue의 라이프 사이클 메소드](https://kr.vuejs.org/v2/guide/instance.html#%EB%9D%BC%EC%9D%B4%ED%94%84%EC%82%AC%EC%9D%B4%ED%81%B4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)를 사용할 수 있다고 말씀드렸습니다. 그럼 두 객체에 중복된 메소드가 있으면 어떤 순서로 진행될까요?

정답은 Mixins가 먼저 적용되고, 그 다음 적용된 객체가 적용됩니다. 그러므로 Mixin이 적용된 객체에서 피요에 따라 재정의를 할 수도 있다는 말이 됩니다. 

```js
//mixin
const hi = {
  mounted() {
    console.log('hello from mixin!')
  }
}

//vue instance or component
new Vue({
  el: '#app',
  mixins: [hi],
  mounted() {
    console.log('hello from Vue instance!')
  }
});

//Output in console
> hello from mixin!
> hello from Vue instance!
```



자 다음 예는 라이프 사이클 메소드 뿐만 아니라 다른 구성요소도 재정의 될 수 있으며 같은 순서로 호출된다는 것을 보여줍니다.

```js
//mixin
const hi = {
  methods: {
    sayHello: function() {
      console.log('hello from mixin!')
    }
  },
  mounted() {
    this.sayHello()
  }
}

//vue instance or component
new Vue({
  el: '#app',
  mixins: [hi],
  methods: {
    sayHello: function() {
      console.log('hello from Vue instance!')
    }
  },
  mounted() {
    this.sayHello()
  }
})

// Output in console
> hello from Vue instance!
> hello from Vue instance!
```

`console.log`가 `this.sayHello()`를 호출하고 있습니다. 예상과 다르게 Vue instance의 sayHello()만 호출되었습니다. Mixin 객체의 sayHello 메소드는 오버라이드 되었기 때문입니다.



## Global Mixin

## Global Mixin

우리가 정의 했던 toggle.js는 필요한 컴포넌트에서 import하여 사용할 수 있었습니다. 하지만 필요에 의해서 이러한 Mixin도 Global하게 구현할 수 있습니다. 마치 Plugin 패턴 처럼 동작하는 것이죠. 제 생각에는 이 기능은 지양해야할 패턴이지만, 알아둘 필요는 있습니다.

```js
Vue.mixin({
  mounted() {
    console.log('hello from mixin!')
  }
})

new Vue({
  ...
})
```

위와 같이 구현하면 모든 전역 인스턴스 구성요소들은 `mounted()` 라이프 사이클이 호출될 때 콘솔 로그를 찍게 됩니다. 이러한 기능은 최대한 지양해야하는 `안티패턴`으로 여겨집니다.



## 결론

Mixin은 재사용하고 싶은 기능을 캡슐화 하는데 사용할 수 있습니다. 하지만 이것만이 그 기능을 할 수 있는 것은 아닙니다. Plugin 패턴이나, 고차원의 컴포넌트, extend 등의 기능을 사용할 수 있으며, 각각 적합한 상황을 고려하여 사용해야합니다. 이에 대해서는 추후에 Vue 컴포넌트 재사용에 대한 고뇌라는 제목의 포스트에서 다루도록 하겠습니다.



## 참고

Vue Mixins에 대한 [공식 문서](https://kr.vuejs.org/v2/guide/mixins.html)가 있으니 참고 바랍니다.



