package com.mm.brave.dubbo;

import com.github.kristofa.brave.Brave;
import com.mm.brave.dubbo.client.BraveConsumerFilter;
import com.mm.brave.dubbo.server.BraveProviderFilter;

/**
 *
 */
public class BraveDubboManagementBean
{

    public Brave brave;

    public BraveDubboManagementBean(Brave brave)
    {
        this.brave = brave;
        BraveConsumerFilter.setBrave(brave);
        BraveProviderFilter.setBrave(brave);
    }

    public void setBrave(Brave brave)
    {
        this.brave = brave;
        BraveConsumerFilter.setBrave(brave);
        BraveProviderFilter.setBrave(brave);
    }
}
