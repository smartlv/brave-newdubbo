package com.mm.brave.dubbo.client.adapter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.internal.Nullable;
import com.mm.brave.dubbo.support.DubboServerNameProvider;
import com.mm.brave.dubbo.support.DubboSpanNameProvider;
import com.mm.brave.dubbo.support.defaults.DefaultServerNameProvider;
import com.mm.brave.dubbo.support.defaults.DefaultSpanNameProvider;
import com.mm.brave.dubbo.utils.IPConvertUtil;
import com.twitter.zipkin.gen.Endpoint;

import java.util.Collection;
import java.util.Collections;

/**
 * 实现brave的ClientRequestAdapter
 * @author smartlv
 */
public class DubboClientRequestAdapter implements ClientRequestAdapter
{
    private Invoker<?> invoker;
    private Invocation invocation;
    private final static DubboSpanNameProvider spanNameProvider = new DefaultSpanNameProvider();
    private final static DubboServerNameProvider serverNameProvider = new DefaultServerNameProvider();

    public DubboClientRequestAdapter(Invoker<?> invoker, Invocation invocation)
    {
        this.invoker = invoker;
        this.invocation = invocation;
    }

    @Override
    public String getSpanName()
    {
        return spanNameProvider.resolveSpanName(this.invoker, this.invocation);
    }

    @Override
    public void addSpanIdToRequest(@Nullable SpanId spanId)
    {
        String application = this.invoker.getUrl().getParameter("application");

        RpcInvocation rpcInvocation = (RpcInvocation) this.invocation;
        rpcInvocation.setAttachment("clientName", application);
        if (spanId == null)
        {
            rpcInvocation.setAttachment("sampled", "0");
        }
        else
        {
            rpcInvocation.setAttachment("sampled", "1");
            rpcInvocation.setAttachment("traceId", IdConversion.convertToString(spanId.traceId));
            rpcInvocation.setAttachment("spanId", IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null)
            {
                rpcInvocation.setAttachment("parentId", IdConversion.convertToString(spanId.parentId));
            }
        }
    }

    @Override
    public Collection<KeyValueAnnotation> requestAnnotations()
    {
        String protocol = this.invoker.getUrl().getProtocol();
        String key = protocol + ".url";
        return Collections.singletonList(KeyValueAnnotation.create(key, this.invoker.getUrl().toString()));
    }

    @Override
    public Endpoint serverAddress()
    {
        URL url = this.invoker.getUrl();
        String ipAddr = url.getIp();
        int port = url.getPort();
        String serverName = serverNameProvider.resolveServerName(RpcContext.getContext());
        // 此处是拿不到服务端的serverName的,所以写了application
        return Endpoint.builder().serviceName(serverName).ipv4(IPConvertUtil.convertToInt(ipAddr)).port(port).build();
    }

}
