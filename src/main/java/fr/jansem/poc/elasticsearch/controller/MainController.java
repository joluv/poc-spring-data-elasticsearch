package fr.jansem.poc.elasticsearch.controller;

import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.jansem.poc.elasticsearch.model.Customer;
import fr.jansem.poc.elasticsearch.repository.CustomerRepository;

@Controller
public class MainController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private CustomerRepository repository;

	@RequestMapping("/")
	public String displayHome() {
		LOGGER.debug("Affichage page d'accueil");
		return "index";
	}

	@RequestMapping("/add")
	public ModelAndView displayAddForm() {
		LOGGER.debug("Affichage page d'ajout à l'index");
		ModelAndView mav = new ModelAndView("addForm");
		mav.addObject("name", "");
		mav.addObject("firstName", "");
		return mav;
	}

	/**
	 * Indexe un nouveau customer
	 * 
	 * @param name
	 * @param firstName
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addCustomer(@ModelAttribute("name") String name,
			@ModelAttribute("firstName") String firstName) {
		LOGGER.debug("Ajout du customer {} {} à l'index", firstName, name);
		ModelAndView mav = new ModelAndView("index");
		this.repository.save(new Customer(firstName, name));
		LOGGER.debug("Ajout à l'index effectué");
		return mav;
	}

	/**
	 * Retourne l'ensemble des documents de type {@link Customer}
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView displayList() {
		LOGGER.debug("Affichage page de la liste des customers");
		ModelAndView mav = new ModelAndView("list");
		mav.addObject("list", this.repository.findAll());
		return mav;
	}

	@RequestMapping("/search")
	public ModelAndView displaySearchForm() {
		LOGGER.debug("Affichage page de recherche de customers");
		ModelAndView mav = new ModelAndView("search");
		mav.addObject("text", "");
		return mav;
	}

	/**
	 * Effectue une recherche sur l'ensemble des champs du document
	 * 
	 * @param text
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView searchCustomer(@ModelAttribute("text") String text) {
		LOGGER.debug("Recherche de customer sur le chaine {}", text);
		ModelAndView mav = new ModelAndView("searchResults");
		QueryBuilder queryBuilder = wildcardQuery("_all", "*" + text + "*");
		FacetedPageImpl<Customer> results = (FacetedPageImpl<Customer>) this.repository.search(queryBuilder);
		mav.addObject("list", results);
		LOGGER.debug("Recherche effectuée, {} résultats", results.getNumberOfElements());
		return mav;
	}

}
