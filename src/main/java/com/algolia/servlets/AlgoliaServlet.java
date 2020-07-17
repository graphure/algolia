package com.algolia.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.algolia.classes.Product;
import com.algolia.search.APIClient;
import com.algolia.search.ApacheAPIClientBuilder;
import com.algolia.search.AsyncIndex;
import com.algolia.search.Index;
import com.algolia.search.exceptions.AlgoliaException;
import com.algolia.search.inputs.query_rules.Condition;
import com.algolia.search.inputs.query_rules.Consequence;
import com.algolia.search.inputs.query_rules.ConsequenceParams;
import com.algolia.search.inputs.query_rules.ConsequenceQueryObject;
import com.algolia.search.inputs.query_rules.Rule;
import com.algolia.search.objects.Edit;
import com.algolia.search.objects.IndexSettings;
import com.algolia.search.objects.Query;

/**
 * 
 * Servlet implementation class SimpleServlet
 * 
 */
public class AlgoliaServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		Indexing indexing=new Indexing();
		out.println("données uploadées");

		String query = request.getParameter("query");

		APIClient client = new ApacheAPIClientBuilder("WZA4OKCTF0", "af24d5770e3860d04f030e79a775de66").build();

		Index<Product> index = client.initIndex("records", Product.class);
		// System.out.println(index.search(new Query(query)));
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"utf-8\" />");
		out.println("<title>Test</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<p>Ceci est une page générée depuis une servlet.</p>");
		try {

			List<Product> products = index.search(new Query(query)).getHits();
			for (int i = 0; i < products.size(); i++) {
				out.println(products.get(i).getName());
				out.println("<br>");
				out.println(products.get(i).getDescription());
				out.println("<br>");
				out.println(products.get(i).getPrice());
				out.println("<br>");
				out.println(products.get(i).getBrand());
				out.println("<br><br>");
			}
			if(products.size()==0) {
				out.println("aucune réponse n'a été trouvée");
			}
		} catch (AlgoliaException e) {
			out.println(e.toString());
			e.printStackTrace();
		}
		out.println("</body>");
		out.println("</html>");
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public String servicesDatatable(@RequestParam(value = "query", defaultValue = "") String query, Model model)
			throws UnsupportedEncodingException, AlgoliaException {
		APIClient client = new ApacheAPIClientBuilder("WZA4OKCTF0", "af24d5770e3860d04f030e79a775de66").build();

		Index<Product> index = client.initIndex("contacts", Product.class);
		// System.out.println(index.search(new Query(query)));
		return index.search(new Query(query)).toString();

	}

}
