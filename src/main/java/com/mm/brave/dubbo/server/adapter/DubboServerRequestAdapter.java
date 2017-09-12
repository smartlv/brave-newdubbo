package com.mm.brave.dubbo.server.adapter;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.*;
import com.mm.brave.dubbo.support.DubboClientNameProvider;
import com.mm.brave.dubbo.support.DubboSpanNameProvider;
import com.mm.brave.dubbo.support.defaults.DefaultClientNameProvider;
import com.mm.brave.dubbo.support.defaults.DefaultSpanNameProvider;
import com.mm.brave.dubbo.utils.IPConvertUtil;
import com.mm.brave.dubbo.utils.TraceLogUtil;
import com.twitter.zipkin.gen.Endpoint;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.IdConversion.convertToLong;

/**
 * 实现brave的ServerRequestAdapter
 * @author smartlv
 */
public class DubboServerRequestAdapter implements ServerRequestAdapter
{
    private Invocation invocation;
    private Invoker<?> invoker;
    private ServerTracer serverTracer;
    private final static DubboSpanNameProvider spanNameProvider = new DefaultSpanNameProvider();
    private final static DubboClientNameProvider clientNameProvider = new DefaultClientNameProvider();

    public DubboServerRequestAdapter(Invoker<?> invoker, Invocation invocation, ServerTracer serverTracer)
    {
        this.invoker = invoker;
        this.invocation = invocation;
        this.serverTracer = serverTracer;
    }

    @Override
    public TraceData getTraceData()
    {
        String sampled = invocation.getAttachment("sampled");
        if (sampled != null && sampled.equals("0"))
        {
            return TraceData.NOT_SAMPLED;
        }
        else
        {
            final String parentId = invocation.getAttachment("parentId");
            final String spanId = invocation.getAttachment("spanId");
            final String traceId = invocation.getAttachment("traceId");
            final String clientName = clientNameProvider.resolveClientName(this.invocation);

            if (traceId != null && spanId != null)
            {
                TraceLogUtil.put("traceId", clientName + "_" + TraceLogUtil.generateTraceLogIdPrefix() + traceId);

                SpanId span = getSpanId(traceId, spanId, parentId);

                return TraceData.create(span);
            }
        }
        return TraceData.EMPTY;

    }

    @Override
    public String getSpanName()
    {
        return spanNameProvider.resolveSpanName(this.invoker, this.invocation);
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations()
    {
        String ipAddr = RpcContext.getContext().getUrl().getIp();
        InetSocketAddress inetSocketAddress = RpcContext.getContext().getRemoteAddress();
        final String clientName = clientNameProvider.resolveClientName(this.invocation);

        serverTracer.setServerReceived(Endpoint.builder().ipv4(IPConvertUtil.convertToInt(ipAddr))
                .port(inetSocketAddress.getPort()).serviceName(clientName).build());

        InetSocketAddress socketAddress = RpcContext.getContext().getLocalAddress();
        if (socketAddress != null)
        {
            KeyValueAnnotation remoteAddrAnnotation = KeyValueAnnotation.create("address", socketAddress.toString());
            return Collections.singleton(remoteAddrAnnotation);
        }
        else
        {
            return Collections.emptyList();
        }

    }

    static SpanId getSpanId(String traceId, String spanId, String parentSpanId)
    {
        return SpanId.builder().traceId(convertToLong(traceId)).spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).sampled(Boolean.TRUE).build();
    }

}
