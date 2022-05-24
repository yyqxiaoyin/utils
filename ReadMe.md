# SmallUtils  [![](https://jitpack.io/v/xiaoshubin/utils.svg)](https://jitpack.io/#xiaoshubin/utils)[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

用kotlin写的Android常用的工具集

# Gradle引入

Step1 仓库地址新增

```
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

Step2 项目build.gradle加入

```
implementation 'com.github.xiaoshubin:utils:1.0.1'
```

#工具使用 

--------

**SpannableStringUtils一个对TextView进行了扩展的设置富文本工具**

例如下面的World设置成粗斜体红色，点击弹出消息

```
textView.buildSpannableString(true) {  
    addText("你好")  
    addText("World!"){ 
        isItalic = true
        isBold = true
        textColor = Color.RED
        onClick{showToast("难道你不觉得中文中夹杂着英文真的很low吗？")}  
    }
}
```

# License

```
Copyright 2017 The xiaoshubin
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
