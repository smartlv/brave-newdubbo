package com.mm.brave.dubbo.support.defaults;

import com.alibaba.dubbo.rpc.Invoker;
import com.mm.brave.dubbo.support.DubboServerNameProvider;

/**
 *   解析dubbo Provider applicationName
 *   @author smatlv
 */
public class DefaultServerNameProvider implements DubboServerNameProvider
{
    @Override
    public String resolveServerName(Invoker<?> invoker)
    {
        String application = invoker.getUrl().getParameter("application");

        return application;
    }
}
