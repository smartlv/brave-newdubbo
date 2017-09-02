package com.mm.brave.dubbo.server;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ServerRequestInterceptor;
import com.github.kristofa.brave.ServerResponseInterceptor;
import com.github.kristofa.brave.ServerSpanThreadBinder;
import com.mm.brave.dubbo.server.adapter.DubboServerRequestAdapter;
import com.mm.brave.dubbo.server.adapter.DubboServerResponseAdapter;
import com.mm.brave.dubbo.utils.TraceLogUtil;

/**
 * dubbo服务端过来上报
 * @author smartlv
 */
@Activate(group = Constants.PROVIDER)
public class BraveProviderFilter implements Filter
{
    private static volatile Brave brave;
    private static volatile ServerRequestInterceptor serverRequestInterceptor;
    private static volatile ServerResponseInterceptor serverResponseInterceptor;
    private static volatile ServerSpanThreadBinder serverSpanThreadBinder;

    public static void setBrave(Brave brave)
    {
        BraveProviderFilter.brave = brave;
        BraveProviderFilter.serverRequestInterceptor = brave.serverRequestInterceptor();
        BraveProviderFilter.serverResponseInterceptor = brave.serverResponseInterceptor();
        BraveProviderFilter.serverSpanThreadBinder = brave.serverSpanThreadBinder();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException
    {
        serverRequestInterceptor.handle(new DubboServerRequestAdapter(invoker, invocation, brave.serverTracer()));
        try
        {
            Result rpcResult = invoker.invoke(invocation);
            serverResponseInterceptor.handle(new DubboServerResponseAdapter(rpcResult));
            return rpcResult;
        }
        catch (Exception e)
        {
            serverResponseInterceptor.handle(new DubboServerResponseAdapter(e));
            throw e;
        }
        finally
        {
            TraceLogUtil.remove("traceId");
        }
    }
}
