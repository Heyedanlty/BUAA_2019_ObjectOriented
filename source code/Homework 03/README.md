# OO HOMEWORK 3 README

-----------

## 一、输入处理

#### 采用递归下降分析法，文法如下：

```
<expression>	::=	[+|-] <term> {(+|-)<term>}
<expdivisor>	::= '('  <expression>  ')'
<term>			::= [+|-] <divisor> {*<divisor>}
<divisor>		::= <expdivisor>|<number>|<sinfun>|<cosfun>|<powfun>
<number>		::= [+|-] <singlenum>{<singlenum>}
<sin>			::= sin '(' <divisor> ')' <powerpart>
<cos>			::= cos '(' <divisor> ')' <powerpart>
<power>			::= x <powerpart>
<powerpart>		::= [^<number>]
<singlenum>		::= 0|1|...|9
```

#### 实际上，上述文法并不是LL（1）的，但是我们可以采用一些手段来使用一个近似递归下降分析法的方法来读取。

---

## 二、类介绍

#### 1.main package

内有Main类，负责主要流程处理

#### 2. io package

内有InputHandler 和 OutputHandler两个类，负责处理输入输出

#### 3.math package

内有多个类，主要处理求导部分，其中：

###### 3.1 Divisor 因子

​	是所有因子类（系数、幂函数、sin、cos、表达式因子）的父类

###### 3.2 Number Power Sin Cos 因子类（普通）

​	代表指导书中指出的所有因子种类

###### 3.3Term 项

​	一个项是多个因子相乘而得的，因此选择使用ArrayList来组织内部存储

###### 3.4 Expression 表达式 特殊因子

​	一个表达式是多个项相加而得的，因此选择使用ArrayList来组织内部存储 



