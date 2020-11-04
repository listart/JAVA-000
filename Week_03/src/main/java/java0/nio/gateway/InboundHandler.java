package java0.nio.gateway;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class InboundHandler extends SimpleChannelInboundHandler<HttpObject> {
    final private String proxyServer;

    public InboundHandler(String proxyServer) {
        super();
        this.proxyServer = proxyServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws InterruptedException {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;


            boolean keepAlive = HttpUtil.isKeepAlive(req);

            NioEventLoopGroup workGroup = new NioEventLoopGroup();
            OutboundHandler outboundHandler = new OutboundHandler();

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
                        pipeline.addLast(outboundHandler);
                    }
                });

                URI uriProxyServer = URI.create(proxyServer);
                ChannelFuture f = bootstrap.connect(uriProxyServer.getHost(), uriProxyServer.getPort()).sync();

                req.headers().set(USER_AGENT, "gateway");
                req.headers().set(HOST, uriProxyServer.getHost());

                f.channel().writeAndFlush(req);

                f.channel().closeFuture().sync();

            } finally {
                workGroup.shutdownGracefully();
            }

            FullHttpResponse response = new DefaultFullHttpResponse(outboundHandler.httpVersion, outboundHandler.httpResponseStatus, Unpooled.wrappedBuffer(outboundHandler.content.getBytes()));
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (keepAlive) {
                if (HTTP_1_1.isKeepAliveDefault()) {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                }
            } else {
                response.headers().set(CONNECTION, CLOSE);
            }

            ChannelFuture f = ctx.writeAndFlush(response);

            if (!keepAlive)
                f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
