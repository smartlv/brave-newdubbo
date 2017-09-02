package com.mm.brave.dubbo.client;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.ClientSpanThreadBinder;
import com.mm.brave.dubbo.client.adapter.DubboClientRequestAdapter;
import com.mm.brave.dubbo.client.adapter.DubboClientResponseAdapter;

/**
 * 扩展dubbo结合brave装备，满足zipkin,核心拦截器
 * @author smartlv
 * @date 170831
 */
@Activate(group = Constants.CONSUMER)
public class BraveConsumerFilter implements Filter
{
    private static volatile Brave brave;
    private static volatile ClientRequestInterceptor clientRequestInterceptor;
    private static volatile ClientResponseInterceptor clientResponseInterceptor;
    private static volatile ClientSpanThreadBinder clientSpanThreadBinder;

    public static void setBrave(Brave brave)
    {
        BraveConsumerFilter.brave = brave;
        BraveConsumerFilter.clientRequestInterceptor = brave.clientRequestInterceptor();
        BraveConsumerFilter.clientResponseInterceptor = brave.clientResponseInterceptor();
        BraveConsumerFilter.clientSpanThreadBinder = brave.clientSpanThreadBinder();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException
    {
        clientRequestInterceptor.handle(new DubboClientRequestAdapter(invoker, invocation));
        try
        {
            Result rpcResult = invoker.invoke(invocation);
            clientResponseInterceptor.handle(new DubboClientResponseAdapter(rpcResult));
            return rpcResult;
        }
        catch (Exception ex)
        {
            clientResponseInterceptor.handle(new DubboClientResponseAdapter(ex));
            throw ex;
        }
        finally
        {
            clientSpanThreadBinder.setCurrentSpan(null);
        }
    }
}
