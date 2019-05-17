# OO Homework 2 README

### 类、抽象类、接口、枚举介绍

1. Main 主类

   负责调用其他类，框架作用

2. InputHandler 输入处理类

   主要负责输入的处理和生成初始多项式

3. OutputHandler 输出处理类

   主要负责输出求导完成的表达式

4. Expression 表达式类

   存放表达式，表达式是由一系列的项相加而成的

5. Term 项类

   存放项，项是由一系列的因子相乘而成的，一个项由4个因子（常系数、幂函数、sin部分、cos部分相乘而得）

6. Divisor 因子类，抽象类

   是幂函数类(PowFun)、三角函数类(TriFun)、常系数类(ConNum)的父类

7. PowFun 幂函数类

   是每一项的幂函数部分

8. TriFun 三角函数类

   是每一项的sin部分、cos部分。注意：一个TriFun的对象只能是sin或者cos其中一者

9. ConNum 常系数类

   是每一项的系数部分

10. TriType 三角类型枚举

    内有sin、cos两种枚举类型

11. IsNull 是否为空接口

    统一规定所有需要的部分实现isNull方法

    
### 主要处理逻辑介绍

1. 读入输入串，判断不合法空格、tab的存在性
2. 清除所有空格、tab
3. 利用正则表达式将输入串按项切割成小串
4. 将小串new出Term对象，将Term对象放入ArrayList中生成表达式
5. 表达式求导
6. 表达式整理输出格式
7. 输出结果表达式

​    

​    