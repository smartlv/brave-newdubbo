package com.mm.brave.dubbo.support;

import com.alibaba.dubbo.rpc.Invocation;

/**
 * 为了方便服务端trace log记录，降低服务端对brave的依赖，故新增TraceLogUtil类，能进行业务日志追踪，其中引入AppId方便快速查询是哪个调用方（
 * @author smartlv
 */
public interface DubboAppIdProvider
{
    String resolveAppId(Invocation invocation);
}
