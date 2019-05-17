# OO Homework 5 README

----

### 一、电梯调度算法——LOOK算法

主要处理思路：使用LOOK算法。简单来说，就是电梯将上下交替运行，防止饥饿情况出现。

特殊情况：某些情况下，电梯会临时改变服务方向。这种情况与我们生活十分类似：

比如电梯正在15楼，A-10-15，过了1s左右，B-13-5。电梯会选择先将B送下去再回来接A，即使A是先来的。

以上算法均是笔者回忆并试图模仿生活中电梯的实际行为尝试写出的。

------

### 二、主要工作方式介绍

主要由主线程负责启动InputHandler、Scheduler、Elevator(Amount 个)线程，之后分工合作：

InputHandler负责读入输入并将所有合理请求送入一级buffer；

Scheduler负责从一级buffer取出请求并分配给合适的电梯（二级buffer）；

Elevator只负责根据自身的buffer（二级buffer）判断自己的运行方式。

以上线程有终止顺序：

InputHandler将在没有input时终止；

Scheduler将在input终止且一级buffer没有待分配的请求时终止；

Elevator将在scheduler终止且二级buffer没有待执行请求且电梯内没人时终止。

------

### 三、执行逻辑介绍

~~过于麻烦，不介绍了~~

其实InputHandler 和 Scheduler 的逻辑比较清晰（scheduler待改进，这回没优化）。

Elevator的执行主要判断当前state（状态：睡觉、上行服务、下行服务）和direc（方向：上行、下行、静止），然后根据当前state、direc和二级buffer决定接下来的state和direc，是否开门等。

