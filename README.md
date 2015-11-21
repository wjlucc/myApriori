# myApriori
### My fist implemen apriori algorithm
========
数据挖掘中的关联规则挖掘算法
---------
输入：

I1 I2 I5
I1 I2
I2 I4
I1 I2 I4
I1 I3
I1 I2 I3 I5
I1 I2 I3
I2 I5
I2 I3 I4
I3 I4


结果：
最终产生的频繁项集是*********************
[I1, I2, I5]---2
[I3, I1, I2]---2
最终产生的关联规则是*********************
[I1, I5]==>[I2], confidence:1.0
