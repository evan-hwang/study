vuex의 state를 특정 컴포넌트에 반응성있게 반응하는 방법이 뭐가 있을까?

```vue
computed: {
  ...mapGetters([
	  'gid'	
  ])
},

watch: {
	gid (val) {
		// 할 일 처리
	}
}
```

