# SmallUtils       [![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)  [![Download](https://api.bintray.com/packages/xiaoshubin/maven/small-utils/images/download.svg?version=1.0.0) ](https://bintray.com/xiaoshubin/maven/small-utils/1.0.0/link)

用kotlin写的常用的工具集

# Gradle引入

```
implementation 'com.smallcake.utils:small-utils:1.0.0'
```



**SpannableStringUtils设置富文本**例如下面的World设置成粗斜体红色，点击弹出消息

```
textView.buildSpannableString {  
     addText("你好")  
     addText("World!"){ 
         isItalic = true
         isBold = true
         textColor = Color.RED
         onClick{
            showToast("难道你不觉得中文中夹杂着英文真的很low吗？")
         }  
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
