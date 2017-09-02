package com.mm.brave.dubbo.support.defaults;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.mm.brave.dubbo.support.DubboSpanNameProvider;

/**
 * @author smartlv
 */
public class DefaultSpanNameProvider implements DubboSpanNameProvider
{
    @Override
    public String resolveSpanName(Invoker<?> invoker, Invocation invocation)
    {
        return invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
    }
}
