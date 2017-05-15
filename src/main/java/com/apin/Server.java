package com.apin;

import com.apin.consumer.Consumer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/16.
 */
public class Server {


    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static AbstractApplicationContext applicationContext;

    public static void main(String [] argv) throws Throwable{
        try{
            init();
            System.exit(0);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public static void init() throws Throwable{
        String baseDir=System.getProperty("user.dir");

        String confDir=baseDir+"/src/main/resources";
        File file=new File(confDir);
        if(!file.exists()){
            confDir=baseDir+"/conf";
            file=new File(confDir);
            if(!file.exists()){
                throw new RuntimeException("conf dir "+confDir+" doesnot exist");
            }
        }


        URLClassLoader urlClassLoader=new URLClassLoader(new URL[]{file.toURI().toURL()});

        Thread.currentThread().setContextClassLoader(urlClassLoader);

        applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //
                }
                applicationContext.close();
                String endDate = simpleDateFormat.format(new Date());
                System.out.println(endDate + " the timer-consumer is closed");
            }
        });

        SignalHandler handler=new SignalHandler() {
            public void handle(Signal signal) {
                String currentDate = simpleDateFormat.format(new Date());
                System.out.println(currentDate + " " + signal.toString() + " has been received,now the process will be closed");
                System.exit(0);
            }
        };

        Signal.handle(new Signal("INT"),handler);
        Signal.handle(new Signal("TERM"), handler);

        Consumer consumer= (Consumer) applicationContext.getBean("consumer");
        consumer.init();
        consumer.receiveMessage();
    }
}
