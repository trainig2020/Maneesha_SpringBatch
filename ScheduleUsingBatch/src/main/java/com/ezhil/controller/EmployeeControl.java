package com.ezhil.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class EmployeeControl {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	Job job;

	@RequestMapping("/")
	public ModelAndView homePage() {
		return new ModelAndView("home");
	}
	@RequestMapping("/back")
	public ModelAndView backPage() {
		return new ModelAndView("home");
	}

	@RequestMapping("/auto")
	
	public ModelAndView autoScheduling() throws Exception {
		ModelAndView modelAndView = new ModelAndView("success");
		jobLauncher.run(job, new JobParametersBuilder().addLong("uniqueness", System.nanoTime()).toJobParameters());
		

		return modelAndView;
	}

	/*@RequestMapping("/manual")
	public ModelAndView manualSchedule() throws Exception {
		ModelAndView modelAndView = new ModelAndView("home");
		List<String> csvfiles = new ArrayList<>();
		File folder = new File("C:\\Users\\EZHILARASI\\Documents\\CSV");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

			csvfiles.add(file.getName());
		}
		modelAndView.addObject("checkManual", "manual");
		modelAndView.addObject("file", csvfiles);

		return modelAndView;
	}

	@RequestMapping("/manualmode")
	public ModelAndView manualmodeSch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("home");
		String[] fileNames = request.getParameterValues("csvfile");
		File folder = new File("C:\\Users\\EZHILARASI\\Documents\\CSV");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			for (String fileName : fileNames) {

				if (fileName.equals(file.getName())) {
					JobParametersBuilder jpBuilder = new   JobParametersBuilder().addString("inputfile", fileName);
					jobLauncher.run(job, jpBuilder.toJobParameters());
				}

			}
		}
		return modelAndView;
	}*/

}
