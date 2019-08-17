package com.hiscat.batch.controller;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hiscat
 */
@RestController
@AllArgsConstructor
public class JobController {
    private JobLauncher jobLauncher;
    private Job launcherJob;
    private JobOperator jobOperator;

    @GetMapping("/job/{p}")
    public String run(@PathVariable String p) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobLauncher.run(launcherJob, new JobParameters(Map.of("jobParameter", new JobParameter(p))));
        return "run success";
    }

    @GetMapping("/job2/{p}")
    public String run2(@PathVariable String p) throws JobParametersInvalidException, JobInstanceAlreadyExistsException, NoSuchJobException {
        jobOperator.start("operatorJob", String.format("jobParameter=%s", p));
        return "run success";
    }
}
