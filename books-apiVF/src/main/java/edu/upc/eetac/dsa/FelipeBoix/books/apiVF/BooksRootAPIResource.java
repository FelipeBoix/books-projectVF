package edu.upc.eetac.dsa.FelipeBoix.books.apiVF;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import edu.upc.eetac.dsa.FelipeBoix.books.apiVF.model.BooksRootAPI;


@Path("/")
public class BooksRootAPIResource {
	@GET
	public BooksRootAPI getRootAPI() {
		BooksRootAPI api = new BooksRootAPI();
		return api;
	}
}