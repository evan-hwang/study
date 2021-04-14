## 이름으로 포맷팅

Locale messages:

```js
const messages = {
  en: {
    message: {
      hello: '{msg} world'
    }
  }
}
```

Template:

```html
<p>{{ $t('message.hello', { msg: 'hello' }) }}</p>
```

Output:

```html
<p>hello world</p>
```



## 리스트로 포맷팅

Locale messages:

```js
const messages = {
  en: {
    message: {
      hello: '{0} world'
    }
  }
}
```

Template:

```html
<p>{{ $t('message.hello', ['hello']) }}</p>
```

Output:

```html
<p>hello world</p>
```

List formatting also accepts array-like objects:

```html
<p>{{ $t('message.hello', {'0': 'hello'}) }}</p>
```

Output:

```html
<p>hello world</p>
```