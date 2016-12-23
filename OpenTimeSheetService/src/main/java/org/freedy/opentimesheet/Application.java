package org.freedy.opentimesheet;


import org.freedy.opentimesheet.model.Colaborador;
import org.freeedy.opentimesheet.persistence.TimeSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.WebApplicationInitializer;

import com.google.common.collect.Lists;
import com.owlike.genson.Genson;


@SpringBootApplication
@Configuration
@ComponentScan(basePackages="org.freedy.opentimesheet")
@EnableMongoRepositories(basePackages="org.freedy.opentimesheet.persistence")
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {  
    
    @Autowired
    private TimeSheetRepository repository;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    public void run(String ... args) {
        repository.deleteAll();
        
        Genson genson = new Genson(); //load dos dados de teste
        try {
            String fileName = "data.js";
            ClassLoader classLoader = getClass().getClassLoader();
            Colaborador[] Colaboradors = 
                    genson.deserialize(classLoader.getResource(fileName).openStream(), 
                            Colaborador[].class);
            
            repository.save(Lists.newArrayList(Colaboradors));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



