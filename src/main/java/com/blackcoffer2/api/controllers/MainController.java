package com.blackcoffer2.api.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackcoffer2.api.Entitys.Authors;
import com.blackcoffer2.api.Entitys.Books;
import com.blackcoffer2.api.Entitys.User;
import com.blackcoffer2.api.Repositorys.AuthorRepository;
import com.blackcoffer2.api.Repositorys.BooksRepository;
import com.blackcoffer2.api.Repositorys.UserRepository;
import com.blackcoffer2.api.configuration.UserCustomConfigService;
import com.blackcoffer2.api.jwtHelper.JwtUtil;

@RestController
public class MainController{
	
	@Autowired
	private UserCustomConfigService userCustomConfigService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private BooksRepository booksRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorRepository authorRepository;
	
	User loginUser;
	@ModelAttribute
	public void setUser(Principal p) {
		if(p!=null)
			this.loginUser=userRepository.findByName(p.getName()).get();
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> generateToken(@RequestBody User user) throws Exception{
		try {
			System.out.println("tyr loggin : "+user.getName());
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
			
		}catch(UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("### Invalid username!!");
		}catch(BadCredentialsException e){
			e.printStackTrace();
			throw new Exception("### Bad credentials!!");
		}
		//generate the token
		UserDetails userDetails= this.userCustomConfigService.loadUserByUsername(user.getName());
		String token=this.jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok("Token is : "+token);
	}
	
	//save the book details
	@PostMapping("/addNewBook")
	public String addTheNewBook(@RequestBody Books books,Principal p) {
		User user = userRepository.findByName(p.getName()).get();
		books.setAuthor(authorRepository.findById(books.getAuthorId()).get());
		user.getBooks().add(books);
		userRepository.save(user);
		return "book is save successfuly";
	}
	
	//view all users
	@GetMapping("/allAuthors")
	public List<Authors> getAllAuthorDetails() {
		List<Authors> authors=authorRepository.findAll();
		return authors;
	}
	
	//delete the book
	@GetMapping("/deleteBook/{id}")
	public String deleteTheBook(@PathVariable int id) {
		Books book=booksRepository.findById(id).get();
		//firstly book remove from the set
		loginUser.getBooks().remove(book);
		//then delete the book record
		booksRepository.delete(book);
		return book.getName()+" book is deleted.";
	}
	
	//update the book
	@PostMapping("updateBook/{id}")
	public String updateTheBook(@RequestBody Books bookData,@PathVariable int id){
		Books book=booksRepository.findById(id).get();
		book.setName(bookData.getName());
		book.setDescription(bookData.getDescription());
		book.setPrice(bookData.getPrice());
		booksRepository.save(book);
		return "book updated successfully";
	}
	
	
	//data get from books table and Author table
	@GetMapping("/getBooks")
	public List<Books> getAllBooks(Principal p) {  
		List<Books> books=this.booksRepository.findAll();
		return books;
	}
}
