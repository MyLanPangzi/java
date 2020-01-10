package com.hiscat.maven.plugin;

import com.alibaba.fastjson.JSONObject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Administrator
 */
@Mojo(name = "sayhi", defaultPhase = LifecyclePhase.COMPILE)
public class GreetingMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Hello world");
//        try {
//            Runtime runtime = Runtime.getRuntime();
//            Process hello = runtime.exec("cmd.exe /c docker run -dp 8080:8080 --name spring-hello hiscat/spring-hello");
//            hello.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
        Object project = getPluginContext().get("project");
        Arrays.stream(project.getClass().getDeclaredFields())
                .forEach(field -> System.out.println(field.getName()));
    }
}
