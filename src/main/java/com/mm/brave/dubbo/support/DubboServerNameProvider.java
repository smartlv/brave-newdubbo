package com.mm.brave.dubbo.support;

import com.alibaba.dubbo.rpc.Invoker;

/**
 * @author smart
 */
public interface DubboServerNameProvider
{
    String resolveServerName(Invoker<?> invoker);
}
