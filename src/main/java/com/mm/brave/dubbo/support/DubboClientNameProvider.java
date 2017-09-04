package com.mm.brave.dubbo.support;

import com.alibaba.dubbo.rpc.Invocation;

/**
 * resolveClientName
 * @author smart
 */
public interface DubboClientNameProvider
{
    String resolveClientName(Invocation invocation);
}
