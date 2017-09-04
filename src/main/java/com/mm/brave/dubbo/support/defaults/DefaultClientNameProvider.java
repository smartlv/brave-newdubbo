package com.mm.brave.dubbo.support.defaults;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.github.kristofa.brave.SpanId;
import com.mm.brave.dubbo.client.adapter.DubboClientRequestAdapter;
import com.mm.brave.dubbo.support.DubboClientNameProvider;

/**
 *   解析dubbo consumer applicationName
 *   @see DubboClientRequestAdapter#addSpanIdToRequest(SpanId spanId)
 *   RpcContext.getContext().setAttachment("clientName", application);
 *   @author smartlv
 */
public class DefaultClientNameProvider implements DubboClientNameProvider
{
    @Override
    public String resolveClientName(Invocation invocation)
    {
        RpcInvocation rpcInvocation = (RpcInvocation) invocation;
        String application = rpcInvocation.getAttachment("clientName", "");
        return application;
    }
}
