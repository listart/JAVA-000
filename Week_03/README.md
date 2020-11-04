# NIO学习记录



## HttpServer复习



### V1 ServerSocket单线程

#### 修复连接重置错误

上周出现的连接错误如下：

```bash
listart@Listart-Pro homework$ curl http://localhost:8801
curl: (56) Recv failure: Connection reset by peer
hello,nio
```

本周根据老师讲义，增加Http头Content-Length字段解决。相关代码如下：

```java
String body = "hello,nio";
printWriter.println("Content-Length:"+ body.getBytes().length);
```

> **分析** 导致客户端报服务器重置连接错误的原因是：客户端在未确定接收完http返回包时，服务器端已经关闭连接。由于服务器返回包头中缺少Content-Length字段，导致客户端因不知包大小而一直在收包。



#### 压测结果

##### 直接回写

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8801
Running 30s test @ http://localhost:8801
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   924.07ms   99.56ms 991.54ms   96.40%
    Req/Sec    15.17      6.75    30.00     85.27%
  Latency Distribution
     50%  940.63ms
     75%  949.80ms
     90%  956.69ms
     99%  965.29ms
  862 requests in 30.09s, 91.79KB read
  Socket errors: connect 0, read 1243, write 32, timeout 0
Requests/sec:     28.65
Transfer/sec:      3.05KB
```



##### 先读后回写

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8801
Running 30s test @ http://localhost:8801
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   925.18ms   85.89ms 987.83ms   97.42%
    Req/Sec    21.47      7.99    49.00     68.66%
  Latency Distribution
     50%  936.98ms
     75%  943.13ms
     90%  949.79ms
     99%  960.33ms
  1278 requests in 30.09s, 99.84KB read
  Socket errors: connect 0, read 1278, write 0, timeout 0
Requests/sec:     42.47
Transfer/sec:      3.32KB
```



##### 小结 

1. 单线程socket模型，每业务处理**>20ms**，因此**30s**内理论最高处理请求数量为**30 * 1000 / 20 = 1500**。测试结果为1278qps，符合预期。
2. **先读后写有利于解决终端socket写错误，进而提高吞吐，略微降低延迟。**



#### 遗留问题

- 但为什么服务器先读InputStream，再写无Content-Length字段头的方式也能解决问题呢？相关代码如下

  ```java
  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  	String line;
  	while ((line = in.readLine()) != null) {
  		if ("".equals(line))
  			break;
  }
  ```

- 如何解决压测结果中的socket读错误



### V2 ServerSocket每任务线程

#### 压测结果

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8802
Running 30s test @ http://localhost:8802
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    32.78ms   55.16ms 650.03ms   97.85%
    Req/Sec   717.35    126.81     0.87k    91.56%
  Latency Distribution
     50%   25.57ms
     75%   27.23ms
     90%   29.18ms
     99%  352.70ms
  16446 requests in 30.05s, 1.25MB read
  Socket errors: connect 0, read 16446, write 0, timeout 0
Requests/sec:    547.30
Transfer/sec:     42.76KB
```



##### 小结

1. 每业务处理**>20ms**，每任务分配单独线程的情况下，理论**30秒40并发**最大处理请求数为**30 * 1000 / 20 * 40 = 60000个**，测试结果16459rps，仅理论值的1/4。
2. 相对单线程版本，吞吐得到极大的提高，延迟也降低明显。
3. socket读错误=请求数，**原因未知？**



### V3 ServerSocket固定线程池

#### 压测结果

##### 固定线程20

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8803
Running 30s test @ http://localhost:8803
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    56.48ms   74.69ms 837.35ms   97.44%
    Req/Sec   421.59     55.32   515.00     80.83%
  Latency Distribution
     50%   45.82ms
     75%   47.75ms
     90%   49.04ms
     99%  532.09ms
  16434 requests in 30.06s, 1.25MB read
  Socket errors: connect 0, read 16434, write 0, timeout 0
Requests/sec:    546.68
Transfer/sec:     42.71KB
```



##### 固定线程40

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8803
Running 30s test @ http://localhost:8803
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    24.24ms    5.99ms 162.94ms   98.83%
    Req/Sec   770.84    103.32     0.93k    86.79%
  Latency Distribution
     50%   23.83ms
     75%   25.25ms
     90%   26.28ms
     99%   31.44ms
  16447 requests in 30.07s, 1.25MB read
  Socket errors: connect 0, read 16447, write 0, timeout 0
Requests/sec:    546.94
Transfer/sec:     42.73KB
```



##### 固定线程80

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8803
Running 30s test @ http://localhost:8803
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.00ms    8.91ms 176.92ms   97.26%
    Req/Sec   756.27    112.64     0.90k    86.57%
  Latency Distribution
     50%   23.89ms
     75%   25.53ms
     90%   26.72ms
     99%   53.12ms
  16453 requests in 30.00s, 1.26MB read
  Socket errors: connect 0, read 16453, write 0, timeout 0
Requests/sec:    548.37
Transfer/sec:     42.84KB
```



##### 固定线程400

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8803
Running 30s test @ http://localhost:8803
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    28.90ms   36.23ms 474.45ms   97.75%
    Req/Sec   743.30    146.45     0.93k    91.28%
  Latency Distribution
     50%   24.04ms
     75%   25.58ms
     90%   26.84ms
     99%  181.04ms
  16433 requests in 30.02s, 1.25MB read
  Socket errors: connect 0, read 16433, write 0, timeout 0
Requests/sec:    547.48
Transfer/sec:     42.77KB
```



##### 小结 

1. 固定线程池方式与每任务独立线程方式比较，rps差不多，P99明显改善
2. 固定线程数仅并发数1/2时，平均延迟和P99明显增加
3. 固定线程在并发数量2倍以内时，平均延迟和P99都比较稳定，尤其是相当的时候。线程过多，由于线程上下文切换消耗的原因，P99也随之上升。



### V4 Netty主从Reactor

#### 压测结果

##### 直接关闭

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8804
Running 30s test @ http://localhost:8804
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    54.32ms  123.13ms   1.14s    95.30%
    Req/Sec   620.11    360.64     1.63k    69.11%
  Latency Distribution
     50%   25.00ms
     75%   41.11ms
     90%   80.16ms
     99%  810.49ms
  16416 requests in 30.04s, 1.44MB read
Requests/sec:    546.39
Transfer/sec:     49.09KB
```



##### KeepAlive

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency  http://localhost:8804
Running 30s test @ http://localhost:8804
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.26ms   72.93ms   1.69s    96.87%
    Req/Sec    17.52k     6.66k   29.58k    84.60%
  Latency Distribution
     50%  791.00us
     75%    1.07ms
     90%    1.70ms
     99%  279.05ms
  1032036 requests in 30.07s, 95.47MB read
  Socket errors: connect 0, read 0, write 0, timeout 11
Requests/sec:  34323.17
Transfer/sec:      3.18MB
```



##### 小结

1. **KeepAlive方式**提高了2个数量级的rps，且P50延迟在us级。可见**建立TCP连接的代价实际是比较高**的。
2. 直接关闭连接方式，netty压测结果与socket多线程方式相当，但是没有socket错误发生。



#### 遗留问题

- 程序启动若干参数未生效：

  ```bash
  十一月 04, 2020 2:16:33 下午 io.netty.bootstrap.AbstractBootstrap setChannelOption
  警告: Unknown channel option 'TCP_NODELAY' for channel '[id: 0xc2b4546f]'
  十一月 04, 2020 2:16:33 下午 io.netty.bootstrap.AbstractBootstrap setChannelOption
  警告: Unknown channel option 'SO_SNDBUF' for channel '[id: 0xc2b4546f]'
  十一月 04, 2020 2:16:33 下午 io.netty.bootstrap.AbstractBootstrap setChannelOption
  警告: Unknown channel option 'io.netty.channel.unix.UnixChannelOption#SO_REUSEPORT' for channel '[id: 0xc2b4546f]'
  ```

  

## Netty Http Gateway Server

### 压测结果

```bash
listart@Listart-Pro homework$ wrk -c 40 -d 30s --latency http://localhost:8888/api/hello
Running 30s test @ http://localhost:8888/api/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   479.39ms  192.89ms   1.35s    71.65%
    Req/Sec    45.42     30.11   191.00     70.00%
  Latency Distribution
     50%  458.72ms
     75%  580.50ms
     90%  717.92ms
     99%    1.10s 
  2505 requests in 30.08s, 173.69KB read
Requests/sec:     83.27
Transfer/sec:      5.77KB
```

### 小结

1. rps仅比单线程HttpServer多一倍，P99甚至达到1.1s，主要问题出在outbound端其实相当于单线程调用backend。

### 遗留问题

1. outbound在inboundhandler线程中，单线程运行

2. outbound中client connection总是断开再连接，消耗大量连接时间

3. filter暂时硬编码实现如下：

   ```java
   req.headers().set(USER_AGENT, "gateway");
   ```

   