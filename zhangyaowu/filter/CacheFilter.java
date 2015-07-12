/*
 * 文 件 名:  CacheFilter.java
 * 版    权:  xxx Technologies Co., Ltd. Copyright 1988-2015,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zhangyaowu
 * 修改时间:  2015年7月10日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package zhangyaowu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 设置response中cache相关header的过滤器
 * Cache相关的一系列Filter参见我的性能优化专题文章:
 * https://github.com/kaelhuawei/blog/blob/master/web/web%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96(%E4%BA%8C)%20%E5%90%88%E7%90%86%E5%88%A9%E7%94%A8%E6%B5%8F%E8%A7%88%E5%99%A8%E7%BC%93%E5%AD%98.md
 * 
 * @author zhangyaowu
 * @version [版本号, 2015年7月10日]
 * @see [相关类/方法]
 * @since xxx xxx xxx
 */
public class CacheFilter implements Filter
{
    private long expiration;

    private String cacheability;

    private boolean mustRevalidate;

    private String vary;

    /**
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        try
        {
            expiration = Long.valueOf(filterConfig.getInitParameter("expiration"));
        }
        catch (NumberFormatException e)
        {
            throw new ServletException(new StringBuilder("The initialization parameter ")
                    .append("expiration")
                    .append(" is invalid or is missing for the filter ")
                    .append(filterConfig.getFilterName()).append(".").toString());
        }
        cacheability = Boolean.valueOf(filterConfig.getInitParameter("private")) ? "private"
                : "public";
        mustRevalidate = Boolean
                .valueOf(filterConfig.getInitParameter("must-revalidate"));
        vary = filterConfig.getInitParameter("vary");
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        StringBuilder cacheControl = new StringBuilder(cacheability).append(", max-age=")
                .append(expiration);
        if (mustRevalidate)
        {
            cacheControl.append(", must-revalidate");
        }

        // Set cache directives
        httpServletResponse.setHeader("Cache-Control", cacheControl.toString());
        httpServletResponse.setDateHeader("Expires", System.currentTimeMillis()
                + expiration * 1000L);

        // Set Vary field
        if (vary != null && !vary.isEmpty())
        {
            httpServletResponse.setHeader("Vary", vary);
        }

        chain.doFilter(request, new HttpServletResponseWrapper(httpServletResponse)
        {
            @Override
            public void addHeader(String name, String value)
            {
                if (!"Pragma".equalsIgnoreCase(name))
                {
                    super.addHeader(name, value);
                }
            }

            @Override
            public void setHeader(String name, String value)
            {
                if (!"Pragma".equalsIgnoreCase(name))
                {
                    super.setHeader(name, value);
                }
            }
        });
    }

    /**
     * 
     */
    @Override
    public void destroy()
    {
    }
}