package com.mm.brave.dubbo.support.defaults;

import com.alibaba.dubbo.rpc.RpcContext;
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
    public String resolveClientName(RpcContext rpcContext)
    {
        String application = RpcContext.getContext().getUrl().getParameter("clientName");
        return application;
    }
}
