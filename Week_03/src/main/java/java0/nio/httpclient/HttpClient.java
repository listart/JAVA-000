package java0.nio.httpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.URI;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(workGroup);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) {
                    ChannelPipeline pipeline = channel.pipeline();

                    pipeline.addLast(new HttpResponseDecoder());
                    pipeline.addLast(new HttpRequestEncoder());
                    pipeline.addLast(new HttpClientHandler());
                }
            });

            ChannelFuture f = bootstrap.connect("localhost", 8804).sync();

            URI uri = new URI("http://localhost:8804");
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, GET, uri.toASCIIString(), Unpooled.wrappedBuffer("\n".getBytes()));
            request.headers().set(HOST, "localhost");
            request.headers().set(CONNECTION, KEEP_ALIVE);
            request.headers().set(CONTENT_LENGTH, request.content().readableBytes());

            f.channel().writeAndFlush(request);

            f.channel().closeFuture().sync();

        } finally {
            workGroup.shutdownGracefully();
        }
    }
}
