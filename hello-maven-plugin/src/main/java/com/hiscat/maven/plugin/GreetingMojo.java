package com.hiscat.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

/**
 * @author Administrator
 */
@Mojo(name = "sayhi", defaultPhase = LifecyclePhase.COMPILE)
public class GreetingMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Hello world");
        getLog().info("context : " + getPluginContext());
        File file = new File("../evn.txt");
        try {
            Files.writeString(file.toPath(), String.valueOf(System.currentTimeMillis()), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
