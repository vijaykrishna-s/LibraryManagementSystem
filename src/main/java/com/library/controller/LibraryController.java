package com.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.dto.ReturnBookDTO;
import com.library.entity.Book;
import com.library.entity.Transaction;
import com.library.entity.User;
import com.library.entity.WishList;
import com.library.service.BookService;
import com.library.service.UserService;
import com.library.service.WishListService;

@Controller
public class LibraryController {

	@Autowired
	private BookService service;
	@Autowired
    private UserService userService;
	@Autowired
	private WishListService listService;
	
	
	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/bookRegister")
	public String bookRegister() {
		return "bookRegister";
	}

	@GetMapping("/bookList")
	public ModelAndView getAllBooks() {
		List<Book> list = service.getAllBooks();
		ModelAndView m = new ModelAndView();
		m.setViewName("bookList");
		m.addObject("book", list);
		return m;
	}

	@PostMapping("/save")
	public String save(@ModelAttribute Book b,Model model) {
		 service.save(b);
	        model.addAttribute("message", "Book added successfully");
		return "redirect:/bookRegister";
	}

	@RequestMapping("/deleteItem/{id}")
	public String deleteItem(@PathVariable("id") int id,RedirectAttributes attributes) {
		service.deleteById(id);
		attributes.addFlashAttribute("errorMessage","Record Deleted Successfully!");
		return "redirect:/bookList";
	}

	@RequestMapping("/updateItem/{id}")
	public String updateItem(@PathVariable("id") int id, Model model) {
		Book b = service.getBookById(id);
		model.addAttribute("book", b);
		return "bookEdit";

	}

	@GetMapping("/search")
	public String searchBooks(@RequestParam("bname") String bookName, Model model) {
		List<Book> books = service.findBooksByName(bookName);
		model.addAttribute("books", books);
		return "searchResults";
	}
	
	
	@GetMapping("/userbooklist")
	public ModelAndView getUserAllBooks() {
		List<Book> list = service.getAllBooks();
		ModelAndView m = new ModelAndView();
		m.setViewName("UserBookList");
		m.addObject("book", list);
		return m;
	}
	
	@GetMapping("/userbooksearch")
	public String userSearchBooks(@RequestParam("bname") String bookName, Model model) {
		List<Book> books = service.findBooksByName(bookName);
		model.addAttribute("books", books);
		return "userSearchResults";
	}
	
	
	@GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "userList";
    }
	
	@GetMapping("/userwishlist")
    public String userWishlist(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<WishList> wishlist = listService.getUserWishlist(email);
        model.addAttribute("wishlist", wishlist);
        return "userwishlist";
    }

    @PostMapping("/userwishlist")
    public String addToWishlist(@RequestParam String name, @RequestParam String author, @RequestParam int year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        WishList wishlist = new WishList(name, author, year);
        listService.addToWishlist(email, wishlist);
        return "redirect:/userwishlist";
    }

    @GetMapping("/adminwishlist")
    public String adminWishlist(Model model) {
        List<WishList> allWishlists = listService.getAllWishlists();
        model.addAttribute("wishlists", allWishlists);
        return "adminWishlist";
    }
    
    @RequestMapping("/remove/{id}")
	public String removeBook(@PathVariable("id")int id) {
		listService.removeBookFromList(id);
		return "redirect:/userwishlist";
	}
    
    @GetMapping("/takeBook")
    public String showTakeBookPage(Model model) {
        return "takeBook";
    }

    @PostMapping("/takeBook")
    public String takeBook(@RequestParam String bname, @RequestParam String email, Model model) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        
        if (!loggedInEmail.equals(email)) {
            model.addAttribute("message", "Failed!! The email does not match");
            return "takeBook";
        }

        
        try {
            service.takeBook(email, bname);
            model.addAttribute("message", "Book taken successfully");
        } catch (RuntimeException e) {
            model.addAttribute("message", "Failed!!Book name is invalid!!");
        }
        return "takeBook";
    }
    
    
    @GetMapping("/returnBookForm")
    public String showReturnBookForm(Model model) {
        model.addAttribute("returnBook", new ReturnBookDTO());
        return "returnBook";
    }

    @PostMapping("/returnBook")
    public String returnBook(@ModelAttribute ReturnBookDTO returnBookDto, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        if (!loggedInEmail.equals(returnBookDto.getEmail())) {
            model.addAttribute("message", "Failed!! The email does not match");
            return "returnBook";
        }

        try {
            if (service.isBookAlreadyReturned(returnBookDto.getEmail(), returnBookDto.getBname())) {
                model.addAttribute("message", "Book has already been returned");
            } else {
                service.returnBook(returnBookDto.getEmail(), returnBookDto.getBname());
                model.addAttribute("message", "Book returned successfully");
            }
        } catch (RuntimeException e) {
            model.addAttribute("message", "Book name invalid");
        }
        return "returnBook";
    }


    @GetMapping("/availableCopiesForm")
    public String showAvailableCopiesPage() {
        return "availableCopies";
    }

    @GetMapping("/availableCopies")
    public String getAvailableCopies(@RequestParam String bname, Model model) {
        try {
            int availableCopies = service.getAvailableCopies(bname);
            model.addAttribute("availableCopies", availableCopies);
        } catch (RuntimeException e) {
            model.addAttribute("message", "Book name invalid");
        }
        return "availableCopies";
    }

    @GetMapping("/currentTransactionsForm")
    public String showCurrentTransactionsPage() {
        return "currentTransactions";
    }

    @GetMapping("/currentTransactions")
    public String getCurrentTransactions(@RequestParam String bname, Model model) {
        try {
            List<Transaction> transactions = service.getCurrentTransactions(bname);
            if (transactions.isEmpty()) {
                model.addAttribute("message", "No transactions found for the given book ID.");
            } else {
                model.addAttribute("transactions", transactions);
            }
        } catch (RuntimeException e) {
            model.addAttribute("message", "Invalid book ID: " + e.getMessage());
        }
        return "currentTransactions";
    }
    @GetMapping("/alltransactions")
    public String listTransactions(Model model) {
        model.addAttribute("transactions", service.getAllTransaction());
        return "allTransactions"; 
    }

}
