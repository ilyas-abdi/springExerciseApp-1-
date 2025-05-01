//ID:C1220041
//name:naima muhumed Abubakar

package com.naima.springExerciseApp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringExerciseAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringExerciseAppApplication.class, args);
	}

}
@RestController
class GreetingController {

	@GetMapping("/")
	public String home() {
		return "Welcome to Spring Boot!";
	}

	@GetMapping("/greet/{name}")
	public String greet(@PathVariable String name) {
		return "Hello, " + name + "!";
	}

	@GetMapping("/success")
	public ResponseEntity<String> success() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Custom-Header", "SuccessHeader");
		return new ResponseEntity<>("Success with custom header", headers, HttpStatus.OK);
	}

	@GetMapping("/not-found")
	public ResponseEntity<String> notFound() {
		return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
	}

	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestBody String body) {
		URI location = URI.create("/resource/123");
		return ResponseEntity.created(location).body("Resource created");
	}
}

@Data
@AllArgsConstructor

@NoArgsConstructor
class Staff {
	private Long id;
	private String name;
	private String position;
}

@Service
class StaffService {
	private final Map<Long, Staff> staffMap = new HashMap<>();
	private Long currentId = 1L;

	public List<Staff> getAll() {
		return new ArrayList<>(staffMap.values());
	}

	public Staff getById(Long id) {
		return staffMap.get(id);
	}

	public Staff create(Staff staff) {
		staff.setId(currentId++);
		staffMap.put(staff.getId(), staff);
		return staff;
	}

	public Staff update(Long id, Staff updated) {
		if (staffMap.containsKey(id)) {
			updated.setId(id);
			staffMap.put(id, updated);
			return updated;
		}
		return null;
	}

	public boolean delete(Long id) {
		return staffMap.remove(id) != null;
	}
}

@RestController
@RequestMapping("/staffs")
class StaffController {

	private final StaffService staffService;

	public StaffController(StaffService staffService) {
		this.staffService = staffService;
	}

	@GetMapping
	public List<Staff> getAll() {
		return staffService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Staff> getById(@PathVariable Long id) {
		Staff staff = staffService.getById(id);
		return staff != null ? ResponseEntity.ok(staff) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Staff> create(@RequestBody Staff staff) {
		Staff created = staffService.create(staff);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Staff> update(@PathVariable Long id, @RequestBody Staff staff) {
		Staff updated = staffService.update(id, staff);
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return staffService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}
