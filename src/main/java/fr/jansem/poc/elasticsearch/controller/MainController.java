package fr.jansem.poc.elasticsearch.controller;

import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.jansem.poc.elasticsearch.model.Customer;
import fr.jansem.poc.elasticsearch.repository.CustomerRepository;

@Controller
public class MainController {

	@Autowired
	private CustomerRepository repository;

	@RequestMapping("/")
	public String displayHome() {
		return "index";
	}

	@RequestMapping("/add")
	public ModelAndView displayAddForm() {
		ModelAndView mav = new ModelAndView("addForm");
		mav.addObject("name", "");
		mav.addObject("firstName", "");
		return mav;
	}

	/**
	 * Indexe un nouveau customer
	 * @param name
	 * @param firstName
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addCustomer(@ModelAttribute("name") String name,
			@ModelAttribute("firstName") String firstName) {
		ModelAndView mav = new ModelAndView("index");
		this.repository.save(new Customer(firstName, name));
		return mav;
	}

	/**
	 * Retourne l'ensemble des documents de type {@link Customer}
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView displayList() {
		ModelAndView mav = new ModelAndView("list");
		mav.addObject("list", this.repository.findAll());
		return mav;
	}

	@RequestMapping("/search")
	public ModelAndView displaySearchForm() {
		ModelAndView mav = new ModelAndView("search");
		mav.addObject("text", "");
		return mav;
	}

	/**
	 * Effectue une recherche sur l'ensemble des champs du document
	 * @param text
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView searchCustomer(@ModelAttribute("text") String text) {
		ModelAndView mav = new ModelAndView("searchResults");
		QueryBuilder queryBuilder = wildcardQuery("_all", "*"+text+"*");
		mav.addObject("list", this.repository.search(queryBuilder));
		return mav;
	}

}
