package com.mm.brave.dubbo.support.defaults;

import com.alibaba.dubbo.rpc.RpcContext;
import com.mm.brave.dubbo.support.DubboServerNameProvider;

/**
 *   解析dubbo Provider applicationName
 *   @author smatlv
 */
public class DefaultServerNameProvider implements DubboServerNameProvider
{
    @Override
    public String resolveServerName(RpcContext rpcContext)
    {
        String application = RpcContext.getContext().getUrl().getParameter("application");
        return application;
    }
}
