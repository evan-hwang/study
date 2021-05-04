# package-lock.json은 왜 필요할까?

어느 날 부터 인가 npm을 사용하면 `package-lock.json` 파일이 같이 생기기 시작했습니다.
이 파일의 정체가 궁금했지만 바쁘다는 핑계로 넘어가기를 몇 차례… 이제서야 `package-lock.json` 파일이 왜 생겼는지 찾아 보았습니다.



### `PACKAGE-LOCK.JSON` 파일이란

`package-lock.json` 파일은 npm을 사용해서 `node_modules` 트리나 `package.json` 파일을 수정하게 되면 자동으로 생성되는 파일입니다.
이 파일은 파일이 생성되는 시점의 의존성 트리에 대한 정확한 정보를 가지고 있습니다.

아래는 `package-lock.json` 파일의 일부입니다.

```
{
  "name": "db-http-proxy",
  "version": "0.0.1",
  "lockfileVersion": 1,
  "requires": true,
  "dependencies": {
    ...
    "abbrev": {
      "version": "1.1.1",
      "resolved": "https://registry.npmjs.org/abbrev/-/abbrev-1.1.1.tgz",
      "integrity": "sha512-nne9/IiQ/hzIhY6pdDnbBtz7DjPTKrY00P/zvPSm5pOFkl6xuGrGnXn/VtTNNfNtAfZ9/1RtehkszU9qcTii0Q=="
    },
    "abstract-logging": {
      "version": "1.0.0",
      "resolved": "https://registry.npmjs.org/abstract-logging/-/abstract-logging-1.0.0.tgz",
      "integrity": "sha1-i33q/TEFWbwo93ck3RuzAXcnjBs="
    },
    "accept": {
      "version": "3.0.2",
      "resolved": "https://registry.npmjs.org/accept/-/accept-3.0.2.tgz",
      "integrity": "sha512-bghLXFkCOsC1Y2TZ51etWfKDs6q249SAoHTZVfzWWdlZxoij+mgkj9AmUJWQpDY48TfnrTDIe43Xem4zdMe7mQ==",
      "requires": {
        "boom": "7.2.0",
        "hoek": "5.0.3"
      }
    },
    ...
  }
}
```



### PACKAGE-LOCK.JSON이 필요한 이유

`package.json` 파일의 의존성 선언에는 `version range`가 사용됩니다. `version range`란 특정 버전이 아니라 버전의 범위를 의미합니다.
가장 흔한 예로 `npm install express`를 실행하게 되면 `package.json` 파일에는 “^4.16.3”([Caret Ranges](https://github.com/npm/node-semver#caret-ranges-123-025-004))로 버전 범위가 추가됩니다.

저 `package.json` 파일로 `npm install`을 실행하면 현재는 4.16.3 버전이 설치되지만 `express`의 새로운 minor, patch가 publish 되면 동일한 `package.json` 파일로 `npm install`을 실행해도 4.17.3, 이나 4.16.4 같은 업데이트된 버전이 설치됩니다.

물론 대부분의 경우에는 문제가 없지만 간혹 업데이트된 버전이 오류를 발생시키는 경우가 있습니다.

`package-lock.json` 파일은 의존성 트리에 대한 정보를 가지고 있으며 `package-lock.json` 파일이 작성된 시점의 의존성 트리가 다시 생성될 수 있도록 보장합니다.

아래 시나리오를 통해서 설명하겠습니다.



#### 시나리오 1: PACKAGE-LOCK.JSON 파일을 소스 저장소에 커밋하지 않은 경우

1. 박과장이 npm으로 프로젝트를 만들어서 git에 소스코드를 push 합니다. 이 때 node_modules 폴더를 제외하고 package.json 파일만 커밋합니다.
2. 김대리는 소스 코드를 pull 하고 `npm install`을 실행합니다.
3. 의존성 트리의 일부 버전이 박대리와 다르게 설치됩니다.
4. 프로그램 실행시 오류가 발생합니다.
5. 김대리는 오늘도 야근을 합니다.



#### 시나리오 2: PACKAGE-LOCK.JSON 파일을 소스 저장소에 커밋한 경우

1. 박과장이 npm으로 프로젝트를 만들어서 git에 소스코드를 push 합니다. 이 때 node_modules 폴더를 제외하고 package.json 파일과 package-lock.json 파일을 같이 커밋합니다.
2. 김대리는 소스 코드를 pull 하고 `npm install`을 실행합니다.
3. 의존성 트리가 박대리가 셋팅한 환경과 동일하게 설치됩니다.
4. 프로그램이 정상적으로 실행됩니다.
5. 김대리는 정시 퇴근을 합니다.

> 실제로 가자고 앱에서 사용하고 있는 `react-native-router-flux`를 예로 들어볼까요?
> 아래는 프로젝트에서 `npm ls`로 조회한 의존성 트리에서 `react-native-router-flux` 부분만 발췌한 부분입니다.
>
> ```
> ├─┬ react-native-router-flux@4.0.0-beta.24
> │ ├── lodash.isequal@4.5.0
> │ ├── mobx@3.6.2
> │ ├─┬ mobx-react@4.4.3
> │ │ └── hoist-non-react-statics@2.5.0
> │ ├── opencollective@1.0.3
> │ ├── path-to-regexp@2.2.0
> │ ├── prop-types@15.6.1]
> │ └── react-navigation@1.0.0-beta.19
> ```

> `package.json` 파일에 버전을 `"react-native-router-flux": "4.0.0-beta.24"` 지정해도 `react-native-router-flux` 프로젝트 내부의 package.json 파일에 `react-navigation`이 Caret Range로 선언되어 있기 때문에 `react-navigation`의 버전은 `npm install`을 수행하는 시점에 따라서 달라집니다.
> 실제로 문제가 있는 `react-navigation` 1.0.0-beta21 버전이 릴리즈 된 이후 앱 빌드가 실패해서 [이슈](https://github.com/aksonov/react-native-router-flux/issues/2865)가 되기도 했습니다.(~~저도 덕분에 야근을 했습니다.~~)
> 결국 react-native-router-flux 개발자가 [Caret Range를 제거하는 패치](https://github.com/aksonov/react-native-router-flux/commit/37c6553c168b9fb6947a5e533df9bd37793f1f68#diff-b9cfc7f2cdf78a7f4b91a753d10865a2)를 했습니다.

`package-lock.json` 파일은 npm에 의해서 프로젝트의 `node_modules`나 `package.json`이 수정되는 경우 생성되거나 업데이트되며 **당시 의존성에 대한 정보를 모두 가지고 있습니다.**

따라서 생성된 `package-lock.json` 파일은 소스 저장소에 커밋해야 합니다.(반드시 커밋할 필요는 없지만 저장소에 커밋하는 것을 강력추천합니다.)



### 요약

1. `package-lock.json` 파일은 의존성 트리에 대한 정보를 모두 가지고 있습니다.
2. `package-lock.json` 파일은 저장소에 꼭 같이 커밋해야 합니다.
3. `package-lock.json` 파일은 `node_modules` 없이 배포하는 경우 반드시 필요합니다.



## 참고

- 원 블로그
  - https://hyunjun19.github.io/2018/03/23/package-lock-why-need/

- `package.json`에서 사용되는 `version range`
  - https://github.com/npm/node-semver
- npm 5 버전 릴리즈 노트
  - https://github.com/npm/npm/releases/tag/v5.0.0
- `package-lock.json` 공식문서
  - https://docs.npmjs.com/files/package-lock.json

