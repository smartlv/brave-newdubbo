package com.mm.brave.dubbo.support;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * resolveClientName
 * @author smart
 */
public interface DubboClientNameProvider
{
    public String resolveClientName(RpcContext rpcContext);
}
