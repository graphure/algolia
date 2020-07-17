package com.algolia.servlets;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.algolia.classes.Product;
import com.algolia.search.APIClient;
import com.algolia.search.ApacheAPIClientBuilder;
import com.algolia.search.Index;
import com.algolia.search.exceptions.AlgoliaException;
import com.algolia.search.inputs.query_rules.Condition;
import com.algolia.search.inputs.query_rules.Consequence;
import com.algolia.search.inputs.query_rules.ConsequenceParams;
import com.algolia.search.inputs.query_rules.ConsequenceQueryObject;
import com.algolia.search.inputs.query_rules.Rule;
import com.algolia.search.objects.Edit;
import com.algolia.search.objects.IndexSettings;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Indexing {

	public static void main(String[] args) throws IOException, AlgoliaException {
		List<Product> products = new ObjectMapper().readValue(new FileReader("records.json"),
				new TypeReference<List<Product>>() {
				});

		//uploading data
		APIClient client = new ApacheAPIClientBuilder("WZA4OKCTF0", "af24d5770e3860d04f030e79a775de66").build();
		Index<Product> index = client.initIndex("demo_ecommerce", Product.class);
		index.addObjects(products);
		index.setSettings(new IndexSettings().setSearchableAttributes(Arrays.asList("name", "brand", "price")));

		//rules
		Condition condition = new Condition().setContext("cellphone maroon").setAnchoring("contains");
		Consequence consequence = new Consequence();
		List<Edit> edits = Arrays.asList(new Edit().setInsert("samsung"));

		ConsequenceParams params = new ConsequenceParams();
		params.setRestrictSearchableAttributes(Arrays.asList("name"));
		params.setQuery(new ConsequenceQueryObject().setEdits(edits));
		consequence.setParams(params);

		Rule rule = new Rule().setObjectID("article").setCondition(condition).setConsequence(consequence);

		index.saveRule(rule.getObjectID(), rule);
	}

}