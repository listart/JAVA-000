package java0.nio.gateway;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static io.netty.util.CharsetUtil.UTF_8;

public class OutboundHandler extends SimpleChannelInboundHandler<HttpObject> {
    public HttpResponseStatus httpResponseStatus;
    public HttpVersion httpVersion;
    public HttpHeaders httpHeaders;
    public String content;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;

            httpResponseStatus = response.status();
            httpVersion = response.protocolVersion();
            httpHeaders = response.headers();
        }

        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;

            this.content = content.content().toString(UTF_8);
        }
    }
}
