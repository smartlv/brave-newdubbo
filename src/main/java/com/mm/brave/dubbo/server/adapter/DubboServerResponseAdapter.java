package com.mm.brave.dubbo.server.adapter;

import com.alibaba.dubbo.rpc.Result;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerResponseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 实现brave的ServerResponseAdapter
 * @author smartlv
 */
public class DubboServerResponseAdapter implements ServerResponseAdapter
{
    private Result rpcResult;
    private Exception exception;

    public DubboServerResponseAdapter(Result rpcResult)
    {
        this.rpcResult = rpcResult;
    }

    public DubboServerResponseAdapter(Exception exception)
    {
        this.exception = exception;
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations()
    {
        List<KeyValueAnnotation> annotations = new ArrayList<>();
        if (exception != null)
        {
            KeyValueAnnotation keyValueAnnotation = KeyValueAnnotation.create("exception", exception.getMessage());
            annotations.add(keyValueAnnotation);
        }
        else
        {
            if (rpcResult.hasException())
            {
                KeyValueAnnotation keyValueAnnotation = KeyValueAnnotation.create("exception", rpcResult.getException()
                        .getMessage());
                annotations.add(keyValueAnnotation);
            }
            else
            {
                KeyValueAnnotation keyValueAnnotation = KeyValueAnnotation.create("server_result", "success");
                annotations.add(keyValueAnnotation);
            }
        }
        return annotations;
    }
}
