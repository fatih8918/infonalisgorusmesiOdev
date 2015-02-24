package com.egitim.beans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.apache.logging.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import com.egitim.model.Student;
import com.egitim.service.StudentService;

@Named("dataTable")
@SessionScoped
public class DataTableBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1596272662226363387L;
	
	@Inject
	private transient Logger log;

	
	@Inject
	private StudentService studentService;
	
	private List<Student> students;
	
	private Student selectedStudent;
	
	@NotNull
    @Pattern(regexp = "([a-zA-Z]+)")
    	private String name;

	@NotNull
	private String surname;
	
	@Past
	private Date birthdate;
	
	SimpleDateFormat sf = new SimpleDateFormat("d.M.yyyy");
	
	@PostConstruct
	public void init() {
		studentService.initStudents();
		students  = studentService.getStudents();
		log.info("Data Table initialized.");
	}
	
	public String addStudent() {
		studentService.addStudent(new Student(name,surname,birthdate));
		students  = studentService.getStudents();
		return "success";
	}
	
	public String removeStudent(Student std) {
		studentService.removeStudent(std);
		students  = studentService.getStudents();
		return "success";
	}
	
	public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Student Selected", ((Student) event.getObject()).getSurname());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

	public void handleFileUpload(FileUploadEvent event) {
		log.info("File name:"+event.getFile().getFileName());
		UploadedFile file = event.getFile();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputstream()));
			while ((line = reader.readLine()) != null) {
				String[] studentData = line.split(";");
				studentService.addStudent(new Student(studentData[0],studentData[1],sf.parse(studentData[2])));
	        }
		} catch (Exception e) {
			log.error("Dosya okuma hatası.",e);
		} 
		students  = studentService.getStudents();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public StudentService getStudentService() {
		return studentService;
	}

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
	}

}
