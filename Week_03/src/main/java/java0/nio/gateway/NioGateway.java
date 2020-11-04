package java0.nio.gateway;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NioGateway {
    public static void start(int port, String backend) throws InterruptedException {
        System.out.println("NioGateway starting...");
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(1000);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(bossGroup, workerGroup);

            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) {
                    ChannelPipeline pipeline = channel.pipeline();

                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new HttpServerExpectContinueHandler());
                    pipeline.addLast(new InboundHandler(backend));
                }
            });

            Channel ch = serverBootstrap.bind(port).sync().channel();

            System.out.println("NioGateway started at http://localhost:" + port + " for " + backend);

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        String proxyServer = System.getProperty("proxyServer", "http://localhost:8804");
        int port = Integer.parseInt(System.getProperty("port", "8888"));

        try {
            start(port, proxyServer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
