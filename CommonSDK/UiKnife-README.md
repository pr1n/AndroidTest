# UiKnife

注入式布局代理类；

使用方式：

```kotlin
@UIConfig(true, "首页", layoutRes, UiHplesInte::class)
class MainActivity : AppCompatActivity() {
    protected var mUiHples: UiHplesInte? = null//通过反射赋值
    //获取布局对象
    protected val mRootLayout by lazy { mUiHples?.getRootLayout() as? LinearLayout }
    //DataBinding 使用方式
    DataBindingUtil.inflate(layoutInflater, layoutRes, mRootLayout,true )
}
```

