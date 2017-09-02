package com.mm.brave.dubbo.support;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * @author smart
 */
public interface DubboServerNameProvider
{
    String resolveServerName(RpcContext rpcContext);
}
