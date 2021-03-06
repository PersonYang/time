package com.apin.timer;

import com.apin.utils.ApinUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.Map;
import java.util.Properties;


/**
 * @author zhangcheng
 */
@Service
public class TimerTaskManagement {

    private static final Logger logger= LoggerFactory.getLogger(TimerTaskManagement.class);

//    @Value("${quartz.threadPool.thread.count}")
//    private String threadCount;

//    @Value("${org.quartz.dataSource.apin.driver}")
//    private String driver;

//    @Value("${org.quartz.dataSource.apin.URL}")
//    private String url;

//    @Value("${org.quartz.dataSource.apin.user}")
//    private String user;

//    @Value("${org.quartz.dataSource.apin.password}")
//    private String password;

//    @Value("${org.quartz.dataSource.apin.maxConnections}")
//    private String maxConnections;

    @Autowired
    private MyJobFactory myJobFactory;

    public void setMyJobFactory(MyJobFactory myJobFactory){
        this.myJobFactory=myJobFactory;
    }

    SchedulerFactory sf;

    @PostConstruct
    public void init() throws Throwable{
        logger.debug("instantiate the StdSchedulerFactory");
        Properties properties=new Properties();
        properties.put("org.quartz.scheduler.instanceName","MyScheduler");
        properties.put("org.quartz.scheduler.instanceId", "AUTO");
        properties.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", ApinUtil.getProperty("quartz.threadPool.thread.count"));
        properties.put("org.quartz.scheduler.jobFactory.class","com.apin.timer.MyJobFactory");
        properties.put("org.quartz.scheduler.jobFactory",myJobFactory);
        properties.put("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.put("org.quartz.jobStore.driverDelegateClass","org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.put("org.quartz.jobStore.tablePrefix","QRTZ_");
        properties.put("org.quartz.jobStore.dataSource","apin");
        properties.put("org.quartz.dataSource.apin.driver", ApinUtil.getProperty("org.quartz.dataSource.apin.driver"));
        properties.put("org.quartz.dataSource.apin.URL",ApinUtil.getProperty("org.quartz.dataSource.apin.URL"));
        properties.put("org.quartz.dataSource.apin.user",ApinUtil.getProperty("org.quartz.dataSource.apin.user"));
        properties.put("org.quartz.dataSource.apin.password",ApinUtil.getProperty("org.quartz.dataSource.apin.password"));
        properties.put("org.quartz.dataSource.apin.maxConnections",ApinUtil.getProperty("org.quartz.dataSource.apin.maxConnections"));
        properties.put("org.quartz.jobStore.isClustered",ApinUtil.getProperty("org.quartz.dataSource.apin.isClustered"));
        properties.put("org.quartz.jobStore.clusterCheckinInterval",ApinUtil.getProperty("org.quartz.dataSource.apin.cluster.check.interval"));
        sf=new StdSchedulerFactory(properties);
    }

    public void addTimerTask(Class<?> taskClass,String jobGroupSuffix,String jobSuffix,long durationInMs,Map<String,String> map)throws SchedulerException{
        Date currentDate=new Date();
        long endDateInMs=currentDate.getTime()+durationInMs;
        Date endDate=new Date(endDateInMs);
        addTimerTask(taskClass, jobGroupSuffix, jobSuffix, endDate, map);
    }

    public void addTimerTask(Class<?> taskClass,String jobGroupSuffix,String jobSuffix,Date endDate,Map<String,String> map) throws SchedulerException{
        //1.创建工作对象
        JobDetail jobDetail=new JobDetail();
        jobDetail.setJobClass(taskClass);
        jobDetail.setName("job" + jobSuffix);
        jobDetail.setGroup("jobGroup" + jobGroupSuffix);
        jobDetail.getJobDataMap().putAll(map);
        jobDetail.addJobListener("jobListener");


        //2.创建Trigger对象
        SimpleTrigger simpleTrigger=new SimpleTrigger();
        simpleTrigger.setName("trigger" + jobSuffix);
        simpleTrigger.setGroup("triggerGroup" + jobGroupSuffix);
        simpleTrigger.setStartTime(endDate);

        //3.创建Scheduler对象，并配置JobDetail和Trigger对象
        Scheduler scheduler=sf.getScheduler();
        scheduler.addJobListener(new TaskListener());
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.start();
    }

    public void removeTimerTask(String jobGroupSuffix,String jobSuffix) throws SchedulerException{
        Scheduler scheduler=sf.getScheduler();
        //1.停止触发器
        scheduler.pauseTrigger("trigger" + jobSuffix, "triggerGroup" + jobGroupSuffix);
        //2.移除触发器
        scheduler.unscheduleJob("trigger" + jobSuffix, "triggerGroup" + jobGroupSuffix);
        //3.删除任务
        scheduler.deleteJob("job" + jobSuffix, "jobGroup" + jobGroupSuffix);
    }

    public long getRemainTime(String jobGroupSuffix,String jobSuffix) throws SchedulerException{
        Scheduler scheduler=sf.getScheduler();
        String triggerName="trigger"+jobSuffix;
        String triggerGroupName="triggerGroup"+jobGroupSuffix;
        Trigger trigger=scheduler.getTrigger(triggerName,triggerGroupName);
        Date endDate=trigger.getNextFireTime();
        logger.info("endTime:{}",endDate);
        long endTime=endDate.getTime();
        long startTime=System.currentTimeMillis();
        long remainTime=endTime-startTime;
        return remainTime;
    }

    @PreDestroy
    public void close(){
        try {
            Scheduler scheduler = sf.getScheduler();
            scheduler.shutdown(true);
        }catch(SchedulerException e){
            logger.error("Exception caught when close the scheduler");
        }

    }

    public class TaskListener implements JobListener{

        @Override
        public String getName() {
            return "jobListener";
        }

        @Override
        public void jobToBeExecuted(JobExecutionContext context) {
            String jobName=context.getJobDetail().getJobDataMap().getString("jobName");
            String jobGroupName=context.getJobDetail().getJobDataMap().getString("jobGroupName");
            logger.info("Group:{},job:{} is to be excuted", jobGroupName, jobName);
        }

        @Override
        public void jobExecutionVetoed(JobExecutionContext context) {

        }

        @Override
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
            logger.info("TaskListener is executing");
            String jobName=context.getJobDetail().getJobDataMap().getString("jobName");
            String jobGroupName=context.getJobDetail().getJobDataMap().getString("jobGroupName");
            if(null==jobException) {
                logger.info("Group:{},job:{} has been successful to excute", jobGroupName, jobName);
            }else{
                logger.error("Exception:{} has been caught when Group:{},job:{} was executed",jobException,jobGroupName,jobName);
            }
            //job执行完毕之后如果jobDetail还存在而trigger已经不存在了，因此可以将jobDetail删除
            logger.info("Group:{},job:{} is to be removed", jobGroupName, jobName);
            try {
                sf.getScheduler().deleteJob(jobName, jobGroupName);
            }catch(SchedulerException e){
                logger.error("Exception has been caught when delete Group:{},job:{}",jobGroupName,jobName);
            }
        }
    }
}
