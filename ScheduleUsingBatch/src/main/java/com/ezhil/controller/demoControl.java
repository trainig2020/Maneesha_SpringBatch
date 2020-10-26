/*
 * package com.ezhil.controller;
 * 
 * import java.io.IOException; import java.nio.file.FileSystems; import
 * java.nio.file.Path; import java.nio.file.Paths; import
 * java.nio.file.WatchEvent; import java.nio.file.WatchKey; import
 * java.nio.file.WatchService; import java.util.HashMap; import java.util.Map;
 * 
 * import org.springframework.batch.core.Job; import
 * org.springframework.batch.core.JobExecution; import
 * org.springframework.batch.core.JobParameter; import
 * org.springframework.batch.core.JobParameters; import
 * org.springframework.batch.core.JobParametersBuilder; import
 * org.springframework.batch.core.launch.JobLauncher; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController; import
 * org.springframework.web.servlet.ModelAndView;
 * 
 * import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE; import
 * static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE; import static
 * java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
 * 
 * @RestController public class demoControl {
 * 
 * @Autowired private JobLauncher jobLauncher;
 * 
 * @Autowired Job job;
 * 
 * @RequestMapping("/") public ModelAndView homePage() { return new
 * ModelAndView("home"); }
 * 
 * @RequestMapping("/back") public ModelAndView backPage() { return new
 * ModelAndView("home"); }
 * 
 * @RequestMapping("/auto") public ModelAndView autoScheduling() throws
 * Exception { ModelAndView modelAndView = new ModelAndView("success");
 * Map<String, JobParameter> maps = new HashMap<>(); maps.put("time", new
 * JobParameter(System.currentTimeMillis())); JobParameters parameters = new
 * JobParameters(maps); JobExecution jobExecution = jobLauncher.run(job,
 * parameters); System.out.println("JobExecution: " + jobExecution.getStatus());
 * 
 * try { WatchService watcher = FileSystems.getDefault().newWatchService(); Path
 * dir = Paths.get("C:/Users/EZHILARASI/Documents/CSV"); dir.register(watcher,
 * ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
 * System.out.println("Watch Service registered for dir: " + dir.getFileName());
 * WatchKey key; while ((key = watcher.take()) != null) { for (WatchEvent<?>
 * event : key.pollEvents()) {
 * 
 * WatchEvent.Kind<?> kind = event.kind();
 * 
 * @SuppressWarnings("unchecked") WatchEvent<Path> ev = (WatchEvent<Path>)
 * event; Path fileName = ev.context();
 * 
 * if (kind == ENTRY_CREATE) { System.out.println("New File Added, file Name " +
 * fileName); JobParameters jobParameters = new JobParametersBuilder()
 * .addLong("time2", System.currentTimeMillis()).toJobParameters(); JobExecution
 * jobExecutions = jobLauncher.run(job, jobParameters);
 * System.out.println("JobExecution: " + jobExecutions.getStatus().toString());
 * }
 * 
 * }
 * 
 * }
 * 
 * } catch (IOException ex) { System.err.println(ex); }
 * 
 * return modelAndView; }
 * 
 * }
 */