package com.mm.brave.dubbo.support.defaults;

import com.alibaba.dubbo.rpc.Invocation;
import com.mm.brave.dubbo.factory.BraveFactoryBean;
import com.mm.brave.dubbo.support.DubboAppIdProvider;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class DefaultAppIdProvider implements DubboAppIdProvider
{
    private static final Logger LOGGER = Logger.getLogger(BraveFactoryBean.class.getName());

    private final String DEFAULT_APP_ID = "000";

    @Override
    public String resolveAppId(Invocation invocation)
    {
        String appId = DEFAULT_APP_ID;
        Object[] args = invocation.getArguments();
        if (args != null && args.length != 0)
        {
            Object obj = args[0];
            if (obj != null)
            {
                try
                {
                    appId = StringUtils.defaultString(BeanUtils.getProperty(obj, "appId"), DEFAULT_APP_ID);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
                {
                    LOGGER.info(e.getMessage());
                }
            }
        }
        return appId;
    }
}
