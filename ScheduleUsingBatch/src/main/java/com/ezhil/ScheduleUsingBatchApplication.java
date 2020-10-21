package com.ezhil;


import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ezhil.controller.EmployeeControl;

@SpringBootApplication
public class ScheduleUsingBatchApplication {
	
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ScheduleUsingBatchApplication.class, args);
		
		try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get("C:/Users/EZHILARASI/Documents/CSV");
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);             
            System.out.println("Watch Service registered for dir: " + dir.getFileName());
            WatchKey key;
            while ((key = watcher.take())!=null) 
            {
                for (WatchEvent<?> event : key.pollEvents()) {
                     
                	WatchEvent.Kind<?> kind = event.kind();
                    
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    
                    if(kind==ENTRY_CREATE)
                    {
                    	System.out.println("New File Added, file Name " + fileName);
                    	//EmployeeControl control = new EmployeeControl();
                    	//control.autoScheduling();
                    	
                    }
					
                }
                 
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
             
        } catch (IOException ex) {
            System.err.println(ex);
        }
	}

}
