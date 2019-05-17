# OO Homework 10 README

---------

本次作业需要实现Path和Graph。

我使用冗余数据保证时间效率，即使用空间换取时间。

在最短路算法中，设置buffer（类似cache）保留已算出的最短路，使得后续查找不虚花费额外时间计算。

最短路算法采用广度优先搜索（基于本次作业是无权无向图），理论拥有O(n)的时间复杂度。
