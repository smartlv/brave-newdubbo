package com.mm.brave.dubbo.support;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

/**
 * @author smartlv
 */
public interface DubboSpanNameProvider
{
    String resolveSpanName(Invoker<?> invoker, Invocation invocation);
}
