package com.hiscat.springdubboconsumer.contoller;

import com.hiscat.springbubbo.api.EchoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@RefreshScope
public class EchoController {

    @Reference(version = "1.0.0")
    private EchoService echoService;

    @GetMapping("/echo/{msg}")
    public String echo(@PathVariable String msg) {
        return echoService.echo(msg);
    }
}
